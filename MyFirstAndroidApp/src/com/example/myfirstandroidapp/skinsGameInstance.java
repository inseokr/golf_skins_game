package com.example.myfirstandroidapp;




	import java.util.List;
	import java.lang.String;


	import java.util.List;
	import java.util.Vector;
	import java.util.Map;
	import java.util.ListIterator;
	import android.util.Log;

	public class skinsGameInstance  {
		
		public enum scoreMode { DIFFERENCE, STROKES};
		
		public skinsGameInstance() {
		       
	        players = new Vector<player>();
	        teams = new Vector<team>();
            holeLogs = new Vector<holeLog>();
            scoreMode__ = scoreMode.DIFFERENCE;
		}
	
		  List<player> players;
		  List<team> teams; 
		  int moneyPerHole=5;
		  int moneyPerBurdie=5;
		
		
		  int curHole=1;

		  int numCarriedHoles=0;
		
		  int betUnit=5;
		
		  List<holeLog> holeLogs;
		  boolean handiHoles[]={false, false,false, false, false, false,
		                        false, false, false, false, false, false,
				                false, false, false, false, false, false};
		  int handiTeam;
		
		  final long serialVersionUID=100L;
		
		  void setHandiHole(int holeNum){handiHoles[holeNum] = true;};
		  void resetHandiHole(int holeNum){handiHoles[holeNum] = false;};
		  boolean getHandiHole(int holeNum){return handiHoles[holeNum];};
		  
		  void setHandiTeam(int teamIdx){handiTeam=teamIdx;};
		  int getHandiTeam(){ return handiTeam;}
		  
	    void startGame() {
			
			
	        // Create Players and team
			team tempTeamA = new team("team A");
			teams.add(tempTeamA);
			
			team tempTeamB = new team("team B");
			teams.add(tempTeamB);   
	       	
			
		    Log.v("skinsGameInstance", "Contructor called");
		    for(int holeIdx = 0; holeIdx<18;holeIdx++)
			{
		    	holeLog tempHole = new holeLog(holeIdx+1, betUnit);
				holeLogs.add(tempHole);
			}
			
		}
		
		  void goToMainMenu() {
			
			Log.v("skinsGameInstance", "goToMainMenu");
			
			// delete team and players
			ListIterator <player>iterator1 = players.listIterator();
			while(iterator1.hasNext())
			{
				player temp = iterator1.next();
				temp = null;
				iterator1.remove();
			}
			
			ListIterator <team>iterator2 = teams.listIterator();
			while(iterator2.hasNext())
			{
				team temp = iterator2.next();
				temp = null;
				iterator2.remove();
			}
			
			
			ListIterator <holeLog>iterator3 = holeLogs.listIterator();
			while(iterator3.hasNext())
			{
				holeLog temp = iterator3.next();
				temp = null;
				iterator3.remove();
			}
		}
		
		 static void goToScoreCard() {
			
		}
		
		 static void updateGameStatus() {

			
		}
		
		public scoreMode getScoreMode(){ return scoreMode__;}
		public void setScoreMode(scoreMode mode) { scoreMode__ = mode;}
		public int getAdjustedTotal(int playerIdx, int holeNum) 
		{
			if(getScoreMode()==scoreMode.STROKES)
			{
				return players.get(playerIdx).getTotalScore();
			}
			else
			{
				int evenParStrokes=0;
				for(int holeIdx=0;holeIdx<holeNum;holeIdx++)
				{
				   evenParStrokes+=parStrokes[holeIdx];
				}
				return (players.get(playerIdx).getTotalScore(holeNum) - evenParStrokes);		
			}
		}
		
		public int getParStrokes(){return parStrokes[curHole-1];};
		public int getParStrokes(int holeIdx) { return parStrokes[holeIdx]; }
		
		static int summitPointeParStrokes[] = { 4,5,3,4,3,4,4,4,5,
			4,4,4,3,5,5,3,4,4};
		static int parStrokes[] = summitPointeParStrokes;
		
        static scoreMode scoreMode__;
}
