package com.crud.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.crud.demo.service.DomainUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	private final DomainUserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//user authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	/*
	 * Providing in memory authentication at runtime. instead from database
	 */
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("arnav").password("admin").roles("ADMIN");
//		/*
//		 * HENCE THE role defined is actually ROLE_ADMIN.
//		 * public UserBuilder roles(String... roles) {
//			List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
//			for (String role : roles) {
//				Assert.isTrue(!role.startsWith("ROLE_"),
//						() -> role + " cannot start with ROLE_ (it is automatically added)");
//				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//			}
//			return authorities(authorities);
//		}
//		 */
//	}
	
	//user authorization
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.authorizeRequests()
	        	.antMatchers("/login", "/h2-console/**", "/h2-console**", "/h2-console/login**")
	        		.permitAll()
	            .antMatchers("/student/save/**","/student/save**","/student/showFormForAdd","/student/403")
	            	.hasAnyRole("USER","ADMIN")
	            .antMatchers(HttpMethod.POST, "/student/save**")
		            .hasRole("ADMIN")
	            .antMatchers("/student/showFormForUpdate","/student/delete")
	            	.hasRole("ADMIN")
	            .anyRequest().authenticated()
	            .and()
	            .formLogin().loginProcessingUrl("/login").successForwardUrl("/student/list").permitAll()
	            .and()
	            .logout()
	            .logoutSuccessUrl("/login").permitAll()
	            .and()
	            .exceptionHandling().accessDeniedPage("/student/403")
	            .and()
	            .cors().and().csrf().disable().headers().frameOptions().disable();
	    }


}
