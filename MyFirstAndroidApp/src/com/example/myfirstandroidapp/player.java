package com.example.myfirstandroidapp;

import java.util.List;
import java.util.Vector;

public class player {
	
	public player(String name_, int balance_){
		name = name_;
		purse = new balance(balance_);
		scores = new int[18];
		numOfWins = 0;
	}
	
	public void reset()
	{
		numOfWins = 0;
		for(int holeIdx=0; holeIdx<18; holeIdx++) scores[holeIdx] = 0;
		purse.reset();
		
	}
	
	public void win(int money){numOfWins++; purse.deposit(money);}
	public void lose(int money){purse.withdraw(money);}
	
	public int getBalance(){ return purse.getBalance();}
	public String getName(){ return name;}
	
	public void setScore(int holeNum, int score)
	{
		scores[holeNum] = score;
	}
	
	public int getScore(int holeNum)
	{
		return scores[holeNum];
	}
	
	public int getTotalScore()
	{
		int total=0;
		int idx=0;
		for(;idx<18;idx++)
		{
			total+=scores[idx];
		}
		return total;
	}
	
	public int getTotalScore(int holeNum)
	{
		int total=0;
		int idx=0;
		for(;idx<holeNum;idx++)
		{
			total+=scores[idx];
		}
		return total;
		
	}
	
	public int getNumOfWins()
	{
		return numOfWins;
	}
	
	

	String name;
	int numOfWins;
	balance purse;
	int[] scores;
}