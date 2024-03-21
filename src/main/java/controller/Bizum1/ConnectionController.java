package controller.Bizum1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionController {

	/*Function that checks if the database exist: if it exists, it will return the connection to it. iF not, it will create the database and return the connection to it*/
	public static Connection checkDatabase() {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bizumdb", "root","Sergio123");
			System.out.println("Connected to the database");
			
			//Databe already exists
			
			return conexion;
	
		} catch (SQLException e) {
			
			try {
				if(e.getMessage().contains("Unknown database")) { //Database not found, it will create a new one with the name bizumdb
				Class.forName("com.mysql.cj.jdbc.Driver");
				System.err.println("Database not found");
				System.out.println("Creating a new databes with name: bizumdb");
				
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=Sergio123"); //Connection to the server
				
				String sql = "CREATE DATABASE bizumdb"; //Creation sentence
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				System.out.println("Database created");
			
				
				Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bizumdb", "root","Sergio123");
				System.out.println("Connection accepted");
				
				 //Creation of the tables: Users, Accounts and transfers
				sql = "CREATE TABLE Users(dni VARCHAR(9) PRIMARY KEY, name VARCHAR(60), surname VARCHAR(60), password VARCHAR(30));"; 
				stmt = conexion.createStatement();
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE Accounts(account_id int AUTO_INCREMENT PRIMARY KEY, balance DECIMAL(12, 2));";
				stmt.executeUpdate(sql);
				
				sql = "ALTER TABLE Accounts ADD COLUMN user_id VARCHAR(9), ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES Users (dni);";
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE Transfers(transfer_id int AUTO_INCREMENT PRIMARY KEY, value DECIMAL(12,2));";
				stmt.executeUpdate(sql);
				
				sql = "ALTER TABLE Transfers ADD COLUMN sender_account int, ADD CONSTRAINT sender_account FOREIGN KEY (sender_account) REFERENCES Accounts (account_id);";
				stmt.executeUpdate(sql);
				
				sql = "ALTER TABLE Transfers ADD COLUMN receiver_account int, ADD CONSTRAINT receiver_account FOREIGN KEY (receiver_account) REFERENCES Accounts (account_id);";
				stmt.executeUpdate(sql);
				
				return conexion; //Creation and connection successfully done
				}
				else { //Unexpected error
					e.printStackTrace(); 
				}
	
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	}
		return null;
		
	}

}
