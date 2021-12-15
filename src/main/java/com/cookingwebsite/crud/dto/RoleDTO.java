package com.cookingwebsite.crud.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RoleDTO {

	private int id;
	private String name;
	private String description;
	private Integer auCreationUser;
	private Timestamp auCreationDate;
	private Integer auModificationUser;
	private Timestamp auModificationDate;
	private Boolean auActive;
}
