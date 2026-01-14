package org.acme.infrastructure.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.NotificacionService;
import org.acme.domain.model.EventoResultado;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ConsumidorAuditoria {

    @Inject NotificacionService servicio;
    @Inject ObjectMapper objectMapper;

    @Incoming("tx-events")
    public void recibirEventoFinal(String json) {
        try {
            EventoResultado evento = objectMapper.readValue(json, EventoResultado.class);
            servicio.registrarEvento(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}