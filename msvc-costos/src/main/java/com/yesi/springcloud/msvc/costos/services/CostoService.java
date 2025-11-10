package com.yesi.springcloud.msvc.costos.services;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.yesi.springcloud.msvc.costos.model.Costo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
        // Origen -> Destino
        cacheCostos.computeIfAbsent(origen, k -> new ConcurrentHashMap<>()).put(destino, costo);
        // Destino -> Origen
        cacheCostos.computeIfAbsent(destino, k -> new ConcurrentHashMap<>()).put(origen, costo);
    }

    // Carga un nuevo costo entre un punto de venta A y un punto de venta B
    public Costo guardarCostoNuevo(Costo nuevoCosto) {
        addCosto(nuevoCosto.idPuntoAOrigen(), nuevoCosto.idPuntoBDestino(), nuevoCosto.costo());
        return nuevoCosto;
    }



    public Optional<Costo> eliminarCosto(Integer idOrigen, Integer idDestino) {
        log.info("Se va a eliminar costo entre: ", idOrigen, " y ", idDestino);

        // para evitar deadlocks:
        // Si un hilo intenta borrar (1, 2) y otro (2, 1)
        // se podría bloquear 1 y el otro 2, y ambos quedarían "congelados" esperando el bloqueo del otro.
        // para evitar esto Se Determina el orden de bloqueo
        // Se bloquea primero el ID maas bajo, luego el mas alto.
        Integer primerId = Math.min(idOrigen, idDestino);
        Integer segundoId = Math.max(idOrigen, idDestino);

        //Obtener los mapas interiores 
        Map<Integer, Integer> mapaPrimerId = cacheCostos.get(primerId);
        Map<Integer, Integer> mapaSegundoId = cacheCostos.get(segundoId);

        // si alguno de los dos mapas no existe, la conexion no puede existir.
        if (mapaPrimerId == null || mapaSegundoId == null) {
            log.warn("No existe conexión para eliminar entre {} y {}", idOrigen, idDestino);
            return Optional.empty();
        }

        
        Integer costo = null;


        // se aplica syncronized:
        //Nadie más puede usar el objeto mapaPrimerId hasta que yo termine este bloque
        // Al anidar los dos synchronized, se garantizaque tenemos el control 
        //exclusivo de ambos mapas interiores antes de hacer el borrado
        // entonces, se adquieren los bloqueos en el orden definido.
        // eesto asegura que la operación sea atómica y segura para la concurrencia
        synchronized (mapaPrimerId) {
            synchronized (mapaSegundoId) {

                log.debug("Bloqueo adquirido para:  ", primerId, segundoId);

                //operaciones de borrado de forma simetrica
                // Ahora que tenemos ambos "mapas" bloqueados, nadie más puede tocarlos 
                costo = mapaPrimerId.remove(segundoId); // se borrar A->B
                mapaSegundoId.remove(primerId); // Borra B-->A
            }
        }

        
        if (costo != null) {
            //si el costo no es null, significa que lo encontró y puede borraar con exitoo
            return Optional.of(new Costo(idOrigen, idDestino, costo));
        } else {
            log.warn("La conexión directa de origen y destino no existe.", idOrigen, idDestino);
            return Optional.empty();
        }
    }

}
