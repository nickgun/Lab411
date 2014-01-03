package com.eegsdk;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.eegproject.R;

public class ICA extends Activity {
	TabHost th_sdk_ica;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdk_ica);

		th_sdk_ica = (TabHost) findViewById(R.id.tabhost_sdk_ica);
		th_sdk_ica.setup();

		tabInit();

		icaProsInit();
		icaProsEven();
	}

	private void tabInit() {
		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("first")
				.setIndicator("ICA PROS").setContent(R.id.tabICApros));
		th_sdk_ica.addTab(th_sdk_ica.newTabSpec("second")
				.setIndicator("ICA INVERSE").setContent(R.id.tabICAinverse));
	}

	/**
	 * ICA pros - ICA thuan
	 */
	Button btnICAPros;
	EditText etCompICAPros, etRowsICAPros, etColsICAPros;
	TextView tvChooseICAProsInput;

	private void icaProsInit() {
		btnICAPros = (Button) findViewById(R.id.btnICApros);
		etCompICAPros = (EditText) findViewById(R.id.etCompICAPros);
		etRowsICAPros = (EditText) findViewById(R.id.etRowsICAPros);
		etColsICAPros = (EditText) findViewById(R.id.etColsICAPros);
		tvChooseICAProsInput = (TextView) findViewById(R.id.tvChooseICAProsInput);
	}

	private void icaProsEven() {
		btnICAPros.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strComp = etCompICAPros.getText().toString();
				String strRows = etRowsICAPros.getText().toString();
				String strCols = etColsICAPros.getText().toString();
				String strInputFile = tvChooseICAProsInput.getText().toString();

				if ((strComp.trim().length() > 0)
						&& (strRows.trim().length() > 0)
						&& (strCols.trim().length() > 0)) {
					// tinh thoi gian tinh toan ICA
					long StartTime = System.currentTimeMillis();
					icaTransform(strInputFile, Integer.parseInt(strRows),
							Integer.parseInt(strCols),
							Integer.parseInt(strComp));
					long EndTime = System.currentTimeMillis();
					SdkMain.tvStatus.append("\n- Run ICA Complete after "
							+ (EndTime - StartTime) / 1000 + "s, Output File:");
					SdkMain.tvStatus.append("\n" + strInputFile + "_ICA\n");
				} else
					SdkMain.tvStatus.append("\nEnter Data!\n");

				SdkMain.tvStatus.scrollTo(
						0,
						SdkMain.tvStatus.getLineCount()
								* SdkMain.tvStatus.getLineHeight()
								+ SdkMain.tvStatus.getTop()
								- SdkMain.tvStatus.getBottom());
			}
		});

		tvChooseICAProsInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
				myIntent.setType("file/*");
				startActivityForResult(myIntent, REQUEST_BROWN_ICAPROS_INPUT);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_BROWN_ICAPROS_INPUT: {
				String strPath = intent.getData().getPath();
				tvChooseICAProsInput.setText(strPath);
			}
				break;
			default:
				break;
			}
		}
	}

	private static final int REQUEST_BROWN_ICAPROS_INPUT = 104;

	public native int icaTransform(String javaString, int rows, int cols,
			int comp);

	static {
		System.loadLibrary("ICA");
	}
}
