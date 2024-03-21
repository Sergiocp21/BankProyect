package view.Bizum1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Bizum1.BizumController;
import controller.Bizum1.ConnectionController;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateUserPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField dni;
	private JTextField name;
	private JTextField surname;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Connection connection = ConnectionController.checkDatabase();
			CreateUserPage dialog = new CreateUserPage(connection);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*Create the dialog.*/
	public CreateUserPage(final Connection connection) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			dni = new JTextField();
			dni.setToolTipText("Write your DNI here");
			dni.setBounds(198, 53, 120, 20);
			contentPanel.add(dni);
			dni.setColumns(10);
		}
		{
			name = new JTextField();
			name.setToolTipText("Write your name here.\r\nMaximum characters: 60");
			name.setBounds(198, 84, 120, 20);
			contentPanel.add(name);
			name.setColumns(10);
		}
		{
			surname = new JTextField();
			surname.setToolTipText("Write your surname here.\r\nMaximum characters: 60");
			surname.setBounds(198, 111, 120, 20);
			contentPanel.add(surname);
			surname.setColumns(10);
		}
		{
			password = new JPasswordField();
			password.setToolTipText("Write your password here.\r\nMaximum characters: 30");
			password.setBounds(198, 142, 120, 20);
			contentPanel.add(password);
		}
		{
			JLabel lblDni = new JLabel("DNI");
			lblDni.setBounds(95, 55, 77, 17);
			contentPanel.add(lblDni);
		}
		{
			JLabel lblNewLabel = new JLabel("Name");
			lblNewLabel.setBounds(95, 87, 46, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Surname");
			lblNewLabel_1.setBounds(95, 114, 77, 14);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Password");
			lblNewLabel_2.setBounds(95, 145, 77, 14);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton createUserButton = new JButton("OK");
				createUserButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						boolean allAnswered =dni.getText() != null && name.getText()!= null && surname.getText() != null && password.getPassword().length > 0; 
						boolean insertCompleted = false;
						if(allAnswered) {
							insertCompleted = BizumController.createUser(dni.getText(), name.getText(), surname.getText(), new String(password.getPassword()), connection);
							
							if(insertCompleted) {
								dispose();
							}
							else {
								JOptionPane.showMessageDialog(null, "This DNI may be already used", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "Check you filled all the fields", "Something went wrong :(", JOptionPane.ERROR_MESSAGE);

						}

					}
				});
				createUserButton.setActionCommand("OK");
				buttonPane.add(createUserButton);
				getRootPane().setDefaultButton(createUserButton);
			}
			{
				JButton botonCancelar = new JButton("Cancel");
				botonCancelar.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
					}
				});
				botonCancelar.setActionCommand("Cancel");
				buttonPane.add(botonCancelar);
			}
		}
	}
}
