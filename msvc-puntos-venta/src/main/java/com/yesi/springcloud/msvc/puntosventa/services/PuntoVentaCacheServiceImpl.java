package com.yesi.springcloud.msvc.puntosventa.services;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import com.yesi.springcloud.msvc.puntosventa.model.PuntoVenta;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PuntoVentaCacheServiceImpl {
    private final Map<Integer, PuntoVenta> cachePuntosDeVenta = new ConcurrentHashMap<>();

    @PostConstruct
    private void cargarDatosIniciales() {
        log.info("Se agregan datos a la memoria cache");
        cachePuntosDeVenta.put(1, new PuntoVenta(1, "CABA"));
        cachePuntosDeVenta.put(2, new PuntoVenta(2, "GBA_1"));
        cachePuntosDeVenta.put(3, new PuntoVenta(3, "GBA_2"));
        cachePuntosDeVenta.put(4, new PuntoVenta(4, "Santa Fe"));
        cachePuntosDeVenta.put(5, new PuntoVenta(5, "Córdoba"));
        cachePuntosDeVenta.put(6, new PuntoVenta(6, "Misiones"));
        cachePuntosDeVenta.put(7, new PuntoVenta(7, "Salta"));
        cachePuntosDeVenta.put(8, new PuntoVenta(8, "Chubut"));
        cachePuntosDeVenta.put(9, new PuntoVenta(9, "Santa Cruz"));
        cachePuntosDeVenta.put(10, new PuntoVenta(10, "Catamarca"));

    }

    public Collection<PuntoVenta> obtenerTodos() {
        return cachePuntosDeVenta.values();
    }

    public PuntoVenta guardarUnPuntoVenta(PuntoVenta nuevoPuntoVenta) {
        log.info("Guardando nuevo punto de venta con ID: {}", nuevoPuntoVenta.id());
        cachePuntosDeVenta.put(nuevoPuntoVenta.id(), nuevoPuntoVenta);
        return nuevoPuntoVenta;
    }

    public Optional<PuntoVenta> actualizarUnPuntoVenta(PuntoVenta cambioPuntoVenta) {
        log.info("Actualizando el siguiente punto de venta con ID: {}", cambioPuntoVenta.id());
        if (cachePuntosDeVenta.containsKey(cambioPuntoVenta.id())) {
            cachePuntosDeVenta.put(cambioPuntoVenta.id(), cambioPuntoVenta);
            return Optional.of(cambioPuntoVenta);
        }
        return Optional.empty();
    }
}
