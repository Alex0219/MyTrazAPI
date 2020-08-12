package de.fileinputstream.mytraz.worldmanagement.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UUIDFetcher {

    public static String getUUID(String username) {
        try {
            URL url = new URL("https://api.minetools.eu/uuid/" + username);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = reader.readLine();

            final JSONParser parser = new JSONParser();

            try {
                final JSONObject json = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));

                if (json.get("id") != null) {
                    String uuid = insertDashUUID(json.get("id").toString());

                    return uuid;
                }


            } catch (ParseException e) {

            }


        } catch (IOException e) {
        }
        return "";


    }

    public static String getName(String uuid) {
        try {
            URL url = new URL("https://api.minetools.eu/uuid/" + uuid);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line = reader.readLine();

            final JSONParser parser = new JSONParser();

            try {
                final JSONObject json = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));

                if (json.get("name") != null) {
                    String username = json.get("name").toString();

                    return username;
                }


            } catch (ParseException e) {

            }


        } catch (IOException e) {
        }
        return "";
    }

    public static String insertDashUUID(String uuid) {
        StringBuffer sb = new StringBuffer(uuid);
        sb.insert(8, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(13, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(18, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }

}
