package com.example.pum2016.asynctasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;

public class DataBaseTaskNonQuery extends AsyncTask<String, Void, Integer>{
	private static final String url="jdbc:mysql://db4free.net:3306/projektpum",username="projektpum",pass="projektPUM"; 
	@Override
	protected Integer doInBackground(String... params) {
		int result = 0;
		Connection conn=null;
		Statement query = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url,username,pass);
			query = conn.createStatement();
			result = query.executeUpdate(params[0]);
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
			if (query!=null && conn!=null) {
				query.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}