package org.acme.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cuenta")
public class Cuenta extends PanacheEntityBase {

    @Id
    public Long numeroCuenta;

    public Long idCliente; // Referencia lógica al otro microservicio
    public BigDecimal saldo;

    public Cuenta() {}
}