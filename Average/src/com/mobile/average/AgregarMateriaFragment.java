package com.mobile.average;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AgregarMateriaFragment extends Fragment
{
	private ExpandableListView expandable;
	ArrayList<MateriaEX> materias;
	ArrayList<MateriaEX> materiasVistas;
	String dataname;
	SQLiteDataBase db;
	JSONObject materiaObj;
	
	public void SetMaterias(JSONObject materiaObj)
	{
		this.materiaObj = materiaObj;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agregarmateria, container, false);
        expandable = (ExpandableListView) rootView.findViewById(R.id.expandableMateria);
        Button bt = (Button)rootView.findViewById(R.id.BTSearch);
        bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Search();
			}
		});
        inflater = LayoutInflater.from(getActivity());
		materias = new ArrayList<MateriaEX>();
		materiasVistas = new ArrayList<MateriaEX>();
		db = new SQLiteDataBase(getActivity());
		Bundle bl = getActivity().getIntent().getExtras();
		dataname = bl.getString("dataname");
		try {
			JSONArray ArrayMat = materiaObj.getJSONArray("materias");
			for (int i = 0; i < ArrayMat.length(); i++) {
				JSONObject mat = ArrayMat.getJSONObject(i);
				MateriaEX matEX = new MateriaEX(mat.getString("nombre_materia"), mat.getString("periodo"));
				JSONArray nots = mat.getJSONArray("componetes");
				for (int j = 0; j < nots.length(); j++) {
					JSONObject nota = nots.getJSONObject(j);
					matEX.notas.add(new NotaEX(nota.getString("desc"), nota.getString("peso")));
				}
				materias.add(matEX);
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getActivity(), "Json Corrupto. "+e.getMessage(), Toast.LENGTH_LONG)
			.show();
		}
		expandable.setAdapter(new ExpandableMateriaList());
        return rootView;
    }
	
	public void Search()
	{
		EditText texto = (EditText)getView().findViewById(R.id.ETinfo);
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);
		String text = texto.getText().toString();
		if (text!=null)
		{
			materiasVistas.clear();
			for (int i = 0; i < materias.size(); i++) {
				MateriaEX mat = materias.get(i);
				if (text.length() > mat.name.length())
					continue;
				else
				{
					String compare = mat.name.substring(0, text.length());
					if (text.equals(compare)){
						materiasVistas.add(mat);
					}
				}
			}
			expandable.invalidateViews();
		}
	}
	
	private class MateriaEX
	{
		public String name;
		public String periodo;
		public ArrayList<NotaEX> notas;
		public MateriaEX(String name, String periodo) {
			super();
			this.name = name;
			this.periodo = periodo;
			notas = new ArrayList<NotaEX>();
		}		
	}
	
	private class NotaEX
	{
		public String name;
		public String Percentage;
		public NotaEX(String name, String percentage) {
			this.name = name;
			Percentage = percentage;
		}
	}
	public void AddMateria(int groupPosition) {
		// TODO Auto-generated method stub
		final int index = groupPosition;
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View promptView = inflater.inflate(R.layout.creditosdialog, null, false);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView.findViewById(R.id.inputcredit);
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						int cred = Integer.parseInt(editText.getText().toString());
						db.AddMateria(dataname, materiasVistas.get(index).name, cred);
						String notaname[]; float notaperc[];
						notaname = new String[materiasVistas.get(index).notas.size()];
						notaperc = new float[materiasVistas.get(index).notas.size()];
						for (int i = 0; i < materiasVistas.get(index).notas.size(); i++) {
							notaname[i] = materiasVistas.get(index).notas.get(i).name;
							notaperc[i] = Float.parseFloat(materiasVistas.get(index).notas.get(i).Percentage);
						}
						db.AddNotas(dataname, materiasVistas.get(index).name, notaname, notaperc);
						getActivity().finish();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	private class ExpandableMateriaList extends BaseExpandableListAdapter {
		
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				convertView = inflater.inflate(R.layout.materiaexpandablelist, null);
				TextView tx = (TextView) convertView.findViewById(R.id.ListMatName);
				tx.setText(materiasVistas.get(groupPosition).name+ " "+ materiasVistas.get(groupPosition).periodo);
				ImageButton img = (ImageButton) convertView.findViewById(R.id.imageButton1);
				final int index = groupPosition;
				img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AddMateria(index);
					}
					
				});
				img.setFocusable(false);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				convertView = inflater.inflate(R.layout.materiaexpandableitem, null);
				TextView left = (TextView)convertView.findViewById(R.id.notaNameitem);
				TextView right = (TextView)convertView.findViewById(R.id.notaPercenItem);
				left.setText(materiasVistas.get(groupPosition).notas.get(childPosition).name);
				right.setText(materiasVistas.get(groupPosition).notas.get(childPosition).Percentage+"");
			return convertView;
		}
		
		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return materiasVistas.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return materiasVistas.get(groupPosition).notas.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return materiasVistas.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return materiasVistas.get(groupPosition).notas.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return materiasVistas.get(groupPosition).hashCode();
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return materiasVistas.get(groupPosition).notas.get(childPosition).hashCode();
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
		
	}
}
