package com.codeja.restbasico.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/*
 * ============================================================
 * Entidad Empleado
 * ============================================================
 *
 * Esta clase representa un empleado dentro de la base de datos.
 *
 * @Entity indica a JPA que esta clase debe ser gestionada como
 * una entidad persistente y que sus objetos pueden almacenarse
 * en una tabla de la base de datos.
 *
 * En nuestro proyecto existe también EmpleadoDTO, pero ambos
 * tienen funciones diferentes:
 *
 * Empleado
 *     → Entity
 *     → representa los datos almacenados en la BBDD.
 *
 * EmpleadoDTO
 *     → objeto de transferencia de datos.
 *     → se utiliza para intercambiar datos entre la aplicación
 *       y el cliente de la API.
 *
 *
 * El Service coordina la lógica de la aplicación y actúa como
 * intermediario entre el Controller y el acceso a datos.
 *
 * Cuando sea necesario, también realiza las conversiones entre
 * Entity y DTO.
 */

@Entity
public class Empleado {


    /*
     * Identificador de la entidad.
     *
     * @Id indica que este campo es la clave primaria de la tabla.
     */
    @Id

    /*
     * El valor del ID será generado automáticamente.
     *
     * GenerationType.IDENTITY delega en la base de datos
     * la generación del identificador.
     *
     * Al crear un nuevo empleado no necesitamos proporcionar
     * el ID; H2 lo generará automáticamente.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;


    /*
     * Constructor vacío requerido por JPA para poder crear
     * instancias de la entidad.
     */
    public Empleado() {
    }


    /*
     * Constructor utilizado para crear un Empleado indicando
     * sus datos.
     */
    public Empleado(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}