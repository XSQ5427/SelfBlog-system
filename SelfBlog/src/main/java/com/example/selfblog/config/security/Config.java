package com.example.selfblog.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**","/login.html","/login/**","/").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.GET,"/blog/*").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.GET,"/publish_blog.html").hasAuthority("ADMIN")
                .and().authorizeRequests().antMatchers(HttpMethod.PUT,"/blog").hasAuthority("ADMIN")
                .and().authorizeRequests().anyRequest().authenticated();
        http.csrf().disable();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public TokenFilter jwtAuthencationTokenFilter(){return new TokenFilter();}
}