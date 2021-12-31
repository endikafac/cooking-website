package com.cookingwebsite.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Keyword;
import com.cookingwebsite.crud.repository.KeywordRepository;

@Service
@Transactional
public class KeywordService {
	
	@Autowired
	KeywordRepository keywordRepository;
	
	public List<Keyword> list() {
		return this.keywordRepository.findAll();
	}
	
	public Optional<Keyword> getOne(final int id) {
		return this.keywordRepository.findById(id);
	}
	
	public List<Keyword> getByKeyword(final String keyword) {
		return this.keywordRepository.findByKeyword(keyword);
	}
	
	public void save(final Keyword keyword) {
		this.keywordRepository.save(keyword);
	}
	
	public void delete(final int id) {
		this.keywordRepository.deleteById(id);
	}
	
	public boolean existsById(final int id) {
		return this.keywordRepository.existsById(id);
	}
	
	public boolean existsByKeyword(final String Keyword) {
		return this.keywordRepository.existsByKeyword(Keyword);
	}
	
	public void deleteInBatch(final List<Keyword> keywords) {
		this.keywordRepository.deleteInBatch(keywords);
	}

	public void clean() {
		this.keywordRepository.clean();
	}
	
	public List<Keyword> listDistinct() {
		return this.keywordRepository.listDistinct();
	}
}
