package com.cookingwebsite.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.model.Recipe;
import com.cookingwebsite.crud.security.entity.User;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	Optional<Recipe> findByName(String name);
	
	List<Recipe> findByNameOrDescription(String name, String description);

	@Query("SELECT r FROM Recipe r WHERE r.name LIKE %?1% OR r.description LIKE %?2%")
	public List<Recipe> searchByNameOrDescription(String name, String description);

	@Query("SELECT r FROM Recipe r WHERE r.id = ?1 OR r.name LIKE %?2% OR r.description LIKE %?3%")
	public List<Recipe> search(Integer id, String name, String description);
	
	@Query("SELECT r FROM Recipe r WHERE r.user = ?1")
	public List<Recipe> searchByUser(User user);

	@Query(nativeQuery = true, value = "SELECT r.* FROM Recipe r INNER JOIN RecipeKeyword rc ON rc.recipe_id = r.id INNER JOIN Keyword k ON k.id = rc.keyword_id AND k.keyword LIKE %?1%")
	public List<Recipe> searchByKeyword(String keyword);
	
	@Query(nativeQuery = true, value = "SELECT r.* FROM Recipe r INNER JOIN RecipeKeyword rc ON rc.recipe_id = r.id AND rc.keyword_id = ?1")
	public List<Recipe> searchByKeywordId(Integer keywordId);

	@Query(nativeQuery = true, value = "SELECT r.* FROM Recipe r LEFT JOIN RecipeComment rc ON rc.recipe_id = r.id LEFT JOIN Comment c ON c.id = rc.comment_id WHERE r.user_id = ?1 OR c.au_creation_user = ?1 OR c.au_modification_user = ?1")
	public List<Recipe> checkIfUserHasOwnRecipesOrComments(Integer userId);
	
	boolean existsByName(String name);
}
