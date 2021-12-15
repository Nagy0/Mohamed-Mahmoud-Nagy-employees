package com.sirma.solutions.utils;

import java.io.InputStream;
import java.util.Date;

import com.sirma.solutions.app.Application;

public class Utility {

	private static final String FILE_NOT_FOUND = "file not found";
	private static final String FILE_NAME = "employees.txt";

	public static Integer convertToInteger(String input) {
		return Integer.parseInt(input);
	}

	public static int calculateProjectWorkingDays(Date startDate, Date endDate) {
		return Math.abs((int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)));
	}
	
	public static InputStream getFileFromResourceAsStream() {
		InputStream inputStream = Application.class.getClassLoader().getResourceAsStream(FILE_NAME);
		if (inputStream == null) {
			throw new IllegalArgumentException(FILE_NOT_FOUND + FILE_NAME);
		} else {
			return inputStream;
		}
	}
}
