package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;


public class EmployeePayrollServiceTest {

	@Test
	public void givenEmployeePayrollInDB_whenRetrieved_shouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
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
	public void givenEmployeePayrollDB_AbilityToRetrievAllTheEmployees_JoinedInParticularDataRanga() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
		List<EmployeePayrollData> employeePayrollDataByGivenDataRange = employeePayrollService.getEmployeePayrollDataByGivenDataRange(LocalDate.of(2018, 01, 01), LocalDate.now());
		Assert.assertEquals(3,employeePayrollDataByGivenDataRange.size());
	}
}
