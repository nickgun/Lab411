package com.dotranvan.control;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

public class MyService extends Service {

	ServerSocket mServer;
	MyThread mMyThread;
	boolean run;

	SharedPreferences mPrefer;

	private List<ResolveInfo> mListResolverInfo;
	private List<AppInfo> mListAppInfo;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mMyThread = new MyThread(6060);
		new getListApp().start();
		mPrefer = getSharedPreferences("Setting", MODE_PRIVATE);

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
			}
			System.out.println("Size: " + mListAppInfo.size());
			for (int i = 0; i < mListAppInfo.size(); i++) {
				System.out.println(mListAppInfo.get(i).getAppName() + "\n"
						+ mListAppInfo.get(i).getClassName() + "\n"
						+ mListAppInfo.get(i).getPackageName() + "\n"
						+ mListAppInfo.get(i).getProcessName());
			}
		}
	}

	@Override
	public void onDestroy() {
		run = false;
		try {
			mServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("Service", "onDestroy");
		super.onDestroy();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		run = true;
		mMyThread.start();
	}

	class MyThread extends Thread {

		int port;

		public MyThread(int port) {
			this.port = port;
		}

		public void run() {
			try {
				mServer = new ServerSocket(port);
				Log.i("Service", "Server ready...");
				while (run) {
					Socket client = mServer.accept();
					Log.i("Service", "Client connected!");
					DataInputStream datain = new DataInputStream(
							client.getInputStream());
					String data = datain.readUTF();
					System.out.println(data);
					new Run(data).start();
					datain.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class Run extends Thread {
		String data;

		Run(String data) {
			
			this.data = data;
		}

		public void run() {
			
			String command = mPrefer.getString(data, "");
			if (command.equalsIgnoreCase("input keyevent 3")) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return;
			}

			if (!command.equals("")) {
				for (int i = 0; i < mListAppInfo.size(); i++) {
					if (command.equalsIgnoreCase(mListAppInfo.get(i)
						.getProcessName())) {

						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setClassName(mListAppInfo.get(i)
								.getPackageName(), mListAppInfo.get(i)
								.getClassName());
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
						startActivity(intent);
						System.out.println("open: "
								+ mListAppInfo.get(i).getProcessName());

						break;
					}
				}
			}

			try {
				
				Runtime runtime = Runtime.getRuntime();
				runtime.exec(new String[] { "su", "-c", command });

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			return;

		}

	}


}


