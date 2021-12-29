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
@Table(name = "Keyword", schema = "cookingwebsite")
public class Keyword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/*
	 * @OneToOne(fetch = FetchType.LAZY)
	 *
	 * @JoinColumn(name = "recipe_id", referencedColumnName = "id")
	 */
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "recipe_id", referencedColumnName = "id")
//	private Recipe recipe;

	@Column(nullable = false, length = 30)
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
	 * @param keyword
	 * @param auCreationUser
	 */
	public Keyword(final String keyword, final Integer auCreationUser) {
		super();
		this.keyword = keyword;
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;
		
	}

	/**
	 * @param keyword
	 * @param auCreationUser
	 * @param auCreationDate
	 */
	public Keyword(final String keyword, final Integer auCreationUser, final Timestamp auCreationDate) {
		super();
		this.keyword = keyword;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Keyword other = (Keyword) obj;
		if (this.keyword == null) {
			if (other.keyword != null) {
				return false;
			}
		} else if (!this.keyword.equals(other.keyword)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.keyword == null) ? 0 : this.keyword.hashCode());
		return result;
	}
	
}
