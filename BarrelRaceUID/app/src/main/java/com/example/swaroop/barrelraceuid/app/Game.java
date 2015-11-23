package com.example.swaroop.barrelraceuid.app;

//Import Dependencies
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


///////////////////////////////////////////////////////////////////////////////
//  File        : Settings.java
//	Course		: CS 6301-022/6301-015 User Interface Design
//	Professor	: Dr. John Cole
//	Semester	: Fall 2014
//	Project		: Barrel Race Game - Assignment 4
//  Author      : Swaroop Kumar Pal Debabrat Mohanty Manalee Panda
//  NetId       : sxp142730/mxp141330/dxm141630
//  Description : Class to for Game Scene Activity
//  Date        : 11/22/2014.
////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : Coordinates
//  Access      : Package Private
//  Description : Used for storing the horse coordinates
///////////////////////////////////////////////////////////////////////////////
class Coordinates{
    int XCoordinate;
    int YCoordinate;
}

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : Game
//  Access      : public
//  Description : Game scene activity implementation
///////////////////////////////////////////////////////////////////////////////
public class Game extends ActionBarActivity implements SurfaceHolder.Callback,SensorEventListener {

	//Member variables to be used across
    private SurfaceView surface;
    private SurfaceHolder holder;

    public static int x;
    public static int y;
    public MediaPlayer bgMusic;

    static Bitmap image;
    static Bitmap bmHorse;
    static Bitmap bmBarrel1;
    static Bitmap bmBarrel2;
    static Bitmap bmFence1;
    static Bitmap bmText;
    static Bitmap bmStart;
    static Bitmap bmPause;
    static Bitmap bmGameover;
    static Bitmap bmLike;

    static Bitmap temp;
    static Bitmap tempHorse;
    static Bitmap tempBarrel1;
    static Bitmap tempBarrel2;
    static Bitmap tempFence1;
    static Bitmap tempText;
    static Bitmap tempStart;
    static Bitmap tempPause;
    static Bitmap tempGameover;
    static Bitmap tempLike;

    int margenMinX;
    int margenMinY;
    int margenMaxX;
    int margenMaxY;

    static boolean barrel1Completed = false;
    static boolean barrel2Completed =false;
    static boolean barrel3Completed = false;
    static boolean timerstarted = false;


    static List<Coordinates> lstCoordinates;
    static CheckBarrel objCheckBarrel;
    private SensorManager sensorManager = null;
    Coordinates homeCoordinates;

