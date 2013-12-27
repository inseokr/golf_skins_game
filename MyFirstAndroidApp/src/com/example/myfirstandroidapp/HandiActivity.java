package com.example.myfirstandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.view.View.OnClickListener;

import com.example.TrackGolfGame.R;




import java.util.ListIterator;

public class HandiActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_handi);

    
    for(int idx=0;idx<18;idx++)
    {
    	CheckBox handiCheckBox = (CheckBox) findViewById(handiHoles[idx]);
    	
    	handiCheckBox.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			CheckBox checkBoxView = (CheckBox)v;
    			skinsGameInstance game = skinsGame.getInstance().getGame();
				int holeIdx = getHoleIdx(v);
    			if(checkBoxView.isChecked()==true){
    				game.setHandiHole(holeIdx);
    			} else {
    				game.resetHandiHole(holeIdx);
    			}
    		}
    	});
    }
    
    final RadioButton teamButtonA = (RadioButton) findViewById(R.id.TeamA);
    
    teamButtonA.setOnClickListener(new OnClickListener(){
    	public void onClick(View v) {
    		RadioButton radioButton = (RadioButton)v;
    		if(radioButton.isChecked()==true)
    		{ 
    			skinsGameInstance game = skinsGame.getInstance().getGame();
    			game.setHandiTeam(0);
    		}
    	}
    });
    
    final RadioButton teamButtonB = (RadioButton) findViewById(R.id.TeamB);
    
    teamButtonB.setOnClickListener(new OnClickListener(){
    	public void onClick(View v) {
    		RadioButton radioButton = (RadioButton)v;
    		if(radioButton.isChecked()==true)
    		{ 
    			skinsGameInstance game = skinsGame.getInstance().getGame();
    			game.setHandiTeam(1);
    		}
    	}
    });
    
    
    final Button homeButton = (Button) findViewById(R.id.goBackToHomeButton);
    homeButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);                
        }
    });    
	}
	
	private int getHoleIdx(View v){
		for(int idx=0; idx<18;idx++){
			if(v.getId()==handiHoles[idx]) return idx;
		}
		
		return 0;
	}
	
	
    protected void onStart() {
    	Log.v("HandiActivity", "onStart");
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
    	Log.v("HandiActivity", "onResume");
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
    	Log.v("HandiActivity", "onPause");
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
    	Log.v("HandiActivity", "onStop");
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
    	Log.v("HandiActivity", "onDestroy");
        super.onDestroy();
        // The activity is about to be destroyed.
    }
	
	int handiHoles[]={R.id.HandiHole1, R.id.HandiHole2, R.id.HandiHole3,
					  R.id.HandiHole4, R.id.HandiHole5, R.id.HandiHole6,
					  R.id.HandiHole7, R.id.HandiHole8, R.id.HandiHole9,
					  R.id.HandiHole10, R.id.HandiHole11, R.id.HandiHole12,
					  R.id.HandiHole13, R.id.HandiHole14, R.id.HandiHole15,
					  R.id.HandiHole16, R.id.HandiHole17, R.id.HandiHole18
	};
}
