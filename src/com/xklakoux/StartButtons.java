package com.xklakoux;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;



public class StartButtons extends Activity {

	public static final int SUBCAL = 0;
	public static final int DATABASE = 1;
	public static final int SENDCOM = 2;
	public static final int PING = 3;
	public static final int INFO = 4;
	public static final int SETTINGS = 5;
	
	ProgressDialog mProgressDialog;


	ImageButton sc;
	ImageButton info;
	ImageButton db;
	ImageButton ping;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Dont show title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.startbuttons);

		GridView gridview = (GridView) findViewById(R.id.gvGridView);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				switch (position) {
				case SUBCAL:
					startActivity(new Intent("com.xklakoux.SUBCAL"));
					break;
				case DATABASE:
			        new AlertDialog.Builder(StartButtons.this)
			        .setTitle("Get update")
			        .setMessage("Do you want to sync you DB?")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	// declare the dialog as a member field of your activity

			            	// instantiate it within the onCreate method
			            	mProgressDialog = new ProgressDialog(StartButtons.this);
			            	mProgressDialog.setMessage("Downloading update");
			            	mProgressDialog.setIndeterminate(false);
			            	mProgressDialog.setMax(100);
			            	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			            	DownloadFile downloadFile = new DownloadFile();
			            	downloadFile.execute("http://pluton.kt.agh.edu.pl/~astaniec/database.db");
			            }

			        })
			        .setNegativeButton("No", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startActivity(new Intent("com.xklakoux.FORM"));
						}
					})
			        .show();
					break;
				case SETTINGS:
					startActivity(new Intent("com.xklakoux.SETTINGS"));
					break;
				case PING:
					startActivity(new Intent("com.xklakoux.PING"));
					break;
				case INFO:
					startActivity(new Intent("com.xklakoux.INFO"));
					break;
				case SENDCOM:
					startActivity(new Intent("com.xklakoux.SENDCOM"));
					break;
				}
			}
		});
	}
	private class DownloadFile extends AsyncTask<String, Integer, String> {
	    @Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
	    protected String doInBackground(String... sUrl) {
	        try {
	            URL url = new URL(sUrl[0]);
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            Log.d("asd","asd");
	            // this will be useful so that you can show a typical 0-100% progress bar
	            int fileLength = connection.getContentLength();
	            Log.d("length",String.valueOf(fileLength));
	            // download the file
	            InputStream input = new BufferedInputStream(url.openStream());
	            OutputStream output = new FileOutputStream("/data/data/com.xklakoux/databases/subnetcalculator.db");

	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	            }

	            output.flush();
	            output.close();
	            input.close();
	        } catch (Exception e) {
	        	 e.printStackTrace();
	        }
	        return null;
	    }
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	    }
	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        mProgressDialog.setProgress(progress[0]);
	    }
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
	        mProgressDialog.dismiss();
			startActivity(new Intent("com.xklakoux.FORM"));
		}
	    
	}
};


