package com.Vcard.EmployeeService.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Vcard.EmployeeService.DTO.CardDto;
import com.Vcard.EmployeeService.DTO.EmployeeDto;
import com.Vcard.EmployeeService.DTO.TransactionDto;

import com.Vcard.EmployeeService.Exception.FeignConfig;

import feign.codec.ErrorDecoder;

@FeignClient(name = "CardService")
public interface CardServiceFeignClient {

	@PutMapping("pin")
	void setPinNumber(@RequestBody CardDto cardDto);
	
	@PostMapping("api/user/convert")
	TransactionDto conversion(@RequestBody TransactionDto transactDto);

	@PostMapping("api/user/Buy")
	TransactionDto order(@RequestBody TransactionDto transactDto);

	@PostMapping("newcard")
	CardDto createCard(@RequestBody EmployeeDto employee);

}

