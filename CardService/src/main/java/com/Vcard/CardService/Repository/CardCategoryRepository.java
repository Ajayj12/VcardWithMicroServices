package com.Vcard.CardService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.CardService.Entity.CardCategory;


@Repository
public interface CardCategoryRepository extends JpaRepository<CardCategory, Integer> {

	CardCategory getById(Integer id);

	

}
