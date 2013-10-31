package com.nickgun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShowNote extends Activity {
	private Button btnNote;
	private TextView tvNoteTime;
	private TextView tvNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_show_note);

		btnNote = (Button) findViewById(R.id.btnNote);
		tvNoteTime = (TextView) findViewById(R.id.tv_note_time);
		tvNote = (TextView) findViewById(R.id.tv_Note);

		Bundle extras = getIntent().getExtras();
		String strNote = extras.getString("NOTE");
		String strNoteTime = extras.getString("NOTE_TIME");

		// add du lieu vao dialog
		tvNoteTime.append(strNoteTime);
		tvNote.setText(strNote);

		btnNote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
