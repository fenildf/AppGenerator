/**
 * Title: JobsSSLSocketFactory.java
 * Description:
 * Copyright: Copyright (c) 2013 richinfo.cn
 * Company:深圳彩讯科技有限公司
 * Author 罗旭东 (hi@luoxudong.com)
 * Date 2014-8-26 上午9:37:14
 * Version 1.0
 */
package com.bchun.jobs.logic.network.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLSocketFactory;

import com.luoxudong.app.utils.LogUtil;

/** 
 * ClassName: JobsSSLSocketFactory
 * Description:使用自定义ssl协议证书
 * Create by 罗旭东
 * Date 2014-8-26 上午9:37:14
 */
public class JobsSSLSocketFactory extends SSLSocketFactory {
	private static final String TAG = JobsSSLSocketFactory.class.getSimpleName();
	
	/** ssl上下文*/
	private SSLContext mSslContext = SSLContext.getInstance("TLS");
	
	/** 对象实例 */
	private static JobsSSLSocketFactory sInstance = null;
	
	public static JobsSSLSocketFactory getInstance() {
		if (sInstance == null) {
			synchronized (JobsSSLSocketFactory.class) {// 放在if里面不需要每次都检测同步，提高效率
				if (sInstance == null) {
					InputStream caInput = null;
					try {
						caInput = JobsSSLSocketFactory.class.getResourceAsStream("certificate.cer");

						String keyStoreType = KeyStore.getDefaultType();
						KeyStore keyStore = KeyStore.getInstance(keyStoreType);
						keyStore.load(caInput, null);
						sInstance = new JobsSSLSocketFactory(keyStore);
					} catch (Exception e) {
						LogUtil.e(TAG, e.toString());
					} finally {
						if (caInput != null) {
							try {
								caInput.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}
		}
		
		return sInstance;
	}
	
	public JobsSSLSocketFactory(KeyStore truststore) throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException{
		super(truststore);

		TrustManager[] tms = createTrustManagers(truststore);

		mSslContext.init(null, tms, null);
	}

	private TrustManager[] createTrustManagers(KeyStore keystore)
			throws KeyStoreException, NoSuchAlgorithmException {
		if (keystore == null) {
			throw new IllegalArgumentException("Keystore may not be null");
		}
		TrustManagerFactory tmfactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmfactory.init(keystore);
		return tmfactory.getTrustManagers();
	}

	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return mSslContext.getSocketFactory().createSocket(socket,
				host, port, autoClose);
	}

	public Socket createSocket() throws IOException {
		return mSslContext.getSocketFactory().createSocket();
	}
}
