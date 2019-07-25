package com.example.https.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


import android.text.TextUtils;


public class HttpRequest extends Thread {

	private int mTimeOut =  20;
	// Post数据
	private ArrayList<byte[]> postDatas = new ArrayList<byte[]>();
	private URL mUrl;
	//请求路径
	private String url = null;

	public static final int CONNECT_GET = 0;
	public static final int CONNECT_POST = 1;
	// Http请求类型，默认为Get方式
	private int mConnectType = CONNECT_GET;

	public interface HttpRequestListener {
		void onResult(byte[] data);

		void onBufferReceive(byte[] data);

		void onError(Exception e, int errorCode);
	}

	private HttpRequestListener mListener = null;

	/**
	 * 设置超时时间
	 * 
	 * @param timeout
	 */
	public void setTimeOut(int timeout) {
		mTimeOut = timeout;
	}

	/**
	 * 开始http请求，启动线程异步处理
	 * 
	 * @param listener
	 */
	public void startRequest(HttpRequestListener listener) {
		mListener = listener;
		start();
	}

	@Override
	public void run() {
		
		if (mConnectType == CONNECT_POST)
			runPost();
		else
			runGet();
	}

	public void setConnectType(int type) {
		mConnectType = type;
	}
	
	private void runPost(){
		if (!TextUtils.isEmpty(url)) {
			if (url.startsWith("https:")) {
				httpsPost();
				return ;
			}
			
			if (url.startsWith("http:")) {
				httpPost();
				return ;
			}
			
			Logger.d_dev(Constants.TAG, "url is not valid");
		}else {
			Logger.d_dev(Constants.TAG, "url is null");
		}
	}
	
	private void runGet(){
		if (!TextUtils.isEmpty(url)) {
			if (url.startsWith("https:")) {
				httpsGet();
				return ;
			}
			
			if (url.startsWith("http:")) {
				httpGet();
				return ;
			}
			
			Logger.d_dev(Constants.TAG, "url is not valid");
		}else {
			Logger.d_dev(Constants.TAG, "url is null");
		}
	}

