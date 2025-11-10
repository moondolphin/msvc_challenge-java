package com.yesi.springcloud.msvc.costos.controller;

import com.yesi.springcloud.msvc.costos.model.Costo;
import com.yesi.springcloud.msvc.costos.services.CostoService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/{idOrigen}/to/{idDestino}")
    public ResponseEntity<Map<String, String>> eliminarCosto(
            @PathVariable Integer idOrigen,
            @PathVariable Integer idDestino) {
        
        Optional<Costo> costoEliminadoOptional = costoService.eliminarCosto(idOrigen, idDestino);
        
        Map<String, String> response = new HashMap<>();

        if (costoEliminadoOptional.isPresent()) {
            // exito: el Optional contiene el registro del costo borrado.
            Costo costoElm = costoEliminadoOptional.get();
            response.put("mensaje", "Costo eliminado con éxito: Origen=" + costoElm.idPuntoAOrigen() + 
                                   ", Destino=" + costoElm.idPuntoBDestino() + ", Costo=" + costoElm.costo());
            return ResponseEntity.ok(response);
        } else {
            // falla: El Optional estaba vacio, no se encontro el costo
            response.put("error", "No se encontró el costo entre " + idOrigen + " y " + idDestino + ".");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}