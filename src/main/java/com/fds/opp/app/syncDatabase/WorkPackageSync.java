package com.fds.opp.app.syncDatabase;

import com.fds.opp.app.model.WorkPackage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkPackageSync {
    public static List<WorkPackage> getListWorkPackage () throws Exception{
        String url = "http://localhost:8080/api/v3/work_packages/";
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
        JSONArray _elements = new JSONArray(_embedded.get("elements").toString());
        List<WorkPackage> listWorkPackage = new ArrayList<>();
        for (int i=0; i<_elements.length(); i++){
            JSONObject jsonObject = _elements.getJSONObject(i);
            System.out.println("jSOn Object " + jsonObject);
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
            workPackage.setTypeWorkPackage(type.getString("title"));
            JSONObject project = new JSONObject(links.get("project").toString());
            workPackage.setNameProject(project.getString("title"));
            JSONObject assignee = new JSONObject(links.get("assignee").toString());
            workPackage.setNameUser(assignee.getString("title"));
            JSONObject author = new JSONObject(links.get("author").toString());
            workPackage.setAuthor(author.getString("title"));

            if (type.getString("title").equals("Milestone") == false )
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
        return listWorkPackage;
    }
}
