package com.reviewping.coflo.global.config;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.enums.Provider;
import com.reviewping.coflo.domain.user.enums.Role;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Profile("local | test")
@Configuration
@RequiredArgsConstructor
public class LocalSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health", "/api/sse/subscribe", "/webhook/*")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        new LocalAuthenticationFilter(userRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @PostConstruct
    public void init() {
        userRepository.save(new User("localUser", "https://placehold.co/400.png", "", Provider.GOOGLE, Role.USER));
    }
}
