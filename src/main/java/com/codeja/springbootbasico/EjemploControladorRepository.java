package com.codeja.springbootbasico;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class EjemploControladorRepository {
	
	private EjemploRepository ejemploRepository;
	
	   // Spring inyecta automáticamente EjemploRepository al crear el controlador.
    public EjemploControladorRepository(EjemploRepository ejemploRepository) {
        this.ejemploRepository = ejemploRepository;
    }

    // @RequestParam recoge el parámetro enviado en la URL.
    // Ejemplo: /repoparam?id=1
    @GetMapping("/repoparam")
    public String repoParam(@RequestParam Integer id) {
        return ejemploRepository.obtenerNombre(id);
    }
    
    // @PathVariable recoge una variable incluida en la propia URL.
    // Ejemplo: /repovariable/1
    @GetMapping("/repovariable/{id}")
    public String repoVariable(@PathVariable Integer id) {
        return ejemploRepository.obtenerNombre(id);
    }
    
    
	
}


/* Diagrama del flujo de la petición HTTP:
 
Tanto con @RequestParam o @PathVariable se obtienen los datos

	/repoparam?id=1
		│
		↓
		Controller
		│
		↓
		Repository
		│
		↓
		"Pedro"


	/repovariable/1
		│
		↓
		Controller
		│
		↓
		Repository
		│
		↓
		"Pedro"
*/

