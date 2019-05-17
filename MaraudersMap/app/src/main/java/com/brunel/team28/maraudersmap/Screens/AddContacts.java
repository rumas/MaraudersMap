package com.brunel.team28.maraudersmap.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.brunel.team28.maraudersmap.Constants.values.FriendID;
import static com.brunel.team28.maraudersmap.Constants.values.FriendName;
import static com.brunel.team28.maraudersmap.Constants.values.UserID;
import static com.brunel.team28.maraudersmap.Constants.values.code;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;
import static com.brunel.team28.maraudersmap.Constants.values.result;

public class AddContacts extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final EditText e_Fusername = (EditText) findViewById(R.id.friendUsername);
		Button add = (Button) findViewById((R.id.addFriendButton));
		add.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {

				FriendName = e_Fusername.getText().toString();
				FriendIDFetch.IDFetch();
				addcontact();

			}
		});
	}

	public void addcontact() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("id", UserID));
		nameValuePairs.add(new BasicNameValuePair("fid", FriendID));
		nameValuePairs.add(new BasicNameValuePair("fna", FriendName));

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://192.168.166.18:5306/AddContact.php");
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

		try {
			JSONObject json_data = new JSONObject(result);
			code = (json_data.getInt("code"));
			if (code == 1) {
				Toast.makeText(getBaseContext(), "Friend Inserted Successfully",
						Toast.LENGTH_SHORT).show();
				setContentView(R.layout.main_menu);
			} else if (code == 2) {
				Toast.makeText(getBaseContext(), "you are already friends with that user",
						Toast.LENGTH_LONG).show();
			} else if (code == 0) {
				Toast.makeText(getBaseContext(), "please insert a username",
						Toast.LENGTH_LONG).show();
			} else if (code == 3) {
				Toast.makeText(getBaseContext(), "please insert a valid username",
						Toast.LENGTH_LONG).show();
			}
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
