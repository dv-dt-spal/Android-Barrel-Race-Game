package com.example.swaroop.barrelraceuid.app;


//Import Dependencies
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

 ///////////////////////////////////////////////////////////////////////////////
//  File        : AddScore.java
//	Course		: CS 6301-022 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Manalee Panda
//  NetId       : mxp141330
//  Description : Class to keep the updated scores and to read and write from 
//				  and to the file handler class. Also sorts the scores
//  Date        : 11/30/2014.
////////////////////////////////////////////////////////////////////////////////
public class DataHandler {

    //string data
	public String m_strData = "Null";
	
    //Memeber object to maintain the current set of data
    private static List<HighScoreDetails> m_lstobjHighScoreDetails;

    //Objects of FileHandler class to read and write data
    private static FileHandler m_objFileHandler;

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : initDataHandler
    //  Paramters   : None
    //  Returns     : None
    //  Description : Initialize the data handler
    ////////////////////////////////////////////////////////////////////////////
    public void initDataHandler() {

        Log.v("DataHandler", "Calling File Handler");
        //Initialize the file handler class
        m_objFileHandler = new FileHandler();

        if (null != m_objFileHandler) {
            m_objFileHandler.initFileHandler();
            m_lstobjHighScoreDetails = m_objFileHandler.readDataFromFile();
            sortList();
            System.out.println("DataHandler:: Size of list" +  m_lstobjHighScoreDetails.size());
        }

    }
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name : getListSize
    //  Paramtera   : None
    //  Returns     : int
    //  Description : Get the size of the list of scores
    ////////////////////////////////////////////////////////////////////////////
    public int getListSize(){
        return m_lstobjHighScoreDetails.size();
    }
	
    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : readScoreList
    //  Paramters   : None
    //  Returns     : List<HighScoreDetails>
    //  Description : Returns the list of scores in the updated list data
    ////////////////////////////////////////////////////////////////////////////
    public List<HighScoreDetails> readScoreList(){

        //return the contact list read from the file
        return m_lstobjHighScoreDetails;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : getScoreDetails
    //  Paramters   : int
    //  Returns     : None
    //  Description : Get the contact list obtained
    ////////////////////////////////////////////////////////////////////////////
    public HighScoreDetails getScoreDetails(int p_iindex){

        //Create a Form Field object and return to the the calling class
        HighScoreDetails l_objHighScoreDetails = new HighScoreDetails();
        l_objHighScoreDetails = m_lstobjHighScoreDetails.get(p_iindex);
        return l_objHighScoreDetails;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name : closeDataHandler
    //  Paramters   : None
    //  Returns     : None
    //  Description : De-initialize the data handler class
    ////////////////////////////////////////////////////////////////////////////
    public void closeDataHandler(){

        m_objFileHandler.closeFileHandler();
        //Make the file handler object null
        m_objFileHandler = null;

    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : addScore
    //  Paramters   : HighScoreDetails
    //  Returns     : None
    //  Description : The function will update member field list of object of
    //                HighScoreDetails so that the display list and file have latest
    //                data. This function only deals with new records being
    //                added to the contact manager
    ////////////////////////////////////////////////////////////////////////////
    public void addScore(HighScoreDetails p_objHighScoreDetails){

        //check if the FormFileds object sent is null
        if(null == p_objHighScoreDetails){
            System.out.println("Form Field Object is null");
            return;
        }

        //Check the list of HighScoreDetails list object
        if(null == m_lstobjHighScoreDetails){
            System.out.println("Form Field List is null");
            return;
        }

        //Add the Form field to the end of the list
        m_lstobjHighScoreDetails.add(p_objHighScoreDetails);
        //Sort
        sortList();
        //Update the File Handler to update the file
        updateFileHandler();
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : updateFileHandler
    //  Paramters   : None
    //  Returns     : None
    //  Description : The function will be called by add/delete/modify score
    //                to send the updated list of objects to the file handler
    //                so the file has the latest data
    ////////////////////////////////////////////////////////////////////////////
    private void updateFileHandler(){

        //Check if the file handler object is null
        if(null == m_objFileHandler){
            System.out.println("File Handler Obejct is null");
            return;
        }

        //Check the list of HighScoreDetails list object
        if(null == m_lstobjHighScoreDetails){
            System.out.println("Form Field List is null");
            return;
        }

        //send the member list of HighScoreDetails object to the File handler so that
        //it gets updated in the file
        m_objFileHandler.writeDataToFile(m_lstobjHighScoreDetails);
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : deleteScore
    //  Paramters   : HighScoreDetails
    //  Returns     : None
    //  Description : The function will delete the corresponding object fromt
    //                the member field list of object of HighScoreDetails so that
    //                the display list and file have latest data.
    //                This function only deals with old records being
    //                deleted from the contact manager
    //  Handler     : IndexOutOfBoundsException
    ////////////////////////////////////////////////////////////////////////////
    public void deleteScore(int p_iIndex){

        //Check if the index is negative
        if(0 > p_iIndex){
            System.out.println("Index is negative");
            return;
        }

        //Check the list of HighScoreDetails list object
        if(null == m_lstobjHighScoreDetails){
            System.out.println("Form Field List is null");
            return;
        }

        //Check if the list is empty
        if(m_lstobjHighScoreDetails.isEmpty()){
            System.out.println("Form Field List is empty");
            return;
        }

        //Delete the Form field from the list from the highlited index
        m_lstobjHighScoreDetails.remove(p_iIndex);

        //Update the File Handler to update the file
        updateFileHandler();
    }


    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : closeDataHandler
    //  Paramters   : None
    //  Returns     : None
    //  Description : sort the scores
    ////////////////////////////////////////////////////////////////////////////
    private void sortList(){

        if (m_lstobjHighScoreDetails.size() > 0) {
            Collections.sort(m_lstobjHighScoreDetails, new Comparator<HighScoreDetails>() {
                @Override
                public int compare(final HighScoreDetails object1, final HighScoreDetails object2) {
                    return (object1.getScoreMillis()) < (object2.getScoreMillis())  ? -1 : 1;
                }
            });
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //  Method Name : modifyScore
    //  Paramters[1]: HighScoreDetails
    //  Paramters[2]: Index
    //  Returns     : None
    //  Description : The function will modify the corresponding object fromt
    //                the member field list of object of HighScoreDetails so that
    //                the display list and file have latest data.
    //                This function only deals with old records being
    //                deleted from the contact manager
    ////////////////////////////////////////////////////////////////////////////
    public void modifyScore(HighScoreDetails p_objHighScoreDetails,int p_iIndex){

        //Check if the index is negative
        if(0 > p_iIndex){
            System.out.println("Index is negative");
            return;
        }

        //check if the FormFileds object sent is null
        if(null == p_objHighScoreDetails){
            System.out.println("Form Field Object is null");
            return;
        }

        //Check the list of HighScoreDetails list object
        if(null == m_lstobjHighScoreDetails){
            System.out.println("Form Field List is null");
            return;
        }

        //Check if the list is empty
        if(m_lstobjHighScoreDetails.isEmpty()){
            System.out.println("Form Field List is empty");
            return;
        }

        //Delete the Form field from the list. The subsequent object if any
        //moves left
        m_lstobjHighScoreDetails.remove(p_iIndex);

        //Add the object at the same position the previously shifted object
        //returns to the original position
        m_lstobjHighScoreDetails.add(p_iIndex, p_objHighScoreDetails);

        //Sort
        sortList();

        //Update the File Handler to update the file
        updateFileHandler();
    }
}