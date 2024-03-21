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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JTextField;

public class PutMoneyPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField value;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Connection connection = ConnectionController.checkDatabase();
			PutMoneyPage dialog = new PutMoneyPage(connection, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PutMoneyPage(final Connection connection, final int nCuenta) {
		setBounds(100, 100, 451, 195);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Please, write the amount you want to put in");
			lblNewLabel.setBounds(99, 11, 295, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			value = new JTextField();
			value.setBounds(99, 50, 215, 20);
			contentPanel.add(value);
			value.setColumns(10);
		}
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
							double moneyToPut = Double.parseDouble(value.getText());
							boolean insertCompleted = BizumController.putMoney(nCuenta, moneyToPut, connection);
							
							if(insertCompleted) {
								JOptionPane.showMessageDialog(null, "Money was added successfully", "All good", JOptionPane.PLAIN_MESSAGE);
								dispose();
							}
							
						else {
							JOptionPane.showMessageDialog(null, "Unexpected error", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);

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
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
