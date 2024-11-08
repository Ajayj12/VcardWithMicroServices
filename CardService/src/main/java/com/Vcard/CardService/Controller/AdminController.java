package com.Vcard.CardService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Vcard.CardService.DTO.CardDto;
import com.Vcard.CardService.DTO.EmployeeDto;
import com.Vcard.CardService.DTO.RewardPointsDto;
import com.Vcard.CardService.DTO.TransactionDto;
import com.Vcard.CardService.Exception.CustomRunTimeException;
import com.Vcard.CardService.Repository.CardRepository;
import com.Vcard.CardService.ServiceImpl.CardService;

@RestController
@RequestMapping(path="/api/user" , headers="Accept=application/json")
public class AdminController {
	
	
	@Autowired
	private CardService cardService;
	@Autowired
	private CardRepository cardRepo;
	
	@PostMapping("newcard")
	public void createCard(@RequestBody EmployeeDto employee) {
		if (employee == null) {
	        throw new CustomRunTimeException("No Application for this employee");
	    }
		cardService.createCard(employee);
	
	}
	
	@PutMapping("pin")
	public void setPinNumber(@RequestBody CardDto cardDto) {
		if(cardDto == null)  {
	        throw new CustomRunTimeException("No Card for this employee");
	    }
		cardService.setPinNumber(cardDto);
	}
	
	
	@PostMapping("Buy")
	public TransactionDto order(@RequestBody TransactionDto transactDto) {
		if(transactDto == null) {
			throw new CustomRunTimeException("Wrong Card");
			
		}
		return cardService.order(transactDto);
	}
	
	@PostMapping("convert")
	public TransactionDto conversion(@RequestBody TransactionDto transactDto) {
		if(transactDto == null) {
			throw new CustomRunTimeException("Wrong Card");
			
		}
		return cardService.conversion(transactDto);
	}
	
	
	
	
	@PutMapping("approve")
	public CardDto approveCard(@RequestBody EmployeeDto employee) {
		
		if (employee == null) {
	        throw new CustomRunTimeException("No Application for this employee");
	    }
		CardDto card = cardService.approveCard(employee);
		return card;
	}
	
	@PutMapping("reject")
	public CardDto rejectCard(@RequestBody EmployeeDto employee) {
		if (employee == null) {
	        throw new CustomRunTimeException("No Application for this employee");
	    }
		CardDto card = cardService.rejectCard(employee);
		return card;
	}
	
	@Scheduled(cron = "0 0 0 1 * ?")
	@PostMapping("send")
	public RewardPointsDto monthlyRewards(){
		return cardService.monthlyRewards();
		
	}
	
	@PostMapping("mdata")
    public ResponseEntity<String> mdata(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            cardService.mdata(file);
            return ResponseEntity.ok("CSV file processed successfully.");
        } catch (CustomRunTimeException e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }
	
	
	
}
