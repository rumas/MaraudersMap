package com.brunel.team28.maraudersmap.FriendScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brunel.team28.maraudersmap.R;

import static com.brunel.team28.maraudersmap.Constants.values.FriendName;
import static com.brunel.team28.maraudersmap.Constants.values.FriendUsernameArray;

public class FriendList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        FriendIDArray.IDArrayFetch();
        FriendUsernameID.UsernameArrayFetch();
        populateListView();
        registerClickCallback();

    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.testlist, FriendUsernameArray);

        ListView list = (ListView) findViewById(R.id.FriendList);
        list.setAdapter(adapter);
    }
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.FriendList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                FriendName = textView.getText().toString();



                ViewFriendIntent(null);

            }
        });

    }
    public void ViewFriendIntent(View view){
        // TODO This is where the intent will go, created by Yasser
        Intent ViewFriendIntent = new Intent(this, FriendProfile.class);
        startActivity(ViewFriendIntent);
    }
}