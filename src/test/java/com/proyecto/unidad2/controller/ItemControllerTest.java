package com.proyecto.unidad2.controller;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService service;

    @Test
    void listar_debeRetornar200YJson() throws Exception {
        when(service.listar()).thenReturn(List.of(Item.builder().id(1L).titulo("FIFA").plataforma("PS5").precio(59.99).build()));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("FIFA"));
    }

    @Test
    void crear_debeRetornar201() throws Exception {
        when(service.crear(any(Item.class))).thenAnswer(invocation -> {
            Item entrada = invocation.getArgument(0);
            entrada.setId(7L);
            return entrada;
        });

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Mario","plataforma":"Switch","precio":49.99}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7L));
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        when(service.actualizar(any(), any(Item.class))).thenAnswer(invocation -> {
            Item entrada = invocation.getArgument(1);
            entrada.setId(1L);
            return entrada;
        });

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Mario","plataforma":"Switch","precio":49.99}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
