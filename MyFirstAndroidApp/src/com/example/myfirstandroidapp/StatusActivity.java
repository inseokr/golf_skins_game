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
import com.example.myfirstandroidapp.R;
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
    
    int totalScore=0;
    
    
    for(int holeIdx=0; holeIdx<18; holeIdx++)
    {
    	TextView tempScoreView = (TextView) findViewById(scoreViewList[holeIdx]);
    	if(holeIdx<tempGame.curHole-1)
    	{
    		int curScore = tempGame.players.get(playerIdx).getScore(holeIdx) - tempGame.getParStrokes(holeIdx);
    		tempScoreView.setText(Integer.toString(curScore));
    		totalScore += curScore;
    	} 
    	else
    	{
    		tempScoreView.setText("-");
    	}
    }
    
    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
    tableLayout.setVisibility(View.INVISIBLE);
    tableLayout.setVisibility(View.VISIBLE);
    
    TextView totalScoreView = (TextView) findViewById(R.id.totalScoreView);
    
    totalScoreView.setText(Integer.toString(totalScore));
    totalScoreView.setVisibility(View.INVISIBLE);
    totalScoreView.setVisibility(View.VISIBLE);
    
    final Button homeButton = (Button) findViewById(R.id.goBackToHomeButton);
    homeButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(gameActivity);                
        }
    });  
    
	}
	
	static int scoreViewList[]={R.id.ScoreView1, R.id.ScoreView2, R.id.ScoreView3, R.id.ScoreView4, R.id.ScoreView5, R.id.ScoreView6,
        R.id.ScoreView7, R.id.ScoreView8, R.id.ScoreView9, R.id.ScoreView10, R.id.ScoreView11, R.id.ScoreView12,
        R.id.ScoreView13, R.id.ScoreView14, R.id.ScoreView15, R.id.ScoreView16, R.id.ScoreView17, R.id.ScoreView18
};
}
