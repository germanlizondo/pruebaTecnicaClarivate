package com.example.pruebatecnica.repositories;

import com.example.pruebatecnica.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {}
