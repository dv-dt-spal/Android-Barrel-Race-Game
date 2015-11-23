package com.example.swaroop.barrelraceuid.app;

//Import Dependencies
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

///////////////////////////////////////////////////////////////////////////////
//  File        : AddScore.java
//	Course		: CS 6301-022 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Swaroop Pal
//  NetId       : sxp142730
//  Description : Implementation of Add Score Screen When the game is completed
//  Date        : 11/26/2014
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : AddScore
//  Access      : Public
//  Description : Add score activity
///////////////////////////////////////////////////////////////////////////////
public class AddScore extends ActionBarActivity {

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : onCreate
    //  Paramters   : Bundle
    //  Returns     : None
    //  Description : overridden method for populating XML
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_score);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : onCreateOptionsMenu
    //  Paramters   : Menu
    //  Returns     : boolean
    //  Description : overridden method for populating XML for action bar menu
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savecancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name : onOptionsItemSelected
    //  Paramters   : MenuItem
    //  Returns     : boolean
    //  Description : overridden method for taking action after the action bar item is selected
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_cancel: {
                Intent intent = new Intent(this, Game.class);
                startActivity(intent);
            }
            break;
            case R.id.action_save: {
                save();
                Intent intent = new Intent(this, Game.class);
                startActivity(intent);
            }
            break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : save
    //  Paramters   : None
    //  Returns     : None
    //  Description : saving the score in the data handler and hence the file
    ////////////////////////////////////////////////////////////////////////////
    public void save(){
        //Get the user input from the edit text field
        EditText editTextFirstName = (EditText) findViewById(R.id.firstName);

        //Typecast it to string type for usage in side file and data handler
        String l_strFirstName = editTextFirstName.getText().toString();

        //Check if the first name is filled else finish
        if (l_strFirstName.isEmpty()) {
            Log.e("AddContact Activity", "First Name Empty");
            Toast.makeText(getApplicationContext(), "Unnamed - Score discarded!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create the form fill object so that it can updated with the latest contact
        HighScoreDetails l_objHighScoreDetails = new HighScoreDetails();
        l_objHighScoreDetails.setName(l_strFirstName);
        l_objHighScoreDetails.setScore(Game.finalTimer);
        l_objHighScoreDetails.setScoreMillis(Game.lFinalTimeinMillis);

        //Update the data in the data handler
        MyActivity.m_objDataHandler.addScore(l_objHighScoreDetails);

    }

    ////////////////////////////////////////////////////////////////////////////
    //  Class Name : PlaceholderFragment    
    //  Description : PlaceholderFragment for a simple view
    ////////////////////////////////////////////////////////////////////////////
    public static class PlaceholderFragment extends Fragment {

		//Constructor
        public PlaceholderFragment() {
        }

	    ////////////////////////////////////////////////////////////////////////////
		//  Method Name : onCreateView
		//  Paramters   : LayoutInflater,ViewGroup,Bundle
		//  Returns     : View
		//  Description : Overridden - on create view
		////////////////////////////////////////////////////////////////////////////
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_score, container, false);
            ((TextView) rootView.findViewById(R.id.scoreView)).setText("Race Time :: " + Game.finalTimer);
            return rootView;
        }
    }
}
