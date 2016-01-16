package com.example.pum2016.asynctasks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public class DataBaseTask extends AsyncTask<String, Void, List<String[]>>{
	private static final String url="jdbc:mysql://db4free.net:3306/projektpum",username="projektpum",pass="projektPUM"; 
	@Override
	protected List<String[]> doInBackground(String... params) {
		ResultSet result = null;
		Connection conn=null;
		Statement query = null;
		List<String[]> wynik=null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url,username,pass);
			query = conn.createStatement();
			result = query.executeQuery(params[0]);
			wynik = obrobWynikSql(result,params[1]);
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
		return wynik;
	}
	private List<String[]> obrobWynikSql(ResultSet result, String ile) {
		List<String[]> tmp = new ArrayList<String[]>();
		int zmienne = Integer.parseInt(ile);
		try {
			String[] tmpRow = new String[zmienne];
			while(result.next()) {
				for(int i=0;i<zmienne;i++) {
					tmpRow[i] = result.getString(i+1);
					if(i==zmienne-1) tmp.add(tmpRow);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tmp;
	}

}
