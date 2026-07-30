package com.proyecto.unidad2.service;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Item> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Item> listarPorPlataforma(String plataforma) {
        return repository.findByPlataformaIgnoreCase(plataforma);
    }

    @Transactional(readOnly = true)
    public Double obtenerPrecioPromedio() {
        return repository.calcularPrecioPromedio();
    }

    public Item crear(Item item) {
        validarReglas(item);
        return repository.save(item);
    }

    public Item actualizar(Long id, Item item) {
        validarReglas(item);
        Item actual = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item con ID " + id + " no encontrado"));
        actual.setTitulo(item.getTitulo());
        actual.setPlataforma(item.getPlataforma());
        actual.setPrecio(item.getPrecio());
        return repository.save(actual);
    }

    @Transactional(readOnly = true)
    public Item obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item con ID " + id + " no encontrado"));
    }

    public Item aplicarDescuento(Item item, Double porcentaje) {
        if (porcentaje == null || porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        double nuevoPrecio = Math.round(item.getPrecio() * (1 - porcentaje / 100.0) * 100.0) / 100.0;
        item.setPrecio(nuevoPrecio);
        return item;
    }

    private void validarReglas(Item item) {
        if (item.getPrecio() == null || item.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (item.getTitulo() == null || item.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio");
        }
        if (item.getPlataforma() == null || item.getPlataforma().isBlank()) {
            throw new IllegalArgumentException("La plataforma es obligatoria");
        }
    }
}
