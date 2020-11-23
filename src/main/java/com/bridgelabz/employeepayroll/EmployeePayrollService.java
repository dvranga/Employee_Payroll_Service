package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollService {

	public enum IOService{CONSOLE_IO, FILE_IO, DB_IO, REST_IO}
	private List<EmployeePayrollData> employeePayrollList;
	private static EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		 employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this();
		this.employeePayrollList = employeePayrollList;
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioservice) {
		if (ioservice.equals(IOService.DB_IO)){
			this.employeePayrollList= employeePayrollDBService.readData();
		}
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result==0)return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData!=null) employeePayrollData.salary=salary;
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollData -> employeePayrollData.name.equals(name))
				.findFirst()
				.orElse(null);
	}

	public boolean checkEmployeeInSyncWithDB(String name) {
		List<EmployeePayrollData> employeePayrollData = employeePayrollDBService.getEmployeePayrollData(name);
		return employeePayrollData.get(0).equals(getEmployeePayrollData(name));
	}

	public List<EmployeePayrollData> getEmployeePayrollDataByGivenDataRange(LocalDate startDate, LocalDate endDate) {
		return employeePayrollDBService.getEmployeePayrollDataByDataRange(startDate,endDate);
	}

	public double performVariousOperations(String average, String m) {
		return employeePayrollDBService.performVariousOperationsOf(average,m);
	}

}
