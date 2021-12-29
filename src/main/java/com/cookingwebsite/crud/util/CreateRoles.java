package com.cookingwebsite.crud.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.service.RoleService;
import com.cookingwebsite.crud.security.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * VERY IMPORTANT: THIS CLASS WILL ONLY BE EXECUTED ONCE TO CREATE THE ROLES. ONCE CREATED, THE CODE MUST BE DELETED OR
 * COMMENTED OUT. THEY CAN BE LOADED FROM THE INITIAL SCRIPT TO GENERATE THE TABLES WITH DOCKER.
 *
 */

@Component
@Slf4j
public class CreateRoles implements CommandLineRunner {
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	UserService userService;
	
	@Override
	public void run(String... args) throws Exception {
		
		/**
		 * Load RoleName
		 */
		this.checkRoleExistenceAndIfNotExistCreate(RoleName.ROLE_ADMIN);
		this.checkRoleExistenceAndIfNotExistCreate(RoleName.ROLE_CHEF);
		this.checkRoleExistenceAndIfNotExistCreate(RoleName.ROLE_USER);
		
	}
	
	/**
	 * @param roleName
	 */
	private void checkRoleExistenceAndIfNotExistCreate(final RoleName roleName) {
		Optional<Role> optionalRole = null;
		try {
			optionalRole = this.roleService.getByRoleName(roleName);
		} catch (Exception e) {
			log.error("ERROR - Getting the roleName --> ".concat((roleName != null) ? roleName.name() : "is null"));
		}
		
		if ((optionalRole != null) && (optionalRole.isEmpty())) {
			Role role = new Role(roleName);
			this.roleService.save(role);
		}
	}
	
	/**
	 * @param user
	 */
	@SuppressWarnings("unused")
	private void checkUserExistenceAndIfNotExistCreate(final User user) {
		String username = user.getUsername();
		Optional<User> optionalUser = null;
		try {
			optionalUser = this.userService.getByUsername(username);
		} catch (Exception e) {
			log.error("ERROR - Getting the user --> ".concat((username != null) ? username : "is null"));
		}
		
		if ((optionalUser != null) && (optionalUser.isEmpty())) {
			this.userService.save(user);
		}
	}
}
