package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Comment;
import com.cookingwebsite.crud.repository.CommentRepository;

@Service
@Transactional
public class CommentService {
	
	@Autowired
	CommentRepository commentRepository;

	public List<Comment> list() {
		return this.commentRepository.findAll();
	}
	
	public Optional<Comment> getOne(final int id) {
		return this.commentRepository.findById(id);
	}
	
	public Optional<Comment> getByComment(final String comment) {
		return this.commentRepository.findByComment(comment);
	}
	
	public void save(final Comment comment) {
		this.commentRepository.save(comment);
	}
	
	public void delete(final int id) {
		this.commentRepository.deleteById(id);
	}
	
	public boolean existsById(final int id) {
		return this.commentRepository.existsById(id);
	}
	
	public boolean existsByComment(final String comment) {
		return this.commentRepository.existsByComment(comment);
	}
	
	public void deleteInBatch(final List<Comment> comments) {
		this.commentRepository.deleteInBatch(comments);
	}
	
	/*
	 * public Optional<Comment> getComments(){ this.em.re return null;
	 *
	 * }
	 */
}
