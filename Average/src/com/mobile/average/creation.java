package com.mobile.average;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class creation extends Fragment {
	
	SQLiteDataBase db;
	private String entrada="";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_creation, container, false);
		final Activity esto = getActivity();
        db = new SQLiteDataBase(esto);
        ListView lista = (ListView) rootView.findViewById(R.id.listaDeDatos);
        lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> Parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				TextView tv = ((TextView)view);
				tv.setBackgroundResource(android.R.color.darker_gray);
				Intent intent = new Intent(esto, Operation.class);
        		intent.putExtra("dataname", tv.getText());
        		startActivity(intent);
			}
		});
        Button bt = (Button)rootView.findViewById(R.id.AgregarNuevo);
        bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Add();
			}
		});
        return rootView;
    }
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		ListView lista = (ListView) this.getView().findViewById(R.id.listaDeDatos);
        List<String> names = db.getAllNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simplelist, names);
        lista.setAdapter(adapter);
        
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		db.close();
		super.onDestroy();
	}
	
	public void Add()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Digitar Nombre");
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        entrada = input.getText().toString();
		        if (!entrada.equals(""))
				{
					SQLiteDatabase dbWritable = db.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put(SQLiteDataBase.col_reg_name, entrada);
					dbWritable.insert(SQLiteDataBase.TABLA_REGISTRO, null, values);
					dbWritable.close();
					Intent intent = new Intent(builder.getContext(), Operation.class);
	        		intent.putExtra("dataname", entrada);
	        		startActivity(intent);
				}
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	entrada="";
		        dialog.cancel();
		    }
		});
		builder.show();
	}
}
