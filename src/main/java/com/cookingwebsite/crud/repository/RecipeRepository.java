package com.cookingwebsite.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	Optional<Recipe> findByName(String name);
	
	boolean existsByName(String name);
}
