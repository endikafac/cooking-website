package com.cookingwebsite.crud.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vw_Comment", schema = "cookingwebsite")
public class VwComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "recipe_id", nullable = true)
	private Integer recipeId;
	
	@Column(nullable = false, length = 1000)
	private String comment;

	@Column(name = "last_date", nullable = true)
	private Timestamp lastDate;

	@Column(name = "author_id", nullable = true)
	private Integer authorId;

	@Column(name = "author", nullable = true)
	private String author;
	
	@Column(name = "author_username", nullable = true)
	private String authorUsername;

}