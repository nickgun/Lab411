package com.eegsdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

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
	private TabHost th_sdk_main, th_sdk_ica;
	private static final int REQUEST_BROWN_EDF_FILE = 100;
	private static final int REQUEST_BROWN_DATA_FILE_WL = 101;
	private static final int REQUEST_BROWN_F8_FILE_DE = 102;
	private static final int REQUEST_BROWN_F7_FILE_DE = 103;
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

	/**
	 * Detect GazeEye, EyeBlink
	 */
	TextView tvBrownFileF8_De, tvBrownFileF7_De;
	EditText etSample_De;
	Button btnDetect;

	void readFileDetect(String str) {
		String Line[] = new String[300];
		int i = 0;
		String a;
		String b;
		File fl = new File(str);
		FileInputStream ips = null;
		Scanner input = null;
		if (fl.exists()) {
			try {
				// tao luong noi den tap tin
				ips = new FileInputStream(fl);
				// dung phuong tien Scanner de doc
				input = new Scanner(ips);
				{
					while (input.hasNextLine()) {
						Line[i] = input.nextLine();
						i++;
					}
				}
				ips = null;
				input = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			int ieb = Integer.parseInt(Line[0]);
			int ir = Integer.parseInt(Line[1]);
			int il = Integer.parseInt(Line[2]);
			tvStatus.append("\nDetect Complete");
			tvStatus.append("\n      #EyeBlink: " + ieb);
			for (i = 0; i < ieb; i++) {
				int j = i + 3;
				tvStatus.append("\n            Sample " + Line[j]);
			}
			tvStatus.append("\n      #GazeRight:" + (ir / 2));
			for (i = 0; i < ir; i += 2) {
				int j = i + 3 + ieb;
				tvStatus.append("\n            Sample " + Line[j] + " to "
						+ Line[j + 1]);
			}
			tvStatus.append("\n      #GazeLeft:" + il / 2);
			for (i = 0; i < il; i += 2) {
				int j = i + 3 + ieb + ir;
				tvStatus.append("\n            Sample " + Line[j] + " to "
						+ Line[j + 1]);
			}
			tvStatus.append("\n");
		}

		try {
			Process p = Runtime.getRuntime().exec("rm " + str);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void detect_eye_init() {
		tvBrownFileF7_De = (TextView) findViewById(R.id.tvBrownFileF7_De);
		tvBrownFileF8_De = (TextView) findViewById(R.id.tvBrownFileF8_De);
		etSample_De = (EditText) findViewById(R.id.etSample_De);
		btnDetect = (Button) findViewById(R.id.btnDetect_de);
	}

	void detect_eye_even() {
		tvBrownFileF7_De.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_F7_FILE_DE);
			}
		});

		tvBrownFileF8_De.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_F8_FILE_DE);
			}
		});
		btnDetect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strF7Path = tvBrownFileF7_De.getText().toString();
				String strF8Path = tvBrownFileF8_De.getText().toString();
				String strSample_De = etSample_De.getText().toString();
				if ((strF7Path.trim().length() > 0)
						&& (strF8Path.trim().length() > 0)) {
					if (strSample_De.trim().length() > 0) {
						detectEye(strF8Path, strF7Path,
								Integer.parseInt(strSample_De));
						readFileDetect(strF8Path + "_DetectEye");
					} else
						tvStatus.append("\nEnter Sample!\n");
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

		th_sdk_main = (TabHost) findViewById(R.id.tabhost);
		th_sdk_main.setup();
		th_sdk_ica = (TabHost) findViewById(R.id.tabhost_sdk_ica);
		th_sdk_ica.setup();
		tab_init();

		readedf_init();
		readedf_even();

		wavelet_init();
		wavelet_even();

		detect_eye_init();
		detect_eye_even();
	}

	private void tab_init() {
		th_sdk_main.addTab(th_sdk_main.newTabSpec("first")
				.setIndicator("CONFIG").setContent(R.id.tab1));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("second")
				.setIndicator("READEDF").setContent(R.id.tab2));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("third")
				.setIndicator("WAVELET").setContent(R.id.tab3));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("fourth").setIndicator("ICA")
				.setContent(R.id.tab4));
		th_sdk_main.addTab(th_sdk_main.newTabSpec("fiveth")
				.setIndicator("DETECT GAZEEYE/EYEBLINK").setContent(R.id.tab5));

		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("first")
				.setIndicator("ICA PROS").setContent(R.id.tabICApros));
		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("second")
				.setIndicator("ICA INVERSE").setContent(R.id.tabICAinverse));
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
			case REQUEST_BROWN_F7_FILE_DE: {
				String strPath = intent.getData().getPath();
				tvBrownFileF7_De.setText(strPath);
			}
				break;
			case REQUEST_BROWN_F8_FILE_DE: {
				String strPath = intent.getData().getPath();
				tvBrownFileF8_De.setText(strPath);
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

	public native int detectEye(String javaString1, String javaString2,
			int Sample);

	static {
		System.loadLibrary("ReadData");
	}
	static {
		System.loadLibrary("Wavelet");
	}
	static {
		System.loadLibrary("DetectEye");
	}
}
