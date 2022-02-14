package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.dto.ScoreByLevel;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.services.ScoresService;
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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ScoresControllerTest {

  private final String DUMMY_TOKEN = "DUMMY_TOKEN";
  private static final Integer DUMMY_LEVEL = 1;
  private static final Integer DUMMY_SCORE = 10;
  private final Integer DUMMY_HIGHEST_SCORE = 100;

  private static final String ADD_SCORE_URL = "/level/" + DUMMY_LEVEL + "/score/" + DUMMY_SCORE;
  private static final String GET_LIST_OF_USER_SCORE_URL = "/level/" + DUMMY_LEVEL + "/score";

  @Mock private ScoresService scoresService = Mockito.mock(ScoresService.class);

  @InjectMocks private ScoresController scoresController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(scoresController).build();
  }

  @Test
  void addScoreTestNoContent() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.put(ADD_SCORE_URL)
                .header("Session-Key", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    Mockito.verify(scoresService).addScore(DUMMY_LEVEL, DUMMY_SCORE, DUMMY_TOKEN);
  }

  @Test
  void addScoreTestNoToken() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(ADD_SCORE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void getListOfUsersScoreOK() throws Exception {

    List<ScoreByLevel> dummyScores =
        Arrays.asList(
            ScoreByLevel.builder().score(1).userId(1).build(),
            ScoreByLevel.builder().score(2).userId(3).build(),
            ScoreByLevel.builder().score(3).userId(2).build());

    Mockito.when(
            scoresService.getHighestScoreByLevel(DUMMY_LEVEL, DUMMY_HIGHEST_SCORE, DUMMY_TOKEN))
        .thenReturn(dummyScores);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(GET_LIST_OF_USER_SCORE_URL)
                .header("Session-Key", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("filter", DUMMY_HIGHEST_SCORE.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.content()
                .json(new ObjectMapper().writeValueAsString(dummyScores)))
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  void getListOfUserScoreNoToken() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(GET_LIST_OF_USER_SCORE_URL)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
