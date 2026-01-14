package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.model.EventoResultado;
import org.acme.domain.model.EventoTransaccion;
import org.acme.domain.model.Transaccion;
import org.acme.infrastructure.outbound.ProductorKafka;

@ApplicationScoped
public class CoordinadorSagaService {

    @Inject ProductorKafka productor;

    // --- PASO 2: RECIBIMOS RESPUESTA DE CLIENTES ---
    @Transactional
    public void procesarRespuestaCliente(EventoResultado resultado) {
        System.out.println(">>> [ORQUESTADOR] Procesando respuesta Cliente: " + resultado.estado);

        Transaccion tx = Transaccion.findById(resultado.idTransaccion);
        if (tx == null) return; // Error grave, loguear

        if ("CLIENTE_OK".equals(resultado.estado)) {
            // ÉXITO PARCIAL: Avanzamos a Cuentas
            tx.estado = "VALIDATED";

            // Construimos la orden para Cuentas (Reusamos datos de la Tx)
            EventoTransaccion orden = new EventoTransaccion(
                    tx.id, tx.cuentaOrigen, tx.cuentaDestino, tx.monto, "DEBIT"
            );
            productor.enviarOrdenCuentas(orden);

        } else {
            // FALLO: Abortamos
            tx.estado = "FAILED";
            tx.motivoFallo = resultado.mensaje;
            // Notificamos el fallo final
            productor.enviarEventoFinal(new EventoResultado(tx.id, "FAILED", resultado.mensaje));
        }
        tx.persist();
    }

    // --- PASO 3: RECIBIMOS RESPUESTA DE CUENTAS ---
    @Transactional
    public void procesarRespuestaCuenta(EventoResultado resultado) {
        System.out.println(">>> [ORQUESTADOR] Procesando respuesta Cuenta: " + resultado.estado);

        Transaccion tx = Transaccion.findById(resultado.idTransaccion);
        if (tx == null) return;

        if ("CUENTA_OK".equals(resultado.estado)) {
            // ÉXITO TOTAL: La Saga se completó
            tx.estado = "SUCCESS";
            productor.enviarEventoFinal(new EventoResultado(tx.id, "SUCCESS", "Transferencia Completada"));
        } else {
            // FALLO: Abortamos
            tx.estado = "FAILED";
            tx.motivoFallo = resultado.mensaje;
            productor.enviarEventoFinal(new EventoResultado(tx.id, "FAILED", resultado.mensaje));
        }
        tx.persist();
    }
}