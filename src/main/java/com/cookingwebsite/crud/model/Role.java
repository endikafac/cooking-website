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
@Table(name = "Role", schema = "atenea")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, length = 50, unique = true)
	private String name;

	@Column(nullable = true, length = 1500)
	private String description;

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
	public Role(final String name, final String description, final Integer auCreationUser) {
		super();
		this.name = name;
		this.description = description;
		this.auCreationUser = auCreationUser;
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;
		
	}

}
