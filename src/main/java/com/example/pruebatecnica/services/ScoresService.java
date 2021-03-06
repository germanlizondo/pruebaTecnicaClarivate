package com.example.pruebatecnica.services;

import com.example.pruebatecnica.builders.JwtBuilder;
import com.example.pruebatecnica.dto.ScoreByLevel;
import com.example.pruebatecnica.models.Level;
import com.example.pruebatecnica.models.Score;
import com.example.pruebatecnica.models.User;
import com.example.pruebatecnica.repositories.LevelRepository;
import com.example.pruebatecnica.repositories.ScoreRepository;
import com.example.pruebatecnica.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScoresService {

  @Autowired private JwtBuilder jwtBuilder;

  @Autowired private UserRepository userRepository;
  @Autowired private LevelRepository levelRepository;
  @Autowired private ScoreRepository scoreRepository;

  public void addScore(final Integer levelId, final Integer userScore, final String token) {

    Integer userId = jwtBuilder.checkTokenAuthAndGetUserId(token);

    Optional<User> user = userRepository.findById(userId);
    Optional<Level> level = levelRepository.findById(levelId);

    if (user.isEmpty() || level.isEmpty()) {
      log.error("User [" + userId + "] or level [" + levelId + "] not found");
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "User [" + userId + "] or level [" + levelId + "] not found");
    }

    Score score = Score.builder().score(userScore).user(user.get()).level(level.get()).build();

    scoreRepository.save(score);
    log.info(
        "Score [{}] to user [{}] and level [{}] saved successfully", userScore, userId, levelId);
  }

  public List<ScoreByLevel> getHighestScoreByLevel(
      final Integer levelId, final Integer highestScore, final String token) {

    jwtBuilder.checkTokenAuthAndGetUserId(token);

    Optional<Level> level = levelRepository.findById(levelId);

    if (level.isEmpty()) {
      log.error("Level [" + levelId + "] not found");
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Level [" + levelId + "] not found");
    }

    return level.get().getScores().stream()
        .filter(score -> highestScore == null || score.getScore() <= highestScore)
        .sorted(Comparator.comparing(Score::getScore).reversed())
        .map(
            score ->
                ScoreByLevel.builder()
                    .score(score.getScore())
                    .userId(score.getUser().getId())
                    .build())
        .collect(Collectors.toList());
  }
}
