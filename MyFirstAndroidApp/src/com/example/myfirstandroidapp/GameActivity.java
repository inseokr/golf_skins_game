package com.example.myfirstandroidapp;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.ImageButton;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myfirstandroidapp.R;

import java.util.ListIterator;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Context;
import android.content.SharedPreferences;

public class GameActivity extends Activity implements LocationListener {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	Log.v("GameActivity", "GameActivity created");
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
    
    gameInstance = skinsGame.getInstance().getGame();
    
    if(gameInstance==null) 
    {
    	Log.v("GameActivity", "Game Resource deallocated");
    	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
    	
        startActivity(mainActivity);
        return;
    }
    
    for(int playerIdx=0; playerIdx<4;playerIdx++)
    	registerWheelView(playerCurrScoreViews[playerIdx], diffs, diffs.length);
    
    Bundle bundle = this.getIntent().getExtras();
    if(bundle!=null)
    {
    	int lastPlayedHole = bundle.getInt("ResumeGame");
    	if(lastPlayedHole>0)
    	{
    		// replay game based on saved scores.
    		restoreGameData(lastPlayedHole);
    	}
    }
	
    if(gameInstance.curHole>1) refreshGameStatus();
    
    if(gameInstance.players.isEmpty()) 
    {
    	Log.v("GameActivity", "No Players");
    	
    	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
    	
        startActivity(mainActivity);
        
    	
        return;
    }
    
