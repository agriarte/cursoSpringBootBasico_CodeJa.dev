# Repaso Spring Boot

Proyecto didáctico para repasar conceptos básicos de Spring Boot y Java basado en el curso **Spring Boot Básico** de [www.codeja.dev](https://www.codeja.dev/).

El proyecto contiene **dos aplicaciones Spring Boot independientes**.

## 1. springbootbasico

Ejemplos sencillos de conceptos y anotaciones de Spring:

* `@Component`
* `@Service`
* `@Repository`
* `@Autowired`
* Inyección de dependencias
* Beans

## 2. restbasico

Ejemplo de API REST con un CRUD básico de empleados y una interfaz web con **Thymeleaf + Bootstrap**.

### API REST

| Método | Endpoint | Operación |
| ------ | -------- | --------- |
| GET | `/empleados` | Listar |
| GET | `/empleados/{id}` | Consultar |
| POST | `/empleados` | Crear |
| PUT | `/empleados/{id}` | Modificar |
| DELETE | `/empleados/{id}` | Eliminar |

En el proyecto inicial, los datos se almacenan en memoria mediante `ArrayList`.

### Interfaz web

| Método | Endpoint | Operación |
| ------ | -------- | --------- |
| GET | `/web/empleados` | Mostrar lista |
| GET | `/web/empleados/nuevo` | Formulario de alta |
| GET | `/web/empleados/editar/{id}` | Formulario de edición |

La interfaz utiliza:

* Thymeleaf
* Bootstrap
* JavaScript `fetch()`
* Formularios para crear y modificar
* Botones para editar y eliminar
* Modal de confirmación para eliminar

## 3. Evolución del Rest Básico

El proyecto evoluciona progresivamente desde una implementación sencilla en memoria hasta una aplicación con persistencia en una base de datos real, ejecución mediante Docker y protección mediante Spring Security.

Cada etapa del desarrollo queda registrada en un commit, por lo que es posible recuperar el estado del proyecto correspondiente a cada etapa consultando y retrocediendo en el historial de Git.

### 3.1. Almacenamiento en memoria con ArrayList

Inicialmente se utiliza una `ArrayList` para almacenar los empleados y realizar las operaciones básicas del CRUD.

Las operaciones pueden realizarse tanto mediante los endpoints de la API REST como desde la interfaz web.

En esta etapa no existe persistencia en una base de datos. Los datos se pierden al reiniciar la aplicación.

### 3.2. Separación de la lógica mediante Service

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

### 3.3. Persistencia con H2

Posteriormente se sustituye la `ArrayList` por una base de datos **H2 en memoria**, utilizando **JPA y Spring Data JPA**.

Se incorporan:

* `@Entity`
* `Empleado`
* `EmpleadoDTO`
* `EmpleadoRepository`
* `JpaRepository`
* H2
* Hibernate

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

La siguiente evolución sustituye H2 por **PostgreSQL**, ejecutándose mediante un contenedor Docker.

Se incorpora un archivo `compose.yml` para crear y gestionar el contenedor de PostgreSQL utilizando la imagen:

```text
postgres:16-alpine
```

La base de datos utilizada por la aplicación es:

```text
empleados
```

La conexión desde Spring Boot se configura mediante `application.properties`, indicando:

* URL JDBC
* Puerto de PostgreSQL
* Base de datos
* Usuario
* Contraseña
* Configuración de Hibernate

Se sustituye la dependencia del driver de H2 por el driver JDBC de PostgreSQL.

La aplicación Spring Boot se construye mediante un Dockerfile dividido en dos etapas. La primera utiliza Maven y JDK 21 para compilar el proyecto y generar el JAR. La segunda utiliza únicamente JRE 21 para ejecutar la aplicación.

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

### 3.5. Spring Boot y PostgreSQL en Docker

En la etapa actual, tanto la aplicación **Spring Boot** como **PostgreSQL** se ejecutan mediante Docker Compose.

La aplicación Spring Boot se construye mediante un Dockerfile utilizando una construcción **multi-stage**, o dicho de otra manera, el Dockerfile está dividido en dos etapas.

La primera utiliza **Maven y JDK 21** para compilar el proyecto y generar el JAR.

La segunda utiliza únicamente **JRE 21** para ejecutar la aplicación.

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

Para iniciar ambos servicios:

```bash
docker compose up -d
```

Para reconstruir la imagen de Spring Boot después de realizar cambios:

```bash
docker compose up -d --build
```

Para comprobar el estado de los contenedores:

```bash
docker compose ps
```

La aplicación web queda disponible en:

```text
http://localhost:8080/web/empleados
```

### 3.6. Spring Security

Como siguiente evolución se incorpora **Spring Security** para proteger la aplicación y controlar el acceso a las diferentes operaciones del CRUD.

Se añade la dependencia:

```text
spring-boot-starter-security
```

La seguridad se configura mediante una clase `SecurityConfig`, donde se define un `SecurityFilterChain`.

La aplicación utiliza **form login** para la autenticación, mostrando la página de login estándar proporcionada por Spring Security.

Actualmente los usuarios se gestionan en memoria mediante `InMemoryUserDetailsManager`.

Se definen dos usuarios con diferentes roles:

```text
user
  ↓
ROLE_USER

admin
  ↓
ROLE_ADMIN
```

El acceso a las operaciones se controla mediante el método HTTP y el rol del usuario.

La configuración actual establece:

| Operación | Acceso |
| --------- | ------ |
| GET `/empleados` | Usuario autenticado |
| GET `/empleados/**` | Usuario autenticado |
| POST `/empleados` | `ADMIN` |
| PUT `/empleados/**` | `ADMIN` |
| DELETE `/empleados/**` | `ADMIN` |
| `/web/**` | Usuario autenticado |
| Resto de peticiones | Permitidas |

De esta forma se diferencia entre **autenticación** y **autorización**:

* **Autenticación:** determina quién es el usuario que ha iniciado sesión.
* **Autorización:** determina qué operaciones puede realizar ese usuario según sus roles.

Por ejemplo, un usuario con `ROLE_USER` puede consultar los empleados, pero no puede crear, modificar o eliminar empleados.

Un usuario con `ROLE_ADMIN` puede realizar además las operaciones de modificación del CRUD.

### Seguridad en la interfaz web

La interfaz Thymeleaf utiliza la extensión:

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

Por ejemplo, los botones **Nuevo**, **Editar** y **Borrar** únicamente se muestran a los usuarios que tienen el rol `ADMIN`.

Para mostrar el nombre del usuario autenticado se utiliza:

```html
sec:authentication="name"
```

La plantilla también utiliza:

```html
th:action="@{/logout}"
```

`th:action` es un atributo de Thymeleaf que permite generar el atributo `action` del formulario HTML.

En este caso el formulario realiza una petición `POST` a `/logout`, que es procesada por Spring Security para cerrar la sesión.

La configuración también establece una URL de redirección después del logout:

```text
/logout
   ↓
/web/empleados
```

Es importante distinguir entre **ocultar botones en la interfaz** y **proteger realmente las operaciones**.

Los atributos `sec:authorize` controlan qué elementos se muestran al usuario, pero la protección real de las operaciones se realiza mediante la configuración de Spring Security en `SecurityConfig`.

Por tanto, aunque un usuario no vea los botones de administración, Spring Security también impide que pueda ejecutar directamente las operaciones `POST`, `PUT` o `DELETE` si no dispone del rol necesario.

## 4. Arquitectura actual

La arquitectura actual del proyecto es:

```text
                         Spring Security
                              ↓
Navegador ───────────→ SecurityFilterChain
                              ↓
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
                       Docker Volume
```

Spring Security se encuentra delante de los Controllers y actúa como filtro de las peticiones HTTP.

Antes de que una petición llegue al Controller, Spring Security comprueba las reglas de seguridad configuradas.

La autenticación se realiza mediante **form login** y la autorización mediante los roles `USER` y `ADMIN`.

La interfaz web utiliza Thymeleaf junto con `thymeleaf-extras-springsecurity6` para adaptar la información mostrada al usuario autenticado.

Spring Boot y PostgreSQL se ejecutan actualmente en contenedores independientes gestionados mediante Docker Compose.

Los datos de PostgreSQL se almacenan en un volumen Docker, por lo que los registros se mantienen aunque los contenedores se reinicien o se vuelvan a crear.

## 5. Evolución de la persistencia

El proyecto ha evolucionado progresivamente desde un almacenamiento en memoria hasta una base de datos persistente ejecutándose junto con la aplicación dentro de Docker:

```text
ArrayList
   ↓
H2 en memoria
   ↓
PostgreSQL
   ↓
PostgreSQL + Spring Boot en Docker
```

Cada etapa representa una evolución del proyecto y permite conservar como referencia didáctica los diferentes conceptos incorporados durante el desarrollo.

## 6. Evolución global del proyecto

La evolución completa del proyecto puede resumirse de la siguiente forma:

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
Spring Security
        ↓
Form Login
        ↓
Roles USER / ADMIN
        ↓
Autorización de operaciones CRUD
        ↓
Integración Thymeleaf + Spring Security
```

El objetivo del proyecto es disponer de una aplicación pequeña pero suficientemente completa para utilizarla como **proyecto de repaso de Spring Boot**, permitiendo revisar de forma práctica diferentes conceptos y observar cómo evoluciona una aplicación desde un CRUD sencillo hasta una aplicación con persistencia, Docker y seguridad.