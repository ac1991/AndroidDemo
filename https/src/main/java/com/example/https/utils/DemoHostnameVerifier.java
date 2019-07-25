package com.example.https.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class DemoHostnameVerifier implements HostnameVerifier{
	private String hostname = "";

	public void setHostname(String hostname){
		this.hostname = hostname;
	}

	@Override
	public boolean verify(String hostname, SSLSession session) {
		if (this.hostname.equals(hostname)) {
			//所有的主机名都验证通过
			return true;
		}

		return false;
	}

}
