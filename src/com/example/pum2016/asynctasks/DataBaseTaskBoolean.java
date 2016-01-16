package com.example.pum2016.asynctasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.AsyncTask;

public class DataBaseTaskBoolean extends AsyncTask<String, Void, Boolean>{
	private static final String url="jdbc:mysql://db4free.net:3306/projektpum",username="projektpum",pass="projektPUM"; 
	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = false;
		Connection conn=null;
		Statement query = null;
		ResultSet set = null;
		String haslo = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url,username,pass);
			query = conn.createStatement();
			set = query.executeQuery(params[0]);
			while(set.next()) {
				haslo = set.getString(1);				
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
			if (set!=null && query!=null && conn!=null) {
				set.close();
				query.close();
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(haslo.equals(params[1])) return true;
		return result;
	}
}