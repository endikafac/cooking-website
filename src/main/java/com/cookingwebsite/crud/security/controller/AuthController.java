package com.cookingwebsite.crud.security.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cookingwebsite.crud.dto.MessageDTO;
import com.cookingwebsite.crud.security.dto.JwtDTO;
import com.cookingwebsite.crud.security.dto.LoginUserDTO;
import com.cookingwebsite.crud.security.dto.RoleDTO;
import com.cookingwebsite.crud.security.dto.UserDTO;
import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.jwt.JwtProvider;
import com.cookingwebsite.crud.security.service.RoleService;
import com.cookingwebsite.crud.security.service.UserService;
import com.cookingwebsite.crud.util.SendEmailsUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Slf4j
public class AuthController {
	
	@Autowired
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/new")
	public ResponseEntity<?> create(@Valid @RequestBody UserDTO userDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Wrong fields or invalid email"), HttpStatus.BAD_REQUEST);
		}
		if (this.userService.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username already exists"), HttpStatus.BAD_REQUEST);
		}
		if (this.userService.existsByEmail(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email already exists"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getPassword())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This password is mandatory"), HttpStatus.BAD_REQUEST);
		}
		
		userDto.setPassword(userDto.getPassword());

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("firstname", userDto.getFirstname());
		parameters.put("username", userDto.getUsername());
		parameters.put("password", userDto.getPassword());
		parameters.put("email", userDto.getEmail());
		parameters.put("isNewUser", true);
		SendEmailsUtils.sendUserCredentials(parameters);

		String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());

		User user = new User(userDto.getUsername(), "", "", userDto.getEmail(), hashedPassword);

		Set<Role> roles = new HashSet<Role>();
		/*
		 * If the role is in the array, we insert it, otherwise the User role is added.
		 */
		if ((userDto.getRoles() != null) && (!userDto.getRoles().isEmpty())) {
			for (RoleDTO roleDTO : userDto.getRoles()) {
				if (roleDTO.getRoleName().equals(RoleName.ROLE_ADMIN)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
				}
				if (roleDTO.getRoleName().equals(RoleName.ROLE_CHEF)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_CHEF).get());
				}
				if (roleDTO.getRoleName().equals(RoleName.ROLE_USER)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_USER).get());
				}
			}
			user.setRoles(roles);
		} else {
			roles.add(this.roleService.getByRoleName(RoleName.ROLE_USER).get());
		}
		user.setRoles(roles);
		this.userService.save(user);
		
		/*
		 * It is saved first and once created, the generated user's own user identifier is saved as the creation user.
		 */
		String username = user.getUsername();
		User createdUser = this.getUserByUsername(username);
		if (createdUser != null) {
			user.setAuCreationUser(createdUser.getId());
			this.userService.save(user);
		}
		log.info("Saved user");
		return new ResponseEntity<MessageDTO>(new MessageDTO("Saved user"), HttpStatus.CREATED);
	}
	
	@PostMapping("/admin-new")
	public ResponseEntity<?> adminCreate(@Valid @RequestBody UserDTO userDto, BindingResult bindingResult) {
		
		if (bindingResult.hasFieldErrors()) {
			List<FieldError> listFieldErrors = bindingResult.getFieldErrors();
			StringBuilder msgStr = new StringBuilder("Wrong fields");
			for (FieldError fieldError : listFieldErrors) {
				msgStr.append("\n -").append(fieldError.getObjectName());
				msgStr.append(" - ").append(fieldError.getCode());
			}
			
			return new ResponseEntity<MessageDTO>(new MessageDTO(msgStr.toString()), HttpStatus.BAD_REQUEST);
		}
		if (bindingResult.hasErrors()) {
			List<ObjectError> listErrors = bindingResult.getAllErrors();
			StringBuilder msgStr = new StringBuilder("Wrong fields or invalid email");
			for (ObjectError objectError : listErrors) {
				msgStr.append("\n -").append(objectError.getObjectName());
				msgStr.append(" - ").append(objectError.getCode());
			}
			return new ResponseEntity<MessageDTO>(new MessageDTO(msgStr.toString()), HttpStatus.BAD_REQUEST);
		}
		
		if (this.userService.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username already exists"), HttpStatus.BAD_REQUEST);
		}
		if (this.userService.existsByEmail(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email already exists"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email is mandatory"), HttpStatus.BAD_REQUEST);
		}
		
		int length = 20;
		String newPassword = com.cookingwebsite.crud.util.StringUtils.randomString(length);
		
		// String newPassword = RandomStringUtils.randomAlphanumeric(15).toUpperCase();
		
		userDto.setPassword(newPassword);

		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("firstname", userDto.getFirstname());
		parameters.put("username", userDto.getUsername());
		parameters.put("password", userDto.getPassword());
		parameters.put("email", userDto.getEmail());
		parameters.put("isNewUser", true);
		SendEmailsUtils.sendUserCredentials(parameters);

		String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());
		
		User user = new User(userDto.getUsername(), userDto.getFirstname(), userDto.getLastname(), userDto.getEmail(), hashedPassword);
		
		Set<Role> roles = new HashSet<Role>();
		/*
		 * If the role is in the array, we insert it, otherwise the User role is added.
		 */
		if ((userDto.getRoles() != null) && (!userDto.getRoles().isEmpty())) {
			for (RoleDTO roleDTO : userDto.getRoles()) {
				if (roleDTO.getRoleName().equals(RoleName.ROLE_ADMIN)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
				}
				if (roleDTO.getRoleName().equals(RoleName.ROLE_CHEF)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_CHEF).get());
				}
				if (roleDTO.getRoleName().equals(RoleName.ROLE_USER)) {
					roles.add(this.roleService.getByRoleName(RoleName.ROLE_USER).get());
				}
			}
			user.setRoles(roles);
		} else {
			roles.add(this.roleService.getByRoleName(RoleName.ROLE_USER).get());
		}
		user.setRoles(roles);
		this.userService.save(user);
		
		/*
		 * It is saved first and once created, the generated user's own user identifier is saved as the creation user.
		 */
		String username = user.getUsername();
		User createdUser = this.getUserByUsername(username);
		if (createdUser != null) {
			user.setAuCreationUser(createdUser.getId());
			this.userService.save(user);
		}
		log.info("Saved user");
		return new ResponseEntity<MessageDTO>(new MessageDTO("Saved user"), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO loginUsuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("Wrong fields"), HttpStatus.BAD_REQUEST);
		}
		Authentication authentication = this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = this.jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		JwtDTO jwtDto = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity<JwtDTO>(jwtDto, HttpStatus.OK);
	}
	
	/**
	 * @param username
	 * @return
	 */
	public User getUserByUsername(final String username) {
		User user = null;
		Optional<User> optionalUser = null;
		try {
			optionalUser = this.userService.getByUsername(username);
		} catch (Exception e) {
			log.error(
					"ERROR - Getting the user --> ".concat((username != null) ? username : "is null").concat(" - ").concat(e.getMessage()));
		}
		
		if ((optionalUser != null) && (!optionalUser.isEmpty())) {
			user = optionalUser.get();
		}
		
		return user;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<List<User>> list() {
		List<User> list = this.userService.list();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/detail/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		if (!this.userService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("User does not exist"), HttpStatus.NOT_FOUND);
		}
		User user = this.userService.getOne(id).get();

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/detail-username/{username}")
	public ResponseEntity<?> getByUsername(@PathVariable("username") String username) {
		if (!this.userService.existsByUsername(username)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("User does not exist"), HttpStatus.NOT_FOUND);
		}
		User user = this.userService.getByUsername(username).get();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/detail-email/{email}")
	public ResponseEntity<?> getByEmail(@PathVariable("email") String email) {
		if (!this.userService.existsByEmail(email)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("User does not exist"), HttpStatus.NOT_FOUND);
		}
		User user = this.userService.getByEmail(email).get();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<?> createAdmin(@RequestBody UserDTO userDto) {
		if (this.userService.existsByUsername(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username already exists"), HttpStatus.BAD_REQUEST);
		}
		if (this.userService.existsByEmail(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email already exists"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getPassword())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This password is mandatory"), HttpStatus.BAD_REQUEST);
		}
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("firstname", userDto.getFirstname());
		parameters.put("username", userDto.getUsername());
		parameters.put("password", userDto.getPassword());
		parameters.put("email", userDto.getEmail());
		parameters.put("isNewUser", true);
		SendEmailsUtils.sendUserCredentials(parameters);
		
		String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());
		
		User user = new User(userDto.getUsername(), userDto.getFirstname(), userDto.getLastname(), userDto.getEmail(), hashedPassword);
		this.userService.save(user);
		return new ResponseEntity<MessageDTO>(new MessageDTO("User creado"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody UserDTO userDto) {
		if (!this.userService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("User does not exist"), HttpStatus.NOT_FOUND);
		}
		if (StringUtils.isBlank(userDto.getUsername())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This username is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (StringUtils.isBlank(userDto.getEmail())) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("This email is mandatory"), HttpStatus.BAD_REQUEST);
		}
		/*
		 * if (StringUtils.isBlank(userDto.getPassword())) { return new ResponseEntity<MessageDTO>(new
		 * MessageDTO("This password is mandatory"), HttpStatus.BAD_REQUEST); }
		 */
		
		User user = this.userService.getOne(id).get();
		user.setUsername(userDto.getUsername());

		if (StringUtils.isBlank(userDto.getPassword())) {
			userDto.setPassword(userDto.getPassword());

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("firstname", userDto.getFirstname());
			parameters.put("username", userDto.getUsername());
			parameters.put("password", userDto.getPassword());
			parameters.put("email", userDto.getEmail());
			parameters.put("isNewUser", false);
			SendEmailsUtils.sendUserCredentials(parameters);

			String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());
			user.setPassword(hashedPassword);
		}
		user.setFirstname(userDto.getFirstname());
		user.setLastname(userDto.getLastname());
		user.setEmail(userDto.getEmail());
		
		final Timestamp currentDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		user.setAuModificationDate(currentDate);
		Optional<User> optionalUser = null;
		User modificationUser = null;
		Integer auModificationUser = null;
		try {
			optionalUser = this.userService.getByUsername("system");
		} catch (Exception e) {
			log.error("ERROR - Getting the user --> ".concat("system").concat(" - ").concat(e.getMessage()));
		}
		if (optionalUser != null) {
			modificationUser = optionalUser.get();
		}
		if (modificationUser != null) {
			auModificationUser = modificationUser.getId();
		}
		if (auModificationUser != null) {
			user.setAuModificationUser(auModificationUser);
		}

		Set<Role> roles = new HashSet<Role>();
		for (RoleDTO roleDTO : userDto.getRoles()) {
			RoleName roleName = null;
			if (roleDTO.getRoleName().equals(RoleName.ROLE_ADMIN)) {
				roleName = RoleName.ROLE_ADMIN;
			}
			if (roleDTO.getRoleName().equals(RoleName.ROLE_CHEF)) {
				roleName = RoleName.ROLE_CHEF;
			}
			if (roleDTO.getRoleName().equals(RoleName.ROLE_USER)) {
				roleName = RoleName.ROLE_USER;
			}
			Role role = new Role(roleName);
			roles.add(role);
		}
		user.setRoles(roles);

		this.userService.save(user);
		return new ResponseEntity<MessageDTO>(new MessageDTO("User updated"), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		if (!this.userService.existsById(id)) {
			return new ResponseEntity<MessageDTO>(new MessageDTO("User does not exist"), HttpStatus.NOT_FOUND);
		}
		this.userService.delete(id);
		return new ResponseEntity<MessageDTO>(new MessageDTO("User deleted"), HttpStatus.OK);
	}
	
}
