package com.sirma.solutions.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.sirma.solutions.model.Employee;
import com.sirma.solutions.model.ResponseData;
import com.sirma.solutions.utils.DateUtil;
import com.sirma.solutions.utils.Utility;

public class Application {

	private static final Logger LOGGER = Logger.getLogger("Application");

	private static Map<Integer, ArrayList<Employee>> map = new HashMap<>();
	private static List<ResponseData> response = new ArrayList<>();

	public static void main(String[] args) {
		try {
			// Get list of employees from text file.
			List<Employee> employees = getEmployees(Utility.getFileFromResourceAsStream());

			// Build map of project id(key) with employees(value) who did worked in this project
			buildDataMap(employees);

			// Sort the employees with top working days
			for (Entry<Integer, ArrayList<Employee>> entry : map.entrySet()) {
				sortData(entry.getValue());
			}

			// Create final output.
			for (Entry<Integer, ArrayList<Employee>> entry : map.entrySet()) {
				Integer key = entry.getKey();
				ArrayList<Employee> list = entry.getValue();
				if (list.size() > 2) {
					// Here I am getting first and second indexes as we are looking for pair of
					// employees
					// and the list is sorted DESC.
					response.add(ResponseData.builder().employeeId1(list.get(0).getEmployeeId())
							.employeeId2(list.get(1).getEmployeeId()).projectId(key)
							.daysWorkedTogether(list.get(0).getDaysWorked() + list.get(1).getDaysWorked()).build());
				} else {
					// Assuming that only one employee was working alone on the project
					response.add(ResponseData.builder().employeeId1(list.get(0).getEmployeeId()).projectId(key)
							.daysWorkedTogether(list.get(0).getDaysWorked()).build());
				}
			}

			// Print result.
			response.stream().forEach(responseData -> System.out.println(responseData.toString()));
			LOGGER.info("done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Build dictionary of project with related employees.
	 * @param employees
	 */
	private static void buildDataMap(List<Employee> employees) {
		employees.stream().forEach(employee -> {
			if (!map.containsKey(employee.getProjectId())) {
				employee.setDaysWorked(
						Utility.calculateProjectWorkingDays(employee.getDateTo(), employee.getDateFrom()));
				map.put(employee.getProjectId(), new ArrayList(Collections.singletonList(employee)));
			} else {
				employee.setDaysWorked(
						Utility.calculateProjectWorkingDays(employee.getDateTo(), employee.getDateFrom()));
				map.get(employee.getProjectId()).add(employee); // Update the list
			}
		});
	}

	/**
	 * Sort the data DESC according to most working days.
	 * @param employees
	 */
	private static void sortData(ArrayList<Employee> employees) {
		Collections.sort(employees, new Comparator<Employee>() {
			@Override
			public int compare(Employee employee1, Employee employee2) {
				return (employee2.getDaysWorked() - employee1.getDaysWorked());
			}
		});
	}

	/**
	 * Build list of employees
	 * 
	 * @param inputStream
	 * @return List<Employee>
	 * @throws IOException
	 */
	private static List<Employee> getEmployees(InputStream inputStream) throws IOException {
		List<Employee> employees = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		boolean isFitstLine = true;
		while (reader.ready()) {
			String line = reader.readLine();
			if (isFitstLine) { // Skip the first line (employeeId, projectId, ...)
				isFitstLine = false;
				continue;
			} else {
				if (!line.isEmpty()) {
					String[] strings = line.split(",");
					if (strings.length != 0) {
						employees.add(Employee.builder().employeeId(Utility.convertToInteger(strings[0].trim()))
								.projectId(Utility.convertToInteger(strings[1].trim()))
								.dateFrom(DateUtil.stringToDate(strings[2].trim()))
								.dateTo(DateUtil.stringToDate(strings[3].trim())).build());
					}
				}
			}
		}
		reader.close();
		return employees;
	}
}
