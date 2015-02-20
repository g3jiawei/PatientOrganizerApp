package com.example.patientorganizerapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class UpdateVitalsActivity extends Activity {

	/** The patient. */
	private Patient patient;
	
	/** The nurse. */
	private Nurse nurse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_vitals);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Obtains intent and required objects.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		nurse = (Nurse) bundle.getSerializable("nurse");
		patient = nurse.findPatient(intent.getStringExtra("patient"));
	}
	
	/** 
	 * Updates the patients vital information.
	 * @param view The view of the activity.
	 * */
	public void updateVitals (View view) {
		EditText editTemperature = (EditText) 
				findViewById(R.id.edit_temperature);
		EditText editSystolic = (EditText)
				findViewById(R.id.edit_systolic);
		EditText editDiastolic = (EditText)
				findViewById(R.id.edit_diastolic);
		EditText editHeartRate = (EditText)
				findViewById(R.id.edit_heart_rate);
		EditText editUpdateTime = (EditText)
				findViewById(R.id.edit_update_time);
		
		String temperature = editTemperature.getText().toString();
		String systolic = editSystolic.getText().toString();
		String diastolic = editDiastolic.getText().toString();
		String heartRate = editHeartRate.getText().toString();
		String updateTime = editUpdateTime.getText().toString();

		// Checks if any input is missing and creates an error message if this
		// is true.
		if (temperature.equals("") || systolic.equals("") ||
				diastolic.equals("") || heartRate.equals("") ||
				updateTime.equals("")) {
			Toast.makeText(getApplicationContext(), "Missing input",
					Toast.LENGTH_SHORT).show();
			
		} 
		// Checks if the time of update is in the correct format and creates 
		// an error message if it isn't.
		else if (!matchTime(updateTime)) {
			Toast.makeText(getApplicationContext(), "Invalid time input",
					Toast.LENGTH_SHORT).show();
		} else {
			// Gets current date from built-in calendar as the defaulted value
			// for date that vitals was updated.
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = df.format(calendar.getTime());
			String updateDateTime = currentDate + " " + updateTime;

			// Updates the patients vitals.
			nurse.updatePatientVitals(patient, Double.parseDouble(temperature),
					Integer.parseInt(diastolic), Integer.parseInt(systolic),
					Integer.parseInt(heartRate), updateDateTime);
			
			// Saves the user object and returns to the previous activity.
			nurse.save(getApplicationContext());
			returnIntentPatientActivity();			
		}
	}

	/** 
	 * Validates the format of the time input.
	 * @param time The time that vitals were updated.
	 * @return true iff the time is a valid time.
	 * */
	private boolean matchTime(String time) {
		// Uses a regular expression to define the format for time.
		Pattern ptime = Pattern.compile("([01]\\d|2[0-3]):([0-5]\\d)");
		Matcher mtime = ptime.matcher(time);
		return mtime.matches();
	}

	/**
     * Returns intent to the activity PrescriptionHistoryActivity.
     */
	private void returnIntentPatientActivity() {
		// Returns intent back to PatientActivity.
		Intent returnIntent = new Intent();
		Bundle bundleBack = new Bundle();
		bundleBack.putSerializable("nurse", nurse);
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
