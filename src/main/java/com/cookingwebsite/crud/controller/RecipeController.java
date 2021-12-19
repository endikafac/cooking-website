package com.cookingwebsite.crud.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.dto.RecipeDTO;
import com.cookingwebsite.crud.model.Recipe;
import com.cookingwebsite.crud.service.RecipeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Recipe", description = "Recipes API")
@RestController
@RequestMapping("/recipe")
//@CrossOrigin(origins = "http://localhost:4200")
public class RecipeController {

	// private static Logger LOGGER =
	// Logger.getLogger(CategoriaController.class.getName());
	
	@Autowired
	RecipeService recipeService;
	
	
	@GetMapping("/list")
	public ResponseEntity<List<Recipe>> list() {
		final List<Recipe> list = recipeService.list();
		return new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Recipe recipe = recipeService.getOne(id).get();
		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}
	
	@GetMapping("/detailname/{name}")
	public ResponseEntity<?> getByNombre(@PathVariable("name") final String name) {
		if (!recipeService.existsByNombre(name)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		final Recipe recipe = recipeService.getByName(name).get();
		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody final RecipeDTO recipeDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(recipeDTO.getName())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The name is mandatory"),
					HttpStatus.BAD_REQUEST);
		}
		if (recipeService.existsByNombre(recipeDTO.getName())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("This name already exists"),
					HttpStatus.BAD_REQUEST);
		}
		
		final Recipe recipe = new Recipe(recipeDTO.getName(), recipeDTO.getDescription(), recipeDTO.getUser());
		recipeService.save(recipe);
		responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("Recipe created"), HttpStatus.OK);
		return responseEntity;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") final int id, @RequestBody final RecipeDTO recipeDTO) {
		if (!recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe not exist"), HttpStatus.NOT_FOUND);
		}
		/*
		 * if(categoriaService.existsByNombre(categoriaDto.getNombre()) &&
		 * categoriaService.getByNombre(categoriaDto.getNombre()).get().getId() == id) {
		 * return new ResponseEntity<MensajeDTO>(new MensajeDTO("ese nombre ya existe"),
		 * HttpStatus.BAD_REQUEST); }
		 */
		if (StringUtils.isBlank(recipeDTO.getName())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("name is mandatory "), HttpStatus.BAD_REQUEST);
		}
		
		final Recipe recipe = recipeService.getOne(id).get();
		recipe.setName(recipeDTO.getName());
		recipe.setDescription(recipeDTO.getDescription());
		recipe.setUser(recipeDTO.getUser());
		recipe.setAuModificationUser(recipeDTO.getAuModificationUser());
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		recipe.setAuModificationDate(fechaActual);
		recipeService.save(recipe);
		return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe updated"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe not exist"), HttpStatus.NOT_FOUND);
		}
		
		recipeService.delete(id);
		return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe deleted"), HttpStatus.OK);
	}
	
}
