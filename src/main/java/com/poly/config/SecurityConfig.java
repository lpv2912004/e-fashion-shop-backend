package com.poly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.poly.service.JwtService;
import com.poly.service.UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Bean
	public UserDetailsService userDetailsService() {
		return userService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
//              		.requestMatchers("/**").permitAll()
						.requestMatchers(
								"/token/**",							
								"/login/**",
								"/api/auth/**", 
								"/api/v1/login", 
								"/api/v1/google-login",
								"/api/v1/facebook-login",
						
								"/user/api/colors",								
								"/user/api/sizes/**", 
								"/user/api/products1/**", 
								"/user/api/variants/**",
								"/user/api/categories/**", 
								"/user/api/flash-sales/**",						
								"/user/api/feedback/list/**", 
								"/user/api/feedback/listFeedback/**",
								"/user/api/voucher/available/**"
								)
						.permitAll()
						.requestMatchers(
								"/user/api/info/**",
								
								"/user/api/like/**",
								
								"/user/api/notify/**",
								
								"/user/api/payment/**",
								
								"/admin/api/sendtoken/**",
								
								"/user/api/cartdetail/**",

								"/user/api/voucher/list/**",
								
								"/user/api/feedback/create/**",
									
								"/user/api/order/create/**",
								"/user/api/order/createbyvnpay/**",
								"/user/api/order/cancel/**",
								"/user/api/order/all/**",
								"/user/api/order-details/**",
								"/user/api/order/verifyOrder/**",
								
								"/user/api/address/**"
//								"/user/api/address/create/**",
//								"/user/api/address/list/**",
//								"/user/api/address/detail/**",
//								"/user/api/address/update/**",
//								"/user/api/address/delete/**",
//								"/user/api/address/getAddress",
//								"/user/api/address/byid/**"
								
								)
						.hasAnyRole("USER", "ADMIN").requestMatchers("/home").permitAll() // với endpoint /hello thì sẽ
																							 //được cho qua
						.requestMatchers("/admin/**").hasRole("ADMIN") // với endpoint /admin/** sẽ yêu cầu authenticate
						.anyRequest().authenticated())

				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/admin/index", true))
				.logout(logout -> logout.logoutUrl("/api/v1/logout").logoutSuccessUrl("/login")
						.invalidateHttpSession(true).deleteCookies("Authorization").permitAll())
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
