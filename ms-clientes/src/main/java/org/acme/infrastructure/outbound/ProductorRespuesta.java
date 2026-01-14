package org.acme.infrastructure.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.domain.model.EventoResultado;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class ProductorRespuesta {

    @Inject
    @Channel("cli-result") // Canal de salida
    Emitter<String> emisor;

    @Inject
    ObjectMapper objectMapper;

    public void enviarResultado(EventoResultado resultado) {
        try {
            String json = objectMapper.writeValueAsString(resultado);
            emisor.send(json);
            System.out.println(">>> [MS-CLIENTES] Respuesta enviada: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}