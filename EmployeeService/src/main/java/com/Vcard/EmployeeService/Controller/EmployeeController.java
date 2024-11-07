package com.Vcard.EmployeeService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Vcard.EmployeeService.DTO.BankDetailsDto;
import com.Vcard.EmployeeService.DTO.CardDto;
import com.Vcard.EmployeeService.DTO.EmployeeDto;
import com.Vcard.EmployeeService.DTO.TransactionDto;

import com.Vcard.EmployeeService.Exception.CustomRunTimeException;


import com.Vcard.EmployeeService.ServiceImpl.EmployeeService;

@RestController
@RequestMapping(path="/api/user", headers="Accept=application/json")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("bank/{associateId}")
	public BankDetailsDto findBankByEmployee_AssociateId(@PathVariable Integer associateId) {
		return employeeService.findBankByEmployee_AssociateId(associateId);
	}
	
	@GetMapping("{associateId}")
	public EmployeeDto findByAssociateId(@PathVariable Integer associateId) {
		return employeeService.findByAssociateId(associateId);
	}
	
	@PostMapping("newcard")
	public CardDto createCard(@RequestBody EmployeeDto employee) {
		
		if (employee == null) {
	        throw new CustomRunTimeException("Associate ID is required");
	    }
		return employeeService.createCard(employee);
	}
	
	@PutMapping("pin")
	public void setPinNumber(@RequestBody CardDto cardDto) {
		Integer card = cardDto.getCardNumber();
		if(card == null) {
			throw new CustomRunTimeException("Card Number cannot be null");
		}
		employeeService.setPinNumber(cardDto);
		
	}
	
	@PostMapping("vpa")
	public BankDetailsDto setVpa(@RequestBody BankDetailsDto bankDTO) {
		Integer emp = bankDTO.getAssociateId();
		if (emp == null) {
	        throw new CustomRunTimeException("Associate ID is required");
	    }
		BankDetailsDto bank = employeeService.setVpa(bankDTO);
		
		return bank;
		
	}
	
	@PostMapping("convert")
	public TransactionDto conversion(@RequestBody TransactionDto transactDto) {
		Integer card = transactDto.getCardNumber();
		if(card == null) {
			throw new CustomRunTimeException("Card Number cannot be null");
		}
		return employeeService.conversion(transactDto);
	}
	
	
	@PostMapping("Buy")
	public TransactionDto order(@RequestBody TransactionDto transactDto) {
		Integer card = transactDto.getCardNumber();
		if(card == null) {
			throw new CustomRunTimeException("Card Number cannot be null");
		}
		return employeeService.order(transactDto);
		
	}
	
	
}

