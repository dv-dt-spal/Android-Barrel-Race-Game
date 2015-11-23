package com.example.swaroop.barrelraceuid.app;

//Import dependencies
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

///////////////////////////////////////////////////////////////////////////////
//  File        : Settings.java
//	Course		: CS 6301-015 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Debabrat Mohanty
//  NetId       : dxm141630
//  Description : Class to for settings screen implementation
//  Date        : 11/21/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : MyActivity
//  Access      : Public
//  Description : Contains the implementation of settings to be reflected in the game
///////////////////////////////////////////////////////////////////////////////
public class Settings extends ActionBarActivity {

	
    protected static final String SETTINGS = null;
    
	//Member Variables to be used for changing the settings
    static int game_sound = 1;
    static int game_bg = 1;
	static final int[] game_sound_items_values = new int[]{1,0};
    static final int[] game_bg_items_values = new int[]{1,0};


	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreate
    //  Paramters       : Bundle
    //  Returns         : None
    //  Description     : Overriden On create method initialize the settings menu
	//					  for all the image buttons
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Setting file to store selected values
        SharedPreferences settings = getSharedPreferences(SETTINGS, 0);

        //Game Sounds Menu
        final Spinner game_sound_dd = (Spinner) findViewById(R.id.game_sound_settings);
        String[] game_sound_setting = new String[]{"On","Off"};
        ArrayAdapter<String> adapterSound = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,game_sound_setting);
        game_sound_dd.setAdapter(adapterSound);
        game_sound_dd.setSelection(settings.getInt("game_sound", 0));


        //Game BG Menu
        final Spinner game_bg_dd = (Spinner) findViewById(R.id.game_bg_settings);
        String[] game_bg_setting = new String[]{"Farm","Desert"};
        ArrayAdapter<String> adapterbg = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,game_bg_setting);
        game_bg_dd.setAdapter(adapterbg);
        game_bg_dd.setSelection(settings.getInt("game_bg", 0));

        Button okBtn = (Button) findViewById(R.id.ok_button);
        okBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V) {

				//Check for the settings selected and the update the value so that it is refelcted across
                game_sound = game_sound_items_values[game_sound_dd.getSelectedItemPosition()];

                if(game_sound == 0)
                {
                    MyActivity.bGameSound = false;
                }
                else
                {
                    MyActivity.bGameSound = true;
                }

                game_bg = game_bg_items_values[game_bg_dd.getSelectedItemPosition()];

                if(game_bg == 0)
                {
                    MyActivity.bGameFarm = false;
                }
                else
                {
                    MyActivity.bGameFarm = true;
                }

                //Store selected value in Settings file so that it can be used while next time loading the app
                SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
                SharedPreferences.Editor editor = settings.edit();

                /*editor.putInt("ball_color", ball_color_dd.getSelectedItemPosition());
                editor.putInt("barrel_color", barrel_color_dd.getSelectedItemPosition());
                editor.putInt("field_color", field_color_dd.getSelectedItemPosition());*/

                editor.putInt("game_sound", game_sound_dd.getSelectedItemPosition());
                editor.putInt("game_bg", game_bg_dd.getSelectedItemPosition());


                // Commit the edits!
                editor.commit();
                finish();
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View V) {
                finish();
            }
        });

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onPause
    //  Paramters       : None
    //  Returns         : None
    //  Description     : Overriden On pause
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onPause() {
        super.onPause();
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onResume
    //  Paramters       : None
    //  Returns         : None
    //  Description     : Overriden On resume
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();


    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onClick
    //  Paramters       : None
    //  Returns         : None
    //  Description     : Overriden On click
    ////////////////////////////////////////////////////////////////////////////
    public void onClick(View v) {

    }
}