package org.acme.domain.model;

public class EventoResultado {
    public Long idTransaccion;
    public String estado; // "CLIENTE_OK" o "CLIENTE_ERROR"
    public String mensaje;

    public EventoResultado() {}

    public EventoResultado(Long id, String estado, String mensaje) {
        this.idTransaccion = id;
        this.estado = estado;
        this.mensaje = mensaje;
    }
}