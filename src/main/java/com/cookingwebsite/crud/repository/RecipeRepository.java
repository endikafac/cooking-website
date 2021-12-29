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

	boolean existsByName(String name);
}
