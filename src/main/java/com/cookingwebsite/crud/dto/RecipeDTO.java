package com.cookingwebsite.crud.dto;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.cookingwebsite.crud.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
	
	private int id;
	private User user;
	private String name;
	private String description;
	private Set<KeywordDTO> keywords = new HashSet<>();
	private Set<CommentDTO> comments = new HashSet<>();
	private Integer auCreationUser;
	private Timestamp auCreationDate;
	private Integer auModificationUser;
	private Timestamp auModificationDate;
	private Boolean auActive;
}
