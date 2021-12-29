package com.cookingwebsite.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.model.VwComment;

@Repository
public interface VwCommentRepository extends JpaRepository<VwComment, Integer> {
	Optional<VwComment> findByComment(String vwComment);

	List<VwComment> findByRecipeId(Integer recipeId);
	
	boolean existsByRecipeId(Integer recipeId);
}
