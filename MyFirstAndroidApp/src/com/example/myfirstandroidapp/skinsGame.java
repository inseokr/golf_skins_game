package com.example.myfirstandroidapp;

import java.util.List;
import java.lang.String;


import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.ListIterator;

public class skinsGame  {
	
	
	public skinsGameInstance getGame() { 
		if(myGame==null){
			myGame = new skinsGameInstance(); 
		}
		return myGame;
	}
	
	public skinsGameInstance getNewGame() {
		myGame = null; //
		myGame = new skinsGameInstance(); 		
		return myGame;
	}
	
	public void setGame(skinsGameInstance game_) { myGame = game_; }
	private static final skinsGame holder = new skinsGame();
	public static skinsGame getInstance() { return holder;}
	
	
	private skinsGameInstance myGame=null;
}