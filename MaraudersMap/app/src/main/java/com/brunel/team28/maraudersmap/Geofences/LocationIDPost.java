package com.brunel.team28.maraudersmap.Geofences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.brunel.team28.maraudersmap.Constants.values.BuldingID;
import static com.brunel.team28.maraudersmap.Constants.values.Username;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;
import static com.brunel.team28.maraudersmap.Constants.values.result;

/**
 * Created by ygimo on 28-Feb-17.
 */

public class LocationIDPost {
    public static void UserLocationIDPost() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("LocationID", BuldingID));
        nameValuePairs.add(new BasicNameValuePair("Username", Username));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.167.45:5306/GeofenceIDPost.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {

        }
        try {

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();

        } catch (Exception e) {

        }
    }
}
