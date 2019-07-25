package com.example.https.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class DemoX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if (chain == null || chain.length <=0){
			return;
		}
      Logger.d(Constants.TAG, chain[0].toString());
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		if (chain == null || chain.length <=0){
			return;
		}
		Logger.d(Constants.TAG, chain[0].toString());
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[]{};
	}

}
