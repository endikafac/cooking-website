package com.cookingwebsite.crud.dto;

import java.sql.Timestamp;
import java.util.List;

import com.cookingwebsite.crud.model.Keyword;
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
	private List<Keyword> keywords;
	private Integer auCreationUser;
	private Timestamp auCreationDate;
	private Integer auModificationUser;
	private Timestamp auModificationDate;
	private Boolean auActive;
}
