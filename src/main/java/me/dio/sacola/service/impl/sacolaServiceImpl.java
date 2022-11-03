package me.dio.sacola.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.formaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.itemRepository;
import me.dio.sacola.repository.produtoRepository;
import me.dio.sacola.repository.sacolaRepository;
import me.dio.sacola.resource.dto.itemDto;
import me.dio.sacola.service.sacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class sacolaServiceImpl implements sacolaService {
    private final sacolaRepository sacolaRepository;
    private final produtoRepository produtoRepository;
    private final itemRepository itemRepository;

    @Override
    public Item incluirItemNaSacola(itemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaId());

        if (sacola.isFechada()) {
            throw new RuntimeException("Esta sacola está fechada.");
        }
        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItems();
        if (itensDaSacola.isEmpty()) {
            itensDaSacola.add(itemParaSerInserido);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();
            if (restauranteAtual.equals(restauranteDoItemParaAdicionar)) {
                itensDaSacola.add(itemParaSerInserido);
            } else {
                throw new RuntimeException("Não é possível adicionar produtos de restaurantes diferentes. Feche a sacola ou esvazie.");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();

        for (Item itemDaSacola: itensDaSacola) {
            double valorTotalItem =
                itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuantidade();
            valorDosItens.add(valorTotalItem);
        }

        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return itemRepository.save(itemParaSerInserido);
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);
        if (sacola.getItems().isEmpty()) {
            throw new RuntimeException("Inclua itens na sacola!");
        }
        /*if (numeroFormaPagamento == 0) {
        sacola.setformaPagamento(formaPagamento.DINHEIRO);
        } else {
        sacola.setformaPagamento(formaPagamento.MAQUINETA)
        }
         */
        formaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? me.dio.sacola.enumeration.formaPagamento.DINHEIRO : me.dio.sacola.enumeration.formaPagamento.MAQUINETA;
        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}