package com.sirma.solutions.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData {

	private Integer employeeId1;
	private Integer employeeId2;
	private Integer projectId;
	private Integer daysWorkedTogether;
	
	@Override
	public String toString() {
		return "Response [employee1=" + employeeId1 + ", employee2=" + employeeId2 + ", projectId=" + projectId
				+ ", daysWorkedTogether=" + daysWorkedTogether + "]";
	}
	
	
}
