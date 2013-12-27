package com.example.myfirstandroidapp;
import android.app.Activity;
import android.content.Intent;
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

import com.example.TrackGolfGame.R;

import java.util.ListIterator;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class GameActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	Log.v("GameActivity", "GameActivity created");
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
    
    
    skinsGameInstance tempGame = skinsGame.getInstance().getGame();
    
    if(tempGame==null) 
    {
    	Log.v("GameActivity", "Game Resource deallocated");
    	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
    	
        startActivity(mainActivity);
        return;
    }
    
    for(int playerIdx=0; playerIdx<4;playerIdx++)
    	registerWheelView(playerCurrScoreViews[playerIdx], diffs, diffs.length);
    
    if(tempGame.curHole>1) refreshGameStatus();
    
    if(tempGame.players.isEmpty()) 
    {
    	Log.v("GameActivity", "No Players");
    	
    	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
    	
        startActivity(mainActivity);
        
    	
        return;
    }

    Button tempPlayerName = (Button) findViewById(R.id.player1View);
    tempPlayerName.setText(tempGame.players.get(0).getName());

   
    tempPlayerName.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent statusActivity = new Intent(getApplicationContext(), StatusActivity.class);
            Bundle bnd = new Bundle();
            bnd.putString("playerIdx", "0");
            statusActivity.putExtras(bnd);
        	startActivity(statusActivity);                
        }
    });

    tempPlayerName = (Button) findViewById(R.id.player2View);
    tempPlayerName.setText(tempGame.players.get(1).getName());
    
    tempPlayerName.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent statusActivity = new Intent(getApplicationContext(), StatusActivity.class);
            Bundle bnd = new Bundle();
            bnd.putString("playerIdx", "1");
            statusActivity.putExtras(bnd);
        	startActivity(statusActivity);                
        }
    });
   
    tempPlayerName = (Button) findViewById(R.id.player3View);
    tempPlayerName.setText(tempGame.players.get(2).getName());
    
    
    tempPlayerName.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent statusActivity = new Intent(getApplicationContext(), StatusActivity.class);
            Bundle bnd = new Bundle();
            bnd.putString("playerIdx", "2");
            statusActivity.putExtras(bnd);
        	startActivity(statusActivity);                
        }
    });
     
    tempPlayerName = (Button) findViewById(R.id.player4View);
    tempPlayerName.setText(tempGame.players.get(3).getName());
    
    tempPlayerName.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent statusActivity = new Intent(getApplicationContext(), StatusActivity.class);
            Bundle bnd = new Bundle();
            bnd.putString("playerIdx", "3");
            statusActivity.putExtras(bnd);
        	startActivity(statusActivity);                
        }
    });
    
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
            
            startActivity(mainActivity);                
        }
    });
	
	final Button updateButton = (Button) findViewById(R.id.updateHoleButton);
	updateButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	updateGameStatus();           
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
    int holeNum = tempGame.curHole-1;
    
    holeSelection.setCurrentItem(holeNum<0? 0: holeNum);
    
	boolean bIsHandiHole = tempGame.getHandiHole(holeNum<0? 0: holeNum);
	
	TextView holeView = (TextView) findViewById(R.id.textView4);
	
	if(bIsHandiHole==true)
	{	
		holeView.setTextColor(Color.parseColor("#ff0000"));
		if(tempGame.getHandiTeam()==0)
		{
			changePlayerViewColor(R.id.player1View, Color.parseColor("#ff0000"));
			changePlayerViewColor(R.id.player2View, Color.parseColor("#ff0000"));
		} else
		{
			changePlayerViewColor(R.id.player3View, Color.parseColor("#ff0000"));
			changePlayerViewColor(R.id.player4View, Color.parseColor("#ff0000"));
		}
		
	} else
	{
		holeView.setTextColor(Color.parseColor("#ffffff"));
	}
	
	holeView.setVisibility(View.INVISIBLE);
	holeView.setVisibility(View.VISIBLE);

	} catch(ArrayIndexOutOfBoundsException e)
	{
	}
    
    holeSelection.addChangingListener(new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
		    selectedHoleNum = newValue + 1;
		    skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		    holeLog tempLog = tempGame.holeLogs.get(newValue);
			if(tempLog.isHoleProcessed()==true)
			{
				refreshPlayerInfo(selectedHoleNum);
			}
			else {
				if(tempGame.curHole>1)
					refreshPlayerInfo(tempGame.curHole-1);
			}
		}
	});
    
    
	mHandler.postDelayed(new Runnable() {
		public void run() {
			Log.v("MainActivity", "Finally I'm awake");
			keepMeAwake();
       }
	}, 3000);
		
	}
	
	public void keepMeAwake(){
		mHandler.postDelayed(new Runnable() {
			public void run() {
				Log.v("MainActivity", "Finally I'm awake");
				keepMeAwake();
           }
		}, 1000*3600);
	}
	
	private void refreshPlayerInfo(int holeNum)
	{
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		// update score and totals
		TextView playerTotalScoreView;
		WheelView playerScoreView;
		for(int playerIdx=0; playerIdx < 4; playerIdx++)
		{
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[playerIdx]);
			playerTotalScoreView.setText(Integer.toString(tempGame.getAdjustedTotal(playerIdx, holeNum)));
			playerTotalScoreView.setVisibility(View.INVISIBLE);
			playerTotalScoreView.setVisibility(View.VISIBLE);
		
			playerScoreView = (WheelView) findViewById(playerCurrScoreViews[playerIdx]);
		
			if(tempGame.getScoreMode()==skinsGameInstance.scoreMode.DIFFERENCE)
			{
				int scoreIdx = (tempGame.players.get(playerIdx).getScore(holeNum-1)-tempGame.getParStrokes(holeNum-1)) + 2;
				playerScoreView.setCurrentItem(scoreIdx);
				playerScoreView.setVisibility(View.INVISIBLE);
				playerScoreView.setVisibility(View.VISIBLE);
			}
			else {
				int scoreIdx = tempGame.players.get(playerIdx).getScore(holeNum-1) - 1;
				playerScoreView.setCurrentItem(scoreIdx);
				playerScoreView.setVisibility(View.INVISIBLE);
				playerScoreView.setVisibility(View.VISIBLE);
			}
		}
	}
		
	private void changePlayerViewColor(int view_id, int colorCode)
	{
		Button playerView = (Button) findViewById(view_id);
		
		playerView.setTextColor(colorCode);
		playerView.setVisibility(View.INVISIBLE);
		playerView.setVisibility(View.VISIBLE);
	}
	
	private  void updateGameStatus() {
		
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		// get hole number
		final WheelView holeEditText = (WheelView) findViewById(R.id.HoleSelection);
		int holeNum = selectedHoleNum;
		
		
		// update scores
		for(int playerIdx=0;playerIdx<4;playerIdx++){
			curScores[playerIdx] = getAdjustedScore(curSelectedScoreItems[playerIdx], holeNum -1);
			tempGame.players.get(playerIdx).setScore(holeNum-1,curScores[playerIdx]);	
		}
		
		holeLog tempLog;
		
		try
		{
			tempLog = tempGame.holeLogs.get(holeNum-1);
			if(tempLog.isHoleProcessed()==true) 
			{
				tempLog.cancelHole();
					
				if(tempLog.isDouble()==true) tempGame.numCarriedHoles--;
			}
		
		} catch(ArrayIndexOutOfBoundsException e)
		{
			Log.v("GameActivity", "Exception" + e);
			Log.v("GameActivity", "holeNum = " + holeNum);
			Log.v("GameActivity", "holeLogs size = " + tempGame.holeLogs.size());
			return; 
		}
				
				
		tempGame.curHole = holeNum + 1;
		
		boolean bIsHandiHole = tempGame.getHandiHole(holeNum-1);
		int handiTeamIdx = tempGame.getHandiTeam();
		int teamA_score,teamB_score;
		
		try
		{
			boolean bIsNextHandiHole = tempGame.getHandiHole(holeNum);

			TextView holeView = (TextView) findViewById(R.id.textView4);
		
			if(bIsNextHandiHole==true)
			{	
				holeView.setTextColor(Color.parseColor("#ff0000"));
				if(tempGame.getHandiTeam()==0)
				{
					changePlayerViewColor(R.id.player1View, Color.parseColor("#ff0000"));
					changePlayerViewColor(R.id.player2View, Color.parseColor("#ff0000"));
					changePlayerViewColor(R.id.player1View, Color.parseColor("#000000"));
					changePlayerViewColor(R.id.player2View, Color.parseColor("#000000"));
				} else
				{
					changePlayerViewColor(R.id.player1View, Color.parseColor("#000000"));
					changePlayerViewColor(R.id.player2View, Color.parseColor("#000000"));
					changePlayerViewColor(R.id.player3View, Color.parseColor("#ff0000"));
					changePlayerViewColor(R.id.player4View, Color.parseColor("#ff0000"));
				}
			} else
			{
				holeView.setTextColor(Color.parseColor("#ffffff"));
				
				changePlayerViewColor(R.id.player1View, Color.parseColor("#000000"));
				changePlayerViewColor(R.id.player2View, Color.parseColor("#000000"));
				changePlayerViewColor(R.id.player3View, Color.parseColor("#000000"));
				changePlayerViewColor(R.id.player4View, Color.parseColor("#000000"));
			}
			
			holeView.setVisibility(View.INVISIBLE);
			holeView.setVisibility(View.VISIBLE);
		}catch(ArrayIndexOutOfBoundsException e)
		{
			Log.v("GameActivity","hole number can't exceed 18");
		}
				
		Log.v("GameActivity", "handiTeamIdx = " + handiTeamIdx);
		
		if(bIsHandiHole && handiTeamIdx==0)
		{
			int betterPlayer = (tempGame.players.get(2).getScore(holeNum-1)<tempGame.players.get(3).getScore(holeNum-1))? 0: 1;
			if(betterPlayer==0)
			{
				teamB_score = (tempGame.players.get(2).getScore(holeNum-1) + 1) * 
			                  tempGame.players.get(3).getScore(holeNum-1);
			} else
			{
				teamB_score = tempGame.players.get(2).getScore(holeNum-1) * 
		                      (tempGame.players.get(3).getScore(holeNum-1)+1);				
			}
		} 
		else
		{
			teamB_score = tempGame.players.get(2).getScore(holeNum-1) * 
					  	  tempGame.players.get(3).getScore(holeNum-1);
		}
		
		if(bIsHandiHole && handiTeamIdx==1)
		{
			int betterPlayer = (tempGame.players.get(0).getScore(holeNum-1)<tempGame.players.get(1).getScore(holeNum-1))? 0: 1;
			if(betterPlayer==0)
			{
				teamA_score = (tempGame.players.get(0).getScore(holeNum-1) + 1) * 
			                  tempGame.players.get(1).getScore(holeNum-1);
			} else
			{
				teamA_score = tempGame.players.get(0).getScore(holeNum-1) * 
		                      (tempGame.players.get(1).getScore(holeNum-1) + 1);

				
			}
		} 
		else
		{
			teamA_score = tempGame.players.get(0).getScore(holeNum-1) * 
					  	  tempGame.players.get(1).getScore(holeNum-1);
		}
		
		
		Log.v("GameActivity", "teamA_score = " + teamA_score + ", teamB_score =" + teamB_score);
		
		if(teamA_score < teamB_score){ // team A win
			team tempTeamWinner = tempGame.teams.get(0);
			tempTeamWinner.updateHole(true, tempGame.betUnit, (tempGame.numCarriedHoles>0) ?  2: 1, false);
					
					
			team tempTeamLoser = tempGame.teams.get(1);
			tempTeamLoser.updateHole(false, tempGame.betUnit, (tempGame.numCarriedHoles>0) ?  2: 1, false);
					
			tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (tempGame.numCarriedHoles>0));
					
			if(tempGame.numCarriedHoles>0) tempGame.numCarriedHoles--;		
					
		}
		else if(teamA_score > teamB_score)
		{
			team tempTeamWinner = tempGame.teams.get(1);
			tempTeamWinner.updateHole(true, tempGame.betUnit, (tempGame.numCarriedHoles>0) ?  2: 1, false);
								
			team tempTeamLoser = tempGame.teams.get(0);
			tempTeamLoser.updateHole(false, tempGame.betUnit, (tempGame.numCarriedHoles>0) ?  2: 1, false);
								
			tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (tempGame.numCarriedHoles>0));
					
			if(tempGame.numCarriedHoles>0) tempGame.numCarriedHoles--;
		}
		else
		{
			tempGame.numCarriedHoles++;
			tempLog.tied();
		}
		
		
		// Update team status
		final EditText tempATeamScore = (EditText)findViewById(R.id.teamAScoreEdit);
		tempATeamScore.setText(Integer.toString(tempGame.teams.get(0).getNumOfWins()));
	    final EditText tempBTeamScore = (EditText)findViewById(R.id.teamBScoreEdit);
	    tempBTeamScore.setText(Integer.toString(tempGame.teams.get(1).getNumOfWins()));
		
		// update player labels
		ListIterator <player>iterator = tempGame.players.listIterator();
		    
		int idx=0;
		TextView playerTotalScoreView;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[idx]);
			playerTotalScoreView.setText(Integer.toString(tempGame.getAdjustedTotal(idx, holeNum)));		
		//String newStatusInfo = "Hole " + curHole + " is in progress." + "Number of carried holes = " + numCarriedHoles;
		    idx++;
		}
		
		TextView carriedHoles = (TextView)findViewById(R.id.carriedHoles);
		carriedHoles.setText(Integer.toString(tempGame.numCarriedHoles));
		carriedHoles.setVisibility(View.INVISIBLE);
		carriedHoles.setVisibility(View.VISIBLE);
		
		// It's the way how to refresh the view?? Unbelievable!!
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	   
	    tableLayout = (TableLayout) findViewById(R.id.tableLayout2);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    
	    holeEditText.setCurrentItem(selectedHoleNum);
	    
	    holeEditText.setVisibility(View.INVISIBLE);
	    holeEditText.setVisibility(View.VISIBLE);
	    
	    Log.v("GameActivity", "selectedHoleNum" + selectedHoleNum);
	}
	
	
