package com.example.pum2016.asynctasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.pum2016.klasy.TableOptionsObject;
import com.example.pum2016.queries.QueriesStatic;

import android.os.AsyncTask;

public class DataBaseTaskSetParameters extends AsyncTask<String, Void, Boolean> {
	private static final String url="jdbc:mysql://db4free.net:3306/projektpum",username="projektpum",pass="projektPUM";
	@Override
	protected Boolean doInBackground(String... params) {
		String prefix="";
		int id_trasy=-1,afterInsert=-1;
		ResultSet result = null;
		Connection conn=null;
		Statement query = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url,username,pass);
			query = conn.createStatement();
			result = query.executeQuery(params[0]);
			while(result.next()) {
				prefix = result.getString(1);
			}
			String sql = QueriesStatic.getAfterlogin21()+prefix+QueriesStatic.getAfterlogin22();
			result = query.executeQuery(sql);
			while(result.next()) {
				id_trasy = result.getInt(1);
			}
			if(id_trasy==-1) {
				
				// Parse the input date
				SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy HH:mm");
				Date inputDate = new Date();

				// Create the MySQL datetime string
				fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = fmt.format(inputDate);
				
				String insertSQL = QueriesStatic.getAfterlogin3Ins1()+prefix+QueriesStatic.getAfterlogin3Ins2()
						+"(0,'"+dateString+"')";
				afterInsert = query.executeUpdate(insertSQL);
			}
			result = query.executeQuery(sql);
			while(result.next()) {
				id_trasy = result.getInt(1);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (result!=null && query!=null && conn!=null) {
				result.close();
				query.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(id_trasy==-1 && afterInsert==-1) return false;
		TableOptionsObject.getInstance(prefix, id_trasy);
		return true;
	}

}
