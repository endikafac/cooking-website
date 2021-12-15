package com.cookingwebsite.crud.model;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Keyword", schema = "cookingwebsite")
public class Keyword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/*
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
	*/
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
	private Recipe recipe;
		
	@Column(nullable = false, length = 50, unique = true)
	private String keyword;

	@Column(name = "au_creation_user", nullable = false)
	private Integer auCreationUser;

	@Column(name = "au_creation_date", nullable = false)
	private Timestamp auCreationDate;

	@Column(name = "au_modification_user", nullable = true)
	private Integer auModificationUser;

	@Column(name = "au_modification_date", nullable = true)
	private Timestamp auModificationDate;

	@Column(name = "au_active", nullable = false)
	private Boolean auActive;

	/**
	 * @param nombre
	 */
	public Keyword(final String keyword, final Recipe recipe, final Integer auCreationUser) {
		super();
		this.keyword = keyword;
		this.recipe = recipe;
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;
		
	}

}
