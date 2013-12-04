package com.eegproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tvCompData;
	Button btnChooseEdfFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tvCompData = (TextView)findViewById(R.id.tvCompData);
		btnChooseEdfFile = (Button)findViewById(R.id.btnChooseEdfFile);
		
		btnChooseEdfFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//choose file edf
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, 1);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				String path = data.getData().getPath();
				Log.e("TAG", "\nPATH= "+path);
				long start_time = System.currentTimeMillis();
				int i = runProcessData();
				long stop_time = System.currentTimeMillis();
				long time = (stop_time - start_time)/1000;
				Log.e("TAG", "\nSignal: " + i + " sample");
				tvCompData.setText("Thoi gian thu tin hieu: " + (i/128) + "s\n");
				tvCompData.append("Thoi gian xu ly tin hieu: "+ time +"s");
			}
		}
	}
	
	private void ReadEdfFile(String path){
		File fl = new File(path);
		FileInputStream ips = null;
		Scanner input = null;
		if (fl.exists()) {
			try {
				ips = new FileInputStream(fl);
				input = new Scanner(ips);
				{
					while (input.hasNextLine()) {
						String line = input.nextLine();
						String[] arrStr = line.split("\\$");
						
					}
				}
				ips = null;
				input = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}
	
	public native int runProcessData();

	static{
		System.loadLibrary("ProcessData");
	}
}
