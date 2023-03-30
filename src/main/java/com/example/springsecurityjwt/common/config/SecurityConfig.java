package com.example.springsecurityjwt.common.config;

import com.example.springsecurityjwt.common.config.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.addFilterBefore(
        new JwtAuthenticationFilter(),
            BasicAuthenticationFilter.class)
        .csrf().disable()
        .authorizeRequests(
            authorize -> authorize.antMatchers("/login", "/members**", "/auth/login**")
                .permitAll() // 로그인 페이지는 모든 사용자에게 허용
                .anyRequest()
                .authenticated() // 나머지 요청은 인증된 사용자만 허용
        ).formLogin() // formLogin() 메소드를 사용하여 로그인 폼 생성
        .defaultSuccessUrl("/members")
        .permitAll(); // 로그인 페이지는 모든 사용자에게 허용

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder encodePassword() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
