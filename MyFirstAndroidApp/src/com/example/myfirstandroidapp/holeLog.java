package com.example.myfirstandroidapp;


public class holeLog {

	public holeLog(int holeNum_, int moneyUnit_){
		holeNum = holeNum_;
		winner = null;
		loser = null;
		bDouble = false;
		bUpdatedOnce = false;
		bTied = false;
		moneyUnit= moneyUnit_;
	}
	
	public boolean isHoleProcessed() { return bUpdatedOnce; }
	public boolean isDouble() { return bDouble; }
	
	public void cancelHole(){
		if(bUpdatedOnce && (bTied == false)){
			// revert previous data
			winner.updateHole(false, moneyUnit, (bDouble==true)? 2: 1, true);
			loser.updateHole(true, moneyUnit,  (bDouble==true)? 2: 1, true);
			bUpdatedOnce = false;
			winner = loser = null;
		}
	}
	
	public void tied()
	{
		bUpdatedOnce = true;
		bTied = true;
	}
	
	public void updateHoleLog(team winner_, team loser_, boolean bDouble_) {
		bUpdatedOnce = true;
		
     	winner = winner_;
		loser = loser_;
		bDouble = bDouble_;	
	}
	
	public void updateHoleLog(player winner, boolean bDouble_) {
		bUpdatedOnce = true;
		individualSkinsWinner = winner;
	}
	
	team winner;
	team loser;
	
	player individualSkinsWinner;
	
	boolean bDouble;
	boolean bUpdatedOnce;
	boolean bTied;
	int holeNum; // starting from 1
	int moneyUnit;
}
