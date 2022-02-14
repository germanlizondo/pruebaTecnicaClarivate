package com.example.pruebatecnica.services;

import com.example.pruebatecnica.builders.JwtBuilder;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginService {

  @Autowired private JwtBuilder jwtBuilder;

  @Autowired private UserRepository userRepository;

  public String login(final User body) {

    User user = userRepository.login(body.getUsername(), body.getPassword());

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or Password not correct");
    }

    return jwtBuilder.buildToken(user.getId());
  }

}
