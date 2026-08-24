package com.codeja.springbootbasico;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class EjemploRepository {

    // @Repository es una especialización de @Component.
    // Indica a Spring que esta clase se encarga de acceder a los datos.
    // Spring detecta la clase y la registra como Bean.

    // En este ejemplo utilizamos un Map para simular una fuente de datos.
    // En una aplicación real podría ser una base de datos, un fichero, una API, etc.
	private final Map<Integer, String> personas = Map.of(
			1, "Pedro",
			2, "Natalia",
			3, "Iván"
	);

	public String obtenerNombre(int id) {
		return personas.get(id);
	}

}


/*
 * Función del Repository:
 *
 * El Repository se encarga de acceder a los datos de la aplicación.
 * Su función es ocultar al resto de la aplicación cómo y dónde se
 * almacenan esos datos.
 * 
 * El Repository se ocupa de obtener y guardar datos,
 * evitando que el resto de la aplicación tenga que conocer
 * cómo están almacenados.
 *
 * En este ejemplo los datos están en un Map, pero en una aplicación real
 * podrían estar en una base de datos como H2, PostgreSQL, Oracle, etc.
 *
 * La idea es separar el acceso a los datos del resto de la aplicación:
 *
 * Controller → recibe la petición
 * Service    → lógica de negocio
 * Repository → acceso a los datos
 */


