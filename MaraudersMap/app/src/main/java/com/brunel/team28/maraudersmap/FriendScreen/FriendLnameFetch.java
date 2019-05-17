package com.brunel.team28.maraudersmap.FriendScreen;

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

import static com.brunel.team28.maraudersmap.Constants.values.FriendLname;
import static com.brunel.team28.maraudersmap.Constants.values.FriendName;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;

/**
 * Created by ygimo on 25-Feb-17.
 */

public class FriendLnameFetch {
    public static void Lname()
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("id",FriendName));

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.166.18/FriendLnameFetch.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        }
        catch(Exception e)
        {

        }

        try
        {

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"UTF-8"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            FriendLname = sb.toString();

        }
        catch(Exception e)
        {

        }
    }
}
