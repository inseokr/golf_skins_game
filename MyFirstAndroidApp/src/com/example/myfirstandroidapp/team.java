package com.example.myfirstandroidapp;

import java.util.List;
import java.util.Vector;
import java.util.ListIterator;


public class team {

	public team(String name_){
		name = name_; 
		members = new Vector<player>();
	}
	
	public void addPlayer(player member){members.add(member);}
	public void removePlayer(player member){members.remove(member);}
	public void updateHole(boolean bWonHole, int money, int numWonHoles, boolean bRevise){
		ListIterator <player>iterator = members.listIterator();
		while(iterator.hasNext())
		{
			player currPlayer = iterator.next();
			
			if(bWonHole) currPlayer.win(money*numWonHoles);
			else         currPlayer.lose(money*numWonHoles);
		}
		
		if(bWonHole){
			if(bRevise==false)
			   numOfWins+=numWonHoles;
			System.out.println(name + ": numOfWins =  " + numOfWins);
		}
		else {
			if(bRevise==true){
				numOfWins-=numWonHoles;
				System.out.println(name + ": numOfWins =  " + numOfWins);
			}
		}		
	}
	
	public int getNumOfWins()
	{
		return numOfWins;
	}
	
	
	public String name;
	public List<player> members;
	
	public int numOfWins;
}
