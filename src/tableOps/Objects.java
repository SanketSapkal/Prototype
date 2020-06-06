package tableOps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import adminOps.Response;
import pojos.ObjectsModel;
import connect.Connect;

public class Objects extends Connect{
	
	private String name, description, image, date, check, operation, token;
	private String id, code, message;
	private ObjectsModel om;
	private Response res = new Response();
	
	public Response selectOp(JSONObject obj, ObjectsModel obm) {
		try {
			operation = obj.getString("operation");
			operation = operation.toLowerCase();
			om = obm;
			
			switch(operation) {
			
			case "add" :
				System.out.println("Add op is selected..");
				Add();
				break;
				
			case "delete" : 
				System.out.println("Delete operation is selected");
				Delete();
				break;
				
			case "edit" :
				System.out.println("Edit operation is selected.");
				Edit();
				break;
				
			case "next" :
				System.out.println("Next operation is selected.");
				token = obj.getString("token");
				System.out.println(token);
				Next();
				break;
				
			case "previous" :
				System.out.println("Previous operation is selected.");
				token = obj.getString("token");
				System.out.println(token);
				Previous();
				break;
				
			default:
				res.setData("0", "INVALID_OPERATION", "Error in JSON Data Construction");
				break;
			}
			
		} catch (JSONException e) {
			System.out.println("JSON Exception!");
			res.setData("0", "JSON_EXCEPTION", "JSON Data cannot be unparsed..");
			e.printStackTrace();
		}
	
		return res;
	}
	
	private void Add() {
		name = om.getName();
		description = om.getDescription();
		image = om.getImage();
		
		System.out.println("Inside Add method..");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = sdf.format(cal.getTime());
		System.out.println(date);
		
		String sql = "insert into objects (Name, Description, Image, Date) values (?,?,?,?)";
		getConnection();
		
		try {
			System.out.println("Creating Statement.....");
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			System.out.println("Statement created. Executing Query....");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, image);
			stmt.setString(4, date);
			
			stmt.executeUpdate();
			System.out.println("Object added to table");
			res.setData("0", "ADD_S", "Object added to table.");
			
		} catch (SQLException e) {
			
			System.out.println("Couldn't create statement...");
			e.printStackTrace();
			res.setData("0","SQL_EXCEPTION","Couldn't create statement (SQL Exception)");
			
		}
	}
	
	private void Delete() {
		name = om.getName();
		check = null;
		System.out.println("Inside delete method....");
		
		String sql = "DELETE FROM objects WHERE Name = ?";
		String sql2 = "SELECT * FROM objects WHERE Name = ?";
		
		getConnection();
		
		try {
			System.out.println("Creating statement...");
			
			//checking whether the input name is present in table
			PreparedStatement stmt2 = connection.prepareStatement(sql2);
			stmt2.setString(1, name);
			ResultSet rs = stmt2.executeQuery();
			while(rs.next()) {
				check = rs.getString("Name");
			}
			
			if(check != null){
				
				PreparedStatement stmt = connection.prepareStatement(sql);
				
				System.out.println("Statement created. Executing delete query..." + check);
				stmt.setString(1, name);
				stmt.executeUpdate();
				id = String.valueOf(name);
				message = "operation successfull deleted object :" + name;
				code = "DELETE_S";
				res.setData(id, code, message);
			}
			else {
				System.out.println("Entry not found in database!!");
				res.setData("0","NO_ENTRY","No entry found found in database!!");
			}
			
		} catch (SQLException e) {
			res.setData("0","SQL_EXCEPTION","Couldn't create statement (SQL Exception)");
			e.printStackTrace();
		}
	}
	
	private void Edit() {
		check = null;
		name = om.getName();
		description = om.getDescription();
		
		System.out.println("Inside edit method...");
		String sql = "UPDATE objects SET Description=? WHERE Name=?";
		String sql2 = "SELECT * FROM objects WHERE Name=?";
		
		getConnection();
		
		try {
			System.out.println("Creating statement...");
			
			PreparedStatement stmt2 = connection.prepareStatement(sql2);
			stmt2.setString(1, name);
			
			//Checking for entered entry..
			ResultSet rs = stmt2.executeQuery();
			while(rs.next()) {
				check = rs.getString("Name");
			}
			
			if(check != null) {
				PreparedStatement stmt = connection.prepareStatement(sql);
				
				System.out.println("Statement created. Executing edit query...");
				stmt.setString(1, description);
				stmt.setString(2, name);
				
				stmt.executeUpdate();
				message = "operation successfull edited object : " +id;
				id = check;
				code = "EDIT_S";
				res.setData(id, code, message);
			}
			else {
				System.out.println("Entry not found in database!!");
				res.setData("0","NO_ENTRY","No entry found found in database!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			res.setData("0","SQL_EXCEPTION","Couldn't create statement (SQL Exception)");
		}
	}
	
	private void Next() {
		check = null;
		System.out.println("Inside Next Method..");
		
		String sql = "SELECT * FROM objects WHERE Name > ? LIMIT 1";
		getConnection();
		try {
			System.out.println("Creating a statement .....");
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			System.out.println("Statement created. Executing getNext query...");
			stmt.setString(1, token);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("name", rs.getString("Name"));
				json.put("description", rs.getString("Description"));
				json.put("image", rs.getString("Image"));
				json.put("date", rs.getString("Date"));
				
				message = json.toString();
				System.out.println(message);
				check = rs.getString("Name");
				//System.out.println(id);
			}
			if(check != null ) {
				code = "NEXT_S";
				id = check;
			}
			else {
				id = "0";
				message = "END OF DATABASE";
				code = "END_OF_DB";
			}
			
			res.setData(id, code, message);
		} catch (SQLException e) {
			res.setData("0", "SQL_EXCEPTION", "SQL Exception occured");
			e.printStackTrace();
		} catch (JSONException e) {
			res.setData("0", "JSON_EXCEPTION", "JSON Exception occured");
			e.printStackTrace();
		}
	}
	
	private void Previous() {
		check = null;
		System.out.println("Inside Previous Method..");
		
		String sql = "SELECT * FROM objects WHERE Name < ? ORDER BY Name DESC LIMIT 1";
		getConnection();
		try {
			System.out.println("Creating a statement .....");
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			System.out.println("Statement created. Executing getNext query...");
			stmt.setString(1, token);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("name", rs.getString("Name"));
				json.put("description", rs.getString("Description"));
				json.put("image", rs.getString("Image"));
				json.put("date", rs.getString("Date"));
				
				message = json.toString();
				System.out.println(message);
				check = rs.getString("Name");
				//System.out.println(id);
			}
			if(check != null ) {
				code = "PREVIOUS_S";
				id = check;
			}
			else {
				id = "0";
				message = "END OF DATABASE";
				code = "END_OF_DB";
			}
			
			res.setData(id, code, message);
		} catch (SQLException e) {
			res.setData("0", "SQL_EXCEPTION", "SQL Exception occured");
			e.printStackTrace();
		} catch (JSONException e) {
			res.setData("0", "JSON_EXCEPTION", "JSON Exception occured");
			e.printStackTrace();
		}
	}
}
