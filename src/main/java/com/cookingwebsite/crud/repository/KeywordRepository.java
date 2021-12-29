package com.cookingwebsite.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.model.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
	List<Keyword> findByKeyword(String keyword);

	boolean existsByKeyword(String keyword);
}
