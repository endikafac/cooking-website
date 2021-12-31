package com.cookingwebsite.crud.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.security.entity.VwUser;
import com.cookingwebsite.crud.security.repository.VwUserRepository;

@Service
@Transactional
public class VwUserService {

	@Autowired
	VwUserRepository vwUserRepository;

	public Optional<VwUser> getByUsername(String username) {
		return this.vwUserRepository.findByUsername(username);
	}

	public boolean existsByUsername(String username) {
		return this.vwUserRepository.existsByUsername(username);
	}

	public Optional<VwUser> getOne(Integer id) {
		return this.vwUserRepository.findById(id);
	}
	
	public List<VwUser> list() {
		return this.vwUserRepository.findAll();
	}
	
	public boolean existsById(final int userId) {
		return this.vwUserRepository.existsById(userId);
	}
	
}
