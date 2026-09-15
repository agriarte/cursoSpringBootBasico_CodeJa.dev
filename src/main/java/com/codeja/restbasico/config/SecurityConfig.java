package com.codeja.restbasico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * Clase de configuración de Spring Security.
 *
 * Aquí definimos:
 *
 * - Qué URLs requieren autenticación.
 * - Qué operaciones requieren el rol ADMIN.
 * - Qué mecanismo de autenticación utilizamos.
 * - Los usuarios que pueden autenticarse.
 * - Cómo se codifican las contraseñas.
 */

@Configuration
public class SecurityConfig {

    /*
     * Define la cadena de filtros de seguridad de Spring Security.
     *
     * HttpSecurity permite configurar las reglas de seguridad
     * que se aplicarán a las peticiones HTTP de la aplicación.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            /*
             * ==========================================================
             * AUTENTICACIÓN Y AUTORIZACIÓN
             * ==========================================================
             *
             * authenticated()
             * ----------------
             * Comprueba que el usuario ha iniciado sesión correctamente.
             *
             * Cualquier usuario autenticado puede acceder,
             * independientemente del rol que tenga.
             *
             *
             * hasRole("ADMIN")
             * ----------------
             * Comprueba que el usuario está autenticado y que,
             * además, tiene el rol ADMIN.
             *
             *
             * Por tanto:
             *
             * authenticated()  -> ¿Está autenticado?
             *
             * hasRole("ADMIN") -> ¿Está autenticado y tiene
             *                      el rol ADMIN?
             */
            .authorizeHttpRequests(authorizeRequests -> {

                /*
                 * ======================================================
                 * CONSULTA DE EMPLEADOS
                 * ======================================================
                 *
                 * Las peticiones GET a /empleados y /empleados/*
                 * requieren que el usuario esté autenticado.
                 *
                 * Tanto USER como ADMIN pueden consultar empleados.
                 */
                authorizeRequests
                    .requestMatchers(
                        HttpMethod.GET,
                        "/empleados",
                        "/empleados/**"
                    )
                    .authenticated();


                /*
                 * ======================================================
                 * CREACIÓN DE EMPLEADOS
                 * ======================================================
                 *
                 * Las peticiones POST solo pueden ser realizadas
                 * por usuarios que tengan el rol ADMIN.
                 */
                authorizeRequests
                    .requestMatchers(
                        HttpMethod.POST,
                        "/empleados",
                        "/empleados/**"
                    )
                    .hasRole("ADMIN");


                /*
                 * ======================================================
                 * MODIFICACIÓN DE EMPLEADOS
                 * ======================================================
                 *
                 * Las peticiones PUT solo pueden ser realizadas
                 * por usuarios que tengan el rol ADMIN.
                 */
                authorizeRequests
                    .requestMatchers(
                        HttpMethod.PUT,
                        "/empleados",
                        "/empleados/**"
                    )
                    .hasRole("ADMIN");


                /*
                 * ======================================================
                 * ELIMINACIÓN DE EMPLEADOS
                 * ======================================================
                 *
                 * Las peticiones DELETE solo pueden ser realizadas
                 * por usuarios que tengan el rol ADMIN.
                 */
                authorizeRequests
                    .requestMatchers(
                        HttpMethod.DELETE,
                        "/empleados",
                        "/empleados/**"
                    )
                    .hasRole("ADMIN");


                /*
                 * ======================================================
                 * INTERFAZ WEB
                 * ======================================================
                 *
                 * Todas las URLs que comienzan por /web/
                 * requieren que el usuario esté autenticado.
                 *
                 * Por tanto, un usuario que no haya iniciado sesión
                 * será redirigido al formulario de login.
                 */
                authorizeRequests
                    .requestMatchers("/web/**")
                    .authenticated();


                /*
                 * ======================================================
                 * RESTO DE PETICIONES
                 * ======================================================
                 *
                 * Cualquier otra URL que no haya coincidido
                 * con las reglas anteriores queda permitida.
                 */
                authorizeRequests
                    .anyRequest()
                    .permitAll();

            })


            /*
             * ==========================================================
             * FORMULARIO DE LOGIN
             * ==========================================================
             *
             * Habilita la autenticación mediante un formulario de login.
             *
             * Spring Security proporciona automáticamente una página
             * de login en /login cuando no hemos creado una personalizada.
             *
             * .permitAll() permite que cualquier usuario pueda acceder
             * al formulario de login.
             */
            .formLogin(formLogin -> formLogin
                .permitAll()
            )


            /*
             * ==========================================================
             * LOGOUT
             * ==========================================================
             *
             * Spring Security proporciona el endpoint POST /logout
             * para cerrar la sesión del usuario.
             *
             * Después de cerrar sesión, se redirige a /web/empleados.
             *
             * Como /web/empleados requiere autenticación, Spring Security
             * redirigirá al usuario al formulario de login.
             */
            .logout(logout -> logout
                .logoutSuccessUrl("/web/empleados")
            );


        /*
         * Construye y devuelve la cadena de filtros de seguridad
         * que utilizará Spring Security.
         */
        return http.build();
    }


    /*
     * ==============================================================
     * USUARIOS
     * ==============================================================
     *
     * Define los usuarios que pueden autenticarse.
     *
     * En este ejemplo los usuarios se almacenan EN MEMORIA.
     *
     * No se guardan en PostgreSQL.
     *
     * El PasswordEncoder se recibe como parámetro y Spring
     * lo inyecta automáticamente.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {


        /*
         * ==========================================================
         * USUARIO NORMAL
         * ==========================================================
         *
         * username: user
         * password: password
         * rol: USER
         *
         * Puede consultar empleados, pero no puede
         * crear, modificar ni eliminar.
         *
         * .roles("USER") hace que Spring Security asigne
         * internamente la autoridad ROLE_USER.
         */
        UserDetails usuario = User
            .withUsername("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build();


        /*
         * ==========================================================
         * USUARIO ADMINISTRADOR
         * ==========================================================
         *
         * username: admin
         * password: admin
         * rol: ADMIN
         *
         * Puede consultar, crear, modificar y eliminar empleados.
         *
         * .roles("ADMIN") hace que Spring Security asigne
         * internamente la autoridad ROLE_ADMIN.
         */
        UserDetails admin = User
            .withUsername("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN")
            .build();


        /*
         * InMemoryUserDetailsManager almacena los usuarios
         * en memoria mientras la aplicación está funcionando.
         *
         * Los usuarios se perderán al detener la aplicación.
         */
        return new InMemoryUserDetailsManager(usuario, admin);
    }


    /*
     * ==============================================================
     * CODIFICADOR DE CONTRASEÑAS
     * ==============================================================
     *
     * Define el mecanismo utilizado para codificar las
     * contraseñas antes de almacenarlas.
     *
     * BCrypt es un algoritmo de hash diseñado específicamente
     * para almacenar contraseñas de forma segura.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
