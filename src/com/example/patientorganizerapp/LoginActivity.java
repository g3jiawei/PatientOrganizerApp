package com.example.patientorganizerapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class LoginActivity extends Activity {
	
	/** 
	 *  A map with usernames as keys and passwords as values. Used for loading
	 *  purposes
	 */
	private Map<String, List<String>> loginData;
	
	/** The name of the file containing login information. */
	private String fileName = "login_file";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
     * Attempt to log the nurse in when login button is clicked.
     * @throws IOException 
     */
	public void login(View view) throws IOException{
		//Takes the input from the text fields
		EditText editUser = (EditText) findViewById(R.id.edit_username);
		EditText editPass = (EditText) findViewById(R.id.edit_password);
		String user = editUser.getText().toString();
		String pass = editPass.getText().toString();
		
		// Sets up shared preferences so all activities can access certain
		// things.
		SharedPreferences settings = getSharedPreferences("Patient Organizer",
				0);
		SharedPreferences.Editor editor = settings.edit();

		// Creates a string based on what type of login is occurring.
		String loginAction = loginValidator(user, pass);
		
		// Only continues if the loginAction is not "" which indicates wrong
		// username or password.
		if (!loginAction.equals("")) {
			// Sets a shared preference variable, depending on the type
			// of account being logged in on.
			if (loginAction.equals("N")) {
				editor.putString("User Type", "NURSE");
			} else if (loginAction.equals("P")) {
				editor.putString("User Type", "PHYSICIAN");
			}
			editor.commit();
			launchIntentMenuActivity();
		} else {
			// Displays error message if invalid login is attempted.
			Toast.makeText(getApplicationContext(),
					"Invalid username or password",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Validates the user's username and password.
	 * @param username The username of the user.
	 * @param password The password of the user.
	 * @throws IOException
     * @return "" iff login fails, "N" iff user is type Nurse, "P" iff user is
     * 		   type Physician
	 */
    private String loginValidator(String username, String password)
    		throws IOException {
		// Creates a new login file for determining valid logins only when
    	// one doesn't exist.
		File file = this.getFileStreamPath(fileName);
		
    	if(!file.exists()) {
    		writeLoginFile();
    	}
		try {
			// Read information from the login file.
			readLoginFile();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Checks validity of username and passwords, and returns a string
		// that determines future actions.
		if (loginData.containsKey(username)) {
			if (loginData.get(username).get(0).equals(password)) {
				return loginData.get(username).get(1);
			}
		}
		return "";
    }
    
	/**
	 * Writes a file for login information.
	 */
	private void writeLoginFile() {
		// Sets up the map to be written in the login file.
    	Map<String, List<String>> loginData = new HashMap<String, List<String>>();
    	List<String> nurse1 = new ArrayList<String>();
    	nurse1.add("npass");
    	nurse1.add("N");
    	loginData.put("n1", nurse1);
    	List<String> nurse2 = new ArrayList<String>();
    	nurse2.add("npass");
    	nurse2.add("N");
    	loginData.put("n2", nurse2);
    	List<String> physician1 = new ArrayList<String>();
    	physician1.add("p1");
    	physician1.add("P");
    	loginData.put("p1", physician1);
    	List<String> physician2 = new ArrayList<String>();
    	physician2.add("p2");
    	physician2.add("P");
    	loginData.put("p2", physician2);

    	FileOutputStream fos;
		try {
			// Create a file in the directory with name of fileName
			fos = openFileOutput(fileName, this.MODE_PRIVATE);
			try {
				// Writes the map of loginData to the file.
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(loginData);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Reads a file for login information.
	 * @throws IOException
	 */
    private void readLoginFile() throws ClassNotFoundException {
    	FileInputStream fis;
		ObjectInputStream is;
		try {
			// Try reading from the login file if it exists.
			fis = this.openFileInput(fileName);
			try {
				is = new ObjectInputStream(fis);
				// Sets loginData to the map that was saved in the file. 
				loginData = (Map<String, List<String>>) is.readObject();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Starts the activity MenuActivity.
     */
	private void launchIntentMenuActivity() {
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#
			// up-vs-back
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
