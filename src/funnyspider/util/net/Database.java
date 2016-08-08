package funnyspider.util.net;
import java.sql.*;
import java.util.Map;
public class Database {
		
	private static Connection conn = null;
	
	private Database(){
		
	}
	
	
	public static void connnectDB(String driveName,String dataBaseName,String username,String password)
			throws ClassNotFoundException, SQLException{
		if(conn == null){
			Class.forName(driveName);
			conn = DriverManager.getConnection(dataBaseName,username,password);
		}
	}
	
	
	public static Connection getConnection(){
		return conn;
	}
	
	
	public static boolean isConnect(){
		return conn == null;
	}
	
	public static void insert(String tableName,Map<String,String> param) 
			throws Exception{
		if(isConnect()){
			Statement sta = conn.createStatement();
			String sql = "insert into " + tableName + "(";
			String sql2  ="value(";
			int num = 1;
			for (Map.Entry<String, String> entry :param.entrySet()){
				sql += "\'" + entry.getKey()+ "\'";
				sql2 += "\'" + entry.getValue()+ "\'";
				if(num != param.size()){
					sql += ",";
					sql2 +=",";
					num++;
				}
			}
			sql+= ") "+ sql2+")";
			sta.executeUpdate(sql);
			sta.close();
		}else{
			throw new IllegalArgumentException("Connection is null");
		}
	}
	
	public static void close() throws SQLException{
		conn.close();
		conn = null;
	}
	
	
}
