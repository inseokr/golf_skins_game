package com.example.myfirstandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.content.Intent;
import android.widget.TextView;
import android.view.ViewGroup;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;



import java.util.ListIterator;

public class StatusActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	Bundle bundle = this.getIntent().getExtras();
	String _getData = bundle.getString("playerIdx");
	
	int playerIdx=Integer.parseInt(_getData);
	
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_score);

    skinsGameInstance tempGame = skinsGame.getInstance().getGame();
    
    TextView tempScoreView = (TextView) findViewById(R.id.ScoreView1);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(0)));
    
    tempScoreView = (TextView) findViewById(R.id.ScoreView2);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(1)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView3);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(2)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView4);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(3)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView5);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(4)));
   
    tempScoreView = (TextView) findViewById(R.id.scoreView6);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(5)));

    tempScoreView = (TextView) findViewById(R.id.scoreView7);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(6)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView8);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(7)));
     
    tempScoreView = (TextView) findViewById(R.id.scoreView9);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(8)));
    
    tempScoreView = (TextView) findViewById(R.id.ScoreView10);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(9)));
    
    tempScoreView = (TextView) findViewById(R.id.ScoreView11);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(10)));
     
    tempScoreView = (TextView) findViewById(R.id.scoreView12);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(11)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView13);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(12)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView14);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(13)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView15);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(14)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView16);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(15)));
     
    tempScoreView = (TextView) findViewById(R.id.scoreView17);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(16)));
    
    tempScoreView = (TextView) findViewById(R.id.scoreView18);
    tempScoreView.setText(Integer.toString(tempGame.players.get(playerIdx).getScore(17)));
    
    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
    tableLayout.setVisibility(View.INVISIBLE);
    tableLayout.setVisibility(View.VISIBLE);
    
    TextView totalScore = (TextView) findViewById(R.id.totalScoreView);
    
    totalScore.setText(Integer.toString(tempGame.players.get(playerIdx).getTotalScore()));
    
    totalScore.setVisibility(View.INVISIBLE);
    totalScore.setVisibility(View.VISIBLE);
    
    final Button homeButton = (Button) findViewById(R.id.goBackToHomeButton);
    homeButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent gameActivity = new Intent(getApplicationContext(), SecondActivity.class);
            startActivity(gameActivity);                
        }
    });
    

      
        
    
	}
}
