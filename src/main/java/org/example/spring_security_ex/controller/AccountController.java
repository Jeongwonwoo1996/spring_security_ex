package org.example.spring_security_ex.controller;

import lombok.RequiredArgsConstructor;
import org.example.spring_security_ex.dto.SignupForm;
import org.example.spring_security_ex.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
  private final AccountService accountService;

  @GetMapping("/signup")
  public String signup(Model model){
    model.addAttribute("myuser", new SignupForm());
    return "signup";
  }


  @PostMapping("/signup")
  public String signup(@ModelAttribute SignupForm form){
    accountService.signup(form);
    return "redirect:/login";
  }
}
