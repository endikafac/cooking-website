package com.cookingwebsite.crud.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.cookingwebsite.crud.dto.KeywordDTO;
import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.dto.RecipeDTO;
import com.cookingwebsite.crud.dto.RecipeFilterDTO;
import com.cookingwebsite.crud.model.Comment;
import com.cookingwebsite.crud.model.Keyword;
import com.cookingwebsite.crud.model.Recipe;
import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.service.UserService;
import com.cookingwebsite.crud.service.CommentService;
import com.cookingwebsite.crud.service.KeywordService;
import com.cookingwebsite.crud.service.RecipeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Recipe", description = "Recipes API")
@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins = "*")
@Slf4j
public class RecipeController {
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	CommentService commentService;

	@Autowired
	UserService userService;
	
	@GetMapping("/list")
	public ResponseEntity<List<Recipe>> list() {
		List<Recipe> list = new ArrayList<Recipe>();
		
		try {
			list = this.recipeService.list();
		} catch (Exception e) {
			log.error("-- list -".concat(e.getMessage()));
		}
		
		return new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") final int id) {
		if (!this.recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Recipe recipe = new Recipe();

		try {
			recipe = this.recipeService.getOne(id).get();
		} catch (Exception e) {
			log.error("-- getById -".concat(e.getMessage()));
		}

		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}

	@GetMapping("/detailname/{name}")
	public ResponseEntity<?> getByNombre(@PathVariable("name") final String name) {
		if (!this.recipeService.existsByName(name)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("It does not exist"), HttpStatus.NOT_FOUND);
		}
		Recipe recipe = new Recipe();
		
		try {
			recipe = this.recipeService.getByName(name).get();
		} catch (Exception e) {
			log.error("-- getByNombre -".concat(e.getMessage()));
		}

		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_CHEF')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody final RecipeDTO recipeDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(recipeDTO.getName())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The name is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (this.recipeService.existsByName(recipeDTO.getName())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("This name already exists"), HttpStatus.BAD_REQUEST);
		}

		final Recipe recipe = new Recipe(recipeDTO.getName(), recipeDTO.getDescription(), recipeDTO.getUser());
		if (recipeDTO.getAuCreationUser() != null) {
			recipe.setAuCreationUser(recipeDTO.getAuCreationUser());
		}
		/*
		 * if (recipeDTO.getAuCreationDate() != null) { recipe.setAuCreationDate(recipeDTO.getAuCreationDate()); }
		 */
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		recipe.setAuCreationDate(fechaActual);

		Set<Keyword> keywords = this.parseListKeywordsDTOKeyword(recipeDTO.getKeywords());
		recipe.setKeywords(keywords);
		
		try {
			this.recipeService.save(recipe);
		} catch (Exception e) {
			log.error("-- create -".concat(e.getMessage()));
		}

		responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("Recipe created"), HttpStatus.OK);
		return responseEntity;
	}
	
	// @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_CHEF')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") final int id, @RequestBody final RecipeDTO recipeDTO) {
		if (!this.recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe not exist"), HttpStatus.NOT_FOUND);
		}
		/*
		 * if(categoriaService.existsByNombre(categoriaDto.getNombre()) &&
		 * categoriaService.getByNombre(categoriaDto.getNombre()).get().getId() == id) { return new
		 * ResponseEntity<MensajeDTO>(new MensajeDTO("ese nombre ya existe"), HttpStatus.BAD_REQUEST); }
		 */
		if (StringUtils.isBlank(recipeDTO.getName())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("name is mandatory "), HttpStatus.BAD_REQUEST);
		}

		Recipe recipe = new Recipe();

		try {
			recipe = this.recipeService.getOne(id).get();
		} catch (Exception e) {
			log.error("-- update -".concat(e.getMessage()));
		}
		
		recipe.setName(recipeDTO.getName());
		recipe.setDescription(recipeDTO.getDescription());
		recipe.setUser(recipeDTO.getUser());
		recipe.setAuModificationUser(recipeDTO.getAuModificationUser());
		final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
		recipe.setAuModificationDate(fechaActual);
		
		Set<Keyword> keywords = this.parseListKeywordsDTOKeyword(recipeDTO.getKeywords());
		recipe.setKeywords(keywords);
		
		Set<Comment> comments = this.parseListCommentsDTOComment(recipeDTO.getComments());
		recipe.setComments(comments);
		try {
			this.recipeService.save(recipe);
		} catch (Exception e) {
			log.error("Error searching recipes (method-recipeService.update)...".concat(e.getMessage()));
		}

