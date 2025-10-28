package org.example.spring_security_ex.dto;

import lombok.Data;

@Data
public class SignupForm {
  private String username;
  private String password;
  private String name;
  private String phone;
}
