package org.example.spring_security_ex.service;

import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.dto.SignupForm;
import org.example.spring_security_ex.entity.Account;
import org.example.spring_security_ex.entity.LoginUser;
import org.example.spring_security_ex.entity.Role;
import org.example.spring_security_ex.repository.AccountRepository;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Account> byUsername = accountRepository.findByUsername(username);
    if(!byUsername.isPresent()){
      throw new UsernameNotFoundException(username);
    }
    Account account = byUsername.get();
      return new LoginUser(
          account.getUsername(),
          account.getPassword(),
          getAuthorityList(account.getAuthority()),
          account.getName(),
          account.getPhone());
  }

  private List<GrantedAuthority> getAuthorityList(Role role) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role.name())); // user =>  USER, admin => ADMIN
    if (role == Role.ADMIN) {
      authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
    }
    return authorities;
  }

  public void signup(SignupForm form) {
    // 동일한 username 이 있으면 안됨
    if(!vaildateDuplicateAccount(form)) {
      Account account = Account.createAccount(form, passwordEncoder);
      //필드 추가
      accountRepository.save(account);
    }
  }

  public boolean vaildateDuplicateAccount(SignupForm form) {
    Optional<Account> byUsername = accountRepository.findByUsername(form.getUsername());
    if(byUsername.isPresent()){
      //return true;
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
    return false;
  }
}
