package com.cookingwebsite.crud.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.security.entity.VwUser;

@Repository
public interface VwUserRepository extends JpaRepository<VwUser, Integer> {
	boolean existsByUsername(String username);

	@Override
	boolean existsById(Integer userId);
	
	@Query("SELECT u FROM VwUser u WHERE u.id = ?1")
	public Optional<VwUser> findByUserId(Integer userId);
	
	@Query("SELECT u FROM VwUser u WHERE u.username = ?1")
	public Optional<VwUser> findByUsername(String username);

}
