package com.eegsdk;

import com.eegproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class ICA extends Activity {
	TabHost th_sdk_ica;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_ica);
		
		th_sdk_ica = (TabHost) findViewById(R.id.tabhost_sdk_ica);
		th_sdk_ica.setup();
		
		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("first")
				.setIndicator("ICA PROS").setContent(R.id.tabICApros));
		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("second")
				.setIndicator("ICA INVERSE").setContent(R.id.tabICAinverse));
	}

}
