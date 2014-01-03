package com.eegsdk;

import com.eegproject.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReadEdf extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_readedf);
		Toast.makeText(getBaseContext(), "Demo", Toast.LENGTH_LONG);
		readedf_init();
		readedf_even();
	}

	/**
	 * ReadEdf Tab
	 */
	RadioButton rbAllDataOfAllChannel, rbAllDataOfChannel, rbDataOfAllChannel,
			rbDataOfChannel;
	Button btnReadEdf;
	TextView tvBrownFile_ReadEdf;
	RadioGroup rbGroup;
	EditText etStart, etEnd;
	Spinner spnChannel_Re;

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
		spnChannel_Re = (Spinner) findViewById(R.id.spnChanne_Re);
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
				int ChanNum = spnChannel_Re.getSelectedItemPosition();
				Log.e("TAG", "Chan"+ ChanNum);
				String strChannel = spnChannel_Re.getSelectedItem().toString();
				if (tvBrownFile_ReadEdf.getText().toString().trim().length() > 0)
					switch (CheckRbGroup) {
					case R.id.rbAllDataOfAllChannel: {
						System.out.print("\nAllDataOfAllChannel");
						ReadData(tvBrownFile_ReadEdf.getText().toString(), 1,
								0, 0, 0);
						SdkMain.tvStatus
								.append("\n- Read All Data Of All Channel, Output File:");
						SdkMain.tvStatus.append("\n"
								+ tvBrownFile_ReadEdf.getText()
								+ "_AllDataAllChan\n");
					}
						break;
					case R.id.rbAllDataOfChannel: {
						System.out.print("\nAllDataOfChannel");
						ReadData(tvBrownFile_ReadEdf.getText().toString(), 2,
								0, 0, ChanNum);
						SdkMain.tvStatus
								.append("\n- Read All Data Of Channel, Output File:");
						SdkMain.tvStatus.append("\n"
								+ tvBrownFile_ReadEdf.getText()
								+ "_AllData_Channel" + strChannel + "\n");
					}
						break;
					case R.id.rbDataOfAllChannel: {
						System.out.print("\nDataOfAllChannel");
						String strStart = etStart.getText().toString();
						String strEnd = etEnd.getText().toString();
						if ((strStart.trim().length() > 0)
								&& (strEnd.trim().length() > 0)) {
							ReadData(tvBrownFile_ReadEdf.getText().toString(),
									3, Integer.parseInt(strStart),
									Integer.parseInt(strEnd), 0);
							SdkMain.tvStatus
									.append("\n- Read Data Of All Channel, Output File:");
							SdkMain.tvStatus.append("\n"
									+ tvBrownFile_ReadEdf.getText() + "_"
									+ strStart + "_" + strEnd + "_AllChan"
									+ "\n");
						} else
							SdkMain.tvStatus.append("\nEnter Data!\n");
					}
						break;
					case R.id.rbDataOfChannel: {
						System.out.print("\nDataOfChannel");
						String strStart = etStart.getText().toString();
						String strEnd = etEnd.getText().toString();
						if ((strStart.trim().length() > 0)
								&& (strEnd.trim().length() > 0)) {
							ReadData(tvBrownFile_ReadEdf.getText().toString(),
									4, Integer.parseInt(strStart),
									Integer.parseInt(strEnd), ChanNum);
							SdkMain.tvStatus
									.append("\n- Read Data Of Channel, Output File:");
							SdkMain.tvStatus.append("\n"
									+ tvBrownFile_ReadEdf.getText() + "_"
									+ strStart + "_" + strEnd + "_Data_Chan"
									+ strChannel + "\n");
						} else
							SdkMain.tvStatus.append("\nEnter Data!\n");
					}
						break;
					default:
						break;
					}
				else
					SdkMain.tvStatus.append("\nChoose Edf File!");

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
			case REQUEST_BROWN_EDF_FILE: {
				String strPath = intent.getData().getPath();
				tvBrownFile_ReadEdf.setText(strPath);
			}
				break;
			default:
				break;
			}
		}
	}

	private static final int REQUEST_BROWN_EDF_FILE = 100;

	public native int ReadData(String javaString, int Type, int Start,
			int Stop, int Channel);

	static {
		System.loadLibrary("ReadData");
	}
}
