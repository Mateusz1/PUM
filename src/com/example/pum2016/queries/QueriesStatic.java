package com.example.pum2016.queries;

public class QueriesStatic {
	private static final String checkPasswordQuery ="select p.password from pracownicy p join sprzet s on p.id=s.id_prac where s.imei=";
	private static final String insertPassword1 ="update pracownicy p join sprzet s on p.id=s.id_prac set `password`=";
	private static final String insertPassword2 =" where s.imei=";
	private static final String selectPassword ="select p.password from pracownicy p join sprzet s on s.id_prac=p.id where s.imei=";
	private static final String afterLogin1 = "select prefix from sprzet where imei=";
	private static final String afterLogin2_1 = "select id from ";
	private static final String afterLogin2_2 = "_trasy where zakonczona=0";
	private static final String afterLogin3_ins1 = "insert into ";
	private static final String afterLogin3_ins2 = "_trasy (zakonczona, data_rozp) values";
	private static final String insertEvent1 = "insert into ";
	private static final String insertEvent2 = "_zdarzenia (id_trasy,tresc,data) values";
	private static final String insertPoints2 = "_punkty (id_trasy,x,y,data) values";
	private static final String update = "update ";
	private static final String update2 = "_trasy set zakonczona=1,data_zak=";
	private static final String update3 = " where id=";
	
	public static String getUpdate3() {
		return update3;
	}

	public static String getUpdate() {
		return update;
	}

	public static String getUpdate2() {
		return update2;
	}

	public static String getInsertpoints2() {
		return insertPoints2;
	}

	public static String getInsertevent1() {
		return insertEvent1;
	}

	public static String getInsertevent2() {
		return insertEvent2;
	}

	public static String getAfterlogin1() {
		return afterLogin1;
	}

	public static String getAfterlogin21() {
		return afterLogin2_1;
	}

	public static String getAfterlogin22() {
		return afterLogin2_2;
	}

	public static String getAfterlogin3Ins1() {
		return afterLogin3_ins1;
	}

	public static String getAfterlogin3Ins2() {
		return afterLogin3_ins2;
	}

	public static String getSelectpassword() {
		return selectPassword;
	}

	public static String getInsertpassword1() {
		return insertPassword1;
	}

	public static String getInsertpassword2() {
		return insertPassword2;
	}

	public static String getCheckpasswordquery() {
		return checkPasswordQuery;
	}
}
