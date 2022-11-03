package me.dio.sacola.repository;

import me.dio.sacola.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface produtoRepository extends JpaRepository<Produto, Long> {
}