    for(int playerIdx=0; playerIdx < 4; playerIdx++)
    {
    	Button tempPlayerName = (Button) findViewById(playerViewIds[playerIdx]);
    	
    	if(playerIdx<gameInstance.getNumOfPlayers())
    	{
    		tempPlayerName.setText(gameInstance.players.get(playerIdx).getName());
       
    		tempPlayerName.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View v) {
    				Intent statusActivity = new Intent(getApplicationContext(), StatusActivity.class);
    				Bundle bnd = new Bundle();
    				bnd.putString("playerIdx", Integer.toString(getPlayerViewIdx(v.getId())));
    				statusActivity.putExtras(bnd);
    				startActivity(statusActivity);                
    			}
    		});
    	}
    	else
    	{
    		tempPlayerName.setText("EMPTY");
    	}

    }

        
    // It's the way how to refresh the view?? Unbelievable!!
    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
    tableLayout.setVisibility(View.INVISIBLE);
    tableLayout.setVisibility(View.VISIBLE);
     
    final ImageButton homeButton = (ImageButton) findViewById(R.id.goHomeButton);
    homeButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        	Bundle bnd = new Bundle();
            bnd.putString("gameFinished", "true");
            mainActivity.putExtras(bnd);
            resetGameActivity();
            startActivity(mainActivity);                
        }
    });
	
	final Button updateButton = (Button) findViewById(R.id.updateHoleButton);
	updateButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	updateGameStatus();           
        };
    });
	
	final Button holeButton = (Button) findViewById(R.id.holeViewButton);
	holeButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final WheelView holeSelection = (WheelView) findViewById(R.id.HoleSelection);
			int holeNum = gameInstance.curHole-1;		    
	    	holeSelection.setCurrentItem(holeNum<0? 0: holeNum);
	    	holeSelection.setVisibility(View.INVISIBLE);
	    	holeSelection.setVisibility(View.VISIBLE);
		}
	});
	

	final Button yardViewButton = (Button) findViewById(R.id.yardView);
	yardViewButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			updateLocation();
			
		}
	});

	final WheelView holeSelection = (WheelView) findViewById(R.id.HoleSelection);
    
    holeSelection.setVisibleItems(18);
    String hole_name[] = {"Hole 1", "Hole 2", "Hole 3", "Hole 4",
    		                 "Hole 5", "Hole 6", "Hole 7", "Hole 8",
    		                 "Hole 9", "Hole 10", "Hole 11", "Hole 12",
    		                 "Hole 13", "Hole 14", "Hole 15", "Hole 16",
    		                 "Hole 17", "Hole 18"};
    ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this, hole_name);
    adapter.setTextSize(15);
    holeSelection.setViewAdapter(adapter);
    
    
    try
    {
    	int holeNum = gameInstance.curHole-1;
    
    	holeSelection.setCurrentItem(holeNum<0? 0: holeNum);
    
    	boolean bIsHandiHole = gameInstance.getHandiHole(holeNum<0? 0: holeNum);
    	this.adjustColorsBasedOnHandiInformation(bIsHandiHole);

	} catch(ArrayIndexOutOfBoundsException e)
	{
	}
    
    holeSelection.addChangingListener(new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
		    selectedHoleNum = newValue + 1;
		    skinsGameInstance gameInstance = skinsGame.getInstance().getGame();
		    holeLog tempLog = gameInstance.holeLogs.get(newValue);
		    //Log.v("GameActivity", "holeSelection onChanged");
			if(tempLog.isHoleProcessed()==true)
			{
				//Log.v("GameActivity", "This hole is already processed, refreshing player information");
				refreshPlayerInfo(selectedHoleNum);
			}
			else {

				//Log.v("GameActivity", "A new hole, just show last hole information");
				if(gameInstance.curHole>1)
					refreshPlayerInfo(gameInstance.curHole-1);
			}
			
			updateHoleInformation(selectedHoleNum-1);
			
			boolean bIsHandiHole = gameInstance.getHandiHole(selectedHoleNum-1);
			adjustColorsBasedOnHandiInformation(bIsHandiHole);
		}
	});
    
    
	//mHandler.postDelayed(new Runnable() {
	//	public void run() {
	//		Log.v("MainActivity", "Finally I'm awake");
	//		keepMeAwake();
    //   }
	//}, 3000);
	
	updateHoleInformation(gameInstance.curHole-1);
	
	
	locationManager = (LocationManager) getSystemService(this.getApplicationContext().LOCATION_SERVICE);

	}
	
	public void keepMeAwake(){
		mHandler.postDelayed(new Runnable() {
			public void run() {
				//Log.v("MainActivity", "Finally I'm awake");
				keepMeAwake();
           }
		}, 1000*3600);
	}
	
	private void refreshPlayerInfo(int holeNum)
	{
		// update score and totals
		TextView playerTotalScoreView;
		WheelView playerScoreView;
		for(int playerIdx=0; playerIdx < gameInstance.getNumOfPlayers(); playerIdx++)
		{
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[playerIdx]);
			
			if(gameInstance.getGameMode()==skinsGameInstance.TEAM_SKINS)
				playerTotalScoreView.setText(Integer.toString(gameInstance.getAdjustedTotal(playerIdx, holeNum)));	
			else
				playerTotalScoreView.setText(Integer.toString(gameInstance.players.get(playerIdx).getNumOfWins()));
			
			playerTotalScoreView.setVisibility(View.INVISIBLE);
			playerTotalScoreView.setVisibility(View.VISIBLE);
		
			playerScoreView = (WheelView) findViewById(playerCurrScoreViews[playerIdx]);
		
			if(gameInstance.getScoreMode()==skinsGameInstance.scoreMode.DIFFERENCE)
			{
				int scoreIdx = (gameInstance.players.get(playerIdx).getScore(holeNum-1)-gameInstance.getParStrokes(holeNum-1)) + 2;
				playerScoreView.setCurrentItem(scoreIdx);
				playerScoreView.setVisibility(View.INVISIBLE);
				playerScoreView.setVisibility(View.VISIBLE);
			}
			else {
				int scoreIdx = gameInstance.players.get(playerIdx).getScore(holeNum-1) - 1;
				playerScoreView.setCurrentItem(scoreIdx);
				playerScoreView.setVisibility(View.INVISIBLE);
				playerScoreView.setVisibility(View.VISIBLE);
			}
		}
		
		boolean bIsNextHandiHole = gameInstance.getHandiHole(holeNum-1);
		adjustColorsBasedOnHandiInformation(bIsNextHandiHole);
	}
		
	private void changePlayerViewColor(int view_id, int colorCode)
	{
		Button playerView = (Button) findViewById(view_id);
		
		playerView.setTextColor(colorCode);
		playerView.setVisibility(View.INVISIBLE);
		playerView.setVisibility(View.VISIBLE);
	}
	
	private  void updateGameStatus() {
		
		// get hole number
		final WheelView holeEditText = (WheelView) findViewById(R.id.HoleSelection);
		int holeNum = selectedHoleNum;
		
		
		// update scores
		for(int playerIdx=0;playerIdx<gameInstance.getNumOfPlayers();playerIdx++){
			curScores[playerIdx] = getAdjustedScore(curSelectedScoreItems[playerIdx], holeNum -1);
			gameInstance.players.get(playerIdx).setScore(holeNum-1,curScores[playerIdx]);
		}
		
		holeLog tempLog;
		
		try
		{
			tempLog = gameInstance.holeLogs.get(holeNum-1);
			if(tempLog.isHoleProcessed()==true) 
			{
				int lastPlayedHole = gameInstance.curHole - 1;
				gameInstance.saveGameData(this.getApplicationContext());
				gameInstance.resetGame(false);
				this.restoreGameData(lastPlayedHole);
				this.refreshGameStatus();
				
				if(selectedHoleNum<18)
			    {
			    	holeEditText.setCurrentItem(selectedHoleNum-1);
			    
			    	holeEditText.setVisibility(View.INVISIBLE);
			    	holeEditText.setVisibility(View.VISIBLE);
			    }
				return;
				
				//tempLog.cancelHole();
				//if(tempLog.isDouble()==true) gameInstance.numCarriedHoles--;
			} 
			else
			{
				gameInstance.curHole = holeNum + 1;
			}
		
		} catch(ArrayIndexOutOfBoundsException e)
		{
			Log.v("GameActivity", "Exception" + e);
			Log.v("GameActivity", "holeNum = " + holeNum);
			Log.v("GameActivity", "holeLogs size = " + gameInstance.holeLogs.size());
			return; 
		}
		
		
		if(gameInstance.getGameMode()==skinsGameInstance.TEAM_SKINS)
		{
		boolean bIsHandiHole = gameInstance.getHandiHole(holeNum-1);
		int handiTeamIdx = gameInstance.getHandiTeam();
		int teamA_score,teamB_score;
		
		try
		{
			boolean bIsNextHandiHole = gameInstance.getHandiHole(holeNum);
			adjustColorsBasedOnHandiInformation(bIsNextHandiHole);
		}catch(ArrayIndexOutOfBoundsException e)
		{
			Log.v("GameActivity","hole number can't exceed 18");
		}
				
		//Log.v("GameActivity", "handiTeamIdx = " + handiTeamIdx);
		
		if(bIsHandiHole && handiTeamIdx==0)
		{
			int betterPlayer = (gameInstance.players.get(2).getScore(holeNum-1)<gameInstance.players.get(3).getScore(holeNum-1))? 0: 1;
			if(betterPlayer==0)
			{
				teamB_score = (gameInstance.players.get(2).getScore(holeNum-1) + 1) * 
			                  gameInstance.players.get(3).getScore(holeNum-1);
			} else
			{
				teamB_score = gameInstance.players.get(2).getScore(holeNum-1) * 
		                      (gameInstance.players.get(3).getScore(holeNum-1)+1);				
			}
		} 
		else
		{
			teamB_score = gameInstance.players.get(2).getScore(holeNum-1) * 
					  	  gameInstance.players.get(3).getScore(holeNum-1);
		}
		
		if(bIsHandiHole && handiTeamIdx==1)
		{
			int betterPlayer = (gameInstance.players.get(0).getScore(holeNum-1)<gameInstance.players.get(1).getScore(holeNum-1))? 0: 1;
			if(betterPlayer==0)
			{
				teamA_score = (gameInstance.players.get(0).getScore(holeNum-1) + 1) * 
			                  gameInstance.players.get(1).getScore(holeNum-1);
			} else
			{
				teamA_score = gameInstance.players.get(0).getScore(holeNum-1) * 
		                      (gameInstance.players.get(1).getScore(holeNum-1) + 1);

				
			}
		} 
		else
		{
			teamA_score = gameInstance.players.get(0).getScore(holeNum-1) * 
					  	  gameInstance.players.get(1).getScore(holeNum-1);
		}
		
		
		//Log.v("GameActivity", "teamA_score = " + teamA_score + ", teamB_score =" + teamB_score);
		
		if(teamA_score < teamB_score){ // team A win
			team tempTeamWinner = gameInstance.teams.get(0);
			tempTeamWinner.updateHole(true, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
					
					
			team tempTeamLoser = gameInstance.teams.get(1);
			tempTeamLoser.updateHole(false, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
					
			tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (gameInstance.numCarriedHoles>0));
					
			if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;		
					
		}
		else if(teamA_score > teamB_score)
		{
			team tempTeamWinner = gameInstance.teams.get(1);
			tempTeamWinner.updateHole(true, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
								
			team tempTeamLoser = gameInstance.teams.get(0);
			tempTeamLoser.updateHole(false, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
								
			tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (gameInstance.numCarriedHoles>0));
					
			if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;
		}
		else
		{
			gameInstance.numCarriedHoles++;
			tempLog.tied();
		}
		// Update team status
				final EditText tempATeamScore = (EditText)findViewById(R.id.teamAScoreEdit);
				tempATeamScore.setText(Integer.toString(gameInstance.teams.get(0).getNumOfWins()));
			    final EditText tempBTeamScore = (EditText)findViewById(R.id.teamBScoreEdit);
			    tempBTeamScore.setText(Integer.toString(gameInstance.teams.get(1).getNumOfWins()));
		}
		// INDIVIDUAL SKINS
		else
		{
			int lowestScoreIdx = 0;
			boolean tied = false;
			for(int playerIdx = 1; playerIdx < gameInstance.getNumOfPlayers(); playerIdx++)
			{
				if(curScores[playerIdx] < curScores[lowestScoreIdx])
				{
					lowestScoreIdx = playerIdx;
					tied = false;
				}
				else if(curScores[playerIdx] == curScores[lowestScoreIdx])
				{
					tied = true;
				}
			}
					
			if(tied==true)
			{
				gameInstance.numCarriedHoles++;
				tempLog.tied();
			} 
			else
			{
				gameInstance.players.get(lowestScoreIdx).win(5);
				if(gameInstance.numCarriedHoles>0) 
					gameInstance.players.get(lowestScoreIdx).win(5);
				tempLog.updateHoleLog(gameInstance.players.get(lowestScoreIdx),(gameInstance.numCarriedHoles>0) ?  true: false);
				if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;	
			}			
		}
		
		
		
		// update player labels
		ListIterator <player>iterator = gameInstance.players.listIterator();
		    
		int idx=0;
		TextView playerTotalScoreView;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[idx]);
			if(gameInstance.getGameMode()==skinsGameInstance.TEAM_SKINS)
				playerTotalScoreView.setText(Integer.toString(gameInstance.getAdjustedTotal(idx, holeNum)));	
			else
				playerTotalScoreView.setText(Integer.toString(currPlayer.getNumOfWins()));	
		//String newStatusInfo = "Hole " + curHole + " is in progress." + "Number of carried holes = " + numCarriedHoles;
		    idx++;
		}
		
		TextView carriedHoles = (TextView)findViewById(R.id.carriedHoles);
		carriedHoles.setText(Integer.toString(gameInstance.numCarriedHoles));
		carriedHoles.setVisibility(View.INVISIBLE);
		carriedHoles.setVisibility(View.VISIBLE);
		
		// It's the way how to refresh the view?? Unbelievable!!
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	   
	    tableLayout = (TableLayout) findViewById(R.id.tableLayout2);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    
	    if(selectedHoleNum<18)
	    {
	    	holeEditText.setCurrentItem(selectedHoleNum);
	    
	    	holeEditText.setVisibility(View.INVISIBLE);
	    	holeEditText.setVisibility(View.VISIBLE);
	    }
	    
	    //Log.v("GameActivity", "selectedHoleNum = " + selectedHoleNum);
	    
	    gameInstance.saveGameData(this.getApplicationContext());
	    
	    updateHoleInformation(selectedHoleNum-1);
	    
	    //Log.v("GameActivity", "before starting service: curHole = " + gameInstance.curHole);
	    
	    Intent refreshWidget = new Intent(getApplicationContext(), CurrentMoodService.class);
	    refreshWidget.setAction(CurrentMoodService.REFRESH_GAME_STATUS);
	    	    
	    startService(refreshWidget);
	}
	
	
