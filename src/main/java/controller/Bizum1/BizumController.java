package controller.Bizum1;

import java.sql.*;
import java.util.ArrayList;

public class BizumController {

	/*Función que comprueba el dni y la contraseña introducida por el usuario y compureba que coinicide con uno de los usuarios*/
	public static boolean checkCredentials(String usr, String password, Connection connection) {
		boolean correctCredentials = false;
		String sql = "SELECT dni, password FROM Users WHERE dni = '"+ usr + "' AND password = '" + password + "'";
		try {
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getString("dni").equals(usr) && rs.getString("password").equals(password)) {
					correctCredentials = true;
				}
			}
			
			return correctCredentials;
		} catch (SQLException e) {
			return false;
		}
	}

	/*Función que añade un usuario a la base de datos*/
	public static boolean createUser(String dni, String name, String surname, String password, Connection connection) {
		boolean insertDone = false;
		String sql = "INSERT INTO Users (dni, name, surname, password) VALUES (?,?,?,?);";  
		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setString(1, dni);
			pstmt.setString(2, name);
			pstmt.setString(3, surname);
			pstmt.setString(4, password);
			
			pstmt.executeUpdate();
			insertDone = true;
			connection.commit();
			return insertDone;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean createAccount(String dni, Connection connection) {
		boolean insertDone = false;
		
		String sql = "INSERT INTO accounts (balance, user_id) VALUES (?,?);";  
		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setDouble(1, 100.00); //100� gifted when u create an account
			pstmt.setString(2, dni);
			
			pstmt.executeUpdate();
			insertDone = true;
			connection.commit();
			return insertDone;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		
	}
	
	public static String getName(Connection conexion, String dni) {
		String name = "Welcome, ";
		String sql = "Select dni, name from users where dni = '" + dni + "';";
		try {
			Statement stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				if (rs.getString("dni").equals(dni)) {
					name += rs.getString("name");
				}
			}
			return name;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean AccountExist(Connection conexion, int account_id) {
		boolean exists = false;
		String sql = "Select account_id from accounts where account_id = " + account_id +";";
		Statement stmt;
		try {
			stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getInt("account_id") == account_id) {
					exists = true;
				}
			}
			
			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static ArrayList<Integer> getAccount(Connection connection, String dni) {
		ArrayList<Integer>accountList = new ArrayList<Integer>();
		Integer accountNumber = 0;
		String sql = "Select account_id, user_id from Accounts where user_id = '" + dni + "';";
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				if (rs.getString("user_id").equals(dni)) {
					accountNumber = rs.getInt("account_id");
					accountList.add(accountNumber);
				}
			}
			return accountList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static boolean putMoney(int accountNumber, double value, Connection connection) {
		boolean updated = false;
		String sql = "" ;
			
		
		try {
			connection.setAutoCommit(false);
			int accountBalance = getBalance(connection, accountNumber);
			
			sql = "Update accounts Set balance = ? where " + accountNumber + " = account_id;";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setDouble(1, accountBalance + value);
			pstmt.executeUpdate();
			updated = true;
			connection.commit();
			return updated;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean withdrawMoney (int accountNumber, double value, Connection connection) {
		boolean updated = false;
		String sql = "" ;
		
		try {
			connection.setAutoCommit(false);
			int accountBalance = getBalance(connection, accountNumber);
			if(accountBalance>value) {
			sql = "Update accounts Set balance = ? where " + accountNumber + " = account_id;";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setDouble(1, accountBalance - value);
			pstmt.executeUpdate();
			updated = true;
			connection.commit();
			return updated;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}
	

	public static ArrayList<Double> getBalance(Connection connection, ArrayList<Integer> accounts_id) {
		ArrayList<Double>balanceList = new ArrayList<Double>();
		Double balance = 0.00;
		
		try {
			for (Integer account_id : accounts_id) {
				String sql = "Select account_id, balance from accounts where account_id =  ? ;";
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setInt(1, account_id);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					if (rs.getInt("account_id") == account_id) {
						balance = rs.getDouble("balance");
						balanceList.add(balance);
					}
				}
				rs.close();
				pstmt.close();
			}
			
			return balanceList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public static int getBalance(Connection conexion, int account_id) {
		int balance = 0;
		try {
			String sql = "Select account_id, balance from accounts where account_id =  ?;";
			PreparedStatement pstmt = conexion.prepareStatement(sql);
			
			pstmt.setInt(1, account_id);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				if (rs.getInt("account_id") == account_id) {
					balance = rs.getInt("balance");
				}
			}
			return balance;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean createTransfer(int accountNumber, Integer receiverAccountNumber, double value, Connection connection) {
		boolean insertHecho = false;

		String sql = "INSERT INTO Transfers (sender_account, receiver_account, value) VALUES (?,?,?);";
		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setInt(1, accountNumber);
			pstmt.setInt(2, receiverAccountNumber);
			pstmt.setDouble(3, value);

			pstmt.executeUpdate();
			insertHecho = true;
			connection.commit();
			return insertHecho;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

	}

	/*public static void main(String[] args) {
		Connection conexion = comprobarBaseDeDatos();
		crearUsuario("01944R", "Sergio", "Carvajal", "1234", conexion);
	}*/
}

