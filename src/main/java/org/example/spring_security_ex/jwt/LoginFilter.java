package org.example.spring_security_ex.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 인증 시도하는 정보 가져와서 로그 찍어서 확인해보기
    String username = super.obtainUsername(request);
    String password = super.obtainPassword(request);

    //인증 시도하는 정보 가져와서 로그 찍어서 확인해보기 - 확인 후 삭제하기
    log.info("Attempting to authenticate username : {} ", username);
    log.info("Attempting to authenticate password : {} ", password);

    //스프링 시큐리티에서 username 값 password를 검증하기 위해 token 에 담기
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(username, password, null);
    // 토큰에 담은 내용을 검증하기 위해 AuthenticationManager의 authenticate() 메서드로 검증
    Authentication authentication = authenticationManager.authenticate(authToken);
    log.info("authentication: {} ", authentication);
    return authentication;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

    String username = customUserDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    Account account = new Account();
    account.setUsername(username);
    // 리팩토링 대상임 ==>
    account.setAuthority(Role.USER);
    String token = tokenProvider.createJWT(account, 60*10*1000L);
    response.addHeader("Authorization", "Bearer " + token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    response.setStatus(401); // unauthorized


  }


}
