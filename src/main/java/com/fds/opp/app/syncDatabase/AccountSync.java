package com.fds.opp.app.syncDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fds.opp.app.model.Account;

public class AccountSync {
	
//	public static void main(String[] args) {
//		try {
//			AccountSync.getListAccountFromAPI();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	public static List<Account> getListAccountFromAPI() throws Exception{
		// TODO Auto-generated method stub
		String url = "http://localhost:8080/api/v3/users/";
		String user = "apikey";
		String key = "e8248254b63b490d8c0fb7a16887cfae3293976cf3fc619c42e33463574d5e39";
		String auth = "";
		String encodeAuth = "";
		if(user != null && key != null) {
			auth = user + ":" +key;
		}
		if(auth != null) {
			encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		}
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		
		// get
		connection.setRequestMethod("GET");
		// add request  header
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("Authorization","Basic :" + encodeAuth);
		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'Get' request to URL: " + url);
		System.out.println("response code : " + responseCode);
		
		// khởi tạo đọc file
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		// in tất cả thông tin user ra console voi toString
		System.out.println(response.toString());
		
		// khởi tạo và lưu thông tin các user dưới json 
		JSONObject myResponse = new JSONObject(response.toString());
		System.out.println(myResponse);
		// Lấy dữ liệu từ embedded 
		System.out.println("Dữ liệu embedded: " + myResponse.get("_embedded"));
		JSONObject Embedded = new JSONObject(myResponse.get("_embedded").toString());
		// lấy elements
		System.out.println(Embedded);
		System.out.println("Dữ liệu elements: " + Embedded.get("elements"));
		JSONArray Elements = new JSONArray(Embedded.get("elements").toString());
		System.out.println("----------------------------------");
		List<Account> accList = new ArrayList();
		for(int i=0;i<Elements.length();i++) {
			JSONObject obj_i = Elements.getJSONObject(i);
			Account account = new Account();
			account.setIdUser(obj_i.getInt("id"));
			account.setFirstname(obj_i.getString("firstName"));
			account.setLastname(obj_i.getString("lastName"));
			account.setFullname(obj_i.getString("firstName") + obj_i.getString("lastName"));
			account.setUsername(obj_i.getString("name"));
			account.setEmail(obj_i.getString("email"));
			account.setStatus(obj_i.getString("status"));
			account.setBotId(null);
			account.setCustomField(null);
			account.setIsAdmin(obj_i.getBoolean("admin"));
			accList.add(account);
		}
//		for(Account Account : accList) {
//			System.out.println(Account);
//		}
		return accList;
		
	}
}
