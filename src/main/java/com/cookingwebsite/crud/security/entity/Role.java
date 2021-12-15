package com.cookingwebsite.crud.security.entity;

import com.cookingwebsite.crud.security.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role", schema = "cookingwebsite")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RoleName roleName;
    
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
}
