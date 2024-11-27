package com.miniProjet.libraryProject.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.miniProjet.libraryProject.Entity.Users;
import com.miniProjet.libraryProject.Repository.UserRepository;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

@Configuration
public class WebSecurityConfig {
 @Autowired
    private UserRepository userRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure HttpSecurity using new practices introduced in Spring Security 6.x
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/users/register").permitAll() // Allow registration without authentication
                        .anyRequest().authenticated() // Other requests require authentication
                )
                .httpBasic(withDefaults()) // Use HTTP Basic authentication (still valid)
                .csrf(csrf -> csrf.disable()); // Disabling CSRF (use cautiously in production)

        return http.build();
    }

    // Password Encoder Bean for encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In-memory user details manager for simple authentication
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Users user = userRepository.findByEmail(username); // Use email as the username
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
        };
    }
    
}
