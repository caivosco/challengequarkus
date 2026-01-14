package org.acme.infrastructure.inbound;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.model.Cliente;
import java.util.List;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    // 1. Obtener todos (Para ver la lista general)
    @GET
    public List<Cliente> listarTodos() {
        return Cliente.listAll();
    }

    // 2. Obtener por ID (Requerimiento específico)
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Cliente cliente = Cliente.findById(id);
        if (cliente == null) {
            return Response.status(404).entity("{\"mensaje\": \"Cliente no encontrado\"}").build();
        }
        return Response.ok(cliente).build();
    }
}