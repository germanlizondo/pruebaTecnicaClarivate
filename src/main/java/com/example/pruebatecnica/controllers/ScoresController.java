package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.config.Constants;
import com.example.pruebatecnica.config.UriConstants;
import com.example.pruebatecnica.dto.ScoreByLevel;
import com.example.pruebatecnica.services.ScoresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UriConstants.BASE_URI_SCORES)
public class ScoresController {

  @Autowired private ScoresService scoresService;

  @PutMapping(value = UriConstants.ADD_SCORE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addScore(
      @RequestHeader(Constants.SESSION_KEY) final String token,
      @PathVariable(Constants.LEVEL_ID) final Integer levelId,
      @PathVariable(Constants.USER_SCORE) final Integer userScore) {
    log.info("Add score to level: [{}], score: [{}]", levelId, userScore);
    scoresService.addScore(levelId, userScore, token);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<ScoreByLevel> getListOfUsersScore(
      @RequestHeader(Constants.SESSION_KEY) final String token,
      @PathVariable(Constants.LEVEL_ID) final Integer levelId,
      @RequestParam(name = Constants.FILTER, required = false) final Integer highestScore) {
    log.info(
        "Getting highest score by the level: [{}], and highestscore: [{}]", levelId, highestScore);
    return scoresService.getHighestScoreByLevel(levelId, highestScore, token);
  }
}
