package com.demo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // HTTP 통신에 대한 인가설정 (어느 요청이든 인가를 받겠다)
            .formLogin(form -> form
                .loginPage("/loginPage")
                .loginProcessingUrl("/loginProc")
                .defaultSuccessUrl("/", true)
                .failureUrl("/failed")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .successHandler((request, response, authentication /*인증 성공한 객체 정보*/) -> {
                    System.out.println("authentication : " + authentication);
                    response.sendRedirect("/home");
                })
                .failureHandler((request, response, exception /*예외 정보*/) -> {
                    System.out.println("exception: " + exception.getMessage());
                    response.sendRedirect("/login");
                })
                .permitAll()
            )

        ;

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password("{noop}1111")
            .roles("USER").build();

        return new InMemoryUserDetailsManager(user);
    }
}
