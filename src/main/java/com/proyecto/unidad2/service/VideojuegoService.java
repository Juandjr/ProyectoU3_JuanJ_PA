package com.proyecto.unidad2.service;

import com.proyecto.unidad2.model.Item;
import com.proyecto.unidad2.repository.ItemRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class VideojuegoService {

    private final ItemRepository repository;
    private final Timer dbLatencyTimer;

    public VideojuegoService(ItemRepository repository, MeterRegistry registry) {
        this.repository = repository;
        // Registro del medidor de latencia síncrono para PostgreSQL
        this.dbLatencyTimer = Timer.builder("tienda.db.latency")
                .description("Latencia de consultas a PostgreSQL")
                .register(registry);
    }

    public List<Item> obtenerCatalogo() {
        long startTime = System.nanoTime();
        try {
            return repository.findAll();
        } finally {
            dbLatencyTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        }
    }

    public Item guardarJuego(Item juego) {
        return repository.save(juego);
    }

    // Flujo masivo bloqueante para simular la carga y medir retención del hilo
    public Stream<Item> obtenerFlujoMasivoBloqueante() {
        return Stream.iterate(1, i -> i + 1)
                .limit(500000)
                .map(i -> Item.builder()
                        .id((long) i)
                        .titulo("Juego Pesado " + i)
                        .plataforma("Multiplataforma")
                        .precio(29.99)
                        .build());
    }
}