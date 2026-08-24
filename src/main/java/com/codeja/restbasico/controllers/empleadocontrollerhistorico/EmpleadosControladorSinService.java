

/*
 * Primera versión del controlador, autónoma y sin Service ni interfaz web.
 *
 * Se conserva con fines didácticos para consultar diferentes formas
 * de obtener y manipular los datos desde un controlador.
 *
 * Los comentarios explican el funcionamiento de:
 * - GET
 * - POST
 * - PUT
 * - DELETE
 * - Stream
 * - Optional
 * - ResponseEntity
 * - ArrayList
 * - removeIf()
 *
 * La versión actual es EmpleadosControlador, que utiliza
 * EmpleadosService y posteriormente se integrará con la interfaz web.
 */





package com.codeja.restbasico.controllers.empleadocontrollerhistorico;

import java.util.ArrayList;
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



/*
 * 
 * En los ejemplos básicos devolvíamos directamente objetos y listas. Ahora 
 * damos un paso más y utilizamos ResponseEntity para controlar de forma 
 * explícita la respuesta HTTP.
 * ResponseEntity es una clase de Spring que representa la respuesta HTTP
 * completa: cuerpo, encabezados y código de estado.
 *
 * Utilizar ResponseEntity permite tener un mayor control sobre lo que
 * nuestra API REST devuelve al cliente.
 *
 * En situaciones simples, podemos devolver directamente un objeto o una
 * lista y Spring los convertirá automáticamente a JSON.
 *
 * Sin embargo, ResponseEntity es útil cuando necesitamos:
 *
 * - Personalizar el código de estado HTTP (200, 404, 500, etc.).
 * - Añadir encabezados HTTP.
 * - Devolver un cuerpo vacío o diferente según la lógica de la aplicación.
 */

/*
 * @RequestMapping("/empleados") define una ruta base común
 * para todos los endpoints de este controlador.
 *
 * Por tanto:
 *
 * @GetMapping("/test")
 *     → GET /empleados/test
 *
 * @GetMapping("/lista")
 *     → GET /empleados/lista
 *
 *
 * Al utilizar @RestController, los objetos devueltos por los métodos
 * se convierten automáticamente a JSON.
 */


// **** ANOTACIONES COMENTATADAS PARA INHABILITARLAS **** //
//@RestController
//@RequestMapping("/empleados")
public class EmpleadosControladorSinService {
	
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
	

 // Lista utilizada como fuente de datos para este ejemplo.
    //
    // List.of() crea una lista no modificable.
    //
    // new ArrayList<>(Collection) crea una NUEVA lista ArrayList
    // copiando los elementos de la Collection recibida.
    // La nueva lista sí es modificable, aunque la lista original
    // creada con List.of() no lo sea.
    //
    // Por tanto:
    //
    // List.of(...)
    //         → lista no modificable
    //
    // new ArrayList<>(List.of(...))
    //         → nueva lista modificable
    //
    // final evita que la referencia "empleados" pueda apuntar
    // posteriormente a otra lista. No impide modificar el contenido
    // de la ArrayList.
    //
    // Ejemplo:
    // empleados.add(...)       → permitido
    // empleados = otraLista    → no permitido
    //
    private final List<EmpleadoDTO> empleados = new ArrayList<>(
            List.of(
            new EmpleadoDTO(1, "Juan"),
            new EmpleadoDTO(2, "Maria"),
            new EmpleadoDTO(3, "José"),
            new EmpleadoDTO(4, "Ricardo"),
            new EmpleadoDTO(5, "Noemí")
            )
    );
    
    
    
