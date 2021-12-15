package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Role;
import com.cookingwebsite.crud.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	public List<Role> list() {
		return roleRepository.findAll();
	}

	public Optional<Role> getOne(final int id) {
		return roleRepository.findById(id);
	}

	public Optional<Role> getByName(final String name) {
		return roleRepository.findByName(name);
	}

	public void save(final Role role) {
		roleRepository.save(role);
	}

	public void delete(final int id) {
		roleRepository.deleteById(id);
	}

	public boolean existsById(final int id) {
		return roleRepository.existsById(id);
	}

	public boolean existsByNombre(final String name) {
		return roleRepository.existsByName(name);
	}
}
