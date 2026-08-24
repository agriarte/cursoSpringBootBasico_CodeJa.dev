package com.codeja.restbasico.controllers;

import org.springframework.http.ResponseEntity;

// Ejercicio del capítulo ResponseEntity 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class EjercicioContraladorUsuarios {
	   private final static List<Usuario> usuarios = new ArrayList<>(List.of(
	            new Usuario(1L, "Juan Pérez", "juan.perez@example.com"),
	            new Usuario(2L, "María López", "maria.lopez@example.com"),
	            new Usuario(3L, "Carlos Sánchez", "carlos.sanchez@example.com")
	    ));

	    // COMPLETA ESTE MÉTODO PARA OBTENER UN USUARIO POR SU ID USANDO @PathVariable
	    @GetMapping("/{id}")
	    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
	        final Optional<Usuario> usuarioEncontrado = usuarios.stream()
	                .filter(usuario -> usuario.id().equals(id))
	                .findFirst()
	                ;

	        if (usuarioEncontrado.isPresent()) {
	            // USA AQUI EL RESPONSE ENTITY PARA DEVOLVER EL USUARIO CON ESTADO OK
	        	// get() extrae el Usuario que hay dentro del Optional.
	            return ResponseEntity.ok(usuarioEncontrado.get());
	        }

	        // USA AQUI EL RESPONSE ENTITY PARA DEVOLVER UN NOT FOUND
	        return ResponseEntity.notFound().build();
	    }

	    // NO MODIFICAR ESTE RECORD
	    record Usuario(
	            Long id,
	            String nombre,
	            String email
	    ) {
	    }
	}