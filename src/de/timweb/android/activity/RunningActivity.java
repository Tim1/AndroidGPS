package de.timweb.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RunningActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);
    }
	
	public void onButtonClick(View view){
    	startActivity(new Intent(this, PreferencesActivity.class));
    }
}
