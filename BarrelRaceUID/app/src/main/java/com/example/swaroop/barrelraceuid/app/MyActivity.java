package com.example.swaroop.barrelraceuid.app;

//Import Dependencies
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import android.support.v7.app.ActionBarActivity;
import android.R.color;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

///////////////////////////////////////////////////////////////////////////////
//  File        : MyActivity.java
//	Course		: CS 6301-015 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Debabrat Mohanty
//  NetId       : dxm141630
//  Description : Class to for home screen implementation
//  Date        : 11/15/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : MyActivity
//  Access      : Public
//  Description : Contains the implementation of homescreen and the call 
//				  to other screen
///////////////////////////////////////////////////////////////////////////////
public class MyActivity extends ActionBarActivity implements OnClickListener {

	//Member variable to be used across class
    private ImageButton highscore;
    private ImageButton settingsBtn;
    private ImageButton startBtn;
    
	//Member variable to be used across package
	public static DataHandler m_objDataHandler;
	public static boolean bGameSound = true;
    public static boolean bGameFarm = true;
    
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreate
    //  Paramters       : Bundle
    //  Returns         : None
    //  Description     : Overriden On create method initialize the click listener
	//					  for all the image buttons
    ////////////////////////////////////////////////////////////////////////////
	@Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        m_objDataHandler = new DataHandler();
        m_objDataHandler.initDataHandler();

        startBtn = (ImageButton) findViewById(R.id.start_button);
        // startBtn.setBackgroundDrawable(R.drawable.ic_start_button);
        startBtn.setOnClickListener(this);

        highscore = (ImageButton) findViewById(R.id.highscore);
        highscore.setOnClickListener(this);

        settingsBtn = (ImageButton) findViewById(R.id.settings_button);
        settingsBtn.setOnClickListener(this);
        Dialog dialog = new Dialog(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout);

        try {
            Drawable d = Drawable.createFromStream(
                    getAssets().open("background.png"), null);
            layout.setBackgroundColor(color.holo_blue_bright);
            layout.setBackgroundDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onStop
    //  Paramters       : None
    //  Returns         : None
    //  Description     : Overriden On stop
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();

    }

	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onOptionsItemSelected
    //  Paramters       : MenuItem
    //  Returns         : boolean
    //  Description     : Overriden On onOptionsItemSelected 
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onClick
    //  Paramters       : MenuItem
    //  Returns         : None
    //  Description     : Overriden On onClick will correspond to the image buttons 
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start_button:
                Intent intent = new Intent(this, Game.class);
                startActivity(intent);
                break;

            case R.id.highscore:
                if(MyActivity.m_objDataHandler.getListSize() == 0){
                    Toast.makeText(getApplicationContext(), "No Scores Available!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intentHighScore = new Intent(this, HighScores.class);
                startActivity(intentHighScore);
                break;

            case R.id.settings_button:
                Intent intentSettings = new Intent(this, Settings.class);
                startActivity(intentSettings);
                break;
        }

    }
}
