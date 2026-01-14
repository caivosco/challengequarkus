package org.acme.infrastructure.inbound;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.application.TransaccionService;
import org.acme.domain.model.Transaccion;

import java.math.BigDecimal;
import java.util.List;

@Path("/transferencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransaccionResource {

    @Inject
    TransaccionService service;

    // DTO de entrada simple
    public static class SolicitudDTO {
        public Long origen;
        public Long destino;
        public BigDecimal monto;
    }

    @POST
    public Response crearTransferencia(SolicitudDTO solicitud) {
        try {
            Long idTx = service.iniciarTransaccion(
                    solicitud.origen,
                    solicitud.destino,
                    solicitud.monto
            );

            return Response.status(202) // 202 Accepted (Procesando)
                    .entity("{\"mensaje\": \"Transaccion iniciada\", \"id\": " + idTx + "}")
                    .build();

        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    public List<Transaccion> verHistorial() {
        // Retorna todas las transacciones ordenadas por fecha (más reciente al final)
        return Transaccion.listAll();
    }

    @GET
    @Path("/{id}")
    public Response verDetalle(@PathParam("id") Long id) {
        Transaccion tx = Transaccion.findById(id);
        if (tx == null) {
            return Response.status(404).build();
        }
        return Response.ok(tx).build();
    }
}