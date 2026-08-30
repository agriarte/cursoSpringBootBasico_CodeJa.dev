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

| Método | Endpoint          | Operación |
| ------ | ----------------- | --------- |
| GET    | `/empleados`      | Listar    |
| GET    | `/empleados/{id}` | Consultar |
| POST   | `/empleados`      | Crear     |
| PUT    | `/empleados/{id}` | Modificar |
| DELETE | `/empleados/{id}` | Eliminar  |

En el proyecto inicial, los datos se almacenan en memoria mediante `ArrayList`.

### Interfaz web

| Método | Endpoint                     | Operación             |
| ------ | ----------------------------- | --------------------- |
| GET    | `/web/empleados`             | Mostrar lista         |
| GET    | `/web/empleados/nuevo`       | Formulario de alta    |
| GET    | `/web/empleados/editar/{id}` | Formulario de edición |

La interfaz utiliza:

* Thymeleaf
* Bootstrap
* JavaScript `fetch()`
* Formularios para crear y modificar
* Botones para editar y eliminar
* Modal de confirmación para eliminar

## 3. Evolución del Rest Básico

El proyecto evoluciona progresivamente desde una implementación sencilla en memoria hasta una aplicación con persistencia en una base de datos real y ejecución mediante Docker.

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

## 4. Arquitectura actual

La arquitectura actual del proyecto es:

```text
Navegador
    ↓
Spring Boot
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
