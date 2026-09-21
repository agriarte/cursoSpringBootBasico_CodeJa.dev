# Repaso Spring Boot

Proyecto didáctico para repasar conceptos básicos de Spring Boot y Java basado en el curso **Spring Boot Básico** de [www.codeja.dev](https://www.codeja.dev/).

El proyecto contiene **dos aplicaciones Spring Boot independientes**.

## Índice

1. [`springbootbasico`](#1-springbootbasico)
2. [`restbasico`](#2-restbasico)
3. [Evolución del proyecto `restbasico`](#3-evolución-del-proyecto-restbasico)
4. [Spring Security](#4-spring-security)
5. [Probar la API REST con Postman](#5-probar-la-api-rest-con-postman)
6. [Interfaz web con Thymeleaf](#6-interfaz-web-con-thymeleaf)
7. [Arquitectura actual](#7-arquitectura-actual)
8. [Evolución global del proyecto](#8-evolución-global-del-proyecto)
9. [Objetivo del proyecto](#9-objetivo-del-proyecto)

---

## Inicio rápido (`restbasico`)

Requisitos: Java 21, Maven, Docker y Docker Compose.

```bash
git clone <url-del-repo>
cd <carpeta-de-restbasico>
docker compose up -d
```

La interfaz web queda disponible en `http://localhost:8080/web/empleados`. Al no haber iniciado sesión, redirige a `/login`. Usuarios de demostración:

| Usuario | Contraseña | Rol |
|---|---|---|
| `user` | `password` | `USER` (solo consulta) |
| `admin` | `admin` | `ADMIN` (consulta, crea, modifica y elimina) |

Si prefieres ejecutar Spring Boot desde Eclipse, consulta [3.5.1](#351-perfiles-de-configuración-eclipse-y-docker). Para probar la API con Postman, consulta [5](#5-probar-la-api-rest-con-postman).

---

## 1. `springbootbasico`

Ejemplos sencillos de conceptos y anotaciones de Spring:

- `@Component`
- `@Service`
- `@Repository`
- `@Autowired`
- Inyección de dependencias
- Beans

## 2. `restbasico`

Ejemplo de una aplicación Spring Boot con un CRUD de empleados, una API REST y una interfaz web basada en **Thymeleaf + Bootstrap**.

La aplicación evoluciona progresivamente desde un almacenamiento en memoria con `ArrayList` hasta una aplicación con **JPA, PostgreSQL, Docker, Docker Compose, Spring Profiles y Spring Security**.

### API REST

| Método | Endpoint | Operación | Acceso |
|---|---|---|---|
| GET | `/empleados` | Listar empleados | Usuario autenticado |
| GET | `/empleados/{id}` | Consultar un empleado | Usuario autenticado |
| POST | `/empleados` | Crear empleado | `ADMIN` |
| PUT | `/empleados/{id}` | Modificar empleado | `ADMIN` |
| DELETE | `/empleados/{id}` | Eliminar empleado | `ADMIN` |
| GET | `/csrf` | Obtener el token CSRF de la sesión | Público |

> El acceso se explica en [4. Spring Security](#4-spring-security). En las etapas anteriores a la seguridad, estos endpoints eran de acceso libre.

### Interfaz web

| Método | Endpoint | Operación |
|---|---|---|
| GET | `/web/empleados` | Mostrar lista de empleados |
| GET | `/web/empleados/nuevo` | Mostrar formulario de alta |
| GET | `/web/empleados/editar/{id}` | Mostrar formulario de edición |

La interfaz utiliza:

- Thymeleaf
- Bootstrap
- `thymeleaf-extras-springsecurity6`
- JavaScript `fetch()`
- Formularios para crear y modificar
- Botones de editar y eliminar
- Modal de confirmación para eliminar

---

## 3. Evolución del proyecto `restbasico`

El proyecto se construye progresivamente para poder estudiar cada concepto de forma aislada y comprobar después cómo se integran todos en una aplicación completa.

Cada etapa del desarrollo queda registrada en Git, por lo que el historial permite recuperar los diferentes estados del proyecto.

### 3.1. Almacenamiento en memoria con `ArrayList`

Inicialmente se utiliza una `ArrayList` para almacenar los empleados y realizar las operaciones básicas del CRUD.

Las operaciones pueden realizarse tanto mediante los endpoints de la API REST como desde la interfaz web.

En esta etapa no existe persistencia en una base de datos. Los datos se pierden al reiniciar la aplicación.

La arquitectura inicial es:

```text
Controller
    ↓
ArrayList
```

### 3.2. Separación de la lógica mediante `Service`

Posteriormente se separa la lógica de negocio del Controller mediante la incorporación de `EmpleadosService`.

La arquitectura pasa a ser:

```text
Controller
    ↓
Service
    ↓
ArrayList
```

El Controller se encarga de recibir las peticiones HTTP y el Service de realizar las operaciones sobre los empleados.

Esta separación permite introducir una primera división clara de responsabilidades.

### 3.3. Persistencia con H2

Posteriormente se sustituye la `ArrayList` por una base de datos **H2 en memoria**, utilizando **JPA y Spring Data JPA**.

Se incorporan:

- `@Entity`
- `Empleado`
- `EmpleadoDTO`
- `EmpleadoRepository`
- `JpaRepository`
- H2
- Hibernate

La arquitectura pasa a ser:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
H2
```

La Entity `Empleado` representa los datos almacenados en la base de datos, mientras que `EmpleadoDTO` se utiliza para transportar los datos entre las diferentes capas de la aplicación.

El ID del empleado se genera automáticamente mediante JPA/H2.

H2 se utiliza en esta etapa como base de datos en memoria para aprendizaje y pruebas.

### 3.4. Persistencia con PostgreSQL

La siguiente evolución sustituye H2 por **PostgreSQL**.

PostgreSQL se ejecuta mediante Docker y se gestiona mediante `compose.yml`.

La imagen utilizada es:

```text
postgres:16-alpine
```

La base de datos utilizada por la aplicación es:

```text
empleados
```

La conexión se configura mediante las propiedades de Spring Boot, indicando:

- URL JDBC
- Puerto
- Base de datos
- Usuario
- Contraseña
- Configuración de Hibernate

Se sustituye la dependencia JDBC de H2 por la correspondiente a PostgreSQL.

La arquitectura pasa a ser:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
    ↓
Docker
```

Hibernate continúa encargándose de adaptar la Entity `Empleado` a la estructura de la base de datos.

El ID continúa generándose automáticamente mediante:

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Para conservar la estructura de las tablas al reiniciar Spring Boot se utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

De esta forma Hibernate mantiene las tablas existentes y no las elimina al reiniciar la aplicación.

Además, PostgreSQL utiliza un **volumen Docker** para conservar los datos aunque el contenedor se detenga, se reinicie o se vuelva a crear.

```text
PostgreSQL
    ↓
Docker Volume
    ↓
Datos persistentes
```

### 3.5. Spring Boot y PostgreSQL en Docker

En la siguiente etapa, tanto **Spring Boot** como **PostgreSQL** se ejecutan mediante Docker Compose.

La aplicación Spring Boot se construye mediante un Dockerfile utilizando una construcción **multi-stage**.

La primera etapa utiliza Maven y JDK 21 para compilar el proyecto y generar el JAR.

La segunda utiliza únicamente JRE 21 para ejecutar la aplicación.

```text
Maven + JDK 21
      ↓
  Compilación
      ↓
      JAR
      ↓
JRE 21 Alpine
      ↓
Contenedor final
```

El contenedor final contiene el JAR de la aplicación y un entorno de ejecución Java 21.

Docker Compose permite ejecutar conjuntamente los dos servicios:

```text
Docker Compose
      │
      ├── Spring Boot
      │      ↓
      │   Puerto 8080
      │
      └── PostgreSQL
             ↓
          Puerto 5432
             ↓
         Docker Volume
```

La aplicación Spring Boot se conecta con PostgreSQL utilizando el nombre del servicio Docker:

```text
postgres:5432
```

Desde el contenedor de Spring Boot, `postgres` identifica al contenedor de PostgreSQL dentro de la red creada por Docker Compose.

#### Comandos principales de Docker

Levantar los dos servicios:

```bash
docker compose up -d
```

Reconstruir la imagen de Spring Boot después de realizar cambios:

```bash
docker compose up -d --build
```

Comprobar el estado de los contenedores:

```bash
docker compose ps
```

La interfaz web queda disponible en:

```text
http://localhost:8080/web/empleados
```

### 3.5.1. Perfiles de configuración: Eclipse y Docker

Al ejecutar el proyecto existen dos escenarios:

1. Ejecutar Spring Boot directamente desde Eclipse.
2. Ejecutar Spring Boot dentro de un contenedor Docker.

En ambos casos PostgreSQL puede continuar ejecutándose en Docker, pero la URL utilizada por Spring Boot cambia según dónde se esté ejecutando la aplicación.

Para evitar modificar manualmente `application.properties`, se utilizan **Spring Profiles**.

La estructura de configuración es:

```text
src/main/resources/

├── application.properties
├── application-eclipse.properties
└── application-docker.properties
```

`application.properties` contiene la configuración común.

`application-eclipse.properties` contiene la configuración específica para ejecutar Spring Boot desde Eclipse.

`application-docker.properties` contiene la configuración específica para ejecutar Spring Boot desde Docker.

#### Perfil `eclipse`

Cuando Spring Boot se ejecuta desde Eclipse, se utiliza:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/empleados
```

Desde Eclipse, Spring Boot se ejecuta directamente en Windows.

PostgreSQL continúa ejecutándose dentro de Docker y su puerto `5432` está publicado en el equipo local.

Para activar este perfil desde Eclipse:

1. Abrir **Run → Run Configurations...**
2. Seleccionar la configuración de la aplicación Spring Boot.
3. Entrar en la pestaña **Arguments**.
4. En **Program arguments**, añadir:

```text
--spring.profiles.active=eclipse
```

5. Pulsar **Apply** y ejecutar la aplicación.

Al iniciar Spring Boot debe aparecer en la consola:

```text
The following 1 profile is active: "eclipse"
```

Esto indica que Spring Boot utiliza `application-eclipse.properties` y, por tanto, la conexión:

```text
jdbc:postgresql://localhost:5432/empleados
```

Para este escenario solo es necesario tener PostgreSQL levantado:

```bash
docker compose up -d postgres
```

No es necesario levantar el servicio `app` de Docker, ya que Spring Boot se está ejecutando desde Eclipse.

La comunicación es:

```text
Spring Boot
(Eclipse / Windows)
       │
       │ localhost:5432
       ▼
PostgreSQL
(Docker)
```

#### Perfil `docker`

Cuando Spring Boot se ejecuta dentro de Docker, se utiliza:

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/empleados
```

En este caso, `postgres` es el nombre del servicio PostgreSQL definido en `compose.yml`.

Los dos contenedores se encuentran dentro de la red creada por Docker Compose y pueden comunicarse utilizando el nombre del servicio.

El perfil `docker` se activa desde `compose.yml` mediante:

```yaml
app:
  build: .
  container_name: codeja-springboot
  environment:
    SPRING_PROFILES_ACTIVE: docker
```

Por tanto, cuando se ejecuta:

```bash
docker compose up -d
```

Spring Boot utiliza `application-docker.properties` y la conexión:

```text
jdbc:postgresql://postgres:5432/empleados
```

La comunicación es:

```text
Spring Boot
   (Docker)
      │
      │ postgres:5432
      ▼
PostgreSQL
   (Docker)
```

#### Resumen de los perfiles

| Entorno | Spring Boot | Perfil activo | PostgreSQL |
|---|---|---|---|
| Eclipse | Windows | `eclipse` | `localhost:5432` |
| Docker | Contenedor | `docker` | `postgres:5432` |

La idea es mantener una única configuración común y utilizar perfiles para las diferencias específicas de cada entorno.

```text
                  ┌─────────────────────────┐
                  │ application.properties  │
                  │   Configuración común    │
                  └────────────┬────────────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
                    ▼                     ▼
 application-eclipse.properties   application-docker.properties
                    │                     │
                    ▼                     ▼
             localhost:5432          postgres:5432
                    │                     │
                    ▼                     ▼
               PostgreSQL           PostgreSQL
                (Docker)             (Docker)
```

De esta forma, **no es necesario modificar manualmente la URL de PostgreSQL al cambiar de entorno**. Solo se cambia el perfil activo y Spring Boot selecciona el archivo correspondiente.

### 3.6. Resumen de la evolución de la persistencia

El proyecto ha evolucionado desde un almacenamiento en memoria hasta una aplicación con persistencia real y ejecución mediante Docker:

```text
ArrayList
   ↓
H2 en memoria
   ↓
PostgreSQL
   ↓
PostgreSQL + Spring Boot en Docker
```

Cada etapa permite revisar un concepto diferente de Spring Boot antes de incorporar el siguiente.

---

## 4. Spring Security

Como siguiente evolución se incorpora **Spring Security** para proteger la aplicación y controlar el acceso a las operaciones del CRUD.

Se añade la dependencia:

```text
spring-boot-starter-security
```

La seguridad se configura mediante `SecurityConfig`, donde se define un `SecurityFilterChain`.

En la arquitectura por capas, `SecurityConfig` pertenece a la configuración de la aplicación y actúa como puerta de entrada antes de los Controllers.

### 4.1. Autenticación y autorización

La aplicación utiliza dos mecanismos de autenticación que conviven en la misma cadena de filtros:

| Mecanismo | Cliente | Cómo se autentica |
|---|---|---|
| **Form Login** | Navegador e interfaz web | Formulario en `/login` y cookie de sesión `JSESSIONID` |
| **HTTP Basic** | Clientes REST como Postman | Cabecera `Authorization: Basic ...` en cada petición |

Los usuarios se gestionan en memoria mediante `InMemoryUserDetailsManager`. No se guardan en PostgreSQL y desaparecen al detener la aplicación.

Se definen dos usuarios de demostración:

```text
user
  ↓
ROLE_USER

admin
  ↓
ROLE_ADMIN
```

Las credenciales son:

| Usuario | Contraseña | Rol | Permisos |
|---|---|---|---|
| `user` | `password` | `USER` | Solo consultar (`GET`) |
| `admin` | `admin` | `ADMIN` | Consultar, crear, modificar y eliminar |

Las contraseñas se codifican mediante `BCryptPasswordEncoder`.

> Estos usuarios son solo de demostración, para uso didáctico. En una aplicación real las credenciales no deben estar en el código.

De esta forma se diferencia entre **autenticación** y **autorización**:

- **Autenticación:** determina quién es el usuario que ha iniciado sesión.
- **Autorización:** determina qué operaciones puede realizar ese usuario según sus roles.

Por ejemplo, un usuario con `ROLE_USER` puede consultar empleados, pero no puede crear, modificar o eliminar empleados.

Un usuario con `ROLE_ADMIN` puede realizar además las operaciones de creación, modificación y eliminación del CRUD.

### 4.2. Reglas de acceso

El acceso se controla mediante el método HTTP y el rol del usuario.

| Operación | Acceso |
|---|---|
| GET `/csrf` | Público |
| GET `/empleados`, `/empleados/**` | Usuario autenticado |
| POST `/empleados`, `/empleados/**` | `ADMIN` |
| PUT `/empleados`, `/empleados/**` | `ADMIN` |
| DELETE `/empleados`, `/empleados/**` | `ADMIN` |
| `/web/**` | Usuario autenticado |
| Resto de peticiones | Permitidas |

Las reglas se evalúan **en orden** y se aplica la primera que coincide, por lo que la regla del resto de peticiones (`anyRequest()`) debe ir siempre la última.

> Al ser el resto de peticiones `permitAll()`, cualquier ruta nueva que no se añada a las reglas anteriores queda **pública**. Al crear nuevos endpoints hay que protegerlos en `SecurityConfig`. En una aplicación real lo habitual es `anyRequest().authenticated()` y abrir solo lo necesario.

### 4.3. CSRF

**CSRF** (*Cross-Site Request Forgery*) es un ataque en el que una web externa hace que el navegador de un usuario autenticado envíe, sin que este lo sepa, una petición a nuestra aplicación aprovechando su sesión.

Para evitarlo, Spring Security mantiene activa la protección CSRF: las peticiones que modifican datos deben incluir un **token CSRF** que solo puede conocer un cliente legítimo.

| Petición | ¿Necesita token CSRF? |
|---|---|
| `GET` | No |
| `POST`, `PUT`, `DELETE` | Sí |

La configuración utiliza el mecanismo estándar de Spring Security:

```java
.csrf(Customizer.withDefaults())
```

No se personaliza el repositorio del token.

El token está asociado a la sesión del usuario y debe corresponder a la cookie `JSESSIONID`.

#### Endpoint `/csrf`

Para facilitar el uso de la API desde JavaScript y Postman se ha creado el endpoint:

```text
GET /csrf
```

Este endpoint es público y devuelve el token CSRF asociado a la sesión actual.

La respuesta tiene esta estructura:

```json
{
  "headerName": "X-CSRF-TOKEN",
  "parameterName": "_csrf",
  "token": "..."
}
```

El cliente debe utilizar el campo `headerName` como nombre de la cabecera y `token` como valor.

Por ejemplo:

```http
X-CSRF-TOKEN: <token>
```

Es importante mantener la misma sesión entre `GET /csrf` y la petición que modifica datos.

#### Quién envía el token en cada caso

| Cliente | Cómo se envía el token |
|---|---|
| Formularios Thymeleaf con `th:action` (por ejemplo, logout) | Automáticamente, como campo oculto |
| JavaScript `fetch()` de la interfaz web | `GET /csrf` y cabecera con el token (ver [6.3](#63-csrf-en-fetch)) |
| Postman | Cabecera `X-CSRF-TOKEN`, a mano o con un script (ver [5](#5-probar-la-api-rest-con-postman)) |

#### ¿Por qué aparece un `403`?

Si un `POST`, `PUT` o `DELETE` llega sin token CSRF, con un token incorrecto o con un token que no corresponde a la sesión actual, Spring Security responde `403 Forbidden`.

La comprobación CSRF se realiza antes de que la petición llegue al Controller y antes de comprobar los roles.

Por eso un usuario autenticado como `admin` también puede recibir un `403` si no envía el token CSRF correcto.

### 4.4. Form Login

El navegador utiliza el formulario de login proporcionado por Spring Security.

La configuración establece:

```java
.formLogin(formLogin -> formLogin
    .defaultSuccessUrl("/web/empleados")
    .permitAll()
)
```

Cuando el usuario accede a una página protegida sin haber iniciado sesión, Spring Security lo redirige a `/login`.

Después de autenticarse correctamente, la aplicación accede a `/web/empleados` cuando no existe otra URL protegida pendiente.

### 4.5. HTTP Basic para Postman y clientes REST

Para probar la API REST mediante Postman se habilita `httpBasic` junto a `formLogin`:

```java
.formLogin(formLogin -> formLogin
    .defaultSuccessUrl("/web/empleados")
    .permitAll()
)
.httpBasic(Customizer.withDefaults())
```

El cliente envía las credenciales en cada petición mediante la cabecera HTTP Basic:

```http
Authorization: Basic base64(usuario:contraseña)
```

Esta línea es **necesaria** para Postman. `formLogin` solo sabe autenticar a un navegador: espera que el usuario rellene el formulario de `/login` y mantiene después la sesión con una cookie. Postman no rellena formularios y, aunque envíe la cabecera `Authorization`, Spring la ignora si `httpBasic` no está activado.

Qué ocurre en Postman con una petición sin autenticar:

| Configuración | Respuesta |
|---|---|
| Sin `httpBasic` | `302` a `/login`. Postman sigue la redirección y muestra un `200` con el HTML del formulario en lugar del JSON |
| Con `httpBasic` | `401 Unauthorized`. Con credenciales válidas, la petición llega al Controller |

`httpBasic` **no afecta al navegador**: Spring elige cómo responder según el tipo de cliente (cabecera `Accept`).

| Cliente | Petición sin autenticar | Respuesta |
|---|---|---|
| Navegador (`Accept: text/html`) | `/web/empleados` | Redirige a `/login` |
| Postman u otro cliente REST | `/empleados` | `401` con cabecera `WWW-Authenticate: Basic` |

Conviene tener en cuenta:

- **Basic Auth solo resuelve la autenticación. No sustituye a CSRF.** Las peticiones `POST`, `PUT` y `DELETE` realizadas desde Postman siguen necesitando credenciales válidas, el token CSRF y la misma sesión `JSESSIONID` asociada al token.
- Las credenciales viajan codificadas en Base64, **no cifradas**. Fuera de un entorno de pruebas debe usarse HTTPS.

### 4.6. Logout

Spring Security proporciona `POST /logout` para cerrar la sesión. Al ser `POST`, también requiere el token CSRF (Thymeleaf lo incluye en el formulario).

La configuración redirige después del logout a:

```text
/logout
   ↓
/web/empleados
```

Como `/web/empleados` requiere autenticación, el usuario será enviado al formulario de login después de cerrar sesión.

---

## 5. Probar la API REST con Postman

Con la seguridad activa, cada petición `POST`, `PUT` o `DELETE` necesita **tres cosas a la vez**. Las peticiones `GET` solo necesitan la primera.

| Qué | Para qué | Cómo se envía |
|---|---|---|
| **Credenciales** | Saber quién eres | Basic Auth (`admin` / `admin`) |
| **Sesión** | Spring guarda ahí el token CSRF y lo compara | Cookie `JSESSIONID` (Postman la guarda sola) |
| **Token CSRF** | Demostrar que la petición es legítima | Cabecera `X-CSRF-TOKEN` |

El token está ligado a la sesión: debe obtenerse **desde Postman** (con `GET /csrf`) y usarse con la misma cookie. Un token copiado del navegador no sirve.

### 5.1. Preparar la colección y Basic Auth

**1. Crear la colección.** En el panel izquierdo, `Collections` → `+` → *New collection*, con el nombre `Empleados`. Guarda dentro todas las peticiones.

**2. Configurar Basic Auth en la colección.** Haz clic en el **nombre de la colección** → pestaña **Authorization**:

```text
Auth type: Basic Auth
Username:  admin
Password:  admin
```

**3. Heredar la autenticación en cada petición.** En cada petición de la colección, pestaña **Authorization** → Auth type: **Inherit auth from parent**. Debe aparecer *Basic Auth* con la etiqueta *Inherited*.

Con el usuario `user` se pueden realizar las operaciones de consulta, pero `POST`, `PUT` y `DELETE` requieren el rol `ADMIN`.

### 5.2. Obtener el token CSRF

Se realiza:

```http
GET http://localhost:8080/csrf
```

con Basic Auth `admin` / `admin`.

La respuesta contiene el token:

```json
{
  "headerName": "X-CSRF-TOKEN",
  "parameterName": "_csrf",
  "token": "..."
}
```

Postman conserva automáticamente la cookie `JSESSIONID` obtenida en esta interacción. Puedes comprobarlo en el botón **Cookies** (bajo el botón Send): debe aparecer `JSESSIONID` para `localhost`.

### 5.3. Enviar el token en POST, PUT y DELETE

En las peticiones que modifican datos, pestaña **Headers**, se añade una fila:

| Key | Value |
|---|---|
| `X-CSRF-TOKEN` | el token copiado (o `{{csrfToken}}`, ver 5.4) |

Comprueba que la casilla de la fila está marcada. Las peticiones `GET` no necesitan esta cabecera.

No borres las cookies entre `GET /csrf` y la petición que modifica datos.

### 5.4. Automatizar el token con un script

Copiar el token a mano es incómodo. Se puede automatizar con un script a nivel de **colección**:

1. Haz clic en el **nombre de la colección**.
2. Pestaña **Scripts** → **Before request** (en versiones antiguas de Postman se llama *Pre-request Script*).
3. Pega el script y guarda con `Ctrl+S`.

```javascript
pm.sendRequest({
    url: "http://localhost:8080/csrf",
    method: "GET",
    header: { Authorization: "Basic " + btoa("admin:admin") }
}, (err, res) => {
    if (!err) {
        pm.collectionVariables.set("csrfToken", res.json().token);
    }
});
```

Si tu aplicación no usa el puerto 8080, cambia la URL.

El script se ejecuta antes de cada petición de la colección y guarda un token nuevo en la variable `csrfToken`. Después, en las peticiones `POST`, `PUT` y `DELETE` se utiliza la cabecera:

```text
X-CSRF-TOKEN: {{csrfToken}}
```

El flujo es:

```text
GET /csrf
    ↓
obtener token
    ↓
conservar JSESSIONID
    ↓
POST / PUT / DELETE
    ↓
X-CSRF-TOKEN: <token>
```

### 5.5. Configurar el body JSON

Para `POST` y `PUT`:

**Body → raw → JSON**

Ejemplo:

```json
{
  "nombre": "Pepe Fon"
}
```

Debe enviarse un único objeto JSON, no un array. No incluyas `id` en el `POST` si lo genera la base de datos.

### 5.6. Peticiones de ejemplo

| Petición | Autenticación | CSRF | Body | Resultado esperado |
|---|---|---|---|---|
| GET `/empleados` | Sí | No | — | `200 OK` |
| GET `/empleados/{id}` | Sí | No | — | `200 OK` o `404` |
| POST `/empleados` | `ADMIN` | Sí | JSON | `201 Created` |
| PUT `/empleados/{id}` | `ADMIN` | Sí | JSON | `2xx` (según el Controller) |
| DELETE `/empleados/{id}` | `ADMIN` | Sí | — | `204 No Content` |

### 5.7. Depurar con la consola de Postman

Para ver qué está enviando Postman realmente, abre **View → Show Postman Console**.

En una petición `POST`, `PUT` o `DELETE` debe aparecer primero la llamada a `/csrf` y después la petición con la cabecera `X-CSRF-TOKEN` rellena y `Cookie: JSESSIONID=...`.

Si el script no funciona, usa el procedimiento manual (5.2 y 5.3): es el más fácil de depurar.

### 5.8. Errores frecuentes en Postman

| Síntoma | Causa probable | Solución |
|---|---|---|
| `200` con HTML ("Please sign in") | La petición no está autenticada y Spring redirige a `/login` | Configurar Basic Auth (y comprobar que `httpBasic` está en `SecurityConfig`) |
| `401 Unauthorized` | Credenciales incorrectas o ausentes | Revisar usuario y contraseña en Authorization |
| `403 Forbidden` en POST/PUT/DELETE | Falta el token CSRF o no corresponde a la sesión | Obtener `/csrf` desde Postman y mantener `JSESSIONID` |
| `403 Forbidden` con `user` | No tiene `ROLE_ADMIN` | Utilizar `admin` / `admin` |
| `400 Bad Request` en POST/PUT | Body incorrecto: array `[ {...} ]` en lugar de un objeto, o campos que no coinciden | Enviar un único objeto JSON con los mismos nombres de campo que devuelve el `GET` |
| `415 Unsupported Media Type` | El body no se envía como JSON | Seleccionar **Body → raw → JSON** |

---

## 6. Interfaz web con Thymeleaf

Además de la API REST, el proyecto incluye una interfaz web basada en Thymeleaf, accesible en:

```text
http://localhost:8080/web/empleados
```

Al acceder sin haber iniciado sesión, Spring Security redirige al formulario `/login`.

Tras autenticarse, se accede a `/web/empleados`.

### 6.1. Seguridad en Thymeleaf

La interfaz utiliza:

```text
thymeleaf-extras-springsecurity6
```

Esta extensión permite utilizar atributos `sec:*` dentro de las plantillas Thymeleaf.

Por ejemplo:

```html
sec:authorize="isAuthenticated()"
```

permite mostrar contenido únicamente a usuarios autenticados.

También se utiliza:

```html
sec:authorize="isAnonymous()"
```

para mostrar contenido únicamente a usuarios que no han iniciado sesión.

Para controlar las opciones disponibles para los administradores se utiliza:

```html
sec:authorize="hasRole('ADMIN')"
```

Por ejemplo, los botones **Nuevo**, **Editar** y **Borrar** únicamente se muestran a los usuarios con `ROLE_ADMIN`.

Para mostrar el nombre del usuario autenticado se utiliza:

```html
sec:authentication="name"
```

La interfaz utiliza también:

```html
th:action="@{/logout}"
```

que genera el `action` del formulario HTML utilizado para cerrar la sesión. Además, cuando Spring Security está activo, Thymeleaf añade automáticamente el token CSRF al formulario como campo oculto.

### 6.2. Ocultar botones no sustituye a la seguridad

Es importante distinguir entre **ocultar elementos en la interfaz** y **proteger realmente las operaciones**.

Los atributos `sec:authorize` controlan qué elementos se muestran al usuario, pero la protección real de la API se realiza mediante `SecurityConfig`.

Por tanto, aunque un usuario no vea los botones de administración, Spring Security sigue impidiendo las operaciones `POST`, `PUT` y `DELETE` si no dispone del rol necesario.

### 6.3. CSRF en `fetch()`

Los formularios HTML tradicionales pueden trabajar con el token CSRF generado por Spring Security a través de Thymeleaf.

En este proyecto, sin embargo, las operaciones de crear, modificar y eliminar se realizan mediante JavaScript `fetch()` contra la API REST.

Por tanto, JavaScript obtiene previamente el token mediante `GET /csrf` y lo envía en la cabecera `X-CSRF-TOKEN`.

La función utilizada es:

```javascript
async function obtenerCsrf() {

    const response = await fetch("/csrf");

    if (!response.ok) {
        throw new Error(
            "No se pudo obtener el token CSRF"
        );
    }

    return response.json();
}
```

#### DELETE desde la interfaz web

Antes de ejecutar el `DELETE` se obtiene el token:

```javascript
const csrf = await obtenerCsrf();
```

y después se envía en la cabecera:

```javascript
const response = await fetch(
    "/empleados/" + idEmpleadoEliminar,
    {
        method: "DELETE",
        headers: {
            [csrf.headerName]: csrf.token
        }
    }
);
```

#### PUT desde la interfaz web

El mismo patrón se utiliza para `PUT`:

```javascript
const csrf = await obtenerCsrf();

const response = await fetch(
    "/empleados/" + idEmpleado,
    {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            [csrf.headerName]: csrf.token
        },
        body: JSON.stringify(empleado)
    }
);
```

#### POST desde la interfaz web

El `POST` sigue el mismo mecanismo:

```javascript
const csrf = await obtenerCsrf();

const response = await fetch("/empleados", {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
        [csrf.headerName]: csrf.token
    },
    body: JSON.stringify(empleado)
});
```

El patrón general es:

```text
Interfaz Thymeleaf
       ↓
JavaScript fetch()
       ↓
GET /csrf
       ↓
obtener token
       ↓
POST / PUT / DELETE
       ↓
X-CSRF-TOKEN
       ↓
Spring Security
       ↓
Controller
```

### 6.4. Los dos accesos utilizan las mismas capas

La aplicación tiene dos entradas principales:

#### API REST

```text
Postman / navegador
        ↓
EmpleadosController
        ↓
EmpleadosService
        ↓
EmpleadoRepository
```

#### Interfaz web

```text
Navegador
    ↓
EmpleadosWebController
    ↓
EmpleadosService
    ↓
EmpleadoRepository
```

En ambos casos la lógica de aplicación termina ejecutándose en la misma capa `Service`.

---

## 7. Arquitectura actual

La arquitectura por capas actual del proyecto es:

```text
                           Spring Security
                                  ↓
                       SecurityFilterChain
                                  ↓
                 ┌───────────────────────────┐
                 │                           │
                 ▼                           ▼
        EmpleadosController       EmpleadosWebController
                 │                           │
                 └─────────────┬─────────────┘
                               ▼
                         EmpleadosService
                               ↓
                       EmpleadoRepository
                               ↓
                         JPA / Hibernate
                               ↓
                           PostgreSQL
                               ↓
                          Docker Volume
```

Spring Security se encuentra delante de los Controllers y actúa como filtro de las peticiones HTTP.

Antes de que una petición llegue al Controller, Spring Security comprueba las reglas de autenticación, autorización y CSRF.

La interfaz web y la API REST terminan utilizando la misma capa `Service`.

La principal característica de esta arquitectura es que las responsabilidades se organizan en capas claramente diferenciadas:

- **Controller:** recibe peticiones HTTP y devuelve respuestas.
- **Service:** contiene la lógica de aplicación.
- **Repository:** proporciona acceso a los datos.
- **JPA / Hibernate:** realiza la persistencia ORM.
- **PostgreSQL:** almacena los datos.
- **Docker:** proporciona el entorno de ejecución de la base de datos y, cuando corresponde, de la aplicación.

A diferencia de la arquitectura hexagonal utilizada en el otro proyecto de repaso, aquí las dependencias siguen directamente la estructura de capas tradicional:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Base de datos
```

Esta estructura es más sencilla y directa, y resulta adecuada para utilizar el proyecto como referencia de Spring Boot por capas.

Los datos de PostgreSQL se almacenan en un volumen Docker, por lo que los registros se mantienen aunque los contenedores se reinicien o se vuelvan a crear. Los escenarios de ejecución (Docker o Eclipse) se describen en [3.5](#35-spring-boot-y-postgresql-en-docker) y [3.5.1](#351-perfiles-de-configuración-eclipse-y-docker).

---

## 8. Evolución global del proyecto

La evolución completa puede resumirse de la siguiente forma:

```text
Spring Boot básico
        ↓
API REST
        ↓
Service
        ↓
JPA / Hibernate
        ↓
H2
        ↓
PostgreSQL
        ↓
Docker
        ↓
Docker Compose
        ↓
Spring Profiles
        ↓
Ejecución Eclipse / Docker
        ↓
Spring Security
        ↓
Form Login
        ↓
HTTP Basic
        ↓
Roles USER / ADMIN
        ↓
CSRF
        ↓
GET /csrf
        ↓
Postman
        ↓
Thymeleaf + Spring Security
        ↓
fetch() + CSRF
```

---

## 9. Objetivo del proyecto

El objetivo es disponer de una aplicación pequeña pero suficientemente completa para utilizarla como **proyecto de repaso de Spring Boot**.

El proyecto permite revisar de forma práctica:

- Spring Boot
- Controllers
- Services
- Repositories
- REST
- DTOs
- JPA / Hibernate
- H2
- PostgreSQL
- Docker
- Docker Compose
- Spring Profiles
- Spring Security
- Autenticación
- Autorización
- Roles `USER` y `ADMIN`
- Form Login
- HTTP Basic
- Protección CSRF
- Postman
- Thymeleaf
- `thymeleaf-extras-springsecurity6`
- JavaScript `fetch()`

La idea es mantener una aplicación de tamaño reducido, pero suficientemente completa para poder volver a ella después de un tiempo y recuperar rápidamente cómo encajan las diferentes piezas de Spring Boot.
