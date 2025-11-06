package ru.lipnin.itmohomework.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.lipnin.itmohomework.security.services.JwtAuthenticationFilter;
import ru.lipnin.itmohomework.security.services.UserDetailService;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration {

    private final UserDetailService userDetailService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Шифрование паролей
     * проверка соответствия зашиврованного пароля и пароля в чистом виде
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Основа процесса аутентификации spring security
     * Делегирует аутентификацию провайдерам - AuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws SecurityException {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new SecurityException("AuthenticationManager not configured: " + e.getMessage());
        }
    }

    /**
     * AuthenticationProvider - провайдер, которому делегируеся процесс аутентификации
     * DaoAuthenticationProvider - реализация AuthenticationProvider, занимающаяся аутентификацией
     */
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);  // для получения пользователя из хранилища
        provider.setPasswordEncoder(passwordEncoder()); // для работы с паролями
        return provider;
    }

    /**
     * Настройки spring security:
     * выбор провайдера
     * способа аутентификации
     * безопасновть запросов
     * управление доступами
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/account/registration", "/api/v1/account/login")// запросы
                        .not().authenticated()// разрешены всем
                        .requestMatchers("/api/v1/discount") // запросы
                        .hasAnyRole("SUPER_USER", "ADMIN") // разрешены только пользователям с указанными ролями
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // запросы необходимо пропускать через фильтр
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
