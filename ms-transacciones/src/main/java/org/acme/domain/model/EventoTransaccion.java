package org.acme.domain.model;

import java.math.BigDecimal;

// Este objeto se convertirá a JSON y viajará por Kafka
public class EventoTransaccion {
    public Long idTransaccion; // ID único de la SAGA
    public Long cuentaOrigen;
    public Long cuentaDestino;
    public BigDecimal monto;
    public String tipo; // "INIT", "CLIENTE_OK", "CUENTA_OK"

    public EventoTransaccion() {}

    public EventoTransaccion(Long id, Long origen, Long destino, BigDecimal monto, String tipo) {
        this.idTransaccion = id;
        this.cuentaOrigen = origen;
        this.cuentaDestino = destino;
        this.monto = monto;
        this.tipo = tipo;
    }
}