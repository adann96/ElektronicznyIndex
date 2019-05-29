import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class Login extends JFrame {

	private JPanel contentPane;
	public JPasswordField passField;
	public JTextField textField;
	
	public static String id;
	public static String pass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
					Class.forName("oracle.jdbc.driver.OracleDriver");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 463, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnNewButton = new JButton("Sign In");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				try 
				{
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orclAD2019","univ_db","szczesny1");
					
					String sql = "select * from STUDENCI where NUMER_INDEKSU=" + textField.getText() 
					+ " and PASSWORD_ACC='" + passField.getText() + "'";
					PreparedStatement st = con.prepareStatement(sql);
					
					ResultSet res = st.executeQuery(sql);
					
					if(res.next()) {
						JOptionPane.showMessageDialog(null, "Username and password matched!");
						
						id = textField.getText();
						pass = passField.getText();
						
						Account field = new Account();
						field.setVisible(true);
						setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "Username or password wrong!");
						textField.setText("");
						passField.setText("");
					}
				}
				catch(Exception e1) {
					JOptionPane.showConfirmDialog(null, e1);
				}
				
			}
		});
		
		btnNewButton.setBounds(84, 183, 89, 23);
		contentPane.add(btnNewButton);
		
		passField = new JPasswordField();
		passField.setBounds(171, 128, 189, 20);
		contentPane.add(passField);
		
		textField = new JTextField();
		textField.setBounds(171, 81, 189, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblStudentId = new JLabel("Student ID");
		lblStudentId.setBounds(84, 84, 52, 14);
		contentPane.add(lblStudentId);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(84, 131, 46, 14);
		contentPane.add(lblPassword);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setText("");
				passField.setText("");
			}
		});
		
		btnNewButton_1.setBounds(271, 183, 89, 23);
		contentPane.add(btnNewButton_1);
	}
}
