package view.Bizum1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Bizum1.BizumController;
import controller.Bizum1.ConnectionController;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

public class TransferPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField senderAccountNumber;
	private JTextField value;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Connection connection = ConnectionController.checkDatabase();
			TransferPage dialog = new TransferPage(connection, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TransferPage(final Connection connection, final int acountNumber) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Write the account number where you want to send the money");
			lblNewLabel.setBounds(46, 50, 341, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			senderAccountNumber = new JTextField();
			senderAccountNumber.setBounds(86, 75, 217, 20);
			contentPanel.add(senderAccountNumber);
			senderAccountNumber.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Write the amount to send");
			lblNewLabel_1.setBounds(88, 106, 341, 14);
			contentPanel.add(lblNewLabel_1);
		}
		{
			value = new JTextField();
			value.setBounds(86, 131, 217, 20);
			contentPanel.add(value);
			value.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton confirmButton = new JButton("Confirmar");
				confirmButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							Integer receiverAccountNumber = Integer.parseInt(senderAccountNumber.getText());
							double amountToSend = Double.parseDouble(value.getText());
							boolean accountExists = BizumController.AccountExist(connection, receiverAccountNumber);
							if(accountExists) {
								
							boolean withdrawCompleted = BizumController.withdrawMoney(acountNumber, amountToSend, connection);
							boolean putMoneyCompleted = BizumController.putMoney(receiverAccountNumber, amountToSend, connection);
							
							if(withdrawCompleted && putMoneyCompleted) {
								BizumController.createTransfer(acountNumber, receiverAccountNumber, amountToSend, connection);
								JOptionPane.showMessageDialog(null, "The money was sent successfully", "All good", JOptionPane.PLAIN_MESSAGE);
								dispose();
							}
							
						else {
							JOptionPane.showMessageDialog(null, "Make sure you have at least the money you want to trasnfer", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);

						}
							}
							else {
								JOptionPane.showMessageDialog(null, "The account with that number does not exist", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);
							}
					}catch(NumberFormatException error) {
						JOptionPane.showMessageDialog(null, "Make sure you wrote a number", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);
					}
					}
				});
				confirmButton.setActionCommand("OK");
				buttonPane.add(confirmButton);
				getRootPane().setDefaultButton(confirmButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
