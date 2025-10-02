package com.yesi.springcloud.msvc.puntosventa.controller;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yesi.springcloud.msvc.puntosventa.model.PuntoVenta;
import com.yesi.springcloud.msvc.puntosventa.services.PuntoVentaCacheServiceImpl;

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

     private final PuntoVentaCacheServiceImpl puntoVentaService;

    public PuntoVentaController(PuntoVentaCacheServiceImpl puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
    }

     @GetMapping
    public ResponseEntity<Collection<PuntoVenta>> devuelveTodosPuntosVentas() {
        return ResponseEntity.ok(puntoVentaService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<PuntoVenta> guardarNuevoPuntoVenta(@RequestBody PuntoVenta nuevoPuntoVenta) {
        PuntoVenta puntoVentaGuardado = puntoVentaService.guardarUnPuntoVenta(nuevoPuntoVenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(puntoVentaGuardado);
    }
}
