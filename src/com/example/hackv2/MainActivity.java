package com.example.hackv2;

//import com.mirasense.demos.ScanditSDKSampleBarcodeActivity;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements ScanditSDKListener {

	// The main object for scanning barcodes.
	private ScanditSDKBarcodePicker mBarcodePicker;

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
