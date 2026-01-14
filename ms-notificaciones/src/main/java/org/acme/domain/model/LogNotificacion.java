package org.acme.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_notificacion")
public class LogNotificacion extends PanacheEntity {

    public String tipoEvento; // "EXITO", "FALLO"
    public String detalleJson; // Guardaremos el payload completo
    public LocalDateTime fechaRegistro;

    public LogNotificacion() {}
}