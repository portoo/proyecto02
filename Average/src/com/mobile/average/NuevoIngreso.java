package com.mobile.average;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuevoIngreso extends Fragment {
	
	private PromedioAndCreditos pac;
	private EditText txcreditos;
	private EditText txpromedio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	pac = new PromedioAndCreditos(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_ingreso, container, false);
        txcreditos = (EditText)rootView.findViewById(R.id.TXcreditos);
        txpromedio = (EditText)rootView.findViewById(R.id.TXpromedio);
        Button b = (Button) rootView.findViewById(R.id.BTSiguiente);
        b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				next(v);
			}
		});
        return rootView;
    }
    
    public void next(View v)
    {
    	int creditos=0; float promedio=-1.0f; boolean good=true;
    	try {
    		creditos = Integer.parseInt(txcreditos.getText().toString());        	
    	} catch (NumberFormatException e) {
    		Toast.makeText(getActivity(), "Numero de créditos erróneo.", Toast.LENGTH_SHORT).show();; good=false;
    	}
    	try {
    		promedio = Float.parseFloat(txpromedio.getText().toString());        	
        } catch (NumberFormatException e) {
        	Toast.makeText(getActivity(), "Promedio erróneo.", Toast.LENGTH_SHORT).show();; good=false;
        }
    	if (promedio < 0.0f && promedio >5.0f) {
    		Toast.makeText(getActivity(), "Promedio erróneo.", Toast.LENGTH_SHORT).show();; good=false;
    	}
    	if (good){
    		pac.PACWriteData(promedio, creditos);
    		FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		transaction.replace(R.id.container, new creation());
    		transaction.commit();
    	}
    }
}
