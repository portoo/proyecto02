package com.mobile.average;

public class Materia {
	
	private String Name;
	private int Id;
	private int Creditos;
	
	
	
	public Materia(String name, int id, int creditos) {
		Name = name;
		Id = id;
		Creditos = creditos;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getCreditos() {
		return Creditos;
	}
	public void setCreditos(int creditos) {
		Creditos = creditos;
	}

}
