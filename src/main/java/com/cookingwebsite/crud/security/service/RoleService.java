package com.cookingwebsite.crud.security.service;

import com.cookingwebsite.crud.security.entity.Role;
import com.cookingwebsite.crud.security.enums.RoleName;
import com.cookingwebsite.crud.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> getByRoleName(RoleName roleName){
        return roleRepository.findByRoleName(roleName);
    }

    public void save(Role rol){
        roleRepository.save(rol);
    }
}
