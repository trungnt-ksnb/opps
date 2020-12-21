package com.fds.opp.app.controller;


import java.io.IOException;
import java.io.InputStream;
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
}
