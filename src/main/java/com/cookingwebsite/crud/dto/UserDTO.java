package com.cookingwebsite.crud.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
	
	private int id;
	private String username;
	private String password;
	private Integer auCreationUser;
	private Timestamp auCreationDate;
	private Integer auModificationUser;
	private Timestamp auModificationDate;
	private Boolean auActive;
}
