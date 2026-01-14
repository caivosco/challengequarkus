package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.model.EventoResultado;
import org.acme.domain.model.LogNotificacion; // Asegúrate de tener esta entidad creada
import java.time.LocalDateTime;

@ApplicationScoped
public class NotificacionService {

    @Transactional
    public void registrarEvento(EventoResultado evento) {
        System.out.println(">>> [MS-NOTIFICACIONES] Registrando evento final: " + evento.estado);

        LogNotificacion log = new LogNotificacion();
        log.tipoEvento = evento.estado; // SUCCESS o FAILED
        log.detalleJson = "Tx ID: " + evento.idTransaccion + " - " + evento.mensaje;
        log.fechaRegistro = LocalDateTime.now();

        log.persist();
    }
}