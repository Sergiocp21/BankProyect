package view.Bizum1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Bizum1.BizumController;
import controller.Bizum1.ConnectionController;

import java.awt.Color;
import java.sql.*;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;


public class LoginPage {

	private JFrame frame;
	private JTextField userDNI;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPage() {
		
	
		Connection connection = ConnectionController.checkDatabase();
		initialize(connection);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	void initialize(final Connection connection) {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 541, 371);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().setResizable(false);
		getFrame().getContentPane().setLayout(null);
		
		userDNI = new JTextField();
		userDNI.setToolTipText("Write your DNI here");
		userDNI.setBounds(231, 102, 108, 20);
		getFrame().getContentPane().add(userDNI);
		userDNI.setBackground(null);
		
		password = new JPasswordField();
		password.setToolTipText("Write your password here");
		password.setBackground((Color) null);
		password.setBounds(231, 156, 108, 20);
		getFrame().getContentPane().add(password);
		
		
		/*When you click on this button, another window appears to create a new user*/
		
		JButton createButton = new JButton("Create user");
		createButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				CreateUserPage ventanaCrearCuenta = new CreateUserPage(connection);
				ventanaCrearCuenta.setVisible(true);

			}
		});
		createButton.setBounds(104, 199, 114, 23);
		getFrame().getContentPane().add(createButton);
		
		
		/*When you click on this button, checks if the fields match with the fields from the database, if it is correct, a new window opens with logged in */
		
		JButton botonIniciar = new JButton("Log in");
		botonIniciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean correcto = false;
				correcto = BizumController.checkCredentials(userDNI.getText(), new String(password.getPassword()), connection);
				if(correcto) {
					UserPage ventanaUsuario = new UserPage(connection, userDNI.getText());
					ventanaUsuario.setVisible(true);
					getFrame().dispose();
				}
				else {
					JOptionPane.showMessageDialog(null, "User or password are wrong", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		botonIniciar.setBounds(317, 199, 121, 23);
		getFrame().getContentPane().add(botonIniciar);
		
		JLabel lblNewLabel = new JLabel("User (DNI)");
		lblNewLabel.setBounds(138, 105, 80, 14);
		getFrame().getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(138, 159, 83, 14);
		getFrame().getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Welcome, please log in");
		lblNewLabel_2.setBounds(43, 37, 229, 14);
		getFrame().getContentPane().add(lblNewLabel_2);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
