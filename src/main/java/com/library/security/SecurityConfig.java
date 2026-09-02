package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Role-based access control across the three Person types:
 *  - LIBRARIAN: full access - manage books, members, staff, issue/return.
 *  - TEACHER / STUDENT: read-only access - dashboard, book catalogue,
 *    and the list of currently-borrowed books.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/img/**", "/login").permitAll()

                // Librarian-only management areas
                .requestMatchers("/books/new").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.POST, "/books/**").hasRole("LIBRARIAN")
                .requestMatchers("/members/**").hasRole("LIBRARIAN")
                .requestMatchers("/staff/**").hasRole("LIBRARIAN")
                .requestMatchers(HttpMethod.POST, "/borrow/**").hasRole("LIBRARIAN")

                // Everyone logged in (any role) can view the dashboard, catalogue, and borrow page
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
