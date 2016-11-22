package jinsoftms.util.accesstoken;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import jinsoftms.database.wechat.AccessToken;
import jinsoftms.util.RuntimeExceptionUtil;

public class DBUtil {
	public Connection c = null;
	public Statement stmt = null;
	
	private String access_token;
	private int expires_in;
	
	public DBUtil(String access_token, int expires_in){
		this.access_token=access_token;
		this.expires_in=expires_in;
	}
	
	public DBUtil(){ }
	
	public void DBOpention(){
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:d:\\jinsoftwc.db");
			System.out.println("Opened database successfully");
			
//			dropTable(c);
//			queryTable(c);
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
	}
	
	public void DBClosetion() {  
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException ex) {
				throw new RuntimeExceptionUtil(ex);
			}
			stmt=null;
		}
		if(c != null){
			try {
				c.close();
			} catch (SQLException ex) {
				throw new RuntimeExceptionUtil(ex);
			}
			c=null;
		}
    }  

	public void createTable(Connection c) {
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE AccessToken "
					+ "(ID INT PRIMARY KEY     NOT NULL,"
					+ " access_token           TEXT    NOT NULL, "
//					+ " access_token1           TEXT    NOT NULL, "
//					+ " access_token2           TEXT    NOT NULL, "
					+ " expires_in            INT     NOT NULL, "
					+ " timestamp        TEXT)";
//					+ "AddWho 		TEXT,"
//					+ "AddTime		TEXT," + "EditWho		TEXT," + "EditTime 	TEXT)";

			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("Table created successfully");
	}

	public boolean insertTable(Connection c) {
		boolean flag=false;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
//			String sql = "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
//					+ "VALUES (1, 'Paul', 32,  '1611211702' );";
//			stmt.executeUpdate(sql);
//
			String sql = "INSERT INTO AccessToken (ID,access_token,expires_in,timestamp) "
					+ "VALUES (1, '"+this.access_token+"', "+this.expires_in+", '"+System.currentTimeMillis()+"');";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			flag=true;
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("Records created successfully");
		return flag;
	}

	public Object queryTable(Connection c, String sql) {
		AccessToken sle = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
//			String sql = "SELECT * FROM AccessToken;";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			sle = new AccessToken();
			while (rs.next()) {
				System.out.println(rs.getInt("id"));
				int id = rs.getInt("id");
				String access_token = rs.getString("access_token");
				int expires_in = rs.getInt("expires_in");
				long timestamp = new Long(rs.getString("timestamp"));
//				String addwho = rs.getString("AddWho");
//				String addtime = rs.getString("AddTime");
//				String editwho = rs.getString("EditWho");
//				String edittime = rs.getString("EditTime");

				System.out.println("ID = " + id);
				System.out.println("access_token = " + access_token);
				System.out.println("expires_in = " + expires_in);
				System.out.println("timestamp = " + timestamp);
//				System.out.println("addwho = " + addwho);
//				System.out.println("addtime = " + addtime);
//				System.out.println("editwho = " + editwho);
//				System.out.println("edittime = " + edittime);
				
				sle.setAccess_token(access_token);
				sle.setExpires_in(expires_in);
				sle.setTimestamp(timestamp);
			}
			rs.close();
			stmt.close();
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("query Records successfully");
		return sle;
	}

	public void updateTable(Connection c,Map<String, String> mp) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			
			String sql = "";
			if(mp.containsKey("access_token")){
				sql = "UPDATE AccessToken set access_token ='"+this.access_token+"' where id=1;";
				stmt.executeUpdate(sql);
			}
			if(mp.containsKey("expires_in")){
				sql = "UPDATE AccessToken set expires_in ='"+this.expires_in+"' where id=1;";
				stmt.executeUpdate(sql);
			}
			if(mp.containsKey("timestamp")){
				sql = "UPDATE AccessToken set timestamp ='"+System.currentTimeMillis()+"' where id=1;";
				stmt.executeUpdate(sql);
			}
			c.commit();
			stmt.close();
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("Update table successfully");
	}

	public void deleteTable(Connection c, String sql) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
//			String sql = "DELETE from AccessToken where ID=2;";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("delete table successfully");
	}

	public void dropTableData(Connection c) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE FROM AccessToken;";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("drop tableData successfully");
	}
	
	public void dropTable(Connection c) {
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DROP TABLE AccessToken;";
			stmt.executeUpdate(sql);
			c.commit();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("DROP table successfully");
	}


	public void alterTable(Connection c, String sql) {
		try {
			stmt = c.createStatement();
//			String sql = "ALTER TABLE AccessToken ADD COLUMN AddTime TEXT;"
//					+ "ALTER TABLE AccessToken ADD COLUMN EditWho TEXT;"
//					+ "ALTER TABLE AccessToken ADD COLUMN EditTime TEXT;";

			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception ex) {
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
			throw new RuntimeExceptionUtil(ex);
		}
		System.out.println("Alter column successfully");
	}
}
