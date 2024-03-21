package view.Bizum1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.Bizum1.BizumController;
import controller.Bizum1.ConnectionController;

public class WithdrawMoneyPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField valor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Connection connection = ConnectionController.checkDatabase();
			WithdrawMoneyPage dialog = new WithdrawMoneyPage(connection, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param accountNumber 
	 * @param connection 
	 */
	public WithdrawMoneyPage(final Connection connection, final int accountNumber) {
		setBounds(100, 100, 451, 195);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Write the amount you want to withdraw\r\n");
			lblNewLabel.setBounds(99, 11, 295, 14);
			contentPanel.add(lblNewLabel);
		}
		
		valor = new JTextField();
		valor.setColumns(10);
		valor.setBounds(99, 50, 215, 20);
		contentPanel.add(valor);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Confirm");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							double moneyToWithdraw = Double.parseDouble(valor.getText());
							boolean insertCompleted = BizumController.withdrawMoney(accountNumber, moneyToWithdraw, connection);
							
							if(insertCompleted) {
								JOptionPane.showMessageDialog(null, "Money withdraw successfully", "All good", JOptionPane.PLAIN_MESSAGE);
								dispose();
							}
							
						else {
							JOptionPane.showMessageDialog(null, "Make sure you have more money than you want to withdraw", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);

						}
					}catch(NumberFormatException error) {
						JOptionPane.showMessageDialog(null, "Make sure you write a number", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);
					}
				}});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
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
