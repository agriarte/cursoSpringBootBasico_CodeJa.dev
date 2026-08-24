package com.codeja.springbootbasico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EjemploAutowired {
	
	
	// @Autowired indica a Spring que debe inyectar automáticamente
    // el Bean EjemploComponent en este atributo.
    //
    // Esta forma se denomina inyección por atributo (field injection).
    // Aunque funciona, normalmente se recomienda la inyección por constructor.
	
	@Autowired
    private EjemploComponent component;

    public String obtenerMensaje() {
        return component.convertirMayusculas("hola desde autowired");
    }
}

/*
 * En este ejemplo utilizamos @Autowired para mostrar cómo funciona
 * la inyección automática de dependencias.
 *
 * Aunque esta forma es válida, en código moderno se suele preferir
 * la inyección por constructor, ya que permite declarar las dependencias
 * como final y hace más evidente qué necesita la clase para funcionar.
 */