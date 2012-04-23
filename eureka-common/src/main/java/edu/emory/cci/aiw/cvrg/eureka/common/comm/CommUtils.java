package edu.emory.cci.aiw.cvrg.eureka.common.comm;

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
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * Utility methods related to dealing with communication between different
 * layers of the application.
 *
 * @author hrathod
 *
 */
public final class CommUtils {

	/**
	 * A private default constructor, to disallow instantiation of this class.
	 */
	private CommUtils() {
		// do not allow instantiation of this class.
	}

	/**
	 * Get a Jersey client capable of making HTTPS requests. NOTE: This method
	 * returns a client with a trust manager that trusts all certificates. This
	 * is very bad form. This should be removed as soon as possible (when a real
	 * certificate is available for the machine).
	 *
	 * @return A Jersey client capable of making SSL requests.
	 * @throws KeyManagementException Thrown when the rest client can not be
	 *             configured for SSL correctly.
	 * @throws NoSuchAlgorithmException Thrown when the rest client can not be
	 *             configured for SSL correctly.
	 */
	public static Client getClient() throws KeyManagementException,
			NoSuchAlgorithmException {
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
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		SSLContext sslContext = SSLContext.getInstance("SSLv3");
		sslContext.init(null, new TrustManager[] { trustManager }, null);
		clientConfig.getProperties().put(
				HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(null, sslContext));
		return Client.create(clientConfig);
	}

}
