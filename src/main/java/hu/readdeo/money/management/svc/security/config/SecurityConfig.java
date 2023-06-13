package hu.readdeo.money.management.svc.security.config;

import hu.readdeo.money.management.svc.security.CustomUserDetailsService;
import hu.readdeo.money.management.svc.security.JwtAuthenticationEntryPoint;
import hu.readdeo.money.management.svc.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

  @Autowired CustomUserDetailsService customUserDetailsService;
  @Autowired private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
        .authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/*"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/v3/*"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/auth/**"))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher(
                            "/api/user/checkUsernameAvailability",
                            "/api/user/checkEmailAvailability"))
                    .permitAll()
                    .anyRequest()
                    .permitAll())
        .httpBasic();

    // Add our custom JWT security filter
    //    http.addFilterBefore(jwtAuthenticationFilter(),
    // UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder)
      throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
  }

  // This is for Spring Data REST to be able to use principal in @Query
  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }
}
