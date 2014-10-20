package com.mobile.average;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Operation extends Activity {
	
	private SQLiteDataBase db;
	private float promedio;
	private int creditos;
	private String dataname;
	private complexadapter adapter;
	private PromedioAndCreditos PAC;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        PAC = new PromedioAndCreditos(this);
        PAC.PACReadData();
        db = new SQLiteDataBase(this);
        Bundle bn = getIntent().getExtras();
        promedio = PAC.PACgetPromedio();
        creditos = PAC.PACgetCreditos();
        dataname = bn.getString("dataname");
        
        final Button Calcular, Guardar;
        Calcular = (Button)findViewById(R.id.BTCalcular);
        Guardar = (Button)findViewById(R.id.BTGuardar);
        Calcular.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
				Calcular(Float.parseFloat(((EditText)findViewById(R.id.ETAcumulado)).getText().toString()));
				} catch (Exception ex) {
					Toast.makeText(Calcular.getContext(), "Introduzca Promedio Deseado", Toast.LENGTH_SHORT).show();
				}
			}

		});
        
        Calcular.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					Calcular.setBackgroundResource(R.color.RareGreen);
					return false;
				}
				if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					Calcular.setBackgroundResource(android.R.color.holo_red_light);
					return false;
				}
				return false;
			}
		});
        Guardar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					Guardar.setBackgroundResource(R.color.RareGreen);
					return false;
				}
				if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					Guardar.setBackgroundResource(android.R.color.holo_red_light);
					return false;
				}
				return false;
			}
		});
        Guardar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveNotas();
			}
		});
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getAllMaterias(dataname);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ExpandableContainer);
		final ExpandableListView list = (ExpandableListView) ll.findViewById(R.id.expandablelist);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				// TODO Auto-generated method stub
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
	                // Your code with group long click 
					final AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
					 alertDialog.setTitle("Aviso");
					 alertDialog.setMessage("¿Desea Eliminar la Materia?");
					 alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {

					  public void onClick(DialogInterface arg0, int arg1) {
					  // do something when the OK button is clicked
						  db.deleteMateria(adapter.materias.get(pos));
							db.deleteNotas(adapter.notas.get(pos));
							adapter.materias.remove(pos);
							adapter.notas.remove(pos);
							list.invalidateViews();
					  }});
					 alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
					       
					  public void onClick(DialogInterface arg0, int arg1) {
					  // do something when the Cancel button is clicked
					  }});
					 alertDialog.show();
	                return true;
	            }

	            return false;
			}
		});
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		super.onResume();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operation_menu, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
		if (item.getItemId() == R.id.newmateria) {
			Intent in = new Intent(this, AgregarMateria.class);
			in.putExtra("dataname", dataname);
			startActivity(in);
			return true;
		}
		if (item.getItemId() == R.id.info_action_bar) {
			Toast.makeText(this, "Promedio Actual: "+complexadapter.round(promedio, 4)+"\n"+"Creditos cursados: "+creditos, Toast.LENGTH_LONG).show();
			return true;
		}
		if (item.getItemId() == R.id.AddPromedio) {
			 final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			 alertDialog.setTitle("Aviso");
			 alertDialog.setMessage("¿Desea Computar con el promedio Actual?");
			 alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

			  public void onClick(DialogInterface arg0, int arg1) {
			  // do something when the OK button is clicked
				  float total = adapter.CalcularPromedioConActual(promedio,creditos);
				  if (total!=-1.0f){
					  PAC.PACWriteData(total, creditos+adapter.getAllCreditos());
					  Toast.makeText(alertDialog.getContext(), "Materias agregadas correctamente", Toast.LENGTH_SHORT).show();
				  } else {
					  Toast.makeText(alertDialog.getContext(), "Existen Notas Incompletas", Toast.LENGTH_SHORT).show();
				  }
				  promedio = total;
				  creditos = creditos+adapter.getAllCreditos();
			  }});
			 alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			       
			  public void onClick(DialogInterface arg0, int arg1) {
			  // do something when the Cancel button is clicked
			  }});
			 alertDialog.show();
			return true;
		}
        return super.onOptionsItemSelected(item);
    }
	
	public void getAllMaterias(String dataname) {
		List<Materia> materias = new ArrayList<Materia>();
        String selectQuery = "SELECT  TM."+SQLiteDataBase.col_mat_id + " , TM."+SQLiteDataBase.col_mat_name+" , TM."+SQLiteDataBase.col_mat_cred+" FROM " + 
        SQLiteDataBase.TABLA_MATERIAS+" AS TM , "+SQLiteDataBase.TABLA_REGISTRO + 
        " AS TR WHERE '"+dataname+"' = TR."+SQLiteDataBase.col_reg_name+" AND TR."+ SQLiteDataBase.col_reg_id+" = TM."+SQLiteDataBase.col_mat_reg_id;
      
        SQLiteDatabase dbreadable = db.getReadableDatabase();
        Cursor cursor = dbreadable.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Materia mat = new Materia(cursor.getString(1), cursor.getInt(0), cursor.getInt(2));
                materias.add(mat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (materias.isEmpty())
        {
        	Toast.makeText(this, "No hay materias escritas. ", Toast.LENGTH_SHORT).show();
        } else {
        	adapter = new complexadapter(this, materias);
        	ExpandableListView list = (ExpandableListView)findViewById(R.id.expandablelist);
        	list.setAdapter(adapter);
        	
        }
        dbreadable.close();
	}
	
	@SuppressWarnings("unchecked")
	public void Calcular(float promDeseado) {
		// TODO Auto-generated method stub
		float m, sumaNotasMateriasCompl=0.0f;
		int creditosTotal=0, creditosIncompl=0;
		List<Integer> IndicesMatIncomp = new ArrayList<Integer>();
		if (adapter!=null){
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			float notaTotal=0.0f;
			List<Notas> notasMateria = (List<Notas>)adapter.getGroup(i);
			for (int j = 0; j < notasMateria.size(); j++) {
				Notas nota = notasMateria.get(j);
				if (nota.getNota()==0.0f) {
					notaTotal=-1.0f; break;
				} else {
					notaTotal += nota.getNota() * nota.getPercentage() / 100.0f;
				}
			}
			int cr = adapter.getMateria(i).getCreditos();
			if (notaTotal > 0) {
				sumaNotasMateriasCompl += cr * notaTotal;
			} else {
				IndicesMatIncomp.add(i);
				creditosIncompl += cr;
			}
			creditosTotal += cr;
		}
		m = promDeseado * (creditos + creditosTotal) - creditos*promedio - sumaNotasMateriasCompl;
		m /= creditosIncompl;
		if (m<0.0f) {
			m = 0.0f;
		}
		if (m<5.0f){
			for (int i = 0; i < IndicesMatIncomp.size(); i++) {
				List<Notas> notas = (List<Notas>)adapter.getGroup(IndicesMatIncomp.get(i));
				float NeededNota=m;
				float Percentage=0.0f;
				for (int j = 0; j < notas.size(); j++) {
					Notas n = notas.get(j);
					if (n.getNota() == 0.0f) {
					Percentage += n.getPercentage();
					} else {
					NeededNota -= n.getNota() * n.getPercentage() / 100;
					}
				}
				NeededNota = NeededNota / (Percentage / 100);
				if (NeededNota<0.0f){
					NeededNota = 0.1f;
				}
				if (NeededNota>5.0f){
					Toast.makeText(this, "No es posible obtener este promedio. La máxima nota no es suficiente", Toast.LENGTH_SHORT).show();
					break;
				}
				for (int j = 0; j < notas.size(); j++) {
					Notas n = notas.get(j);
					if (n.getNota()==0.0f){
						n.setNota(NeededNota);
					}
				}
			}
		} else {
			Toast.makeText(this, "No es posible obtener este promedio o ya están todas las notas.", Toast.LENGTH_SHORT).show();
		}
		LinearLayout ll = (LinearLayout) findViewById(R.id.ExpandableContainer);
		final ExpandableListView list = (ExpandableListView) ll.findViewById(R.id.expandablelist);
		list.invalidateViews();
		} else {
			Toast.makeText(this, "No hay materias.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void SaveNotas()
	{
		if (adapter != null) {
			adapter.SaveNotas();
			Toast.makeText(this, "Guardado Correctamente.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "No hay materias.", Toast.LENGTH_SHORT).show();
		}
	}
}
