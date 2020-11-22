package com.bridgelabz.employeepayroll;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;


public class EmployeePayrollServiceTest {
	
	@Test
	public void givenEmployeePayrollInDB_whenRetrived_shouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	
}
