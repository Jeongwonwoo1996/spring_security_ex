package org.example.spring_security_ex.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // request 에서 Authorization 헤더 찾아오기
    String authorization = request.getHeader("Authorization");
    // authorization 헤더에 들어있는 토큰 꺼재오기
    if(authorization == null || !authorization.startsWith("Bearer")) {
      log.info("authorization 헤더가 없거나, 헤더에 token 이 없음");
      filterChain.doFilter(request, response);
      // 메서드 종료
      return;
    }
    // 헤더에 토큰이 있는 경우
    log.info("authorization 헤더가 존재함");
    // "Bearer token" 을 분리해서 token 값만 꺼내옴
    String token = authorization.split(" ")[1];
    if(tokenProvider.isExpired(token)) {
      log.info("token 이 만료됨");
      filterChain.doFilter(request, response);
      return;
    }
    // token 이 유효한 경우, 토큰에서 name, username, role 을 획득
    String name = tokenProvider.getName(token);
    String username = tokenProvider.getUsername(token);
    Role role = tokenProvider.getRole(token) ;
    log.info("JwtFilter ::: role ==> {}", role.toString());

    Account account = new Account();
    account.setName(name);
    account.setUsername(username);
    account.setAuthority(role);

    // CustomUserDetails 에 회원정보 담기 --> userDetails 구현한 user 승속
    CustomUserDetails customUserDetails = new CustomUserDetails(account);
    // 스프링 시큐리티 인증 토큰 생성
    Authentication authToken  = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    // 세션에 사용자 정보 등록 ???
    SecurityContextHolder.getContext().setAuthentication(authToken);
    filterChain.doFilter(request, response);
  }
}