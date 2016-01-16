package com.example.pum2016.klasy;

public final class TableOptionsObject {
	private String prefix;
	private int id_trasy;
	
	private static volatile TableOptionsObject instance = null;
	
	public static TableOptionsObject getInstance(String prefix, int id) {
		if(instance==null) {
			synchronized (TableOptionsObject.class) {
				if(instance==null) {
					instance = new TableOptionsObject(prefix,id);
				}
			}
		}
		return instance;
	}
	public static TableOptionsObject getInstance() {
		if(instance==null) {
			synchronized (TableOptionsObject.class) {
				if(instance==null) {
					instance = new TableOptionsObject();
				}
			}
		}
		return instance;
	}
	
	private TableOptionsObject() {}
	private TableOptionsObject(String p, int i) {
		this.prefix = p;
		this.id_trasy = i;
	}
	
	public String getPrefix() {
		return instance.prefix;
	}
	public int getId_trasy() {
		return instance.id_trasy;
	}
}
