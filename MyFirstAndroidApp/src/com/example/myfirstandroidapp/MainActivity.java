package com.example.myfirstandroidapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.*;
import android.util.Log;
import android.content.Intent;
import android.widget.NumberPicker;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.v("MainActivity", "mainActivity created, counter=");
        
        
        final Button button1 = (Button) findViewById(R.id.button1);
        
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	skinsGame.getInstance().getGame().startGame();
            	skinsGameInstance tempGame = skinsGame.getInstance().getGame();
            	
            	EditText playerName = (EditText) findViewById(R.id.teamA_player1);
            	
            	player tempPlayer = new player(playerName.getText().toString(),0);
            	tempGame.players.add(tempPlayer);
            	tempGame.teams.get(0).addPlayer(tempPlayer);
            	
            	playerName = (EditText) findViewById(R.id.teamA_player2);
            	tempPlayer = new player(playerName.getText().toString(),0);
            	tempGame.players.add(tempPlayer);
            	tempGame.teams.get(0).addPlayer(tempPlayer);
            	
            	playerName = (EditText) findViewById(R.id.teamB_player1);
            	tempPlayer = new player(playerName.getText().toString(),0);
            	tempGame.players.add(tempPlayer);
            	tempGame.teams.get(1).addPlayer(tempPlayer);
            	
            	playerName = (EditText) findViewById(R.id.teamB_player2);
            	tempPlayer = new player(playerName.getText().toString(),0);
            	tempGame.players.add(tempPlayer);
            	tempGame.teams.get(1).addPlayer(tempPlayer);
            	
            	EditText betMoney = (EditText) findViewById(R.id.betPerHole);
            	tempGame.betUnit = Integer.parseInt(betMoney.getText().toString());
            	 	
            	Intent secondActivity = new Intent(getApplicationContext(), SecondActivity.class);
            	Log.v("MainActivity", "starting secondActivity");
                startActivity(secondActivity);                
            }
        });   
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    protected void onStart() {
    	Log.v("MainActivity", "mainActivity onStart");
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
    	Log.v("MainActivity", "mainActivity onResume");
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
    	Log.v("MainActivity", "mainActivity onPause");
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
    	Log.v("MainActivity", "mainActivity onStop");
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
    	Log.v("MainActivity", "mainActivity onDestroy");
        super.onDestroy();
        // The activity is about to be destroyed.
    }
        
}
