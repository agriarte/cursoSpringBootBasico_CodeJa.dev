package com.codeja.springbootbasico;

import org.springframework.stereotype.Service;

@Service
public class EjemploService {
	
    // @Service es una especialización de @Component.
    // Indica a Spring que esta clase contiene lógica de negocio.
    // Spring detecta la clase y la registra como Bean en el contexto de Spring.
	
	public String obtenerSaludo() {
        return "Hola desde el servicio!";
    }
}

/*
 * @Service, @Repository y @Controller son especializaciones de @Component.
 *
 * Todas permiten que Spring detecte la clase automáticamente y la registre
 * como Bean, pero cada una se utiliza para una cosa diferente:
 *
 * @Component   → componente genérico.
 * @Service     → lógica de negocio.
 * @Repository  → acceso a datos.
 * @Controller  → controladores web.
 */