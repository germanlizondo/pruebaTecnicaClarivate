package com.example.pruebatecnica.services;

import com.example.pruebatecnica.builders.JwtBuilder;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class LoginServiceTest {

  private final String DUMMY_USERNAME = "dummy_username";
  private final String DUMMY_PASSWORD = "dummy_password";
  private final String DUMMY_TOKEN = "DUMMY_TOKEN";

  @Mock private JwtBuilder jwtBuilder;

  @Mock private UserRepository userRepository;

  @InjectMocks private LoginService loginService;

  @Test
  void loginTest() {

    User userRequest = User.builder().username(DUMMY_USERNAME).password(DUMMY_PASSWORD).build();

    User userDatabase =
        User.builder().username(DUMMY_USERNAME).password(DUMMY_PASSWORD).id(1).build();

    Mockito.when(userRepository.login(DUMMY_USERNAME, DUMMY_PASSWORD)).thenReturn(userDatabase);
    Mockito.when(jwtBuilder.buildToken(1)).thenReturn(DUMMY_TOKEN);

    String actualResponse = loginService.login(userRequest);

    Assertions.assertEquals(DUMMY_TOKEN, actualResponse);
  }

  @Test
  void loginTestBadRequest() {

    User userRequest = User.builder().username(DUMMY_USERNAME).password(DUMMY_PASSWORD).build();

    Mockito.when(userRepository.login(DUMMY_USERNAME, DUMMY_PASSWORD)).thenReturn(null);
    Mockito.when(jwtBuilder.buildToken(1)).thenReturn(DUMMY_TOKEN);

    try {
      loginService.login(userRequest);
    } catch (ResponseStatusException ex) {
      Assertions.assertEquals("400 BAD_REQUEST \"User or Password not correct\"", ex.getMessage());
      Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
  }
}
