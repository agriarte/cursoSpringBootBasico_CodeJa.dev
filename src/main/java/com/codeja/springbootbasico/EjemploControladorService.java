package com.codeja.springbootbasico;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EjemploControladorService {

    // Dependencia que necesita el controlador.
    private final EjemploService service;

    // Spring inyecta automáticamente EjemploService al crear este controlador.
    // Al existir un único constructor, no es necesario utilizar @Autowired.
    public EjemploControladorService(EjemploService service) {
        this.service = service;
    }

    // Define el endpoint /saludoservice.
    // El controlador delega la lógica al Service.
    @GetMapping("/saludoservice")
    @ResponseBody
    public String saludoService() {
        return service.obtenerSaludo();
    }
}


/*
 
Nota 1:
En este ejemplo básico se utiliza @Controller junto con @ResponseBody.
El uso de @RestController evita tener que incluir @ResponseBody,
ya que @RestController la incluye implícitamente.


Nota 2:
Existen técnicas que permiten evitar escribir el constructor manualmente,
como Lombok (@RequiredArgsConstructor).

Sin embargo, la inyección por constructor sigue siendo recomendable porque:
- Hace explícitas las dependencias de la clase.
- Permite utilizar final.
- Facilita las pruebas.
- Evita la inyección directa sobre atributos.

Por ejemplo, con Lombok:

@RequiredArgsConstructor
@Controller
public class EjemploControladorService {

    private final EjemploService service;

    // ...
}

Lombok genera automáticamente el constructor necesario.

*/