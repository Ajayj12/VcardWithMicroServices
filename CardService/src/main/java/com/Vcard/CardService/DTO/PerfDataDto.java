package com.Vcard.CardService.DTO;



import com.Vcard.CardService.Enum.Performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PerfDataDto {
	private Performance performance;
    private Integer daysWorked;
    private Integer associateId;
}
