package com.eegsdk;

import com.eegproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Wavelet extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_wavelet);

		wavelet_init();
		wavelet_even();
	}

	/**
	 * Wavelet Tab
	 */
	Button btnDWT, btnIDWT;
	EditText etSample_Wl, etLever_Wl;
	TextView tvBrownFile_Wl;

	// khai bao cac Button, EditText, ... trong giao dien Tab Wavelet
	private void wavelet_init() {
		btnDWT = (Button) findViewById(R.id.btnDWT);
		btnIDWT = (Button) findViewById(R.id.btnIDWT);
		etSample_Wl = (EditText) findViewById(R.id.etSample_Wl);
		etLever_Wl = (EditText) findViewById(R.id.etLever_Wl);
		tvBrownFile_Wl = (TextView) findViewById(R.id.tvBrownFile_Wl);
	}

	// xay dung cac su kien click button trong giao dien Tab Wavelet
	private void wavelet_even() {
		tvBrownFile_Wl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_DATA_FILE_WL);
			}
		});

		btnDWT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strSample = etSample_Wl.getText().toString();
				if (tvBrownFile_Wl.getText().toString().trim().length() > 0) {
					if (strSample.trim().length() > 0) {
						WaveletTransform(tvBrownFile_Wl.getText().toString(),
								Integer.parseInt(strSample));
						SdkMain.tvStatus
								.append("\n- Wavelet Transform Complete, Output File:");
						SdkMain.tvStatus.append("\n" + tvBrownFile_Wl.getText()
								+ "_DWT\n");
					} else
						SdkMain.tvStatus.append("\nEnter Data!\n");
				} else
					SdkMain.tvStatus.append("\nChoose Data File!");

				SdkMain.tvStatus.scrollTo(
						0,
						SdkMain.tvStatus.getLineCount()
								* SdkMain.tvStatus.getLineHeight()
								+ SdkMain.tvStatus.getTop()
								- SdkMain.tvStatus.getBottom());
			}
		});

		btnIDWT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strSample = etSample_Wl.getText().toString();
				String strLever = etLever_Wl.getText().toString();
				if (tvBrownFile_Wl.getText().toString().trim().length() > 0) {
					if ((strSample.trim().length() > 0)
							&& (strLever.trim().length() > 0)) {
						WaveletTransformInverse(tvBrownFile_Wl.getText()
								.toString(), Integer.parseInt(strSample),
								Integer.parseInt(strLever));
						SdkMain.tvStatus
								.append("\n- Wavelet Transform Inverse Complete, Output File:");
						SdkMain.tvStatus.append("\n" + tvBrownFile_Wl.getText()
								+ "_IDWT\n");
					} else
						SdkMain.tvStatus.append("\nEnter Data!\n");
				} else
					SdkMain.tvStatus.append("\nChoose Data File!");

				SdkMain.tvStatus.scrollTo(
						0,
						SdkMain.tvStatus.getLineCount()
								* SdkMain.tvStatus.getLineHeight()
								+ SdkMain.tvStatus.getTop()
								- SdkMain.tvStatus.getBottom());
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_BROWN_DATA_FILE_WL: {
				String strPath = intent.getData().getPath();
				tvBrownFile_Wl.setText(strPath);
			}
				break;
			default:
				break;
			}
		}
	}

	private static final int REQUEST_BROWN_DATA_FILE_WL = 101;

	public native int WaveletTransform(String javaString, int Sample);

	public native int WaveletTransformInverse(String javaString, int Sample,
			int Lever);

	public native int detectEye(String javaString1, String javaString2,
			int Sample);

	static {
		System.loadLibrary("Wavelet");
	}
}
