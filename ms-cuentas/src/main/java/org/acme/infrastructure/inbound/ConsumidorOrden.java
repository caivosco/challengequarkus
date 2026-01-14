package org.acme.infrastructure.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.CuentaService;
import org.acme.domain.model.EventoTransaccion;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ConsumidorOrden {

    @Inject
    CuentaService cuentaService;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("cta-orden") // Escucha órdenes del orquestador
    public void recibirOrden(String mensajeJson) {
        try {
            System.out.println(">>> [MS-CUENTAS] Orden recibida: " + mensajeJson);
            EventoTransaccion evento = objectMapper.readValue(mensajeJson, EventoTransaccion.class);

            cuentaService.procesarDebito(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}