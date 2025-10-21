package com.yesi.springcloud.msvc.costos.controller;

import com.yesi.springcloud.msvc.costos.model.Costo;
import com.yesi.springcloud.msvc.costos.services.CostoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/costos")
public class CostoController {

    private final CostoService costoService;

    public CostoController(CostoService costoService) {
        this.costoService = costoService;
    }

    // Endpoint para (1) Cargar un nuevo costo
    @PostMapping("/nuevo-costo")
    public ResponseEntity<Costo> guardarNuevoCosto(@Valid @RequestBody Costo nuevoCosto) {
        Costo costoGuardado = costoService.guardarCostoNuevo(nuevoCosto);
        return ResponseEntity.status(HttpStatus.CREATED).body(costoGuardado);
    }
}