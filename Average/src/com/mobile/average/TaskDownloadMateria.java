package com.mobile.average;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

public class TaskDownloadMateria extends AsyncTask<URL, Void, JSONObject>{

	private AgregarMateria c;
	public AgregarMateriaFragment fragment;
	public TaskDownloadMateria(AgregarMateria c)
	{
		this.c=c;
	}
	
	@Override
	protected JSONObject doInBackground(URL... params) {
		// TODO Auto-generated method stub
		
		File dir = c.getCacheDir();
		File file = new File(dir, "jsoncache.txt");
		if (file.exists()){
			try {
				FileInputStream inFile = new FileInputStream(file);
				byte[] data = new byte[(int)file.length()];
				inFile.read(data);
				inFile.close();
				String stringdata = new String(data);
				JSONObject jsondata = new JSONObject(stringdata);
				return jsondata;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(c, "Error caché Interno", Toast.LENGTH_SHORT).show();
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			} catch(JSONException e3) {
				e3.printStackTrace();
				return null;
			}
		} else {
			int responseCode = -1;
			try {
				URL url = params[0];
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				responseCode = con.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					Toast.makeText(c, "Code " + responseCode+": "+con.getResponseMessage(), Toast.LENGTH_SHORT).show();
					return null;
				} else {
					BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
					String responseData = reader.readLine();
					System.out.println(responseData);
					JSONObject result = new JSONObject(responseData);
					FileOutputStream str = new FileOutputStream(file, false);
					str.write(responseData.getBytes());
					str.flush();
					str.close();
					reader.close();
					con.disconnect();
					return result;
				}
			
			} catch(IOException e2) {
				e2.printStackTrace();
				return null;
			} catch(JSONException e3) {
				e3.printStackTrace();
				return null;
			}
		}
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		if (result!=null) {
			final JSONObject resu = result;
			new Handler().post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					AgregarMateriaFragment fr = new AgregarMateriaFragment();
					fragment = fr;
					fr.SetMaterias(resu);
					c.Loading.setVisibility(View.INVISIBLE);
					c.getFragmentManager().beginTransaction()
					.add(R.id.container_creation, fr)
					.commit();
				}
			});
		} else {
			c.Error();
		}
	}
	
}
