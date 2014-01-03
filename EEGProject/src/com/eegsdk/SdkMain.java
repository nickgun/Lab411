package com.eegsdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.eegproject.R;

public class SdkMain extends ActivityGroup {
	private TabHost th_sdk_main, th_sdk_ica;
	public static TextView tvStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sdk_main);

		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvStatus.setMovementMethod(new ScrollingMovementMethod());
		
		th_sdk_main = (TabHost) findViewById(R.id.tabhost);
		th_sdk_main.setup(this.getLocalActivityManager());
		tab_init();
	}

	private void tab_init() {
		th_sdk_main.addTab(th_sdk_main.newTabSpec("first")
				.setIndicator("CONFIG")
				.setContent(new Intent(this, Config.class)));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("second")
				.setIndicator("READEDF")
				.setContent(new Intent(this, ReadEdf.class)));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("third")
				.setIndicator("WAVELET")
				.setContent(new Intent(this, Wavelet.class)));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("fourth").setIndicator("ICA")
				.setContent(new Intent(this, ICA.class)));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("fiveth")
				.setIndicator("DETECT GAZEEYE")
				.setContent(new Intent(this, DetectEye.class)));
	}
}
