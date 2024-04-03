package com.microservicesproject.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // @Bean
    // public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity
    // serverHttpSecurity) {
    // serverHttpSecurity
    // .csrf(ServerHttpSecurity.CsrfSpec::disable)
    // .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
    // .permitAll()
    // .anyExchange()
    // .authenticated())
    // .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));
    // return serverHttpSecurity.build();
    // }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(auth -> auth
                .pathMatchers("/actuator/prometheus")
                .permitAll()
                .anyExchange()
                .authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }
}
