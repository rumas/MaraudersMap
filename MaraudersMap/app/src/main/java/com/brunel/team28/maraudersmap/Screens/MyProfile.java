package com.brunel.team28.maraudersmap.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brunel.team28.maraudersmap.R;

import static com.brunel.team28.maraudersmap.Constants.values.BuldingID;
import static com.brunel.team28.maraudersmap.Constants.values.Fname;
import static com.brunel.team28.maraudersmap.Constants.values.Lname;
import static com.brunel.team28.maraudersmap.Constants.values.UserStatus;
import static com.brunel.team28.maraudersmap.Constants.values.Username;

public class MyProfile extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);

		final TextView fname = (TextView) findViewById(R.id.textView6);
		final TextView lname = (TextView) findViewById(R.id.textView7);
		final TextView userName = (TextView) findViewById(R.id.textView4);
		final TextView currentLocation = (TextView) findViewById(R.id.currentLocationLabel);
		final TextView userStatus = (TextView) findViewById(R.id.textView_userStatus);
		final Button button_updateStatus = (Button) findViewById(R.id.button3);
		final Button button_EditName = (Button) findViewById(R.id.button2);
		final Button button_Delete = (Button) findViewById(R.id.button5);


		button_updateStatus.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					openStatusUpdateView(null);
		}
	});
		button_EditName.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openUpdateFirstName(null);
			}
		});
		button_Delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userDeletionPage.Deletion();
				loginPage(null);
			}
		});

		fname.setText(Fname);
		lname.setText(Lname);
		userName.setText(Username);
		userStatus.setText(UserStatus);
		currentLocation.setText(BuldingID);
	}
	public void openStatusUpdateView(View view) {
		// TODO This is where the intent will go, created by Yasser
		Intent userStatusIntent = new Intent(this, UpdateUserStatus.class);
		startActivity(userStatusIntent);

	}
	public void loginPage (View view) {
		Intent Login = new Intent(this, com.brunel.team28.maraudersmap.Login.Login.class);
		startActivity(Login);
	}

	public void openUpdateFirstName(View view) {
		// TODO This is where the intent will go, created by Yasser
		Intent firstNameIntent = new Intent(this, UpdateUserName.class);
		startActivity(firstNameIntent);
	}

}
