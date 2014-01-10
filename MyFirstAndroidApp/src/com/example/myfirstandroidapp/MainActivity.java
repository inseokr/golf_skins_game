package com.example.myfirstandroidapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.*;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.Intent;
import android.widget.NumberPicker;
import android.content.res.Configuration;
//import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.os.Handler;

import com.example.myfirstandroidapp.R;

import android.content.Context;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public MainActivity() {

		playerViews = new WheelView[4];
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// mLandscapeView = getLayoutInflater().inflate(R.layout.main_landscape,
		// null);
		// mPortraitView = getLayoutInflater().inflate(R.layout.activity_main,
		// null);

		// if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		// setContentView(R.layout.main_landscape);
		// else

		gameInstance = skinsGame.getInstance().getGame();

		if (gameInstance.getGameMode() == skinsGameInstance.TEAM_SKINS) {
			setContentView(R.layout.activity_main);

			WheelView tempWheelView = (WheelView) findViewById(R.id.numberOfPlayers);
			tempWheelView.setVisibility(View.INVISIBLE);

			TextView tempView = (TextView) findViewById(R.id.textView2);
			tempView.setVisibility(View.INVISIBLE);
		} else
			setContentView(R.layout.activity_main_v1);

		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.isEmpty() == false) {
			String _getData = bundle.getString("gameFinished");
			if (_getData != null && _getData.isEmpty() == false) {
				gameInstance = skinsGame.getInstance().getNewGame();
			}
		}

		Log.v("MainActivity", "mainActivity created");

		final Button restoreButton = (Button) findViewById(R.id.RestoreGameData);
		restoreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resumePreviousGame();

			}
		});
		
		final Button courseButton = (Button) findViewById(R.id.CourseSelection);
		courseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupMenu(v);
			}
		});

		final Button button1 = (Button) findViewById(R.id.button1);

		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				skinsGame.getInstance().getGame().startGame();
				skinsGameInstance tempGame = skinsGame.getInstance().getGame();

				for (int idx = 0; idx < MAX_NUM_PLAYERS; idx++) {
					player tempPlayer = new player(
							(bShuffled == true) ? selected_players_name[selectedPlayerIdx[idx]]
									: players_name[selectedPlayerIdx[idx]], 0);
					tempGame.players.add(tempPlayer);
					tempGame.teams.get(idx / 2).addPlayer(tempPlayer);
				}

				// EditText betMoney = (EditText) findViewById(R.id.betPerHole);
				// tempGame.betUnit =
				// Integer.parseInt(betMoney.getText().toString());

				// Intent secondActivity = new Intent(getApplicationContext(),
				// SecondActivity.class);
				// Log.v("MainActivity", "starting secondActivity");
				// startActivity(secondActivity);
				Intent gameActivity = new Intent(getApplicationContext(),
						GameActivity.class);
				Log.v("MainActivity", "starting gameActivity");
				startActivity(gameActivity);
			}
		});

		for (int idx = 0; idx < MAX_NUM_PLAYERS; idx++) {
			playerViews[idx] = (WheelView) findViewById(playerViewIds[idx]);
			playerViews[idx].setVisibleItems(10);

			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
					this, (selected_players_name[idx] == null) ? players_name
							: selected_players_name);
			adapter.setTextSize(20);
			playerViews[idx].setViewAdapter(adapter);
			if (selectedPlayerIdx[idx] == -1)
				selectedPlayerIdx[idx] = idx;

			playerViews[idx].setCurrentItem(selectedPlayerIdx[idx]);
			playerViews[idx].setCyclic(true);

			playerViews[idx].addChangingListener(new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue,
						int newValue) {

					for (int viewIdx = 0; viewIdx < MAX_NUM_PLAYERS; viewIdx++) {
						if (playerViews[viewIdx] == wheel) {
							selectedPlayerIdx[viewIdx] = newValue;
						}

					}

				}
			});
		}

		ArrayWheelAdapter<String> numOfPlayerAdapter = new ArrayWheelAdapter<String>(
				this, numOfPlayers_string);
		numOfPlayerAdapter.setTextSize(20);

		WheelView numOfPlayerWheel = (WheelView) findViewById(R.id.numberOfPlayers);

		numOfPlayerWheel.setViewAdapter(numOfPlayerAdapter);

		numOfPlayerWheel.setCurrentItem(3);
		numOfPlayerWheel.setCyclic(true);

		numOfPlayerWheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int numOfPlayers = newValue + 1;
				skinsGameInstance tempGame = skinsGame.getInstance().getGame();
				tempGame.setNumOfPlayers(numOfPlayers);

				for (int playerIdx = 0; playerIdx < numOfPlayers; playerIdx++) {
					WheelView tempWheelView = (WheelView) findViewById(playerViewIds[playerIdx]);
					tempWheelView.setVisibility(View.VISIBLE);
				}

				for (int playerIdx = numOfPlayers; playerIdx < 4; playerIdx++) {
					WheelView tempWheelView = (WheelView) findViewById(playerViewIds[playerIdx]);
					tempWheelView.setVisibility(View.INVISIBLE);
				}

			}
		});

		Button shuffle = (Button) findViewById(R.id.shuffle);

		shuffle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shuffleSelectedPlayers();
			}
		});

		final RadioButton teamSkinsRadioButton = (RadioButton) findViewById(R.id.TeamSkinsRadioButton);

		teamSkinsRadioButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RadioButton radioButton = (RadioButton) v;
				if (radioButton.isChecked() == true) {
					skinsGameInstance game = skinsGame.getInstance().getGame();
					game.setGameMode(0);

					Intent mainActivity = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(mainActivity);
				}
			}
		});

		final RadioButton individualSkinsRadioButton = (RadioButton) findViewById(R.id.IndividualSkinsRadioButton);

		individualSkinsRadioButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RadioButton radioButton = (RadioButton) v;
				if (radioButton.isChecked() == true) {
					skinsGameInstance game = skinsGame.getInstance().getGame();
					game.setGameMode(1);

					Intent mainActivity = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(mainActivity);
				}
			}
		});

		Button handi = (Button) findViewById(R.id.handi);

		handi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent handiActivity = new Intent(getApplicationContext(),
						HandiActivity.class);
				Log.v("MainActivity", "starting HandiActivity");
				startActivity(handiActivity);
			}
		});

		bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.isEmpty() == false) {
			int lastPlayedHole = bundle.getInt("ResumeGame", -1);
			if (lastPlayedHole > 0) {
				// replay game based on saved scores.
				gameInstance = skinsGame.getInstance().getNewGame();
				resumePreviousGame();
			}
		}
	}

	public void shuffleSelectedPlayers() {
		Log.v("MainActivity", "shuffleSelectedPlayers");

		bShuffled = true;

		for (int idx = 0; idx < MAX_NUM_PLAYERS; idx++) {
			selectedItemsList[idx] = selectedPlayerIdx[idx];
			Log.v("MainActivity", "player#" + idx + " = "
					+ selectedItemsList[idx]);
			if (selected_players_name[idx] == null)
				selected_players_name[idx] = players_name[selectedItemsList[idx]];
			selectedItemsList[idx] = idx;
		}

		for (int idx = 0; idx < MAX_NUM_PLAYERS; idx++) {
			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
					this, selected_players_name);
			adapter.setTextSize(20);
			playerViews[idx].setViewAdapter(adapter);
		}

		scrollIdx = 0;

		playerViews[scrollIdx].scroll(-350 + (int) (Math.random() * 50), 1000);

		mHandler.postDelayed(new Runnable() {
			public void run() {
				Log.v("MainActivity", "Finally I'm awake");
				bCheckScrollData = true;
				processScrolledData();
			}
		}, 2000);
	}

	public boolean processScrolledData() {
		Log.v("MainActivity",
				"scrollIdx = item" + playerViews[scrollIdx].getCurrentItem());

		boolean bFound = false;
		for (int searchIdx = 0; searchIdx < MAX_NUM_PLAYERS; searchIdx++) {
			Log.v("MainActivity", "Expected item = "
					+ selectedItemsList[searchIdx]);

			if (selectedItemsList[searchIdx] == playerViews[scrollIdx]
					.getCurrentItem()) {
				bFound = true;
				selectedItemsList[searchIdx] = -1;
				Log.v("MainActivity", "Found item");
				break;
			}
		}

		if (bFound)
			scrollIdx++;

		if (scrollIdx < 3) // no more scroll ??
		{
			playerViews[scrollIdx].scroll(-350 + (int) (Math.random() * 50),
					1000);

			mHandler.postDelayed(new Runnable() {
				public void run() {
					Log.v("MainActivity", "Finally I'm awake");
					bCheckScrollData = true;
					processScrolledData();
				}
			}, 3000);
		} else {
			for (int searchIdx = 0; searchIdx < MAX_NUM_PLAYERS; searchIdx++) {

				if (selectedItemsList[searchIdx] != -1) {
					Log.v("MainActivity", "Filling the last item with = "
							+ selectedItemsList[searchIdx]);
					playerViews[3].setCurrentItem(selectedItemsList[searchIdx]);
					playerViews[3].setVisibility(View.INVISIBLE);
					playerViews[3].setVisibility(View.VISIBLE);
					break;
				}
			}
		}

		return bFound;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		if (config.orientation == (int) Configuration.ORIENTATION_LANDSCAPE) {
			// adjust mLandscapeView as needed
			setContentView(mLandscapeView);
		} else {
			setContentView(mPortraitView);
		}

	}

	public void resumePreviousGame() {
		SharedPreferences gameData = getApplicationContext()
				.getSharedPreferences("SKINS_GAME_NAME", 0);

		// This may not be necessary.
		gameInstance.setGameMode(gameData.getInt("SkinsType", 0));
		gameInstance.setNumOfPlayers(gameData.getInt("NumOfPlayers", 4));

		// 1. Get last played hole
		int lastPlayedHole = gameData.getInt("LastPlayedHole", 1);

		// 2. handi information
		gameInstance.setHandiTeam(gameData.getInt("HandiTeamIdx", 0));
		int handiHoleList = gameData.getInt("HandiHoleList", 0x00000000);

		// EditText betMoney = (EditText) findViewById(R.id.betPerHole);

		// betMoney.setText(Integer.toString(handiHoleList));
		// betMoney.setVisibility(View.INVISIBLE);
		// betMoney.setVisibility(View.VISIBLE);

		int handiHoleMask = 0x00000001;
		int maskedValue = 0x00000000;

		for (int holeIdx = 0; holeIdx < 18; holeIdx++) {
			maskedValue = 0x00000000;
			int adjustedMask = handiHoleMask << holeIdx;
			maskedValue = handiHoleList & adjustedMask;
			if (maskedValue != 0x00000000) {
				gameInstance.setHandiHole(holeIdx);
			}
		}

		// 3. player information
		for (int playerIdx = 0; playerIdx < 4; playerIdx++) {
			selected_players_name[playerIdx] = gameData.getString(
					savedPlayerVariables[playerIdx], "Seo In");
			selectedItemsList[playerIdx] = playerIdx;
		}

		for (int playerIdx = 0; playerIdx < 4; playerIdx++) {
			
				ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
						this, selected_players_name);
				adapter.setTextSize(20);
				playerViews[playerIdx].setViewAdapter(adapter);
				playerViews[playerIdx].setCurrentItem(playerIdx);
				playerViews[playerIdx].setVisibility(View.INVISIBLE);
				if (playerIdx < gameInstance.getNumOfPlayers()) 
					playerViews[playerIdx].setVisibility(View.VISIBLE);
		}

		gameInstance.startGame();

		for (int playerIdx = 0; playerIdx < gameInstance.getNumOfPlayers(); playerIdx++) {
			player tempPlayer = new player(
					selected_players_name[selectedItemsList[playerIdx]], 0);
			gameInstance.players.add(tempPlayer);
			// if(gameInstance.getGameMode()==skinsGameInstance.TEAM_SKINS)
			gameInstance.teams.get(playerIdx / 2).addPlayer(tempPlayer);
		}

		// Not saved yet.
		gameInstance.betUnit = 5;

		Intent gameActivity = new Intent(getApplicationContext(),
				GameActivity.class);
		Log.v("MainActivity", "starting gameActivity");
		Bundle bnd = new Bundle();
		bnd.putInt("ResumeGame", lastPlayedHole);
		gameActivity.putExtras(bnd);
		startActivity(gameActivity);
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
		// Another activity is taking focus (this activity is about to be
		// "paused").
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
	
	private void showPopupMenu(View v){
		PopupMenu popupMenu = new PopupMenu(this, v);
		popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
		    
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
		   
		   @Override
		   public boolean onMenuItemClick(MenuItem item) {
			   
			int courseIdx;
			for(courseIdx=0; courseIdx < courseIds.length; courseIdx++)
			{
				if(courseIds[courseIdx] == item.getItemId()) break;
			}
			
			gameInstance.setCourseIdx(courseIdx);
			
		    Toast.makeText(getBaseContext(),
		      Integer.toString(courseIdx),
		      Toast.LENGTH_LONG).show();
		    return true;
		   }
		  });
		    
		 popupMenu.show();
	}

	View mLandscapeView;
	View mPortraitView;

	static int courseIds[] = {R.id.summitPointe, R.id.sunolPalm, R.id.shoreLine, R.id.theRanch };
	
	static int selectedPlayerIdx[] = { -1, -1, -1, -1 };
	WheelView[] playerViews;
	static final int MAX_NUM_PLAYERS = 4;
	static int playerViewIds[] = { R.id.teamA_player1, R.id.teamA_player2,
			R.id.teamB_player1, R.id.teamB_player2 };

	static String players_name[] = { "서 인", "정 수길", "정 현태", "최 보경", "최 성훈",
			"이 상원", "최 일해", "이 태원", "장 충순", "김 범수", "조 한욱", "문 봉기", "박 창서",
			"박 성파", "안 성준", };

	static String numOfPlayers_string[] = { "1", "2", "3", "4" };

	static String selected_players_name[] = { null, null, null, null };
	private Handler mHandler = new Handler();
	static boolean bCheckScrollData = false;
	static int scrollIdx = 0;
	static int selectedItemsList[] = { 0, 1, 2, 3 };
	static boolean bShuffled = false;
	static String savedPlayerVariables[] = { "TeamA_Player1_Name",
			"TeamA_Player2_Name", "TeamB_Player1_Name", "TeamB_Player2_Name" };
	skinsGameInstance gameInstance;
}
