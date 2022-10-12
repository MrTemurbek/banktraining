package uz.banktraining.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import uz.banktraining.service.MyUserDetailsService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MyUserDetailsService service;
    private final JWTFilter jwtFilter;



    @Autowired
    public SecurityConfig(MyUserDetailsService service, JWTFilter jwtFilter) {
        this.service = service;
        this.jwtFilter = jwtFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .headers()
                .httpStrictTransportSecurity()
                .disable()
                .and()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/auth/check", "/auth/download/**")
                .permitAll()
                .antMatchers("/api/**")
                .hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .disable();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }





}
