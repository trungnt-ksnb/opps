package com.fds.opp.app.syncDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fds.opp.app.controller.ReadConfig;
import com.fds.opp.app.model.WorkPackage;
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
		int offset=1;
		int count;
		List<Account> accList = new ArrayList<>();
		do {
			// TODO Auto-generated method stub
			String url = ReadConfig.readKey("urlapiaccount") + offset;
			String user = ReadConfig.readKey("userapi");
			String key = ReadConfig.readKey("keyapi");
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
			// khởi tạo và lưu thông tin các user dưới json
			JSONObject myResponse = new JSONObject(response.toString());
			count = Integer.parseInt(myResponse.get("count").toString());
			if (count!=0){
				offset++;
			}
			JSONObject Embedded = new JSONObject(myResponse.get("_embedded").toString());
			JSONArray Elements = new JSONArray(Embedded.get("elements").toString());

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
		}
		while(count != 0);
		return accList;
		
	}
}
