package com.cookingwebsite.crud.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.security.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<User> findByAuActive(Boolean auActive);
	
	@Query("SELECT u FROM User u WHERE u.lastname LIKE %?1% OR u.firstname LIKE %?1% OR u.email LIKE %?1% OR u.username LIKE %?1%")
	public List<User> searchByLastnameOrFirstnameOrEmailOrUsername(String filter);

}
