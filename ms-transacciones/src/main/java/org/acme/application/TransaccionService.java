package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.model.Transaccion;
import org.acme.domain.model.EventoTransaccion;
import org.acme.infrastructure.outbound.ProductorKafka;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApplicationScoped
public class TransaccionService {

    @Inject
    ProductorKafka productorKafka;

    @Transactional // <--- Atomicidad con PostgreSQL
    public Long iniciarTransaccion(Long origen, Long destino, BigDecimal monto) {

        // 1. Guardar en Base de Datos (Estado PENDING)
        Transaccion tx = new Transaccion();
        tx.cuentaOrigen = origen;
        tx.cuentaDestino = destino;
        tx.monto = monto;
        tx.fechaCreacion = LocalDateTime.now();
        tx.estado = "PENDING"; // Estado inicial de la Máquina de Estados

        tx.persist(); // Se genera el ID automáticamente

        // 2. Publicar Evento de Inicio a Kafka
        // Nota: Enviamos el ID de la transacción para rastrearla
        EventoTransaccion evento = new EventoTransaccion(
                tx.id, origen, destino, monto, "INIT"
        );

        productorKafka.enviarInicioValidacion(evento);

        return tx.id;
    }
}