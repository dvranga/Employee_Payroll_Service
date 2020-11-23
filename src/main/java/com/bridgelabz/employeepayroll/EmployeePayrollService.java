package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

	public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService,
																	 LocalDate startDate, LocalDate endDate) {
		if (ioService.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getEmployeePayrollForDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getAverageSalaryByGender();
		}
		return null;
	}

	public void addEmployeePayroll(String name, double salary, LocalDate startDate, String gender) {
		employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary, startDate, gender));
	}

	public boolean addEmployeeDepartments(String name,  String[] departments) {
		boolean result = employeePayrollDBService.addNewEmployeeDepartments(name, departments);
		return result;
	}

	public boolean addNewEmployee(String name, double salary, LocalDate startDate, String gender, String[] departments) {
		return employeePayrollList.add(employeePayrollDBService.addNewEmployee(name, salary, startDate, gender, departments));
	}

	public boolean deleteEmployee(String name, int id) {
		List<EmployeePayrollData> employeePayrollData = employeePayrollDBService.deleteEmployee(name, id);
		System.out.println(employeePayrollData);
		boolean remove = employeePayrollList.remove(employeePayrollData);
		return true;

	}

}
