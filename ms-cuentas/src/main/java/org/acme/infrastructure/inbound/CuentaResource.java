package org.acme.infrastructure.inbound;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.model.Cuenta;
import java.util.List;

@Path("/cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CuentaResource {

    @GET
    public List<Cuenta> listarCuentas() {
        return Cuenta.listAll();
    }

    @GET
    @Path("/{numeroCuenta}")
    public Response obtenerCuenta(@PathParam("numeroCuenta") Long numeroCuenta) {
        Cuenta cuenta = Cuenta.findById(numeroCuenta);

        if (cuenta == null) {
            return Response.status(404).entity("{\"mensaje\": \"Cuenta no encontrada\"}").build();
        }
        return Response.ok(cuenta).build();
    }
}