package com.nickgun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	Button btnCamera, btnVideoView, btnExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnVideoView = (Button) findViewById(R.id.btnVideoView);
		btnExit = (Button) findViewById(R.id.btnExit);

		btnCamera.setOnClickListener(this);
		btnVideoView.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		switch (i) {
		case R.id.btnCamera:
			startActivity(new Intent(this, CameraRecorder.class));
			break;
		case R.id.btnVideoView: {
			Intent myIntent = new Intent(Intent.ACTION_GET_CONTENT);
			myIntent.setType("file/*");
			startActivityForResult(myIntent, 1);
		}
			break;
		case R.id.btnExit:

			break;
		default:
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				String Path = data.getData().getPath();
				Intent myIntent = new Intent(this, ViewVideo.class);
				myIntent.putExtra("path", Path);
				startActivity(myIntent);
			}
		}
	}
}