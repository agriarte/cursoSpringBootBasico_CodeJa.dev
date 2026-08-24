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

Ejemplo de API REST con un CRUD básico de empleados.

| Método | Endpoint | Operación |
|---|---|---|
| GET | `/empleados` | Listar |
| GET | `/empleados/{id}` | Consultar |
| POST | `/empleados` | Crear |
| PUT | `/empleados/{id}` | Modificar |
| DELETE | `/empleados/{id}` | Eliminar |

Los datos se almacenan actualmente en memoria mediante `ArrayList`.

## Próximo paso

Añadir una interfaz web sencilla con **Thymeleaf**.
