package com.proyecto.unidad2.controller;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository repository;

    @BeforeEach
    void limpiarDatos() {
        repository.deleteAll();
    }

    @Test
    void crear_y_listar_debenFuncionarContraLaBaseDeDatos() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Halo","plataforma":"Xbox","precio":39.99}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Halo"));

        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void actualizar_debeReflejarCambiosPersistidos() throws Exception {
        Item guardado = repository.save(Item.builder().titulo("Old").plataforma("PC").precio(10.0).build());

        mockMvc.perform(put("/items/" + guardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"New","plataforma":"PC","precio":15.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("New"));
    }
}
