package be.ucll.ip.minor.reeks1210.general;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean // Also overrides default spring boot password-encoder
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("t"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("t"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
    @Bean // Turn off security for certain URLs
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/api/**", "/js/**", "/images/**")
                .requestMatchers(antMatcher("/h2/**")); // https://stackoverflow.com/questions/74680244/h2-database-console-not-opening-with-spring-security;
    }

    @Bean // Configure security (authorization) for all URLs for which it is enabled
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http // Order matters!!
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/home").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/language").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("header.html").permitAll()
                .requestMatchers("head.html").permitAll()
                .requestMatchers("footer.html").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/*/add").hasRole("ADMIN")
                .requestMatchers("/*/delete").hasRole("ADMIN")
                .requestMatchers("/*/update").hasRole("ADMIN")
                .requestMatchers("/**").authenticated()
                .and()
                .headers().frameOptions().disable()
                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .and()
                .logout().permitAll()
                .deleteCookies("JSESSIONID")
                .and()
                .httpBasic();

        return http.build();
    }



}