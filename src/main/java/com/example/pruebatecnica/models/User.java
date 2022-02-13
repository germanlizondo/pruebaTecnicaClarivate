package com.example.pruebatecnica.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private static final long serialVersionUID = -1300156129252672397L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String username;

  private String password;
}
