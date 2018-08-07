/**
 *@author Minh Nguyen
 *ITMD 411-Final
 *4/24/2018
 *ticketGUI.java
 *This class will create main GUI for user to access and interact with the database.
 *Admin can create, view, update, delete tickets but normal user only can view and update ticket.  
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ticketsGUI extends JFrame implements  ActionListener {
	
	public static final String[] status = { "Requested", "Opened", "Closed","Awaiting","Responded","Escalated", "Resolved" };
	public static final String[] viewOption = { "ticket_id", "ticket_issuer", "ticket_description","ticket_date","ticket_status"};
	static java.sql.Timestamp RealTime = new java.sql.Timestamp(new java.util.Date().getTime());

	// class level member objects

	Dao dao = new Dao(); // for CRUD operations
	String chkIfAdmin = null;
	private JFrame mainFrame, layoutFrame;

	JScrollPane sp = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	/* add any more Main menu object items below */

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemChangeStatus;
	JLabel loginNote;
	JTable jt;
	JButton CreateTicket, ViewTicket, UpdateTicket, DeleteTicket, LogOut, switchUser, submitUpdate,submitCreate;
	JPanel sub, sub1,sub2,sub3,sub4;
	JRadioButton asc, desc;
	ButtonGroup order;
	JComboBox statCb, viewCb;
	JTextField UpdateID;
	JTextArea Udescription;
	JLabel UpdateId, UpdateInfo, UpdateStat;
	JTextField CreateName;
	JTextArea Cdescription;
	JLabel CreateIssuer, Createinfo, CreateStat;

	/* add any more Sub object items below */

	// constructor
	public void refresh()
	{
		
	}
	
	public ticketsGUI(String verifyRole, String role) throws SQLException {

		chkIfAdmin = verifyRole; 
		JOptionPane.showMessageDialog(null, "Welcome " + verifyRole);
		if (chkIfAdmin.equals("Admin") ||   role.equals("admin"))
		{
			System.out.println("ADMIN GUI");
			dao.createTables(); // fire up table creations (tickets / user
		}	// tables)
		else if(role.equals("user"))
		{
			System.out.println("USER GUI");
		}
		/*
		 * else do something else if you like
		 *
		 */

		createMenu();
		prepareGUI(role);
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);
		
		//
		mnuItemChangeStatus = new JMenuItem("Change Ticket Status");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemChangeStatus);
		
		
		

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemChangeStatus.addActionListener(this);

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI(String role) throws SQLException {
		// initialize frame object
		Connection dbConn = DriverManager
				.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
						+ "&user=fp411&password=411");

		Statement statement = dbConn.createStatement();
		mainFrame = new JFrame("Tickets System-Login as "+role.toUpperCase()+"@ "+RealTime);

		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if(role.equals("admin"))
		{
			bar.add(mnuAdmin);
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		mainFrame.setJMenuBar(bar);

		mainFrame.addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		mainFrame.setSize(1500, 500);
		mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		//loginNote=new JLabel("You are login as "+role);
		//loginNote.setHorizontalAlignment(JLabel.CENTER);
		
		sub = new JPanel();
		sub1= new JPanel();
		sub3= new JPanel();
		sub3.setLayout(new BoxLayout(sub3, BoxLayout.Y_AXIS));
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.Y_AXIS));
		sub4= new JPanel();
		//sub4.setLayout(new BoxLayout(sub4, BoxLayout.PAGE_AXIS));
		sub4.setPreferredSize(new Dimension (100, 200));
		order = new ButtonGroup();
		CreateTicket=new JButton("New Ticket");
		ViewTicket= new JButton("View Ticket");
		UpdateTicket=new JButton("Update Ticket");
		DeleteTicket= new JButton("Delete Ticket");
		LogOut= new JButton("Log Out        ");
		switchUser= new JButton("Switch User");
		
		asc = new JRadioButton("Ascending  ");
		desc= new JRadioButton("Descending");
		//byId= new JRadioButton("ID");
		//byName= new JRadioButton("Name");
		//byStatus= new JRadioButton("Status");
		//byDate = new JRadioButton("Date");
		
		asc.setMaximumSize(desc.getPreferredSize());
		
		sub1.add(CreateTicket);
		CreateTicket.setMaximumSize(UpdateTicket.getPreferredSize());
		DeleteTicket.setMaximumSize(UpdateTicket.getPreferredSize());
		if(role.equals("admin"))
		{
			//UpdateTicket=new JButton("Update Ticket");
			//DeleteTicket= new JButton("Delete Ticket");
			sub1.add(UpdateTicket);
			sub1.add(DeleteTicket);
		}
		sub3.add(switchUser);
		sub3.add(LogOut);
		LogOut.setMaximumSize(switchUser.getPreferredSize());
		sub.add(ViewTicket);
		order.add(asc);
		order.add(desc);
		sub.add(asc);
		sub.add(desc);
		
		
		//order.add(byStatus);
		//order.add(byDate);
		//sub3.add(byId);
		//sub3.add(byName);
		//sub3.add(byStatus);
		//sub3.add(byDate);
		
		viewCb= new JComboBox(viewOption);
		viewCb.setMaximumSize(asc.getPreferredSize());
		sub.add(viewCb);
		
		
		
		
		
		
		
		ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

		// Use JTable built in functionality to build a table model and
		// display the table model off your result set!!!
		jt = new JTable(ticketsJTable.buildTableModel(view));

		jt.setBounds(30, 40, 200, 300);
	    sp = new JScrollPane(jt);
		//mainFrame.add(sp);
		mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
		mainFrame.getContentPane().add(sub, BorderLayout.NORTH);
		mainFrame.getContentPane().add(sub1, BorderLayout.WEST);
		mainFrame.getContentPane().add(sub3, BorderLayout.EAST);
		mainFrame.getContentPane().add(sub4,BorderLayout.SOUTH);
		mainFrame.setVisible(true); // refreshes or repaints frame on
									// screen
		LogOut.addActionListener(this);
		switchUser.addActionListener(this);
		CreateTicket.addActionListener(this);
		UpdateTicket.addActionListener(this);
		DeleteTicket.addActionListener(this);
		ViewTicket.addActionListener(this);
		
		viewCb.addActionListener(this);
	
		
	}
	boolean valid= false;
	private String ticketName=null;
	private String ticketDesc=null;

	/*
	 * action listener fires up items clicked on from sub menus with one action
	 * performed event handler!
	 */
	@Override
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit || e.getSource() == LogOut) {
			System.exit(0);
		} else if(e.getSource()== switchUser){
			mainFrame.dispose();
			new Login();
			
		} else if(e.getSource()== ViewTicket){
			
			String Order=null;
			
			
			if(asc.isSelected()){
				Order="asc";
			}
			else if (desc.isSelected())
			{
				Order="desc";
			}
			else
			{
				Order="desc";
			}
				//System.out.println(viewCb.getSelectedItem());
			
			try {

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();

				ResultSet results = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by "+ viewCb.getSelectedItem()+" "+ Order+";");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(results));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen  
				statement.close();
				dbConn.close(); // close connections!!!

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
						
		} 
		else if (e.getSource() == mnuItemOpenTicket) {

			try {
				ticketName=null;
				ticketDesc=null;
				valid=false;
				while(valid==false)
				{

				// get ticket information
					ticketName = JOptionPane.showInputDialog(null, "Enter your name");
					if (ticketName.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid name! Try again!");
					}
					else
					{
						valid=true;
					}
				}
				valid=false;
				while(valid==false)
				{
					ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
					if (ticketDesc.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid description! Try again!");
					}
					else
					{
						valid=true;
					}
				}
				
				
				java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				String ticket_status = (String) JOptionPane.showInputDialog(null, 
				        "Enter ticket status",
				        "ticket status",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        status, 
				        status[0]);

				// insert ticket information to database
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
				
				
				
				if (ticketName != null && !ticketName.isEmpty() && ticketDesc !=null && !ticketDesc.isEmpty() ) {
					  // doSomething
				
					int result = statement
						.executeUpdate("Insert into mnguye13_tickets3(ticket_issuer, ticket_description, ticket_date, ticket_status) values(" + " '"
								+ ticketName + "','" + ticketDesc + "','" + date+ "','" + ticket_status + "')", Statement.RETURN_GENERATED_KEYS);
				
				
					// retrieve ticket id number newly auto generated upon record
					// insertion
					ResultSet resultSet = null;
					resultSet = statement.getGeneratedKeys();
				
				
					int id = 0;
					if (resultSet.next()) {
						id = resultSet.getInt(1); // retrieve first field in table
					}
					// display results if successful or not to console / dialog box
					if (result != 0) {
						System.out.println("Ticket ID : " + id + " created successfully!!!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
					} else {
						System.out.println("Ticket cannot be created!!!");
						JOptionPane.showMessageDialog(null,"TICKET CANNOT BE CREATED","ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Invalid Insertion! Ticket cannot be created");
				}
				
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen

			} catch (SQLException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} else if (e.getSource() == mnuItemViewTicket) {

			// retrieve ticket information for viewing in JTable

			try {

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();

				ResultSet results = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(results));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen
				statement.close();
				dbConn.close(); // close connections!!!

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource() == mnuItemDelete || e.getSource()== DeleteTicket) 
			// delete ticket 
		{
			valid=false;
			String id=null;
			String reason = null;
			String deleter= null;
			 JFrame frame = new JFrame("DELETE TICKET");
			 while (valid==false)
			    {
				 id = JOptionPane.showInputDialog(
					        frame, 
					        "Enter ticket ID to continue", 
					        "DELETE TICKET", 
					        JOptionPane.WARNING_MESSAGE);
			        if (id.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid ID! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			 valid=false;
			    while (valid==false)
			    {
			    	deleter = JOptionPane.showInputDialog(
			    			frame, 
			    			"Enter your name",
			    			JOptionPane.WARNING_MESSAGE);
			    	if (deleter.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid description! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			    valid=false;
			    while (valid==false)
			    {
			    	reason = JOptionPane.showInputDialog(
			    			frame, 
			    			"Enter reason to delete the ticket",
			    			JOptionPane.WARNING_MESSAGE);
			    	if (reason.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid description! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			try {

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");
				

				Statement statement = dbConn.createStatement();
				java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				
				int result = statement.executeUpdate("Insert into mnguye13_deleteLog3(ticket_id, ticket_deleter, delete_reason, delete_date) values(" + " '"+ id + "','" + deleter + "','" + reason+ "','" + date + "')", Statement.RETURN_GENERATED_KEYS);
				int results = statement.executeUpdate("DELETE FROM mnguye13_tickets3 WHERE ticket_id = "+id+";",Statement.RETURN_GENERATED_KEYS);
				
				
				ResultSet resultSet = null;
				resultSet = statement.getGeneratedKeys();
			
			
				int id1 = 0;
				if (resultSet.next()) {
					id1 = resultSet.getInt(1); // retrieve first field in table
				}
				// display results if successful or not to console / dialog box
				if (results != 0) {
					System.out.println("Ticket ID : " + id + " deleted successfully!!!");
					//JOptionPane.showMessageDialog(null, "Ticket id: " + id1 + " deleted");
					JOptionPane.showMessageDialog(null,"DELETE TICKET [" +id+ "] SUCCESSFULLY!","TICKET DELETED",JOptionPane.INFORMATION_MESSAGE);
				} else {
					System.out.println("Ticket cannot be deleted!!!");
					JOptionPane.showMessageDialog(null,"TICKET CANNOT BE DELETED","ERROR",JOptionPane.ERROR_MESSAGE);
				}
				
				
				
				
				
				
				
				
				
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen
				statement.close();
				dbConn.close(); // close connections!!!
				
				

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource()==mnuItemUpdate)
		{
			valid=false;
			String description = null;
			String id=null;
			JFrame frame = new JFrame("UPDATE TICKET");
			  while (valid==false)
			    {
			        id = JOptionPane.showInputDialog(
			        frame, 
			        "Enter ticket ID to continue", 
			        "UPDATE TICKET", 
			        JOptionPane.WARNING_MESSAGE);
			        if (id.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid ID! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			    valid=false;
			    while (valid==false)
			    {
			    	description = JOptionPane.showInputDialog(
			    			frame, 
			    			"Enter ticket description to change",
			    			JOptionPane.WARNING_MESSAGE);
			    	if (description.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid description! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			try {

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
				int results = statement.executeUpdate("UPDATE mnguye13_tickets3 SET ticket_description = '"+description+"' WHERE ticket_id ="+id+";",Statement.RETURN_GENERATED_KEYS);
				
				ResultSet resultSet = null;
				resultSet = statement.getGeneratedKeys();
			
			
				int id1 = 0;
				if (resultSet.next()) {
					id1 = resultSet.getInt(1); // retrieve first field in table
				}
				// display results if successful or not to console / dialog box
				if (results != 0) {
					System.out.println("Ticket ID : " + id + " updated successfully!!!");
					JOptionPane.showMessageDialog(null,"UPDATE TICKET ["+id+"] SUCCESFULLY!","TICKET UPDATE",JOptionPane.INFORMATION_MESSAGE);
				} else {
					System.out.println("Ticket cannot be deleted!!!");
					JOptionPane.showMessageDialog(null,"TICKET CANNOT BE UPDATED","ERROR",JOptionPane.ERROR_MESSAGE);
				}
				
				
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen

				statement.close();
				dbConn.close(); // close connections!!!
				//JOptionPane.showMessageDialog(null,"UPDATE TICKET ["+id+"] SUCCESFULLY!","TICKET UPDATE",JOptionPane.INFORMATION_MESSAGE);

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (e.getSource()==mnuItemChangeStatus)
		{
			valid=false;
			String id=null;
			 JFrame frame = new JFrame("CHANGE TICKET STATUS");
			 while (valid==false)
			    {
				 id = JOptionPane.showInputDialog(
					        frame, 
					        "Enter ticket ID to continue", 
					        "CHANGE TICKET STATUS", 
					        JOptionPane.WARNING_MESSAGE);
			        if (id.isEmpty()){
						JOptionPane.showMessageDialog(null, "Invalid ID! Try again!");
					}
					else
					{
						valid=true;
					}
			    }
			    String ticket_status = (String) JOptionPane.showInputDialog(frame, 
				        "Enter ticket status",
				        "ticket status",
				        JOptionPane.QUESTION_MESSAGE, 
				        null, 
				        status, 
				        status[0]);
			try {

				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
				int results = statement.executeUpdate("UPDATE mnguye13_tickets3 SET ticket_status = '"+ticket_status+"' WHERE ticket_id ="+id+";",Statement.RETURN_GENERATED_KEYS);
				
				ResultSet resultSet = null;
				resultSet = statement.getGeneratedKeys();
			
			
				int id1 = 0;
				if (resultSet.next()) {
					id1 = resultSet.getInt(1); // retrieve first field in table
				}
				// display results if successful or not to console / dialog box
				if (results != 0) {
					System.out.println("Ticket ID : " + id + " updated successfully!!!");
					JOptionPane.showMessageDialog(null,"UPDATE TICKET ["+id+"] SUCCESFULLY!","TICKET UPDATE",JOptionPane.INFORMATION_MESSAGE);
				} else {
					System.out.println("Ticket cannot be deleted!!!");
					JOptionPane.showMessageDialog(null,"TICKET CANNOT BE UPDATED","ERROR",JOptionPane.ERROR_MESSAGE);
				}
				
				
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen

				statement.close();
				dbConn.close(); // close connections!!!
				//JOptionPane.showMessageDialog(null,"UPDATE TICKET ["+id+"] SUCCESFULLY!","TICKET UPDATE",JOptionPane.INFORMATION_MESSAGE);

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}else if (e.getSource()==CreateTicket)
		{
			sub4.setVisible(false);
			//jt.setVisible(false);
			//sub4.setVisible(false);
			try {
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));

				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				//sp.setVisible(true);	
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen

				statement.close();
				dbConn.close(); // close connections!!!
				
				
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			System.out.println("Button Create");
			
			CreateName = new JTextField(10);
			Cdescription = new JTextArea(5,20);
			
			CreateIssuer= new JLabel("Issuer Name: ");
			Createinfo= new JLabel("Description: ");
			CreateStat= new JLabel("Status: ");
			
			
			submitCreate=new JButton("CREATE");
			sub4= new JPanel();
			//sub4.setLayout(new BoxLayout(sub4, BoxLayout.PAGE_AXIS));
			sub4.setPreferredSize(new Dimension (100, 200));
			sub4.add(CreateIssuer);
			sub4.add(CreateName);
			
			
			sub4.add(Createinfo);
			sub4.add(Cdescription);
			sub4.add(CreateStat);
			statCb=new JComboBox(status);
			//cb.setMaximumSize( asc.getPreferredSize() );
			sub4.add(statCb);
			
			sub4.add(submitCreate);
		    //mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
			mainFrame.getContentPane().add(sub4,BorderLayout.SOUTH);
			mainFrame.setVisible(true);
			submitCreate.addActionListener(this);
			
			
			
		}
		else if (e.getSource()==UpdateTicket)
		{
			sub4.setVisible(false);
			//jt.setVisible(false);
			
			try {
				Connection dbConn = DriverManager
						.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
								+ "&user=fp411&password=411");

				Statement statement = dbConn.createStatement();
				ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				jt = new JTable(ticketsJTable.buildTableModel(view));
				jt.setBounds(30, 40, 200, 300);
				sp = new JScrollPane(jt);
				//mainFrame.add(sp);
				mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
				mainFrame.setVisible(true); // refreshes or repaints frame on
				//sp.setVisible(true);							// screen
				statement.close();
				dbConn.close(); // close connections!!!
				
				
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			System.out.println("Button Update");
			
			
			UpdateID = new JTextField(10);
			Udescription = new JTextArea(5,20);
			
			UpdateId= new JLabel("ID: ");
			UpdateInfo= new JLabel("Description: ");
			UpdateStat= new JLabel("Status: ");
			
			
			submitUpdate=new JButton("UPDATE");
			sub4= new JPanel();
			//sub4.setLayout(new BoxLayout(sub4, BoxLayout.PAGE_AXIS));
			sub4.setPreferredSize(new Dimension (100, 200));
			sub4.add(UpdateId);
			sub4.add(UpdateID);
			
			
			sub4.add(UpdateInfo);
			sub4.add(Udescription);
			
			sub4.add(UpdateStat);
			statCb=new JComboBox(status);
			//cb.setMaximumSize( asc.getPreferredSize() );
			sub4.add(statCb);
			
			sub4.add(submitUpdate);
			
			submitUpdate.addActionListener(this);
		    //mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
			mainFrame.getContentPane().add(sub4,BorderLayout.SOUTH);
			mainFrame.setVisible(true);
			
		} else if (e.getSource()==submitUpdate) {
			System.out.println(UpdateID.getText());
			System.out.println(Udescription.getText());
			System.out.println(statCb.getSelectedItem());
			if(UpdateID.getText().isEmpty() || Udescription.getText().isEmpty())
			{
				System.out.println("ERROR");
				JOptionPane.showMessageDialog(null,"Error! Missing 1 or more field(s)","INVALID",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				try {

					Connection dbConn = DriverManager
							.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
									+ "&user=fp411&password=411");

					Statement statement = dbConn.createStatement();
					int results = statement.executeUpdate("UPDATE mnguye13_tickets3 SET ticket_description = '"+Udescription.getText()+"', ticket_status = '"+statCb.getSelectedItem() +"'  WHERE ticket_id ="+UpdateID.getText()+";",Statement.RETURN_GENERATED_KEYS);
					ResultSet resultSet = null;
					resultSet = statement.getGeneratedKeys();
					int id1 = 0;
					if (resultSet.next()) {
						id1 = resultSet.getInt(1); // retrieve first field in table
					}
					// display results if successful or not to console / dialog box
					if (results != 0) {
						System.out.println("Ticket ID : " + UpdateID.getText() + " updated successfully!!!");
						JOptionPane.showMessageDialog(null,"UPDATE TICKET ["+UpdateID.getText()+"] SUCCESFULLY!","TICKET UPDATE",JOptionPane.INFORMATION_MESSAGE);
						
					} else {
						System.out.println("Ticket cannot be updated!!!");
						JOptionPane.showMessageDialog(null,"TICKET CANNOT BE UPDATED","ERROR",JOptionPane.ERROR_MESSAGE);
					}

					ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");
					
					// Use JTable built in functionality to build a table model and
					// display the table model off your result set!!!
					jt = new JTable(ticketsJTable.buildTableModel(view));

					jt.setBounds(30, 40, 200, 300);
					sp = new JScrollPane(jt);
					//mainFrame.add(sp);
					mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
					mainFrame.setVisible(true); // refreshes or repaints frame on
												// screen

					statement.close();
					dbConn.close(); // close connections!!!
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				UpdateID.setText("");
				Udescription.setText("");
				statCb.setSelectedIndex(0);
			}
			
		} else if (e.getSource()==submitCreate) {
			System.out.println(CreateName.getText());
			System.out.println(Cdescription.getText());
			System.out.println(statCb.getSelectedItem());
			if(CreateName.getText().isEmpty() || Cdescription.getText().isEmpty())
			{
				System.out.println("ERROR");
				JOptionPane.showMessageDialog(null,"Error! Missing 1 or more field(s)","INVALID",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				try {
					java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());

					// insert ticket information to database
					Connection dbConn = DriverManager
							.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
									+ "&user=fp411&password=411");

					Statement statement = dbConn.createStatement();
					
						  // doSomething
					
						int result = statement
							.executeUpdate("Insert into mnguye13_tickets3(ticket_issuer, ticket_description, ticket_date, ticket_status) values(" + " '"
									+ CreateName.getText() + "','" + Cdescription.getText() + "','" + date+ "','" + statCb.getSelectedItem() + "')", Statement.RETURN_GENERATED_KEYS);
					
					
						// retrieve ticket id number newly auto generated upon record
						// insertion
						ResultSet resultSet = null;
						resultSet = statement.getGeneratedKeys();
					
					
						int id = 0;
						if (resultSet.next()) {
							id = resultSet.getInt(1); // retrieve first field in table
						}
						// display results if successful or not to console / dialog box
						if (result != 0) {
							System.out.println("Ticket ID : " + id + " created successfully!!!");
							//JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
							JOptionPane.showMessageDialog(null,"CREATE TICKET ["+id+"] SUCCESFULLY!","TICKET CREATED",JOptionPane.INFORMATION_MESSAGE);
						} else {
							System.out.println("Ticket cannot be created!!!");
							JOptionPane.showMessageDialog(null,"TICKET CANNOT BE CREATED","ERROR",JOptionPane.ERROR_MESSAGE);
						}
					
					ResultSet view = statement.executeQuery("SELECT * FROM mnguye13_tickets3 order by ticket_date desc");

					// Use JTable built in functionality to build a table model and
					// display the table model off your result set!!!
					jt = new JTable(ticketsJTable.buildTableModel(view));

					jt.setBounds(30, 40, 200, 300);
					sp = new JScrollPane(jt);
					//mainFrame.add(sp);
					mainFrame.getContentPane().add(sp, BorderLayout.CENTER);
					mainFrame.setVisible(true); // refreshes or repaints frame on
												// screen

				} catch (SQLException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				CreateName.setText("");
				Cdescription.setText("");
				statCb.setSelectedIndex(0);
				
				
			}
			
		}
		/*
			 * continue implementing any other desired sub menu items (like for
			 * update and delete sub menus for example) with similar syntax &
			 * logic as shown above*
			 */
		
		
	}

	private JButton JButton(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
