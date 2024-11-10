package com.example.footballblog.Security;

import com.example.footballblog.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Шифруем пароли, чтобы они хранились безопасно
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/createUser", "/login", "/blog/view/**", "/blogDetails", "/addPost", "/blog/edit/**")
                        .permitAll()  // Разрешаем доступ к этим маршрутам
                        .anyRequest().authenticated() // Остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Указываем страницу входа
                        .defaultSuccessUrl("/dashboard", true) // Перенаправляем на дашборд после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Перенаправляем на страницу логина после выхода
                        .deleteCookies("JSESSIONID", "remember-me") // Удаляем cookies при выходе
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecretKey") // Уникальный ключ для безопасности
                        .tokenValiditySeconds(86400) // Длительность действия cookies (например, 1 день)
                        .userDetailsService(userDetailsService) // Указываем сервис для загрузки пользователей
                )
                .csrf(csrf -> csrf.disable()); // Отключаем CSRF для упрощения

        return http.build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}