package com.codeja.restbasico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codeja.restbasico.entities.Empleado;


/*
 * ============================================================
 * Repository de Empleado
 * ============================================================
 *
 * El Repository es la capa encargada del acceso a los datos.
 *
 * En nuestra aplicación, EmpleadoRepository permite trabajar
 * con la entidad Empleado almacenada en la base de datos H2.
 *
 * Extiende JpaRepository<Empleado, Long>.
 *
 * Empleado
 *     → indica la entidad con la que trabajará el Repository.
 *
 * Long
 *     → indica el tipo de dato del identificador de Empleado.
 *
 * Al extender JpaRepository heredamos métodos ya preparados
 * para realizar las operaciones habituales sobre la BBDD:
 *
 *     findAll()       → obtener todos
 *     findById()      → buscar por ID
 *     save()          → guardar o modificar
 *     deleteById()    → eliminar por ID
 *
 * No necesitamos implementar estos métodos.
 * Spring Data JPA proporciona automáticamente su implementación.
 *
 * El Service utilizará este Repository para acceder a los datos,
 * evitando que los Controllers trabajen directamente con la BBDD.
 */

public interface EmpleadoRepository
        extends JpaRepository<Empleado, Long> {

}