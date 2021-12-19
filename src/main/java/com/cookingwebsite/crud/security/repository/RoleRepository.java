package com.cookingwebsite.crud.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(RoleName roleName);

	public boolean existsByRoleName(String roleName);

	public Optional<Role> findByAuActive(Boolean auActive);
}
