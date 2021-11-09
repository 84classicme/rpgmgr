package io.festerson.rpgvault;

import io.festerson.rpgvault.security.AuthenticationManager;
import io.festerson.rpgvault.security.SecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
        return http
            .exceptionHandling()
            .authenticationEntryPoint((swe, e) ->
                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
            ).accessDeniedHandler((swe, e) ->
                Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
            ).and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers("/auth").permitAll()
            .pathMatchers("/pf2e/**").permitAll()
            // swagger ui entry point is  http://localhost:8080/swagger-ui.html and then redirects
            // springdoc v3 api json entry point is /v3/api-docs
            .pathMatchers("/webjars/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs", "/v3/api-docs/**").permitAll()
            .anyExchange().authenticated()
            .and().build();
    }
}