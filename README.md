# 💳 Wallet API Core — Transactional Financial Engine

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit_5-Mockito-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

API RESTful transaccional para la gestión de cuentas y transferencias monetarias de una billetera virtual. Diseñada bajo estándares corporativos con foco en **integridad contable (ACID)**, **seguridad ante concurrencia** y **arquitectura limpia**.

---

## 🏛️ Decisiones de Arquitectura (ADR)

Este motor financiero resuelve los problemas críticos de los sistemas de pagos reales mediante los siguientes patrones:

1. **Control de Concurrencia con Optimistic Locking (`@Version`):**
   Evita condiciones de carrera (*Race Conditions*) y el problema de *Lost Updates* cuando múltiples peticiones intentan debitar o acreditar sobre una misma cuenta en simultáneo, sin penalizar el rendimiento con bloqueos físicos (`SELECT FOR UPDATE`) en la base de datos.
2. **Modelo Híbrido Ledger + Balance en $O(1)$:**
   Cada movimiento genera un registro inmutable de auditoría en la tabla `transactions` (*Ledger*), mientras que la entidad `Account` mantiene el saldo consolidado actualizado dentro de la misma frontera transaccional (`@Transactional`). Esto garantiza trazabilidad histórica total y consultas de saldo en tiempo constante $O(1)$.
3. **Aritmética Financiera Exacta (`BigDecimal`):**
   Se descarta el uso de tipos de punto flotante (`float` / `double`) en toda la capa de dominio y transferencia de datos para prevenir errores de redondeo binario (IEEE 754).
4. **Diseño Defensivo y *Fail-Fast*:**
   Validaciones estrictas de negocio antes de impactar la capa de persistencia: verificación de existencia de cuentas (`UUID`), integridad de `alias_cvu` y comprobación de fondos disponibles.
5. **Migraciones Versionadas con Flyway:**
   El esquema de base de datos no depende de `hibernate.ddl-auto`, sino de scripts SQL versionados e inmutables que aseguran reproducibilidad en cualquier entorno.

---

## 🛠️ Stack Tecnológico

| Componente | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Lenguaje** | Java 25 | Uso de las últimas capacidades y políticas de seguridad de la JVM |
| **Framework** | Spring Boot (Web, Data JPA) | Inyección de dependencias, capa REST y gestión transaccional |
| **Base de Datos** | PostgreSQL 15 (Alpine) | Motor relacional con soporte ACID para persistencia financiera |
| **Migraciones** | Flyway | Control de versiones del esquema SQL |
| **Infraestructura** | Docker & Docker Compose | Contenedorización multi-perfil (Desarrollo Local vs. Full Stack) |
| **Testing** | JUnit 5 + Mockito | Pruebas unitarias aisladas siguiendo el patrón AAA |
| **Documentación** | SpringDoc OpenAPI (Swagger) | Documentación interactiva y prueba de endpoints en vivo |

---

## 🚀 Cómo Ejecutar el Proyecto

El archivo `docker-compose.yml` está diseñado con **perfiles (`profiles`)** para separar la infraestructura del servidor de aplicaciones y evitar colisiones de puertos (`8080` vs `5432`).

### Prerrequisitos
* **JDK 25** instalado y configurado en `JAVA_HOME`.
* **Docker** y **Docker Compose** corriendo.
* **Maven** (o usar el wrapper `./mvnw` incluido en el proyecto).

### Modo 1: Desarrollo Local (Recomendado para el día a día)
Levanta únicamente el contenedor de PostgreSQL en el puerto `5432` para poder ejecutar y depurar la API directamente desde el IDE (IntelliJ IDEA):

```bash
# 1. Clonar el repositorio
git clone [https://github.com/facundoriosdev/wallet-API.git](https://github.com/facundoriosdev/wallet-API.git)
cd wallet-API

# 2. Levantar solo la base de datos en segundo plano
docker compose up -d

# 3. Iniciar la aplicación Spring Boot localmente
./mvnw spring-boot:run
```

### Modo 2: Entorno Completo en Contenedores (Simulación de Producción)
Activa el perfil `app` para que Docker compile el proyecto Java y levante tanto PostgreSQL como la API conectadas en su red interna:

```bash
docker compose --profile app up --build
```

---

## 📖 Documentación de la API y Pruebas

Con la aplicación en marcha, la documentación interactiva **Swagger UI** se encuentra disponible en:

👉 **`http://localhost:8080/swagger-ui/index.html`**

### Endpoint Principal: Ejecutar Transferencia
* **Método:** `POST`
* **URL:** `/api/v1/accounts/transfer`
* **Content-Type:** `application/json`

#### Ejemplo de Petición (cURL / Terminal)
```bash
curl -X POST http://localhost:8080/api/v1/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccountId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
    "destinationAccountId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
    "amount": 1500.50
  }'
```

#### Ejemplo de Petición (PowerShell)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/accounts/transfer" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{
    "sourceAccountId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
    "destinationAccountId": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
    "amount": 1500.50
  }'
```

---

## 🧪 Estrategia de Testing (Unit Tests)

Las reglas de negocio del motor transaccional (`AccountService`) están cubiertas por pruebas unitarias con **JUnit 5** y **Mockito**, estructuradas bajo el estándar **AAA (Arrange, Act, Assert)**:

* ✅ **Happy Path:** Transferencia exitosa con actualización exacta de saldos y persistencia del registro en el Ledger.
* ❌ **Fondos Insuficientes:** Bloqueo preventivo de la operación y verificación (`verify(..., never())`) de que no se realizan escrituras en la base de datos.
* ❌ **Validación de Alias / Cuentas Inexistentes:** Comportamiento *Fail-Fast* cuando el origen, destino o `alias_cvu` son inválidos.

Para ejecutar la suite de pruebas:

```bash
./mvnw test
```

> **Nota técnica sobre Java 25:** El `pom.xml` incluye los argumentos de JVM `-XX:+EnableDynamicAgentLoading -Xshare:off` en `<argLine>` para autorizar de forma explícita la instrumentación de Byte Buddy (Mockito) bajo las políticas de seguridad de JDK 25+.

---

## 🗺️ Roadmap de Evolución

- [x] Motor transaccional core con bloqueo optimista (`@Version`).
- [x] Infraestructura en Docker Compose aislada por perfiles.
- [x] Suite de Unit Tests con JUnit 5 y Mockito en Java 25.
- [x] Documentación viva con OpenAPI 3 / Swagger UI.
- [ ] Manejo centralizado de excepciones con `@ControllerAdvice` y respuestas RFC 7807 (`ProblemDetail`).
- [ ] Autenticación y autorización *stateless* mediante **Spring Security + JWT**.
- [ ] Desacoplamiento hacia arquitectura de microservicios: creación de **`cards-api`** (emisión y gestión de tarjetas de crédito bajo lineamientos de aislamiento PCI-DSS).
- [ ] Integración de procesamiento por lotes (*Batch Processing*) con sistemas legacy en **COBOL**.

---

## 👤 Autor

**Facundo Rios**  
* [GitHub: @facundoriosdev](https://github.com/facundoriosdev)
