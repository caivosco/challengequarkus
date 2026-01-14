package org.acme.infrastructure.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.model.EventoTransaccion;
import org.acme.domain.model.EventoResultado; // Usaremos esto para notificar el final
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class ProductorKafka {

    @Inject @Channel("tx-init") Emitter<String> emisorInit;
    @Inject @Channel("cta-orden") Emitter<String> emisorCuentas; // NUEVO
    @Inject @Channel("tx-events") Emitter<String> emisorFinal;   // NUEVO

    @Inject ObjectMapper objectMapper;

    // YA EXISTÍA
    public void enviarInicioValidacion(EventoTransaccion evento) {
        enviar(emisorInit, evento, "tx-init");
    }

    // NUEVO: Ordenar a Cuentas mover el dinero
    public void enviarOrdenCuentas(EventoTransaccion evento) {
        enviar(emisorCuentas, evento, "cta-orden");
    }

    // NUEVO: Avisar a Notificaciones el resultado final
    public void enviarEventoFinal(EventoResultado resultado) {
        enviar(emisorFinal, resultado, "tx-events");
    }

    // Helper privado para no repetir try-catch
    private void enviar(Emitter<String> emisor, Object payload, String canal) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            emisor.send(json);
            System.out.println(">>> [ORQUESTADOR] Enviado a " + canal + ": " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}