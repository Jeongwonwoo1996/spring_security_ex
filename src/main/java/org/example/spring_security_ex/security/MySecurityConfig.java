package org.example.spring_security_ex.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MySecurityConfig {

//  private final PasswordEncoder passwordEncoder;

//  @Bean
//  public UserDetailsService userDetailsService(){
//    UserDetails user = User.withDefaultPasswordEncoder()
//        .username("user")
//        .password("user1111")
//        .roles("USER")
//        .build();
//    UserDetails admin = User.withDefaultPasswordEncoder()
//        .username("admin")
//        .password("admin1111")
//        .roles("ADMIN", "USER")
//        .build();
//    return new InMemoryUserDetailsManager(user, admin);
//  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//  security - step3
//    httpSecurity.authorizeHttpRequests(authorize -> authorize
//            .requestMatchers("/index.html").permitAll()
//            .anyRequest().authenticated())
//        .formLogin(Customizer.withDefaults());
//    DefaultSecurityFilterChain chain = httpSecurity.build();
//    chain.getFilters().forEach(System.out::println);

    //  security - step4 : 커스텀 로그인 페이지
    httpSecurity.authorizeHttpRequests(auth -> auth
          .requestMatchers("/login", "/account/signup").permitAll()
          .anyRequest().authenticated())
//        .csrf(csrf -> csrf.disable())
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/authentication")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error"))
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID"));
    return httpSecurity.build();
  }
}
