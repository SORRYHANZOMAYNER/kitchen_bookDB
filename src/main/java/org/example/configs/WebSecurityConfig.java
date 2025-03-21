package org.example.configs;

import lombok.AllArgsConstructor;
import org.example.configs.filter.JwtFilter;
import org.example.configs.handlers.CustomAccessDeniedHandler;
import org.example.configs.handlers.CustomLogoutHandler;
import org.example.services.UserSessionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@AllArgsConstructor

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserSessionService userService;
    @Autowired
    private final CustomLogoutHandler logoutHandler;
    @Autowired
    private final JwtFilter jwtFilter;
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // отключение защиты от csrf
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET,"/api1/v1/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api1/v1/ingredient", "/api1/v1/category","/api1/v1/**").permitAll()
                        .requestMatchers("/", "/api1/v1/login", "/api1/v1/register", "/refresh_token/**").permitAll()
                        .anyRequest().authenticated()

                )
                .userDetailsService(userService)
                .logout(log -> {
                    log.logoutUrl("/logout");
                    log.addLogoutHandler(logoutHandler);})
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .exceptionHandling(
//                        httpSecurityExceptionHandlingConfigurer ->
//                                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
//                                        (request, response, authException) ->
//                                                response.sendError(
//                                                        HttpStatus.FORBIDDEN.value(),
//                                                        authException.getMessage()
//                                                )
//                                )
//                )
//                .formLogin(
//                        form ->
//                                form
//                                        .loginPage("/api1/v1/login")
//                                        .successHandler(
//                                                (request, response, authentication) -> response.setStatus(HttpStatus.NO_CONTENT.value())
//                                        )
//                                        .failureHandler(
//                                                (request, response, authentication) -> response.sendError(HttpStatus.UNAUTHORIZED.value())
//                                        )
//                                        .permitAll()
//                )
//                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest().permitAll()
//                )
//                .authenticationProvider(authenticationProvider());
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
}