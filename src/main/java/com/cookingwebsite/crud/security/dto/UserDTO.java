package com.cookingwebsite.crud.security.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	@NotBlank
	private String username;
	
	private String firstname;
	
	private String lastname;
	@Email
	private String email;

	private String password;
	
	private Boolean auActive;
	
	private Set<RoleDTO> roles = new HashSet<>();
	
}
