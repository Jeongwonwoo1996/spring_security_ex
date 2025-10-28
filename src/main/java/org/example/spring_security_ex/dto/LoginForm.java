package org.example.spring_security_ex.dto;

import lombok.Data;

@Data
public class LoginForm {
  private String username;
  private String password;
}
