package org.example.spring_security_ex.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spring_security_ex.dto.SignupForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.FileStore;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String username;
  @Column(length = 255)
  private String password;
  @Enumerated(EnumType.STRING) //  숫자가 아닌 문자열로 저장되게
  private Role authority; // USER, ADMIN
  private String name;
  private String phone;

  public static Account createAccount(SignupForm form, PasswordEncoder passwordEncoder){

    return new Account
        (0,
            form.getUsername(),
            passwordEncoder.encode(form.getPassword()),
            Role.USER,
            form.getName(),
            form.getPhone());

  }

  public boolean checkPassword(String password, PasswordEncoder passwordEncoder){
    return passwordEncoder.matches(password,this.password);
  }

}

