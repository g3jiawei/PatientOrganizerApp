package com.example.patientorganizerapp;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class PatientActivity extends Activity {
	
	/** A nurse. */
	private Nurse nurse;
	
	/** A physician. */
	private Physician physician;
	
	/** A patient. */
	private Patient patient;
	
	/** The type of user that is using the app. */
	private String userType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Initializes the user and the buttons depending on the user.
		initUser();
		setupButtons();
		
		// Displays all of the latest patient info.
		setPatientInfo();
		setPatientVitals();
		setPatientPrescription();
		setPatientDoctorTime();
	}

	/** 
	 * Initializes either the nurse or physician.
	 * */
	private void initUser() {
		// Gets the patient whose information will be displayed.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		// Creates the user object, and the relevant patient depending
		// on the type of user.
		SharedPreferences settings = getSharedPreferences("Patient Organizer", 0);
		userType = settings.getString("User Type", "NURSE"); 
		
		if (userType.equals("NURSE")) {
			nurse = (Nurse)bundle.getSerializable("nurse");
			patient = nurse.findPatient(intent.getStringExtra("patient"));
		} else if (userType.equals("PHYSICIAN")) {
			physician = (Physician)bundle.getSerializable("physician");
			patient = physician.findPatient(intent.getStringExtra("patient"));
		}
	}
	
	/** 
	 * Initializes buttons for the page.
	 * */
	private void setupButtons() {
		// Adds buttons to existing xml layout.
		LinearLayout l_layout = (LinearLayout) findViewById(R.id.linearLayout1);

		Button btnUpdateVitals = new Button(this);
		Button btnRecordDoctorTime = new Button(this);
		Button btnUpdatePrescription = new Button(this);

		btnUpdateVitals.setText("Update Vitals");
		btnRecordDoctorTime.setText("Record Time Seen By Doctor");
		btnUpdatePrescription.setText("Add Prescription");
		
		// Sets up listeners to allow each button to perform an action.
		btnUpdateVitals.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	// Goes to the update vitals screen.
		    	launchIntentUpdateVitalsActivity(v);
		    }
		});
		btnRecordDoctorTime.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	// Goes to the record time seen by doctor screen.
		    	launchIntentRecordDoctorTimeActivity(v);
		    }
		});
		btnUpdatePrescription.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	// Goes to the update prescription screen.
		    	launchIntentUpdatePrescriptionActivity(v);
		    }
		});
		
		// Only nurses can have update vitals and record doctor checkup time,
		// while only physicians can add prescriptions.
		if (userType.equals("NURSE")) {
			l_layout.addView(btnUpdateVitals);
			l_layout.addView(btnRecordDoctorTime);
		} else if (userType.equals("PHYSICIAN")) {
			l_layout.addView(btnUpdatePrescription);
		}
	}

	/** 
	 * Sets the text for the patients general information in the layout. 
	 * */
	private void setPatientInfo () {
		// Sets all of the patients general information in their respective
		// text fields.
		TextView textViewName = (TextView) findViewById(R.id.name);
		TextView textViewBirth = (TextView) findViewById(R.id.date_of_birth);
		TextView textViewHealthCard = (TextView) findViewById
				(R.id.health_card_number);
		TextView textViewArrival = (TextView) findViewById(R.id.arrival_time);
		
		textViewName.setText(patient.getName());
		textViewBirth.setText(patient.getDoB());
		textViewHealthCard.setText(patient.getHealthNumber());
		textViewArrival.setText(patient.getArrivalTime());
	}
	
	/** 
	 * Sets the text for patient vitals in the layout. 
	 * */
	private void setPatientVitals() {
		// Sets all of the patients vitals information in their respective
		// text fields.
		TextView textViewTemperature = (TextView) findViewById
				(R.id.temperature);
		TextView textViewSystolic = (TextView) findViewById(R.id.systolic);
		TextView textViewDiastolic = (TextView) findViewById(R.id.diastolic);
		TextView textViewHeartRate = (TextView) findViewById(R.id.heart_rate);
		TextView textViewVitalsTime = (TextView) findViewById
				(R.id.vitals_time);
		TextView textViewUrgency = (TextView) findViewById
				(R.id.urgency);
		
		// Creates default text in case there are no vitals information, which
		// is the case for newly created patients.
		if (patient.getTemperature() != null) {
			textViewTemperature.setText(patient.getTemperature().toString());
		} else {
			textViewTemperature.setText("N/A");
		}
		if (patient.getBloodPressureSys() != null) {
			textViewSystolic.setText(patient.getBloodPressureSys().toString());
		} else {
			textViewSystolic.setText("N/A");
		}
		if (patient.getBloodPressureDias() != null) {
			textViewDiastolic.setText(patient.getBloodPressureDias().
					toString());
		} else {
			textViewDiastolic.setText("N/A");
		}
		if (patient.getHeartRate() != null) {
			textViewHeartRate.setText(patient.getHeartRate().toString());
		} else {
			textViewHeartRate.setText("N/A");
		}
		if (patient.getVitalsTime() != null) {
			textViewVitalsTime.setText(patient.getVitalsTime());
		} else {
			textViewVitalsTime.setText("N/A");
		}
		textViewUrgency.setText(patient.getUrgency().toString());
	}
	
	/** 
	 * Sets the text for patient prescription in the layout. 
	 * */
	private void setPatientPrescription() {
		// Sets all of the patients prescription information in their 
		// respective text fields.
		TextView textViewPrescriptionName = (TextView) findViewById
				(R.id.prescription_name);
		TextView textViewPrescriptionInstructions = (TextView) findViewById
				(R.id.prescription_instructions);
		
		textViewPrescriptionName.setText(patient.getRecentPrescriptionName());
		textViewPrescriptionInstructions.setText
			(patient.getRecentPrescriptionInstruc());
	}
	
	/** 
	 * Sets the text for date and time when patient performed a doctor checkup
	 * in the layout. 
	 * */
	private void setPatientDoctorTime() {
		// Sets the date and time that the patient last visited a doctor.
		TextView textViewDoctorTime = (TextView) findViewById
				(R.id.doctor_time);
		
		textViewDoctorTime.setText(patient.getLastSeenDoctor());
	}
	
	/**
     * Starts the activity VitalsHistoryActivity.
     * * @param view The activities view.
     */
	public void launchIntentVitalsHistoryActivity (View view) {
		Intent intent = new Intent(this, VitalsHistoryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("patient", patient);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
	}
	
	/**
     * Starts the activity PrescriptionHistoryActivity.
     * * @param view The activities view.
     */
	public void launchIntentPrescriptionHistoryActivity (View view) {
		Intent intent = new Intent(this, PrescriptionHistoryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("patient", patient);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
	}
	
	/**
     * Starts the activity UpdateVitalsActivity.
     * * @param view The activities view.
     */
	public void launchIntentUpdateVitalsActivity (View view) {
		// Sends objects required to update the patients vitals.
		Intent intent = new Intent(this, UpdateVitalsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
		intent.putExtra("patient", patient.getHealthNumber());
		startActivityForResult(intent, 2);
	}
	
	/**
     * Starts the activity RecordDoctorTimeActivity.
     * * @param view The activities view.
     */
	public void launchIntentRecordDoctorTimeActivity (View view) {
		// Sends objects required to update the patients doctor checkup time.
		Intent intent = new Intent(this, RecordDoctorTimeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
		intent.putExtra("patient", patient.getHealthNumber());
		startActivityForResult(intent, 3);
	}
	
	/**
     * Starts the activity UpdatePrescriptionActivity.
     * * @param view The activities view.
     */
	public void launchIntentUpdatePrescriptionActivity (View view) {
		// Sends objects required to update the patients prescriptions.
		Intent intent = new Intent(this, UpdatePrescriptionActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("physician", physician);
		intent.putExtras(bundle);
		intent.putExtra("patient", patient.getHealthNumber());
		startActivityForResult(intent, 4);
	}
	
	/** 
	 * Sets the text for patient vitals in the layout. 
	 * @param requestCode The code that defines which activities were 
	 * 					  initially started.
	 * @param resultCode The code that determines the results of the started
	 * 					  activities.
	 * @param data	The intent that was sent back.
	 * */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// Performs actions whenever a called activity is finished.
		// It either updates text on the page to display newly updated
		// information or does nothing when the back button is pressed in a
		// activity, or nothing gets updated.
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				patient = (Patient)bundle.getSerializable("patient");
		    }
			if (resultCode == RESULT_CANCELED) { 
			}
		} else if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				nurse = (Nurse)bundle.getSerializable("nurse");
				patient = (Patient)bundle.getSerializable("patient");
				
				setPatientVitals();
		    }
			if (resultCode == RESULT_CANCELED) { 
			}
		} else if (requestCode == 3) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				nurse = (Nurse)bundle.getSerializable("nurse");
				patient = (Patient)bundle.getSerializable("patient");
				
				setPatientDoctorTime();
		    }
			if (resultCode == RESULT_CANCELED) { 
			}
		} else if (requestCode == 4) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				physician = (Physician)bundle.getSerializable("physician");
				patient = (Patient)bundle.getSerializable("patient");
				
				setPatientPrescription();
		    }
			if (resultCode == RESULT_CANCELED) { 
			}
		}
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
