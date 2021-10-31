package de.paetz.feuerwehr.informationdashboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/login";
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//			.authorizeRequests()
//			.antMatchers("/editCal/**").hasRole("ADMIN")
//			.anyRequest().not().authenticated()
//			.and()
//			.formLogin().permitAll()
//			.and()
//			.logout().permitAll()
//			.and()
//			.exceptionHandling().accessDeniedPage("/403")
//			;

		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()

				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache().requestCache(new CustomRequestCache())

				// Restrict access to our application.
				.and()
				.authorizeRequests()
				.antMatchers("/showCal/**").permitAll()
				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

				// Allow all requests by logged in users.
				.anyRequest().authenticated()

				.and().formLogin().permitAll()
			.and()
			.logout().permitAll()
				// Configure the login page.
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)

				// Configure logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest",
				"/sw.js",
				"/offline-page.html",

				// icons and images
				"/icons/**",
				"/images/**",

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**",

				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**");
	}
	
}
