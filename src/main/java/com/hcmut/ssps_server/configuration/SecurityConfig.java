package com.hcmut.ssps_server.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/students",
            "/students/**",
            "/admin",
            "/admin/**",
    };

    private final String[] STUDENT_ENDPOINTS = {
            "/students",
            "/students/**",
    };

    private final String[] ADMIN_ENDPOINTS = {
            "admin",
            "admin/**"
    };
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, "/students/recharge").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/admin/add-printer").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/ssps/students/upload").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, STUDENT_ENDPOINTS).hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.GET, ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/view-print-log/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/generate-usage-reports").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/view-print-logs").hasAuthority("ROLE_ADMIN")
                        //.requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS).permitAll()
                        //.requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
