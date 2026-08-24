package com.codeja.restbasico.controllers;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codeja.restbasico.dto.EmpleadoDTO;
import com.codeja.restbasico.services.EmpleadosService;


/*
 * ============================================================
 * Controlador para la interfaz web
 * ============================================================
 *
 * Este Controller se utiliza para las páginas web de la aplicación.
 *
 * A diferencia de @RestController, @Controller no devuelve
 * directamente objetos JSON.
 *
 * Sus métodos devuelven el nombre de una vista Thymeleaf.
 *
 * La ruta base será:
 *
 * /web/empleados
 *
 * La lógica de los empleados no está en este Controller.
 * Se delega en EmpleadosService.
 *
 * Tanto este Controller como EmpleadosControlador utilizan
 * el mismo EmpleadosService y, por tanto, trabajan sobre
 * la misma lista de empleados.
 */

@Controller
@RequestMapping("/web/empleados")
public class EmpleadosWebController {


    /*
     * EmpleadosService se inyecta mediante constructor.
     *
     * El Controller utiliza el Service para obtener y buscar
     * empleados, sin acceder directamente a la lista.
     */
    private final EmpleadosService empleadosService;

    public EmpleadosWebController(EmpleadosService empleadosService) {
        this.empleadosService = empleadosService;
    }


    /*
     * ============================================================
     * Mostrar lista de empleados
     * ============================================================
     *
     * La ruta completa será:
     * GET /web/empleados
     *
     * Obtiene la lista mediante EmpleadosService y la añade
     * al Model con el nombre "empleados".
     *
     * Thymeleaf utilizará ese nombre en la plantilla:
     *
     * ${empleados}
     *
     * return "empleados" indica que se debe cargar:
     *
     * templates/empleados.html
     */
    @GetMapping
    public String mostrarEmpleados(Model model) {

        model.addAttribute(
                "empleados",
                empleadosService.getEmpleados()
        );

        return "empleados";
    }


    /*
     * ============================================================
     * Mostrar formulario para crear un empleado
     * ============================================================
     *
     * La ruta completa será:
     * GET /web/empleados/nuevo
     *
     * No necesita consultar ningún empleado.
     *
     * return "nuevo-empleado" indica que Thymeleaf debe cargar:
     *
     * templates/nuevo-empleado.html
     */
    @GetMapping("/nuevo")
    public String nuevoEmpleado() {

        return "nuevo-empleado";
    }


    /*
     * ============================================================
     * Mostrar formulario para modificar un empleado
     * ============================================================
     *
     * La ruta completa será:
     * GET /web/empleados/editar/{idEmpleado}
     *
     * @PathVariable obtiene el ID del empleado de la URL.
     *
     * El Service busca el empleado y devuelve un
     * Optional<EmpleadoDTO> porque puede existir o no.
     *
     * Si existe:
     *     → se añade al Model.
     *     → se muestra la vista editar-empleado.html.
     *
     * Si no existe:
     *     → se redirige a la lista de empleados.
     */
    @GetMapping("/editar/{idEmpleado}")
    public String mostrarFormularioEdicion(
            @PathVariable long idEmpleado,
            Model model) {

        Optional<EmpleadoDTO> empleado =
                empleadosService.getEmpleadoById(idEmpleado);

        if (empleado.isEmpty()) {

            return "redirect:/web/empleados";
        }

        model.addAttribute(
                "empleado",
                empleado.get()
        );

        return "editar-empleado";
    }
}