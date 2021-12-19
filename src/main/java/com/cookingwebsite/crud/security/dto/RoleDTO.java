package com.cookingwebsite.crud.security.dto;

import javax.validation.constraints.NotBlank;

import com.cookingwebsite.crud.security.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
	
	@NotBlank
	private RoleName roleName;
	
}
