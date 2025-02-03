package com.jv.events.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.jv.events.repository.UserRepository;
import com.jv.events.model.Role;
import com.jv.events.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Bean // Makes this method's return value available as a Spring Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disables CSRF protection for API endpoints
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints that don't require authentication
                        .requestMatchers("/api/events").permitAll()
                        .requestMatchers("/api/events/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}").hasRole("STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/{id}").hasRole("STAFF")
                        .anyRequest().authenticated())
                .oauth2Login() // Enables OAuth2 login
                .successHandler((request, response, authentication) -> {
                    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                    OAuth2User oauth2User = token.getPrincipal();
                    String email = oauth2User.getAttribute("email");

                    User user = userRepository.findByEmail(email)
                            .orElseGet(() -> {
                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setName(oauth2User.getAttribute("name"));
                                newUser.setRole(Role.STAFF);
                                return newUser;
                            });

                    // Store the access token
                    OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                            token.getAuthorizedClientRegistrationId(),
                            token.getName());
                    user.setGoogleAccessToken(
                            client.getAccessToken().getTokenValue());
                    System.out.println("Token: " + client.getAccessToken().getTokenValue());

                    userRepository.save(user);
                    response.sendRedirect("/api/events");
                });

        return http.build();
    }
}