private  void refreshGameStatus() {
			
		//Log.v("GameActivity", "refreshGameStatus. holeNum = " + gameInstance.curHole);
			
		int holeNum = gameInstance.curHole-1;
		
		if(holeNum<0) holeNum = 0;
		
		
		if(gameInstance.players.isEmpty()) 
		{
			Log.v("GameActivity", "No players found");
			return;
		}
		
		WheelView tempCurrScoreView;
		
		for(int playerIdx=0;playerIdx<gameInstance.getNumOfPlayers();playerIdx++){
			
			tempCurrScoreView = (WheelView) findViewById(playerCurrScoreViews[playerIdx]);
			tempCurrScoreView.setCurrentItem( curSelectedScoreItems[playerIdx]);
			tempCurrScoreView.setVisibility(View.INVISIBLE);
			tempCurrScoreView.setVisibility(View.VISIBLE);
		}
		
		
		if(gameInstance.teams.isEmpty()) 
		{
			Log.v("GameActivity", "No teams found");
			return;
		}
		
		// Update team status
		final EditText tempATeamScore = (EditText)findViewById(R.id.teamAScoreEdit);
		tempATeamScore.setText(Integer.toString(gameInstance.teams.get(0).getNumOfWins()));
	    final EditText tempBTeamScore = (EditText)findViewById(R.id.teamBScoreEdit);
	    tempBTeamScore.setText(Integer.toString(gameInstance.teams.get(1).getNumOfWins()));
		
		// update player labels
		ListIterator <player>iterator = gameInstance.players.listIterator();
		
		
		int idx=0;
		TextView playerTotalScoreView;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[idx]);
			if(gameInstance.getGameMode()==skinsGameInstance.TEAM_SKINS)
				playerTotalScoreView.setText(Integer.toString(gameInstance.getAdjustedTotal(idx, holeNum)));	
			else
				playerTotalScoreView.setText(Integer.toString(currPlayer.getNumOfWins()));	
		//String newStatusInfo = "Hole " + curHole + " is in progress." + "Number of carried holes = " + numCarriedHoles;
		    idx++;
		}
				
		
		TextView carriedHoles = (TextView)findViewById(R.id.carriedHoles);
		carriedHoles.setText(Integer.toString(gameInstance.numCarriedHoles));
		carriedHoles.setVisibility(View.INVISIBLE);
		carriedHoles.setVisibility(View.VISIBLE);
		
		// It's the way how to refresh the view?? Unbelievable!!
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    
	    tableLayout = (TableLayout) findViewById(R.id.tableLayout2);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    
	    
	    updateHoleInformation(holeNum+1);
	    
	    
	}

