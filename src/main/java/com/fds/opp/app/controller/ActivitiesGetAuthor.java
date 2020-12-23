package com.fds.opp.app.controller;

import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.model.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ActivitiesGetAuthor {

    public static String getAuthor(int idWorkPackage) throws Exception{
        Account accountResult = new Account();
            String url = ReadConfig.readKey("urlapiactivites") + idWorkPackage + "/activities";
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
            JSONObject _embedded = new JSONObject(myResponse.get("_embedded").toString());
            JSONArray _elements = new JSONArray(_embedded.get("elements").toString());
            int element_length = _elements.length();
            JSONObject lastVersion = _elements.getJSONObject(element_length -1);
            JSONObject _links = new JSONObject(lastVersion.get("_links").toString());
            JSONObject userJS = new JSONObject(_links.get("user").toString());
            String hrefUserJS = userJS.get("href").toString();
            System.out.println(hrefUserJS);
            String idUser = hrefUserJS.substring(14);
            int integerIdUser = Integer.parseInt(idUser);
            System.out.println(integerIdUser+1);
            System.out.println(idUser);
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            accountResult = session.get(Account.class, integerIdUser);
        return accountResult.getUsername();
    }
    @Test
    public void test(){

    }
}
