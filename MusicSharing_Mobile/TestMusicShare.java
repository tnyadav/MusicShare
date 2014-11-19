package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestMusicShare {

	/**
	 * Method takes the data for post call and the request url.
	 * 
	 * @param postInput
	 * @param requestUrl
	 * @return JSONObject
	 */
	public static JSONObject postHttpUrlConnection(String postInput,
			String requestUrl) {

		JSONObject responseObject = null;
		InputStream in = null;
		HttpURLConnection conn = null;
		String jsonString = null;
		try {
			URL url = new URL(requestUrl);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			conn.setRequestProperty("Content-Length", "" + postInput.length());
			String input = postInput;

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("Inside Error" + conn.getResponseCode());

				System.out.println("Inside Error " + conn.getResponseMessage());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			in = new BufferedInputStream(conn.getInputStream());
			jsonString = getStringFromInputStream(in);
			System.out.println("JSON Response : " + jsonString);
			responseObject = new JSONObject(jsonString);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (RuntimeException e) {
			jsonString = "{\"message\":\"Internal server error\",\"statusCode\":\"err001\"}";
			try {
				responseObject = new JSONObject(jsonString);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseObject;

	}

	/**
	 * Method returns String from input stream.
	 * 
	 * @param is
	 * @return jsonString
	 */
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static void main(String s[]) {
		//createUser();
		//addConnection();
		//updateConnectionStatus();
		addToMyLibrary();
	}

	public static void createUser() {
		UserMusicShare user = new UserMusicShare();
		user.mobileNumber = "919999223388";
		user.name = "Nitesh 2";
		user.password = "test";
		JSONObject json = new JSONObject(user);
		postHttpUrlConnection(json.toString(),
				"http://localhost:8080/MusicShare/rest/musicshare-service/registerUser");
	}

	public static void addConnection() {

		JSONObject json = new JSONObject();
		try {
			json.put("fromUserId", 1);
			json.put("toUserId", 3);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postHttpUrlConnection(json.toString(),
				"http://localhost:8080/MusicShare/rest/musicshare-service/add-connection");
	}

	public static void updateConnectionStatus() {

		JSONObject json = new JSONObject();
		try {
			json.put("requesterUserId", 1); //this user has sent an connection request
			json.put("userId", 3); // this user is accepting the request
			json.put("connectionStatus", "ACCEPTED"); //ACCEPTED/REJECTED

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postHttpUrlConnection(
				json.toString(),
				"http://localhost:8080/MusicShare/rest/musicshare-service/update-connection-status");
	}

	public static void addToMyLibrary() {

		JSONArray libArray = new JSONArray();

	
		try {
			JSONObject json = new JSONObject();
			json.put("name", "New Song"); //this user has sent an connection request
			json.put("fileName", "/nitesh/folder1/folder2/folder3/mp4");// this user is accepting the request
			libArray.put(0, json);
			json = new JSONObject();
			json.put("name", "New Song 1"); //this user has sent an connection request
			json.put("fileName", "/nitesh/folder1/folder2/folde33/mp5");// this user is accepting the request
			libArray.put(1, json);

			JSONObject json1 = new JSONObject();
			json1.put("userId", 1);
			json1.put("userLibraryDTOList", libArray);
			postHttpUrlConnection(
					json1.toString(),
					"http://localhost:8080/MusicShare/rest/musicshare-service/add-to-mylibrary");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
