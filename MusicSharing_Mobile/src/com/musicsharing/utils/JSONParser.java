package com.musicsharing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This class is responsible for making calls to web service (REST-ful) and
 * returning result in JSON format.
 * 
 * @author Atul Mittal
 */
public class JSONParser {
	private InputStream mInputStream;

	private String mResponseJson = "";

	private int mTimeoutConnection = 10000;

	private int mTimeoutSocket = 10000;

	/**
	 * Get JSON String from the request URL. In case if any exceptions are
	 * thrown or invalid result is obtained (in formats other than JSON), return
	 * blank string and let caller handle from there.
	 * 
	 * @param argRestURL
	 *            Target web service (REST-ful) URL
	 * @return JSON response from the input URL
	 */
	public String getJSONFromUrl(String argRestURL) {
		// Making HTTP request
		try {
			// Set timeout parameters
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					mTimeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			HttpConnectionParams.setSoTimeout(httpParameters, mTimeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpGet httpGet = new HttpGet(argRestURL);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			mInputStream = httpEntity.getContent();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "";
		}

		// Parse the response to string
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(mInputStream,
					"iso-8859-1"), 8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			mInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		mResponseJson = sb.toString();

		return mResponseJson;

	}
}