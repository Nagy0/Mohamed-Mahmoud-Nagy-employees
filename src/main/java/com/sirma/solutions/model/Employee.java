package com.sirma.solutions.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

	private Integer employeeId;
	private Integer projectId;
	private Date dateFrom;
	private Date dateTo;
	private Integer daysWorked;

}
