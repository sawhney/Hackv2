package com.example.hackv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpRequestClient {

	public static final String INPUT_USERNAME = "username";
	public static final String INPUT_PASSWORD = "password";
	public static final String PREFERENCE_NAME = "credentials";
	public static DefaultHttpClient mClient; 
	private HttpHost httpHost;
	private Context context;
	
	public HttpRequestClient(Context c) {
		HttpParams params = new BasicHttpParams();
	    params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		mClient = new DefaultHttpClient(params);
		httpHost = new HttpHost("nl.ks07.co.uk", 3000);
		context = c;
	}		

	public JSONObject execute(final HttpRequest req) {
		ExecutorService ex = Executors.newSingleThreadExecutor();

		Callable<JSONObject> callable = new Callable<JSONObject>() {
			
			@Override
			public JSONObject call() throws Exception {
				// TODO Auto-generated method stub
				JSONObject res = null;
				try {
				    HttpResponse response = mClient.execute(httpHost, req);
				    res = getJSON(response);				    
				    //Log.d("Http Response:",  mClient.getCookieStore().getCookies().toString());
				} catch (ClientProtocolException e) {
				    // writing exception to log
				    e.printStackTrace();
				} catch (IOException e) {
				    // writing exception to log
				    e.printStackTrace();
				}
				
				return res;
			}
		};
		
	    Future<JSONObject> future = ex.submit(callable);
	    JSONObject res = null;
		try {
			res = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ex.shutdown();
	    
	    return res;
	}
		
	public Boolean executeUpload(final HttpRequest req) {
		ExecutorService ex = Executors.newSingleThreadExecutor();

		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				Boolean res = false;
				try {
				    HttpResponse response = mClient.execute(httpHost, req);
				    JSONObject json = getJSON(response);
					Log.d("HttpRequest: ", json.toString());

//				    try {
////						res = json.getBoolean("res");
//						res = 0==json.getJSONObject("status").getInt("code");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				    //Log.d("Http Response:",  mClient.getCookieStore().getCookies().toString());
				} catch (ClientProtocolException e) {
				    // writing exception to log
				    e.printStackTrace();
				} catch (IOException e) {
				    // writing exception to log
				    e.printStackTrace();
				}
				
				return res;
			}
		};
		
	    Future<Boolean> future = ex.submit(callable);
	    Boolean b = false;
		try {
			b = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ex.shutdown();
	    
	    return b;
	}

	
	public Boolean login(String username, String password) {
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("username", username));
	    nameValuePair.add(new BasicNameValuePair("password", password));
	    
	    HttpPost httpLogin = new HttpPost("/login");
		
	    try {
		    httpLogin.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
		    e.printStackTrace();
		}
		
	    JSONObject res = execute(httpLogin);
	    Boolean b = false;
	    try {
			b = res.getBoolean("res");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return b;
	}
	
	public Boolean addReview(String id, float rating, String review) {
		SharedPreferences prefs = context.getSharedPreferences(HttpRequestClient.PREFERENCE_NAME, context.MODE_PRIVATE);
		String username = prefs.getString("username", null);
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
		nameValuePair.add(new BasicNameValuePair("username", username));
	    nameValuePair.add(new BasicNameValuePair("rating", ""+rating));
	    nameValuePair.add(new BasicNameValuePair("review", review));
	    
	    HttpPost postRev = new HttpPost("/review/"+id);
	    try {
			postRev.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    JSONObject res = execute(postRev);
	    Boolean b = false;
	    try {
			b = res.getBoolean("res");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return b;
	}
	
	public JSONObject getReviews(String id) {
		HttpGet getReview = new HttpGet("/reviews/"+id);
		JSONObject res = execute(getReview);
		System.out.println(res.toString());
		return res;
	}
	
	public int getRating(String id) {
		HttpGet getReview = new HttpGet("/score/"+id);
		JSONObject res = execute(getReview);
		System.out.println(res.toString());
		return 0;
	}
	
	public Boolean register(String name, String username, String password) {
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
		nameValuePair.add(new BasicNameValuePair("username", username));
	    nameValuePair.add(new BasicNameValuePair("password", password));
	    HttpPost httpRegister = new HttpPost("/register");
	    
	    try {
		    httpRegister.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
		    // writing error to Log
		    e.printStackTrace();
		}
	    
	    JSONObject res = execute(httpRegister);
	    Boolean b = false;
	    try {
			b = res.getBoolean("res");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return b;
	}

	
	private static JSONObject getJSON(HttpResponse response) {
		 
		JSONTokener tokener = null;
		JSONObject json = null;
		String s="";
		try {
			s = EntityUtils.toString(response.getEntity());
			System.out.println("JSON: : " + s);
			tokener = new JSONTokener(s);
			json = new JSONObject(tokener);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return json;
	}
}
