package com.Vcard.CardService.DTO;

import java.time.LocalDate;

import com.Vcard.CardService.Enum.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
	
	 	private Integer associateId;
	    private Position position;
	    private String emailId;
	    private String mobile;
		private LocalDate dateOfHire;
		private Integer perf;
	
	

}
