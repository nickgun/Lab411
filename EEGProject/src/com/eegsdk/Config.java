package com.eegsdk;

import com.eegproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Config extends Activity {
	private Button btnKillFile;
	private static final int REQUEST_KILL_FILE = 105;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_config);
		
		btnKillFile = (Button) findViewById(R.id.btnKillFile);

		btnKillFile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_KILL_FILE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_KILL_FILE: {
				String strPath = intent.getData().getPath();
				Process p;
				try {
					p = Runtime.getRuntime().exec("rm " + strPath);
					p.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				break;
			default:
				break;
			}
		}
	}
}
