package com.fds.opp.app.controller;


import com.fds.opp.app.model.Account;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

public class ReadConfig {
    public static String readKey(String key) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = "application.properties";
            input = ReadConfig.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return "";
            }

            // load a properties file from class path, inside static method
            prop.load(input);

            return prop.getProperty(key);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    @Test
    public void WriteConfig() throws IOException {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = "account.properties";
            input = ReadConfig.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
            }
            prop.load(input);
//            if(prop.getProperty("Hello") == null){
                OutputStream output = null;
                output = new FileOutputStream("account.properties");
                prop.setProperty("4", "1326814419");
                prop.store(output, "Ngọc Ngu như chó");
//            }


        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
