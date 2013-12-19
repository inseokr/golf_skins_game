package com.example.myfirstandroidapp;

import java.util.List;
import java.util.Vector;

public class player {
	
	public player(String name_, int balance_){
		name = name_;
		purse = new balance(balance_);
		scores = new int[18];
	}
	
	public void win(int money){purse.deposit(money);}
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
			total+=scores[idx];
		
		return total;
	}
	
	String name;
	balance purse;
	int[] scores;
}