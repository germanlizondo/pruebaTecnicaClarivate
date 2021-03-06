package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.config.UriConstants;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

  @Autowired private LoginService loginService;

  @PostMapping(value = UriConstants.LOGIN, produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public String login(@RequestBody final User body) {
    log.info("Request: /login with username: [{}]", body.getUsername());
    return loginService.login(body);
  }
}
