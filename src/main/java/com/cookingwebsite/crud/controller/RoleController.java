package com.cookingwebsite.crud.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MensajeDTO;
import com.cookingwebsite.crud.dto.RoleDTO;
import com.cookingwebsite.crud.model.Role;
import com.cookingwebsite.crud.service.RoleService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Role", description = "Roles API")
@RestController
@RequestMapping("/role")
//@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

	// private static Logger LOGGER =
	// Logger.getLogger(CategoriaController.class.getName());
	
	@Autowired
	RoleService roleService;
	
	@GetMapping("/lista")
	public ResponseEntity<List<Role>> list() {
		final List<Role> list = roleService.list();
		return new ResponseEntity<List<Role>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!roleService.existsById(id)) {
			return new ResponseEntity<MensajeDTO>(new MensajeDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Role role = roleService.getOne(id).get();
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@GetMapping("/detailname/{name}")
	public ResponseEntity<?> getByNombre(@PathVariable("name") final String name) {
		if (!roleService.existsByNombre(name)) {
			return new ResponseEntity<MensajeDTO>(new MensajeDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Role role = roleService.getByName(name).get();
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody final RoleDTO roleDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(roleDTO.getName())) {
			responseEntity = new ResponseEntity<MensajeDTO>(new MensajeDTO("The name is mandatory"),
					HttpStatus.BAD_REQUEST);
		}
		if (roleService.existsByNombre(roleDTO.getName())) {
			responseEntity = new ResponseEntity<MensajeDTO>(new MensajeDTO("This name already exists"),
					HttpStatus.BAD_REQUEST);
		}
		
		final Role role = new Role(roleDTO.getName(), roleDTO.getDescription(), roleDTO.getAuCreationUser());
		roleService.save(role);
		responseEntity = new ResponseEntity<MensajeDTO>(new MensajeDTO("Role created"), HttpStatus.OK);
		return responseEntity;
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") final int id, @RequestBody final RoleDTO roleDTO) {
		if (!roleService.existsById(id)) {
			return new ResponseEntity<MensajeDTO>(new MensajeDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		/*
		 * if(categoriaService.existsByNombre(categoriaDto.getNombre()) &&
		 * categoriaService.getByNombre(categoriaDto.getNombre()).get().getId() == id) {
		 * return new ResponseEntity<MensajeDTO>(new MensajeDTO("ese nombre ya existe"),
		 * HttpStatus.BAD_REQUEST); }
		 */
		if (StringUtils.isBlank(roleDTO.getName())) {
			return new ResponseEntity<MensajeDTO>(new MensajeDTO("name is mandatory "), HttpStatus.BAD_REQUEST);
		}
		
		final Role role = roleService.getOne(id).get();
		role.setName(roleDTO.getName());
		role.setDescription(roleDTO.getDescription());
		role.setAuModificationUser(roleDTO.getAuModificationUser());
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		role.setAuModificationDate(fechaActual);
		roleService.save(role);
		return new ResponseEntity<MensajeDTO>(new MensajeDTO("role updated"), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!roleService.existsById(id)) {
			return new ResponseEntity<MensajeDTO>(new MensajeDTO("no existe"), HttpStatus.NOT_FOUND);
		}
		
		roleService.delete(id);
		return new ResponseEntity<MensajeDTO>(new MensajeDTO("role deleted"), HttpStatus.OK);
	}
	
}
