package org.example.spring_security_ex.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class LoginUser extends User {
  private int userid;
  private String name;
  private String phone;

  public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name, String phone) {
    super(username, password, authorities);
    this.name = name;
    this.phone = phone;
  }

  public int getUserid() {
    return userid;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }
}

