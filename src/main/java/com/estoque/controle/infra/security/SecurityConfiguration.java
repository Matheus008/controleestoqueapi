package com.estoque.controle.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    //Filtro de rotas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorize -> autorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/fornecedor").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/fornecedor").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/fornecedor").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.POST, "/produtos").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/produtos").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/produtos").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.POST, "/movimentacao").hasAnyRole("ADMIN", "GERENTE", "ESTOQUE")
                        .requestMatchers(HttpMethod.GET, "/movimentacao").hasAnyRole("ADMIN", "GERENTE", "ESTOQUE")
                        .requestMatchers(HttpMethod.POST, "/cliente").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/cliente").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.PUT, "/cliente").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers(HttpMethod.POST,"/vendas").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers(HttpMethod.GET, "/relatorio").hasAnyRole("ADMIN", "GERENTE")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //Guardar a configuração de autenticação da aplicação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Criptografar a senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
