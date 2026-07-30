package com.proyecto.unidad2.repository;

import com.proyecto.unidad2.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Test
    void findByPlataformaIgnoreCase_debeFiltrarPorPlataforma() {
        repository.saveAll(List.of(
                Item.builder().titulo("Halo").plataforma("Xbox").precio(50.0).build(),
                Item.builder().titulo("Zelda").plataforma("Switch").precio(60.0).build()
        ));

        var resultado = repository.findByPlataformaIgnoreCase("xbox");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Halo");
    }

    @Test
    void calcularPrecioPromedio_debeRetornarPromedioCorrecto() {
        repository.saveAll(List.of(
                Item.builder().titulo("A").plataforma("PC").precio(10.0).build(),
                Item.builder().titulo("B").plataforma("PC").precio(30.0).build()
        ));

        assertThat(repository.calcularPrecioPromedio()).isEqualTo(20.0);
    }
}
