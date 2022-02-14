package com.example.pruebatecnica.builders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtBuilder {

  @Autowired private Key jwtKey;

  public String buildToken(final Integer userId) {
    return Jwts.builder()
        .setSubject(userId.toString())
        .signWith(jwtKey)
        .setExpiration(getDAteInTenMinutes())
        .compact();
  }

  public Integer checkTokenAuthAndGetUserId(final String token) {

    Jws<Claims> jws;
    try {
      jws = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(token);

      return Integer.parseInt(jws.getBody().get("sub").toString());

    } catch (JwtException e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Require authentication");
    }
  }

  private Date getDAteInTenMinutes() {
    Calendar date = Calendar.getInstance();
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    long timeInSecs = date.getTimeInMillis();
    return new Date(timeInSecs + (10 * 60 * 1000));
  }
}
