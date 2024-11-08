package com.Vcard.CardService.DTO;

import java.util.Date;

import com.Vcard.CardService.Enum.Status;
import com.Vcard.CardService.Enum.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
	
	private Integer amountOfPoints;
	private Integer pin;
	private String vpa;
	
	private TransactionType transactType;
	private Status status;
	private Double cost;
	private Date transactionDate;
	private Integer cardNumber;
	private Integer purchaseId;
	
}
