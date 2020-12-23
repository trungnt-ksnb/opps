package com.fds.opp.app.syncDatabase;

import com.fds.opp.app.controller.ReadConfig;
import com.fds.opp.app.model.Project;
import com.fds.opp.app.model.WorkPackage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ProjectSync {

    public static List<Project> getListProjectFromAPI() throws Exception {
//        int offset=1;
//        int count;
        List<Project> listProject = new ArrayList<>();
//        do {
            String url = ReadConfig.readKey("urlapiproject");
            String user = ReadConfig.readKey("userapi");
            String key = ReadConfig.readKey("keyapi");
            String auth = user + ":" + key;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Authorization","Basic :" +  encodedAuth);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            JSONObject myResponse = new JSONObject(response.toString());
//            count = Integer.parseInt(myResponse.get("count").toString());
//            if (count!=0){
//                offset++;
//            }
            System.out.println("Kết quả trả về file JSON ");
            System.out.println("Content- "+myResponse);
            JSONObject _embedded = new JSONObject(myResponse.get("_embedded").toString());
            JSONArray _elements = new JSONArray(_embedded.get("elements").toString());
            for (int i =0; i<_elements.length();i++){
                JSONObject obj_i = _elements.getJSONObject(i);
                Project p = new Project();
                p.setIdProject(obj_i.getInt("id"));
                p.setNameProject(obj_i.getString("name"));
                JSONObject description = new JSONObject(obj_i.get("description").toString());
                p.setDescriptionProject(description.get("raw").toString());
                listProject.add(p);
            }
//        } while(count != 0);
        return listProject;
    }
}

