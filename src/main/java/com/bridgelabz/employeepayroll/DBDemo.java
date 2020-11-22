package com.bridgelabz.employeepayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DBDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName="root";
        String password="root";
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the class path",e);
        }
        listDriver();

        try {
            connection = DriverManager.getConnection(jdbcURL,userName,password);
            System.out.println("Connection is succesful");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new IllegalStateException("Connection is nor established",e);
        }
    }




    private static void listDriver() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = (Driver) drivers.nextElement();
            System.out.println(driver.getClass().getName());
        }


    }
}
