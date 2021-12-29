package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.VwComment;
import com.cookingwebsite.crud.repository.VwCommentRepository;

@Service
@Transactional
public class VwCommentService {
	
	@Autowired
	VwCommentRepository vwCommentRepository;

	public List<VwComment> list() {
		return this.vwCommentRepository.findAll();
	}
	
	public Optional<VwComment> getOne(final int id) {
		return this.vwCommentRepository.findById(id);
	}
	
	public boolean existsById(final int id) {
		return this.vwCommentRepository.existsById(id);
	}
	
	public List<VwComment> getByRecipeId(final int recipeId) {
		return this.vwCommentRepository.findByRecipeId(recipeId);
	}
	
	public boolean existsByRecipeId(final int recipeId) {
		return this.vwCommentRepository.existsByRecipeId(recipeId);
	}
}
