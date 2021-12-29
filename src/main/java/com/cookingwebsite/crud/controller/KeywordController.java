package com.cookingwebsite.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.model.Keyword;
import com.cookingwebsite.crud.service.KeywordService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Keyword", description = "Keywords API")
@RestController
@RequestMapping("/keyword")
@CrossOrigin(origins = "*")
public class KeywordController {
	
	@Autowired
	KeywordService keywordService;
	
	@GetMapping("/list")
	public ResponseEntity<List<Keyword>> list() {
		final List<Keyword> list = this.keywordService.list();
		return new ResponseEntity<List<Keyword>>(list, HttpStatus.OK);
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.keywordService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Keyword keyword = this.keywordService.getOne(id).get();
		return new ResponseEntity<Keyword>(keyword, HttpStatus.OK);
	}

	@GetMapping("/detailkeyword/{keyword}")
	public ResponseEntity<?> getByNombre(@PathVariable("Keyword") final String keywordStr) {
		if (!this.keywordService.existsByKeyword(keywordStr)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Keyword keyword = this.keywordService.getByKeyword(keywordStr).get();
		return new ResponseEntity<Keyword>(keyword, HttpStatus.OK);
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_CHEF')")
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@RequestBody final KeywordDTO keywordDTO) {
//		ResponseEntity<?> responseEntity = null;
//		if (StringUtils.isBlank(keywordDTO.getKeyword())) {
//			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The keyword is mandatory"), HttpStatus.BAD_REQUEST);
//		}
//		if (this.keywordService.existsByKeyword(keywordDTO.getKeyword())) {
//			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("This keyword already exists"), HttpStatus.BAD_REQUEST);
//		}
//
//		final Keyword keyword = new Keyword(keywordDTO.getKeyword(), keywordDTO.getRecipe(), keywordDTO.getAuCreationUser());
//		this.keywordService.save(keyword);
//		responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("Keyword created"), HttpStatus.OK);
//		return responseEntity;
//	}
//
//	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_CHEF')")
//	@PutMapping("/update/{id}")
//	public ResponseEntity<?> update(@PathVariable("id") final int id, @RequestBody final KeywordDTO keywordDTO) {
//		if (!this.keywordService.existsById(id)) {
//			return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword not exist"), HttpStatus.NOT_FOUND);
//		}
//		if (StringUtils.isBlank(keywordDTO.getKeyword())) {
//			return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword is mandatory "), HttpStatus.BAD_REQUEST);
//		}
//
//		final Keyword keyword = this.keywordService.getOne(id).get();
//		keyword.setKeyword(keywordDTO.getKeyword());
//		keyword.setRecipe(keywordDTO.getRecipe());
//		keyword.setAuModificationUser(keywordDTO.getAuModificationUser());
//		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
//		keyword.setAuModificationDate(fechaActual);
//		this.keywordService.save(keyword);
//		return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword updated"), HttpStatus.OK);
//	}
//
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!this.keywordService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword not exist"), HttpStatus.NOT_FOUND);
		}

		this.keywordService.delete(id);
		return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword deleted"), HttpStatus.OK);
	}

}
