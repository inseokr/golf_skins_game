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

import com.example.myfirstandroidapp.R;

import java.util.ListIterator;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class SecondActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	Log.v("SecondActivity", "SecondActivity created");
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_sub);
    
    
    skinsGameInstance tempGame = skinsGame.getInstance().getGame();
    
    if(tempGame==null) 
    {
    	Log.v("SecondActivity", "Game Resource deallocated");
    	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
    	
        startActivity(mainActivity);
        return;
    }
    
    if(tempGame.curHole>1) refreshGameStatus();
    
    if(tempGame.players.isEmpty()) 
    {
    	Log.v("SecondActivity", "No Players");
    	
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
		EditText tempScore;
		int score;
		for(int playerIdx=0;playerIdx<4;playerIdx++){
			switch(playerIdx)
			{
			case 0: 
				tempScore = (EditText) findViewById(R.id.player1ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			case 1: 
				tempScore = (EditText) findViewById(R.id.player2ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			case 2: 
				tempScore = (EditText) findViewById(R.id.player3ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;	
			case 3: 
				tempScore = (EditText) findViewById(R.id.player4ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			}
			
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
			Log.v("SecondActivity", "Exception" + e);
			Log.v("SecondActivity", "holeNum = " + holeNum);
			Log.v("SecondActivity", "holeLogs size = " + tempGame.holeLogs.size());
			return; 
		}
				
				
		tempGame.curHole = holeNum + 1;
		
		boolean bIsHandiHole = tempGame.getHandiHole(holeNum-1);
		int handiTeamIdx = tempGame.getHandiTeam();
		int teamA_score,teamB_score;
		
		if(tempGame.getGameMode()==skinsGameInstance.TEAM_SKINS)
		{
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
					changePlayerViewColor(R.id.player1View, Color.parseColor("#ffffff"));
					changePlayerViewColor(R.id.player2View, Color.parseColor("#ffffff"));
				} else
				{
					changePlayerViewColor(R.id.player1View, Color.parseColor("#ffffff"));
					changePlayerViewColor(R.id.player2View, Color.parseColor("#ffffff"));
					changePlayerViewColor(R.id.player3View, Color.parseColor("#ff0000"));
					changePlayerViewColor(R.id.player4View, Color.parseColor("#ff0000"));
				}
			} else
			{
				holeView.setTextColor(Color.parseColor("#ffffff"));
				
				changePlayerViewColor(R.id.player1View, Color.parseColor("#ffffff"));
				changePlayerViewColor(R.id.player2View, Color.parseColor("#ffffff"));
				changePlayerViewColor(R.id.player3View, Color.parseColor("#ffffff"));
				changePlayerViewColor(R.id.player4View, Color.parseColor("#ffffff"));
			}
			
			holeView.setVisibility(View.INVISIBLE);
			holeView.setVisibility(View.VISIBLE);
		}catch(ArrayIndexOutOfBoundsException e)
		{
			Log.v("SecondActivity","hole number can't exceed 18");
		}
				
		Log.v("SecondActivity", "handiTeamIdx = " + handiTeamIdx);
		
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
			} 
			else
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
		
		
		Log.v("SecondActivity", "teamA_score = " + teamA_score + ", teamB_score =" + teamB_score);
		
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
		
		}
		// INDIVIDUAL SKINS
		else
		{
			int lowestScoreIdx = 0;
			boolean tied = false;
			// pick the lowest score
			for(int playerIdx = 1; tempGame.getNumOfPlayers() < 4; playerIdx++)
			{
				if(tempGame.players.get(playerIdx).getScore(holeNum-1) < tempGame.players.get(lowestScoreIdx).getScore(holeNum-1))
				{
					lowestScoreIdx = playerIdx;
					tied = false;
				}
				else if(tempGame.players.get(playerIdx).getScore(holeNum-1) == tempGame.players.get(lowestScoreIdx).getScore(holeNum-1))
				{
					tied = true;
				} 
			}
			
			if(tied==true)
			{
				tempGame.numCarriedHoles++;
				tempLog.tied();
			} 
			else
			{
				tempGame.players.get(lowestScoreIdx).win(5);
				tempLog.updateHoleLog(tempGame.players.get(lowestScoreIdx),(tempGame.numCarriedHoles>0) ?  true: false);
			}
			
		}
		
		// update player labels
		ListIterator <player>iterator = tempGame.players.listIterator();
		    
		int idx=0;
		TextView balance;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();

		    switch(idx)
			{
			case 0: 
				balance = (TextView) findViewById(R.id.player1BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			case 1: 
				balance = (TextView) findViewById(R.id.player2BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			case 2: 
				balance = (TextView) findViewById(R.id.player3BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;	
			case 3: 
				balance = (TextView) findViewById(R.id.player4BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			}
		
				
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
	    
	    Log.v("SecondActivity", "selectedHoleNum" + selectedHoleNum);
	}
	
	
private  void refreshGameStatus() {
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		Log.v("SecondActivity", "refreshGameStatus. holeNum = " + tempGame.curHole);
			
		int holeNum = tempGame.curHole-1;
		
		if(holeNum<0) holeNum = 0;
		
		
		// update scores
		EditText tempScore;
		int score;
		
		if(tempGame.players.isEmpty()) 
		{
			Log.v("SecondActivity", "No players found");
			return;
		}
		
		for(int playerIdx=0;playerIdx<4;playerIdx++){
			switch(playerIdx)
			{
			case 0: 
				tempScore = (EditText) findViewById(R.id.player1ScoreView);
				//tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			case 1: 
				tempScore = (EditText) findViewById(R.id.player2ScoreView);
				//tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			case 2: 
				tempScore = (EditText) findViewById(R.id.player3ScoreView);
				//tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;	
			case 3: 
				tempScore = (EditText) findViewById(R.id.player4ScoreView);
				//tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
				break;
			}
			
		}
		
		if(tempGame.teams.isEmpty()) 
		{
			Log.v("SecondActivity", "No teams found");
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
		TextView balance;
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();

		    switch(idx)
			{
			case 0: 
				balance = (TextView) findViewById(R.id.player1BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			case 1: 
				balance = (TextView) findViewById(R.id.player2BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			case 2: 
				balance = (TextView) findViewById(R.id.player3BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;	
			case 3: 
				balance = (TextView) findViewById(R.id.player4BalanceView);
				balance.setText(Integer.toString(currPlayer.getBalance()));
				break;
			}
		
				
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


protected void onStart() {
	Log.v("SecondActivity", "SecondActivity onStart");
    super.onStart();
    // The activity is about to become visible.
}
@Override
protected void onResume() {
	Log.v("SecondActivity", "SecondActivity onResume");
    super.onResume();
    // The activity has become visible (it is now "resumed").
}
@Override
protected void onPause() {
	Log.v("SecondActivity", "SecondActivity onPause");
    super.onPause();
    // Another activity is taking focus (this activity is about to be "paused").
}
@Override
protected void onStop() {
	Log.v("SecondActivity", "SecondActivity onStop");
    super.onStop();
    // The activity is no longer visible (it is now "stopped")
}
@Override
protected void onDestroy() {
	Log.v("SecondActivity", "SecondActivity onDestroy");
    super.onDestroy();
    // The activity is about to be destroyed.
}

	static int selectedHoleNum =1;
	
	private Handler mHandler = new Handler();
}
