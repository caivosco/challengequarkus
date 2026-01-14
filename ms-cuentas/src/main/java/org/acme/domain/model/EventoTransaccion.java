package org.acme.domain.model;

import java.math.BigDecimal;

public class EventoTransaccion {
    public Long idTransaccion;
    public Long cuentaOrigen; // Usaremos esto como ID de Cliente para simplificar
    public Long cuentaDestino;
    public BigDecimal monto;
    public String tipo;

    public EventoTransaccion() {}
}