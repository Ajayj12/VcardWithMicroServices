package com.Vcard.EmployeeService.ServiceImpl;


import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Vcard.EmployeeService.DTO.BankDetailsDto;
import com.Vcard.EmployeeService.DTO.CardCategoryDto;
import com.Vcard.EmployeeService.DTO.CardDto;
import com.Vcard.EmployeeService.DTO.EmployeeDto;
import com.Vcard.EmployeeService.DTO.TransactionDto;
import com.Vcard.EmployeeService.Entity.BankDetails;

import com.Vcard.EmployeeService.Enum.CardStatus;
import com.Vcard.EmployeeService.Entity.EmployeeEntity;
import com.Vcard.EmployeeService.Enum.Limitpoints;

import com.Vcard.EmployeeService.Enum.Status;
import com.Vcard.EmployeeService.Enum.TransactionType;
import com.Vcard.EmployeeService.Exception.CustomIllegalArguementException;
import com.Vcard.EmployeeService.Exception.CustomRunTimeException;
import com.Vcard.EmployeeService.Feign.CardServiceFeignClient;
import com.Vcard.EmployeeService.Repository.BankDetailsRepository;

import com.Vcard.EmployeeService.Repository.EmployeeRepository;

import jakarta.transaction.Transactional;


@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private BankDetailsRepository bankDetailsRepo;
	
	@Autowired
	private CardServiceFeignClient cardServiceClient;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public BankDetailsDto findBankByEmployee_AssociateId(Integer associateId) {
		BankDetails bank =  bankDetailsRepo.findByEmployee_AssociateId(associateId);
		if(bank == null) {
			throw new CustomRunTimeException("Bank Details Not found");
		}
		
		return modelMapper.map(bank, BankDetailsDto.class);
		
	}
	
	
	
	public EmployeeDto findByAssociateId(Integer associateId) {
		EmployeeEntity empEntity = employeeRepo.findByAssociateId(associateId);
	    if (empEntity == null) {
	        throw new CustomRunTimeException("Employee not found for associateId: " + associateId);
	    }
	    return modelMapper.map(empEntity, EmployeeDto.class);
	}
	
	@Transactional
	public CardDto createCard(EmployeeDto employee) {
		if (employee == null) {
	        throw new CustomRunTimeException("No Application for this employee");
	    }
	    
	    return cardServiceClient.createCard(employee); // Rethrow or handle accordingly
		    }
	

	
	
	public void setPinNumber(CardDto cardDto) {
		
		cardServiceClient.setPinNumber(cardDto);
		
	}
	
	
	
	public BankDetailsDto setVpa(BankDetailsDto bankdetails) {
		EmployeeEntity empOptional = employeeRepo.findByAssociateId(bankdetails.getAssociateId());
		
	    if (empOptional == null) {
	        throw new CustomRunTimeException("Employee not found for associateId: " + bankdetails.getAssociateId());
	    }
	    
	    BankDetails bank = new BankDetails();
	    
	   
	    bank.setVpaId(empOptional.getMobile());
	    bank.setEmployee(empOptional);
	    
	
	    return modelMapper.map(bankDetailsRepo.save(bank), BankDetailsDto.class);
	}
	
	
	

	@Transactional
	public TransactionDto conversion(TransactionDto transactDto) {
		return cardServiceClient.conversion(transactDto);
	}
	
	
	
	
	@Transactional
	public TransactionDto order(TransactionDto transactDto) {
		
		return cardServiceClient.order(transactDto);
	}
	
	

	
	
	

}
