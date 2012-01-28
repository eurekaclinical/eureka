package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

abstract class AbstractWorker implements ServletWorker {

	Client getClient() throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager trustManager = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] inArg0,
					String inArg1) throws CertificateException {
				// nothing todo
			}

			@Override
			public void checkClientTrusted(X509Certificate[] inArg0,
					String inArg1) throws CertificateException {
				// nothing to do
			}
		};
		ClientConfig clientConfig = new DefaultClientConfig();
		SSLContext sslContext = SSLContext.getInstance("SSLv3");
		sslContext.init(null, new TrustManager[] { trustManager }, null);
		clientConfig.getProperties().put(
				HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(null, sslContext));
		return new Client().create(clientConfig);
	}
}
