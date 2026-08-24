package com.codeja.springbootbasico;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EjemploControladorAutowired {
	
	// Dependencia necesaria para que el controlador pueda funcionar.
	private final EjemploAutowired ejemploAutowired;

    // Spring detecta el único constructor e inyecta automáticamente
    // el Bean EjemploAutowired.
    // No es necesario utilizar @Autowired.
	public EjemploControladorAutowired(EjemploAutowired ejemploAutowired) {
		this.ejemploAutowired = ejemploAutowired;
	}
	
	@GetMapping("/autowired")
    public String mensaje() {
        return ejemploAutowired.obtenerMensaje();
    }
}


/*
 * En este controlador utilizamos inyección de dependencias por constructor.
 *
 * Aunque no aparece @Autowired, Spring detecta que existe un único constructor
 * y lo utiliza automáticamente para inyectar EjemploAutowired.
 *
 * Esta forma suele ser preferible a la inyección por atributo porque:
 * - Las dependencias quedan explícitas.
 * - Podemos utilizar final.
 * - Facilita las pruebas.
 * 
 * IMPORTANTE RECORDAR:
 * La inyección de dependencias (DI) no significa necesariamente utilizar
 * la anotación @Autowired.
 */