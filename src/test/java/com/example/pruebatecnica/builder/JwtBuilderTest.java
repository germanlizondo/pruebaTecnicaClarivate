package com.example.pruebatecnica.builder;

import com.example.pruebatecnica.builders.JwtBuilder;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;

@SpringBootTest
public class JwtBuilderTest {

  @InjectMocks private JwtBuilder jwtBuilder;
  private Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  @BeforeEach
  void setUp() {
    jwtBuilder = new JwtBuilder(jwtKey);
  }

  @Test
  void buildTokenTest() throws Exception {

    String actualToken = jwtBuilder.buildToken(1);

    Jws<Claims> jws =
        Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(actualToken);

    Assertions.assertNotNull(actualToken);
    Assertions.assertEquals("1", jws.getBody().get("sub"));
  }

  @Test
  void checkTokenAuthAndGetUserIdTest() {

    String actualToken = jwtBuilder.buildToken(1);

    Integer actualResponse = jwtBuilder.checkTokenAuthAndGetUserId(actualToken);

    Assertions.assertEquals(1, actualResponse);
  }

  @Test
  void checkTokenAuthAndGetUserIdTestTokenNotValidated() {

    String actualToken = jwtBuilder.buildToken(1);
    jwtBuilder = new JwtBuilder(Keys.secretKeyFor(SignatureAlgorithm.HS256));

    try{
      jwtBuilder.checkTokenAuthAndGetUserId(actualToken);
    }catch (ResponseStatusException ex){
      Assertions.assertEquals(
              "403 FORBIDDEN \"Require authentication\"", ex.getMessage());
      Assertions.assertEquals(HttpStatus.FORBIDDEN, ex.getStatus());
    }



  }
}
