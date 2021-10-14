package com.company;
import java.util.Scanner; // For user input
import java.net.URL; // For  checking if URL is valid
import java.util.concurrent.ThreadLocalRandom;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

class UrlCheck {
    // Returns true if valid URL
    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI(); // Trying to create valid URL
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
/* Class for performing various json operations */
class jsonOperations {
    public static void writeToJson(String filename, JSONArray jsonObj) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonObj.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray readFromJson(String filename) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            Object obj = jsonParser.parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
    public static void parseUrlObject(JSONObject url){
        JSONObject urlObject = (JSONObject) url.get("link");
    }
}
@SuppressWarnings("unchecked")
public class Main {
    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	boolean running = true;
	JSONArray urlList = jsonOperations.readFromJson("urls.json");
        while (running) {
        System.out.println("Enter command(type help for list of commands): ");
        String cmd = in.nextLine(); // Cmd to be entered
        String[] cmd_split = cmd.split(" "); // splitting the string in order to process the commands
        // help command
        if (cmd_split[0].toLowerCase().equals("help")) {
            System.out.println("storeurl <url> : Stores <url> into JSON file with a unique short key.\nget <url> :" +
                    "Returns the unique key of stored url and increases usage count.\ncount <url> : returns current " +
                    "usage count of url.\nlist : Lists all urls with their counts.\nexit : Terminate program.");
        }
        // storeurl command
        else if (cmd_split[0].toLowerCase().equals("storeurl")) {
            if (cmd_split.length != 2){
                System.out.println("Invalid Syntax: storeurl <url>");
            }
            else {
                String url = cmd_split[1];
                // Check Validity of URL before storing
                boolean protocolPresent = url.contains("http") || url.contains("ftp");
                if (!protocolPresent) {
                    url = "http://" + url;
                }
                if (UrlCheck.isValid(url)) {
                    int urlID = ThreadLocalRandom.current().nextInt(0, 100000 + 1); // generate ID for url
                    int length = urlList.size();
                    boolean duplicate = false;
                    for(int i=0; i < length; i++)
                    {
                        JSONObject urlObj = (JSONObject) urlList.get(i);
                        JSONObject urlObj2 = (JSONObject) urlObj.get("link");
                        if(urlObj2.get("url").equals(url)){
                            System.out.println("URL already exists!");
                            duplicate = true;
                            break;
                        }
                    }
                    if (!duplicate) {
                        JSONObject urlData = new JSONObject();
                        urlData.put("url", url);
                        urlData.put("urlID", urlID);
                        urlData.put("count", 0);

                        JSONObject urlObject = new JSONObject();
                        urlObject.put("link", urlData);
                        urlList.add(urlObject);
                        jsonOperations.writeToJson("urls.json", urlList);
                        System.out.println("URL stored!");
                    }
                }
                else {
                    System.out.println("Invalid URL.");
                }
            }
        }
        else if (cmd_split[0].toLowerCase().equals("get"))
        {
            if (cmd_split.length != 2){
                System.out.println("Invalid Syntax: get <url>");
            }
            String url = cmd_split[1];
            // Check Validity of URL before storing
            boolean protocolPresent = url.contains("http") || url.contains("ftp");
            if (!protocolPresent) {
                url = "http://" + url;
            }
            int length = urlList.size();
            for(int i=0; i < length; i++)
            {
                JSONObject urlObj = (JSONObject) urlList.get(i);
                JSONObject urlObj2 = (JSONObject) urlObj.get("link");
                if(urlObj2.get("url").equals(url)){
                    urlObj2.put("count",(long) urlObj2.get("count") + 1);
                    System.out.println("ID:" + urlObj2.get("urlID"));
                }
                jsonOperations.writeToJson("urls.json", urlList);
            }
        }
        else if (cmd_split[0].toLowerCase().equals("count"))
        {
            if (cmd_split.length != 2){
                System.out.println("Invalid Syntax: count <url>");
            }
            String url = cmd_split[1];
            // Check Validity of URL before storing
            boolean protocolPresent = url.contains("http") || url.contains("ftp");
            if (!protocolPresent) {
                url = "http://" + url;
            }
            int length = urlList.size();
            for(int i=0; i < length; i++)
            {
                JSONObject urlObj = (JSONObject) urlList.get(i);
                JSONObject urlObj2 = (JSONObject) urlObj.get("link");
                if(urlObj2.get("url").equals(url)) {
                    System.out.println("Count:" + urlObj2.get("count"));
                }
            }
        }
        else if (cmd_split[0].toLowerCase().equals("list")){
            int length = urlList.size();
            for(int i=0; i < length; i++) {
                JSONObject urlObj = (JSONObject) urlList.get(i);
                JSONObject urlObj2 = (JSONObject) urlObj.get("link");
                System.out.println(urlObj2);
                }
            }
        else if (cmd_split[0].toLowerCase().equals("exit")) {
            running = false;
        }
    }
    }
}


