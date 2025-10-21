package com.yesi.springcloud.msvc.costos.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.yesi.springcloud.msvc.costos.model.Costo;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;

@Service
public class CostoService {

        private final Map<Integer, Map<Integer, Integer>> cacheCostos = new ConcurrentHashMap<>();

         @PostConstruct
    private void cargarDatosIniciales() {
        // Usamos un método helper para cargar los datos y asegurar la simetría
        addCosto(1, 2, 2);
        addCosto(1, 3, 3);
        addCosto(2, 3, 5);
        addCosto(2, 4, 10);
        addCosto(1, 4, 11);
        addCosto(4, 5, 5);
        addCosto(2, 5, 14);
        addCosto(6, 7, 32);
        addCosto(8, 9, 11);
        addCosto(10, 7, 5);
        addCosto(3, 8, 10);
        addCosto(5, 8, 30);
        addCosto(10, 5, 5);
        addCosto(4, 6, 6);
    }

    /**
     * Agrega un costo de forma simetrica al cache
     */
    private void addCosto(Integer origen, Integer destino, Integer costo) {
        //Origen -> Destino
        cacheCostos.computeIfAbsent(origen, k -> new ConcurrentHashMap<>()).put(destino, costo);
        //Destino -> Origen
        cacheCostos.computeIfAbsent(destino, k -> new ConcurrentHashMap<>()).put(origen, costo);
    }

    //Carga un nuevo costo entre un punto de venta A y un punto de venta B
    public Costo guardarCostoNuevo(Costo nuevoCosto) {
        addCosto(nuevoCosto.idPuntoAOrigen(), nuevoCosto.idPuntoBDestino(), nuevoCosto.costo());
        return nuevoCosto;
    }
}
