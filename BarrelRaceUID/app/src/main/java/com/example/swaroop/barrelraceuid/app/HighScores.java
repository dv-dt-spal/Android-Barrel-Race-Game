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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

///////////////////////////////////////////////////////////////////////////////
//  File        : HighScores.java
//	Course		: CS 6301-015 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Debabrat Mohanty
//  NetId       : dxm141630
//  Description : Class to control the activities for the high score screen
//				  read data from the data handler and display in the screen
//  Date        : 11/18/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : HighScores
//  Access      : Public
//  Description : Contains the fields for overridden getView method of 
//				  arrayadapter to create a custom view
///////////////////////////////////////////////////////////////////////////////
public class    HighScores extends ActionBarActivity {

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreate
    //  Paramters       : Bundle
    //  Returns         : None
    //  Description     : Overriden On create method 
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreateOptionsMenu
    //  Paramters       : Menu
    //  Returns         : boolean
    //  Description     : Overriden On onCreateOptionsMenu 
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.high_scores, menu);
        return true;
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
    //  Class Name : PlaceholderFragment    
    //  Description : PlaceholderFragment for a simple view
    ////////////////////////////////////////////////////////////////////////////
    public static class PlaceholderFragment extends Fragment {

		//Custom Adapter class
        private HighScoreAdapter mHighScoreAdapter;
		
		//constructor
        public PlaceholderFragment() {

        }

		
		////////////////////////////////////////////////////////////////////////////
		//  Method Name : onCreateView
		//  Paramters   : LayoutInflater,ViewGroup,Bundle
		//  Returns     : View
		//  Description : Overridden - on create view in the fragment show the list
		////////////////////////////////////////////////////////////////////////////
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.v("MyActivity","Creating View");
            //Initialize the data handler to read the contact list from the file

            View rootView = inflater.inflate(R.layout.fragment_high_scores, container, false);


            //Get the contact list
            List<HighScoreDetails> l_lstobjHighScoreDetails;
            List<HighScoreDetails> l_lstobjHighScoreDetailsToBeShown;
            l_lstobjHighScoreDetails = new ArrayList<HighScoreDetails>();
            l_lstobjHighScoreDetailsToBeShown = new ArrayList<HighScoreDetails>();
            l_lstobjHighScoreDetails = MyActivity.m_objDataHandler.readScoreList();

            //String to fill the list model
            String l_strData = null;

            //Local objec to get the vlue
            HighScoreDetails l_objHighScoreDetails;

            List<String> lststrContactList = new ArrayList<String>();

            int iHighScoreList;
            if(l_lstobjHighScoreDetails.size() > 10){
                System.out.println("HighScore :: Size of List(>10) :: " + l_lstobjHighScoreDetails.size());
                iHighScoreList = 10;
            }
            else
            {
                System.out.println("HighScore :: Size of List(<=10) :: " + l_lstobjHighScoreDetails.size());
                iHighScoreList = l_lstobjHighScoreDetails.size();
            }
            System.out.println("HighScore :: List Size to be shown :: " + iHighScoreList);

            //Iterate the list of form field to get the name and phone number
            for(int l_iPos = 0 ; l_iPos < iHighScoreList; l_iPos++ ){

                //Get the object from the list
                l_objHighScoreDetails = l_lstobjHighScoreDetails.get(l_iPos);
                l_lstobjHighScoreDetailsToBeShown.add(l_objHighScoreDetails);

                //check if the object is null
                if(null == l_objHighScoreDetails){
                    Log.v("MyActivity", "Form Field object in the member is null");
                }

                //Construct the string data Name and Phone Number
                l_strData = l_objHighScoreDetails.getName()
                        + "\t\t" +
                        l_objHighScoreDetails.getScore();

                Log.v("MyActivity",l_strData);

                lststrContactList.add(l_strData);
                //set the data as null for next iteration or exit
                l_strData = null;
                l_objHighScoreDetails = null;
            }

            //Initialize the Array Adapter
            /*mContactListAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.contact_list,
                    R.id.contact_list_textview,
                    lststrContactList);*/

            mHighScoreAdapter = new HighScoreAdapter(getActivity(),
                    R.layout.highscorelist,
                    l_lstobjHighScoreDetailsToBeShown);

            //Get a reference to the list view and set the adapter to it.
            ListView listView = (ListView) rootView.findViewById(R.id.listViewHighScoreList);
            //listView.setAdapter(mContactListAdapter);
            listView.setAdapter(mHighScoreAdapter);

            return rootView;
        }
    }
}
