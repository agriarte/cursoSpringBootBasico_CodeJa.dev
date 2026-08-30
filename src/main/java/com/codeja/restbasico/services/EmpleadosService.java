package com.codeja.restbasico.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeja.restbasico.dto.EmpleadoDTO;
import com.codeja.restbasico.entities.Empleado;
import com.codeja.restbasico.repositories.EmpleadoRepository;
@Service
public class EmpleadosService {
	
	private final EmpleadoRepository empleadoRepository;
	
	public EmpleadosService (EmpleadoRepository empleadoRepository) {
		this.empleadoRepository = empleadoRepository;
	}
	
	

	 /*
	  * ============================================================
	  * Obtener todos los empleados
	  * ============================================================
	  *
	  * El Repository obtiene de la BBDD todas las entidades
	  * Empleado mediante findAll().
	  *
	  * Como el Controller trabaja con EmpleadoDTO y no directamente
	  * con la Entity, el Service convierte cada Empleado en un DTO.
	  *
	  * Repository:
	  *     → List<Empleado>
	  *
	  * Service:
	  *     → List<EmpleadoDTO>
	  */
	 public List<EmpleadoDTO> getEmpleados() {

	     return empleadoRepository.findAll()
	             .stream()
	             .map(empleado -> new EmpleadoDTO(
	                     empleado.getId(),
	                     empleado.getNombre()
	             ))
	             .toList();
	 }


	 /*
	  * ============================================================
	  * Obtener un empleado por ID
	  * ============================================================
	  *
	  * Busca en la BBDD el empleado cuyo ID coincide con el recibido.
	  *
	  * El Repository proporciona el método findById(), que realiza
	  * la búsqueda utilizando la clave primaria de la entidad.
	  *
	  * findById():
	  *     → recibe el ID del empleado.
	  *     → devuelve Optional<Empleado>.
	  *
	  * Optional permite representar que el empleado puede existir
	  * o no existir.
	  *
	  * Como el Controller trabaja con EmpleadoDTO, el Service convierte
	  * la Entity encontrada en un EmpleadoDTO.
	  */
	 public Optional<EmpleadoDTO> getEmpleadoById(long idEmpleado) {

	     return empleadoRepository.findById(idEmpleado)
	             .map(empleado -> new EmpleadoDTO(
	                     empleado.getId(),
	                     empleado.getNombre()
	             ));
	 }

	 /*
	  * ============================================================
	  * Crear un empleado
	  * ============================================================
	  *
	  * El Controller recibe un EmpleadoDTO.
	  *
	  * Como el Repository trabaja con la Entity Empleado, el Service
	  * convierte el DTO en una Entity antes de guardarla.
	  *
	  * save():
	  *     → guarda la Entity en la BBDD.
	  *     → como el ID es generado automáticamente, no necesitamos
	  *       proporcionar el ID al crear el empleado.
	  *
	  * H2 genera el nuevo identificador.
	  */
	 public EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDTO) {

	     Empleado empleado = new Empleado(
	             null,
	             empleadoDTO.nombre()
	     );

	     Empleado empleadoGuardado =
	             empleadoRepository.save(empleado);

	     return new EmpleadoDTO(
	             empleadoGuardado.getId(),
	             empleadoGuardado.getNombre()
	     );
	 }

	 /*
	  * ============================================================
	  * Modificar un empleado
	  * ============================================================
	  *
	  * Busca en la BBDD el empleado cuyo ID coincide con el recibido.
	  *
	  * Si existe:
	  *     → se modifican sus datos.
	  *     → save() guarda los cambios en la BBDD.
	  *     → devuelve true.
	  *
	  * Si no existe:
	  *     → devuelve false.
	  *
	  * findById() devuelve Optional<Empleado> porque el empleado
	  * puede existir o no existir.
	  */
	 public boolean modificarEmpleado(
	         long idEmpleado,
	         EmpleadoDTO empleadoModificado) {

	     Optional<Empleado> empleado =
	             empleadoRepository.findById(idEmpleado);

	     if (empleado.isEmpty()) {
	         return false;
	     }

	     Empleado empleadoExistente = empleado.get();

	     empleadoExistente.setNombre(
	             empleadoModificado.nombre()
	     );

	     empleadoRepository.save(empleadoExistente);

	     return true;
	 }


	    /*
	     * ============================================================
	     * Eliminar un empleado
	     * ============================================================
	     *
	     * Elimina de la BBDD el empleado cuyo ID coincide con el
	     * recibido.
	     *
	     * Antes, cuando los datos estaban almacenados en un
	     * ArrayList, utilizábamos removeIf() para recorrer la
	     * colección y localizar el empleado.
	     *
	     * Ahora el Repository se encarga de realizar la operación
	     * directamente sobre la BBDD mediante deleteById().
	     *
	     * Como necesitamos saber si el empleado existe antes de
	     * eliminarlo, primero utilizamos findById().
	     *
	     * Devuelve:
	     *     → true  si el empleado existe y se elimina.
	     *     → false si no existe.
	     */
	    public boolean eliminarEmpleado(long idEmpleado) {

	        if (empleadoRepository.existsById(idEmpleado)) {

	            empleadoRepository.deleteById(idEmpleado);

	            return true;
	        }

	        return false;
	    }
}