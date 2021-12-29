package com.cookingwebsite.crud.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	Optional<Comment> findByComment(String comment);

	boolean existsByComment(String comment);
}
