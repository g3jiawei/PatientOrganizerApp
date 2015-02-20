package com.example.patientorganizerapp;

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

public class RecordDoctorTimeActivity extends Activity {

	/** The patient. */
	private Patient patient;
	
	/** The nurse. */
	private Nurse nurse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_doctor_time);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		nurse = (Nurse) bundle.getSerializable("nurse");
		patient = nurse.findPatient(intent.getStringExtra("patient"));
	}
	
	/** 
	 * Sets the time that the patient was seen by a doctor.
	 * @param view The view of the activity.
	 * */
	public void recordDoctorTime(View view) {
		// Records 
		EditText editDoctorDate = (EditText) 
				findViewById(R.id.edit_doctor_date);
		EditText editDoctorTime = (EditText)
				findViewById(R.id.edit_doctor_time);
		
		String date = editDoctorDate.getText().toString();
		String time = editDoctorTime.getText().toString();

		// Checks correctness of input.
		if (validateInput(date, time)) {
			nurse.setSeenByDoctor(patient, date + " " + time);
			
			// Saves the user object and returns to the previous activity.
			nurse.save(getApplicationContext());
			returnIntentPatientActivity();
		}
	}
	
	/** 
	 * Validates date and time inputs.
	 * @param date Date that the patient sees a doctor.
	 * @param time Time that the patients sees a doctor.
	 * @return true iff the date and time are valid.
	 * */
	private boolean validateInput(String date, String time) {
		// Checks if input is missing and creates the corresponding
		// error message if it is.
		if (date.equals("") || time.equals("")) {
			Toast.makeText(getApplicationContext(), "Missing input",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// Checks if the date and time inputs are valid and creates the
		// corresponding error message if it is.
		else if (!matchDateTime(date, time)) {
			Toast.makeText(getApplicationContext(),
					"Invalid date or time input",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/** 
	 * Checks date and time input for correct format.
	 * @param date Date that the patient sees a doctor.
	 * @param time Time that the patients sees a doctor.
	 * @return true iff the date and time are valid.
	 * */
	private boolean matchDateTime(String date, String time) {
		// Uses a regular expression to make sure the input follows a
		// specific format.
		Pattern pdate = Pattern.compile("(0[1-9]|[12]\\d|3[01])" +
				"/(0[1-9]|1[012])/(19\\d\\d|20[01]\\d)");
		Pattern ptime = Pattern.compile("([01]\\d|2[0-3]):([0-5]\\d)");
		Matcher mdate = pdate.matcher(date);
		Matcher mtime = ptime.matcher(time);
		
		String[] dates = date.split("/");
		Integer day = Integer.parseInt(dates[0]);
		Integer month = Integer.parseInt(dates[1]);
		Integer year = Integer.parseInt(dates[2]);
		
		// Only returns true if both formats match, and the dates are
		// confirmed to be valid by matchDaysInMonth.
		return (mdate.matches() && mtime.matches() &&
				matchDaysInMonth(day, month, year));
	}
	
	/** 
	 * Checks date and time input for correct format.
	 * @param date Date that the patient sees a doctor.
	 * @param time Time that the patients sees a doctor.
	 * @return true iff the date is a valid date.
	 * */
	private boolean matchDaysInMonth(Integer day, Integer month,
			Integer year) {
		// Compares inputed date with the calendar dates.
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day == 31) {
				return false;
			}
		} else if (month == 2) {
			// Checks for leap years.
			if (year % 4 == 0) {
				if (day > 29) {
					return false;
				}
			} else {
				if (day > 28) {
					return false;
				}
			}
		}
		return true;
	}

	/**
     * Returns intent to the activity PrescriptionHistoryActivity.
     */
	private void returnIntentPatientActivity() {
		// Returns intent back to the PatientActivity.
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
