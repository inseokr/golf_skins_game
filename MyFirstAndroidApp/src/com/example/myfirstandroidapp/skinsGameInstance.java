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
import android.location.Location;
	

	public class skinsGameInstance  {
		
		public class golfCourse {
			public golfCourse(int parStrokes_[], int yardages_[], double latitudes[], double longitudes[])
			{
				parStrokes = new int[18];
				yardages   = new int[18]; // blue only for now
				greenLocations = new Location[18];
				
				parStrokes = parStrokes_;
				yardages   = yardages_;
				
				for(int holeIdx=0; holeIdx<18; holeIdx++)
				{
					greenLocations[holeIdx] = new Location("GreenLocation");
					
					greenLocations[holeIdx].setLatitude(latitudes[holeIdx]);
					greenLocations[holeIdx].setLongitude(longitudes[holeIdx]);
				}
				
			}
			
			public int getParStrokes(int holeIdx){return parStrokes[holeIdx];}
			public int getYardage(int teeIdx, int holeIdx){return yardages[holeIdx];}
			int parStrokes[];
			int yardages[];
			Location greenLocations[];
		}
		
		public Location getLocationOfGreen()
		{
			return courses[selectedCourseIdx].greenLocations[curHole-1];
		}

		
		public enum scoreMode { DIFFERENCE, STROKES};
		public static final int MAX_PLAYERS=4;
		
		public void setCourseIdx(int courseIdx) {selectedCourseIdx = courseIdx;};
		
		public skinsGameInstance() {
		       
	        players = new Vector<player>();
	        teams = new Vector<team>();
            holeLogs = new Vector<holeLog>();
            scoreMode__ = scoreMode.DIFFERENCE;
            
            courses = new golfCourse[4];
            
            courses[0] = new golfCourse(summitPointeParStrokes, summitYardages, summitGreenlatitudes, summitGreenLongitudes);
            courses[1] = new golfCourse(sunolParStrokes, sunolYardages, sunolGreenlatitudes, sunolGreenLongitudes);    
            courses[2] = new golfCourse(shoreLineParStrokes, shoreLineYardages, shoreLineGreenLatitudes, shoreLineGreenLongitudes );
            courses[3] = new golfCourse(theRanchParStrokes, theRanchYardages, theRanchGreenLatitudes, theRanchGreenLongitudes );
            
            
            selectedCourseIdx = 2; // Shoreline
            
            gameMode = TEAM_SKINS; // TEAM_SKINS/INDIVIDUAL_SKINS
  		    numOfPlayers = 4;
		}
	
		  static final int TEAM_SKINS = 0;
		  static final int INDIVIDUAL_SKINS = 1;
		  
		  List<player> players;
		  List<team> teams; 
		  int moneyPerHole=5;
		  int moneyPerBurdie=5;
		  
		  int gameMode; // TEAM_SKINS/INDIVIDUAL_SKINS
		  int numOfPlayers;
				
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
		  
		  void setGameMode(int mode) { gameMode = mode; }
		  int  getGameMode() { return gameMode; }
		  
		  void setNumOfPlayers(int numOfPlayers_) { numOfPlayers = numOfPlayers_; }
		  int getNumOfPlayers() { return numOfPlayers; }
		  
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
		
		  void resetGame(boolean bCleanObjects) {
			
			Log.v("skinsGameInstance", "goToMainMenu");
			
			if(bCleanObjects)
			{
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
			else
			{
				teams.get(0).numOfWins = 0;
				teams.get(1).numOfWins = 0;
				
				for(int playerIdx=0;playerIdx<getNumOfPlayers();playerIdx++)
				{
					players.get(playerIdx).reset();
				}
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
				for(int playerIdx=0; playerIdx<getNumOfPlayers(); playerIdx++)
					players.get(playerIdx).setScore(holeIdx, scores[playerIdx]);
			}
		
			
			if(gameMode == TEAM_SKINS)
			{
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
			else
			{
				int lowestScoreIdx = 0;
				boolean tied = false;
				// pick the lowest score
				for(int playerIdx = 1; playerIdx < numOfPlayers; playerIdx++)
				{
					if(scores[playerIdx] < scores[lowestScoreIdx])
					{
						lowestScoreIdx = playerIdx;
						tied = false;
					}
					else if(scores[playerIdx] == scores[lowestScoreIdx])
					{
						tied = true;
					} 
				}
				
				if(tied==true)
				{
					numCarriedHoles++;
					tempLog.tied();
				} 
				else
				{
					players.get(lowestScoreIdx).win(5);
					if(numCarriedHoles>0) 
						players.get(lowestScoreIdx).win(5);
					tempLog.updateHoleLog(players.get(lowestScoreIdx),(numCarriedHoles>0) ?  true: false);
					if(numCarriedHoles>0) numCarriedHoles--;
				}
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
			for(int playerIdx=0;playerIdx<numOfPlayers;playerIdx++)
				editor.putString(playerNameVariables[playerIdx], players.get(playerIdx).getName());
			
			// 2.2 scores
			for(int holeIdx=0;holeIdx<18;holeIdx++)
			{
				for(int playerIdx=0;playerIdx<numOfPlayers;playerIdx++)
					editor.putInt(scoreDataVariables[playerIdx][holeIdx], 
							      players.get(playerIdx).getScore(holeIdx));
			}
			
			// 3. Game settings
			editor.putInt("SkinsType", gameMode);
			editor.putInt("NumOfPlayers", this.numOfPlayers);
			
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
		
		static double summitGreenlatitudes[] = {37.4291954112, 37.4322303694, 
			   37.43009524, 37.4302045113,
			   37.4332766729, 37.4305533028,
			   37.4338258306, 37.433368669, 
			   37.4303588292, 37.4302131776, 
			   37.4306033003, 37.4291504517, 
			   37.4275412514, 37.42709168, 
			   37.4260422164, 37.4258804821, 
			   37.4278333401, 37.4290770493};

		static double summitGreenLongitudes[] = {-122.079107072, -122.079598744,
          -122.079790962, -122.081320043,
          -122.080432081, -122.078326711,
          -122.079404379, -122.081490858, 
          -122.084385496, -122.091784472, 
          -122.092794094, -122.092682999, 
          -122.087548196, -122.091086912, 
          -122.087825468, -122.09258101, 
          -122.091916162, -122.08761876};

		
		static int sunolParStrokes[] = { 4,4,5,4,3,4,4,3,5,
										 5,4,4,3,4,3,5,4,4};
		static int sunolYardages[] = {375, 343, 543, 424, 196, 309, 342, 233, 588,
			                          559, 361, 414, 194, 367, 207, 539, 435, 436};
		
		
		static double sunolGreenlatitudes[] = {37.4291954112, 37.4322303694, 
			   37.43009524, 37.4302045113,
			   37.4332766729, 37.4305533028,
			   37.4338258306, 37.433368669, 
			   37.4303588292, 37.4302131776, 
			   37.4306033003, 37.4291504517, 
			   37.4275412514, 37.42709168, 
			   37.4260422164, 37.4258804821, 
			   37.4278333401, 37.4290770493};

		static double sunolGreenLongitudes[] = {-122.079107072, -122.079598744,
             -122.079790962, -122.081320043,
             -122.080432081, -122.078326711,
             -122.079404379, -122.081490858, 
             -122.084385496, -122.091784472, 
             -122.092794094, -122.092682999, 
             -122.087548196, -122.091086912, 
             -122.087825468, -122.09258101, 
             -122.091916162, -122.08761876};

		
		static double shoreLineGreenLatitudes[] = {37.4291954112, 37.4322303694, 
												   37.43009524, 37.4302045113,
												   37.4332766729, 37.4305533028,
												   37.4338258306, 37.433368669, 
												   37.4303588292, 37.4302131776, 
												   37.4306033003, 37.4291504517, 
												   37.4275412514, 37.42709168, 
												   37.4260422164, 37.4258804821, 
												   37.4278333401, 37.4290770493};
		
		static double shoreLineGreenLongitudes[] = {-122.079107072, -122.079598744,
			                                        -122.079790962, -122.081320043,
			                                        -122.080432081, -122.078326711,
			                                        -122.079404379, -122.081490858, 
			                                        -122.084385496, -122.091784472, 
			                                        -122.092794094, -122.092682999, 
			                                        -122.087548196, -122.091086912, 
			                                        -122.087825468, -122.09258101, 
			                                        -122.091916162, -122.08761876};
		static int shoreLineParStrokes[] = {5,4,4,3,4,4,4,3,5,
			                                5,3,4,4,4,4,5,3,4};
		static int shoreLineYardages[] = {554,373,378,165,374,409,383,170,494,
			                              519,157,383,386,380,396,510,160,417};
		
		static double theRanchGreenLatitudes[] = {
			37.2832331813, 
			37.2814947297, 
			37.2820827906, 
			37.2844319156, 
			37.2835521111, 
			37.2855290654, 
			37.2851569374, 
			37.2854268849, 
			37.2898973512, 
			37.2927663334, 
			37.2929386519, 
			37.2938172018, 
			37.2937728211, 
			37.2913115567, 
			37.2899880138, 
			37.2928747902, 
			37.2909698804, 
			37.2867463793, 
		};

		static double theRanchGreenLongitudes[] = {
			-121.795795092
			, -121.792647373
			, -121.796082445
			, -121.799893506
			, -121.797839225
			, -121.799808573
			, -121.805800074
			, -121.80209681
			, -121.800447585
			, -121.805141504
			, -121.800082772
			, -121.790041199
			, -121.78931213
			, -121.7851952
			, -121.784439827
			, -121.789600933
			, -121.793299803
			, -121.79843574
		};
		
		static int theRanchParStrokes[] = {4,4,4,5,3,4,5,3,5,
            4,4,4,3,4,3,5,4,4};
        static int theRanchYardages[] = {348,416,360,567,180,449,511,124,472,
          320,306,467,92,382,102,503,355,351};

		
		static int parStrokes[] = summitPointeParStrokes;
		
        static scoreMode scoreMode__;
        static int selectedCourseIdx = 0;
        static golfCourse courses[];
        static String playerNameVariables[] = {"TeamA_Player1_Name", "TeamA_Player2_Name",
        										"TeamB_Player1_Name", "TeamB_Player2_Name"};
        
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
