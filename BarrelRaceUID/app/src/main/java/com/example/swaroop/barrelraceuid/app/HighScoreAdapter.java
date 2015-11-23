package com.example.swaroop.barrelraceuid.app;


//Import Dependencies
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaroop on 11/3/2014.
 */
 ///////////////////////////////////////////////////////////////////////////////
//  File        : FileHandler.java
//	Course		: CS 6301-022 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Swaroop Kumar Pal
//  NetId       : sxp142730
//  Description : Class to create a custom  view for the score to be displayes
//  Date        : 11/18/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : HighScoreAdapter
//  Access      : Public
//  Description : Contains the fields for overridden getView method of 
//				  arrayadapter to create a custom view
///////////////////////////////////////////////////////////////////////////////
public class HighScoreAdapter extends ArrayAdapter {
    
	//Member variables context and list of object of values
	private final Context context;
    private final List<HighScoreDetails> m_lstobjValues;

	//contructor
    public HighScoreAdapter(Context context,int resource,List<HighScoreDetails> p_lstobjValues) {
        super(context, resource, p_lstobjValues);
        this.context = context;
        //this.m_lstobjValues = new ArrayList<FormFields>();
        this.m_lstobjValues = p_lstobjValues;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getView
    //  Paramters       : int position, View convertView, ViewGroup parent
    //  Returns         : View
    //  Description     : Overriden class to create a custom adapter
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.highscorelist, parent, false);
        TextView nametextView = (TextView) rowView.findViewById(R.id.NameListView);
        TextView phonetextView = (TextView) rowView.findViewById(R.id.ScoreListView);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.ContactListImage);

        String l_strName = m_lstobjValues.get(position).getName();
        nametextView.setText(l_strName);
        phonetextView.setText(m_lstobjValues.get(position).getScore());

        return rowView;
    }
}