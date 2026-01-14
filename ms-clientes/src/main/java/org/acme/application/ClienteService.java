package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.model.Cliente;
import org.acme.domain.model.EventoResultado;
import org.acme.domain.model.EventoTransaccion;
import org.acme.infrastructure.outbound.ProductorRespuesta;

@ApplicationScoped
public class ClienteService {

    @Inject
    ProductorRespuesta productor;

    // @Transactional abre la sesión de Hibernate.
    // Sin esto, findById falla cuando viene desde Kafka.
    @Transactional
    public void validarCliente(EventoTransaccion evento) {
        // Usamos cuentaOrigen como si fuera el ID del cliente para el ejemplo
        Long idCliente = evento.cuentaOrigen;

        System.out.println(">>> [MS-CLIENTES] Validando cliente ID: " + idCliente);

        Cliente cliente = Cliente.findById(idCliente);

        EventoResultado respuesta;

        if (cliente == null) {
            respuesta = new EventoResultado(evento.idTransaccion, "CLIENTE_ERROR", "Cliente no existe");
        } else if ("BLOQUEADO".equals(cliente.estado)) {
            respuesta = new EventoResultado(evento.idTransaccion, "CLIENTE_ERROR", "Cliente bloqueado");
        } else {
            respuesta = new EventoResultado(evento.idTransaccion, "CLIENTE_OK", "Cliente validado exitosamente");
        }

        // Enviamos la respuesta a Kafka
        productor.enviarResultado(respuesta);
    }
}