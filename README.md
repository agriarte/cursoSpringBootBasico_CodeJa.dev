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

Inicialmente se utiliza una `ArrayList` para almacenar los empleados y realizar las operaciones básicas del CRUD.

Las operaciones pueden realizarse tanto mediante los endpoints de la API REST como desde la interfaz web.

### Persistencia con H2

En la etapa actual se sustituye la `ArrayList` por una base de datos H2 en memoria, utilizando JPA y Spring Data JPA.

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
