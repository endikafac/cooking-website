package com.cookingwebsite.crud.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/role")
@CrossOrigin
@Slf4j
public class RoleController {

	@Autowired
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	RoleService roleService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<List<Role>> list() {
		List<Role> list = this.roleService.list();
		return new ResponseEntity<List<Role>>(list, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		if (!this.roleService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Role does not exist"), HttpStatus.NOT_FOUND);
		}
		Role role = this.roleService.getOne(id).get();
		
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/detail-rolename/{rolename}")
	public ResponseEntity<?> getByUsername(@PathVariable("rolename") String rolenameStr) {
		if (!this.roleService.existsByRoleName(rolenameStr)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Role does not exist"), HttpStatus.NOT_FOUND);
		}
		Role role = null;

		if (rolenameStr.equals(RoleName.ROLE_ADMIN.name())) {
			role = (this.roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
		}
		if (rolenameStr.equals(RoleName.ROLE_CHEF.name())) {
			role = (this.roleService.getByRoleName(RoleName.ROLE_CHEF).get());
		}
		if (rolenameStr.equals(RoleName.ROLE_USER.name())) {
			role = (this.roleService.getByRoleName(RoleName.ROLE_USER).get());
		}

		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

}