private void registerWheelView(int wheelViewId, String[] initString, int totalItems)
{
final WheelView tempWheelView = (WheelView) findViewById(wheelViewId);
    
	tempWheelView.setVisibleItems(totalItems);
    ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this, initString);
    adapter.setTextSize(15);
    tempWheelView.setViewAdapter(adapter);
        
    tempWheelView.addChangingListener(new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int playerIdx = getPlayerIdxByWheelId(wheel);
			
			// 1. find out selected items and convert it into integer value
			//curScores[playerIdx] = getAdjustedScore(newValue);
			curSelectedScoreItems[playerIdx] = newValue;
		}
	});
}

private static int getPlayerViewIdx(int view_id)
{
	for(int playerIdx=0; playerIdx<gameInstance.getNumOfPlayers(); playerIdx++)
	{
		if(playerViewIds[playerIdx] == view_id) return playerIdx;
	}
	
	return 0;
}

private int getAdjustedScore(int newValue, int holeIdx)
{
	int value;
	
	if(gameInstance.getScoreMode()==skinsGameInstance.scoreMode.DIFFERENCE)
	{
		value = Integer.parseInt(diffs[newValue]);
		//Log.v("GameActivity", "before adjustment = " + value);
		value = value + gameInstance.getParStrokes(holeIdx);
		//Log.v("GameActivity", "parStrokes = " + gameInstance.getParStrokes(holeIdx));
	} 
	else
	{
		value = Integer.parseInt(strokes[newValue]);
	}
	
	return value;
}

