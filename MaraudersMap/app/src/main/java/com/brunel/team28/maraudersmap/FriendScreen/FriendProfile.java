package com.brunel.team28.maraudersmap.FriendScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brunel.team28.maraudersmap.Geofences.FriendLocationIDFetch;
import com.brunel.team28.maraudersmap.R;

import static com.brunel.team28.maraudersmap.Constants.values.FriendBuldingID;
import static com.brunel.team28.maraudersmap.Constants.values.FriendFname;
import static com.brunel.team28.maraudersmap.Constants.values.FriendLname;
import static com.brunel.team28.maraudersmap.Constants.values.FriendName;


public class FriendProfile extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile);
        FriendFnameFetch.Fname();
        FriendLnameFetch.Lname();
        FriendLocationIDFetch.FriendLocationIDFetch();
        //FriendBuildingNameFetch.FriendBuildingNameFetch();

        final TextView Friendfname =(TextView) findViewById(R.id.NameOfYourFriend);
        final TextView Friendlname=(TextView) findViewById(R.id.LastNameOfYourFriend);
        final TextView FrienduserName=(TextView) findViewById(R.id.UsernameOfYourFriend);
        final TextView FriendLocation=(TextView) findViewById(R.id.currentLocationLabelFriend);
        final Button Update=(Button) findViewById(R.id.UpdateLocationFriend);

        Friendfname.setText(FriendFname);
        Friendlname.setText(FriendLname);
        FrienduserName.setText(FriendName);
        FriendLocation.setText("Currently at: "+FriendBuldingID);

        Update.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                FriendLocationIDFetch.FriendLocationIDFetch();
                UpdateClicked(null);

            }
        });
    }
    public void UpdateClicked(View view) {
        // TODO This is where the intent will go, created by Yasser
        Intent UpdateIntent = new Intent(this, FriendProfile.class);
        startActivity(UpdateIntent);
    }
}
