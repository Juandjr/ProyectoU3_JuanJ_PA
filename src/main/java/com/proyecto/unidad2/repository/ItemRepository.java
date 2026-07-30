package com.proyecto.unidad2.repository;

import com.proyecto.unidad2.model.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByPlataformaIgnoreCase(String plataforma);

    @Query("select avg(i.precio) from Item i")
    Double calcularPrecioPromedio();
}
