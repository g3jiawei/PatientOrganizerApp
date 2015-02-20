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
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class AddPatientActivity extends Activity {
	
	/** The nurse. */
	private Nurse nurse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_patient);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		nurse = (Nurse)bundle.getSerializable("nurse");
	}
	
	/**
     * Attempt to create a new patient when save button is clicked. 
     * * @param view The layouts view.
     */
	public void saveNewPatient(View view) {
		EditText editName = (EditText) findViewById(R.id.edit_name);
		EditText editDateOfBirth = (EditText)
				findViewById(R.id.edit_date_of_birth);
		EditText editHealthCardNumber = (EditText)
				findViewById(R.id.edit_health_card_number);
		EditText editArrivalTime = (EditText)
				findViewById(R.id.edit_arrival_time);
		
		String name = editName.getText().toString();
		String dateOfBirth = editDateOfBirth.getText().toString();
		String healthCardNumber = editHealthCardNumber.getText().toString();
		String arrivalTime = editArrivalTime.getText().toString();
		
		if (validateInput(name, dateOfBirth, healthCardNumber, arrivalTime)) {
			// Gets current date from built-in calendar as the default date
			// for arrival time.
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = df.format(calendar.getTime());
			String arrivalDateTime = currentDate + " " + arrivalTime;
			
			// Adds this patient to the user object.
			nurse.addPatient(name, dateOfBirth, arrivalDateTime,
					healthCardNumber);
			
			// Saves the user object.
			nurse.save(getApplicationContext());
			launchIntentPatientActivity(healthCardNumber);
		}
	}
	
	/**
     * Check the validity of the input.
     * @param name The patients name.
     * @param dateOfBirth The patients date of birth.
     * @param healthCardNumber The patients health card number.
     * @param arrivalTime The patients arrival time.
     * @return true iff input is valid.
     */
	private boolean validateInput(String name, String dateOfBirth,
			String healthCardNumber, String arrivalTime) {
		// Checks if input is missing and creates the corresponding
		// error message if it is.
		if (name.equals("") || dateOfBirth.equals("")
				|| healthCardNumber.equals("") || arrivalTime.equals("")) {
			Toast.makeText(getApplicationContext(), "Missing input",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		// Checks if the date and time inputs are valid and creates the
		// corresponding error message if it is.
		else if (!matchDateTime(dateOfBirth, arrivalTime)) {
			Toast.makeText(getApplicationContext(),
					"Invalid date or time input",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
     * Check the validity of the date and time inputs.
     * @param dateOfBirth The patients date of birth.
     * @param arrivalTime The patients arrival time.
     * @return true iff input is valid.
     */
	private boolean matchDateTime(String dateOfBirth, String arrivalTime) {
		// Uses a regular expression to make sure the input follows a
		// specific format.
		Pattern pdate = Pattern.compile("(0[1-9]|[12]\\d|3[01])" +
				"/(0[1-9]|1[012])" +
				"/(19\\d\\d|20[01]\\d)");
		Pattern ptime = Pattern.compile("([01]\\d|2[0-3]):([0-5]\\d)");
		Matcher mdate = pdate.matcher(dateOfBirth);
		Matcher mtime = ptime.matcher(arrivalTime);
		
		String[] date = dateOfBirth.split("/");
		Integer day = Integer.parseInt(date[0]);
		Integer month = Integer.parseInt(date[1]);
		Integer year = Integer.parseInt(date[2]);
		
		// Only returns true if both formats match, and the dates are
		// confirmed to be valid by matchDaysInMonth.
		return (mdate.matches() && mtime.matches() &&
				matchDaysInMonth(day, month, year));
	}

	/**
     * Checks if the patients date of birth input matches calendar dates.
     * @param day The day the patient was born on.
     * @param month The month the patient was born on.
     * @param year The year the patient was born on.
     * @return true iff input is valid.
     */
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
     * Starts a new PatientActivity.
     * @param healthCardNumber The patients health card number.
     */
	private void launchIntentPatientActivity(String healthCardNumber) {
		// Goes to the information page for the newly created patient.
		Intent intent = new Intent(this, PatientActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
		intent.putExtra("patient", healthCardNumber);
		
		startActivity(intent);
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
