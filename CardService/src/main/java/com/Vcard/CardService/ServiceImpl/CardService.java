package com.Vcard.CardService.ServiceImpl;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Vcard.CardService.DTO.BankDetailsDto;
import com.Vcard.CardService.DTO.CardDto;
import com.Vcard.CardService.DTO.EmployeeDto;
import com.Vcard.CardService.DTO.PerfDataDto;
import com.Vcard.CardService.DTO.RewardPointsDto;
import com.Vcard.CardService.DTO.TransactionDto;
import com.Vcard.CardService.Entity.CardCategory;
import com.Vcard.CardService.Entity.CardEntity;
import com.Vcard.CardService.Enum.CardStatus;
import com.Vcard.CardService.Enum.Limitpoints;
import com.Vcard.CardService.Entity.PerfData;
import com.Vcard.CardService.Enum.Performance;
import com.Vcard.CardService.Enum.Position;
import com.Vcard.CardService.Enum.Status;
import com.Vcard.CardService.Enum.TransactionType;
import com.Vcard.CardService.Entity.RewardPoints;
import com.Vcard.CardService.Exception.CustomIllegalArguementException;
import com.Vcard.CardService.Exception.CustomRunTimeException;
import com.Vcard.CardService.FeignClient.EmployeeFeignClient;
import com.Vcard.CardService.Repository.CardCategoryRepository;
import com.Vcard.CardService.Repository.CardRepository;
import com.Vcard.CardService.Repository.PerformanceRepository;
import com.Vcard.CardService.Repository.RewardsRepository;
import com.Vcard.CardService.Repository.TransactionsRepository;
import com.Vcard.CardService.Entity.MTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import jakarta.transaction.Transactional;


@Service
public class CardService {

	
	@Autowired
	private CardRepository cardRepo;
	@Autowired
	private CardCategoryRepository cardCategoryRepo;

	@Autowired
	private PerformanceRepository perfRepo;
	
	@Autowired
	private EmployeeFeignClient employeeFeignClient;
	@Autowired
	private TransactionsRepository transactRepo;
	@Autowired
	private RewardsRepository rewardPointsRepo;
	@Autowired
	private ModelMapper modelMapper;
	
	

	@Transactional
	public void createCard(EmployeeDto employee) {

		EmployeeDto emp = employeeFeignClient.findByAssociateId(employee.getAssociateId());
		if (emp == null) {
			throw new CustomRunTimeException("Employee not found for associateId: " + employee.getAssociateId());
		}
		
		
	
		if (cardRepo.existsByassociateId(emp.getAssociateId())){
			throw new CustomRunTimeException("Card already applied for associateId: " + emp.getAssociateId());
		}

		CardEntity card = new CardEntity();
		card.setAssociateId(emp.getAssociateId());
		card.setCardNumber(emp.getAssociateId());

		if (emp.getPosition() == Position.G1) {
			card.setRewardLimits(Limitpoints.GOLD.getPts());
			card.setBalance(Limitpoints.GOLD.getPts());
			card.setCardType(cardCategoryRepo.getById(1));
		} else if (emp.getPosition() == Position.G3) {
			card.setRewardLimits(Limitpoints.DIAMOND.getPts());
			card.setBalance(Limitpoints.DIAMOND.getPts());
			card.setCardType(cardCategoryRepo.getById(2));
		} else if (emp.getPosition() == Position.G4) {
			card.setRewardLimits(Limitpoints.PLATINUM.getPts());
			card.setBalance(Limitpoints.PLATINUM.getPts());
			card.setCardType(cardCategoryRepo.getById(3));
		} else {
			throw new CustomIllegalArguementException("You're not Eligible");
		}

		card.setDateOfCard(LocalDate.now());
		card.setPin(1000);
		card.setStatus(CardStatus.PENDING);
		
		
	
		modelMapper.map(cardRepo.save(card), CardDto.class);

	}

