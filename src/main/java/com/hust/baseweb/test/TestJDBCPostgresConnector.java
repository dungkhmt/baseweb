package com.hust.baseweb.test;

public class TestJDBCPostgresConnector {


    public static void main(String[] args) {

        TestJDBCPostgresConnector app = new TestJDBCPostgresConnector();
        app.test1();
    }

    public void test1() {
		/*
		try {

			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/baseweb", "postgres",
					"123456");

			System.out.println("Java JDBC PostgreSQL Example");
			// When this class first attempts to establish a connection, it
			// automatically loads any JDBC 4.0 drivers found within
			// the class path. Note that your application must manually load any
			// JDBC drivers prior to version 4.0.
			// Class.forName("org.postgresql.Driver");

			System.out.println("Connected to PostgreSQL database!");
			Statement statement = connection.createStatement();
			System.out.println("Reading car records...");
			System.out.printf("%-30.30s  %-30.30s%n", "Model", "Price");
			ResultSet rs = statement
					.executeQuery("SELECT * FROM public.party");
			while (rs.next()) {
				System.out.println(rs.getString("party_id") + "\t" + rs.getString("party_type_id"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		*/
    }

}
