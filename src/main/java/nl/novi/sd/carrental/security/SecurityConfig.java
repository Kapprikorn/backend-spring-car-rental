package nl.novi.sd.carrental.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        http
                .csrf(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/locations/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/parking-lots/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/parking-spaces/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/reservations/**").authenticated()
                        .requestMatchers("/users/{id}").authenticated()
                        .requestMatchers("/users/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/vehicles/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")

                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled" +
                        " FROM users" +
                        " WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, authority" +
                        " FROM authorities " +
                        " WHERE username=?");
        return authenticationManagerBuilder.build();
    }
}
