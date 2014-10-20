package com.mobile.average;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class complexadapter extends BaseExpandableListAdapter{
	
	List<Materia> materias;
	List<List<Notas>> notas;
	LayoutInflater inflater;
	SQLiteDataBase db;
	
	public complexadapter(Context context, List<Materia> materias)
	{
		this.materias = materias; 
		notas = new ArrayList<List<Notas>>();
		inflater = LayoutInflater.from(context);
		db = new SQLiteDataBase(context);
		for (int i = 0; i < materias.size(); i++) {
			List<Notas> nota_each_mat = db.getFullNotesOf(materias.get(i).getId());
			notas.add(nota_each_mat);
		}	
		notifyDataSetChanged();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		// TODO Auto-generated method stub
		return notas.get(groupPosition).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosititon) {
		// TODO Auto-generated method stub
		return notas.get(groupPosition).get(childPosititon).ID;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			convertView = inflater.inflate(R.layout.complexlistnotas, parent, false);
			TextView name1 = (TextView)convertView.findViewById(R.id.name1);
			TextView percen1 = (TextView)convertView.findViewById(R.id.percen1);
			final EditText nota1 = (EditText)convertView.findViewById(R.id.nota1);
			final Notas nota = notas.get(groupPosition).get(childPosition);
			name1.setText(nota.getDescripcion());
			percen1.setText(nota.getPercentage()+"");
			if (nota.getNota()!=0.0f) {
				nota1.setText(round(nota.getNota(), 1)+"");
			}
			nota1.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String t = s.toString();
					if (t.equals("")) {
						nota.setNota(0.0f);
					} else {
						float data = round(Float.parseFloat(t),3);
						if (data>5.0f){
							nota.setNota(5.0f);
						} else {
							nota.setNota(data);
						}
					}
				}
			});

		return convertView;
	}

	public Materia getMateria(int Position)
	{
		return materias.get(Position);
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return notas.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return notas.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return notas.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return materias.get(groupPosition).getId();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			convertView = inflater.inflate(R.layout.complexlist, parent, false);
			TextView tx = (TextView)convertView.findViewById(R.id.MateriaName);
			tx.setText(materias.get(groupPosition).getName());
			tx = (TextView)convertView.findViewById(R.id.MateriaFinalNota);
			boolean completa=true;
			float notafinal=0.0f;
			for (int i = 0; i < notas.get(groupPosition).size(); i++) {
				Notas not = notas.get(groupPosition).get(i);
				if (not.getNota()==0.0f){
					completa = false; break;
				}
				notafinal += not.getNota()*not.getPercentage()/100;
			}
			if (completa)
				tx.setText(round(notafinal, 1)+"");
			else
				tx.setText("");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	public void SaveNotas()
	{
		SQLiteDatabase writable = db.getWritableDatabase();
		for (int i = 0; i < notas.size(); i++) {
			for (int j = 0; j < notas.get(i).size(); j++) {
				ContentValues values = new ContentValues();
				Notas not = notas.get(i).get(j);
				values.put(SQLiteDataBase.col_not_id, not.ID);
				values.put(SQLiteDataBase.col_not_mat_id, materias.get(i).getId());
				values.put(SQLiteDataBase.col_not_name, not.getDescripcion());
				values.put(SQLiteDataBase.col_not_percentage, not.getPercentage());
				values.put(SQLiteDataBase.col_not_nota, not.getNota());
				writable.update(SQLiteDataBase.TABLA_NOTAS, values, SQLiteDataBase.col_not_id+"="+not.ID, null);	
			}
		}
	}

	public float CalcularPromedioConActual(float promedioActual, int creditosActual) {
		// TODO Auto-generated method stub
		boolean completa=true;
		float notafinaltotal=promedioActual*creditosActual;
		for (int i = 0; i < notas.size(); i++) {
			
			float notafinalmateria=0.0f;
			for (int j = 0; j < notas.get(i).size(); j++) {
				Notas not = notas.get(i).get(j);
				if (not.getNota()==0.0f){
					completa = false; break;
				}
				notafinalmateria += not.getNota()*not.getPercentage()/100;
			}
			notafinaltotal += materias.get(i).getCreditos()*notafinalmateria;
			if (!completa)
				break;
		}
		if (completa){
			return notafinaltotal/(creditosActual+getAllCreditos());
		} else
			return -1.0f;
	}

	public int getAllCreditos() {
		// TODO Auto-generated method stub
		int mat=0;
		for (int i = 0; i < materias.size(); i++) {
			mat+=materias.get(i).getCreditos();
		}
		return mat;
	}
}
