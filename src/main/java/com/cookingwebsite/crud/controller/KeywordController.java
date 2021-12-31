package com.cookingwebsite.crud.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.KeywordDTO;
import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.model.Keyword;
import com.cookingwebsite.crud.service.KeywordService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Keyword", description = "Keywords API")
@RestController
@RequestMapping("/keyword")
@CrossOrigin(origins = "*")
@Slf4j
public class KeywordController {
	
	@Autowired
	KeywordService keywordService;
	
	@GetMapping("/list")
	public ResponseEntity<List<Keyword>> list() {
		List<Keyword> list = new ArrayList<Keyword>();

		try {
			list = this.keywordService.list();
		} catch (Exception e) {
			log.error("-- list -".concat(e.getMessage()));
		}

		return new ResponseEntity<List<Keyword>>(list, HttpStatus.OK);
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.keywordService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Keyword keyword = new Keyword();

		try {
			keyword = this.keywordService.getOne(id).get();
		} catch (Exception e) {
			log.error("-- getById -".concat(e.getMessage()));
		}

		return new ResponseEntity<Keyword>(keyword, HttpStatus.OK);
	}

	@GetMapping("/detailkeyword/{keyword}")
	public ResponseEntity<?> getByNombre(@PathVariable("Keyword") final String keywordStr) {
		if (!this.keywordService.existsByKeyword(keywordStr)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Keyword keyword = new Keyword();

		try {
			keyword = this.keywordService.getByKeyword(keywordStr).get(0);

		} catch (Exception e) {
			log.error("-- getByNombre -".concat(e.getMessage()));
		}
		
		return new ResponseEntity<Keyword>(keyword, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody final KeywordDTO keywordDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(keywordDTO.getKeyword())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The keyword is mandatory"), HttpStatus.BAD_REQUEST);
		}
		
		List<Keyword> list = new ArrayList<Keyword>();
		try {
			list = this.keywordService.getByKeyword(keywordDTO.getKeyword());
		} catch (Exception e) {
			log.error("Error searching keywords (method-keywordService.getByKeyword)...".concat(e.getMessage()));
		}
		responseEntity = new ResponseEntity<List<Keyword>>(list, HttpStatus.OK);

		return responseEntity;
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!this.keywordService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword not exist"), HttpStatus.NOT_FOUND);
		}
		
		try {
			this.keywordService.delete(id);
		} catch (Exception e) {
			log.error("-- delete -".concat(e.getMessage()));
		}
		
		return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword deleted"), HttpStatus.OK);
	}

	@DeleteMapping("/clean")
	public ResponseEntity<?> clean() {

		try {
			this.keywordService.clean();
		} catch (Exception e) {
			log.error("-- clean -".concat(e.getMessage()));
		}
		
		return new ResponseEntity<MessageDTO>(new MessageDTO("Keyword cleaned"), HttpStatus.OK);
	}
	
	@GetMapping("/listdistinct")
	public ResponseEntity<List<Keyword>> listDistinct() {
		List<Keyword> list = new ArrayList<Keyword>();

		try {
			list = this.keywordService.listDistinct();
		} catch (Exception e) {
			log.error("-- listDistinct -".concat(e.getMessage()));
		}

		return new ResponseEntity<List<Keyword>>(list, HttpStatus.OK);
	}

}
