package org.example;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DataExchange {
    private static final Logger logger = LoggerFactory.getLogger(DataExchange.class);

    private Config config = Config.getInstance();

    private CurrentSetup cs = CurrentSetup.getInstance();



    //sending get requests,put it into StringBuffer
    public StringBuffer sendreq(URL url){
        try{
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            logger.debug("Response Status: " + status + "\n" + "Server obtained,getting data...");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();


            return content;
        }
        catch(Exception e){
            logger.error(e.toString());
        }
        return new StringBuffer("NoBitches");
    }

    public Map<String,String> cl() {
        try {
            URL url = new URL(String.format("http://%s:5001/char_list",config.getIP()));

            StringBuffer content = sendreq(url);

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return new Gson().fromJson(content.toString(), type);
        } catch (Exception e) {
            logger.error("Server hosts no backend or No route to the host");
        }
        return null;
    }


    public Map<String, Object> msgbuff() {
        try {
            URL url = new URL(String.format("http://%s:5001/msg_buffer", config.getIP()));

            StringBuffer content = sendreq(url);


            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> resultMap = new Gson().fromJson(content.toString(), type);

            return resultMap;

        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }


    //obtain server info
    public String neofetch() {
        try {
            URL url = new URL(String.format("http://%s:5001/neofetch", config.getIP()));

            StringBuffer content = sendreq(url);

            return content.toString();

        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }



    // xyz - data xnxyxz - keys
    public void jsoCreate(String x,String y,String z,String xn,String xy,String xz,String endpoint){
        Map<String,String> xyz = new HashMap<>(){{
            String[] tempx = new String[] {x,y,z};
            String[] tempy = new String[] {xn,xy,xz};
            for (int tx = 0;tx<tempx.length;tx++){
                if (!tempx[tx].equals("null")){   //ignoring null items
                    put(tempx[tx],tempy[tx]);
                }
            }

        }};
        JSONObject json = new JSONObject();
        for (String key : (Set<String>) xyz.keySet()){
            logger.debug(key);
            logger.debug(xyz.get(key));
            json.put(xyz.get(key),key);
        }
        chatEX(endpoint,json);
    }

    public void chatEX(String endpoint,JSONObject json) {
        try {
            String engine = cs.getEngine();
           logger.debug(String.format("Engine: %s", engine));
            URL url = new URL(String.format("http://%s:5001/%s", config.getIP(),endpoint));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            String jsonInputString = json.toString();

            logger.debug("JSON Input: " + jsonInputString);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = con.getResponseCode();
            logger.debug(String.format("EXCHANGE_STATUS: %s", status));

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

}

