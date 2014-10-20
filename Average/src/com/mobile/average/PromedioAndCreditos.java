package com.mobile.average;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;

public class PromedioAndCreditos {
	
	private float Promedio;
	private int Creditos;
	private static final String FILENAME="inneruser.txt";
	private Context C;
	
	public PromedioAndCreditos(Context c) {
		this.C=c;
	}
	
	public float PACgetPromedio(){
		return Promedio;
	}
	public int PACgetCreditos(){
		return Creditos;
	}
	
	public boolean PACReadData()
	{
		InputStream InSt = null;
		try {
			InSt = C.openFileInput(FILENAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		InputStreamReader InSrRe = new InputStreamReader(InSt);
		BufferedReader BuffRead = new BufferedReader(InSrRe);
		String line;
		try {
		line =  BuffRead.readLine();
		Promedio = Float.parseFloat(line);
		line = BuffRead.readLine();
		Creditos = Integer.parseInt(line);
		InSt.close();
		return true;
		} catch (Exception ex) {
			try {
				InSt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public boolean PACWriteData(float promedio, int creditos) {
		String linea = System.getProperty("line.separator");
		try {
			OutputStreamWriter writer = new OutputStreamWriter(C.openFileOutput(FILENAME, Context.MODE_PRIVATE));
			writer.write(promedio+linea);
			writer.write(creditos+"");
			writer.close();
			return true;
		} catch (Exception ex){
			return false;
		}
	}
	
}
