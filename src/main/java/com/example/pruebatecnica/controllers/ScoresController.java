package com.example.pruebatecnica.controllers;

import com.example.pruebatecnica.dto.ScoreByLevel;
import com.example.pruebatecnica.services.ScoresService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;

@RestController
@RequestMapping("/level/{levelId}/score")
public class ScoresController {

  @Autowired private ScoresService scoresService;

  @PutMapping(value = "/{userScore}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void addScore(
      @RequestHeader("Session-Key") final String token,
      @PathVariable("levelId") final Integer levelId,
      @PathVariable("userScore") final Integer userScore) {
    scoresService.addScore(levelId, userScore, token);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public List<ScoreByLevel> getListOfUserScore(
      @RequestHeader("Session-Key") final String token,
      @PathVariable("levelId") final Integer levelId,
      @RequestParam(name = "filter", required = false) final Integer highestScore) {
    return scoresService.getHighestScoreByLevel(levelId, highestScore, token);
  }
}
