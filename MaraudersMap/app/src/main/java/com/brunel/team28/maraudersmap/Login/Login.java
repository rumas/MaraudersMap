package com.brunel.team28.maraudersmap.Login;

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
import com.brunel.team28.maraudersmap.Screens.AddContacts;
import com.brunel.team28.maraudersmap.Screens.MyProfile;
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

import static com.brunel.team28.maraudersmap.Constants.values.Password;
import static com.brunel.team28.maraudersmap.Constants.values.Username;
import static com.brunel.team28.maraudersmap.Constants.values.code;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;
import static com.brunel.team28.maraudersmap.Constants.values.result;

public class Login extends AppCompatActivity {

	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO Change to login screen

		setContentView(R.layout.login);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final EditText e_username = (EditText) findViewById(R.id.logUsername);
		final EditText e_pwd = (EditText) findViewById(R.id.logPass);
		Button login = (Button) findViewById(R.id.login);
		Button signup = (Button) findViewById((R.id.signup));
		login.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {

				Username = e_username.getText().toString();
				Password = e_pwd.getText().toString();
				idfetch.IDFetch();
				LnameFetch.Lname();
				FnameFetch.Fname();
				UserStatusFetch.UserStatusFetch();

				insert();

			}
		});
		signup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				createUser(null);
			}
		});
	}

	public void insert() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("id", Username));
		nameValuePairs.add(new BasicNameValuePair("pwd", Password));
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://192.168.166.18:5306/Login.php");
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
			if (code == 0) {
				Toast.makeText(getBaseContext(), "Username or Password are incorrect",
						Toast.LENGTH_SHORT).show();
			} else {
				setContentView(R.layout.main_menu);
			}
		} catch (Exception e) {

		}
	}

	public void createUser(View view) {
		setContentView(R.layout.user_creation);
		Intent create = new Intent(this, UserCreation.class);
		startActivity(create);
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
