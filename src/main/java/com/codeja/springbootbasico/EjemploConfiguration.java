package com.codeja.springbootbasico;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/*
 * @Configuration indica a Spring que esta clase contiene configuración
 * de la aplicación y definiciones de Beans.
 *
 * A diferencia de @Component, @Service o @Repository, aquí los Beans
 * se declaran explícitamente mediante @Bean.
 *
 * @Component
 * @Service
 * @Repository
 *      ↓
 * Spring detecta automáticamente la clase y crea el Bean.
 *
 *
 * @Configuration
 *      ↓
 * @Bean
 *      ↓
 * Nosotros indicamos qué objeto debe crear Spring y cómo crearlo.
 */
@Configuration
public class EjemploConfiguration {

    // @Bean indica a Spring que el objeto devuelto por este método
    // debe registrarse y gestionarse como un Bean.
    //
    // En este ejemplo creamos un objeto que contiene información
    // básica sobre nuestra aplicación.
    @Bean
    public InfoAplicacion infoAplicacion() {
        return new InfoAplicacion("Spring Boot Básico", "1.0");
    }

    // Un segundo Bean para demostrar que una clase @Configuration
    // puede definir varios Beans.
    @Bean
    public String mensajeAplicacion() {
        return "Hola desde el Bean";
    }
}


/*
 * Record utilizado para almacenar la información de la aplicación.
 *
 * El objeto será creado por EjemploConfiguration y registrado
 * como Bean mediante @Bean.
 */
record InfoAplicacion(String nombre, String version) {
}


/*
 * Este controlador demuestra que podemos inyectar varios Beans
 * creados mediante @Configuration + @Bean.
 */
@RestController
class EjemploControladorConfiguration {

    private final InfoAplicacion infoAplicacion;
    private final String mensajeAplicacion;

    // Spring detecta el único constructor e inyecta automáticamente
    // los dos Beans necesarios:
    //
    // - InfoAplicacion
    // - String
    public EjemploControladorConfiguration(
            InfoAplicacion infoAplicacion,
            String mensajeAplicacion) {

        this.infoAplicacion = infoAplicacion;
        this.mensajeAplicacion = mensajeAplicacion;
    }

    // Al acceder a /infoapp, utilizamos los dos Beans inyectados.
    //
    // Ejemplo:
    // http://localhost:8080/infoapp
    @GetMapping("/infoapp")
    public String info() {
        return infoAplicacion.nombre()
                + " - "
                + infoAplicacion.version()
                + " - "
                + mensajeAplicacion;
    }
}


/*
 * FLUJO DEL EJEMPLO
 *
 * @Configuration
 *       ↓
 * @Bean
 *       ↓
 * InfoAplicacion
 *       ↓
 * Spring registra el objeto como Bean
 *       ↓
 * Inyección por constructor
 *       ↓
 * EjemploControladorConfiguration
 *       ↓
 * GET /infoapp
 *       ↓
 * JSON
 *
 *
 * IDEA IMPORTANTE:
 *
 * Con @Component, @Service o @Repository, Spring detecta la clase
 * y crea automáticamente un Bean a partir de ella.
 *
 * Con @Configuration + @Bean, nosotros indicamos explícitamente
 * qué objeto queremos que Spring cree y cómo debe crearlo.
 */