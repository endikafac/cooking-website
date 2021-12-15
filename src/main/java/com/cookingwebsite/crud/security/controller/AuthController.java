package com.cookingwebsite.crud.security.controller;

import com.cookingwebsite.crud.dto.MensajeDTO;
import com.cookingwebsite.crud.security.dto.JwtDTO;
import com.cookingwebsite.crud.security.dto.LoginUserDTO;
import com.cookingwebsite.crud.security.dto.NewUserDTO;
import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.jwt.JwtProvider;
import com.cookingwebsite.crud.security.service.RoleService;
import com.cookingwebsite.crud.security.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Slf4j
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/new")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUserDTO newUserDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("Wrong fields or invalid email"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUsername(newUserDTO.getUsername()))
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("This username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(newUserDTO.getEmail()))
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("This email already exists"), HttpStatus.BAD_REQUEST);
        User user =
                new User(newUserDTO.getUsername(),newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getEmail(),
                        passwordEncoder.encode(newUserDTO.getPassword()));
        Set<Role> roles = new HashSet<>();
        /*
         * If the role is in the array, we insert it, otherwise the User role is added.
         */
        if ((newUserDTO.getRoles() != null) && (!newUserDTO.getRoles().isEmpty())) {
	        if(newUserDTO.getRoles().contains(RoleName.ROLE_ADMIN.name())) {
	            roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
	        }
	        if(newUserDTO.getRoles().contains(RoleName.ROLE_CHEF.name())) {
	            roles.add(roleService.getByRoleName(RoleName.ROLE_CHEF).get());
	        }
	        if(newUserDTO.getRoles().contains(RoleName.ROLE_USER.name())) {
	            roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
	        }
        } else {
        	roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
        }
        user.setRoles(roles);
        userService.save(user);
               
        /*
         * It is saved first and once created, the generated user's own user identifier is saved as the creation user.
         */
        String username = user.getUsername();
        User createdUser = this.getUserByUsername(username);
    	if (createdUser!= null) {
    		user.setAuCreationUser(createdUser.getId());
    		userService.save(user);
    	}
        log.info("Saved user");
        return new ResponseEntity<MensajeDTO>(new MensajeDTO("Saved user"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("Wrong fields"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
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
    		optionalUser = userService.getByUsername(username);
    	} catch (Exception e) {
    		log.error("ERROR - Getting the user --> ".concat((username!=null)?username:"is null"));
    	}
    	
    	if ((optionalUser!= null) && (!optionalUser.isEmpty())) {
    		user = optionalUser.get();
    	}
    	
    	return user;
    }
}
