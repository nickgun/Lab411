package com.eegproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.detectevent.GazeEye;
import com.eegsdk.ReadEdf;
import com.eegsdk.SdkMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button btnSdkMain, btnRemoveEyeBlink, btnDetectGazeEye;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnSdkMain = (Button) findViewById(R.id.btnSdkMain);
		btnSdkMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), SdkMain.class));
			}
		});

		btnRemoveEyeBlink = (Button) findViewById(R.id.btnRemoveEyeblink);
		btnRemoveEyeBlink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		btnDetectGazeEye = (Button) findViewById(R.id.btnDetectGazeEye);
		btnDetectGazeEye.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), GazeEye.class));
			}
		});
	}

}
