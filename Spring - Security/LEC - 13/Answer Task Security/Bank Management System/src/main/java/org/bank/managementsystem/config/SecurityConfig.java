package org.bank.managementsystem.config;

import org.bank.managementsystem.security.BankAuthenticationProvider;
import org.bank.managementsystem.security.RestAccessDeniedHandler;
import org.bank.managementsystem.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   BankAuthenticationProvider authenticationProvider,
                                                   RestAuthenticationEntryPoint authenticationEntryPoint,
                                                   RestAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/accounts").hasAuthority("ACCOUNT_CREATE")
                        .requestMatchers(HttpMethod.GET, "/accounts/my-accounts").hasAuthority("ACCOUNT_READ")
                        .requestMatchers(HttpMethod.GET, "/accounts/*").hasAuthority("ACCOUNT_READ")
                        .requestMatchers(HttpMethod.PUT, "/accounts/*").hasAuthority("ACCOUNT_UPDATE")
                        .requestMatchers(HttpMethod.DELETE, "/accounts/*").hasAuthority("ACCOUNT_DELETE")
                        .requestMatchers(HttpMethod.GET, "/customers/*").hasAuthority("CUSTOMER_READ")
                        .requestMatchers(HttpMethod.PUT, "/customers/*").hasAuthority("CUSTOMER_UPDATE")
                        .requestMatchers(HttpMethod.POST, "/transfers").hasAuthority("TRANSFER_CREATE")
                        .requestMatchers(HttpMethod.POST, "/transfers/*/approve").hasAuthority("TRANSFER_APPROVE")
                        .requestMatchers(HttpMethod.GET, "/transfers/*").hasAuthority("TRANSFER_READ")
                        .requestMatchers(HttpMethod.GET, "/reports/**").hasAuthority("REPORT_VIEW")
                        .requestMatchers("/accounts/**", "/transfers/**", "/customers/**", "/reports/**").authenticated()
                        .anyRequest().denyAll())
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }
}