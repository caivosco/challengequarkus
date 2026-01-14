package org.acme.domain.model;

public class EventoResultado {
    public Long idTransaccion;
    public String estado; // "CLIENTE_OK", "CUENTA_ERROR", etc.
    public String mensaje;

    public EventoResultado() {}

    public EventoResultado(Long idTransaccion, String estado, String mensaje) {
        this.idTransaccion = idTransaccion;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    // Getters/Setters o campos publicos (usamos publicos por simplicidad)
}