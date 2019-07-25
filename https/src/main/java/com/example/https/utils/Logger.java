package com.example.https.utils;

import static android.os.Environment.MEDIA_MOUNTED;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


public class Logger {

	// 用于开发者的log
	protected static boolean mLogger_dev = true;
	private static Context mContext;

	public static void setDebugLogging(boolean enabled) {
		mLogger_dev = enabled;
	}

	public static void d_dev(String tag, String msg) {
		if (mLogger_dev) {
			Log.d(tag, msg);
		}
	}

	public static void i_dev(String tag, String msg) {
		if (mLogger_dev) {
			Log.i(tag, msg);
		}
	}

	public static void w_dev(String tag, String msg) {
		if (mLogger_dev) {
			Log.w(tag, msg);
			Logger.writeLog(null, "warn:" + msg, 2);
		}
	}

	public static void e_dev(String tag, String msg) {
		if (mLogger_dev) {
			Log.e(tag, msg);
			Logger.writeLog(null, "error:" + msg, 2);
		}
	}
	
	public static void e_dev( Throwable exception ){
		if( mLogger_dev && null != exception ){
			exception.printStackTrace();
		}//end of show error log and exception not null
	}

	// 用于内部打印log
	 protected static boolean mLogger_inner = true;
//	protected static boolean mLogger_inner = false;

	public static boolean isDebugLogging() {
		return mLogger_inner;
	}

	public static void v(String tag, String msg) {
		if (mLogger_inner) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (mLogger_inner) {
			Log.v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (mLogger_inner) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (mLogger_inner) {
			Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (mLogger_inner) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (mLogger_inner) {
			Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (mLogger_inner) {
			Log.w(tag, msg);
			Logger.writeLog(null, "warn:" + msg, 2);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (mLogger_inner) {
			Log.w(tag, msg, tr);
		}
	}

	public static void w(String tag, Throwable tr) {
		if (mLogger_inner) {
			Log.w(tag, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (mLogger_inner) {
			Log.e(tag, msg);
			Logger.writeLog(null, "error:" + msg, 2);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (mLogger_inner) {
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 私有日志，正式版本不会进行打印
	 * @param log
	 */
	
	public static void s(String tag,String log)
	{
		if( isShowSafeLog() ){
			Log.d(tag, log);
		}//end of if show safe log
	}
	
	public static void s(Throwable throwable)
	{
		if( isShowSafeLog() ){
			throwable.printStackTrace();
		}//end of if show safe log
	}
		
	private static boolean isShowSafeLog(){
		return mLogger_dev && mLogger_inner;
	}
	
	 private static boolean writeLogEnable = true;
//	private static boolean writeLogEnable = false;

	/**
	 * 写本地log，测试使用
	 * 
	 * @param context
	 * @param content
	 *            0:/data/data/(package)/cache
	 *            1:/storage/sdcard0/Android/data/(package)/cache
	 *            2:/storage/sdcard0
	 */
	public static void writeLog(Context context, String content, int pathId) {
		try {
			// 发正式版本时，禁止写log
			if (!writeLogEnable) {
				return;
			}

			if (mContext == null) {
				if (context == null) {
					return;
				} else {
					mContext = context;
				}
			}

			if (!MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				pathId = 0;
			}

			String path = null;
			switch (pathId) {
			case 0:
				path = mContext.getCacheDir().getPath();
				break;
			case 1:
				path = mContext.getExternalCacheDir().getPath();
				break;
			case 2:
				path = Environment.getExternalStorageDirectory().getPath();
				break;
			default:
				break;
			}

			if (TextUtils.isEmpty(path)) {
				Logger.w(Constants.TAG, "path of log is invalid, change to cache path");
				path = mContext.getCacheDir().getPath();
			}

			content = "\n" + getDate() + "   " + content;
			String filename = "voiceads_log.txt";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mContext = null;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

}
