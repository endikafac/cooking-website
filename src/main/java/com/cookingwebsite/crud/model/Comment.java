package com.cookingwebsite.crud.model;

import java.sql.Timestamp;
import java.util.Calendar;

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
@Table(name = "Comment", schema = "cookingwebsite")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "recipe_id", referencedColumnName = "id")
//	private Recipe recipe;
	
	@Column(nullable = false, length = 1000)
	private String comment;
	
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
	 * @param comment
	 * @param auCreationUser
	 */
	public Comment(final String comment, final Integer auCreationUser) {
		super();
		this.comment = comment;
		/* this.user = user; */
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;

	}
	
	/**
	 * @param comment
	 * @param auCreationUser
	 * @param auCreationDate
	 */
	public Comment(final String comment, final Integer auCreationUser, final Timestamp auCreationDate) {
		super();
		this.comment = comment;
		/* this.user = user; */
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		if (auCreationDate != null) {
			this.auCreationDate = auCreationDate;
		} else {
			this.auCreationDate = currentDate;
		}
		this.auActive = true;

	}
	
}
