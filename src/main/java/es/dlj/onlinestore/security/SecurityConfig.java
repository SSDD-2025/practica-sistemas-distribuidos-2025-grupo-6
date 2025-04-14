package es.dlj.onlinestore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.dlj.onlinestore.security.jwt.JwtRequestFilter;
import es.dlj.onlinestore.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
	private JwtRequestFilter jwtRequestFilter;

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
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
					.requestMatchers(HttpMethod.POST, "/api/products/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/api/products/*").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/*").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/products/*/reviews/").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/*/reviews/*").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/products/cart").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/products/*/images/").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/api/products/*/images/").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/*/images/").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/products/tags/").hasRole("USER")
					.requestMatchers(HttpMethod.GET, "/api/users/").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT, "/api/users/*").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/sellproducts").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/reviews").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/image").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/users/*/image").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT, "/api/users/*/image").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*/image").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/cart").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*/cart").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/users/*/cart/products/*").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*/cart/products/*").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/orders").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/*/orders/*").hasRole("USER")
                    .requestMatchers(HttpMethod.POST, "/api/users/*/orders").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/*/orders/*").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/api/users/logout").hasRole("USER")
					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
    @Order(1)
    public SecurityFilterChain webFilterChain (HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
        .authorizeHttpRequests(authorize -> authorize
            //Public pages
            .requestMatchers(
                    "/", "/css/**", "/js/**",
                    "/privacy",
                    "/image/**",
                    "/product/*", 
                    "/register",
                    "/login", "/login-error",
                    "/error/**",
                    "/swagger-ui.html",
                    "/v3/api-docs",
                    "/swagger-ui/**",
                    "/webjars/**"
                ).permitAll()
            //Private Pages
            .requestMatchers(
                "/profile", "/profile/**", "/profile/deleteaccount",
                "/cart", "/cart/**", 
                "/product/*/**" 
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
        .exceptionHandling(exception -> exception
            .accessDeniedPage("/error/403")
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
