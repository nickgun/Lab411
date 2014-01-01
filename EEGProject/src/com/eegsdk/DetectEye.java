package com.eegsdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.eegproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DetectEye extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_detect_gazeeye);

		detect_eye_init();
		detect_eye_even();
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
			SdkMain.tvStatus.append("\nDetect Complete");
			SdkMain.tvStatus.append("\n      #EyeBlink:  " + ieb);
			for (i = 0; i < ieb; i++) {
				int j = i + 3;
				SdkMain.tvStatus.append("\n            Sample " + Line[j]);
			}
			SdkMain.tvStatus.append("\n      #GazeRight:  " + (ir / 2));
			for (i = 0; i < ir; i += 2) {
				int j = i + 3 + ieb;
				SdkMain.tvStatus.append("\n            Sample " + Line[j]
						+ " to " + Line[j + 1]);
			}
			SdkMain.tvStatus.append("\n      #GazeLeft:  " + il / 2);
			for (i = 0; i < il; i += 2) {
				int j = i + 3 + ieb + ir;
				SdkMain.tvStatus.append("\n            Sample " + Line[j]
						+ " to " + Line[j + 1]);
			}
			SdkMain.tvStatus.append("\n");
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
						SdkMain.tvStatus.append("\nEnter Sample!\n");
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

	private static final int REQUEST_BROWN_F8_FILE_DE = 102;
	private static final int REQUEST_BROWN_F7_FILE_DE = 103;

	public native int detectEye(String javaString1, String javaString2,
			int Sample);

	static {
		System.loadLibrary("DetectEye");
	}
}
