package com.brunel.team28.maraudersmap.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brunel.team28.maraudersmap.FriendScreen.FriendList;
import com.brunel.team28.maraudersmap.R;
import com.brunel.team28.maraudersmap.UserCreate.UserCreation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import static com.brunel.team28.maraudersmap.Constants.values.UserID;
import static com.brunel.team28.maraudersmap.Constants.values.UserStatus;
import static com.brunel.team28.maraudersmap.Constants.values.is;

public class UpdateUserStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_status);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final EditText e_newStatus = (EditText) findViewById(R.id.editText_newUserStatus);
        Button addStatus = (Button) findViewById((R.id.btn_addStatus));
        addStatus.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                UserStatus = e_newStatus.getText().toString();
                addStatus();
                myProfileCardClicked(null);

            }
        });
    }

    public void addStatus() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("id", UserID));
        nameValuePairs.add(new BasicNameValuePair("uSta", UserStatus));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.166.18:5306/insertUserStatus.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
        }
    }

    public void newuserclicked(View view) {
        // TODO This is where the intent will go, created by Yasser
        Intent myuserIntent = new Intent(this, UserCreation.class);
        startActivity(myuserIntent);
    }

    public void myProfileCardClicked(View view) {
        // TODO This is where the intent will go, created by Yasser
        Intent myProfileIntent = new Intent(this, MyProfile.class);
        startActivity(myProfileIntent);
    }

    public void addFriendsCardClicked(View view) {
        // TODO This is where the intent will go, created by Yasser
        Intent addFriendsIntent = new Intent(this, AddContacts.class);
        startActivity(addFriendsIntent);
    }

    public void myFriendsCardClicked(View view) {
        // TODO This is where the intent will go, created by Yasser
        Intent myFriendsIntent = new Intent(this, FriendList.class);
        startActivity(myFriendsIntent);
    }
}
