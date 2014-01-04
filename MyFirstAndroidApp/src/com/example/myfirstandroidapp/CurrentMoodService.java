package com.example.myfirstandroidapp;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.myfirstandroidapp.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


public class CurrentMoodService extends Service {
	public static final String FETCH_PLAYER = "FetchPlayer";
	public static final String REFRESH_SCORE[] = {"RefreshScore_1", "RefreshScore_2", "RefreshScore_3", "RefreshScore_4"};
	public static final String UPLOAD_SCORE = "UploadScore";
	public static final String CURRENTMOOD = "CurrentMood";
	public static final String REFRESH_GAME_STATUS = "RefreshGameStatus";
	private static skinsGameInstance gameInstance;
	private static int widgetId=0;
	public static int scoreViewIds[]={R.id.player1_score, R.id.player2_score, R.id.player3_score, R.id.player4_score};
	public static int playerViewIds[]={R.id.teamA_player1, R.id.teamA_player2, R.id.teamB_player1, R.id.teamB_player2};
	private static int curScores[]={-1,-1,-1,-1};
	
	public CurrentMoodService(){
		gameInstance = skinsGame.getInstance().getGame();
		for(int playerIdx=0;playerIdx<4;playerIdx++) curScores[playerIdx] = -1;
	}


	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		
		gameInstance = skinsGame.getInstance().getGame();
		
        Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "onStartCommand");        
        manageGame(intent);

        //stopSelf(startId);
		
		return START_STICKY;
	}

	private int getPlayerIdxByAction(String action_)
	{
		if(action_.equals(REFRESH_SCORE[0])) return 0;
		else if(action_.equals(REFRESH_SCORE[1])) return 1;
		else if(action_.equals(REFRESH_SCORE[2])) return 2;
		else if(action_.equals(REFRESH_SCORE[3])) return 3;
		else return -1;
	}
	
	private void refreshView()
	{
		if(CurrentMoodWidgetProvider.isCreated()==false) return;
		
		Log.v("CurrentModdService", "refreshView: curHole = " + gameInstance.curHole);
		
		for(int playerIdx=0; playerIdx<4; playerIdx++)
    	{
			//Log.v("CurrentModdService", "playerIdx = " + playerIdx);
			
    		CurrentMoodWidgetProvider.updateTextView(0, CurrentMoodService.scoreViewIds[playerIdx],
    				Integer.toString(gameInstance.getAdjustedTotal(playerIdx, gameInstance.curHole-1)));
    	}
    	
    	CurrentMoodWidgetProvider.updateTextView(0, R.id.teamA_Score,
    				Integer.toString(gameInstance.teams.get(0).getNumOfWins()));
    	
    	Log.v("CurrentModdService", "teamA_Score = " + gameInstance.teams.get(0).getNumOfWins());
    	
    	CurrentMoodWidgetProvider.updateTextView(0, R.id.teamB_Score,
				Integer.toString(gameInstance.teams.get(1).getNumOfWins()));
    	CurrentMoodWidgetProvider.updateTextView(0, R.id.holeNumberView,
				Integer.toString(gameInstance.curHole));
    	
    	if(gameInstance.getHandiHole(gameInstance.curHole-1)==true)
	    {
    		CurrentMoodWidgetProvider.updateTextColor(0, R.id.holeNumberView, Color.parseColor("#FF0000"));
	    } 
    	else
	    {
    		CurrentMoodWidgetProvider.updateTextColor(0, R.id.holeNumberView, Color.parseColor("#FFFFFF"));
	    }
	}
	
	private void manageGame(Intent intent) {
				
        Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "This is the intent " + intent);
        if (intent != null){
    		String requestedAction = intent.getAction();
    		
    		if (requestedAction != null){ 
    			int playerIdx = getPlayerIdxByAction(requestedAction);
	            if(playerIdx!=-1)
	            {	
	            	curScores[playerIdx] += 1;
	            	if(curScores[playerIdx]>gameInstance.getParStrokes()) curScores[playerIdx]=-1;
	            	
	            	//Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "curScores = " + curScores[playerIdx]);
	            	
	            	CurrentMoodWidgetProvider.updateTextView(0, scoreViewIds[playerIdx],Integer.toString(curScores[playerIdx]));
	            	//Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "Update Score!");
	            }
	            else if(requestedAction.equals(UPLOAD_SCORE))
	            {
	            	int parameter = intent.getIntExtra("appPlayerIndexNumber", 0);
	            	
	            	//int widgetId= intent.getIntExtra((AppWidgetManager.EXTRA_APPWIDGET_ID,0);
	            	int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);       	
	            	// update scores, TBD
	            	//Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "Upload score!. widgetId = " + widgetId);
	            	//Log.v(CurrentMoodWidgetProvider.WIDGETTAG, "Upload score!. appPlayerIndexNumber = " + parameter);
	            	
	            	Log.v("Service", "curHole = " + gameInstance.curHole);
	            	for(playerIdx=0; playerIdx<4; playerIdx++)
	            	{
	            		gameInstance.players.get(playerIdx).setScore(gameInstance.curHole-1, 
	            				curScores[playerIdx]+gameInstance.getParStrokes(gameInstance.curHole-1));
	            		//Log.v("Service", "curScores = " + curScores[playerIdx]);
	            	}
	            	
	            	gameInstance.processHole(gameInstance.curHole-1, curScores, false);
	            	
	            	gameInstance.curHole = gameInstance.curHole + 1;
	            	gameInstance.saveGameData(getApplicationContext());
	            	
	            	
	            	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
	            	Log.v("Service", "starting mainActivity");
	            	Bundle bnd = new Bundle();
	                bnd.putInt("ResumeGame", gameInstance.curHole);
	                mainActivity.putExtras(bnd);
	                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(mainActivity);
	                
	                refreshView();
	            	for(playerIdx=0; playerIdx<4; playerIdx++)
	            	{
	            		curScores[playerIdx]=0;
	            	}	            	
	            }
	            else if(requestedAction.equals(REFRESH_GAME_STATUS))
	            {
	            	refreshView();
	            }
    		}
        }
        else
        {
        	//AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
        	//RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widgetlayout);
        	
        	//for(int playerIdx=0; playerIdx<4; playerIdx++)
        	//{
        		//views.setTextViewText(scoreViewIds[playerIdx], Integer.toString(3));
        	//}
        	
        	//appWidgetMan.updateAppWidget(widgetId, views);
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
