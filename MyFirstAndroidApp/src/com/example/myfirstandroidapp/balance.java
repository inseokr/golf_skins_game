package com.example.myfirstandroidapp;


public class balance {
	public balance(int initBalance){
		balance = initBalance;
	}
	
	public void deposit(int winMoney){
		balance += winMoney;
	}

	public void withdraw(int loseMoney){
		balance -= loseMoney;
	}
	
	public int getBalance(){
		return balance;
	}
	
	public void updateBalance(int money){
		balance = money;
	}
	
	int balance;
}
