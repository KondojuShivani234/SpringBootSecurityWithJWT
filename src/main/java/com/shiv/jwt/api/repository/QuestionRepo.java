package com.shiv.jwt.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shiv.jwt.api.entity.Question;

public interface QuestionRepo extends JpaRepository<Question,Long>{

}



