package org.acme.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional; // Vital para el UPDATE
import org.acme.domain.model.Cuenta;
import org.acme.domain.model.EventoResultado;
import org.acme.domain.model.EventoTransaccion;
import org.acme.infrastructure.outbound.ProductorRespuesta;

@ApplicationScoped
public class CuentaService {

    @Inject
    ProductorRespuesta productor;

    @Transactional // Abre la transacción de Base de Datos
    public void procesarDebito(EventoTransaccion evento) {
        System.out.println(">>> [MS-CUENTAS] Procesando débito para Tx ID: " + evento.idTransaccion);

        // 1. Buscar Cuentas
        Cuenta origen = Cuenta.findById(evento.cuentaOrigen);
        Cuenta destino = Cuenta.findById(evento.cuentaDestino);
        EventoResultado respuesta;

        // 2. Validaciones
        if (origen == null || destino == null) {
            respuesta = new EventoResultado(evento.idTransaccion, "CUENTA_ERROR", "Cuenta origen o destino no existe");
        } else if (origen.saldo.compareTo(evento.monto) < 0) {
            // Saldo insuficiente
            respuesta = new EventoResultado(evento.idTransaccion, "CUENTA_ERROR", "Saldo insuficiente");
        } else {
            // 3. MOVIMIENTO DE DINERO (Atomicidad Local)
            origen.saldo = origen.saldo.subtract(evento.monto);
            destino.saldo = destino.saldo.add(evento.monto);

            // Persistimos cambios
            origen.persist();
            destino.persist();

            respuesta = new EventoResultado(evento.idTransaccion, "CUENTA_OK", "Transferencia realizada");
        }

        // 4. Responder al Orquestador
        productor.enviarResultado(respuesta);
    }
}