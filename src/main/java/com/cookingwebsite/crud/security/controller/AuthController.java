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
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
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

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NewUserDTO newUserDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUsername(newUserDTO.getUsername()))
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(newUserDTO.getEmail()))
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("ese email ya existe"), HttpStatus.BAD_REQUEST);
        User user =
                new User(newUserDTO.getUsername(),newUserDTO.getFirstname(), newUserDTO.getLastname(), newUserDTO.getEmail(),
                        passwordEncoder.encode(newUserDTO.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getByRolNombre(RoleName.ROLE_USER).get());
        if(newUserDTO.getRoles().contains("admin"))
            roles.add(roleService.getByRolNombre(RoleName.ROLE_ADMIN).get());
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<MensajeDTO>(new MensajeDTO("usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<MensajeDTO>(new MensajeDTO("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDTO jwtDto = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<JwtDTO>(jwtDto, HttpStatus.OK);
    }
}
