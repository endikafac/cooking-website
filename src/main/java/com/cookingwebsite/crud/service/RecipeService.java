package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Recipe;
import com.cookingwebsite.crud.repository.RecipeRepository;

@Service
@Transactional
public class RecipeService {

	@Autowired
	RecipeRepository recipeRepository;

	public List<Recipe> list() {
		return recipeRepository.findAll();
	}

	public Optional<Recipe> getOne(final int id) {
		return recipeRepository.findById(id);
	}

	public Optional<Recipe> getByName(final String name) {
		return recipeRepository.findByName(name);
	}

	public void save(final Recipe recipe) {
		recipeRepository.save(recipe);
	}

	public void delete(final int id) {
		recipeRepository.deleteById(id);
	}

	public boolean existsById(final int id) {
		return recipeRepository.existsById(id);
	}

	public boolean existsByNombre(final String name) {
		return recipeRepository.existsByName(name);
	}
}
