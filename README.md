# 🏦 Sistema Bancario Distribuido con Patrón SAGA (Quarkus + Kafka)

Este proyecto implementa una arquitectura de **Microservicios Reactivos** para simular un sistema bancario transaccional. Utiliza el **Patrón SAGA de Orquestación** para garantizar la consistencia de datos entre servicios distribuidos, comunicación asíncrona mediante **Kafka**, y persistencia aislada (**Database-per-Service**).

## 🚀 Tecnologías y Arquitectura

* **Java 17+** & **Quarkus Framework** (Superfast, Subatomic).

* **Apache Kafka**: Bus de mensajería para comunicación asíncrona.

* **PostgreSQL**: 4 bases de datos aisladas (una por microservicio).

* **Docker & Docker Compose**: Orquestación de infraestructura.

---

## 🏗️ Arquitectura de Microservicios

El sistema consta de 4 microservicios independientes:

| Servicio | Puerto | Base de Datos | Responsabilidad |

| :--- | :--- | :--- | :--- |

| **`ms-transacciones`** | `8083` | `transacciones_db` | **Orquestador SAGA**. Expone API REST segura, inicia el flujo y decide el estado final (SUCCESS/FAILED). |

| **`ms-clientes`** | `8081` | `clientes_db` | **Trabajador**. Valida si el cliente existe y está activo. |

| **`ms-cuentas`** | `8082` | `cuentas_db` | **Trabajador**. Valida saldos y ejecuta débitos/créditos (Atomicidad local). |

| **`ms-notificaciones`** | `8084` | `notificaciones_db` | **Auditor**. Escucha eventos finales y registra bitácora de auditoría. |

---

/

├── docker-compose.yaml # Infraestructura (Kafka, Zookeeper, Postgres x4)

├── ms-transacciones/ # Microservicio Orquestador (Rest, Security, JWT)

├── ms-clientes/ # Microservicio Validación de Identidad

├── ms-cuentas/ # Microservicio Core Bancario (Saldos)

└── ms-notificaciones/ # Microservicio Auditoría

## 🛠️ Prerrequisitos

* Docker y Docker Compose instalados y corriendo.

* Java JDK 17 o superior.

* Maven (opcional, el proyecto incluye `mvnw`).

---

## ▶️ Guía de Inicio Rápido

### 1. Levantar la Infraestructura

Ejecuta el siguiente comando en la raíz del proyecto para levantar Kafka, Zookeeper y las 4 bases de datos PostgreSQL:

```bash

docker-compose up -d

Espera unos 30 segundos hasta que los contenedores estén saludables.

cd ms-clientes

./mvnw clean quarkus:dev

cd ms-cuentas

./mvnw clean quarkus:dev

cd ms-transacciones

./mvnw clean quarkus:dev

cd ms-notificaciones

./mvnw clean quarkus:dev

Flujo de una Transacción (SAGA)
El proceso sigue estos pasos orquestados:

Inicio: ms-transacciones recibe petición REST, crea registro PENDING y emite evento tx-init.

Validación: ms-clientes consume evento, valida cliente y responde CLIENTE_OK o ERROR.

Decisión 1: Orquestador recibe respuesta. Si es OK, emite orden cta-orden. Si no, marca FAILED.

Operación Financiera: ms-cuentas valida saldo, actualiza DB y responde CUENTA_OK o ERROR.

Cierre: Orquestador actualiza estado a SUCCESS o FAILED y emite evento final tx-events.

Auditoría: ms-notificaciones guarda el resultado final.


