package com.example.pruebatecnica.services;

import com.example.pruebatecnica.builders.JwtBuilder;
import com.example.pruebatecnica.dto.ScoreByLevel;
import com.example.pruebatecnica.models.Level;
import com.example.pruebatecnica.models.Score;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.repositories.LevelRepository;
import com.example.pruebatecnica.repositories.ScoreRepository;
import com.example.pruebatecnica.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ScoresServiceTest {

  private final String DUMMY_USERNAME = "dummy_username";
  private final String DUMMY_PASSWORD = "dummy_password";
  private final String DUMMY_TOKEN = "DUMMY_TOKEN";

  @Mock private JwtBuilder jwtBuilder;
  @Mock private UserRepository userRepository;
  @Mock private LevelRepository levelRepository;
  @Mock private ScoreRepository scoreRepository;

  @InjectMocks private ScoresService scoresService;

  @Test
  void addScoreTest() {

    User userDatabase =
        User.builder().username(DUMMY_USERNAME).password(DUMMY_PASSWORD).id(1).build();
    Level levelDatabase = Level.builder().id(1).build();
    Score score = Score.builder().score(1234).level(levelDatabase).user(userDatabase).build();

    Mockito.when(jwtBuilder.checkTokenAuthAndGetUserId(DUMMY_TOKEN)).thenReturn(1);
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(userDatabase));
    Mockito.when(levelRepository.findById(1)).thenReturn(Optional.of(levelDatabase));

    scoresService.addScore(1, 1234, DUMMY_TOKEN);

    Mockito.verify(scoreRepository).save(score);
  }

  @Test
  void addScoreTestNotFound() {

    Mockito.when(jwtBuilder.checkTokenAuthAndGetUserId(DUMMY_TOKEN)).thenReturn(1);
    Mockito.when(userRepository.findById(1)).thenReturn(Optional.empty());
    Mockito.when(levelRepository.findById(1)).thenReturn(Optional.empty());

    try {
      scoresService.addScore(1, 1234, DUMMY_TOKEN);
    } catch (ResponseStatusException ex) {
      Assertions.assertEquals(
          "404 NOT_FOUND \"User [" + 1 + "] or level [" + 1 + "] not found\"", ex.getMessage());
      Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
  }

  @Test
  void getHighestScoreByLevelTest() {

    User user1 = User.builder().id(1).build();
    User user2 = User.builder().id(2).build();
    User user3 = User.builder().id(3).build();
    User user4 = User.builder().id(4).build();
    Level levelDatabase =
        Level.builder()
            .id(1)
            .scores(
                Arrays.asList(
                    Score.builder().score(1234).user(user1).build(),
                    Score.builder().score(10000).user(user2).build(),
                    Score.builder().score(11).user(user3).build(),
                    Score.builder().score(999).user(user4).build(),
                    Score.builder().score(456).user(user2).build(),
                    Score.builder().score(32).user(user4).build()))
            .build();

    Mockito.when(levelRepository.findById(1)).thenReturn(Optional.of(levelDatabase));
    Mockito.when(jwtBuilder.checkTokenAuthAndGetUserId(DUMMY_TOKEN)).thenReturn(1);

    List<ScoreByLevel> actualResponse = scoresService.getHighestScoreByLevel(1, 999, DUMMY_TOKEN);

    Assertions.assertEquals(999, actualResponse.get(0).getScore());
    Assertions.assertEquals(456, actualResponse.get(1).getScore());
    Assertions.assertEquals(32, actualResponse.get(2).getScore());
    Assertions.assertEquals(11, actualResponse.get(3).getScore());
    Assertions.assertEquals(4, actualResponse.get(0).getUserId());
    Assertions.assertEquals(2, actualResponse.get(1).getUserId());
    Assertions.assertEquals(4, actualResponse.get(2).getUserId());
    Assertions.assertEquals(3, actualResponse.get(3).getUserId());
  }

  @Test
  void getHighestScoreByLevelTestNotFound() {
    Mockito.when(jwtBuilder.checkTokenAuthAndGetUserId(DUMMY_TOKEN)).thenReturn(1);
    Mockito.when(levelRepository.findById(1)).thenReturn(Optional.empty());

    try {
      scoresService.getHighestScoreByLevel(1, 1234, DUMMY_TOKEN);
    } catch (ResponseStatusException ex) {
      Assertions.assertEquals("404 NOT_FOUND \"Level [" + 1 + "] not found\"", ex.getMessage());
      Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
  }
}