    // La ruta completa será: /empleados
    //
    // @GetMapping sin una ruta adicional utiliza la ruta base
    // definida en @RequestMapping("/empleados").
    //
    // Devuelve la lista completa de empleados.
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
        return empleados;
    }
    
    
    

    /*
     * ============================================================
     * Obtener un elemento concreto
     * VERSIÓN 1: for-each + ResponseEntity
     * ============================================================
     *
     * La ruta completa será:
     * /empleados/{idEmpleado}
     *
     * @PathVariable recoge el valor de {idEmpleado} de la URL.
     * Ejemplo:
     * /empleados/3
     *
     * El for-each recorre la lista y busca un empleado cuyo
     * ID coincida con el ID recibido.
     *
     * Si lo encuentra:
     *     → devuelve HTTP 200 OK con el EmpleadoDTO.
     *
     * Si no lo encuentra:
     *     → devuelve HTTP 404 NOT FOUND.
     *
     * Esta versión no utiliza Optional.
     *
     *
     * @GetMapping("/{idEmpleado}")
     * public ResponseEntity<EmpleadoDTO> empleadoByID(@PathVariable long idEmpleado) {
     *
     *     for (EmpleadoDTO empleadoDTO : empleados) {
     *         if (empleadoDTO.id() == idEmpleado) {
     *             return ResponseEntity.ok(empleadoDTO);
     *         }
     *     }
     *
     *     return ResponseEntity.notFound().build();
     * }
     *
     *
     * ============================================================
     * VERSIÓN 2: Stream + Optional
     * ============================================================
     *
     * Stream permite expresar la búsqueda de forma más funcional.
     *
     * stream()
     *     → crea un Stream a partir de la lista.
     *
     * filter()
     *     → conserva únicamente los empleados cuyo ID coincide.
     *
     * findFirst()
     *     → obtiene el primer empleado encontrado.
     *     → devuelve un Optional<EmpleadoDTO>.
     *
     * orElse(null)
     *     → obtiene el EmpleadoDTO del Optional.
     *     → si el Optional está vacío, devuelve null.
     *
     * Esta versión es más sencilla, pero no permite expresar
     * explícitamente un HTTP 404 mediante ResponseEntity.
     *
     *
     * @GetMapping("/{idEmpleado}")
     * public EmpleadoDTO empleadoByID(@PathVariable long idEmpleado) {
     *
     *     return empleados.stream()
     *             .filter(e -> e.id() == idEmpleado)
     *             .findFirst()
     *             .orElse(null);
     * }
     *
     *
     * ============================================================
     * VERSIÓN 3: Stream + Optional + ResponseEntity
     * ============================================================
     *
     * Esta versión permite controlar explícitamente los dos
     * posibles resultados de la petición HTTP:
     *
     *     Empleado encontrado
     *         → HTTP 200 OK + EmpleadoDTO
     *
     *     Empleado no encontrado
     *         → HTTP 404 NOT FOUND
     *
     *
     * El Optional aparece como resultado intermedio de findFirst().
     * No es el tipo que finalmente devuelve el método.
     *
     *
     * stream()
     *     ↓
     * Stream<EmpleadoDTO>
     *
     * filter()
     *     ↓
     * Stream<EmpleadoDTO>
     *
     * findFirst()
     *     ↓
     * Optional<EmpleadoDTO>
     *
     * map(ResponseEntity::ok)
     *     ↓
     * Optional<ResponseEntity<EmpleadoDTO>>
     *
     * orElseGet(...)
     *     ↓
     * ResponseEntity<EmpleadoDTO>
     *
     * Por tanto, el tipo final coincide con el tipo declarado
     * en el método:
     *
     * ResponseEntity<EmpleadoDTO>
     *
     */

    // Versión utilizando Stream, Optional y ResponseEntity.
    //
    // Permite devolver:
    //         → HTTP 200 OK si se encuentra el empleado.
    //         → HTTP 404 NOT FOUND si no se encuentra.

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> empleadoByID(@PathVariable long idEmpleado) {

        // stream() crea un Stream a partir de la lista.
        return empleados.stream()

                // filter() conserva únicamente los empleados cuyo ID
                // coincide con el ID recibido en la URL.
                .filter(e -> e.id() == idEmpleado)

                // findFirst() obtiene el primer empleado que ha pasado
                // el filtro.
                //
                // Devuelve un Optional<EmpleadoDTO>.
                .findFirst()

                // Si existe un empleado, map() transforma:
                //
                // EmpleadoDTO
                //     ↓
                // ResponseEntity<EmpleadoDTO>
                //
                // El resultado sigue estando dentro de un Optional:
                //
                // Optional<ResponseEntity<EmpleadoDTO>>
                .map(ResponseEntity::ok)

                // Si el Optional está vacío, significa que no se encontró
                // ningún empleado.
                //
                // En ese caso se devuelve HTTP 404 NOT FOUND sin cuerpo.
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
     * El empleado recibido se añade a la lista mediante add().
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
     * 
     * NOTA: aunque también funcionaría si el return solo devolviera
     * ResponseEntity.ok(empleado), no sería lo más apropiado porque
     * devolvería el código HTTP 200 OK.
     *
     * Cuando una petición crea correctamente un nuevo recurso,
     * el código HTTP adecuado es 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<EmpleadoDTO> crearEmpleado(
            @RequestBody EmpleadoDTO empleado) {

        empleados.add(empleado);

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
     */

    
    /*
     * DELETE /empleados/3
     * 		  ↓
	 * removeIf()
	 * 		  ↓
	 * ¿Se eliminó?
   		   ↙          ↘
 		 sí            no
 		 ↓              ↓
		204            404
		NO CONTENT     NOT FOUND
     */
    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminarEmpleado(
            @PathVariable long idEmpleado) {

    	
    	// Recorre la colección y elimina los elementos que cumplen la condición
        boolean eliminado = empleados.removeIf(
                empleado -> empleado.id() == idEmpleado);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
    
    
    /* otra forma de DELETE mediante streams, tal como se ve en la teoría del curso
     * en el capítulo PostMapping RequestMapping
		@DeleteMapping("/{id}")
		public ResponseEntity eliminarUsuario(@PathVariable Long id) {
    	Usuario usuarioExistente = usuarios.stream()
            	.filter(u -> u.getId().equals(id))
            	.findFirst()
            	.orElse(null);
    	if (usuarioExistente != null) {
        	usuarios.remove(usuarioExistente);
        	// 204 No Content 
        	return ResponseEntity.noContent().build();
    	} else {
        	// 404 Not Found si no existe 
        	return ResponseEntity.notFound().build();
    		}
		}
     */
    
    
    /*
		PUT /empleados/{idEmpleado}
				↓
		buscar empleado
				↓
			  ¿existe?
			  ↙       ↘
			sí         no
			↓           ↓
		modificar   404
			↓
		  200 OK    
 */
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
     * Stream + filter() buscan el empleado cuyo ID coincide.
     *
     * findFirst() devuelve un Optional<EmpleadoDTO> porque el
     * empleado puede existir o no.
     *
     * Si se encuentra:
     *     → se sustituye el empleado en la lista.
     *     → HTTP 200 OK.
     *
     * Si no se encuentra:
     *     → HTTP 404 NOT FOUND.
     */
    	/*
		PUT /empleados/{idEmpleado}
				↓
		buscar empleado
				↓
			  ¿existe?
			  ↙       ↘
			sí         no
			↓           ↓
		modificar   404
			↓
		  200 OK    
     */
    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoDTO> modificarEmpleado(
            @PathVariable long idEmpleado,
            @RequestBody EmpleadoDTO empleadoModificado) {

        for (int i = 0; i < empleados.size(); i++) {

            if (empleados.get(i).id() == idEmpleado) {

                empleados.set(i, empleadoModificado);

                return ResponseEntity.ok(empleadoModificado);
            }
        }

        return ResponseEntity.notFound().build();
    }
    
    
}