private int getPlayerIdxByWheelId(WheelView wheel)
{
	for(int idx=0; idx<gameInstance.getNumOfPlayers(); idx++)
	{
		if(wheel.getId()==playerCurrScoreViews[idx]) return idx;
	}
	
	return 0;
}

//obsolete
private void saveGameData()
{
	SharedPreferences  gameDataContainer = getApplicationContext().getSharedPreferences("SKINS_GAME_NAME", 0);
	SharedPreferences.Editor editor = gameDataContainer.edit();
	
	// 1. Game Settings
	editor.putInt("LastPlayedHole", gameInstance.curHole-1);
	editor.putInt("HandiTeamIdx", gameInstance.getHandiTeam());
	int handiHoles = 0x00000000;
	int handiMask  = 0x00000001;
	for(int holeIdx=0;holeIdx<18;holeIdx++)
	{
		if(gameInstance.handiHoles[holeIdx])
		{
			handiHoles |= handiMask << holeIdx;
		}
	}
	editor.putInt("HandiHoleList", handiHoles);
	
	// 2. Player Information
	// 2.1 Name
	editor.putString("TeamA_Player1_Name", gameInstance.players.get(0).getName());
	editor.putString("TeamA_Player2_Name", gameInstance.players.get(1).getName());
	editor.putString("TeamB_Player1_Name", gameInstance.players.get(2).getName());
	editor.putString("TeamB_Player2_Name", gameInstance.players.get(3).getName());
	
	// 2.2 scores
	for(int holeIdx=0;holeIdx<18;holeIdx++)
	{
		for(int playerIdx=0;playerIdx<gameInstance.getNumOfPlayers();playerIdx++)
			editor.putInt(scoreDataVariables[playerIdx][holeIdx], 
					      gameInstance.players.get(playerIdx).getScore(holeIdx));
	}
	
	editor.commit();
}


