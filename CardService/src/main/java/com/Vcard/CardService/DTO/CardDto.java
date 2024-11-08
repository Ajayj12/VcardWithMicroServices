package com.Vcard.CardService.DTO;


import java.time.LocalDate;

import com.Vcard.CardService.Entity.CardCategory;
import com.Vcard.CardService.Enum.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
	
	private Integer cardNumber;
    private Integer rewardLimits;
    private Integer balance;
    private LocalDate dateOfCard;
    private Integer pin;
    private CardStatus status;  // Assuming you want to use a string for the enum value
    private Integer associateId;
    private String pinstring ;
	
	
}