package com.fds.opp.app.syncDatabase;

import com.fds.opp.app.model.WorkPackage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class WorkPackageSync {
    public static List<WorkPackage> getListWorkPackage () throws Exception{
        int offset=1;
        int count;
        List<WorkPackage> listWorkPackage = new ArrayList<>();
        do {
            String url = "http://localhost:8080/api/v3/work_packages/?offset=" + offset;
            String user = "apikey";
            String key = "86c12665ab843cb3f96690c7c53554adbfc95cf1544f0ece2519f269860488ab";
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
            System.out.println("Kết quả trả về file JSON ");
            System.out.println("Content- "+myResponse);
            count = Integer.parseInt(myResponse.get("count").toString());
            if (count!=0){
                offset++;
            }
            JSONObject _embedded = new JSONObject(myResponse.get("_embedded").toString());
            JSONArray _elements = new JSONArray(_embedded.get("elements").toString());

            for (int i=0; i<_elements.length(); i++){
                JSONObject jsonObject = _elements.getJSONObject(i);
                WorkPackage workPackage = new WorkPackage();
                workPackage.setIdWorkPackage(jsonObject.getInt("id"));
                workPackage.setNameWorkPackage(jsonObject.getString("subject"));
                JSONObject description = new JSONObject(jsonObject.get("description").toString());
                String descriptionDetail = "";
                if(description.get("raw").toString() == "null")
                    descriptionDetail = "";
                else
                    descriptionDetail=description.getString("raw");
                workPackage.setDescriptionWorkPackage(descriptionDetail);
                JSONObject links = new JSONObject(jsonObject.get("_links").toString());
                JSONObject priority = new JSONObject(links.get("priority").toString());
                workPackage.setPriorityWorkPackage(priority.getString("title"));
                JSONObject status = new JSONObject(links.get("status").toString());
                workPackage.setStatusWorkPackage(status.getString("title"));
                JSONObject type = new JSONObject(links.get("type").toString());
                workPackage.setTypeWorkPackage(type.get("title").toString());
                JSONObject project = new JSONObject(links.get("project").toString());
                workPackage.setNameProject(project.get("title").toString());
                JSONObject assigneeJS = new JSONObject(links.get("assignee").toString());
                String assignee = links.get("assignee").toString();
                String assigneeString = "";
                if(assignee.equals("{\"href\":null}")){
                    assigneeString = "null";
                    workPackage.setNameUser(assigneeString);
                } else {
                    workPackage.setNameUser(assigneeJS.get("title").toString());
                }
                JSONObject author = new JSONObject(links.get("author").toString());
                workPackage.setAuthor(author.getString("title"));
                JSONObject responsibleJS = new JSONObject(links.get("responsible").toString());
                String responsible = links.get("responsible").toString();
                String responsibleString = "";
                if(responsible.equals("{\"href\":null}")){
                    responsibleString = "null";
                } else {
                    responsibleString = responsibleJS.get("title").toString();
                }
                workPackage.setAccountable(responsibleString);
                if (!type.get("title").toString().equals("Milestone"))
                {
                    String startDateString = jsonObject.get("startDate").toString();
                    String dueDateString = jsonObject.get("dueDate").toString();
                    String deadlineDateString = "2021-02-20";
                    SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate, dueDate, deadlineDate;
                    if(startDateString == "null" || startDateString.equals(""))
                    {
                        startDate = null;
                        workPackage.setStartDate(startDate);
                    }
                    else
                    {
                        startDate = formatter1.parse(startDateString);
                        workPackage.setStartDate(startDate);
                    }
                    if (dueDateString == "null" || dueDateString.equals(""))
                    {
                        dueDate = null;
                        workPackage.setDueDate(dueDate);
                    }
                    else
                    {
                        dueDate = formatter1.parse(dueDateString);
                        workPackage.setDueDate(dueDate);
                    }
                    if (deadlineDateString == "null" || deadlineDateString.equals(""))
                    {
                        deadlineDate = null;
                        workPackage.setDeadlineDate(deadlineDate);
                    }
                    else
                    {
                        deadlineDate = formatter1.parse(deadlineDateString);
                        workPackage.setDeadlineDate(deadlineDate);
                    }
                }
                else
                {
                    String Date = jsonObject.getString("date");
                    SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = formatter1.parse(Date);
                    workPackage.setStartDate(startDate);
                    Date dueDate, deadlineDate;
                    dueDate = null;
                    workPackage.setDueDate(dueDate);
                    deadlineDate = null;
                    workPackage.setDeadlineDate(deadlineDate);
                }
                listWorkPackage.add(workPackage);
            }
        }
        while(count != 0);
        return listWorkPackage;
    }
}
