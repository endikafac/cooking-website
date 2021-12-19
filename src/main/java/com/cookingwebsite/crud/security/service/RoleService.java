package com.cookingwebsite.crud.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.repository.RoleRepository;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	public Optional<Role> getByRoleName(RoleName roleName) {
		return this.roleRepository.findByRoleName(roleName);
	}
	
	public void save(Role rol) {
		this.roleRepository.save(rol);
	}

	public boolean existsByRoleName(String roleName) {
		return this.roleRepository.existsByRoleName(roleName);
	}
	
	public List<Role> list() {
		return this.roleRepository.findAll();
	}

	public Optional<Role> getOne(int id) {
		return this.roleRepository.findById(id);
	}

	public Optional<Role> getByAuActive(Boolean auActive) {
		return this.roleRepository.findByAuActive(auActive);
	}

	public void delete(int id) {
		this.roleRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return this.roleRepository.existsById(id);
	}
}
