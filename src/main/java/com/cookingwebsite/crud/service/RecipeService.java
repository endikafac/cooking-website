package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Recipe;
import com.cookingwebsite.crud.repository.RecipeRepository;
import com.cookingwebsite.crud.security.entity.User;

@Service
@Transactional
public class RecipeService {

	@Autowired
	RecipeRepository recipeRepository;

	public List<Recipe> list() {
		return this.recipeRepository.findAll();
	}

	public Optional<Recipe> getOne(final int id) {
		return this.recipeRepository.findById(id);
	}

	public Optional<Recipe> getByName(final String name) {
		return this.recipeRepository.findByName(name);
	}

	public List<Recipe> searchByNameOrDescription(final String name, final String description) {
		return this.recipeRepository.searchByNameOrDescription(name, description);
	}

	public List<Recipe> search(final Integer id, final String name, final String description) {
		return this.recipeRepository.search(id, name, description);
	}
	
	public List<Recipe> searchByUser(final User user) {
		return this.recipeRepository.searchByUser(user);
	}

	public void save(final Recipe recipe) {
		this.recipeRepository.save(recipe);
	}

	public void delete(final int id) {
		this.recipeRepository.deleteById(id);
	}

	public boolean existsById(final int id) {
		return this.recipeRepository.existsById(id);
	}

	public boolean existsByName(final String name) {
		return this.recipeRepository.existsByName(name);
	}
}