private  void refreshGameStatus() {
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		Log.v("GameActivity", "refreshGameStatus. holeNum = " + tempGame.curHole);
			
		int holeNum = tempGame.curHole-1;
		
		if(holeNum<0) holeNum = 0;
		
		
		if(tempGame.players.isEmpty()) 
		{
			Log.v("GameActivity", "No players found");
			return;
		}
		
		WheelView tempCurrScoreView;
		
		for(int playerIdx=0;playerIdx<4;playerIdx++){
			
			tempCurrScoreView = (WheelView) findViewById(playerCurrScoreViews[playerIdx]);
			tempCurrScoreView.setCurrentItem( curSelectedScoreItems[playerIdx]);
			tempCurrScoreView.setVisibility(View.INVISIBLE);
			tempCurrScoreView.setVisibility(View.VISIBLE);
		}
		
		if(tempGame.teams.isEmpty()) 
		{
			Log.v("GameActivity", "No teams found");
			return;
		}
		
		// Update team status
		final EditText tempATeamScore = (EditText)findViewById(R.id.teamAScoreEdit);
		tempATeamScore.setText(Integer.toString(tempGame.teams.get(0).getNumOfWins()));
	    final EditText tempBTeamScore = (EditText)findViewById(R.id.teamBScoreEdit);
	    tempBTeamScore.setText(Integer.toString(tempGame.teams.get(1).getNumOfWins()));
		
		// update player labels
		ListIterator <player>iterator = tempGame.players.listIterator();
		
		
		int idx=0;
		TextView playerTotalScoreView;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();
			playerTotalScoreView = (TextView) findViewById(playerTotalScoreViews[idx]);
			playerTotalScoreView.setText(Integer.toString(tempGame.getAdjustedTotal(idx, holeNum)));	
		//String newStatusInfo = "Hole " + curHole + " is in progress." + "Number of carried holes = " + numCarriedHoles;
		    idx++;
		}
				
		
		TextView carriedHoles = (TextView)findViewById(R.id.carriedHoles);
		carriedHoles.setText(Integer.toString(tempGame.numCarriedHoles));
		carriedHoles.setVisibility(View.INVISIBLE);
		carriedHoles.setVisibility(View.VISIBLE);
		
		// It's the way how to refresh the view?? Unbelievable!!
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    
	    tableLayout = (TableLayout) findViewById(R.id.tableLayout2);
	    tableLayout.setVisibility(View.INVISIBLE);
	    tableLayout.setVisibility(View.VISIBLE);
	    	    
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

