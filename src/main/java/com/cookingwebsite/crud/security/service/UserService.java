package com.cookingwebsite.crud.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Optional<User> getByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	public boolean existsByUsername(String username) {
		return this.userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return this.userRepository.existsByEmail(email);
	}

	public void save(User user) {
		this.userRepository.save(user);
	}

	public List<User> list() {
		return this.userRepository.findAll();
	}

	public List<User> search(String filter) {
		return this.userRepository.searchByLastnameOrFirstnameOrEmailOrUsername(filter);
	}

	/*
	 * public Page<User> listUserActives(){ Pageable pageable = new Pageable return userRepository.findAll(pageable); }
	 */

	public Optional<User> getOne(int id) {
		return this.userRepository.findById(id);
	}

	public Optional<User> getByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	public Optional<User> getByAuActive(Boolean auActive) {
		return this.userRepository.findByAuActive(auActive);
	}

	public void delete(int id) {
		this.userRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return this.userRepository.existsById(id);
	}

}
