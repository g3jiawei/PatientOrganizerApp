

package com.example.patientorganizerapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	/** The amount of time this activity will display the splash screen. */
	protected int SPLASH_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Sets main layout as a splash screen.
		setContentView(R.layout.splash);
		displaySplashScreen();
	}
	
    /**
     * Allows splash screen to be displayed for a certain amount of time.
     */
	protected void displaySplashScreen () {
		// Creates new thread to act as timer.
        Thread logoTimer = new Thread() { 
            public void run() {
                try {
                    int logoTimer = 0;
                    // Pauses for SPLASH_TIME amount of time
                    while(logoTimer < SPLASH_TIME){
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    };
                    // Calls the activity from manifest.xml
                    startActivity(new Intent("com.example.CLEARSCREEN"));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        // Starts the thread.
        logoTimer.start();
	}
}