private int getAdjustedScore(int newValue, int holeIdx)
{
	skinsGameInstance tempGame = skinsGame.getInstance().getGame();
	int value;
	
	if(tempGame.getScoreMode()==skinsGameInstance.scoreMode.DIFFERENCE)
	{
		value = Integer.parseInt(diffs[newValue]);
		Log.v("GameActivity", "before adjustment = " + value);
		value = value + tempGame.getParStrokes();
		Log.v("GameActivity", "parStrokes = " + tempGame.getParStrokes(holeIdx));
	} 
	else
	{
		value = Integer.parseInt(strokes[newValue]);
	}
	
	return value;
}

private int getPlayerIdxByWheelId(WheelView wheel)
{
	for(int idx=0; idx<4; idx++)
	{
		if(wheel.getId()==playerCurrScoreViews[idx]) return idx;
	}
	
	return 0;
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

	static int selectedHoleNum =1;
	
	static String strokes[]={"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String diffs[]={"-2", "-1", "0", "1", "2", "3", "4", "5"};
	static int curScores[]={0,0,0,0};
	static int curSelectedScoreItems[]={3,3,3,3};
	static String curScoresString[]={"0", "0", "0", "0"};
	static int playerTotalScoreViews[]={R.id.player1TotalView, R.id.player2TotalView,
										R.id.player3TotalView, R.id.player4TotalView
	};
	static int playerCurrScoreViews[]={R.id.player1ScoreView, R.id.player2ScoreView,
		R.id.player3ScoreView, R.id.player4ScoreView
};
	private Handler mHandler = new Handler();
}
