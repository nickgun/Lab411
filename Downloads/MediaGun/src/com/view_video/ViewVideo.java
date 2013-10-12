package com.view_video;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.nickgun.R;
import com.nickgun.R.id;
import com.nickgun.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class ViewVideo extends Activity {
	private VideoView mVideoView = null;
	private String mInputFileName = null;
	private String mInputFileTxt = null;
	private ArrayList<String> arrayList = new ArrayList<String>(); // include
																	// data note
	private int indexNote;

	private Button cleanNote;
	private Button showNote;
	private EditText noteDislay;

	private String myVideoFilePath;
	private Context myContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		myContext = this.getApplicationContext();
		setContentView(R.layout.videoview);

		mVideoView = (VideoView) this.findViewById(R.id.videoView);
		cleanNote = (Button) findViewById(R.id.cleanNoteBtn);
		showNote = (Button) findViewById(R.id.showNoteBtn);
		noteDislay = (EditText) findViewById(R.id.noteDislay);

		String myStorage = Environment.getExternalStorageDirectory().toString();
		myVideoFilePath = myStorage + "/NickGun/CameraGun/";

		// get data from MainActivity
		Bundle myBundle = getIntent().getExtras();

		// get Path
		mInputFileName = myBundle.getString("path");
		// get Path of file txt
		String[] arrStr1 = mInputFileName.split("\\/");
		String nameVideo = arrStr1[arrStr1.length - 1];
		String[] arrStr2 = nameVideo.split("\\.");
		String name = arrStr2[0];
		mInputFileTxt = myVideoFilePath + name + "/" + name + ".txt";
		playRecording();
		readNote();

		cleanNote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				noteDislay.setText("");
			}
		});

		showNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mVideoView.pause();
				String str = noteDislay.getText().toString();
				Intent myIntent = new Intent();
				Bundle extras = new Bundle();
				extras.putString("NOTE", str);
				extras.putString("NOTE_TIME",
						String.valueOf(arrayList.get(indexNote * 2)));
				myIntent.putExtras(extras);
				myIntent.setClass(myContext, ShowNote.class);
				startActivity(myIntent);

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void playRecording() {
		MediaController mc = new MediaController(this);
		mVideoView.setMediaController(mc);
		mVideoView.setVideoPath(mInputFileName);
		mVideoView.start();
	}

	private void readNote() {
		try {
			FileReader myFileReader = new FileReader(mInputFileTxt);
			BufferedReader bf = new BufferedReader(myFileReader);
			String s;
			while ((s = bf.readLine()) != null) {
				Log.e("LOG", "Read note: " + s);
				arrayList.add(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread thr = new Thread(null, doBackGroundThreadProcessing,
				"background");
		thr.start();
	}

	private Handler my_handler = new Handler();
	private Runnable doBackGroundThreadProcessing = new Runnable() {
		@Override
		public void run() {
			ArrayList<Integer> time = new ArrayList<Integer>();
			for (int i = 0; i < arrayList.size(); i = i + 2) {
				time.add(Integer.valueOf(arrayList.get(i)));
			}
			indexNote = time.size() + 1;
			while (true) {
				int currentTime = mVideoView.getCurrentPosition();
				for (int count = time.size(); count > 0; count--)
					if (currentTime > time.get(count - 1)) {
						if (indexNote != (count - 1)) {
							Log.e("LOG", "Current time:" + currentTime
									+ "\nRead time : " + time.get(count - 1));
							indexNote = count - 1;
							my_handler.post(doUpdateGUI);
						}
						count = 0;
					}
			}
		}
	};

	private Runnable doUpdateGUI = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			updateGUI("");
		}
	};

	private void updateGUI(String str) {
		// execute dislay note in video
		showNote.setEnabled(true);
		String note = arrayList.get(indexNote * 2 + 1);
		Log.e("LOG", "Dislay note : " + note);
		noteDislay.setText(note);
	}
}
