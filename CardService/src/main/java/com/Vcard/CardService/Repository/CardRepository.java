package com.Vcard.CardService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.CardService.DTO.EmployeeDto;
import com.Vcard.CardService.Entity.CardEntity;




@Repository
public interface CardRepository extends JpaRepository<CardEntity, Integer>{

	Optional<CardEntity> findByAssociateId(Integer associateId);

	Optional<CardEntity> findByCardNumber(Integer cardNumber);

	


	boolean existsByassociateId(Integer integer);

	
	

}
