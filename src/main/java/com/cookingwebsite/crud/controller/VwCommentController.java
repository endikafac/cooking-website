package com.cookingwebsite.crud.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.model.VwComment;
import com.cookingwebsite.crud.service.VwCommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "VwComment", description = "Comments View API")
@RestController
@RequestMapping("/vwcomment")
@CrossOrigin(origins = "*")
@Slf4j
public class VwCommentController {

	@Autowired
	VwCommentService vwCommentService;

	@GetMapping("/list")
	public ResponseEntity<List<VwComment>> list() {
		List<VwComment> list = new ArrayList<VwComment>();

		try {
			list = this.vwCommentService.list();
		} catch (Exception e) {
			log.error("-- list -".concat(e.getMessage()));
		}

		return new ResponseEntity<List<VwComment>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.vwCommentService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		VwComment vwComment = new VwComment();
		
		try {
			vwComment = this.vwCommentService.getOne(id).get();
		} catch (Exception e) {
			log.error("-- getById -".concat(e.getMessage()));
		}
		
		return new ResponseEntity<VwComment>(vwComment, HttpStatus.OK);
	}

	@GetMapping("/detailrecipe/{recipeId}")
	public ResponseEntity<?> getByRecipeId(@PathVariable("recipeId") final int recipeId) {
		if (!this.vwCommentService.existsByRecipeId(recipeId)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		
		List<VwComment> vwComments = new ArrayList<VwComment>();

		try {
			vwComments = this.vwCommentService.getByRecipeId(recipeId);
		} catch (Exception e) {
			log.error("-- getByRecipeId -".concat(e.getMessage()));
		}

		return new ResponseEntity<List<VwComment>>(vwComments, HttpStatus.OK);
	}

}
