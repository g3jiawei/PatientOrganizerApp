package com.example.patientorganizerapp;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class VitalsHistoryActivity extends Activity {
	
	/** The patient. */
	private Patient patient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		setContentView(R.layout.activity_vitals_history);
		
		// Gets the patient whose information will be displayed.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		patient = (Patient) bundle.getSerializable("patient");
		
		// Sets up the table of information.
		setVitalsTable();
	}
	
	/** 
	 * Sets the table for vitals history.
	 * */
	private void setVitalsTable() {
		// Loops over the patients vitals history, with each individual 
		// update as a row. Loops over the indices backwards in order to show
		// information from latest to oldest information.
		List<String[]> vitalsHistory = patient.getVitalsHistory();
		for(int i = vitalsHistory.size() - 1; i >= 0; i--)
		{
			setVitalsRow(vitalsHistory, i);
		}
	}
	
	/**
     * Adds a row of vitalsHistory dynamically to the layout.
     * * @param vitalsHistory The vital signs history of a patient
     * * @param i An iterator number.
     */
	@SuppressLint("NewApi")
	private void setVitalsRow(List<String[]> vitalsHistory, int i) {
		TableLayout table = (TableLayout) findViewById(R.id.vitals_table);
		table.setStretchAllColumns(true);  
	    table.setShrinkAllColumns(true);
	    
	    TableRow row = new TableRow(this);
	    String[] vitals = vitalsHistory.get(i);
	    
	    // Sets a row with the corresponding vitals information in the cells.
	    for (int k = 0; k < vitals.length; k++) {
	    	TextView column = new TextView(this);
	    	column.setText(vitals[k].toString());
	    	column.setGravity(Gravity.CENTER_HORIZONTAL);
	    	// Creates a border for each cell.
	    	column.setBackground(getResources().
	    			getDrawable(R.drawable.cell_border));
	    	row.addView(column);
	    }
	    // Adds the row to the table.
	    table.addView(row);
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
