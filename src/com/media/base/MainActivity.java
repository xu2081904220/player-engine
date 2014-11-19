package com.media.base;

import com.media.base.engine.ActionHandler;
import com.media.base.engine.ActionOperation;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// for test
		ActionHandler.execute(ActionOperation.PLAY);
		ActionHandler.execute(ActionOperation.PAUSE);
		ActionHandler.execute(ActionOperation.RESUME);
		ActionHandler.execute(ActionOperation.NEXT);
		ActionHandler.execute(ActionOperation.PREV);
		ActionHandler.execute(ActionOperation.STOP);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
