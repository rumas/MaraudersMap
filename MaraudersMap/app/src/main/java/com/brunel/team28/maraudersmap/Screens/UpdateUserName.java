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

import static com.brunel.team28.maraudersmap.Constants.values.Fname;
import static com.brunel.team28.maraudersmap.Constants.values.UserID;
import static com.brunel.team28.maraudersmap.Constants.values.Username;
import static com.brunel.team28.maraudersmap.Constants.values.is;

public class UpdateUserName extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_name);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final EditText e_newFirstName = (EditText) findViewById(R.id.editText_editFirstName);
        final EditText e_newUserName = (EditText) findViewById(R.id.new_username);
        Button button_updateFirstName = (Button) findViewById((R.id.btn_addFirstName));
        button_updateFirstName.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Username = e_newUserName.getText().toString();
                Fname = e_newFirstName.getText().toString();
                addFirstName();
                updateUsername();
                myProfileCardClicked(null);

            }
        });
    }

    public void addFirstName() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("id", UserID));
        nameValuePairs.add(new BasicNameValuePair("newFirstName", Fname));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.166.18:5306/insertFirstName.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
        }
    }
    public void updateUsername() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("id", UserID));
        nameValuePairs.add(new BasicNameValuePair("newUserName", Username));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.166.18:5306/ChangingUserName.php");
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
