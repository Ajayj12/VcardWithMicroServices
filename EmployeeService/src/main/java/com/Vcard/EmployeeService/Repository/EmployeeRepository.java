package com.Vcard.EmployeeService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.EmployeeService.DTO.EmployeeDto;
import com.Vcard.EmployeeService.Entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

	

	EmployeeEntity findByAssociateId(Integer associateId);

}

