package org.acme.infrastructure.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.ClienteService;
import org.acme.domain.model.EventoTransaccion;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ConsumidorTransaccion {

    @Inject
    ClienteService clienteService;

    @Inject
    ObjectMapper objectMapper;

    @Incoming("tx-init") // Escucha el mismo tópico donde escribe el orquestador
    public void recibirEventoInicial(String mensajeJson) {
        try {
            System.out.println(">>> [MS-CLIENTES] Evento recibido: " + mensajeJson);

            EventoTransaccion evento = objectMapper.readValue(mensajeJson, EventoTransaccion.class);

            // Pasamos a la capa de aplicación
            clienteService.validarCliente(evento);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}