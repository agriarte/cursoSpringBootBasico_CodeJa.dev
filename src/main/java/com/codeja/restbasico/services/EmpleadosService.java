package com.codeja.restbasico.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codeja.restbasico.dto.EmpleadoDTO;


@Service
public class EmpleadosService {

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

    public List<EmpleadoDTO> getEmpleados() {
        return empleados;
    }
    
    public Optional<EmpleadoDTO> getEmpleadoById(long idEmpleado) {

        return empleados.stream()
                .filter(e -> e.id() == idEmpleado)
                .findFirst();
    }

    public void crearEmpleado(EmpleadoDTO empleado) {
        empleados.add(empleado);
    }

    public boolean eliminarEmpleado(long idEmpleado) {

        return empleados.removeIf(
                empleado -> empleado.id() == idEmpleado);
    }
    
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
}
