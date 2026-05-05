package com.example.babybee.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.babybee.repository.UserRepository;
import com.example.babybee.entity.User;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtFilter;
        private final CorsConfigurationSource corsConfigurationSource;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                                                .permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/api/user/**").hasRole("USER")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService(UserRepository userRepository) {
                return username -> userRepository.findByEmail(username)
                                .map(user -> org.springframework.security.core.userdetails.User
                                                .withUsername(user.getEmail())
                                                .password(user.getPassword())
                                                .roles(user.getRole() != null ? user.getRole() : "USER")
                                                .build())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        }
}