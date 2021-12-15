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
@Table(name = "User", schema = "atenea")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 50, unique = true)
	private String username;
	
	@Column(nullable = true, length = 10)
	private String password;
	
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
	 * @param descripcion
	 */
	public User(final String username, final String password, final Integer auCreationUser) {
		super();
		this.username = username;
		this.password = password;
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;

	}
	
}
