package com.example.hackv2;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends Activity implements
		OnRatingBarChangeListener, OnClickListener {

	HttpRequestClient mClient;
	ProgressDialog dialog;
	TextView titleText;
	RatingBar ratingBar;
	Button ratingButton, reviewButton;
	TextView ratingText;
	TextView scoreText;
	EditText reviewText;
	String barcode;
	JSONArray reviews;
	JSONObject score;
	String title;
	ListView reviewList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		barcode = getIntent()
				.getStringExtra(BarcodeActivity.BARCODE_KEY);
		mClient = new HttpRequestClient(this);
		dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		dialog.show();
		titleText = (TextView) findViewById(R.id.titleText);
		reviewText = (EditText) findViewById(R.id.etReview);
		ratingText = (TextView) findViewById(R.id.tvResult);
		scoreText = (TextView) findViewById(R.id.tvScore);
		ratingBar = ((RatingBar) findViewById(R.id.rbFood));
		ratingBar.setOnRatingBarChangeListener(this);
		reviewButton = (Button) findViewById(R.id.bWrite);
		reviewList = (ListView) findViewById(R.id.reviewList);
		setTitle(barcode);
		
		if(title!=null){
			titleText.setText(title);
		}
		if(score!=null) {
			try {
				scoreText.append(score.getString("average")+"out of "+score.getString("count"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TextAdapter mAdapter = new TextAdapter();
		reviewList.setAdapter(mAdapter);
		
		dialog.dismiss();
//		ratingButton = (Button) findViewById(R.id.bRating);
//		ratingButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				float rating = ratingBar.getRating();
//				Boolean b = mClient.addReview(barcode, rating, null);
//				if(b) {
//					Toast.makeText(ProductActivity.this, "Rating added successfully", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(ProductActivity.this, "Rating failed", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		reviewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float rating = ratingBar.getRating();
				String review = reviewText.getText().toString();
				reviewText.setText("");
				Boolean b = mClient.addReview(barcode, rating, review);
				if(b) {
					Toast.makeText(ProductActivity.this, "Review added successfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ProductActivity.this, "Review failed", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void setTitle(final String barcode) {
		
		
		HttpGet lookup = new HttpGet("/lookup/" + barcode);
		JSONObject json = mClient.execute(lookup);
		try {
			if (json!= null && json.getBoolean("res")) {
				title = json.getJSONObject("data").getString(
						"product");
			} else {
				title = json.getJSONObject("err").getString("msg");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpGet req = new HttpGet("/reviews/"+barcode);
		JSONObject o = mClient.execute(req);
		try {
			if(o.getBoolean("res")) {
				reviews = o.getJSONArray("reviews");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpGet req2 = new HttpGet("/score/"+barcode);
		JSONObject o2 = mClient.execute(req2);
		try {
			if(o2!= null && o2.getBoolean("res")) {
				score = (JSONObject) o2.getJSONArray("stats").get(0);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		Thread t1 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpGet req = new HttpGet("/reviews/"+barcode);
//				JSONObject o = new HttpRequestClient(ProductActivity.this).execute(req);
//				try {
//					if(o.getBoolean("res")) {
//						reviews = o.getJSONArray("reviews");
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		Thread t2 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpGet lookup = new HttpGet("/lookup/" + barcode);
//				JSONObject json = new HttpRequestClient(ProductActivity.this).execute(lookup);
//				try {
//					if (json!= null && json.getBoolean("res")) {
//						title = json.getJSONObject("data").getString(
//								"product");
//					} else {
//						title = json.getJSONObject("err").getString("msg");
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		Thread t3 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpGet req = new HttpGet("/score/"+barcode);
//				JSONObject o = new HttpRequestClient(ProductActivity.this).execute(req);
//				try {
//					if(o!= null && o.getBoolean("res")) {
//						score = (JSONObject) o.getJSONArray("stats").get(0);
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		t1.start();
//		t2.start();
//		t3.start();
//		try {
//			t1.join(10000);
//			t2.join(10000);
//			t3.join(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// titleText.setText("TESTING");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
		return true;
	}

	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromTouch) {
		final int numStars = ratingBar.getNumStars();
		ratingText.setText(rating + "/" + numStars);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public class TextAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public TextAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return reviews.length();
		}

		@Override
		public Object getItem(int i) {
			return i;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup container) {
			view = mInflater.inflate(R.layout.list_item, container, false);
			JSONObject array;
			String username=null;
			String rating=null;
			String review=null;
			try {
				array = reviews.getJSONObject(i);
				rating = array.getString("rating");
				username = array.getString("user");
				review = array.getString("review");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TextView u = (TextView)view.findViewById(R.id.username);
			TextView ra = (TextView)view.findViewById(R.id.rating);
			TextView re = (TextView)view.findViewById(R.id.review);
			u.setText(username);
			ra.setText(rating);
			re.setText(review);
			return view;
		}
		
	}

}
