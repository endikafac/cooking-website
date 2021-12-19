package com.cookingwebsite.crud.security.service;

import com.cookingwebsite.crud.security.entity.User;
import com.cookingwebsite.crud.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public void save(User user){
    	userRepository.save(user);
    }
    
    public List<User> list(){
        return userRepository.findAll();
    }

    /*
    public Page<User> listUserActives(){
    	Pageable pageable = new Pageable
        return userRepository.findAll(pageable);
    }
    */
    
    public Optional<User> getOne(int id){
        return userRepository.findById(id);
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getByAuActive(Boolean auActive){
        return userRepository.findByAuActive(auActive);
    }

    public void delete(int id){
    	userRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return userRepository.existsById(id);
    }

    
}
