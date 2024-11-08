package com.Vcard.CardService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Vcard.CardService.DTO.BankDetailsDto;
import com.Vcard.CardService.DTO.EmployeeDto;
import com.Vcard.CardService.Exception.FeignConfig;

@FeignClient(name = "EmployeeService")
public interface EmployeeFeignClient {

	
	@GetMapping("api/user/bank/{associateId}")
	BankDetailsDto findBankByEmployee_AssociateId(@PathVariable Integer associateId);

	@GetMapping("api/user/{associateId}")
	EmployeeDto findByAssociateId(@PathVariable Integer associateId);

}
