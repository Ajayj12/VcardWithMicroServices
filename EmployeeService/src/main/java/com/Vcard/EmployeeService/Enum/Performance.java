package com.Vcard.EmployeeService.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum Performance {
	A(100),
	B(50);

	
	private final int perf;
	
	
}