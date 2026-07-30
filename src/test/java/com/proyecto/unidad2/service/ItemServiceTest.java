package com.proyecto.unidad2.service;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    private Item item;

    @BeforeEach
    void setUp() {
        item = Item.builder().titulo("Zelda").plataforma("Switch").precio(59.99).build();
    }

    @Test
    void listar_debeRetornarItemsDelRepositorio() {
        when(repository.findAll()).thenReturn(List.of(item));

        var resultado = service.listar();

        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void crear_debePersistirCuandoCumpleReglas() {
        when(repository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.crear(item);

        assertThat(resultado.getTitulo()).isEqualTo("Zelda");
        verify(repository).save(item);
    }

    @Test
    void crear_debeFallarSiElPrecioNoEsValido() {
        item.setPrecio(0.0);

        assertThatThrownBy(() -> service.crear(item))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mayor a 0");

        verifyNoInteractions(repository);
    }

    @Test
    void actualizar_debeModificarCamposExistentes() {
        when(repository.findById(1L)).thenReturn(Optional.of(Item.builder().id(1L).titulo("Old").plataforma("PC").precio(10.0).build()));
        when(repository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.actualizar(1L, item);

        assertThat(resultado.getTitulo()).isEqualTo("Zelda");
        verify(repository).findById(1L);
        verify(repository).save(any(Item.class));
    }

    @Test
    void aplicarDescuento_debeReducirElPrecioSegunElPorcentaje() {
        item.setPrecio(100.0);
        var resultado = service.aplicarDescuento(item, 20.0);
        assertThat(resultado.getPrecio()).isEqualTo(80.0);
    }

    @Test
    void aplicarDescuento_debeLanzarExcepcionSiPorcentajeInvalido() {
        item.setPrecio(100.0);
        assertThatThrownBy(() -> service.aplicarDescuento(item, 150.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("entre 0 y 100");
    }
}
