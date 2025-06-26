//package com.example.JARvis.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
////@Configuration
////@EnableWebSecurity
//public class SecurityConfig {
//
////    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/swagger-ui.html",
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/api-docs/**",
//                                "/docs/**",
//                                "/webjars/**",
//                                "/swagger-resources/**",
//                                "/favicon.ico",
//                                "/ask" // Allow public access to your RAG endpoint
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf.disable())  // Disable CSRF for API endpoints
//                .build();
//    }
//}