package com.example.myfirstandroidapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.content.Intent;
import android.widget.TextView;
import android.view.ViewGroup;

import java.util.ListIterator;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class CopyOfSecondActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	Log.v("MainActivity", "SecondActivity created");
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sub);
    
    
    skinsGameInstance tempGame = skinsGame.getInstance().getGame();
    
    if(tempGame.curHole>1) refreshGameStatus();

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
     
    /*final Button homeButton = (Button) findViewById(R.id.goHomeButton);
    homeButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);                
        }
    });*/
	
	final Button updateButton = (Button) findViewById(R.id.updateHoleButton);
	updateButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	updateGameStatus();           
        }
    });
	
	}
	
	private  void updateGameStatus() {
		
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		// get hole number
		final EditText holeEditText = (EditText) findViewById(R.id.HoleSelection);
		int holeNum = Integer.parseInt(holeEditText.getText().toString());
		
		
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
				break;
			case 1: 
				tempScore = (EditText) findViewById(R.id.player2ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				break;
			case 2: 
				tempScore = (EditText) findViewById(R.id.player3ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				break;	
			case 3: 
				tempScore = (EditText) findViewById(R.id.player4ScoreView);
				score = Integer.parseInt(tempScore.getText().toString());
				tempGame.players.get(playerIdx).setScore(holeNum-1, score);
				break;
			}
			
		}
				
		holeLog tempLog = tempGame.holeLogs.get(holeNum-1);
				
		if(tempLog.isHoleProcessed()==true) 
		{
			tempLog.cancelHole();
					
			if(tempLog.isDouble()==true) tempGame.numCarriedHoles--;
		}
				
		tempGame.curHole = holeNum + 1;
				
		int teamA_score = tempGame.players.get(0).getScore(holeNum-1) * 
				          tempGame.players.get(1).getScore(holeNum-1);
		
		int teamB_score = tempGame.players.get(2).getScore(holeNum-1) * 
						  tempGame.players.get(3).getScore(holeNum-1);
		
		
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

	
	
private  void refreshGameStatus() {
		
		skinsGameInstance tempGame = skinsGame.getInstance().getGame();
		
		int holeNum = tempGame.curHole-1;
		// get hole number
		final EditText holeEditText = (EditText) findViewById(R.id.HoleSelection);
		holeEditText.setText(Integer.toString(holeNum));

		
		// update scores
		EditText tempScore;
		int score;
		for(int playerIdx=0;playerIdx<4;playerIdx++){
			switch(playerIdx)
			{
			case 0: 
				tempScore = (EditText) findViewById(R.id.player1ScoreView);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				break;
			case 1: 
				tempScore = (EditText) findViewById(R.id.player2ScoreView);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				break;
			case 2: 
				tempScore = (EditText) findViewById(R.id.player3ScoreView);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				break;	
			case 3: 
				tempScore = (EditText) findViewById(R.id.player4ScoreView);
				tempScore.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(holeNum-1)));
				break;
			}
			
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

}
