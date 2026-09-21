package com.codeja.restbasico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
             * Comprueba que el usuario está autenticado.
             *
             *
             * hasRole("ADMIN")
             * ----------------
             * Comprueba que el usuario está autenticado y que,
             * además, tiene el rol ADMIN.
             *
             *
             * Las reglas se evalúan en orden.
             */
            .authorizeHttpRequests(authorizeRequests -> {

                /*
                 * ======================================================
                 * TOKEN CSRF
                 * ======================================================
                 *
                 * GET /csrf permite obtener el token CSRF.
                 *
                 * Los clientes que realizan POST, PUT o DELETE
                 * necesitan obtener primero este token.
                 */
                authorizeRequests
                    .requestMatchers("/csrf")
                    .permitAll();


                /*
                 * ======================================================
                 * CONSULTA DE EMPLEADOS
                 * ======================================================
                 *
                 * Las peticiones GET requieren autenticación.
                 *
                 * USER y ADMIN pueden consultar empleados.
                 *
                 * Las peticiones GET no necesitan token CSRF.
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
                 * requieren autenticación.
                 */
                authorizeRequests
                    .requestMatchers("/web/**")
                    .authenticated();


                /*
                 * ======================================================
                 * RESTO DE PETICIONES
                 * ======================================================
                 *
                 * Las peticiones que no coincidan con ninguna
                 * regla anterior quedan permitidas.
                 */
                authorizeRequests
                    .anyRequest()
                    .permitAll();

            })


            /*
             * ==========================================================
             * PROTECCIÓN CSRF
             * ==========================================================
             *
             * Spring Security activa CSRF por defecto.
             *
             * POST, PUT y DELETE necesitan un token CSRF válido.
             *
             * Los clientes pueden obtenerlo mediante:
             *
             * GET /csrf
             *
             * y enviarlo posteriormente en la cabecera:
             *
             * X-CSRF-TOKEN
             *
             * Si el token falta o no corresponde a la sesión,
             * Spring Security responde con HTTP 403.
             */
            .csrf(Customizer.withDefaults())


            /*
             * ==========================================================
             * FORMULARIO DE LOGIN
             * ==========================================================
             *
             * Habilita la autenticación mediante formulario.
             *
             * Spring Security proporciona automáticamente
             * la página de login en /login.
             *
             * defaultSuccessUrl() indica dónde ir después de
             * un login correcto cuando no existe una petición
             * protegida pendiente.
             *
             * permitAll() permite acceder al login sin autenticarse.
             */
            .formLogin(formLogin -> formLogin

                .defaultSuccessUrl("/web/empleados")

                .permitAll()
            )


            /*
             * ==========================================================
             * BASIC AUTH
             * ==========================================================
             *
             * Habilita autenticación mediante HTTP Basic.
             *
             * Es útil para Postman y otros clientes REST.
             *
             * El cliente envía:
             *
             * Authorization: Basic usuario:contraseña
             *
             * codificado en Base64.
             *
             * Basic Auth autentica al usuario, pero no sustituye
             * la protección CSRF.
             *
             * Las peticiones POST, PUT y DELETE siguen necesitando
             * el token X-CSRF-TOKEN.
             */
            .httpBasic(Customizer.withDefaults())


            /*
             * ==========================================================
             * LOGOUT
             * ==========================================================
             *
             * Spring Security proporciona el endpoint POST /logout.
             *
             * Después de cerrar sesión se redirige a:
             *
             * /web/empleados
             *
             * Como /web/empleados requiere autenticación,
             * posteriormente se podrá mostrar el login.
             */
            .logout(logout -> logout
                .logoutSuccessUrl("/web/empleados")
            );


        /*
         * Construye y devuelve la cadena de filtros de seguridad.
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
     * En este proyecto se almacenan EN MEMORIA.
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
         * Puede consultar empleados.
         *
         * No puede crear, modificar ni eliminar.
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
         */
        UserDetails admin = User
            .withUsername("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN")
            .build();


        /*
         * InMemoryUserDetailsManager almacena ambos usuarios
         * en memoria mientras la aplicación está funcionando.
         *
         * Los usuarios se pierden al detener la aplicación.
         */
        return new InMemoryUserDetailsManager(usuario, admin);
    }


    /*
     * ==============================================================
     * CODIFICADOR DE CONTRASEÑAS
     * ==============================================================
     *
     * BCrypt se utiliza para codificar las contraseñas.
     *
     * Las contraseñas no se almacenan directamente en texto plano.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}