    long mStartTime;
    public static String timer="0:00:00";
    public static String finalTimer="0:00:00";
    public static long lFinalTimeinMillis = 00;
    private volatile boolean running = true;

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreate
    //  Paramters       : Bundle
    //  Returns         : None
    //  Description     : Overriden On create method initialize the surface and sensors
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        surface = (SurfaceView) findViewById(R.id.gamescene);
        holder = surface.getHolder();
        surface.getHolder().addCallback(this);

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Get the sensors
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : draw
    //  Paramters       : none
    //  Returns         : None
    //  Description     : Called for creating the canvas and then call do draw
    ////////////////////////////////////////////////////////////////////////////
    private void draw() {
        // thread safety - the SurfaceView could go away while we are drawing

        Canvas c = null;
        try {
            // NOTE: in the LunarLander they don't have any synchronization
            // here,
            // so I guess this is OK. It will return null if the holder is not
            // ready
            c = holder.lockCanvas();

            // this needs to synchronize on something
            if (c != null) {
                doDraw(c);
            }
        } finally {
            if (c != null) {
                holder.unlockCanvasAndPost(c);
            }
        }
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getBitmapScalingFactor
    //  Paramters       : none
    //  Returns         : float
    //  Description     : get the scaling factor for various screen resolution
    ////////////////////////////////////////////////////////////////////////////
    private float getBitmapScalingFactor(Bitmap bm, String strName) {
        try{
            AssetManager assetManager = getAssets();
            InputStream istr = assetManager.open(strName + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(istr);

            float screenWidth = getResources().getDisplayMetrics().widthPixels;
            int ih=bitmap.getHeight();
            int iw=bitmap.getWidth();
            float scalefactor = screenWidth/iw;
            return scalefactor;
        }
        catch(IOException e) {

        }
        return 1;
    }

    // Coordinates of three barrels
    private int barrels[][];
    private int fence[][];
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : doDraw
    //  Paramters       : none
    //  Returns         : none
    //  Description     : Drawing on the canvas and all the check if the barrel hit 
	//					  fence hit reached home
    ////////////////////////////////////////////////////////////////////////////
    public void doDraw(Canvas canvas) {

        //set the bitmaps to be drawn on the screen
        System.out.println("On Draw");
        image = Bitmap.createScaledBitmap(temp,canvas.getWidth(),canvas.getHeight(),true);
        margenMaxX = mWidth - 100;
        margenMinX = 10;
        margenMaxY = ((int)((3*mHeight)/4))+60;
        margenMinY = ((int)((mHeight)/3))-20;
        bmHorse = Bitmap.createScaledBitmap(tempHorse,tempHorse.getWidth(),tempHorse.getHeight(),true);

        //get the scaling factor as per the screen resolution
        int ScaleBarrel1 = (int)getBitmapScalingFactor(tempBarrel1,"ic_barrel2");
        int ScaleBarrel2 = (int)getBitmapScalingFactor(tempBarrel2,"ic_barrel3");
        int ScaleFence = (int)getBitmapScalingFactor(tempFence1,"fence2");
        int ScaleGameover = (int)getBitmapScalingFactor(tempGameover,"gameover");
        int ScaleLike = (int)getBitmapScalingFactor(tempLike,"ic_like");

        //create the scaled bitmap images
        bmBarrel1 = Bitmap.createScaledBitmap(tempBarrel1,tempBarrel1.getWidth()*ScaleBarrel1,
                                                tempBarrel1.getHeight()*ScaleBarrel1,true);
        bmBarrel2 = Bitmap.createScaledBitmap(tempBarrel2,tempBarrel2.getWidth()*ScaleBarrel2,
                                              tempBarrel2.getHeight()*ScaleBarrel2,true);
        bmFence1 = Bitmap.createScaledBitmap(tempFence1,tempFence1.getWidth()*ScaleFence,
                                             tempFence1.getHeight()*ScaleFence,true);

        bmGameover = Bitmap.createScaledBitmap(tempGameover,tempGameover.getWidth()*ScaleGameover,
                                                tempGameover.getHeight()*ScaleGameover,true);

        bmLike = Bitmap.createScaledBitmap(tempLike,tempLike.getWidth()*ScaleGameover,
                tempLike.getHeight()*ScaleGameover,true);

        //draw the bitmaps
        canvas.drawBitmap(image,0,0,null);
        canvas.drawBitmap(bmBarrel2,((int)(mWidth/3)) - 100 ,((int)((2*mHeight)/3))-100,null);
        canvas.drawBitmap(bmBarrel2, ((int)(mWidth/3)) + 200 ,((int)((2*mHeight)/3))-100, null);
        canvas.drawBitmap(bmBarrel1,((int)(mWidth/2))-50 ,((int)((mHeight)/3))+120,null);



        // Coordinates of three barrels
        barrels[0][0] = ((int)(mWidth/3)) - 100;
        barrels[0][1] = ((int)((2*mHeight)/3))-100;
        barrels[1][0] = ((int)(mWidth/3)) + 200;
        barrels[1][1] = ((int)((2*mHeight)/3))-100;
        barrels[2][0] = ((int)(mWidth/2))-50;
        barrels[2][1] = ((int)((mHeight)/3))+120;

        //draw the bitmaps on the surface
        Bitmap bmNearFence = Game.scaleDown(bmFence1,200,true);
        canvas.drawBitmap(bmNearFence,5 ,((int)((3*mHeight)/4))+30,null);
        canvas.drawBitmap(bmNearFence,((int)(mWidth/4))+ 200 ,((int)((3*mHeight)/4))+30,null);
        canvas.drawBitmap(bmNearFence,100 ,((int)((3*mHeight)/4))+30,null);
        canvas.drawBitmap(bmNearFence,((int)(mWidth/4))+ 350 ,((int)((3*mHeight)/4))+30,null);

        //scale down the fence
        bmFence1 = Game.scaleDown(bmFence1,150,true);

        //draw the bitmaps
        canvas.drawBitmap(bmFence1,((int)(mWidth/4))- 50 ,((int)((mHeight)/3))-20, null);
        canvas.drawBitmap(bmFence1,((int)(mWidth/3))+20 ,((int)((mHeight)/3))-20,null);
        canvas.drawBitmap(bmFence1,((int)(mWidth/4))+ 50 ,((int)((mHeight)/3))-20, null);
        canvas.drawBitmap(bmFence1,((int)(mWidth/3))+ 170 ,((int)((mHeight)/3))-20,null);
        if(!MyActivity.bGameFarm){
            canvas.drawBitmap(bmFence1, 0, ((int)((mHeight)/3))-20, null);
            canvas.drawBitmap(bmFence1,((int)(mWidth/3))+ 320,((int)((mHeight)/3))-20,null);
        }

        // Coordinates of fence
        fence[0][0] = 5;
        fence[0][1] = ((int)((3*mHeight)/4))+30;
        fence[1][0] = ((int)(mWidth/4))+ 200;
        fence[1][1] = ((int)((3*mHeight)/4))+30;
        fence[2][0] = 100;
        fence[2][1] = ((int)((3*mHeight)/4))+30;
        fence[3][0] = ((int)(mWidth/4))+ 350;
        fence[3][1] = ((int)((3*mHeight)/4))+30;

        fence[4][0] = ((int)(mWidth/4))- 50;
        fence[4][1] = ((int)((mHeight)/3))-20;
        fence[5][0] = ((int)(mWidth/3))+20;
        fence[5][1] = ((int)((mHeight)/3))-20;
        fence[6][0] = ((int)(mWidth/4))+50;
        fence[6][1] = ((int)((mHeight)/3))-20;
        fence[7][0] = ((int)(mWidth/3))+ 170;
        fence[7][1] = ((int)((mHeight)/3))-20;

        Matrix matrix = new Matrix();
        //matrix.setTranslate(10,10);
        matrix.setRotate(-70, 220, 30);
        //canvas.drawBitmap(bmFence1, matrix, null);

        //set tand draw the timer
        Paint timerPaint = new Paint();
        timerPaint.setColor(Color.RED);
        timerPaint.setTextAlign(Paint.Align.LEFT);
        timerPaint.setTextScaleX((float) 2.2);
        timerPaint.setTextSize(50);
        timerPaint.setStyle(Paint.Style.FILL);
        timerPaint.setStrokeWidth(6);
        timerPaint.setAntiAlias(true);
        canvas.drawText(timer, 150, 190, timerPaint);
        if(bGameStatus) {
            canvas.drawBitmap(bmHorse, Game.x, Game.y, null);
            Coordinates objCoordinates = new Coordinates();
            objCoordinates.XCoordinate = (int) x;
            objCoordinates.YCoordinate = (int) y;
            lstCoordinates.add(objCoordinates);
            if(MyActivity.bGameSound) {
                bgMusic.start();
            }

            int iRadius = 2500;
            //Check for Barrel Hit
            for (int iPos = 0; iPos < 3; iPos++) {
                if(iPos== 2){
                    iRadius = 1600;
                }
                if ((x - barrels[iPos][0]) * (x - barrels[iPos][0])
                        + (y - barrels[iPos][1]) * (y - barrels[iPos][1]) <= iRadius) {
                    touchBarrel = true;
                    break;
                }

            }

            //Check for Fence Hit
            //This loop will check if moving ball has hit any of the barrel
            for (int i = 0; i < 8; i++) {

                if ((x - fence[i][0]) * (x - fence[i][0])
                        + (y - fence[i][1]) * (y - fence[i][1]) <= 4300) {

                    hitfence = true;
                }

            }

            //if the barrel is touched
            if(touchBarrel){
                System.out.println("Game Over");
                canvas.drawBitmap(bmGameover, ((int)(mWidth/3))-40, ((int)((mHeight)/3))+100, null);
            }

            //check for barrel covered
            if(!barrel1Completed) {
                barrel1Completed = objCheckBarrel.checkBarrel1Covered(x,y);
            }

            if(!barrel2Completed) {
                barrel2Completed = objCheckBarrel.checkBarrel2Covered(x,y);
            }

            if(!barrel3Completed) {
                barrel3Completed = objCheckBarrel.checkBarrel3Covered(x,y);
            }

            //if the barrels are covered draw the like
            if(barrel1Completed){
                canvas.drawBitmap(bmLike,((int)(mWidth/3)) - 90 ,((int)((2*mHeight)/3))-130,null);
            }

            if(barrel2Completed){
                canvas.drawBitmap(bmLike, ((int)(mWidth/3)) + 220 ,((int)((2*mHeight)/3))-130, null);
            }

            if(barrel3Completed){
                canvas.drawBitmap(bmLike,((int)(mWidth/2))-50 ,((int)((mHeight)/3))+90,null);
            }

            //if all barrel covered
            if(barrel1Completed && barrel2Completed && barrel3Completed){
                //Check if reached home
                if (((x - Xinitial) * (x - Xinitial))
                        + ((y - Yinitial) * (y - Yinitial)) <= 900) {
                    bReachedHome = true;
                }
            }

            //if reached home
            if(bReachedHome){
                //stop the timer
                finalTimer = timer;
                lFinalTimeinMillis = objStopWatch.stop();

                //unregister the sensor
                UnregisterSensor();
                bGameCompleted = true;
            }
        }
    }

    static int mWidth;
    static int mHeight;

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : surfaceChanged
    //  Paramters       : SurfaceHolder holder, int format, int width, int height
    //  Returns         : none
    //  Description     : overriden - Called when surface view is changed 
	//					  and store the height and width
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        //objHorseGallpoing.setSize(width, height);
        mWidth = width;
        mHeight = height;
    }

    private GameThread objGameThread;
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : surfaceCreated
    //  Paramters       : SurfaceHolder holder
    //  Returns         : none
    //  Description     : overriden - Called when surface view is created and start the thread for game
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("Game","Surface Created");
        running = true;
        objGameThread = new GameThread();
        objGameThread.start();
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : surfaceCreated
    //  Paramters       : SurfaceHolder holder
    //  Returns         : none
    //  Description     : overriden - called when surface view is destroyed
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            objGameThread.safeStop();
        } finally {
            objGameThread = null;
        }

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : RegisterSensor
    //  Paramters       : None
    //  Returns         : none
    //  Description     : register to get the sensor values
    ////////////////////////////////////////////////////////////////////////////
    void RegisterSensor(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : UnregisterSensor
    //  Paramters       : None
    //  Returns         : none
    //  Description     : unregister to stop getting the sensor values
    ////////////////////////////////////////////////////////////////////////////
    void UnregisterSensor(){
        sensorManager.unregisterListener(this);
    }

    static boolean bGameStatus = false;
    static boolean bGamePaused = false;
    static boolean bGameStopped = false;
    static ImageButton StartButton;
    static ImageButton RestartButton;
    static ImageButton PauseButton;
    static Stopwatch objStopWatch;
    private boolean gameOver = false;
    private boolean bGameCompleted = false;
    private boolean touchBarrel = false;
    private boolean hitfence = false;
    private boolean bReachedHome = false;
    private int hitfencecount = 0;
    static int Xinitial;
    static int Yinitial;
	
	////////////////////////////////////////////////////////////////////////////
    //  Class Name     : GameThread
    //  Description    : Runs the game in a thread context calling draw and checking for 
    //					 pause stop or game over
    ////////////////////////////////////////////////////////////////////////////
    private class GameThread extends Thread implements View.OnClickListener {

		////////////////////////////////////////////////////////////////////////////
		//  Method Name     : safeStop
		//  Paramters       : None
		//  Returns         : none
		//  Description     : stop the game 
		////////////////////////////////////////////////////////////////////////////
        public void safeStop() {
            if(timerstarted) {
                objStopWatch.stop();
                timerstarted = false;
            }
            mStartTime = 0;
            timer = "0:00:00";
            bGamePaused = false;
            bGameStatus = false;
            barrel1Completed = false;
            barrel2Completed =false;
            barrel3Completed = false;
            running = false;
        }

        long millis = 00;
        int seconds = 00;
        int minutes = 00;

        long pausedmillis = 00;

		////////////////////////////////////////////////////////////////////////////
		//  Method Name     : onClick
		//  Paramters       : View
		//  Returns         : none
		//  Description     : overriden to take action when image buttons are pressed 
		////////////////////////////////////////////////////////////////////////////
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.startgame:{

                    if(touchBarrel){
                        touchBarrel = false;
                        running = true;
                    }

                    if(!bGameStatus){

                        //mStartTime=SystemClock.uptimeMillis();
                        objStopWatch = new Stopwatch();
                        objStopWatch.start();
                        timerstarted = true;
                        mStartTime = objStopWatch.startTime;
                        System.out.println("Start Time :: " + mStartTime );
                        RegisterSensor();
                        bGameStatus = true;
                    }

                    if(bGamePaused){
                        objStopWatch.resume();
                        bGamePaused = false;
                        RegisterSensor();
                    }
                }
                break;
                case R.id.restartgame:{
                    if(timerstarted){
                        objStopWatch.stop();
                        timerstarted =false;
                    }
                    bGamePaused = false;
                    bGameStatus = false;
                    timer = "0:00:00";
                    mStartTime = 0;
                    millis = 00;
                    seconds = 00;
                    minutes = 00;
                    bGamePaused = false;
                    bGameStatus = false;
                    barrel1Completed = false;
                    barrel2Completed =false;
                    barrel3Completed = false;
                    hitfence = false;
                    Intent intent = Game.this.getIntent();
                    finish();
                    startActivity(intent);
                }
                break;
                case R.id.pausegame:{
                    if(bGameStatus) {
                        bGamePaused = true;
                        objStopWatch.pause();
                        if(MyActivity.bGameSound) {
                            bgMusic.pause();
                        }
                        UnregisterSensor();
                        pausedmillis = millis;
                    }
                }
                break;
            }

        }

