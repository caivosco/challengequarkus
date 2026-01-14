package org.acme.infrastructure.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.application.CoordinadorSagaService;
import org.acme.domain.model.EventoResultado;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ConsumidorRespuestas {

    @Inject CoordinadorSagaService coordinador;
    @Inject ObjectMapper objectMapper;

    // Escucha respuesta de Clientes
    @Incoming("cli-result")
    public void recibirCliente(String json) {
        procesar(json, "CLIENTE");
    }

    // Escucha respuesta de Cuentas
    @Incoming("cta-result")
    public void recibirCuenta(String json) {
        procesar(json, "CUENTA");
    }

    private void procesar(String json, String origen) {
        try {
            EventoResultado res = objectMapper.readValue(json, EventoResultado.class);
            if ("CLIENTE".equals(origen)) {
                coordinador.procesarRespuestaCliente(res);
            } else {
                coordinador.procesarRespuestaCuenta(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}