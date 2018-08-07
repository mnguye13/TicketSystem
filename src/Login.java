/**
 *@author Minh Nguyen
 *ITMD 411-Final
 *4/24/2018
 *Login.java
 *This class will allow user authentication (identify and verify) for database access. 
 */



import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.PreparedStatement;

public class Login {

	// create instance fields for class
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JLabel namelabel;
	private JLabel passwordLabel;
	private JTextField userText;
	private JPasswordField passwordText;
	private JButton loginButton;
	private JPanel controlPanel;
	
	static private String role= null; 

	public Login() {
		prepareGUI();
		showTextFields();
	}

	private void prepareGUI() {

		// instantiate objects

		mainFrame = new JFrame("Ticket System"); // title of window form
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		controlPanel = new JPanel();

		// window frame settings
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.getContentPane().setBackground(Color.GRAY);
		mainFrame.setLocationRelativeTo(null);

		// frame object settings
		headerLabel.setText("Account Access");
		statusLabel.setSize(350, 100);

		// add frame objects to mainframe
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent wE) { // define a window close
														// operation
				System.exit(0);
			}
		});
	}

	private void showTextFields() {

		// instantiate controls
		namelabel = new JLabel("User ID: ", JLabel.RIGHT);
		passwordLabel = new JLabel("Password: ", JLabel.CENTER);
		userText = new JTextField(6);
		passwordText = new JPasswordField(6);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				/*
				 * Check credentials for various users
				 * 
				 * Administrator is a super user to the ticket system 
				 * Code below uses a default (temporary) hard coded admin user
				 * name/password for verification. You can change this setting
				 * if you like.
				 */
				// Create user friendly variable name to store user name from text box
				String userName = userText.getText();
				// convert characters from password field to string for input validation
				String password = new String(passwordText.getPassword());
				boolean adminFlag = false;
				
				
				
				
				
				
				if (userName.equals("minhnguyen") && password.equals("root")) {
					adminFlag = true;
					role= "admin";
					// close of Login window
					mainFrame.dispose();
					// open up ticketsGUI file upon successful login
					try {
						new ticketsGUI("Admin", role);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // establish role as admin via constructor call
				}
				/*
				 * match credentials from text fields with users table for a
				 * match for regular users
				 */
				else if (!adminFlag) {
				
					Connection connect = Dao.getConnection();
					String queryString = "SELECT uname, upass, urole FROM mnguye13_users3 where uname=? and upass=?";
					PreparedStatement ps;
					ResultSet results = null;
					try {
						// set up prepared statements to execute query string cleanly and safely
						ps = (PreparedStatement) connect.prepareStatement(queryString);
						ps.setString(1, userName);
						ps.setString(2, password);
						results = ps.executeQuery();
						
						if (results.next()) { // verify if a record match exists
												// in table
							JOptionPane.showMessageDialog(null, "Username and Password exist");
							// close of Login window
							mainFrame.dispose();
							// open up ticketsGUI file upon successful login
							role= results.getString(3);
							new ticketsGUI(userName,role); // establish role as
														// regular user via
														// constructor call
							
							
							//System.out.println(role);
							
						} else {
							JOptionPane.showMessageDialog(null, "Please Check Username and Password ");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally {
						try {
							results.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							connect.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}

		});
		// add layout type /background color to control panel
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setBackground(Color.GRAY);
		// add controls to control panel
		controlPanel.add(namelabel);
		controlPanel.add(userText);
		controlPanel.add(passwordLabel);
		controlPanel.add(passwordText);
		controlPanel.add(loginButton);
		// lastly set visibility of Window as all controls are instantiated for
		// frame
		mainFrame.setVisible(true);

	}

	public static void main(String[] args) {
		new Login();
	}

}