//		this.deleteKeywords(recipe.getKeywords());
//		this.deleteComments(recipe.getComments());
		
		return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe updated"), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_CHEF')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") final int id) {
		if (!this.recipeService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe not exist"), HttpStatus.NOT_FOUND);
		}
		
		try {
			this.recipeService.delete(id);
		} catch (Exception e) {
			log.error("-- delete -".concat(e.getMessage()));
		}

		return new ResponseEntity<MessageDTO>(new MessageDTO("Recipe deleted"), HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody final RecipeFilterDTO recipeFilterDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(recipeFilterDTO.getFilter())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The filter is mandatory"), HttpStatus.BAD_REQUEST);
		}
		Integer id = null;
		String name = null;
		String description = null;
		if (recipeFilterDTO.getFilter() != null) {
			name = recipeFilterDTO.getFilter();
			description = recipeFilterDTO.getFilter();
			try {
				id = Integer.parseInt(recipeFilterDTO.getFilter());
			} catch (Exception e) {
				id = 0;
			}
		}
		
		List<Recipe> list = new ArrayList<Recipe>();
		try {
			list = this.recipeService.search(id, name, description);
		} catch (Exception e) {
			log.error("Error searching recipes (method-recipeService.search)...".concat(e.getMessage()));
		}
		
		List<User> userList = new ArrayList<User>();
		try {
			userList = this.userService.search(recipeFilterDTO.getFilter());
			
		} catch (Exception e) {
			log.error("Error searching recipes...".concat(e.getMessage()));
		}
		if (!userList.isEmpty()) {
			List<Recipe> listAux = new ArrayList<Recipe>();
			for (User userAux : userList) {
				try {
					listAux = this.recipeService.searchByUser(userAux);
				} catch (Exception e) {
					log.error("Error searching recipes  (method-recipeService.searchByUser)...".concat(e.getMessage()));
				}
				if (!listAux.isEmpty()) {
					for (Recipe recipeAux : listAux) {
						if (!list.contains(recipeAux)) {
							list.add(recipeAux);
						}
					}
				}
			}
		}
		responseEntity = new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
		
		return responseEntity;
	}

	@PostMapping("/findByFilters")
	public ResponseEntity<?> findByFilters(@RequestBody final RecipeFilterDTO recipeFilterDTO) {
		ResponseEntity<?> responseEntity = null;
		if (StringUtils.isBlank(recipeFilterDTO.getFilter())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The filter is mandatory"), HttpStatus.BAD_REQUEST);
		}
		String name = null;
		String description = null;
		if (recipeFilterDTO.getFilter() != null) {
			name = recipeFilterDTO.getFilter();
			description = recipeFilterDTO.getFilter();
		}
		
		List<Recipe> list = new ArrayList<Recipe>();
		
		try {
			list = this.recipeService.searchByNameOrDescription(name, description);
		} catch (Exception e) {
			log.error("-- findByFilters -".concat(e.getMessage()));
		}
		
		responseEntity = new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
		
		return responseEntity;
	}
	
	@PostMapping("/checkIfUserHasOwnRecipesOrComments")
	public ResponseEntity<?> checkIfUserHasOwnRecipesOrComments(@RequestBody final RecipeFilterDTO recipeFilterDTO) {
		ResponseEntity<?> responseEntity = null;
		Integer userId = 0;
		if (StringUtils.isBlank(recipeFilterDTO.getFilter())) {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The filter is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (recipeFilterDTO.getFilter() != null) {
			try {
				userId = Integer.parseInt(recipeFilterDTO.getFilter());
			} catch (Exception e) {
				userId = 0;
			}
		}
		
		List<Recipe> list = new ArrayList<Recipe>();
		
		try {
			list = this.recipeService.checkIfUserHasOwnRecipesOrComments(userId);
		} catch (Exception e) {
			log.error("-- checkIfUserHasOwnRecipesOrComments -".concat(e.getMessage()));
		}

		responseEntity = new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
		
		return responseEntity;
	}

	@PostMapping("/searchByKeyword")
	public ResponseEntity<?> searchByKeyword(@RequestBody final List<RecipeFilterDTO> recipeFilterDTOs) {
		ResponseEntity<?> responseEntity = null;
		List<Recipe> list = new ArrayList<Recipe>();

		if ((recipeFilterDTOs != null) && !recipeFilterDTOs.isEmpty()) {
			for (RecipeFilterDTO recipeFilterDTO : recipeFilterDTOs) {
				if (StringUtils.isBlank(recipeFilterDTO.getFilter())) {
					responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The filter is mandatory"), HttpStatus.BAD_REQUEST);
				}
			}
		} else {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("It must be an array and cannot be empty."),
					HttpStatus.BAD_REQUEST);
		}
		if (responseEntity == null) {
			String keyword = null;
			if ((recipeFilterDTOs != null) && !recipeFilterDTOs.isEmpty()) {
				for (RecipeFilterDTO recipeFilterDTO : recipeFilterDTOs) {
					if (recipeFilterDTO.getFilter() != null) {
						keyword = recipeFilterDTO.getFilter();
						if (keyword != null) {
							List<Recipe> listAux = new ArrayList<Recipe>();
							try {
								listAux = this.recipeService.searchByKeyword(keyword);
							} catch (Exception e) {
								log.error("Error searching recipes (method-recipeService.searchByKeyword)...".concat(e.getMessage()));
							}
							if (!listAux.isEmpty()) {
								for (Recipe recipeAux : listAux) {
									if (!list.contains(recipeAux)) {
										list.add(recipeAux);
									}
								}
							}
						}

					}
				}
			}
			responseEntity = new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
		}
		return responseEntity;
	}

	@PostMapping("/searchByKeywordId")
	public ResponseEntity<?> searchByKeywordId(@RequestBody final List<RecipeFilterDTO> recipeFilterDTOs) {
		ResponseEntity<?> responseEntity = null;
		List<Recipe> list = new ArrayList<Recipe>();

		if ((recipeFilterDTOs != null) && !recipeFilterDTOs.isEmpty()) {
			for (RecipeFilterDTO recipeFilterDTO : recipeFilterDTOs) {
				if (StringUtils.isBlank(recipeFilterDTO.getFilter())) {
					responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("The filter is mandatory"), HttpStatus.BAD_REQUEST);
				}
			}
		} else {
			responseEntity = new ResponseEntity<MessageDTO>(new MessageDTO("It must be an array and cannot be empty."),
					HttpStatus.BAD_REQUEST);
		}
		if (responseEntity == null) {
			Integer keywordId = null;
			if ((recipeFilterDTOs != null) && !recipeFilterDTOs.isEmpty()) {
				for (RecipeFilterDTO recipeFilterDTO : recipeFilterDTOs) {
					if (recipeFilterDTO.getFilter() != null) {
						try {
							keywordId = Integer.parseInt(recipeFilterDTO.getFilter());
						} catch (Exception e) {
							keywordId = null;
						}
						if (keywordId != null) {
							List<Recipe> listAux = new ArrayList<Recipe>();
							try {
								listAux = this.recipeService.searchByKeywordId(keywordId);
							} catch (Exception e) {
								log.error("Error searching recipes (method-recipeService.searchByKeywordId)...".concat(e.getMessage()));
							}
							if (!listAux.isEmpty()) {
								for (Recipe recipeAux : listAux) {
									if (!list.contains(recipeAux)) {
										list.add(recipeAux);
									}
								}
							}
						}
					}
				}
			}
			responseEntity = new ResponseEntity<List<Recipe>>(list, HttpStatus.OK);
		}
		return responseEntity;
	}
	
	/**
	 * @param KeywordsDTOList
	 * @return Set<Keyword>
	 */
	public Set<Keyword> parseListKeywordsDTOKeyword(Set<KeywordDTO> keywordsDTO) {
		Set<Keyword> keywords = new HashSet<Keyword>();
		if ((keywordsDTO != null) && (!keywordsDTO.isEmpty())) {
			for (KeywordDTO keywordDTO : keywordsDTO) {
				final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
				Keyword keyword = new Keyword(keywordDTO.getKeyword(), keywordDTO.getAuCreationUser(), fechaActual);
				
				if (!keywords.contains(keyword)) {
					keywords.add(keyword);
				}
			}
		}
		return keywords;

	}
	
	/**
	 * @param keywords
	 * @return boolean
	 */
	public boolean deleteKeywords(Set<Keyword> keywords) {
		boolean isCorrect = false;
		if ((keywords != null) && (!keywords.isEmpty())) {
			for (Keyword keyword : keywords) {
				try {
					this.keywordService.delete(keyword.getId());
				} catch (Exception e) {
					log.error("-- deleteKeywords -".concat(e.getMessage()));
				}

			}
			isCorrect = true;
		}
		return isCorrect;

	}
	
	/**
	 * @param commentsDTO
	 * @return Set<Comment>
	 */
	public Set<Comment> parseListCommentsDTOComment(Set<CommentDTO> commentsDTO) {
		Set<Comment> comments = new HashSet<Comment>();
		if ((commentsDTO != null) && (!commentsDTO.isEmpty())) {
			for (CommentDTO commentDTO : commentsDTO) {
				final Timestamp fechaActual = new Timestamp(Calendar.getInstance().getTimeInMillis());
				
				Comment comment = new Comment(commentDTO.getComment(), commentDTO.getAuCreationUser(), fechaActual);
				if (!comments.contains(comment)) {
					comments.add(comment);
				}
			}
		}
		return comments;

	}

	/**
	 * @param keywords
	 * @return boolean
	 */
	public boolean deleteComments(Set<Comment> comments) {
		boolean isCorrect = false;
		if ((comments != null) && (!comments.isEmpty())) {
			for (Comment comment : comments) {

				try {
					this.commentService.delete(comment.getId());
				} catch (Exception e) {
					log.error("-- deleteKeywords -".concat(e.getMessage()));
				}

			}
			isCorrect = true;
		}
		return isCorrect;

	}
}
