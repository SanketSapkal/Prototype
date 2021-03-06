package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	
	protected Connection connection = null;
	
	//Database user name and password
	private String name = "root";
	private String pass = "root";
	
	//Database URL and JDBC Driver
	private String url = "jdbc:mysql://127.0.0.1:3306/prototype";
	private String driver = "com.mysql.jdbc.Driver";
	
	protected void /*Connection*/ getConnection () {
		
		if (connection == null){
			System.out.println("Registering driver....");
			try {
				//Driver Registration
				Class.forName(driver).newInstance();
				System.out.println("Driver Registered successfully!!.");
				
				//Initiate a connection
				System.out.println("Connecting to database...");
				connection = DriverManager.getConnection(url, name, pass);
				System.out.println("Connected to database!!!");
				
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Couldnt register driver...");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Couldnt connect to database...");
			}
		}
		
		//return connection;
	}
	
	/*public static void main (String [] args){
		Connection con = new Connect().getConnection();
	}*/
}
