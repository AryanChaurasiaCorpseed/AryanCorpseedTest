package com.corpseed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.*;

@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	private AuthenticationSuccessHandler successHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public UserDetailsService getUserDetailService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;

	}

	/// configure method...

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.headers().frameOptions().sameOrigin();
	http.authorizeRequests()
				  .antMatchers("//**","/payment/**","/admin/assets/**", "/admin/css/**",
				  "/admin/js/**", "/assets/**", "/css/**", "/js/**","/chat_boat/**", "/category/**",
				  "/service/**", "/enquiry/**", "/search/**", "/blogs/**", "/city/**",
				  "/knowledge-centre/**","/join-our-team/**", "/pages/**", "/contact-us/**",
				  "/partner/**", "/change-your-ca/**", "/life-at-corpseed/**","/hrm/**","/faq/**","/reset-password/**",
				  "/mca-fee-calculator/**", "/api/**", "/ckfinder/**", "/pictures/**","/forget-password",
				  "/law-update/**", "/thanks/**", "/about-us/**","/subscribe/**","/candidate/**","/support/**",
				  "/service/**","/admin/images/**","/images/**","/fonts/**","/admin/fonts/**","/bitrix24/**","/book-meeting/**","/industry/**","/sitemap.xml",
				  "/robots.txt","/allow-cookie/**","/rating/**","/ticket-support/**","/ckeditor/**","/feedback/**").permitAll()
				 
				.antMatchers("/candidate/**").hasRole("USER")
				.antMatchers("/my-profile/**").hasAnyRole("HR_MANAGER","HR_EXECUTIVE","ADMIN","DELIVERY","MARKETING","QUALITY","USER")
				.antMatchers("/hrm/**").hasAnyRole("HR_MANAGER","HR_EXECUTIVE","ADMIN")
				.antMatchers("/admin/life-at-corpseed/**").hasAnyRole("HR_MANAGER","ADMIN")
				.antMatchers("/admin/life/**").hasAnyRole("HR_MANAGER","ADMIN")
				.antMatchers("/admin/lawupdate/**").hasAnyRole("DELIVERY","MARKETING","ADMIN")
				.antMatchers("/admin/meeting/**","/admin/master/enquiry/**","/admin/master/address/**","/admin/master/contact/**","/admin/subscriber/**").hasAnyRole("QUALITY", "ADMIN")
				.antMatchers("/admin/blogs/**","/press/**","/admin/news/**","/admin/services/**","/admin/industry/**","/admin/product/**").hasAnyRole("MARKETING", "ADMIN")
				.antMatchers("/admin/interviews/**").hasAnyRole("HR_MANAGER","HR_EXECUTIVE","DELIVERY","MARKETING","QUALITY")				
				.antMatchers("/admin/**").hasRole("ADMIN")
				
				.and().formLogin().failureHandler(authenticationFailureHandler()).loginPage("/login")
				.defaultSuccessUrl("/my-profile/").permitAll().and().logout().invalidateHttpSession(true)
				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout").permitAll()
				.addLogoutHandler(new HeaderWriterLogoutHandler(
					new ClearSiteDataHeaderWriter(CACHE, COOKIES, STORAGE)))
				.and().rememberMe().userDetailsService(this.userDetailsService);
	}
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
