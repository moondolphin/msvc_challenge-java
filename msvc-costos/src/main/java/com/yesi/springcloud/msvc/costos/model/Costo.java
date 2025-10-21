package com.yesi.springcloud.msvc.costos.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record Costo(
    @NotNull Integer idPuntoAOrigen, 
    @NotNull Integer idPuntoBDestino, 
    
    @NotNull(message = "El costo no puede ser nulo.")
    @Min(value = 0, message = "El costo no puede ser negativo.")
    Integer costo
    ) {

}
