package org.acme.infrastructure.inbound;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.model.LogNotificacion;
import java.util.List;

@Path("/notificaciones")
@Produces(MediaType.APPLICATION_JSON)
public class NotificacionResource {

    @GET
    public List<LogNotificacion> verLogs() {
        // listAll() viene gratis con PanacheEntity
        return LogNotificacion.listAll();
    }
}