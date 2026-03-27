package com.pasteleria.common.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.pasteleria.common.logging.RequestCorrelationLoggingFilter;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configura seguridad stateless del backend y el CORS minimo para frontends locales.
 */
@Configuration
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final RequestCorrelationLoggingFilter requestCorrelationLoggingFilter;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      RequestCorrelationLoggingFilter requestCorrelationLoggingFilter
  ) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.requestCorrelationLoggingFilter = requestCorrelationLoggingFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Expone login y superficies publicas; todo lo demas exige JWT valido.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/actuator/health",
                "/actuator/info",
                "/error",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/api/v1/public/**",
                "/api/v1/auth/login",
                "/assets/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(requestCorrelationLoggingFilter, SecurityContextHolderFilter.class)
        .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
        .build();
  }

  /**
   * Habilita consumo local desde Angular y Astro sin abrir el backend a cualquier origen.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    // Se permite cualquier puerto local de desarrollo para soportar Angular/Astro en dev server,
    // preview o servidores temporales sin abrir el backend a dominios externos.
    configuration.setAllowedOriginPatterns(List.of(
        "http://localhost:*",
        "http://127.0.0.1:*"
    ));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "X-Request-Id"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(false);
    configuration.setMaxAge(3600L);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}