	/**
	 * http get请求
	 */
	private void httpGet() {
		HttpURLConnection urlConn = null;
		InputStream in = null;
		try {
			urlConn = (HttpURLConnection) mUrl.openConnection();
			urlConn.setConnectTimeout(mTimeOut);
			urlConn.setReadTimeout(mTimeOut);
			urlConn.setRequestMethod("GET");

			int responseCode = urlConn.getResponseCode();

			Logger.d_dev(Constants.TAG, "HttpRequest response code: "
					+ responseCode);

			if (HttpURLConnection.HTTP_OK == responseCode) {
				in = urlConn.getInputStream();
				throwResult(readStream(in));
			} else {
				throwError(new Exception("Http Request Failed!"), responseCode);
			}
		} catch (Exception e) {
			Logger.w(Constants.TAG, "runGet error!" + "\n" + e.toString());
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (urlConn != null) {
					urlConn.disconnect();
					urlConn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * http post请求
	 *
	 */
	private void httpPost() {
		// 开始时间
		long startTime = System.currentTimeMillis();
		InputStream in = null;
		OutputStream outputStream = null;
		HttpURLConnection httpConn = null;
		try {
			httpConn = (HttpURLConnection) mUrl.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");
			httpConn.setConnectTimeout(mTimeOut);
			httpConn.setReadTimeout(mTimeOut);
			httpConn.setRequestProperty("Charset", "utf-8");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("X-encryption", "MIGUEncryption");
			// 测试服务器用，测试时需带上
//			 httpConn.setRequestProperty("X-real-ip", "114.80.166.240");
			//
			// httpConn.setRequestProperty("X-ACCESS-EP", "server");
			outputStream = httpConn.getOutputStream();
			for (byte[] buf : postDatas) {
				outputStream.write(buf);
			}
			outputStream.flush();
			outputStream.close();

			int responseCode = httpConn.getResponseCode();
			Logger.d(Constants.TAG, "HttpRequest response code: "
					+ responseCode);

			if (HttpURLConnection.HTTP_OK == responseCode) {
				in = httpConn.getInputStream();
				throwResult(readStream(in));
				Logger.d_dev("响应时长：", (System.currentTimeMillis() - startTime)
						+ "");
			} else {
				throwError(new Exception("Http Request Failed."), responseCode);
			}
		} catch (Exception e) {
			Logger.w(Constants.TAG, "runPost error!" + "\n" + e.toString());
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (httpConn != null) {
					httpConn.disconnect();
					httpConn = null;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * https post请求
	 *
	 */
	private void httpsPost() {

		HttpsURLConnection httpsURLConnection = null;
		InputStream in = null;
		OutputStream outputStream = null;
		try {
			if (mUrl != null) {
				SSLContext scContext = SSLContext.getInstance("TLS");
				scContext.init(null,new TrustManager[] { new DemoX509TrustManager() },new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(scContext.getSocketFactory());

				DemoHostnameVerifier hostnameVerifier = new DemoHostnameVerifier();
				hostnameVerifier.setHostname(mUrl.getHost());
				HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

				httpsURLConnection = (HttpsURLConnection) mUrl.openConnection();
				httpsURLConnection.setDoOutput(true);
				httpsURLConnection.setDoInput(true);
				httpsURLConnection.setUseCaches(false);
				httpsURLConnection.setRequestMethod("POST");
				httpsURLConnection.setConnectTimeout(mTimeOut);
				httpsURLConnection.setReadTimeout(mTimeOut);
				httpsURLConnection.setRequestProperty("Charset", "utf-8");
				httpsURLConnection.setRequestProperty("Content-Type","application/json");
				httpsURLConnection.setRequestProperty("X-encryption","MIGUEncryption");
				outputStream = httpsURLConnection.getOutputStream();
				for (byte[] buf : postDatas) {
					outputStream.write(buf);
				}
				outputStream.flush();
				outputStream.close();

				int responseCode = httpsURLConnection.getResponseCode();
				Logger.d(Constants.TAG, "HttpRequest response code: "
						+ responseCode);

				if (HttpURLConnection.HTTP_OK == responseCode) {
					in = httpsURLConnection.getInputStream();
					throwResult(readStream(in));
					// Logger.d_dev("响应时长：", (System.currentTimeMillis() -
					// startTime) + "");
				} else {
					throwError(new Exception("Http Request Failed."),
							responseCode);
				}
			}
		} catch (Exception e) {
			Logger.w(Constants.TAG, "runPost error!" + "\n" + e.toString());
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (httpsURLConnection != null) {
					httpsURLConnection.disconnect();
					httpsURLConnection = null;
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * https get请求
	 */
	private void httpsGet(){
		HttpsURLConnection httpsURLConnection = null;
		InputStream in = null;
		OutputStream outputStream = null;
		try {
			httpsURLConnection = (HttpsURLConnection) mUrl.openConnection();
			httpsURLConnection.setConnectTimeout(mTimeOut);
			httpsURLConnection.setReadTimeout(mTimeOut);
			httpsURLConnection.setRequestMethod("GET");

			int responseCode = httpsURLConnection.getResponseCode();

			Logger.d_dev(Constants.TAG, "HttpRequest response code: "
					+ responseCode);

			if (HttpURLConnection.HTTP_OK == responseCode) {
				in = httpsURLConnection.getInputStream();
				throwResult(readStream(in));
			} else {
				throwError(new Exception("Http Request Failed!"), responseCode);
			}
		} catch (Exception e) {
			Logger.w(Constants.TAG, "runGet error!" + "\n" + e.toString());
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (httpsURLConnection != null) {
					httpsURLConnection.disconnect();
					httpsURLConnection = null;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 设置请求数据
	 * 
	 * @param url
	 *            请求url
	 * @param param
	 *            url参数
	 * @param data
	 *            Post数据
	 */
	public void setRequest(String url, String param, byte[] data) {
		postDatas.clear();
		appendData(data);
		
		this.url = url;
		
		try {
			mUrl = composeUrl(url, param);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Logger.e(Constants.TAG, "url error:" + e);
		}
	}

	/**
	 * 追加数据
	 * 
	 * @param data
	 */
	private void appendData(byte[] data) {
		if (data != null)
			postDatas.add(data);
	}

	/**
	 * 将参数追加到url
	 * 
	 * @param baseurl
	 * @param param
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static URL composeUrl(String baseurl, String param)
			throws MalformedURLException {
		String url = baseurl;
		if (!TextUtils.isEmpty(baseurl) && !TextUtils.isEmpty(param)) {
			if (!baseurl.endsWith("?"))
				url += "?";
			url += param;
		}
		// url中包含空格
		if (url.contains(" ")) {
			url = url.replaceAll(" ", "%20");
		}
		return (new URL(url));
	}

	/**
	 * 通知回调结果
	 * 
	 * @param data
	 */
	private void throwResult(byte[] data) {
		if (mListener == null)
			return;
		mListener.onResult(data);
	}

	/**
	 * 请求发生错误
	 * 
	 * @param e
	 */
	private void throwError(Exception e, int errorCode) {
		if (mListener == null)
			return;
		mListener.onError(e, errorCode);
	}

	/**
	 * 读取数据流
	 *
	 * @param in http流
	 * @return 数据
	 * @throws IOException
	 */
	byte[] readStream(InputStream in) throws IOException {
		int ONE_K = 1024;
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int templen ;
		byte[] temp = new byte[ONE_K];
		while ((templen = bis.read(temp, 0, ONE_K)) != -1) {
			byteArrayOutputStream.write(temp, 0, templen);
		}
		return byteArrayOutputStream.toByteArray();
	}
}
