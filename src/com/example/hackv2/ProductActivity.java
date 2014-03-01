package com.example.hackv2;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ProductActivity extends Activity {

	HttpRequestClient mClient;
	ProgressDialog dialog;
	TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		String barcode = getIntent().getStringExtra(BarcodeActivity.BARCODE_KEY);
		
		mClient = new HttpRequestClient(this);
		dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		titleText = (TextView) findViewById(R.id.titleText);
		setTitle(barcode);
		
	}
	
	public void setTitle(String barcode) {
		HttpGet lookup = new HttpGet("/lookup/"+barcode);
		JSONObject json = mClient.execute(lookup);
		try {
			if(json.getBoolean("res")) {
				titleText.setText(json.getJSONObject("data").getString("product"));
			}
			else {
				titleText.setText(json.getJSONObject("err").getString("msg"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
		return true;
	}

}
