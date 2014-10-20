package com.mobile.average;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AgregarMateria extends Activity {
	
	ProgressBar Loading;
	final static String URLdir = "http://augustodesarrollador.com/promedio_app/read.php";
	JSONObject result;
	TaskDownloadMateria task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregarmateria);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Loading = (ProgressBar)findViewById(R.id.LoadingBar);
		isNetworkAvailable();
		Initialize();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.ingreso, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (task.getStatus() == Status.RUNNING){
			Toast.makeText(this, "Cargando..", Toast.LENGTH_SHORT).show();
		} else {
			super.onBackPressed();
		}
	}
	
	private void Initialize()
	{
		try {
			task = new TaskDownloadMateria(this); 
			task.execute(new URL(URLdir));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Error();
		}
	}
	
	public void Error()
	{
		finish();
	}
	
	private void isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (!(networkInfo != null && networkInfo.isConnected())) {
			Toast.makeText(this, "Red no Disponible.", Toast.LENGTH_LONG)
			.show();
			Error();
		}
	}
}
