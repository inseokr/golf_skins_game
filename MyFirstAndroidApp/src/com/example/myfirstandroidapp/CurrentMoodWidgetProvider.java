package com.example.myfirstandroidapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.myfirstandroidapp.R;

import android.content.Context;
import android.graphics.Color;

public class CurrentMoodWidgetProvider extends AppWidgetProvider {
	public static final String WIDGETTAG = "WidgetMood";
	public static int widgetid=0;
	public static RemoteViews rviews;
	public static AppWidgetManager widgetMan;
	private skinsGameInstance gameInstance=null;
	static Intent uploadScore;
	PendingIntent uploadScorePendingIntent;
	static Intent scoreUpdate[];
	static PendingIntent temp_pendingIntent[];
	
	public CurrentMoodWidgetProvider()
	{
		gameInstance = skinsGame.getInstance().getGame();
		scoreUpdate = new Intent[4];
	    temp_pendingIntent = new PendingIntent[4];
		
	}
	
	public static void updateTextView(int widgetid_, int viewId, String text)
	{
		rviews.setTextViewText(viewId, text);
		if(widgetid_==0)
		{
			widgetMan.updateAppWidget(widgetid, rviews);
		} else
		{
			widgetMan.updateAppWidget(widgetid_, rviews);
		}
	}
	
	public static void updateTextColor(int widgetid_, int viewId, int colorCode)
	{
		rviews.setTextColor(viewId, colorCode);
		if(widgetid_==0)
		{
			widgetMan.updateAppWidget(widgetid, rviews);
		} else
		{
			widgetMan.updateAppWidget(widgetid_, rviews);
		}
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	    //Log.i(WIDGETTAG, "onUpdate");
		Log.v("Widget", "onUpdate");
		
		final int N = appWidgetIds.length;
		widgetMan = appWidgetManager;
		
		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
		    int appWidgetId = appWidgetIds[i];
		    widgetid = appWidgetId;
		    		    
		    //Log.i(WIDGETTAG, "updating widget[id] " + appWidgetId);

		    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
		    rviews = views;
		    
		    // score update
		    for(int playerIdx=0; playerIdx<4; playerIdx++)
		    {
		    	scoreUpdate[playerIdx] = new Intent(context, CurrentMoodService.class);
		    	scoreUpdate[playerIdx].setAction(CurrentMoodService.REFRESH_SCORE[playerIdx]);
		    	temp_pendingIntent[playerIdx] = PendingIntent.getService(context, 0, scoreUpdate[playerIdx], 0);
			    
			    views.setOnClickPendingIntent(CurrentMoodService.scoreViewIds[playerIdx], temp_pendingIntent[playerIdx]);
			    
			    if(gameInstance!=null)
			    {
			    	// Update view using gameInstance.
			    	views.setTextViewText(CurrentMoodService.playerViewIds[playerIdx], 
			    						gameInstance.players.get(playerIdx).getName());
			    	views.setTextViewText(CurrentMoodService.scoreViewIds[playerIdx], 
			    			Integer.toString(gameInstance.getAdjustedTotal(playerIdx, gameInstance.curHole-1)));
			    }
		    }
		    
		    CurrentMoodWidgetProvider.updateTextView(0, R.id.teamA_Score,
    				Integer.toString(gameInstance.teams.get(0).getNumOfWins()));
		    CurrentMoodWidgetProvider.updateTextView(0, R.id.teamB_Score,
				Integer.toString(gameInstance.teams.get(1).getNumOfWins()));
		    CurrentMoodWidgetProvider.updateTextView(0, R.id.holeNumberView,
				Integer.toString(gameInstance.curHole));
		    
		    if(gameInstance.getHandiHole(gameInstance.curHole-1)==true)
		    {
		    	views.setTextColor(R.id.holeNumberView, Color.parseColor("#FF0000"));
		    }
		    
		    uploadScore = new Intent(context, CurrentMoodService.class);
		    uploadScore.setAction(CurrentMoodService.UPLOAD_SCORE);
		    uploadScore.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    uploadScore.putExtra("appPlayerIndexNumber", 2);
		    uploadScorePendingIntent = PendingIntent.getService(context, 0, uploadScore, 0);
		    views.setOnClickPendingIntent(R.id.UpdateButton, uploadScorePendingIntent);
		    
		    Log.v(WIDGETTAG, "upload_score. widgetid = " + appWidgetId);
		    
		    int widgetId = uploadScore.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
		    int playerIdx = uploadScore.getIntExtra("appPlayerIndexNumber", 0);
		    Log.v(WIDGETTAG, "upload_score. widgetId = " + widgetId);
		    Log.v(WIDGETTAG, "upload_score. appPlayerIndexNumber = " + playerIdx);
		    
		    // Tell the AppWidgetManager to perform an update on the current App Widget
		    appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}	
}
