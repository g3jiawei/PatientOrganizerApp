package com.example.patientorganizerapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class MenuActivity extends Activity {
	
	/** A nurse. */
	private Nurse nurse;
	
	/** A physician. */
	private Physician physician;
	
	/** The type of user that is using the app. */
	private String userType;
	 
	/** 
	 * The list of options for the layout.
	 * */
	private List<Map<String, String>> actionListMenu =
			new ArrayList<Map<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		setupActionBar();
		
		// Sets up the user.
		initUser();
		// Sets up all action labels in the menu.
		initMenuOptions();
		// Sets up a listview in the layout containing the actions.
		initListView();
	}
	
	/** 
	 * Initializes either the nurse or physician.
	 * */
    private void initUser() {
    	// Checks shared preferences to determine type of user.
		SharedPreferences settings =
				getSharedPreferences("Patient Organizer", 0);
		userType = settings.getString("User Type", "NURSE");
		
		// Creates either a new nurse or physician based on the type of account
		// that was logged in as. This also loads up saved data.
		if (userType.equals("NURSE")) {
			nurse = new Nurse(getApplicationContext());
		} else if (userType.equals("PHYSICIAN")) {
			physician = new Physician(getApplicationContext());
		}
	}

	/**
     * Initializes the menu options.
     */
	private void initMenuOptions() {
		SharedPreferences settings =
				getSharedPreferences("Patient Organizer", 0);
		userType = settings.getString("User Type", "NURSE"); 
		
		// Sets up the menu differently for nurses and physicians, based on
		// what each is allowed to do. createAction is called in order to set
		// up the listview.
		actionListMenu.add(createAction("action", "Search Patient"));
		if (userType.equals("NURSE")) {
			actionListMenu.add(createAction("action", "Patients By Urgency"));
			actionListMenu.add(createAction("action", "Add Patient"));
		}
	}
	
	/**
     * Creates a hashmap.
     * @param key A key for the hashmap.
     * @param name The value of the key.
     * @return hashmap of <key, name>
     */
	private HashMap<String, String> createAction(String key, String name) {
		// Creates a hashmap that refers to certain action in the menu.
		HashMap<String, String> action = new HashMap<String, String>();
		action.put(key, name);
		return action;
	}
	
	/** 
	 * Initializes a listview in the layout.
	 * */
	private void initListView() {
		ListView lv = (ListView) findViewById(R.id.listView);
		// Takes options from actionList and creates a listview from a default
		// layout.
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, actionListMenu,
				android.R.layout.simple_list_item_1, new String[] {"action"},
				new int[] {android.R.id.text1});
		lv.setAdapter(simpleAdpt);
		// Makes the actions clickable.
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				// Listener will perform different actions depending on which
				// item was clicked in the listview layout.
				if (id == 0) {
					// Creates a alert dialog window for finding patients.
					createFindPatientDialog(view);
				} else if (id == 1) {
					// Goes to list of patients by urgency screen if an item
					// of id 1 is clicked.
			    	launchIntentPatientsByUrgencyActivity();
			    } else if (id == 2) {
					// Goes to add patient screen if an item of id 2 is
			    	// clicked.
					launchIntentAddPatientActivity();
			    }
			}
	    });
	}

	/** 
	 * Creates a dialog box for searching patients by health card number.
	 * */
	private void createFindPatientDialog(View view){
		// Uses a view from xml files in order to allow edittext boxes.
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.find_patient_dialog,
				null);
		// Builds the dialog box.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.
				Builder(this);
		alertDialogBuilder.setView(promptsView);
		
		alertDialogBuilder
			// Makes user unable to leave dialog by clicking
			// outside its borders.
			.setTitle("     Input Health Card Number")
			.setCancelable(false)
			// Sets a button on the left for submitting data.
			.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int id) {
					// Needs to get context in order to read
					// values from the text boxes.
					Dialog f = (Dialog) dialog;
					EditText userInput = (EditText) f.findViewById
							(R.id.edit_find_patient_dialog);
					String healthCardNumber = userInput.getText().toString();
					// Checks if a patient exists with the inputted health
					// card number.
					validateHealthCardNumber(healthCardNumber);
				}
		  	})
		  	// Sets a button on the right for exiting the dialog.
		  	.setNegativeButton("Cancel",
		  			new DialogInterface.OnClickListener() {
		  		public void onClick(DialogInterface dialog,
		  				int id) {
		  			// Closes dialog when user chooses to cancel.
		  			dialog.cancel();
		  		}
		  	});

		// Create and show the alert dialog.
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/** 
	 * Checks if a patient with the desired health card number exists.
	 * @param healthCardNumber A health card number.
	 * */
	private void validateHealthCardNumber(String healthCardNumber) {
		Patient patient = null;
		
		// Gets a patient from the users depending on the type of user.
		if (userType.equals("NURSE")){
			patient = nurse.findPatient(healthCardNumber);
		} else if (userType.equals("PHYSICIAN")){
			patient = physician.findPatient(healthCardNumber);
		}
		
		// Goes to the patient screen only if the patient corresponding 
		// to the inputed health card exists, otherwise shows an error
		// message.
		if (patient != null) {
			launchIntentPatientActivity(healthCardNumber);
		} else {
			Toast.makeText(getApplicationContext(),
					"Invalid Card Number",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
     * Starts the activity FindPatientActivity.
     * * @param healthCardNumber A patients health card number.
     */
	private void launchIntentPatientActivity(String healthCardNumber) {
		// Sends intent containing a user and a health card number.
		// The health card number is used instead of the patient object
		// itself in order to save new patient variables properly.
		Intent intent = new Intent(this, PatientActivity.class);
		// Sending a bundle is required for transferring user-defined objects.
		Bundle bundle = new Bundle();
		if (userType.equals("NURSE")){
			bundle.putSerializable("nurse", nurse);
		} else if (userType.equals("PHYSICIAN")){
			bundle.putSerializable("physician", physician);
		}
		intent.putExtras(bundle);
		intent.putExtra("patient", healthCardNumber);
		startActivity(intent);
	}
	
	/**
     * Starts the activity AddPatientActivity.
     */
	private void launchIntentAddPatientActivity() {
		// Only sends nurse to the adding patient screen, as only nurses can
		// access it.
		Intent intent = new Intent(this, AddPatientActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	/**
     * Starts the activity PatientsByUrgencyActivity.
     */
	private void launchIntentPatientsByUrgencyActivity() {
		// Only sends nurse to the urgency list screen, as only nurses can
		// access it.
		Intent intent = new Intent(this, PatientsByUrgencyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
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
