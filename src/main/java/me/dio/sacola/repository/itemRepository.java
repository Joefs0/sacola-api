package me.dio.sacola.repository;

import me.dio.sacola.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface itemRepository extends JpaRepository<Item, Long> {
}
