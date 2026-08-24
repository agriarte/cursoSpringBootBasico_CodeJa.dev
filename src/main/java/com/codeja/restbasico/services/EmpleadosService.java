package com.codeja.restbasico.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeja.restbasico.dto.EmpleadoDTO;

@Service
public class EmpleadosService {

    /*
     * ============================================================
     * Lista de empleados
     * ============================================================
     *
     * El Service mantiene la lista de empleados en memoria.
     *
     * Antes la lista estaba directamente en el Controller.
     * Al utilizar un Service, la lista queda centralizada en un
     * único lugar y puede ser utilizada tanto por el Controller
     * REST como por el Controller web.
     *
     * List.of() crea inicialmente una lista no modificable.
     *
     * new ArrayList<>(List.of(...)) crea una nueva ArrayList
     * modificable a partir de esos elementos.
     *
     * final evita que la referencia "empleados" pueda apuntar
     * posteriormente a otra lista, pero permite modificar el
     * contenido de la ArrayList.
     */
    private final List<EmpleadoDTO> empleados = new ArrayList<>(
            List.of(
                    new EmpleadoDTO(1, "Juan"),
                    new EmpleadoDTO(2, "Maria"),
                    new EmpleadoDTO(3, "José"),
                    new EmpleadoDTO(4, "Ricardo"),
                    new EmpleadoDTO(5, "Noemí")
            )
    );


    /*
     * ============================================================
     * Obtener todos los empleados
     * ============================================================
     *
     * Devuelve la lista completa de empleados.
     *
     * El Controller utiliza este método para obtener los datos
     * que necesita devolver al cliente o mostrar mediante
     * Thymeleaf.
     */
    public List<EmpleadoDTO> getEmpleados() {
        return empleados;
    }


    /*
     * ============================================================
     * Obtener un empleado por ID
     * ============================================================
     *
     * Busca el empleado cuyo ID coincide con el recibido.
     *
     * Stream:
     *     → recorre la lista.
     *
     * filter():
     *     → conserva únicamente los empleados cuyo ID coincide.
     *
     * findFirst():
     *     → obtiene el primer resultado.
     *     → devuelve Optional<EmpleadoDTO>.
     *
     * Optional permite representar que el empleado puede existir
     * o no existir.
     */
    public Optional<EmpleadoDTO> getEmpleadoById(long idEmpleado) {

        return empleados.stream()
                .filter(e -> e.id() == idEmpleado)
                .findFirst();
    }


    /*
     * ============================================================
     * Crear un empleado
     * ============================================================
     *
     * Añade el empleado recibido a la lista.
     *
     * add() modifica directamente la ArrayList.
     */
    public void crearEmpleado(EmpleadoDTO empleado) {
        empleados.add(empleado);
    }


    /*
     * ============================================================
     * Modificar un empleado
     * ============================================================
     *
     * Recorre la lista mediante un for clásico utilizando un índice.
     *
     * Si encuentra un empleado cuyo ID coincide:
     *     → set() sustituye el elemento de esa posición.
     *     → devuelve true.
     *
     * Si no encuentra ningún empleado:
     *     → devuelve false.
     */
    public boolean modificarEmpleado(
            long idEmpleado,
            EmpleadoDTO empleadoModificado) {

        for (int i = 0; i < empleados.size(); i++) {

            if (empleados.get(i).id() == idEmpleado) {

                empleados.set(i, empleadoModificado);

                return true;
            }
        }

        return false;
    }


    /*
     * ============================================================
     * Eliminar un empleado
     * ============================================================
     *
     * removeIf() pertenece a Collection y está disponible desde
     * Java 8.
     *
     * Recorre la colección y elimina los elementos que cumplen
     * la condición indicada.
     *
     * Modifica directamente la colección original.
     *
     * Devuelve:
     *     → true  si se ha eliminado algún elemento.
     *     → false si no se ha encontrado ningún elemento.
     */
    public boolean eliminarEmpleado(long idEmpleado) {

        return empleados.removeIf(
                empleado -> empleado.id() == idEmpleado);
    }
}