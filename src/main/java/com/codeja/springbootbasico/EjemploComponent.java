package com.codeja.springbootbasico;

import org.springframework.stereotype.Component;

/*
 * @Component es la anotación genérica para indicar a Spring que una clase
 * debe ser gestionada como Bean.
 *
 * Se utiliza cuando la clase no encaja en una categoría más específica:
 *
 * @Component   → componente genérico o auxiliar.
 * @Service     → lógica de negocio.
 * @Repository  → acceso a datos.
 * @Controller  → peticiones web.
 *
 * @Service, @Repository y @Controller se basan en @Component y añaden
 * un significado más específico sobre la finalidad de la clase.
 */

@Component
public class EjemploComponent {
	public String convertirMayusculas(String texto) {
		return texto.trim().toUpperCase();
	}

}
