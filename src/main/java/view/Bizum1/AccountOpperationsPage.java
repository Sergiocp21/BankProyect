package view.Bizum1;

import java.awt.EventQueue;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Bizum1.ConnectionController;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AccountOpperationsPage extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connection connection = ConnectionController.checkDatabase();
					AccountOpperationsPage frame = new AccountOpperationsPage(connection, 0);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AccountOpperationsPage(final Connection connection, final int accountNumber) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("number account: 0");
		lblNewLabel.setBounds(10, 11, 133, 14);
		contentPane.add(lblNewLabel);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setBounds(335, 227, 89, 23);
		contentPane.add(cancelButton);
		
		JButton putMoneyButton = new JButton("Put money");
		putMoneyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PutMoneyPage ptllaIngreso = new PutMoneyPage(connection, accountNumber);
				ptllaIngreso.setVisible(true);
				
			}
		});
		putMoneyButton.setBounds(10, 110, 112, 23);
		contentPane.add(putMoneyButton);
		
		JButton withdrawButton = new JButton("Withdraw monay");
		withdrawButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WithdrawMoneyPage ptllaIngreso = new WithdrawMoneyPage(connection, accountNumber);
				ptllaIngreso.setVisible(true);
			}
		});
		withdrawButton.setBounds(159, 110, 105, 23);
		contentPane.add(withdrawButton);
		
		JButton transferButton = new JButton("Do a transfer");
		transferButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TransferPage transferPage = new TransferPage(connection, accountNumber);
				transferPage.setVisible(true);
			}
		});
		transferButton.setBounds(296, 110, 128, 23);
		contentPane.add(transferButton);
	}
}
