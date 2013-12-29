package com.eegsdk;

import com.eegproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class SdkMain extends Activity {
	private TabHost tabhost;
	private static final int REQUEST_BROWN_EDF_FILE = 100;
	private static final int REQUEST_BROWN_DATA_FILE_WL = 101;
	private TextView tvStatus;

	/**
	 * ReadEdf Tab
	 */
	RadioButton rbAllDataOfAllChannel, rbAllDataOfChannel, rbDataOfAllChannel,
			rbDataOfChannel;
	Button btnReadEdf;
	TextView tvBrownFile_ReadEdf;
	RadioGroup rbGroup;
	EditText etStart, etEnd, etChannel;

	// khai bao cac Button, EditText, ... trong giao dien Tab Read Edf
	private void readedf_init() {
		rbAllDataOfAllChannel = (RadioButton) findViewById(R.id.rbAllDataOfAllChannel);
		rbAllDataOfChannel = (RadioButton) findViewById(R.id.rbAllDataOfChannel);
		rbDataOfAllChannel = (RadioButton) findViewById(R.id.rbDataOfAllChannel);
		rbDataOfChannel = (RadioButton) findViewById(R.id.rbDataOfChannel);
		rbGroup = (RadioGroup) findViewById(R.id.rbGroup);

		tvBrownFile_ReadEdf = (TextView) findViewById(R.id.tvBrownEdfFile_ReadEdf);
		btnReadEdf = (Button) findViewById(R.id.btnReadEdf);
		etStart = (EditText) findViewById(R.id.etStartSample);
		etEnd = (EditText) findViewById(R.id.etEndSample);
		etChannel = (EditText) findViewById(R.id.etChannel);
	}

	// xay dung cac su kien click button trong giao dien Tab Read Edf
	private void readedf_even() {
		tvBrownFile_ReadEdf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_EDF_FILE);
			}
		});

		btnReadEdf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int CheckRbGroup = rbGroup.getCheckedRadioButtonId();
				if (tvBrownFile_ReadEdf.getText().toString().trim().length() > 0)
					switch (CheckRbGroup) {
					case R.id.rbAllDataOfAllChannel: {
						System.out.print("\nAllDataOfAllChannel");
						ReadData(tvBrownFile_ReadEdf.getText().toString(), 1,
								0, 0, 0);
						tvStatus.append("\n- Read All Data Of All Channel, Output File:");
						tvStatus.append("\n" + tvBrownFile_ReadEdf.getText()
								+ "_AllData\n");
					}
						break;
					case R.id.rbAllDataOfChannel: {
						System.out.print("\nAllDataOfChannel");
						String strChannel = etChannel.getText().toString();
						if (strChannel.trim().length() > 0) {
							ReadData(tvBrownFile_ReadEdf.getText().toString(),
									2, 0, 0, Integer.parseInt(strChannel));
							tvStatus.append("\n- Read All Data Of Channel, Output File:");
							tvStatus.append("\n"
									+ tvBrownFile_ReadEdf.getText()
									+ "_AllData_Channel" + strChannel + "\n");
						} else
							tvStatus.append("\nEnter Data!\n");
					}
						break;
					case R.id.rbDataOfAllChannel:
						System.out.print("\nDataOfAllChannel");
						break;
					case R.id.rbDataOfChannel: {
						System.out.print("\nDataOfChannel");
						String strChannel = etChannel.getText().toString();
						String strStart = etStart.getText().toString();
						String strEnd = etEnd.getText().toString();
						if ((strChannel.trim().length() > 0)
								&& (strStart.trim().length() > 0)
								&& (strEnd.trim().length() > 0)) {
							ReadData(tvBrownFile_ReadEdf.getText().toString(),
									4, Integer.parseInt(strStart),
									Integer.parseInt(strEnd),
									Integer.parseInt(strChannel));
							tvStatus.append("\n- Read Data Of Channel, Output File:");
							tvStatus.append("\n"
									+ tvBrownFile_ReadEdf.getText() + "_"
									+ strStart + "_" + strEnd + "_Data_Channel"
									+ strChannel + "\n");
						} else
							tvStatus.append("\nEnter Data!\n");
					}
						break;
					default:
						break;
					}
				else
					tvStatus.append("\nChoose Edf File!");

				tvStatus.scrollTo(0,
						tvStatus.getLineCount() * tvStatus.getLineHeight()
								+ tvStatus.getTop() - tvStatus.getBottom());
			}
		});
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
						tvStatus.append("\n- Wavelet Transform Complete, Output File:");
						tvStatus.append("\n" + tvBrownFile_Wl.getText()
								+ "_DWT\n");
					} else
						tvStatus.append("\nEnter Data!\n");
				} else
					tvStatus.append("\nChoose Data File!");

				tvStatus.scrollTo(0,
						tvStatus.getLineCount() * tvStatus.getLineHeight()
								+ tvStatus.getTop() - tvStatus.getBottom());
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
						tvStatus.append("\n- Wavelet Transform Inverse Complete, Output File:");
						tvStatus.append("\n" + tvBrownFile_Wl.getText()
								+ "_IDWT\n");
					} else
						tvStatus.append("\nEnter Data!\n");
				} else
					tvStatus.append("\nChoose Data File!");

				tvStatus.scrollTo(0,
						tvStatus.getLineCount() * tvStatus.getLineHeight()
								+ tvStatus.getTop() - tvStatus.getBottom());
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sdk_main);

		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvStatus.setMovementMethod(new ScrollingMovementMethod());

		tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();
		tab_init();

		readedf_init();
		readedf_even();

		wavelet_init();
		wavelet_even();
	}

	private void tab_init() {
		tabhost.addTab(tabhost.newTabSpec("first").setIndicator("CONFIG")
				.setContent(R.id.tab1));
		tabhost.addTab(tabhost.newTabSpec("second").setIndicator("READEDF")
				.setContent(R.id.tab2));
		tabhost.addTab(tabhost.newTabSpec("third").setIndicator("WAVELET")
				.setContent(R.id.tab3));
		tabhost.addTab(tabhost.newTabSpec("fourth").setIndicator("ICA")
				.setContent(R.id.tab4));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_BROWN_EDF_FILE: {
				String strPath = intent.getData().getPath();
				tvBrownFile_ReadEdf.setText(strPath);
			}
				break;
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

	public native int ReadData(String javaString, int Type, int Start,
			int Stop, int Channel);

	public native int WaveletTransform(String javaString, int Sample);

	public native int WaveletTransformInverse(String javaString, int Sample,
			int Lever);

	static {
		System.loadLibrary("ReadData");
	}
	static {
		System.loadLibrary("Wavelet");
	}
}
