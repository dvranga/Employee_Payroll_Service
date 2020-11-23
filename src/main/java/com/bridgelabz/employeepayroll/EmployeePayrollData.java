package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {

    public int id;
    public String name;
    public double salary;
    public LocalDate startDate;
    public String department;

    public EmployeePayrollData(int id, String name, double salary) {

        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
        this(id,name,salary);
        this.startDate = startDate;
    }

    public EmployeePayrollData(int employeeId, String name, double salary, LocalDate startDate, String department) {

        id = employeeId;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
        this.department = department;
    }


    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeePayrollData)) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return id == that.id &&
                Double.compare(that.salary, salary) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(startDate, that.startDate);
    }
}
