package me.dio.sacola.resource;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resource.dto.itemDto;
import me.dio.sacola.service.sacolaService;
import org.springframework.web.bind.annotation.*;

@Api(value = "/ifood-devweek/sacolas")
@RestController
@RequestMapping("/ifood-devweek/sacolas")
@RequiredArgsConstructor
public class sacolaResource {
    private final sacolaService sacolaService;

    @PostMapping
    public Item incluirItemNaSacola(@RequestBody itemDto itemDto) {
        return sacolaService.incluirItemNaSacola(itemDto);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable("id") Long id) {
        return sacolaService.verSacola(id);
    }

    @PatchMapping("/fecharSacola/{sacolaId}")
    public Sacola fecharSacola(@PathVariable("sacolaId") Long sacolaId, @RequestParam("formaPagamanto") int formaPagamento) {
        return sacolaService.fecharSacola(sacolaId, formaPagamento);
    }
}
