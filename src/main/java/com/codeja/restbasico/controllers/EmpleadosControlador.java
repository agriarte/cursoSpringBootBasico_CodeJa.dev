package com.codeja.restbasico.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeja.restbasico.dto.EmpleadoDTO;
import com.codeja.restbasico.services.EmpleadosService;


@RestController
@RequestMapping("/empleados")
public class EmpleadosControlador {
	
	// -------------------- TEST -----------------------------
    // @GetMapping define un endpoint HTTP de tipo GET.
    // La ruta completa será: /empleados/test
    //
    // Spring convierte automáticamente el EmpleadoDTO a JSON.
    @GetMapping("/test")
    public EmpleadoDTO getNuevoEmpleado() {
        return new EmpleadoDTO(123, "Pedro");
    }
	// --------------------------------------------------------	
	


    private final EmpleadosService empleadosService;

    public EmpleadosControlador(EmpleadosService empleadosService) {
        this.empleadosService = empleadosService;
    }
    
    
    
    // La ruta completa será: /empleados
    //
    // @GetMapping sin una ruta adicional utiliza la ruta base
    // definida en @RequestMapping("/empleados").
    //
    // Devuelve la lista completa de empleados.
    //
    // La lista es obtenida mediante EmpleadosService.
    // El controlador no accede directamente a la colección.
    //
    // No necesita Stream ni Optional:
    // - No estamos filtrando ni transformando los elementos.
    // - La lista existe aunque esté vacía.
    // - Si no hay empleados, devuelve [] en JSON.
    //
    // Spring convierte automáticamente la List<EmpleadoDTO>
    // en un array JSON.
    @GetMapping
    public List<EmpleadoDTO> getLista() {
        return empleadosService.getEmpleados();
    }
    
    
    /*
     * ============================================================
     * Obtener un empleado concreto
     * ============================================================
     *
     * La ruta completa será:
     * GET /empleados/{idEmpleado}
     *
     * @PathVariable recoge el valor de {idEmpleado} de la URL.
     * Ejemplo:
     * GET /empleados/3
     *
     * El controlador delega la búsqueda en EmpleadosService.
     *
     * El Service devuelve un Optional<EmpleadoDTO> porque el
     * empleado puede existir o no.
     *
     * Si el Optional contiene un empleado:
     *     → map() lo transforma en ResponseEntity<EmpleadoDTO>.
     *     → devuelve HTTP 200 OK con el empleado.
     *
     * Si el Optional está vacío:
     *     → orElseGet() devuelve HTTP 404 NOT FOUND.
     *
     * El Optional se utiliza como resultado intermedio.
     * El tipo que finalmente devuelve el método es:
     *
     * ResponseEntity<EmpleadoDTO>
     */
    
    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> empleadoByID(
            @PathVariable long idEmpleado) {

        return empleadosService.getEmpleadoById(idEmpleado)

                // Si existe el empleado, devuelve HTTP 200 OK.
                .map(ResponseEntity::ok)

                // Si no existe, devuelve HTTP 404 NOT FOUND.
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    /*
     * ============================================================
     * Crear un nuevo empleado
     * ============================================================
     *
     * La ruta completa será:
     * POST /empleados
     *
     * @PostMapping sin una ruta adicional utiliza la ruta base
     * definida en @RequestMapping("/empleados").
     *
     * @RequestBody recibe los datos enviados en el cuerpo de la
     * petición HTTP.
     *
     * Spring convierte automáticamente el JSON recibido en un
     * objeto EmpleadoDTO.
     *
     * Ejemplo de JSON recibido:
     *
     * {
     *     "id": 6,
     *     "nombre": "Pedro"
     * }
     *
     * El controlador delega la creación del empleado en
     * EmpleadosService.
     *
     * ResponseEntity permite indicar explícitamente el estado
     * HTTP de la respuesta.
     *
     * HTTP 201 CREATED indica que se ha creado correctamente
     * un nuevo recurso.
     *
     * El EmpleadoDTO creado se devuelve en el cuerpo de la
     * respuesta y Spring lo convierte automáticamente a JSON.
     *
     * NOTA: aunque también funcionaría devolver directamente
     * ResponseEntity.ok(empleado), no sería lo más apropiado
     * porque devolvería HTTP 200 OK.
     *
     * Cuando una petición crea correctamente un nuevo recurso,
     * el código HTTP adecuado es 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<EmpleadoDTO> crearEmpleado(
            @RequestBody EmpleadoDTO empleado) {

        empleadosService.crearEmpleado(empleado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empleado);
    }
    
    
    
    /*
     * ============================================================
     * Eliminar un empleado
     * ============================================================
     *
     * La ruta completa será:
     * DELETE /empleados/{idEmpleado}
     *
     * @PathVariable recoge el valor de {idEmpleado} de la URL.
     * Ejemplo:
     * DELETE /empleados/3
     *
     * El controlador delega la eliminación en EmpleadosService.
     *
     * En el Service se utiliza removeIf().
     *
     * removeIf() pertenece a la interfaz Collection y está disponible
     * desde Java 8.
     *
     * Recorre la colección y elimina los elementos que cumplen
     * la condición indicada.
     *
     * A diferencia de las operaciones habituales de Stream,
     * removeIf() modifica directamente la colección original.
     *
     * Devuelve:
     *     → true  si se ha eliminado algún elemento.
     *     → false si no se ha encontrado ningún elemento.
     *
     * Si se elimina correctamente:
     *     → HTTP 204 NO CONTENT.
     *
     * Si no existe el empleado:
     *     → HTTP 404 NOT FOUND.
     *
     *
     * DELETE /empleados/3
     *          ↓
     * EmpleadosService
     *          ↓
     *       removeIf()
     *          ↓
     *     ¿Se eliminó?
     *        ↙      ↘
     *      sí        no
     *      ↓          ↓
     *     204        404
     * NO CONTENT   NOT FOUND
     */

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminarEmpleado(
            @PathVariable long idEmpleado) {

        boolean eliminado = empleadosService.eliminarEmpleado(idEmpleado);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
    
    
    /*
     * ============================================================
     * Modificar un empleado
     * ============================================================
     *
     * La ruta completa será:
     * PUT /empleados/{idEmpleado}
     *
     * @PathVariable recoge el ID del empleado que queremos modificar.
     *
     * @RequestBody recibe los nuevos datos del empleado.
     *
     * Ejemplo:
     *
     * PUT /empleados/3
     *
     * {
     *     "id": 3,
     *     "nombre": "José García"
     * }
     *
     * El controlador delega la modificación en EmpleadosService.
     *
     * En el Service se recorre la lista mediante un for clásico
     * utilizando un índice.
     *
     * Si se encuentra un empleado cuyo ID coincide:
     *     → set() sustituye el elemento de esa posición.
     *     → devuelve HTTP 200 OK.
     *
     * Si no se encuentra:
     *     → devuelve HTTP 404 NOT FOUND.
     *
     * Se utiliza un for con índice porque necesitamos conocer
     * la posición del elemento para poder utilizar set().
     *
     *
     * PUT /empleados/3
     *          ↓
     * EmpleadosService
     *          ↓
     *      buscar ID
     *          ↓
     *      ¿existe?
     *       ↙      ↘
     *      sí       no
     *      ↓         ↓
     *    set()      404
     *      ↓
     *    200 OK
     */

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> modificarEmpleado(
            @PathVariable long idEmpleado,
            @RequestBody EmpleadoDTO empleadoModificado) {

        if (empleadosService.modificarEmpleado(
                idEmpleado, empleadoModificado)) {

            return ResponseEntity.ok(empleadoModificado);
        }

        return ResponseEntity.notFound().build();
    }
    
}



