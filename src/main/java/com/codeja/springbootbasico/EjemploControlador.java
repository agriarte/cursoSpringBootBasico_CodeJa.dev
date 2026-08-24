package com.codeja.springbootbasico;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class EjemploControlador {

    // Define el endpoint /hola.
    // @ResponseBody indica que el resultado se devuelve directamente
    // en la respuesta HTTP y no se interpreta como una vista.
    @GetMapping("/hola")
    @ResponseBody
    public String hola() {
        return "Hola desde @Controller";
    }

    // Define el endpoint /pagina.
    // El String retornado se interpreta como el nombre de una vista.
    @GetMapping("/pagina")
    public String pagina() {
        return "pagina.html";
    }
}


@RestController
class EjemploRestControlador {

    // Define un endpoint que devuelve directamente el contenido
    // de la respuesta HTTP.
    // @RestController incluye implícitamente @ResponseBody.
    @GetMapping("/api/hola")
    public String hola() {
        return "Hola desde @RestController";
    }

    // Spring convierte automáticamente el objeto Java a JSON
    // y lo devuelve en la respuesta HTTP.
    @GetMapping("/api/persona")
    public Persona persona() {
        return new Persona("Pedro", 55);
    }
}

//Un record permite definir una clase de datos de forma muy concisa.
//Genera automáticamente el constructor y los métodos de acceso,
//además de equals(), hashCode() y toString().
//Sus componentes son inmutables: no existen setters.
record Persona(String nombre, int edad) {
}


//************************
// JAVA CLASICO SIN RECORD
//class Persona {
//	String nombre;
//	int edad;
//	public Persona(String nombre, int i) {
//		this.nombre = nombre;
//		this.edad = i;
//	}
//	public String getNombre() {
//		return nombre;
//	}
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//	public int getEdad() {
//		return edad;
//	}
//	public void setEdad(int edad) {
//		this.edad = edad;
//	}	
//}