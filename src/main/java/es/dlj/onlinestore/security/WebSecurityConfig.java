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
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http.authenticationProvider(authenticationProvider());
        http
        .authorizeHttpRequests(authorize -> authorize
            //Public pages
            .requestMatchers("/", "/product/*", "/privacy", "/product/search").permitAll()
            //Private Pages
            .requestMatchers("/profile", "/cart/*", "/profile/order/*").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/admin").hasAnyRole("ADMIN")
            .requestMatchers("/product/new").hasAnyRole("ADMIN")
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .failureUrl("/loginerror")
            .defaultSuccessUrl("/user")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        );

        return http.build();
    }
}
