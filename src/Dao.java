/**
 *@author Minh Nguyen
 *ITMD 411-Final
 *4/24/2018
 *Dao.java
 *This class will allow for database connectivity and CRUD (Create Read Update Delete) like operations.  
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;
	static List<String> rowData;

	// constructor
	public static Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE mnguye13_tickets3(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30) NOT NULL, ticket_description VARCHAR(200) NOT NULL, ticket_date DATETIME NOT NULL, ticket_status VARCHAR(10) NOT NULL)";
		final String createUsersTable = "CREATE TABLE mnguye13_users3(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30) NOT NULL, upass VARCHAR(30) NOT NULL, urole VARCHAR(30) NOT NULL)";
		final String createDeleteLogTable = "CREATE TABLE mnguye13_deleteLog3(delete_id INT AUTO_INCREMENT PRIMARY KEY, ticket_id INT, ticket_deleter VARCHAR(30) NOT NULL, delete_reason VARCHAR(200) NOT NULL, delete_date DATETIME NOT NULL)";

		
		try {

			// create table

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			statement.executeUpdate(createDeleteLogTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	@SuppressWarnings("null")
	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;
		Connection connect = null;
		Statement statement = null;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
														// spreadsheet rows &
														// columns

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into mnguye13_users3(uname,upass,urole) " + "values('" + rowData.get(0) + "','" + rowData.get(1) + "','" + rowData.get(2)
						+ "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	// add other desired CRUD methods needed like for updates, deletes, etc.
	
}