	public CardDto approveCard(EmployeeDto employee) {
		EmployeeDto emp = employeeFeignClient.findByAssociateId(employee.getAssociateId());
		if (emp == null) {
			throw new CustomRunTimeException("Employee not found for associateId: " + employee.getAssociateId());
		}
	
		
		
		
		Optional<CardEntity> cardOpt = cardRepo.findByAssociateId(emp.getAssociateId());
		if (cardOpt.isEmpty()) {
			throw new CustomRunTimeException("Card not found for associateId: " + employee.getAssociateId());
		}
		CardEntity card = cardOpt.get();
		LocalDate currentDate = LocalDate.now();

		LocalDate doH = emp.getDateOfHire();

		if (card == null) {
			throw new CustomIllegalArguementException("Invalid card.");
		} else if (card.getStatus() == CardStatus.REJECTED) {
			throw new CustomIllegalArguementException("Card is Rejected.");
		} else if (card.getStatus() != CardStatus.PENDING) {
			throw new CustomIllegalArguementException("Card is already approved.");
		}

		else if (doH != null && Period.between(doH, currentDate).getYears() < 1) {
			card.setStatus(CardStatus.REJECTED);
			

			modelMapper.map(cardRepo.save(card), CardDto.class);
			throw new CustomIllegalArguementException(
					"Card cannot be approved since employment under company is less than 1 year.");

		}
		card.setAssociateId(emp.getAssociateId());
		card.setStatus(CardStatus.ACTIVE);

		
		return modelMapper.map(cardRepo.save(card), CardDto.class);

	}

	public CardDto rejectCard(EmployeeDto employee) {
		EmployeeDto emp = employeeFeignClient.findByAssociateId(employee.getAssociateId());
		if (emp == null) {
			throw new CustomRunTimeException("Employee not found for associateId: " + employee.getAssociateId());
		}
		Optional<CardEntity> cardOpt = cardRepo.findByAssociateId(emp.getAssociateId());
		if (cardOpt.isEmpty()) {
			throw new CustomRunTimeException("Card not found for associateId: " + employee.getAssociateId());
		}
		CardEntity card = cardOpt.get();
		LocalDate currentDate = LocalDate.now();
		LocalDate doH = emp.getDateOfHire();

		if (card.getStatus() == CardStatus.REJECTED) {
			throw new CustomIllegalArguementException("Card is already Rejected.");
		}

		else if (card.getStatus() == CardStatus.ACTIVE) {
			card.setStatus(CardStatus.PENDING);
			
			modelMapper.map(cardRepo.save(card), CardDto.class);
			throw new CustomIllegalArguementException("Card is Blocked Successfully.");

		}

		else if (card.getStatus() != CardStatus.PENDING && card.getStatus() != CardStatus.REJECTED) {
			throw new CustomIllegalArguementException("Card is already approved.");
		}
		card.setAssociateId(emp.getAssociateId());
		card.setStatus(CardStatus.REJECTED);

		return modelMapper.map(cardRepo.save(card), CardDto.class);

	}
	
	
	
	public CardDto setPinNumber(CardDto cardDto) {
		
		String str = cardDto.getPinstring();
		
		EmployeeDto emp = employeeFeignClient.findByAssociateId(cardDto.getAssociateId());
		if (emp == null) {
			throw new CustomRunTimeException("Employee not found for associateId: " + cardDto.getAssociateId());
		}
		Optional<CardEntity> cardopt = cardRepo.findByCardNumber(cardDto.getCardNumber());
		if(str.length()!= 4 || !str.matches("\\d{4}")) {
			throw new CustomIllegalArguementException("Pin Should be 4 digits");
		}
	
		else if(cardopt.isEmpty()) {
			throw new CustomIllegalArguementException("No card with the Card Number");
		}
		
		CardEntity card = cardopt.get();
		
		LocalDate currentDate = LocalDate.now();
		LocalDate dateOfApproval = card.getDateOfCard();
		
		if(Period.between(currentDate, dateOfApproval).getDays() > 30) {
			card.setStatus(CardStatus.REJECTED);
			
			
			
			modelMapper.map(cardRepo.save(card), CardDto.class);
			
			
			throw new CustomIllegalArguementException("Activation time Expired");
		}
		
		int pin = Integer.parseInt(str);
		 
		if(card.getPin() == pin) {
			throw new CustomIllegalArguementException("Pin cannot be same as previous one");
		}
		card.setPin(pin);
		card.setAssociateId(emp.getAssociateId());
		
		
		return modelMapper.map(cardRepo.save(card), CardDto.class);
		
		
		
	}
	
	
	
	

