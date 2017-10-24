package database;

//STEP 1. Import required packages
import java.sql.*;
import java.util.ArrayList;

import controllers.MyBusinessController.Client;

//INSERT INTO LOGIN(user, password) VALUES('Hisao', '1234');

public class ClientQuery {
	// JDBC driver name and database URL
	public static String serverAddress = "138.197.29.147:3306";
	public static String databaseName = "A01720044_HomeOffice";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://" + serverAddress + "/" + databaseName;

	// Database credentials
	public static String USER = "luis";
	public static String PASS = "luisantos1";
	
	private static Connection conn = null;
	static ResultSet rs = null;
	public static ArrayList<Client> query(String sql) {
		ArrayList<Client> clientList = new ArrayList<Client>();
	
		Statement stmt = null;
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Retrieving Business Clients...");

			stmt = conn.createStatement();

			System.out.println();
			rs = stmt.executeQuery(sql);
		
			Client client;
			while (rs.next()) {
				
				String name = rs.getString("name");
				String lastName = rs.getString("lastName");
				String address = rs.getString("address");
				String phoneNumber = rs.getString("phoneNumber");
				Double balance = Double.parseDouble(rs.getString("balance"));
				int id = Integer.parseInt(rs.getString("codigo"));
				client = new Client(name, lastName, address, phoneNumber, balance, id);
				clientList.add(client);
				System.out.println("Retrieved!");
			System.out.println();

			// stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} }catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Done!");
		return clientList;
	}// end query
	
	public static ResultSet getRS() {
		return rs;
	}

	public static void Insert(String ins) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");

			stmt = conn.createStatement();

			stmt.executeUpdate(ins);

			// stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end query

	public static Connection getConnection() {
		return conn;
	}
	
	
	
	
	
}// end JDBCExample