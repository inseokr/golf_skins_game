package com.example.myfirstandroidapp;




	import java.util.List;
	import java.lang.String;


	import java.util.List;
	import java.util.Vector;
	import java.util.Map;
	import java.util.ListIterator;

	public class skinsGameInstance  {
		
		public skinsGameInstance() {
		       
	        players = new Vector<player>();
	        teams = new Vector<team>();
	        
		}
	
		  List<player> players;
		  List<team> teams; 
		  int moneyPerHole=5;
		  int moneyPerBurdie=5;
		
		
		  int curHole=1;

		  int numCarriedHoles=0;
		
		  int betUnit=5;
		
		  List<holeLog> holeLogs; 
		
		  final long serialVersionUID=100L;
		
		
	    void startGame() {
			
			
	        // Create Players and team
			team tempTeamA = new team("team A");
			teams.add(tempTeamA);
			
			team tempTeamB = new team("team B");
			teams.add(tempTeamB);   
	       	
			holeLogs = new Vector<holeLog>();
			
			for(int holeIdx = 0; holeIdx<18;holeIdx++)
			{
				holeLog tempHole = new holeLog(holeIdx+1, betUnit);
				holeLogs.add(tempHole);
			}
		}
		
		  void goToMainMenu() {
			
			
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

}
