package com.example.patientorganizerapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class UpdatePrescriptionActivity extends Activity {

	/** The patient. */
	private Patient patient;
	
	/** The physician. */
	private Physician physician;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_prescription);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		// Retrieves the sent user object as a physician, as only physicians
		// can access this activity.
		physician = (Physician) bundle.getSerializable("physician");
		patient = physician.findPatient(intent.getStringExtra("patient"));
	}
	
	/** 
	 * Adds a new prescription for the patient.
	 * @param view The view of the activity.
	 * */
	public void updatePrescription (View view) {
		// Obtains prescription information from the text fields. 
		EditText editPrescriptionName = (EditText) 
				findViewById(R.id.edit_prescription_name);
		EditText editPrescriptionInstructions = (EditText)
				findViewById(R.id.edit_prescription_instructions);
		
		String prescriptionName = editPrescriptionName.getText().toString();
		String prescriptionInstructions = editPrescriptionInstructions.
				getText().toString();
		
		// Checks if input is missing and displays an error message if it is.
		if (prescriptionName.equals("") || 
				prescriptionInstructions.equals("")) {
			Toast.makeText(getApplicationContext(), "Missing input",
					Toast.LENGTH_SHORT).show();
		} else {
			// Adds a new prescription to the patient.
			physician.addPatientPrescription(patient, prescriptionName, prescriptionInstructions);
			
			// Saves the user object and returns to the previous activity.
			physician.save(getApplicationContext());
			returnIntentPatientActivity();
		}
	}
	
	/**
     * Returns intent to the activity PrescriptionHistoryActivity.
     */
	private void returnIntentPatientActivity() {
		Intent returnIntent = new Intent();
		Bundle bundleBack = new Bundle();
		bundleBack.putSerializable("physician", physician);
		bundleBack.putSerializable("patient", patient);
		returnIntent.putExtras(bundleBack);
		setResult(RESULT_OK, returnIntent);
		finish();
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
