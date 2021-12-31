package com.cookingwebsite.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cookingwebsite.crud.model.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
	List<Keyword> findByKeyword(String keyword);

	boolean existsByKeyword(String keyword);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM Keyword  WHERE id NOT IN (SELECT k.id FROM (SELECT * FROM Keyword ) as k INNER JOIN RecipeKeyword rk ON rk.keyword_id = k.id)")
	public void clean();

	@Query(nativeQuery = true, value = "SELECT k.* FROM Keyword k WHERE k.id IN (SELECT id  FROM vw_Keyword ) ORDER BY keyword ASC")
	public List<Keyword> listDistinct();
	
}
