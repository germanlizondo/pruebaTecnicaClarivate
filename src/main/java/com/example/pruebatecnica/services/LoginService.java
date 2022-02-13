package com.example.pruebatecnica.services;

import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Service
public class LoginService {

  @Autowired private Key jwtKey;

  @Autowired private UserRepository userRepository;

  public String login(final User body) {

    User user = userRepository.login(body.getUsername(), body.getPassword());

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User or Password not correct");
    }

    return buildToken(user.getId());
  }

  private String buildToken(final Integer userId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .signWith(jwtKey)
        .setExpiration(getDAteInTenMinutes())
        .compact();
  }

  private Date getDAteInTenMinutes() {
    Calendar date = Calendar.getInstance();
    long timeInSecs = date.getTimeInMillis();
    return new Date(timeInSecs + (10 * 60 * 1000));
  }
}
