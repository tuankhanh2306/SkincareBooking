package edu.uth.skincarebookingsystem.config;

import edu.uth.skincarebookingsystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class Security {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService UserDetailsService;

    @Autowired
    public Security(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.UserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //Authentication
                        .requestMatchers("/api/auth/**").permitAll()  // Cho phép truy cập API auth

                        //USERS
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")


                        //SERVICES
                        .requestMatchers(HttpMethod.POST, "/api/services").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/services").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/services").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/services").hasRole("ADMIN")

                        // Appointment
                        .requestMatchers(HttpMethod.POST, "/api/appointments").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/appointments").hasAnyRole("ADMIN", "CUSTOMER", "SPECIALIST")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/customer/{customerId}").hasAnyRole("ADMIN", "CUSTOMER", "SPECIALIST")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/specialist/{specialistId}").hasAnyRole("ADMIN", "SPECIALIST")
                        .requestMatchers(HttpMethod.GET, "/api/appointments/{appointmentId}").hasAnyRole("ADMIN", "CUSTOMER", "SPECIALIST")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{appointmentId}").hasAnyRole("ADMIN", "SPECIALIST")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{appointmentId}/status").hasAnyRole("ADMIN", "SPECIALIST")
                        .requestMatchers(HttpMethod.PUT, "/api/appointments/{appointmentId}/complete").hasAnyRole("ADMIN", "SPECIALIST")
                        .requestMatchers(HttpMethod.DELETE, "/api/appointments/{appointmentId}").hasAnyRole("ADMIN", "SPECIALIST")

                        //Specialist
                        .requestMatchers(HttpMethod.POST, "/api/specialists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/specialists").hasAnyRole("ADMIN", "SPECIALIST","CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/specialists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/specialists").hasRole("ADMIN")

                        //Payment
                        .requestMatchers("/api/payment/**").hasRole("ADMIN")

                        //Feedback
                        .requestMatchers(HttpMethod.POST, "/api/feedback").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/feedback").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/specialist/{specialistId}").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/feedback/specialist/{specialistId}/average-rating0").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/feedback/{feedbackId}").hasRole("ADMIN")

                        //Quiz
                        .requestMatchers(HttpMethod.POST, "/api/quiz-question").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/quiz-question").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/quiz-question/{id}").hasRole("ADMIN")

                        .requestMatchers("/error").permitAll()  // Cho phép truy cập trang lỗi
                        .anyRequest().authenticated()  // Yêu cầu xác thực cho các request khác
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();


    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(UserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
