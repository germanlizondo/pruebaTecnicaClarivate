package com.example.pruebatecnica.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Serializable {

  private static final long serialVersionUID = 8310174587553390801L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "userId")
  private User user;

  private Integer score;

  @ManyToOne
  @JoinColumn(name = "levelId")
  private Level level;
}
