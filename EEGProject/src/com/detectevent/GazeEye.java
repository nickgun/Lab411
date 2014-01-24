package com.detectevent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eegproject.R;

public class GazeEye extends Activity {
	Button btnDetectGazeEye;
	TextView tvBrownFileF8, tvBrownFileF7;
	EditText etSample;
	TextView tvStatusEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detect_gazeeye);

		tvStatusEvent = (TextView) findViewById(R.id.tvStatusEvent);

		tvBrownFileF8 = (TextView) findViewById(R.id.tvBrownFileF8_DetectGazeEye);
		tvBrownFileF8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_F8_FILE_DE);
			}
		});

		tvBrownFileF7 = (TextView) findViewById(R.id.tvBrownFileF7_DetectGazeEye);
		tvBrownFileF7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_F7_FILE_DE);
			}
		});

		etSample = (EditText) findViewById(R.id.etSample_DetectGazeEye);

		btnDetectGazeEye = (Button) findViewById(R.id.btnStartDetectGazeEye);
		btnDetectGazeEye.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strF7Path = tvBrownFileF7.getText().toString();
				String strF8Path = tvBrownFileF8.getText().toString();
				String strSample = etSample.getText().toString();
				if ((strF7Path.trim().length() > 0)
						&& (strF8Path.trim().length() > 0)) {
					if (strSample.trim().length() > 0) {
						detectEye(strF8Path, strF7Path,
								Integer.parseInt(strSample));
						readFileDetect(strF8Path + "_DetectEye");
					} else
						tvStatusEvent.append("\nEnter Sample!\n");
				} else
					tvStatusEvent.append("\nChoose Data File!");

				tvStatusEvent.scrollTo(
						0,
						tvStatusEvent.getLineCount()
								* tvStatusEvent.getLineHeight()
								+ tvStatusEvent.getTop()
								- tvStatusEvent.getBottom());
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
				tvBrownFileF7.setText(strPath);
			}
				break;
			case REQUEST_BROWN_F8_FILE_DE: {
				String strPath = intent.getData().getPath();
				tvBrownFileF8.setText(strPath);
			}
				break;
			default:
				break;
			}
		}
	}

	private static final String Ip = "127.0.0.1";
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

			int ir = Integer.parseInt(Line[0]);
			int il = Integer.parseInt(Line[1]);
			tvStatusEvent.append("\nDetect Complete");
			tvStatusEvent.append("\n      #GazeRight:  " + ir / 2);
			for (i = 0; i < ir; i += 2) {
				int j = i + 2;
				tvStatusEvent.append("\n            Sample " + Line[j] + " to "
						+ Line[j + 1]);
				new SendData(Ip, 8899, 300).start();
			}
			tvStatusEvent.append("\n      #GazeLeft:  " + (il / 2));
			for (i = 0; i < il; i += 2) {
				int j = i + 2 + ir;
				tvStatusEvent.append("\n            Sample " + Line[j] + " to "
						+ Line[j + 1]);
				new SendData(Ip, 8899, 400).start();
			}
			tvStatusEvent.append("\n");
		}

		try {
			Process p = Runtime.getRuntime().exec("rm " + str);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SendData extends Thread {

		String Ip;
		int port;
		int data;

		SendData(String Ip, int port, int data) {
			this.Ip = Ip;
			this.port = port;
			this.data = data;
		}

		public void run() {
			try {
				Socket client = new Socket(InetAddress.getByName(Ip),
						8899);
				DataOutputStream dataout = new DataOutputStream(
						client.getOutputStream());
				dataout.writeInt(data);

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
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
