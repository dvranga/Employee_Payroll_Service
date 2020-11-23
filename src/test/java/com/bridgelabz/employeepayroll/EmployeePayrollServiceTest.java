package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;


public class EmployeePayrollServiceTest {

	@Test
	public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
		Assert.assertEquals(7, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa",6000000.00);
		boolean result = employeePayrollService.checkEmployeeInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}

	@Test
	public void givenDateRange_whenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData=
		employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
		Assert.assertEquals(6,employeePayrollData.size());
	}

	@Test
	public void givenPayRollData_WhenAverageSalaryRetrievdByGender_ShouldReturnProperValue() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
		System.out.println(averageSalaryByGender.get("M")+" "+averageSalaryByGender.get("F"));
		Assert.assertTrue(averageSalaryByGender.get("M").equals(3800000.00) &&
				averageSalaryByGender.get("F").equals(6000000.00));
	}

	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(DB_IO);
		employeePayrollService.addEmployeePayroll("Mark", 5000000.00, LocalDate.now(), "M");
		boolean result = employeePayrollService.checkEmployeeInSyncWithDB("Mark");
		Assert.assertTrue(result);
	}

	@Test
	public void givenDifferentDepartmentOfEmployee_ShouldInsertIntoTheNewModifiedTablesOfEmployee_payroll() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(DB_IO);
		String departments[]={"Devops","Testing","Fullstack"};
		employeePayrollService.addEmployeeDepartments("Terisa",departments);
	}
}
