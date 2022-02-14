package com.example.pruebatecnica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ScoreByLevel implements Serializable {

  private static final long serialVersionUID = -8275895132052110350L;

  @JsonProperty("user_id")
  private Integer userId;

  @JsonProperty("score")
  private Integer score;
}
