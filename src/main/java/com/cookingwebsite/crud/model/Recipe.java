package com.cookingwebsite.crud.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cookingwebsite.crud.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipe", schema = "cookingwebsite")
public class Recipe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	//	cascade=CascadeType.ALL
	@OneToMany(targetEntity=Keyword.class, mappedBy="recipe", fetch = FetchType.LAZY)
    private List<Keyword> keywords; 
	
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
	public Recipe(final String name, final String description, final User user) {
		super();
		this.name = name;
		this.description = description;
		this.user = user;
		this.auCreationUser = user.getId();
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.auCreationDate = currentDate;
		this.auActive = true;

	}
	
}
