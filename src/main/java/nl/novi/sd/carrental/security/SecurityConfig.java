package nl.novi.sd.carrental.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()

                // Admin-only endpoints
                .requestMatchers(HttpMethod.POST, "/api/auth/register/admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/vehicles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/vehicles/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN")
                .requestMatchers("/locations/**").hasRole("ADMIN")
                .requestMatchers("/parking-lots/**").hasRole("ADMIN")
                .requestMatchers("/parking-spaces/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                // User and Admin accessible endpoints
                .requestMatchers(HttpMethod.GET, "/vehicles/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/locations/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/reservations/**").authenticated()
                .requestMatchers("/users/{id}").authenticated()

                // Deny all other requests by default
                .anyRequest().denyAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
