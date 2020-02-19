package com.hust.baseweb.test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void Test1() {
        BigDecimal a = new BigDecimal(123);
        a.add(new BigDecimal(456));
        System.out.println(a);
    }

    public static void main(String[] args) {


        Test.Test1();
        if (true) {
            return;
        }

        System.out.println("test..");
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list = list.stream().map(sg -> {
            return sg + "W";
        }).collect(Collectors.toList());
        list.forEach(sg -> System.out.println(sg));

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/baseweb", "postgres", "123456")) {

            System.out.println("Java JDBC PostgreSQL Example");
            // When this class first attempts to establish a connection, it automatically loads any JDBC 4.0 drivers found within
            // the class path. Note that your application must manually load any JDBC drivers prior to version 4.0.
//	          Class.forName("org.postgresql.Driver"); 

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.track_locations");
            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("party_id"), resultSet.getString("location"));
            }

        } /*catch (ClassNotFoundException e) {
	            System.out.println("PostgreSQL JDBC driver not found.");
	            e.printStackTrace();
	        }*/ catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

    }

}
