package es.dlj.onlinestore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
        .authorizeHttpRequests(authorize -> authorize
            //Public pages
            .requestMatchers(
                "/", "/css/**", "/js/**",
                "/privacy",
                "/image/**",
                "/product/*", 
                "/register"
                ).permitAll()
            //Private Pages
            .requestMatchers(
                "/profile", "/profile/**", 
                "/cart", "/cart/**", 
                "/product/*/**" 
                // "/profile/deleteaccount"
                ).authenticated()
            .requestMatchers(
                "/product/new"
                ).hasAnyRole("ADMIN")
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .failureUrl("/login-error")
            .defaultSuccessUrl("/profile", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        );

        // Disable CSRF at the moment
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
