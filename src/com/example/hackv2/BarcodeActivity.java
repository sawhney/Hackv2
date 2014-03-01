package com.example.hackv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//import com.mirasense.demos.SettingsActivity;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class BarcodeActivity extends Activity implements ScanditSDKListener {

	public static final String BARCODE_KEY = "barcode key";
	// The main object for recognizing a displaying barcodes.
	private ScanditSDK mBarcodePicker;
	
	// Scandit APK goes here
	private static String sScanditSdkAppKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sScanditSdkAppKey = getString(R.string.scandit_key);
		initializeAndStartBarcodeScanning();
	}

	@Override
	protected void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		mBarcodePicker.stopScanning();

		super.onPause();
	}

	@Override
	public void onBackPressed() {
		mBarcodePicker.stopScanning();
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	protected void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		mBarcodePicker.startScanning();
		super.onResume();
	}

	@Override
	public void didCancel() {
		// TODO Auto-generated method stub
		mBarcodePicker.stopScanning();

	}

	@Override
	public void didManualSearch(String entry) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "User entered: " + entry, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void didScanBarcode(String barcode, String symbology) {
		// Remove non-relevant characters that might be displayed as rectangles
		// on some devices. Be aware that you normally do not need to do this.
		// Only special GS1 code formats contain such characters.
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

//		Toast.makeText(this, symbology + ": " + cleanedBarcode,
//				Toast.LENGTH_LONG).show();
		mBarcodePicker.stopScanning();
		Intent result = new Intent(BarcodeActivity.this, ProductActivity.class);
		result.putExtra(BARCODE_KEY, cleanedBarcode);
		startActivity(result);
		finish();
		// Example code that would typically be used in a real-world app using
		// the Scandit SDK.
		/*
		 * // Access the image in which the bar code has been recognized. byte[]
		 * imageDataNV21Encoded =
		 * barcodePicker.getCameraPreviewImageOfFirstBarcodeRecognition(); int
		 * imageWidth = barcodePicker.getCameraPreviewImageWidth(); int
		 * imageHeight = barcodePicker.getCameraPreviewImageHeight();
		 * 
		 * // Stop recognition to save resources. mBarcodePicker.stopScanning();
		 */
	}
	
	private void initializeAndStartBarcodeScanning() {
		// Switch to full screen.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// We instantiate the automatically adjusting barcode picker that will
		// choose the correct picker to instantiate. Be aware that this picker
		// should only be instantiated if the picker is shown full screen as the
		// legacy picker will rotate the orientation and not properly work in
		// non-fullscreen.
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				this, sScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		// Add both views to activity, with the scan GUI on top.
		setContentView(picker);
		mBarcodePicker = picker;

		// Register listener, in order to be notified about relevant events
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);

		// Set all settings according to the settings activity. Normally there
		// will be no settings
		// activity for the picker and you just hardcode the setting your app
		// needs.
//		SettingsActivity.setSettingsForPicker(this, mBarcodePicker);
	}
}