	@Transactional
	public RewardPointsDto monthlyRewards() {
		List<PerfData> allPerfData = perfRepo.findAll();
		RewardPoints rew = new RewardPoints();
		for(PerfData perfData : allPerfData) {
			int daysWorked = perfData.getDaysWorked();
			
			Integer emp = perfData.getAssociateId();
			
			if(emp == null) {
                new CustomIllegalArguementException("No employee data");

			}
						
			Optional<CardEntity> cardOpt = cardRepo.findByAssociateId(emp);
			if (cardOpt.isEmpty()) {
				throw new CustomRunTimeException("Card not found for associateId: " + emp);
			}
			CardEntity card = cardOpt.get();
			
			if(card == null) {
                new CustomIllegalArguementException("No card for employee");

			}
		
			
			double rewards = perfData.getPerformance().getPerf() *  card.getCardType().getPointMultiplier(); 
			
			card.setBalance(card.getBalance() + (int)rewards);
			modelMapper.map(cardRepo.save(card), CardDto.class);

			
			
			Date currentDate = new Date();
			
			
			rew.setMonthlyawards((int) rewards);
			rew.setAwardedDate(currentDate);
			rew.setCard(card);
			
			modelMapper.map(rewardPointsRepo.save(rew), RewardPointsDto.class);
		
	
		}
		return modelMapper.map(rewardPointsRepo.save(rew), RewardPointsDto.class);
		
	}
	
	
	
	
	
	
	public void mdata(MultipartFile file) throws Exception {
	    try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
	        reader.readNext(); // Skips the headers first line

	        String[] line;
	        while ((line = reader.readNext()) != null) {
	            // Print the line for debugging purposes
	            System.out.println("Processing line: " + Arrays.toString(line));

	            // Check for blank lines
	            if (line.length == 0 || Arrays.stream(line).allMatch(String::isEmpty)) {
	                System.out.println("Skipping blank line: " + Arrays.toString(line));
	                continue; // Skip blank lines
	            }

	            // Check if the line has enough columns
	            if (line.length < 3) {
	                System.out.println("Skipping line due to insufficient columns: " + Arrays.toString(line));
	                continue; // Skip this iteration if there aren't enough columns
	            }

	            // Validate and parse associateId
	            String associateIdStr = line[0].trim(); // Trim whitespace
	            if (associateIdStr.isEmpty()) {
	                System.out.println("Associate ID is empty. Skipping line: " + Arrays.toString(line));
	                continue; // Skip this iteration if associateId is empty
	            }
	            Integer associateId = Integer.parseInt(associateIdStr);

	            // Validate and parse daysWorked
	            String daysWorkedStr = line[1].trim(); // Trim whitespace
	            if (daysWorkedStr.isEmpty()) {
	                System.out.println("Days Worked is empty. Skipping line: " + Arrays.toString(line));
	                continue; // Skip this iteration if daysWorked is empty
	            }
	            Integer daysWorked = Integer.parseInt(daysWorkedStr);

	            // Get performance and ensure it's valid
	            String performanceStr = line[2].trim().toUpperCase(); // Trim whitespace
	            if (performanceStr.isEmpty()) {
	                System.out.println("Performance is empty. Skipping line: " + Arrays.toString(line));
	                continue; // Skip this iteration if performance is empty
	            }

	            try {
	                Performance performance = Performance.valueOf(performanceStr);
	                EmployeeDto emp = employeeFeignClient.findByAssociateId(associateId);
	        		if (emp == null) {
	        			throw new CustomRunTimeException("Employee not found for associateId: " + associateId);
	        		}
	                if (emp != null) {
	                    
	                    System.out.println("Found employee with ID: " + emp.getAssociateId());

	                    Integer perf = emp.getPerf();
	                    
	                    PerfData perfData = perfRepo.getPerformaceById(perf);
	                    if (perfData != null) {
	                        perfData.setDaysWorked(daysWorked);
	                        perfData.setPerformance(performance);
	                        perfRepo.save(perfData);
	                    } else {
	                        PerfData perfDa = new PerfData();
	                        perfDa.setAssociateId(associateId);
	                        perfDa.setDaysWorked(daysWorked);
	                        perfDa.setPerformance(performance);
	                        perfRepo.save(perfDa);
	                    }
	                } else {
	                    throw new CustomIllegalArguementException("Employee with associateId " + associateId + " not found.");
	                }
	            } catch (IllegalArgumentException e) {
	                System.out.println("Invalid performance value: " + performanceStr + ". Skipping line: " + Arrays.toString(line));
	                continue; // Skip this iteration if performance value is invalid
	            }
	        }
	    }
	}

	
	
	
	
	
	
	
	
	
	
	public TransactionDto conversion(TransactionDto transactDto) {
		Optional<CardEntity> cardopt = cardRepo.findByCardNumber(transactDto.getCardNumber());
		if(cardopt.isEmpty()) {
			throw new CustomIllegalArguementException("No Card with the Card Number");
		}
		Date Cd = new Date();
		
		CardEntity card = cardopt.get();
		MTransaction mtr = new MTransaction();
		
		Integer empOptional = card.getAssociateId();
	    if (empOptional  == null) {
	        throw new CustomRunTimeException("associateId cannot be null");
	    }
	    
	    
		BankDetailsDto bank = employeeFeignClient.findBankByEmployee_AssociateId(empOptional) ;

		CardCategory ctype = card.getCardType();
		LocalDate currentDate = LocalDate.now();
		int dayOfMonth = currentDate.getDayOfMonth();
		double finalWithdrawal = (transactDto.getAmountOfPoints() * ctype.getConversionRate());
		
		if(card.getStatus() != CardStatus.ACTIVE){
			throw new CustomIllegalArguementException("Not Eligible");
		}
		
		else if(card.getBalance() <= card.getRewardLimits()/2){
			throw new CustomIllegalArguementException("Balance is less than half of the Limit");
		}
		
		else if(dayOfMonth >= 1 && dayOfMonth <= 25){
			throw new CustomIllegalArguementException("Conversion is Only between 26th and 31st of Month ");
		}
		
		else if (transactDto.getAmountOfPoints() > card.getBalance()) {
            throw new CustomIllegalArguementException("Not enough points to convert.");
        }
		
		else if (card.getPin() == null || transactDto.getPin() == null || !card.getPin().equals(transactDto.getPin())) {
			mtr.setTransactionType(TransactionType.CONVERSION);
			mtr.setStatus(Status.FAILED);
			mtr.setCost(finalWithdrawal);
			mtr.setTransactionDate(Cd);
			mtr.setCard(card);
			
			
			modelMapper.map(transactRepo.save(mtr), TransactionDto.class);
			
			throw new CustomIllegalArguementException("Wrong PIN entered.");

		}
		
		else if(!bank.getVpaId().equals(transactDto.getVpa())) {
			mtr.setTransactionType(TransactionType.CONVERSION);
			mtr.setStatus(Status.FAILED);
			mtr.setCost(finalWithdrawal);
			mtr.setTransactionDate(Cd);
			mtr.setCard(card);
			
			modelMapper.map(transactRepo.save(mtr), TransactionDto.class);
			throw new CustomIllegalArguementException("Wrong UPI entered.");

		}
		
		
	
		
		
		int pts = card.getBalance() - transactDto.getAmountOfPoints();
		
		bank.setVpaId(transactDto.getVpa());
		mtr.setTransactionType(TransactionType.CONVERSION);
		mtr.setStatus(Status.SUCCESS);
		card.setBalance(pts);
		mtr.setCost(finalWithdrawal);
		
		mtr.setTransactionDate(Cd)	;	
		mtr.setCard(card);
		
		return modelMapper.map(transactRepo.save(mtr), TransactionDto.class);
		
		

	}
	
	
	
	
	
	public TransactionDto order(TransactionDto transactDto) {
		Optional<CardEntity> cardopt = cardRepo.findByCardNumber(transactDto.getCardNumber());
		if(cardopt.isEmpty()) {
			throw new CustomIllegalArguementException("Wrong Card Number");
		}
		Date Cd = new Date();
		CardEntity card = cardopt.get();
		
		Integer empId = card.getAssociateId();;
		
		MTransaction mtr = new MTransaction();
		
		CardCategory ctype = card.getCardType();
		
		double finalWithdrawal = (transactDto.getAmountOfPoints() * ctype.getConversionRate());
		
		EmployeeDto emp = employeeFeignClient.findByAssociateId(empId);
	    if (emp == null) {
	        throw new CustomRunTimeException("Employee not found");
	    }
	    
	    
	    if(card.getStatus() != CardStatus.ACTIVE){
			throw new CustomIllegalArguementException("Not Eligible");
		}
		
		else if(card.getBalance() <= transactDto.getAmountOfPoints()){
			throw new CustomIllegalArguementException("Insufficient Balance");
		}
		else if (card.getPin() == null || transactDto.getPin() == null || !card.getPin().equals(transactDto.getPin())){
			mtr.setTransactionType(transactDto.getTransactType());
			mtr.setStatus(Status.FAILED);
			mtr.setCost(finalWithdrawal);
			mtr.setTransactionDate(Cd);
			mtr.setCard(card);
			
			
			
			modelMapper.map(transactRepo.save(mtr), TransactionDto.class);
			
			throw new CustomIllegalArguementException("Wrong PIN entered.");

		}
	    
	    int pts = card.getBalance() - transactDto.getAmountOfPoints();
	    
	    mtr.setTransactionType(transactDto.getTransactType());
		mtr.setStatus(Status.SUCCESS);
		card.setBalance(pts);
		mtr.setCost(finalWithdrawal);
		mtr.setTransactionDate(Cd);
		mtr.setCard(card);
		
		
		return modelMapper.map(transactRepo.save(mtr), TransactionDto.class);
	    
		
	}
	
	
	
	
	
	
	
	

}