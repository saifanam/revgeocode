package com.saif.revgeocode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class LatLong extends Activity {

	private TextView revGeo;
	private EditText lat = null, lon = null;
	private String strLat = null, strLon = null;
	private Button goButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lat_long);

		revGeo = (TextView) findViewById(R.id.textView3);

		goButton = (Button) findViewById(R.id.button1);
		goButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					
					lat = (EditText) findViewById(R.id.editText1);
					strLat = lat.getText().toString().trim();

					lon = (EditText) findViewById(R.id.editText2);
					strLon = lon.getText().toString().trim();

					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ strLat + "," + strLon + "&sensor=false";
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj
							.openConnection();
					con.setRequestMethod("GET");
					BufferedReader in = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					JsonElement jelement = new JsonParser().parse(response
							.toString());
					String address = jelement.getAsJsonObject().get("results")
							.getAsJsonArray().get(0).getAsJsonObject()
							.get("formatted_address").toString();

					revGeo.setText(address);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					revGeo.setText("Couldn't find address");
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lat_long, menu);
		return true;
	}

}
