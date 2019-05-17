package com.brunel.team28.maraudersmap.UserCreate;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brunel.team28.maraudersmap.R;

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

import static com.brunel.team28.maraudersmap.Constants.values.Fname;
import static com.brunel.team28.maraudersmap.Constants.values.Lname;
import static com.brunel.team28.maraudersmap.Constants.values.Password;
import static com.brunel.team28.maraudersmap.Constants.values.Username;
import static com.brunel.team28.maraudersmap.Constants.values.code;
import static com.brunel.team28.maraudersmap.Constants.values.is;
import static com.brunel.team28.maraudersmap.Constants.values.line;
import static com.brunel.team28.maraudersmap.Constants.values.result;

/**
 * Created by ygimo on 10-Feb-17.
 */

public class UserCreation extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_creation);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		final EditText e_username = (EditText) findViewById(R.id.createuser);
		final EditText e_Fname = (EditText) findViewById(R.id.CreateFname);
		final EditText e_Lname = (EditText) findViewById(R.id.CreateLname);
		final EditText e_pwd = (EditText) findViewById(R.id.createPass);
		Button insert = (Button) findViewById(R.id.CreateAcc);


		insert.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Username = e_username.getText().toString();
				Fname = e_Fname.getText().toString();
				Lname = e_Lname.getText().toString();
				Password = e_pwd.getText().toString();
				insert();


			}
		});
	}

	public void insert() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("id", Username));
		nameValuePairs.add(new BasicNameValuePair("Fname", Fname));
		nameValuePairs.add(new BasicNameValuePair("Lname", Lname));
		nameValuePairs.add(new BasicNameValuePair("pwd", Password));

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://192.168.166.18:5306/insert.php");
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
				Toast.makeText(getBaseContext(), "Inserted Successfully",
						Toast.LENGTH_SHORT).show();
				usercreated(null);
			} else if (code == 2) {
				Toast.makeText(getBaseContext(), "the username is already taken.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getBaseContext(), "Sorry, Try Again a field must have been left blank",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {

		}
	}

	public void usercreated(View view) {
		setContentView(R.layout.user_creation);
		Intent Login = new Intent(this, com.brunel.team28.maraudersmap.Login.Login.class);
		startActivity(Login);
	}
}
