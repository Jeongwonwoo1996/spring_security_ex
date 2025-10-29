package org.example.spring_security_ex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {
  @GetMapping("/main")
  public String main(){
    return "ok";
  }

  @GetMapping("/sub")
    public String sub(){
      return "not ok";
    }
  }

