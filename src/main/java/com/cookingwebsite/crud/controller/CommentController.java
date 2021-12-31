package com.cookingwebsite.crud.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.CommentDTO;
import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.model.Comment;
import com.cookingwebsite.crud.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Comment", description = "Comments API")
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*")
@Slf4j
public class CommentController {

	@Autowired
	CommentService commentService;

	@GetMapping("/list")
	public ResponseEntity<List<Comment>> list() {
		final List<Comment> list = this.commentService.list();
		return new ResponseEntity<List<Comment>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.commentService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Comment comment = new Comment();
		try {
			comment = this.commentService.getOne(id).get();
		} catch (Exception e) {
			log.error("-- getById -".concat(e.getMessage()));
		}
		return new ResponseEntity<Comment>(comment, HttpStatus.OK);
	}
	
	@GetMapping("/detailcomment/{comment}")
	public ResponseEntity<?> getByNombre(@PathVariable("comment") final String commentStr) {
		if (!this.commentService.existsByComment(commentStr)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Comment comment = new Comment();
		try {
			this.commentService.getByComment(commentStr).get();
		} catch (Exception e) {
			log.error("-- getByNombre -".concat(e.getMessage()));
		}
		return new ResponseEntity<Comment>(comment, HttpStatus.OK);
	}
	
	// @PreAuthorize("hasRole('ROLE_ADMIN','ROLE_CHEF')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody final CommentDTO commentDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(commentDTO.getComment())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The comment is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (this.commentService.existsByComment(commentDTO.getComment())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("This comment already exists"), HttpStatus.BAD_REQUEST);
		}
		
		final Comment comment = new Comment(commentDTO.getComment(), commentDTO.getAuCreationUser());
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		comment.setAuCreationDate(fechaActual);
		
		try {
			this.commentService.save(comment);
		} catch (Exception e) {
			log.error("-- create -".concat(e.getMessage()));
		}

		responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("Comment created"), HttpStatus.OK);
		return responseEntity;
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") final int id, @RequestBody final CommentDTO commentDTO) {
		if (!this.commentService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Comment not exist"), HttpStatus.NOT_FOUND);
		}
		if (StringUtils.isBlank(commentDTO.getComment())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("comment is mandatory "), HttpStatus.BAD_REQUEST);
		}

		final Comment comment = this.commentService.getOne(id).get();
		comment.setComment(commentDTO.getComment());
		comment.setAuModificationUser(commentDTO.getAuModificationUser());
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		comment.setAuModificationDate(fechaActual);
		try {
			this.commentService.save(comment);
		} catch (Exception e) {
			log.error("-- update -".concat(e.getMessage()));
		}
		return new ResponseEntity<MessageDTO>(new MessageDTO("Comment updated"), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!this.commentService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Comment not exist"), HttpStatus.NOT_FOUND);
		}

		try {
			this.commentService.delete(id);
		} catch (Exception e) {
			log.error("-- delete -".concat(e.getMessage()));
		}

		return new ResponseEntity<MessageDTO>(new MessageDTO("Comment deleted"), HttpStatus.OK);
	}
	
}
