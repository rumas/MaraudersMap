package com.brunel.team28.maraudersmap.FriendScreen;

import android.util.Log;

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

import static com.brunel.team28.maraudersmap.Constants.values.FriendID;
import static com.brunel.team28.maraudersmap.Constants.values.FriendIDArrayvalues;
import static com.brunel.team28.maraudersmap.Constants.values.FriendUsernameArray;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;

/**
 * Created by ygimo on 25-Feb-17.
 */

public class FriendUsernameID {
    public static void UsernameArrayFetch() {
        String enditpls="";
        for (int i = 0; i < FriendIDArrayvalues.length; i++) {
            String Temp;
            FriendID = FriendIDArrayvalues[i];
            Log.e("FriendID",FriendID);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("id", FriendID));


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://192.168.166.18:5306/FriendUsernameArray.php");
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
                    sb.append(line );
                }
                is.close();
                    Temp = sb.toString()+",";
                Log.d("build11",Temp);
                enditpls = Temp + enditpls;

                    Log.d("build",enditpls);
            }
            catch (Exception e) {

            }
        }
        FriendUsernameArray=enditpls.split(",");
        for(int i=0;i<FriendUsernameArray.length;i++){
            Log.d("KillmePLS",FriendUsernameArray[i]);
        }
    }
    }

