package com.yesi.springcloud.msvc.puntosventa.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yesi.springcloud.msvc.puntosventa.model.PuntoVenta;
import com.yesi.springcloud.msvc.puntosventa.services.PuntoVentaCacheServiceImpl;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaCacheServiceImpl puntoVentaService;

    public PuntoVentaController(PuntoVentaCacheServiceImpl puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
    }

    @GetMapping("/devolver-todos")
    public ResponseEntity<Collection<PuntoVenta>> devuelveTodosPuntosVentas() {
        return ResponseEntity.ok(puntoVentaService.obtenerTodos());
    }

    @PostMapping("/guardar-nuevo")
    public ResponseEntity<PuntoVenta> guardarNuevoPuntoVenta(@RequestBody PuntoVenta nuevoPuntoVenta) {
        PuntoVenta puntoVentaGuardado = puntoVentaService.guardarUnPuntoVenta(nuevoPuntoVenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(puntoVentaGuardado);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PuntoVenta> actualizarDePuntoVenta(@PathVariable Integer id,
            @RequestBody PuntoVenta actPuntoVenta) {
        PuntoVenta puntoParaActualizar = new PuntoVenta(id, actPuntoVenta.name());

        return puntoVentaService.actualizarUnPuntoVenta(puntoParaActualizar)
                .map(puntoGuardado -> ResponseEntity.ok(puntoGuardado))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarPuntoVenta(@PathVariable Integer id) {
        Optional<PuntoVenta> puntoOptional = puntoVentaService.eliminarUnPuntoVenta(id);

        Map<String, String> response = new HashMap<>();

        if (puntoOptional.isPresent()) {
            response.put("mensaje", "Punto de venta con ID " + id + " eliminado con éxito.");
            return ResponseEntity.ok(response); 
        } else {
            response.put("error", "Punto de venta con ID " + id + " no fue encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); 
        }
    }

}
