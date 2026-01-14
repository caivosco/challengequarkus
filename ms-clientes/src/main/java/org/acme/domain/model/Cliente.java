package org.acme.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente extends PanacheEntityBase {

    @Id
    public Long id; // DNI o ID Cliente

    public String nombre;
    public String estado; // "ACTIVO", "BLOQUEADO"

    public Cliente() {}
}