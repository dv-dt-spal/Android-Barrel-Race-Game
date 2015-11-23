package com.example.swaroop.barrelraceuid.app;

//Import dependencies
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

///////////////////////////////////////////////////////////////////////////////
//  File        : FileHandler.java
//	Course		: CS 6301-022 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Manalee Panda
//  NetId       : mxp141330
//  Description : Class to read and write from the file
//  Date        : 11/15/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : HighScoreDetails
//  Access      : Package Private
//  Description : Contains the fields for the user interface as private. They
//                can be read from or written to class by set/get functions
///////////////////////////////////////////////////////////////////////////////
class HighScoreDetails{

    //Field decalaration
    //First Name in the field
    private String m_strName;

    //Phone number in the field
    private String m_strScore;

    //Phone number in the field
    private long m_lHighScore;

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : setName
    //  Paramters[1]    : String
    //  Returns         : None
    //  Description     : Set the  name to the member variable so that it
    //                    can be used by other classes by calling the get method
    ////////////////////////////////////////////////////////////////////////////
    public void setName(String p_strName){
        m_strName = p_strName;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getName
    //  Paramters       : None
    //  Returns         : String
    //  Description     : Get the name from the member variable so that it
    //                    can be used by other classes
    ////////////////////////////////////////////////////////////////////////////
    public String getName(){
        return m_strName;
    }


    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : SetScore
    //  Paramters[1]    : String
    //  Returns         : None
    //  Description     : Set the Score. to the member variable so that it
    //                    can be used by other classes by calling the get method
    ////////////////////////////////////////////////////////////////////////////
    public void setScore(String p_strScore){
        m_strScore = p_strScore;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getScore
    //  Paramters       : None
    //  Returns         : String
    //  Description     : Get the score. from the member variable so that it
    //                    can be used by other classes
    ////////////////////////////////////////////////////////////////////////////
    public String getScore(){
        return m_strScore;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : setScoreMillis
    //  Paramters[1]    : long
    //  Returns         : None
    //  Description     : Set the Score. to the member variable so that it
    //                    can be used by other classes by calling the get method
    ////////////////////////////////////////////////////////////////////////////
    public void setScoreMillis(long p_lScore){
        m_lHighScore = p_lScore;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getScoreMillis
    //  Paramters       : None
    //  Returns         : long
    //  Description     : Get the score. from the member variable so that it
    //                    can be used by other classes
    ////////////////////////////////////////////////////////////////////////////
    public long getScoreMillis(){
        return m_lHighScore;
    }

}
public class FileHandler {

    //File Object to open the file
    private File m_objfFile;

    //New Folder
    private File m_objNewFolder;

    //Object to read the data from the file
    private Scanner m_objfRead;

    //Object to write data into the file
    private PrintWriter m_objfWrite;

    //Object to store the intialization status
    boolean m_objInitStatus;

    //Memeber object to maintain the current set of data
    public List<HighScoreDetails> m_lstobjHighScoreDetails;


    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : FileHandler
    //  Paramters       : None
    //  Returns         : None
    //  Description     : No parameter constructor
    ////////////////////////////////////////////////////////////////////////////
    FileHandler(){

        //Initialize the member variables

        //File Object to open the file
        m_objfFile = null;

        //Object to read the data from the file
        m_objfRead = null;

        //Object to write data into the file
        m_objfWrite = null;

        //Object to store the intialization status as false
        m_objInitStatus = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : initFileHandler
    //  Paramters       : None
    //  Returns         : boolean
    //  Description     : No parameter constructor
    ////////////////////////////////////////////////////////////////////////////
    void initFileHandler(){

        //Check of file handler is already initialized
        if(m_objInitStatus){
            Log.v("File Handler", "File handler already Initialized!!!");
            return;
        }

        /*Initialize the member variables in a try catch block to catch any file
        handling exceptions;if any.*/
        try{

            m_objNewFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "BarrelRace");
            if (!m_objNewFolder.exists())
            {
                Log.v("File Handler","Creating Directory");
                m_objNewFolder.mkdirs();
            }

            //File Object to open the file to open a file Data.txt
            m_objfFile = new File(m_objNewFolder,"HighScoreList.txt");

            //Check if the file doesn't exist then create one
            if(!m_objfFile.exists()){
                Log.v("File Handler","Creating File");
                m_objfFile.createNewFile();
            }

            //Check if all objects are intiialized
            if(null != m_objfFile){
                //Set the intialization status as true
                m_objInitStatus = true;
            }
        }
        catch(IOException e){
            Log.v("File Handler","Exception!! Caught in InitFileHandler");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : closeFileHandler
    //  Paramters       : None
    //  Returns         : None
    //  Description     : No parameter constructor
    ////////////////////////////////////////////////////////////////////////////
    public void closeFileHandler(){

        //Check of file handler is not intialised
        if(!m_objInitStatus){
            Log.v("File Handler","File Handler not initialized ");
            return;
        }
        //Reset the member variables as default
        //File Object to open the file
        m_objfFile = null;

        //Object to read the data from the file
        m_objfRead = null;

        //Object to write data into the file
        m_objfWrite = null;

        //Object to store the intialization status as false
        m_objInitStatus = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : readDataFromFile
    //  Paramters       : None
    //  Returns         : List<HighScoreDetails>
    //  Description     : The class reads data from the file and converts the
    //                    file data in to list of objects of HighScoreDetails.
    //                    Each object of list corresponds to a line in file.
    //                    This fucntion will be called by UI class at the
    //                    beginning when data has to be displayed on screen
    ////////////////////////////////////////////////////////////////////////////
    public List<HighScoreDetails> readDataFromFile(){

        //Check of file handler is not intialised
        if(!m_objInitStatus){
            Log.v("File Handler","File Handler not initialized");
            return null;
        }

        //Local variable to store the list of Form field object
        List<HighScoreDetails> l_lstobjHighScoreDetails = new ArrayList<HighScoreDetails>();
        try{

            //Check if the File object is not null
            if(null != m_objfFile){

                //Open the Scanner object to read data from the file
                m_objfRead = new Scanner(m_objfFile);
            }

            //Check if the file is empty
            if(0 == m_objfFile.length()){
                Log.v("File Handler","File is empty");
            }

            //Local string to keep a single line read from the file
            String l_strLineFromFile = null;

            //Declare HighScoreDetails object where the field value can be stored
            HighScoreDetails l_objHighScoreDetails;

            //Loop for reading the lines from the file and putting it one object
            //of the HighScoreDetails class and then inserting it in to the list to be
            //returned to the calling class
            while(m_objfRead.hasNextLine()){

                //Assign the line read from the file to the temporary variable
                l_strLineFromFile = m_objfRead.nextLine();

                Log.v("File Handler",l_strLineFromFile);
                //Since the fields are dilimited by "," in the file so we split
                //the string and put it in string list so that individual fields
                //can be placed in the member field of the HighScoreDetails class
                String[] l_arrStringLineFromFile = l_strLineFromFile.split(";");

                //create HighScoreDetails object where the field value can be stored
                l_objHighScoreDetails = new HighScoreDetails();

                //Check the HighScoreDetails object is null or not
                if(null == l_objHighScoreDetails){
                    Log.v("File Handler","Form Field object is null");
                    return null;
                }

                //Store the data from the file in the object created
                //First Name
                l_objHighScoreDetails.setName(l_arrStringLineFromFile[0]);
                System.out.println("FileHandler:: Name " +  l_arrStringLineFromFile[0]);

                //Score
                l_objHighScoreDetails.setScore(l_arrStringLineFromFile[1]);
                System.out.println("FileHandler:: Score :: "  +  l_arrStringLineFromFile[1]);

                //Score in  millis
                l_objHighScoreDetails.setScoreMillis(Integer.parseInt(l_arrStringLineFromFile[2]));
                System.out.println("FileHandler:: Score in millis "  +  l_arrStringLineFromFile[2]
                                + " in millis :: "  + Integer.parseInt(l_arrStringLineFromFile[2]));


                //Add the object to the list
                boolean l_bAddResult = l_lstobjHighScoreDetails.add(l_objHighScoreDetails);
                System.out.println("FileHandler:: Add result " +  l_bAddResult);
                System.out.println("FileHandler:: Size of list " +  l_lstobjHighScoreDetails.size());
                //Make the temp object null for next iteration or final exit
                l_strLineFromFile = null;
                l_objHighScoreDetails = null;
                l_arrStringLineFromFile = null;
            }

            //Close the scanner object
            m_objfRead.close();
        }
        catch(IOException e){

        }
        catch(ArrayIndexOutOfBoundsException e){

        }

        //Keep the data in the file handler class
        m_lstobjHighScoreDetails = l_lstobjHighScoreDetails;
        System.out.println("FileHandler:: Size of list" +  m_lstobjHighScoreDetails.size());
        //return the object
        return l_lstobjHighScoreDetails;
    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : writeDataToFile
    //  Paramters       : List<HighScoreDetails>
    //  Returns         : None
    //  Description     : The class reads data from the file and converts the
    //                    file data in to list of objects of HighScoreDetails.
    //                    Each object of list corresponds to a line in file.
    //                    This fucntion will be called by UI class at the
    //                    beginning when data has to be displayed on screen
    ////////////////////////////////////////////////////////////////////////////
    public void writeDataToFile(List<HighScoreDetails> p_lstobjHighScoreDetails){

        //Check of file handler is not intialised
        if(!m_objInitStatus){
            Log.e("File Handler","File Handler not initialized ");
            return;
        }

        try{
            //Check if the File object is not null
            if(null != m_objfFile){
                //Open the Printwriter object to read data from the file
                m_objfWrite = new PrintWriter(m_objfFile);
            }

            //Local varibale to store the constrcuted string to be written to file
            String l_strConstructedDataToFile = null;

            //Loop to iterate the list of objects of HighScoreDetails, get the
            //fields from the object. construct a string to be written to file
            //Write the data to the file using the printwriter object
            for (ListIterator<HighScoreDetails> l_iteratorList =
                         p_lstobjHighScoreDetails.listIterator();
                 l_iteratorList.hasNext(); ){

                //Get the HighScoreDetails object from the iterating index
                HighScoreDetails l_objHighScoreDetails = l_iteratorList.next();

                //check if the object is null
                if(null == l_objHighScoreDetails){
                    System.out.println("Form Field object in the member is null");
                    return;
                }

                //Construct the data using the fields in the object
                l_strConstructedDataToFile = (
                        l_objHighScoreDetails.getName().trim()
                                + ";" +
                                l_objHighScoreDetails.getScore().trim()
                                + ";" +
                                Integer.toString((int)l_objHighScoreDetails.getScoreMillis()));


                //Write the data constructed to the line
                m_objfWrite.println(l_strConstructedDataToFile);

                System.out.println("Writing to filr " + l_strConstructedDataToFile );

                //Make the local variable null for next iteration or exit
                l_strConstructedDataToFile = null;
                l_objHighScoreDetails = null;
            }
            //Close the printwriter object
            m_objfWrite.close();
        }
        catch(IOException e){

        }
    }
}
