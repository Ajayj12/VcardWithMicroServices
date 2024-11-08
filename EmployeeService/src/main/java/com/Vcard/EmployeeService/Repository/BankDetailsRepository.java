package com.Vcard.EmployeeService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Vcard.EmployeeService.DTO.BankDetailsDto;
import com.Vcard.EmployeeService.Entity.BankDetails;



@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Integer>{

	
	BankDetails findByEmployee_AssociateId(int associateId);

}
