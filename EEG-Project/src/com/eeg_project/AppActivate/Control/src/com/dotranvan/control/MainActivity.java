package com.dotranvan.control;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dotranvan.myDataBase.MyDataBase;

public class MainActivity extends Activity {

	private Button btn_Start;
	private Button btn_Stop;
	private Button btn_select;
	private MyDataBase mdb;
	boolean test =true;

	private SharedPreferences mPrefer;
	
	ArrayList<String> str = new ArrayList<String>();
	private Boolean firstLvl = true;

	private static final String TAG = "F_PATH";

	private Item[] fileList;
	
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;

	ListAdapter adapter;

	private List<ResolveInfo> mListResolverInfo;
	private List<AppInfo> mListAppInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mdb = new MyDataBase(MainActivity.this);
		if(mdb.selectAll().size() == 0) {
			mdb.insert("000001","back", "input keyevent 4");
			mdb.insert("000010","home", "input keyevent 3");
			mdb.insert("000011","power", "input keyevent 26");
			
		}
		
		mPrefer = getSharedPreferences(" Setting", MODE_PRIVATE);

		btn_Start = (Button) findViewById(R.id.btnstart);
		btn_Stop = (Button) findViewById(R.id.btnstop);
		btn_select=(Button)findViewById(R.id.btnsele);
		
		

		btn_Start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = mPrefer.edit();
				editor.putString("back", "input keyevent 4");
				editor.putString("home", "input keyevent 3");
				editor.putString("call", "input keyevent 5");
				editor.putString("left", "input keyevent 21");
				editor.putString("right", "input keyevent 22");
				editor.putString("enter", "input keyevent 66");
				editor.putString("search", "input keyevent 84");
				editor.putString("menu", "input keyevent 82");
				
				editor.putString("key0", "input keyevent 7");
				editor.putString("key1", "input keyevent 8");
				editor.putString("key2", "input keyevent 9");
				editor.putString("key3", "input keyevent 10");
				editor.putString("key4", "input keyevent 11");
				editor.putString("key5", "input keyevent 12");
				editor.putString("key6", "input keyevent 13");
				editor.putString("key7", "input keyevent 14");
				editor.putString("key8", "input keyevent 15");
				editor.putString("key9", "input keyevent 16");
				
				//test
				editor.putString("callmp3", "am start -n com.android.music/.MusicBrowserActivity");
				new getListApp().start();
				
				editor.commit();
				
				Intent mService = new Intent(getBaseContext(), MyService.class);
				startService(mService);
			}
		});

		btn_Stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mService = new Intent(getBaseContext(), MyService.class);
				stopService(mService);
			}
		});
		
		btn_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadFileList();
				showDialog(DIALOG_LOAD_FILE);
				
				
				
			}
		});
		
	}

	//
	private void loadFileList() {
		try {
			path.mkdirs();
		} catch (SecurityException e) {
			Log.e(TAG, "unable to write on the sd card ");
		}

		// Checks whether path exists
		if (path.exists()) {
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();

				}
			};

			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++) {
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				
				if (sel.isDirectory()) {
					fileList[i].icon = R.drawable.diretory_icon;
					Log.d("DIRECTORY", fileList[i].file);
				} else {
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl) {
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++) {
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		} else {
			Log.e(TAG, "path does not exist");
		}

		adapter = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};

	}
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		if (fileList == null) {
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			return dialog;
		}

		switch (id) {
		case DIALOG_LOAD_FILE:
			builder.setTitle("Choose your file");
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(DialogInterface dialog, int which) {
					chosenFile = fileList[which].file;
					File sel = new File(path + "/" + chosenFile);
					if (sel.isDirectory()) {
						firstLvl = false;

						// Adds chosen directory to list
						str.add(chosenFile);
						fileList = null;
						path = new File(sel + "");

						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}

					// Checks if 'up' was clicked
					else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

						// present directory removed from list
						String s = str.remove(str.size() - 1);

						// path modified to exclude present directory
						path = new File(path.toString().substring(0,
								path.toString().lastIndexOf(s)));
						fileList = null;

						// its the first level
						if (str.isEmpty()) {
							firstLvl = true;
						}
						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}
					// File picked
					else {
//						public void
						String str ="";
						try {
							
							
						    File sdcard = Environment.getExternalStorageDirectory();
						    File file = new File(sdcard + "/Android_Project/EEG_Data","back.txt");
						    System.out.println("exception");

						        BufferedReader br = new BufferedReader(new FileReader(file));  
						        String line;   
						        while ((line = br.readLine()) != null) {
						        	  str+=line+"\n";
						                    } }
						    catch (IOException e) {
						        e.printStackTrace();

						    }
						String id=str.toString().trim();
						
						
					   	String command=mdb.selectAll(id);
						
						Runtime runtime = Runtime.getRuntime();
						try {
							runtime.exec(new String[] { "su", "-c", command });
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

				}
			});
			break;
		}
		dialog = builder.show();
		return dialog;
	}
	
	

	class getListApp extends Thread {
		public void run() {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			mListResolverInfo = getPackageManager().queryIntentActivities(
					intent, 0);

			mListAppInfo = new ArrayList<AppInfo>();
			mListAppInfo.clear();
			for (int i = 0; i < mListResolverInfo.size(); i++) {
				AppInfo appInfo = new AppInfo();
				appInfo.setAppName(mListResolverInfo.get(i)
						.loadLabel(getPackageManager()).toString());
				appInfo.setClassName(mListResolverInfo.get(i).activityInfo.name);
				appInfo.setPackageName(mListResolverInfo.get(i).activityInfo.packageName);
				appInfo.setProcessName(mListResolverInfo.get(i).activityInfo.processName);

				mListAppInfo.add(appInfo);
				Editor editor = mPrefer.edit();
				editor.putString("open " + String.valueOf(i),
						mListResolverInfo.get(i).activityInfo.processName);
				editor.commit();

			}
			Editor editor = mPrefer.edit();
			String str[]={"open " + String.valueOf(0),"open " + String.valueOf(1),"open " + String.valueOf(9)};
			editor.putString(str[0].replace(str[0], "Camera"),
					mListResolverInfo.get(0).activityInfo.processName);
			editor.putString(str[1].replace(str[1], "Web"),
					mListResolverInfo.get(1).activityInfo.processName);
			editor.putString(str[2].replace(str[2], "Music"),
					mListResolverInfo.get(9).activityInfo.processName);
		    editor.commit(); 

		}
	}

}


