package jinsoftms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccessTokenController {
	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:jinsoftwc.db");
			System.out.println("Opened database successfully");

//			 stmt = c.createStatement();
//			 String sql = "CREATE TABLE AccessToken "
//			 + "(ID INT PRIMARY KEY     NOT NULL,"
//			 + " access_token           TEXT    NOT NULL, "
//			 + " expires_in            INT     NOT NULL, "
//			 + " timestamp        TEXT, " + "AddWho 		TEXT,"
//			 + "AddTime		TEXT," + "EditWho		TEXT," + "EditTime 	TEXT)";
//			
//			 stmt.executeUpdate(sql);
//			 System.out.println("Table created successfully");
			
//			 c.setAutoCommit(false);
//			 stmt = c.createStatement();
//			 String sql =
//			 "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
//			 +
//			 "VALUES (1, 'Paul', 32,  '1611211702' );";
//			 stmt.executeUpdate(sql);
//			
//			 sql =
//			 "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
//			 +
//			 "VALUES (2, 'Allen', 25, '1611211703');";
//			 stmt.executeUpdate(sql);
//			 stmt.close();
//			 c.commit();
			
//			c.setAutoCommit(false);
//			System.out.println("Opened database successfully");
//
//			stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM AccessToken;");
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String access_token = rs.getString("access_token");
//				int expires_in = rs.getInt("expires_in");
//				String timestamp = rs.getString("timestamp");
//				String addwho = rs.getString("AddWho");
//				String addtime = rs.getString("AddTime");
//				System.out.println("ID = " + id);
//				System.out.println("NAME = " + access_token);
//				System.out.println("AGE = " + expires_in);
//				System.out.println("ADDRESS = " + timestamp);
//				System.out.println("SALARY = " + addwho);
//				System.out.println("SALARY = " + addtime);
//			}
//			rs.close();
//			stmt.close();
//			System.out.println("Operation done successfully");
			
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}
}
