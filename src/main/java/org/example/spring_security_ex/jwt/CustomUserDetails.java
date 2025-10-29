package org.example.spring_security_ex.jwt;

import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails  implements UserDetails {
  private final Account account;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return account.getAuthority().name();
      }
    });
    return collection;
  }
  @Override
  public String getPassword() {
    return account.getPassword();
  }
  @Override
  public String getUsername() {
    return account.getUsername();
  }
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}