package com.fds.opp.app.syncDatabase;

import com.fds.opp.app.controller.ReadConfig;
import com.fds.opp.app.model.MemberInProject;
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
public class MemberInProjectSync {
    public static List<MemberInProject> getListMemberInProject() throws Exception {
        int offset=1;
        List<MemberInProject> listMemberInProject = new ArrayList<>();
        int count;
        do {
            String url = ReadConfig.readKey("urlapimemberinproject") + offset;
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
//        con.setRequestProperty("Authorization","Basic :" + "YXBpa2V5OmFiMjdmNThlNWI1ZGVjNmFiMzRiMWE3OTMzZDkwMTkyZDBiM2QxOWU3ZGY4MGY4YTdkYjdjM2Y4NWNhNjQxN2M=");
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
            JSONObject myResponse = new JSONObject(response.toString());
            count = Integer.parseInt(myResponse.get("count").toString());
            if (count!=0){
                offset++;
            }
            JSONObject _embedded = new JSONObject(myResponse.get("_embedded").toString());
            JSONArray _elements = new JSONArray(_embedded.get("elements").toString());
            for (int i =0; i<_elements.length();i++) {
                JSONObject obj_i = _elements.getJSONObject(i);
                MemberInProject memberInProject = new MemberInProject();
                memberInProject.setIdMemberShip(obj_i.getInt("id"));
                JSONObject links = new JSONObject(obj_i.get("_links").toString());
                JSONArray roleList = new JSONArray(links.get("roles").toString());
                for(int j=0; j<roleList.length();j++)
                {
                    JSONObject obj_j = roleList.getJSONObject(j);
                    memberInProject.setRoles(obj_j.getString("title"));
                }
                JSONObject project = new JSONObject(links.get("project").toString());
                memberInProject.setNameProject(project.getString("title"));
                JSONObject principal = new JSONObject(links.get("principal").toString());
                memberInProject.setNameUser(principal.getString("title"));
                listMemberInProject.add(memberInProject);
            }
        } while(count != 0);
        return listMemberInProject;
    }

}
