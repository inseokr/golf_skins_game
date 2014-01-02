package com.example.myfirstandroidapp;




import java.util.List;
import java.lang.String;


import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.ListIterator;

import android.content.SharedPreferences;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;	
	

	public class skinsGameInstance  {
		
		public class golfCourse {
			public golfCourse(int parStrokes_[], int yardages_[])
			{
				parStrokes = new int[18];
				yardages   = new int[18]; // blue only for now
				
				parStrokes = parStrokes_;
				yardages   = yardages_;
			}
			
			public int getParStrokes(int holeIdx){return parStrokes[holeIdx];}
			public int getYardage(int teeIdx, int holeIdx){return yardages[holeIdx];}
			int parStrokes[];
			int yardages[];
		}

		
		public enum scoreMode { DIFFERENCE, STROKES};
		
		public skinsGameInstance() {
		       
	        players = new Vector<player>();
	        teams = new Vector<team>();
            holeLogs = new Vector<holeLog>();
            scoreMode__ = scoreMode.DIFFERENCE;
            
            courses = new golfCourse[2];
            
            courses[0] = new golfCourse(summitPointeParStrokes, summitYardages);
            courses[1] = new golfCourse(sunolParStrokes, sunolYardages);    
            
            selectedCourseIdx = 1; // Sunol
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
		
		  void resetGame() {
			
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
			
			this.curHole = 1;
			this.numCarriedHoles = 0;
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
				   evenParStrokes+=getParStrokes(holeIdx);
				}
				return (players.get(playerIdx).getTotalScore(holeNum) - evenParStrokes);	
			}
		}
		
		public void processHole(int holeIdx, int scores[], boolean bSetScore)
		{
			holeLog tempLog;
				
			tempLog = holeLogs.get(holeIdx);
				
			// update scores
			if(bSetScore==true)
			{
				for(int playerIdx=0; playerIdx<4; playerIdx++)
					players.get(playerIdx).setScore(holeIdx, scores[playerIdx]);
			}
			
			boolean bIsHandiHole = getHandiHole(holeIdx);
			int handiTeamIdx = getHandiTeam();
			int teamA_score,teamB_score;

			if(bIsHandiHole && handiTeamIdx==0)
			{
				int betterPlayer = 
						(players.get(2).getScore(holeIdx)<players.get(3).getScore(holeIdx))? 0: 1;
				if(betterPlayer==0)
				{
					teamB_score = (players.get(2).getScore(holeIdx) + 1) * 
			   	                  players.get(3).getScore(holeIdx);
				} else
				{
					teamB_score = players.get(2).getScore(holeIdx) * 
			                      (players.get(3).getScore(holeIdx)+1);				
				}
			} 
			else
			{
				teamB_score = players.get(2).getScore(holeIdx) * 
								  	  players.get(3).getScore(holeIdx);
			}
						
			if(bIsHandiHole && handiTeamIdx==1)
			{
				int betterPlayer = (players.get(0).getScore(holeIdx)<players.get(1).getScore(holeIdx))? 0: 1;					if(betterPlayer==0)
				{
					teamA_score = (players.get(0).getScore(holeIdx) + 1) * 
						                  players.get(1).getScore(holeIdx);
				} 
				else
				{
					teamA_score = players.get(0).getScore(holeIdx) * 
						                      (players.get(1).getScore(holeIdx) + 1);
								
				}
			} 
			else
			{
				teamA_score = players.get(0).getScore(holeIdx) * 									  	  players.get(1).getScore(holeIdx);
			}
									
			if(teamA_score < teamB_score){ // team A win
				team tempTeamWinner = teams.get(0);
				tempTeamWinner.updateHole(true, betUnit, (numCarriedHoles>0) ?  2: 1, false);
									
									
				team tempTeamLoser = teams.get(1);
				tempTeamLoser.updateHole(false, betUnit, (numCarriedHoles>0) ?  2: 1, false);
									
				tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (numCarriedHoles>0));
									
				if(numCarriedHoles>0) numCarriedHoles--;		
									
			}
			else if(teamA_score > teamB_score)
			{
				team tempTeamWinner = teams.get(1);
				tempTeamWinner.updateHole(true, betUnit, (numCarriedHoles>0) ?  2: 1, false);
												
				team tempTeamLoser = teams.get(0);
				tempTeamLoser.updateHole(false, betUnit, (numCarriedHoles>0) ?  2: 1, false);
												
				tempLog.updateHoleLog(tempTeamWinner, tempTeamLoser, (numCarriedHoles>0));
									
				if(numCarriedHoles>0) numCarriedHoles--;				
			}
			else
			{
				numCarriedHoles++;
				tempLog.tied();
			}
		}
		
		public void saveGameData(Context appContext)
		{
			SharedPreferences  gameDataContainer = appContext.getSharedPreferences("SKINS_GAME_NAME", 0);
			SharedPreferences.Editor editor = gameDataContainer.edit();
			
			// 1. Game Settings
			editor.putInt("LastPlayedHole", curHole-1);
			editor.putInt("HandiTeamIdx", getHandiTeam());
			int handiHoles_ = 0x00000000;
			int handiMask  = 0x00000001;
			for(int holeIdx=0;holeIdx<18;holeIdx++)
			{
				if(handiHoles[holeIdx])
				{
					handiHoles_ |= handiMask << holeIdx;
				}
			}
			editor.putInt("HandiHoleList", handiHoles_);
			
			// 2. Player Information
			// 2.1 Name
			editor.putString("TeamA_Player1_Name", players.get(0).getName());
			editor.putString("TeamA_Player2_Name", players.get(1).getName());
			editor.putString("TeamB_Player1_Name", players.get(2).getName());
			editor.putString("TeamB_Player2_Name", players.get(3).getName());
			
			// 2.2 scores
			for(int holeIdx=0;holeIdx<18;holeIdx++)
			{
				for(int playerIdx=0;playerIdx<4;playerIdx++)
					editor.putInt(scoreDataVariables[playerIdx][holeIdx], 
							      players.get(playerIdx).getScore(holeIdx));
			}
			
			editor.commit();
		}
		
		public int getParStrokes(){return courses[selectedCourseIdx].getParStrokes(curHole-1);};
		public int getParStrokes(int holeIdx) { return courses[selectedCourseIdx].getParStrokes(holeIdx);};
		public int getYardages(int holeIdx) {return courses[selectedCourseIdx].getYardage(0, holeIdx);};
		//public int getParStrokes(){return sunolParStrokes[curHole-1];};
		//public int getParStrokes(int holeIdx){return sunolParStrokes[holeIdx];};
		//public int getYardages(int holeIdx) {return sunolYardages[holeIdx];};
		
		static int summitPointeParStrokes[] = { 4,5,3,4,3,4,4,4,5,
												4,4,4,3,5,5,3,4,4};
		static int summitYardages[] = {375, 343, 543, 424, 196, 309, 342, 233, 588,
            559, 361, 414, 194, 367, 207, 539, 435, 436};
		static int sunolParStrokes[] = { 4,4,5,4,3,4,4,3,5,
										 5,4,4,3,4,3,5,4,4};
		static int sunolYardages[] = {375, 343, 543, 424, 196, 309, 342, 233, 588,
			                          559, 361, 414, 194, 367, 207, 539, 435, 436};
		static int parStrokes[] = summitPointeParStrokes;
		
        static scoreMode scoreMode__;
        static int selectedCourseIdx = 0;
        static golfCourse courses[];
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
}