private void restoreGameData(int lastPlayedHole)
{
	SharedPreferences gameData = getApplicationContext().getSharedPreferences("SKINS_GAME_NAME", 0);
	int playerScores[]={0,0,0,0};
	
	
	gameInstance.setGameMode(gameData.getInt("SkinsType", 0));
	gameInstance.setNumOfPlayers(gameData.getInt("NumOfPlayers", 4));
	
	for(int holeIdx=0; holeIdx<lastPlayedHole; holeIdx++)
	{
		for(int playerIdx=0; playerIdx<gameInstance.getNumOfPlayers(); playerIdx++)
		{
			playerScores[playerIdx] = gameData.getInt(scoreDataVariables[playerIdx][holeIdx], 0);
		}
		gameInstance.processHole(holeIdx,playerScores,true);
	}
	
	gameInstance.curHole = lastPlayedHole+1;
	selectedHoleNum = lastPlayedHole+1;
}

private void processHole(int holeIdx, int scores[])
{
	holeLog tempLog;
	
	tempLog = gameInstance.holeLogs.get(holeIdx);
	
	// update scores
	for(int playerIdx=0; playerIdx<gameInstance.getNumOfPlayers(); playerIdx++)
		gameInstance.players.get(playerIdx).setScore(holeIdx, scores[playerIdx]);

	if(gameInstance.getGameMode() == skinsGameInstance.TEAM_SKINS)
	{
	boolean bIsHandiHole = gameInstance.getHandiHole(holeIdx);
	int handiTeamIdx = gameInstance.getHandiTeam();
	int teamA_score,teamB_score;

	if(bIsHandiHole && handiTeamIdx==0)
	{
		int betterPlayer = 
			(gameInstance.players.get(2).getScore(holeIdx)<gameInstance.players.get(3).getScore(holeIdx))? 0: 1;
		if(betterPlayer==0)
		{
			teamB_score = (gameInstance.players.get(2).getScore(holeIdx) + 1) * 
				                  gameInstance.players.get(3).getScore(holeIdx);
		} else
		{
			teamB_score = gameInstance.players.get(2).getScore(holeIdx) * 
			                      (gameInstance.players.get(3).getScore(holeIdx)+1);				
		}
	} 
	else
	{
		teamB_score = gameInstance.players.get(2).getScore(holeIdx) * 
						  	  gameInstance.players.get(3).getScore(holeIdx);
	}
			
	if(bIsHandiHole && handiTeamIdx==1)
	{
		int betterPlayer = (gameInstance.players.get(0).getScore(holeIdx)<gameInstance.players.get(1).getScore(holeIdx))? 0: 1;
		if(betterPlayer==0)
		{
			teamA_score = (gameInstance.players.get(0).getScore(holeIdx) + 1) * 
				                  gameInstance.players.get(1).getScore(holeIdx);
		} 
		else
		{
			teamA_score = gameInstance.players.get(0).getScore(holeIdx) * 
			                      (gameInstance.players.get(1).getScore(holeIdx) + 1);

					
		}
	} 
	else
	{
		teamA_score = gameInstance.players.get(0).getScore(holeIdx) * 
						  	  gameInstance.players.get(1).getScore(holeIdx);
	}
			
			
	//Log.v("GameActivity", "teamA_score = " + teamA_score + ", teamB_score =" + teamB_score);
			
	if(teamA_score < teamB_score){ // team A win
		team tempTeamWinner = gameInstance.teams.get(0);
		tempTeamWinner.updateHole(true, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
						
						
		team tempTeamLoser = gameInstance.teams.get(1);
		tempTeamLoser.updateHole(false, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
						
		tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (gameInstance.numCarriedHoles>0));
						
		if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;		
						
	}
	else if(teamA_score > teamB_score)
	{
		team tempTeamWinner = gameInstance.teams.get(1);
		tempTeamWinner.updateHole(true, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
									
		team tempTeamLoser = gameInstance.teams.get(0);
		tempTeamLoser.updateHole(false, gameInstance.betUnit, (gameInstance.numCarriedHoles>0) ?  2: 1, false);
									
		tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (gameInstance.numCarriedHoles>0));
						
		if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;
	}
	else
	{
		gameInstance.numCarriedHoles++;
		tempLog.tied();
	}
	}
	else
	{
		int lowestScoreIdx = 0;
		boolean tied = false;
		for(int playerIdx = 1; playerIdx < gameInstance.getNumOfPlayers(); playerIdx++)
		{
			if(curScores[playerIdx] < curScores[lowestScoreIdx])
			{
				lowestScoreIdx = playerIdx;
				tied = false;
			}
			else if(curScores[playerIdx] == curScores[lowestScoreIdx])
			{
				tied = true;
			}
		}
				
		if(tied==true)
		{
			gameInstance.numCarriedHoles++;
			tempLog.tied();
		} 
		else
		{
			gameInstance.players.get(lowestScoreIdx).win(5);
			if(gameInstance.numCarriedHoles>0) 
				gameInstance.players.get(lowestScoreIdx).win(5);
			tempLog.updateHoleLog(gameInstance.players.get(lowestScoreIdx),(gameInstance.numCarriedHoles>0) ?  true: false);
			if(gameInstance.numCarriedHoles>0) gameInstance.numCarriedHoles--;	
		}	
	}
}

private void updateHoleInformation(int holeIdx)
{
	if(holeIdx>=18) return;
	
	TextView yardView = (TextView) findViewById(R.id.yardView);
	TextView holeTypeView = (TextView) findViewById(R.id.holeTypeView);
	
	yardView.setText(Integer.toString(gameInstance.getYardages(holeIdx)));
	holeTypeView.setText(Integer.toString(gameInstance.getParStrokes(holeIdx)));
	
	yardView.setVisibility(View.INVISIBLE);
	yardView.setVisibility(View.VISIBLE);
	
	holeTypeView.setVisibility(View.INVISIBLE);
	holeTypeView.setVisibility(View.VISIBLE);
}

private void adjustColorsBasedOnHandiInformation(boolean bIsHandiHole){

	Button holeViewButton = (Button) findViewById(R.id.holeViewButton);
	
	if(bIsHandiHole==true)
	{	
		holeViewButton.setTextColor(Color.parseColor("#00ff00"));
		
	} else
	{
		holeViewButton.setTextColor(Color.parseColor("#ffffff"));
	}
	
	holeViewButton.setVisibility(View.INVISIBLE);
	holeViewButton.setVisibility(View.VISIBLE);
}


private void updateLocation()
{
		     
	// CAL METHOD requestLocationUpdates           
	// Parameters :
	//   First(provider)    :  the name of the provider with which to register
	//   Second(minTime)    :  the minimum time interval for notifications,
	//                         in milliseconds. This field is only used as a hint
	//                         to conserve power, and actual time between location
	//                         updates may be greater or lesser than this value.
	//   Third(minDistance) :  the minimum distance interval for notifications, in meters
	//   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location)
	//                         method will be called for each location update    
	locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
			3000,   // 3 sec
	        10, this); 
}

