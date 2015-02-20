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
import android.view.Menu;
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
import android.os.Build;

public class PrescriptionHistoryActivity extends Activity {

	/** The patient. */
	private Patient patient;
	
	/** A list of prescription names. */
	private List<String> prescriptionNames = new ArrayList<String>();
	
	/** A list of prescription instructions. */
	private List<String> prescriptionInstructions = new ArrayList<String>();
	
	/** A list of maps of <String, String> used for creating a listview. */
	private List<Map<String, String>> actionListPrescriptionHistory =
			new ArrayList<Map<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescription_history);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Gets the intent that was sent in.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		patient = (Patient) bundle.getSerializable("patient");
		
		initPrescriptionHistoryList();
		initListView();
	}

	/** 
	 * Initializes the list of prescription history.
	 * */
	private void initPrescriptionHistoryList() {
		String name, instruction;
		List<List<String>> prescriptionHistory = patient.getPrescriptions();
		
		// Loops over prescription history backwards in order to show
		// the more recent information at the top, and the older information
		// at the bottom.
		for (int i = prescriptionHistory.size() - 1; i >= 0; i--) {
			name = prescriptionHistory.get(i).get(0);
			instruction = prescriptionHistory.get(i).get(1);
			
			// Creates actions for the listview.
    		actionListPrescriptionHistory.add(createAction("action", name));
    		// Adds names and instructions to the lists.
    		prescriptionNames.add(name);
    		prescriptionInstructions.add(instruction);
    	}
	}
	
	/** 
	 * Creates a hashmap of <String, String> to determine actions in a
	 * listview.
	 * @param key A key value.
	 * @param name A prescription name.
	 * */
	private HashMap<String, String> createAction(String key, String name) {
		HashMap<String, String> action = new HashMap<String, String>();
		action.put(key, name);
		return action;
	}
	
	/** 
	 * Initializes a listview in the layout. 
	 * */
	private void initListView() {
		// Sets up the listview.
		ListView lv = (ListView) findViewById(R.id.list_view);
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, 
				actionListPrescriptionHistory,
				android.R.layout.simple_list_item_1, new String[] {"action"},
				new int[] {android.R.id.text1});
		lv.setAdapter(simpleAdpt);
		// Allows each action, which shows a prescription name, to be
		// clickable.
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				String name = prescriptionNames.get(position);
				String instruction = prescriptionInstructions.get(position);
				// Shows a dialog box with instructions for a prescription 
				// whenever the corresponding item is clicked.
				createPrescriptionInstructionsDialog(view, name, instruction);
			}
	    });
	}

	/** 
	 * Displays a dialog showing prescription information.
	 * */
	private void createPrescriptionInstructionsDialog(View view, String name,
			String instruction) {
		// Sets up a dialog box, displaying a prescription name, and the
		// instructions for that prescription.
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
			.setTitle("Instructions: " + name)
			.setMessage(instruction)
			// Allows the box to be cancelable when the outside of the box
			// is clicked.
			.setCancelable(true)
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
	  		public void onClick(DialogInterface dialog,
	  				int id) {
	  			dialog.cancel();
	  		}
	  	});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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
			// Returns a blank intent and result when the back button is
			// clicked.
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
