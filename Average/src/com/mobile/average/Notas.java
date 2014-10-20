package com.mobile.average;

public class Notas {
	
	private String Descripcion;
	private float Nota;
	private float percentage;
	public int ID;
	
	public Notas (String desc, float nota)
	{
		this.Descripcion = desc;
		this.Nota = nota;
		percentage = 0.0f;
	}
	public Notas (String desc, float percentage, float nota)
	{
		this.Descripcion = desc;
		this.Nota = nota;
		this.percentage = percentage;
	}
	public String getDescripcion() {
		return Descripcion;
	}
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	public float getNota() {
		return Nota;
	}
	public void setNota(float nota) {
		Nota = nota;
	}	
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
}