protected void onStart() {
	Log.v("GameActivity", "GameActivity onStart");
    super.onStart();
    // The activity is about to become visible.
}
@Override
protected void onResume() {
	Log.v("GameActivity", "GameActivity onResume");
    super.onResume();
    // The activity has become visible (it is now "resumed").
}
@Override
protected void onPause() {
	Log.v("GameActivity", "GameActivity onPause");
    super.onPause();
    // Another activity is taking focus (this activity is about to be "paused").
}
@Override
protected void onStop() {
	Log.v("GameActivity", "GameActivity onStop");
    super.onStop();
    // The activity is no longer visible (it is now "stopped")
}
@Override
protected void onDestroy() {
	Log.v("GameActivity", "GameActivity onDestroy");
    super.onDestroy();
    // The activity is about to be destroyed.
}

@Override

public void onBackPressed() {

    // TODO Auto-generated method stub
	// Will ignore it.

}

private void resetGameActivity(){
    selectedHoleNum =1;
	
    for(int playerIdx=0; playerIdx<4; playerIdx++)
    {
    	curScores[playerIdx]=0;
    	curSelectedScoreItems[playerIdx]=3;
    }
}

    static LocationManager locationManager;
    
	static int selectedHoleNum =1;
	
	static String strokes[]={"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String diffs[]={"-2", "-1", "0", "1", "2", "3", "4", "5"};
	static int curScores[]={0,0,0,0};
	static int curSelectedScoreItems[]={3,3,3,3};
	static int playerViewIds[] = {R.id.player1View, R.id.player2View, R.id.player3View, R.id.player4View };
	static int playerTotalScoreViews[]={R.id.player1TotalView, R.id.player2TotalView,
										R.id.player3TotalView, R.id.player4TotalView
	};
	static int playerCurrScoreViews[]={R.id.player1ScoreView, R.id.player2ScoreView,
		R.id.player3ScoreView, R.id.player4ScoreView
};
	static String scoreDataVariables[][] = {
		{"TeamA_Player1_Score1","TeamA_Player1_Score2","TeamA_Player1_Score3",
		 "TeamA_Player1_Score4","TeamA_Player1_Score5","TeamA_Player1_Score6",
		 "TeamA_Player1_Score7","TeamA_Player1_Score8","TeamA_Player1_Score9",
		 "TeamA_Player1_Score10","TeamA_Player1_Score11","TeamA_Player1_Score12",
		 "TeamA_Player1_Score13","TeamA_Player1_Score14","TeamA_Player1_Score15",
		 "TeamA_Player1_Score16","TeamA_Player1_Score17","TeamA_Player1_Score18",
		},
		{"TeamA_Player2_Score1","TeamA_Player2_Score2","TeamA_Player2_Score3",
		 "TeamA_Player2_Score4","TeamA_Player2_Score5","TeamA_Player2_Score6",
		 "TeamA_Player2_Score7","TeamA_Player2_Score8","TeamA_Player2_Score9",
		 "TeamA_Player2_Score10","TeamA_Player2_Score11","TeamA_Player2_Score12",
	     "TeamA_Player2_Score13","TeamA_Player2_Score14","TeamA_Player2_Score15",
	     "TeamA_Player2_Score16","TeamA_Player2_Score17","TeamA_Player2_Score18",
		},
		{"TeamB_Player1_Score1","TeamB_Player1_Score2","TeamB_Player1_Score3",
		 "TeamB_Player1_Score4","TeamB_Player1_Score5","TeamB_Player1_Score6",
		 "TeamB_Player1_Score7","TeamB_Player1_Score8","TeamB_Player1_Score9",
		 "TeamB_Player1_Score10","TeamB_Player1_Score11","TeamB_Player1_Score12",
		 "TeamB_Player1_Score13","TeamB_Player1_Score14","TeamB_Player1_Score15",
		 "TeamB_Player1_Score16","TeamB_Player1_Score17","TeamB_Player1_Score18",
		},
		{"TeamB_Player2_Score1","TeamB_Player2_Score2","TeamB_Player2_Score3",
		 "TeamB_Player2_Score4","TeamB_Player2_Score5","TeamB_Player2_Score6",
		 "TeamB_Player2_Score7","TeamB_Player2_Score8","TeamB_Player2_Score9",
		 "TeamB_Player2_Score10","TeamB_Player2_Score11","TeamB_Player2_Score12",
		 "TeamB_Player2_Score13","TeamB_Player2_Score14","TeamB_Player2_Score15",
		 "TeamB_Player2_Score16","TeamB_Player2_Score17","TeamB_Player2_Score18",
		}		
	};
	private Handler mHandler = new Handler();
	
	private static skinsGameInstance gameInstance;
    static private Location lastLocation = null;
    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Location greenLocation = gameInstance.getLocationOfGreen();
        String distance_str = Integer.toString((int)distance(location.getLatitude(), location.getLongitude(), 
				greenLocation.getLatitude(), greenLocation.getLongitude()));
        final Button yardView = (Button) findViewById(R.id.yardView);
        yardView.setText(distance_str);
        yardView.setVisibility(View.INVISIBLE);
        yardView.setVisibility(View.VISIBLE);
       
        if(lastLocation!=null)
        {
        	// update hit distance
        	String hit_distance_str = Integer.toString((int)distance(location.getLatitude(), location.getLongitude(), 
        			lastLocation.getLatitude(), lastLocation.getLongitude()));
        	
        	final TextView trackDistanceView = (TextView) findViewById(R.id.TrackDistance);
        	trackDistanceView.setText(hit_distance_str);
        	// Display it through Remote View
        	//CurrentMoodWidgetProvider.updateTextView(0, R.id.TrackLastHit, hit_distance_str);
        	
        	lastLocation = location;
        } 
        else
        {
        	lastLocation = location;
        }
        
        locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
	      double theta = lon1 - lon2;
	      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	      dist = Math.acos(dist);
	      dist = rad2deg(dist);
	      dist = dist * 60 * 1.1515;
	 
	      return (dist*1760);
	    //return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
