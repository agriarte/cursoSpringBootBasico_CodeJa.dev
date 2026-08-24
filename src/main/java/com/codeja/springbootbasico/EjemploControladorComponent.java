package com.codeja.springbootbasico;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class EjemploControladorComponent {
	
	private EjemploComponent ejemploComponent;
	// Spring inyecta automáticamente EjemploComponent mediante el constructor.
	public EjemploControladorComponent(EjemploComponent ejemploComponent) {
		this.ejemploComponent = ejemploComponent;
	}
	

    // @RequestParam recoge el parámetro enviado en la URL.
    // Ejemplo: /component?param=hola mundo
	@GetMapping("/component")
	public String dameMayusculas(@RequestParam String param) {
		return ejemploComponent.convertirMayusculas(param);
	}
	
	// @PathVariable recoge el valor de una variable incluida en la propia URL.
	// Ejemplo: /componentpath/hola → txt = "hola"
	@GetMapping("/componentpath/{txt}")
	public String dameMayusculasPath(@PathVariable String txt) {
	    return ejemploComponent.convertirMayusculas(txt);
	}
	
	
}
