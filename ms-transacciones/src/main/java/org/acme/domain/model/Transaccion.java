package org.acme.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
public class Transaccion extends PanacheEntityBase {

    // --- NUEVO: Definimos el ID explícitamente ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Postgres manejará el autoincremento
    public Long id;

    public Long cuentaOrigen;
    public Long cuentaDestino;
    public BigDecimal monto;

    public LocalDateTime fechaCreacion;

    // ESTADO DE LA MÁQUINA DE ESTADOS
    // PENDING -> VALIDATED -> PROCESSED -> SUCCESS
    //         -> FAILED
    public String estado;
    public String motivoFallo;

    public Transaccion() {}
}