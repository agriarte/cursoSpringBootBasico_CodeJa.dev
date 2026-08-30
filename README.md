# Repaso Spring Boot

Proyecto didáctico para repasar conceptos básicos de Spring Boot y Java basado en el curso **Spring Boot Básico** de [www.codeja.dev](https://www.codeja.dev/).

El proyecto contiene **dos aplicaciones Spring Boot independientes**.

## 1. springbootbasico

Ejemplos sencillos de conceptos y anotaciones de Spring:

- `@Component`
- `@Service`
- `@Repository`
- `@Autowired`
- Inyección de dependencias
- Beans

## 2. restbasico

Ejemplo de API REST con un CRUD básico de empleados y una interfaz web con **Thymeleaf + Bootstrap**.

### API REST

| Método | Endpoint | Operación |
|---|---|---|
| GET | `/empleados` | Listar |
| GET | `/empleados/{id}` | Consultar |
| POST | `/empleados` | Crear |
| PUT | `/empleados/{id}` | Modificar |
| DELETE | `/empleados/{id}` | Eliminar |

En el proyecto inicial, los datos se almacenan en memoria mediante `ArrayList`.

### Interfaz web

| Método | Endpoint | Operación |
|---|---|---|
| GET | `/web/empleados` | Mostrar lista |
| GET | `/web/empleados/nuevo` | Formulario de alta |
| GET | `/web/empleados/editar/{id}` | Formulario de edición |

La interfaz utiliza:

- Thymeleaf
- Bootstrap
- JavaScript `fetch()`
- Formularios para crear y modificar
- Botones para editar y eliminar
- Modal de confirmación para eliminar

## 3. Evolución del Rest Básico

El proyecto evoluciona progresivamente desde una implementación sencilla en memoria hasta una aplicación con persistencia en una base de datos real.

###Etapa 1 — ArrayList

Inicialmente se utiliza una `ArrayList` para almacenar los empleados y realizar las operaciones básicas del CRUD.

Las operaciones pueden realizarse tanto mediante los endpoints de la API REST como desde la interfaz web.

En esta etapa no existe persistencia en una base de datos. Los datos se pierden al reiniciar la aplicación.

###Etapa 2 — Persistencia con H2

Posteriormente se sustituye la `ArrayList` por una base de datos **H2 en memoria**, utilizando **JPA y Spring Data JPA.**

Se incorporan:

- `@Entity`
- `Empleado`
- `EmpleadoDTO`
- `EmpleadoRepository`
- `JpaRepository`
- H2
- Hibernate

La arquitectura pasa a ser:


```
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

###Etapa 3 — Persistencia con PostgreSQL

La siguiente evolución sustituye H2 por **PostgreSQL**, ejecutándose mediante un contenedor Docker.

Se incorpora un archivo compose.yml para crear y gestionar el contenedor de PostgreSQL utilizando la imagen:

postgres:16-alpine

La base de datos utilizada por la aplicación es:

empleados

La conexión desde Spring Boot se configura mediante `application.properties`, indicando:

- URL JDBC
- Puerto de PostgreSQL
- Base de datos
- Usuario
- Contraseña
- Configuración de Hibernate

Se sustituye la dependencia del driver de H2 por el driver JDBC de PostgreSQL.

La arquitectura pasa a ser:

```
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
Hibernate continúa encargándose de adaptar la Entity Empleado a la estructura de la base de datos.

El ID continúa generándose automáticamente mediante:

`@GeneratedValue(strategy = GenerationType.IDENTITY)`

Para conservar los datos al reiniciar Spring Boot se utiliza:

`spring.jpa.hibernate.ddl-auto=update`

De esta forma Hibernate no elimina la tabla ni sus registros al reiniciar la aplicación.

Además, PostgreSQL utiliza un volumen Docker para conservar los datos aunque el contenedor se detenga y vuelva a crearse.

**Evolución de la persistencia**

```
ArrayList
   ↓
H2 en memoria
   ↓
PostgreSQL en Docker
   ↓
PostgreSQL persistente
```

**Próximo paso**

El siguiente desarrollo será ejecutar también la aplicación **Spring Boot dentro de Docker**, incorporando el nuevo servicio al compose.yml.

De esta forma, tanto la aplicación como PostgreSQL podrán ejecutarse conjuntamente mediante Docker Compose.
