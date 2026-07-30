package com.proyecto.unidad2.controller;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Item>> listar(@RequestParam(required = false) String plataforma) {
        return ResponseEntity.ok(plataforma == null ? service.listar() : service.listarPorPlataforma(plataforma));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Item> crear(@Valid @RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> actualizar(@PathVariable Long id, @Valid @RequestBody Item item) {
        return ResponseEntity.ok(service.actualizar(id, item));
    }
}
