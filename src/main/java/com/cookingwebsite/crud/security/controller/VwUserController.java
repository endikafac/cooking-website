package com.cookingwebsite.crud.security.controller;

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
import com.cookingwebsite.crud.security.entity.VwUser;
import com.cookingwebsite.crud.security.service.VwUserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "VwUser", description = "Users View API")
@RestController
@RequestMapping("/vwuser")
@CrossOrigin(origins = "*")
public class VwUserController {
	
	@Autowired
	VwUserService vwUserService;
	
	@GetMapping("/list")
	public ResponseEntity<List<VwUser>> list() {
		final List<VwUser> list = this.vwUserService.list();
		return new ResponseEntity<List<VwUser>>(list, HttpStatus.OK);
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.vwUserService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final VwUser vwUser = this.vwUserService.getOne(id).get();
		return new ResponseEntity<VwUser>(vwUser, HttpStatus.OK);
	}
	
}
