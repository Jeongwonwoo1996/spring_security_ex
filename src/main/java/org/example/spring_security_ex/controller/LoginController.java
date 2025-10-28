package org.example.spring_security_ex.controller;

import org.example.spring_security_ex.dto.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String showLoginPage(@ModelAttribute LoginForm loginForm){
    return "login";
  }
}
