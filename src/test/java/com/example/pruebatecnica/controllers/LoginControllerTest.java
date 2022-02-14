package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class LoginControllerTest {

  private static final String LOGIN_URL = "/login";

  private final String DUMMY_TOKEN = "DUMMY_TOKEN";
  private final String DUMMY_USERNAME = "DUMMY_USERNAME";
  private final String DUMMY_PASSWORD = "DUMMY_PASSWORD";

  @Mock private LoginService loginService;

  @InjectMocks private LoginController loginController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
  }

  @Test
  void loginTestAccepted() throws Exception {

    User dummyUser = User.builder().username(DUMMY_USERNAME).password(DUMMY_PASSWORD).build();

    Mockito.when(loginService.login(dummyUser)).thenReturn(DUMMY_TOKEN);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(LOGIN_URL)
                .content(new ObjectMapper().writeValueAsString(dummyUser))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(MockMvcResultMatchers.content().string(DUMMY_TOKEN));
  }

  @Test
  void loginTestBadRequest() throws Exception {

    mockMvc
        .perform(MockMvcRequestBuilders.post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
