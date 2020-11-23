package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

    private PreparedStatement employeePayrollDataStatement;

    private static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null) {
            employeePayrollDBService = new EmployeePayrollDBService();
        }
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "root";
        Connection connection;
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println(connection + " connection succesful");
        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String query = "SELECT * from employee_payroll where is_active=1;";
        return this.getEmployeePayrollDataUsingDB(query);
    }


    public int updateEmployeeData(String name, double salary) {
        return this.updateDataUsingStatement(name, salary);
    }

    private int updateDataUsingStatement(String name, double salary) {
        String query = String.format("update employee_payroll set salary = %.2f where name= '%s' and is_active=1;", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null) {
            this.prepareStatementForEmployeeData();
        }
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String query = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(query);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String query) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public Map<String, Double> getAverageSalaryByGender() {
        String query = "SELECT gender,AVG(salary) as avg_salary from employee_payroll group by gender;";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double avg_salary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender, avg_salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate,
                                                    String gender) {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO employee_payroll (name, gender,salary, start) VALUES" +
                    "('%s','%s','%s','%s')", name, gender, salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll_details (employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) VALUES" +
                    "(%s,%s,%s,%s,%s,%s)", employeeId, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return employeePayrollData;

    }

    public boolean addNewEmployeeDepartments(String name, String[] departments) {
        List<EmployeePayrollData> employeePayrollData = this.getEmployeePayrollData(name);
        int id = employeePayrollData.get(0).id;
        try {
            if (insertDepartment(departments, id)) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean insertDepartment(String[] departments, int id) throws SQLException {
        int executeUpdate;
        Connection connection = this.getConnection();
        Statement statement = connection.createStatement();
        for (String department : departments) {
            String sql = String.format("INSERT INTO employee_departments (employee_id, department) VALUES" +
                    "('%s','%s')", id, department);
            executeUpdate = statement.executeUpdate(sql);
            System.out.println(executeUpdate + " result");
            if (executeUpdate == 1) {
                return true;
            }
        }
        return false;
    }

    public EmployeePayrollData addNewEmployee(String name, double salary, LocalDate startDate, String gender, String[] departments) {
        EmployeePayrollData employeePayrollData = null;
        employeePayrollData = this.addEmployeeToPayroll(name, salary, startDate, gender);
        try {
            boolean result = insertDepartment(departments, employeePayrollData.id);
            if (result) {
                return employeePayrollData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EmployeePayrollData> deleteEmployee(String name, int id) {
        List<EmployeePayrollData> employeePayrollData = this.getEmployeePayrollData(name);
        int executeUpdate;
        Connection connection = null;
        try {
            connection = this.getConnection();
            Statement statement = connection.createStatement();
            String query = String.format("update employee_payroll set is_active=0 where name= '%s';", name);
            executeUpdate = statement.executeUpdate(query);
            if (executeUpdate >0) {
                System.out.println(employeePayrollData);
                return employeePayrollData;
            }
            System.out.println(executeUpdate + " result");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}













