package com.bridgelabz.employeepayroll;

import java.util.List;

public class EmployeePayrollService {

	public enum IOService{CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
	private List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioservice) {
		if (ioservice.equals(IOService.DB_IO)){
			this.employeePayrollList= new EmployeePayrollDBService().readData();
		}
		return employeePayrollList;
	}

}
