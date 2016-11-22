package jinsoftms.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import jinsoftms.AbstractBusiness;
import jinsoftms.database.wechat.AccessToken;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.accesstoken.DBUtil;

public class AccessTokenController extends AbstractBusiness {
	public DBUtil db;
	
	//检查accesstoken是否有效
	public boolean checkToken(){
		db = new DBUtil();
		try{
			db.DBOpention();
//			db.createTable(db.c);
			
			AccessToken accesstoken = (AccessToken) db.queryTable(db.c, "SELECT * FROM AccessToken where id=1;");
			System.out.println(accesstoken.getAccess_token());
			if(accesstoken.getAccess_token()==null){
				return false;
			}else{
				long time = (System.currentTimeMillis() - accesstoken.getTimestamp()) / (1000 * 60);
				System.out.println("使用时长："+time);
				long expires_in =accesstoken.getExpires_in()-time;
				System.out.println("剩余有效时间："+ expires_in);
				if (expires_in > 0) {
					Map<String, String> mp = new HashMap<String, String>();
					mp.put("expires_in", (expires_in+""));
					db.updateTable(db.c, mp);
					this.resultstr=accesstoken.getAccess_token().toString() + "," + expires_in;
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}finally{
			db.DBClosetion();
		}
	}
	
	//保存accesstoken
	public boolean saveorupdate(AccessToken access_token){
		db = new DBUtil(access_token.getAccess_token(),access_token.getExpires_in());
		try{
			db.DBOpention();
			AccessToken accesstoken = (AccessToken) db.queryTable(db.c, "SELECT * FROM AccessToken where id=1;");
			if(accesstoken.getAccess_token()==null){
				db.insertTable(db.c);
			}else{
				Map<String, String> mp = new HashMap<String, String>();
				mp.put("access_token", access_token.getAccess_token());
				mp.put("expires_in", (access_token.getExpires_in()+""));
				db.updateTable(db.c, mp);
			}
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}finally{
			db.DBClosetion();
		}
		return true;
	}
	
//	public static void main(String[] args) {
//		Connection c = null;
//		Statement stmt = null;
//		try {
//			Class.forName("org.sqlite.JDBC");
//			c = DriverManager.getConnection("jdbc:sqlite:jinsoftwc.db");
//			System.out.println("Opened database successfully");
////			createTable(c, stmt);
////			queryTable(c, stmt);
//			c.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//	}
//
//	public static void createTable(Connection c, Statement stmt) {
//		try {
//			stmt = c.createStatement();
//			String sql = "CREATE TABLE AccessToken "
//					+ "(ID INT PRIMARY KEY     NOT NULL,"
//					+ " access_token           TEXT    NOT NULL, "
//					+ " expires_in            INT     NOT NULL, "
//					+ " timestamp        TEXT)";
////					+ "AddWho 		TEXT,"
////					+ "AddTime		TEXT," + "EditWho		TEXT," + "EditTime 	TEXT)";
//
//			stmt.executeUpdate(sql);
//			stmt.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("Table created successfully");
//	}
//
//	public static void insertTable(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			String sql = "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
//					+ "VALUES (1, 'Paul', 32,  '1611211702' );";
//			stmt.executeUpdate(sql);
//
//			sql = "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
//					+ "VALUES (2, 'Allen', 25, '1611211703');";
//			stmt.executeUpdate(sql);
//
//			stmt.close();
//			c.commit();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("Records created successfully");
//	}
//
//	public static void queryTable(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM AccessToken;");
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String access_token = rs.getString("access_token");
//				int expires_in = rs.getInt("expires_in");
//				String timestamp = rs.getString("timestamp");
////				String addwho = rs.getString("AddWho");
////				String addtime = rs.getString("AddTime");
////				String editwho = rs.getString("EditWho");
////				String edittime = rs.getString("EditTime");
//
//				System.out.println("ID = " + id);
//				System.out.println("access_token = " + access_token);
//				System.out.println("expires_in = " + expires_in);
//				System.out.println("timestamp = " + timestamp);
////				System.out.println("addwho = " + addwho);
////				System.out.println("addtime = " + addtime);
////				System.out.println("editwho = " + editwho);
////				System.out.println("edittime = " + edittime);
//			}
//			rs.close();
//			stmt.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("query Records successfully");
//	}
//
//	public static void updateTable(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			String sql = "UPDATE AccessToken set access_token = 'aaaaaaaaaaaaaaaaaaaaaaaa' where ID=1;";
//			stmt.executeUpdate(sql);
//			c.commit();
//			stmt.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("Update table successfully");
//	}
//
//	public static void deleteTable(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			String sql = "DELETE from AccessToken where ID=2;";
//			stmt.executeUpdate(sql);
//			c.commit();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("delete table successfully");
//	}
//
//	public static void dropTableData(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			String sql = "DELETE FROM AccessToken;";
//			stmt.executeUpdate(sql);
//			c.commit();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("drop tableData successfully");
//	}
//	
//	public static void dropTable(Connection c, Statement stmt) {
//		try {
//			c.setAutoCommit(false);
//			stmt = c.createStatement();
//			String sql = "DROP TABLE AccessToken;";
//			stmt.executeUpdate(sql);
//			c.commit();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("DROP table successfully");
//	}
//
//	public static void alterColumn(Connection c, Statement stmt) {
//		try {
//			stmt = c.createStatement();
//			String sql = "ALTER TABLE AccessToken ADD COLUMN AddTime TEXT;"
//					+ "ALTER TABLE AccessToken ADD COLUMN EditWho TEXT;"
//					+ "ALTER TABLE AccessToken ADD COLUMN EditTime TEXT;";
//
//			stmt.executeUpdate(sql);
//			stmt.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("Alter column successfully");
//	}
//	
//	/**
//	 * 没有用
//	 * @param c
//	 * @param stmt
//	 */
//	public static void dropColumn(Connection c, Statement stmt) {
//		try {
//			stmt = c.createStatement();
//			String sql = "ALTER TABLE AccessToken drop COLUMN AddWho;"
//					+ "ALTER TABLE AccessToken drop COLUMN EditTime;"
//					+ "ALTER TABLE AccessToken drop COLUMN EditWho;"
//					+ "ALTER TABLE AccessToken drop COLUMN EditTime;";
//
//			stmt.executeUpdate(sql);
//			stmt.close();
//		} catch (Exception e) {
//			System.err.println(e.getClass().getName() + ": " + e.getMessage());
//			System.exit(0);
//		}
//		System.out.println("Alter column successfully");
//	}
}
