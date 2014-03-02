package com.example.hackv2;

//import com.mirasense.demos.ScanditSDKSampleBarcodeActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class MainActivity extends Activity implements ScanditSDKListener {

	// The main object for scanning barcodes.
	private ScanditSDKBarcodePicker mBarcodePicker;
	public static final int BARCODE_REQUEST = 1001;
	public static final String TAG = "Hackv2: MainActivity: ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 14) {
			super.setTheme(android.R.style.Theme_DeviceDefault);
		} else if (Build.VERSION.SDK_INT >= 11) {
			super.setTheme(android.R.style.Theme_Holo);
		} else {
			super.setTheme(android.R.style.Theme_Black_NoTitleBar);
		}

		setContentView(R.layout.activity_main);

		Button startScanner = (Button) findViewById(R.id.bScanner);
		startScanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						BarcodeActivity.class));
			}
		});
		
		Button login = (Button) findViewById(R.id.bLogin);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						LoginActivity.class));
			}
		});
	}

	@Override
	protected void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		if (mBarcodePicker != null) {
			mBarcodePicker.stopScanning();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		if (mBarcodePicker != null) {
			mBarcodePicker.startScanning();
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.action_settings){
			logout();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
    private void logout() {
    	SharedPreferences prefs = getSharedPreferences(HttpRequestClient.PREFERENCE_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(HttpRequestClient.INPUT_USERNAME, "");
		editor.putString(HttpRequestClient.INPUT_PASSWORD, "");
		editor.commit();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void didCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void didManualSearch(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didScanBarcode(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}
}