		////////////////////////////////////////////////////////////////////////////
		//  Method Name     : run
		//  Paramters       : none
		//  Returns         : none
		//  Description     : for the main thread  game
		////////////////////////////////////////////////////////////////////////////
        public void run() {

            StartButton = (ImageButton) findViewById(R.id.startgame);
            // startBtn.setBackgroundDrawable(R.drawable.ic_start_button);
            StartButton.setOnClickListener(this);

            RestartButton = (ImageButton) findViewById(R.id.restartgame);
            // startBtn.setBackgroundDrawable(R.drawable.ic_start_button);
            RestartButton.setOnClickListener(this);

            PauseButton = (ImageButton) findViewById(R.id.pausegame);
            // startBtn.setBackgroundDrawable(R.drawable.ic_start_button);
            PauseButton.setOnClickListener(this);

            //Set the Barrel coordinates
            objCheckBarrel = new CheckBarrel();
            objCheckBarrel.setBarrel1Center(((int)(mWidth/3)) - 100,
                                               ((int)((2*mHeight)/3))-100);
            objCheckBarrel.setBarrel2Center(((int)(mWidth/3)) + 200,
                                              ((int)((2*mHeight)/3))-100);
            objCheckBarrel.setBarrel3Center(((int)(mWidth/2))-50,
                                                ((int)((mHeight)/3))+120);

            //generate the circle center
            objCheckBarrel.GenerateCircleCenters();


            lstCoordinates = new ArrayList<Coordinates>();
            barrels = new int[3][3];
            fence = new int[8][8];
            bgMusic = MediaPlayer.create(Game.this,R.raw.banjo);
            if(MyActivity.bGameFarm) {
                temp = BitmapFactory.decodeResource(getResources(), R.drawable.background12);
            }
            else
            {
                temp = BitmapFactory.decodeResource(getResources(), R.drawable.background10);
            }
            tempHorse = BitmapFactory.decodeResource(getResources(),R.drawable.horse1);
            tempBarrel1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_barrel2);
            tempBarrel2 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_barrel3);
            tempFence1 = BitmapFactory.decodeResource(getResources(),R.drawable.fence2);

            tempGameover = BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
            tempLike = BitmapFactory.decodeResource(getResources(),R.drawable.ic_like);

            x = ((int)((mWidth)/2))-60;
            y = ((int)((3*mHeight)/4))+60;
            Xinitial = ((int)((mWidth)/2))-60;
            Yinitial = ((int)((3*mHeight)/4))+60;

            homeCoordinates = new Coordinates();
            homeCoordinates.XCoordinate = (int)x;
            homeCoordinates.YCoordinate = (int)y;



            //if (rotation == Configuration.ORIENTATION_LANDSCAPE){

            //}
           // else{



           // }

            while(running){
                if(!bGamePaused) {
                    if (mStartTime > 0) {
                        System.out.println("Start Time in Loop :: " + mStartTime );
                        if(hitfence){
                            objStopWatch.setPenaltyCount();
                            hitfence = false;
                        }
                        millis = objStopWatch.getElapsedTimeMili();
                        System.out.println("Game Time Elapsed :: " + millis);
                        seconds = (int) (millis / 1000);
                        minutes = seconds / 60;
                        seconds = seconds % 60;
                        millis = millis - minutes * 60 * 1000 - seconds * 1000;
                        pausedmillis = 00;
                    }
                    // If game is not started then timer will be 0:00:00
                    timer = minutes + ":" + seconds + ":" + millis;

                    if (timer.length() > 8)
                        timer = timer.substring(0, 8);

                    draw();

                    if(touchBarrel){
                        running=false;
                        if(timerstarted) {
                            objStopWatch.stop();
                            timerstarted = false;
                        }
                        gameOver=true;
                        draw();
                        if(MyActivity.bGameSound) {
                            bgMusic.stop();
                        }
                        bGameStatus = false;
                        mStartTime = 0;
                        millis = 00;
                        seconds = 00;
                        minutes = 00;
                        timer = "0:00:00";
                        bGamePaused = false;
                        bGameStatus = false;
                        barrel1Completed = false;
                        barrel2Completed =false;
                        barrel3Completed = false;
                    }

                    if(bGameCompleted) {
                            timer = "0:00:00";
                            mStartTime = 0;
                            millis = 00;
                            seconds = 00;
                            minutes = 00;
                            bGamePaused = false;
                            bGameStatus = false;
                            barrel1Completed = false;
                            barrel2Completed =false;
                            barrel3Completed = false;
                            hitfence = false;
                        Intent intentAddScore = new Intent(Game.this, AddScore.class);
                        startActivity(intentAddScore);
                        running = false;
                    }
                }
            }
        }

    }
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onSensorChanged
    //  Paramters       : SensorEvent
    //  Returns         : none
    //  Description     : function to get the latest sensor values 
    ////////////////////////////////////////////////////////////////////////////
    public void onSensorChanged(SensorEvent sensorEvent) {

        //Try synchronize the events
        synchronized(this){
            //For each sensor
            switch (sensorEvent.sensor.getType()) {
                /*case Sensor.TYPE_MAGNETIC_FIELD: //Magnetic sensor to know when the screen is landscape or portrait
                    //Save values to calculate the orientation
                    mMagneticValues = sensorEvent.values.clone();
                    break;*/
                case Sensor.TYPE_ACCELEROMETER://Accelerometer to move the ball
                    if (Configuration.ORIENTATION_LANDSCAPE == this.getResources().getConfiguration().orientation){//Landscape
                        //Positive values to move on x
                        if (sensorEvent.values[1]>0){
                            //In margenMax I save the margin of the screen this value depends of the screen where we run the application. With this the ball not disapears of the screen
                            if (x<=margenMaxX){
                                //We plus in x to move the ball
                                x = x + (int) Math.pow(sensorEvent.values[1], 2);
                            }
                        }
                        else{
                            //Move the ball to the other side
                            if (x>=margenMinX){
                                x = x - (int) Math.pow(sensorEvent.values[1], 2);
                            }
                        }
                        //Same in y
                        if (sensorEvent.values[0]>0){
                            if (y<=margenMaxY){
                                y = y + (int) Math.pow(sensorEvent.values[0], 2);
                            }
                        }
                        else{
                            if (y>=margenMinY){
                                y = y - (int) Math.pow(sensorEvent.values[0], 2);
                            }
                        }
                    }
                    else{//Portrait
                        //Eje X
                        if (sensorEvent.values[0]<0){
                            if (x<=margenMaxX){
                                x = x + (int) Math.pow(sensorEvent.values[0], 2);
                            }
                        }
                        else{
                            if (x>=margenMinX){
                                x = x - (int) Math.pow(sensorEvent.values[0], 2);
                            }
                        }
                        //Eje Y
                        if (sensorEvent.values[1]>0){
                            if (y<=margenMaxY){
                                y = y + (int) Math.pow(sensorEvent.values[1], 2);
                            }
                        }
                        else{
                            if (y>=margenMinY){
                                y = y - (int) Math.pow(sensorEvent.values[1], 2);
                            }
                        }

                    }
                    //Save the values to calculate the orientation
                    //mAccelerometerValues = sensorEvent.values.clone();
                    break;
                default:
                    break;
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onAccuracyChanged
    //  Paramters       : Sensor arg0, int arg1
    //  Returns         : none
    //  Description     : function to get the latest sensor values 
    ////////////////////////////////////////////////////////////////////////////
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onResume
    //  Paramters       : none
    //  Returns         : none
    //  Description     : overriden on reusme
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        mStartTime = 0;
        bGamePaused = false;
        bGameStatus = false;
        barrel1Completed = false;
        barrel2Completed =false;
        barrel3Completed = false;


    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onPause
    //  Paramters       : none
    //  Returns         : none
    //  Description     : overriden on pause
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        if(null != bgMusic){
            if(MyActivity.bGameSound) {
                bgMusic.stop();
            }
        }
        // Unregister the listener and set back to default
        UnregisterSensor();
        if(timerstarted) {
            objStopWatch.stop();
            timerstarted = false;
        }
        mStartTime = 0;
        timer = "0:00:00";
        bGamePaused = false;
        bGameStatus = false;
        barrel1Completed = false;
        barrel2Completed =false;
        barrel3Completed = false;
        super.onPause();
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onStop
    //  Paramters       : none
    //  Returns         : none
    //  Description     : overriden on onstop
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop()
    {
        super.onStop();
		//release the music
        if(null != bgMusic){
            bgMusic.release();

        }

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : scaleDown
    //  Paramters       : Bitmap realImage, float maxImageSize,boolean filter
    //  Returns         : Bitmap
    //  Description     : to scale up/down an image 
    ////////////////////////////////////////////////////////////////////////////
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onCreateOptionsMenu
    //  Paramters       : Menu
    //  Returns         : none
    //  Description     : overriden on onCreateOptionsMenu
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.highscore, menu);
        return super.onCreateOptionsMenu(menu);
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : onOptionsItemSelected
    //  Paramters       : MenuItem
    //  Returns         : none
    //  Description     : overriden on onOptionsItemSelected
    ////////////////////////////////////////////////////////////////////////////     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.highscoreaction: {
                if(MyActivity.m_objDataHandler.getListSize() == 0){
                    Toast.makeText(getApplicationContext(), "No Scores Available!!!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intentHighScore = new Intent(this, HighScores.class);
                startActivity(intentHighScore);
            }
            break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

}

///////////////////////////////////////////////////////////////////////////////
//  Class Name  : Stopwatch
//  Access      : Package Private
//  Description : Used for the timer maintained in the game
///////////////////////////////////////////////////////////////////////////////
 class Stopwatch {
 
	//Member variables
    public long startTime = 0;
    public long penaltyTime = 0;
    private boolean running = false;
    private long currentTime = 0;
    private int penaltyCount = 0;

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : setPenaltyCount
    //  Paramters       : None
    //  Returns         : None
    //  Description     : set the number of times it hit the boundary
    ////////////////////////////////////////////////////////////////////////////
    void setPenaltyCount(){
        this.penaltyCount = this.penaltyCount + 1;
        this.penaltyTime = 5000*penaltyCount;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : start
    //  Paramters       : None
    //  Returns         : None
    //  Description     : start the timer
    ////////////////////////////////////////////////////////////////////////////
    public void start() {
        System.out.println("Stop Watch GetTime :: " + System.currentTimeMillis());
        this.startTime = System.currentTimeMillis();
        this.running = true;
        System.out.println("Stop Watch Start :: " + this.startTime);

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : stop
    //  Paramters       : None
    //  Returns         : long
    //  Description     : stop the timer
    ////////////////////////////////////////////////////////////////////////////
    public long stop() {
        this.running = false;
        long lastTime = this.elapsed;
        elapsed = 0;
        startTime = 0;
        penaltyTime = 0;
        currentTime = 0;
        penaltyCount = 0;
        return lastTime;

    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : pause
    //  Paramters       : None
    //  Returns         : None
    //  Description     : pause the timer
    ////////////////////////////////////////////////////////////////////////////
    public void pause() {
        this.running = false;
        System.out.println("Stop Watch Get Pause:: " + System.currentTimeMillis());
        currentTime = System.currentTimeMillis() - startTime;
        System.out.println("Stop Watch Current Time :: " + currentTime);


    }
	
	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : resume
    //  Paramters       : None
    //  Returns         : None
    //  Description     : resume the timer
    ////////////////////////////////////////////////////////////////////////////
    public void resume() {
        this.running = true;
        System.out.println("Stop Watch Get Resume:: " + System.currentTimeMillis());
        this.startTime = System.currentTimeMillis() - currentTime;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getElapsedTimeMili
    //  Paramters       : None
    //  Returns         : long
    //  Description     : getElapsedTimeMili
    ////////////////////////////////////////////////////////////////////////////
    long elapsed = 0;
    //elaspsed time in milliseconds
    public long getElapsedTimeMili() {

        if (this.running) {
            this.elapsed = System.currentTimeMillis() - this.startTime + this.penaltyTime;
        }
        System.out.println("Stop Watch Get Elapsed:: " + System.currentTimeMillis());
        return this.elapsed;
    }


	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getElapsedTimeSecs
    //  Paramters       : None
    //  Returns         : long
    //  Description     : //elaspsed time in seconds
    ////////////////////////////////////////////////////////////////////////////
    public long getElapsedTimeSecs() {
        long elapsed = 0;
        if (this.running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
        }
        return elapsed;
    }

	////////////////////////////////////////////////////////////////////////////
    //  Method Name     : getCurrentTime
    //  Paramters       : None
    //  Returns         : None
    //  Description     : getCurrentTime
    ////////////////////////////////////////////////////////////////////////////
    public long getCurrentTime() {

        if(this.running){
            return System.currentTimeMillis();}

        return 0;
    }
}