package view.Bizum1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Bizum1.BizumController;
import controller.Bizum1.ConnectionController;

import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;

public class UserPage extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Connection connection = ConnectionController.checkDatabase();
					UserPage frame = new UserPage(connection, "");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Create the frame.
	 */
	public UserPage(final Connection conexion, final String dniCliente) {
		setTitle("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(100, 100, 965, 615);
		contentPane = new JPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("hola");
			}
		});
		contentPane.setBackground(new Color(95, 158, 160));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setSize(screenSize);
		
		setContentPane(contentPane);
		String userName = BizumController.getName(conexion, dniCliente);
			contentPane.setLayout(null);
			
			JLabel lblNewLabel = new JLabel("These are your accounts, click the one you want to opearate with");
			lblNewLabel.setBounds(45, 86, 410, 14);
			contentPane.add(lblNewLabel);
			JLabel welcome = new JLabel(userName);
			welcome.setFont(new Font("Tahoma", Font.PLAIN, 13));
			welcome.setBounds(45, 37, 197, 22);
			contentPane.add(welcome);
			
			JLabel lblNewLabel_1 = new JLabel("Create an account now and get 100€!");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblNewLabel_1.setForeground(new Color(255, 215, 0));
			lblNewLabel_1.setBounds(490, 150, 449, 34);
			contentPane.add(lblNewLabel_1);
			
			JLabel lblNewLabel_2 = new JLabel("Create one");
			lblNewLabel_2.setBounds(566, 182, 66, 14);
			contentPane.add(lblNewLabel_2);
			
			JLabel lblNewLabel_3 = new JLabel("here");
			lblNewLabel_3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					CreateAccountPage createAccountWindow = new CreateAccountPage(conexion, dniCliente);
					createAccountWindow.setVisible(true);
				}
			});
			lblNewLabel_3.setForeground(new Color(0, 0, 128));
			lblNewLabel_3.setBounds(628, 182, 46, 14);
			contentPane.add(lblNewLabel_3);
			
			JLabel lblNewLabel_4 = new JLabel("Log out");
			lblNewLabel_4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					LoginPage loginPage = new LoginPage();
					loginPage.getFrame().setVisible(true);  //Come back to LogIn page
					dispose();
				}
			});
			lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblNewLabel_4.setForeground(new Color(0, 0, 128));
			lblNewLabel_4.setBounds(780, 524, 144, 14);
			contentPane.add(lblNewLabel_4);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(45, 126, 303, 312);
			contentPane.add(tabbedPane);
			
			JPanel accounts = new JPanel();
			tabbedPane.addTab("Accounts", null, accounts, null);
			accounts.setLayout(null);
			
			JPanel transfers = new JPanel();
			tabbedPane.addTab("Transfers", null, transfers, null);
			transfers.setLayout(null);
			
			
			
			accounts.setLayout(new BoxLayout(accounts, BoxLayout.Y_AXIS));
			
			JLabel lblNewLabel_5 = new JLabel("Dont forget to log in again to see your changes!");
			lblNewLabel_5.setBounds(501, 237, 410, 59);
			contentPane.add(lblNewLabel_5);
			
			//Shows on screen all user's account
		    final ArrayList<Integer> accountList = BizumController.getAccount(conexion, dniCliente);
			ArrayList<Double> balanceList = BizumController.getBalance(conexion, accountList);
			if(accountList.size()>0) {
			for(int i = 0; i<accountList.size(); i++) {
				
				final Integer nCuenta = accountList.get(i);
			  
			  JLabel cuenta = new JLabel("Account number: " + accountList.get(i).toString()+ "       Available balance: " + balanceList.get(i).toString() );
			  cuenta.setForeground(new Color(55, 0, 255));
			  
				cuenta.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						AccountOpperationsPage createAccountWindow = new AccountOpperationsPage(conexion, nCuenta);
						createAccountWindow.setVisible(true);
					}
				});
			  
			  accounts.add(cuenta);
			}
			
			}
			else {
				JOptionPane.showMessageDialog(null, "You dont have an account, create one now and get 100€ for free!", "WARNING", JOptionPane.WARNING_MESSAGE);

			}
			
			
			//Shows on screen all user's transfers
			
			try {
				for (Integer accountNumber : accountList) {
					String sql = "Select sender_account, receiver_account, value FROM Transfers where sender_account = '" +accountNumber + "';";
					Statement stmt = conexion.createStatement();;
					String tranfer = "";
					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next()) {
						tranfer = "";
						tranfer = "Send to: " + rs.getString("receiver_account") + "    Money send: " + rs.getDouble("value");
						JLabel t = new JLabel(tranfer);
						transfers.add(t);
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			  
			  
	
			
			
			
	}
	

}
