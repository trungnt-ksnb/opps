package com.fds.opp.app.syncDatabase;

import com.fds.opp.app.model.Project;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProjectSync {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/api/v3/projects/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
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
        System.out.println("Kết quả trả về file JSON ");
        System.out.println("Content- "+myResponse);
        System.out.println("Lấy dữ liệu _embedded" + myResponse.get("_embedded"));
        JSONObject _embedded = new JSONObject(myResponse.get("_embedded").toString());
        System.out.println("Lấy dữ liệu elements"+ _embedded.get("elements"));
        JSONArray _elements = new JSONArray(_embedded.get("elements").toString());
        List<Project> listProject = new ArrayList<>();
        for (int i =0; i<_elements.length();i++){
            JSONObject obj_i = _elements.getJSONObject(i);
            Project p = new Project();
            p.setIdProject(obj_i.getInt("id"));
            p.setNameProject(obj_i.getString("name"));
            JSONObject description = new JSONObject(obj_i.get("description").toString());
            p.setDescriptionProject(description.get("raw").toString());
            System.out.println("Object p : " + p);
            listProject.add(p);
        }
        for (Project project : listProject) {
            System.out.println(project);
        }
    }

}

