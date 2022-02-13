package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired private LoginService loginService;

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public String login(@RequestBody final User body) {
    return loginService.login(body);
  }
}
