package com.example.patientorganizerapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class PatientsByUrgencyActivity extends Activity {

	/** The nurse. */
	private Nurse nurse;
	
	/** The patient. */
	private Patient patient;
	
	/** A arraylist of hashmaps of <String, String> used for creating a
	 *  listview. */
	private ArrayList<HashMap<String, String>> actionListPatients = 
			new ArrayList<HashMap<String, String>>();
	
	/** The list of patients not seen by a doctor. */
	private List<Patient> patientsByUrgency = new ArrayList<Patient>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients_by_urgency);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Gets intent containing nurse, as only nurse can access this
		// activity.
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		nurse = (Nurse)bundle.getSerializable("nurse");
		patientsByUrgency = nurse.getPatientUrgencies();
		
		// Sets up the list of patients.
		initPatientsList();
		// Sets up the listview.
		initListView();
	}

	/** 
	 * Initializes the list of patients.
	 * */
	private void initPatientsList() {
		for(int i = 0; i < patientsByUrgency.size(); i++){
			patient = patientsByUrgency.get(i);
			// Adds items to actionListPatients to be displayed in the list.
			actionListPatients.add(createItem(patient));
	    }
	}

	/** 
	 * Creates a hashmap of <String, String> which show patient information.
	 * @param patient A patient.
	 * @return hashmap of <String, String> containing patient information.
	 * */
	private HashMap<String, String> createItem(Patient patient) {
		HashMap<String,String> item = new HashMap<String,String>();
		String info1, info2;
		
		// Shows all necessary information for this list.
		info1 = "(Urg:" + patient.getUrgency() + ")  " + patient.getName();
		info2 = "DOB: " + patient.getDoB() + 
			"   HC#: " + patient.getHealthNumber();
			
		item.put("line1", info1);
		item.put("line2", info2);
		
		return item;
	}

	/** 
	 * Initializes a list view in the layout.
	 * */
	private void initListView() {
		// Adds an adapter to an existing listview, which will show two lines
		// of information for each patient in the list of patients by
		// decreasing urgency.
		ListView lv = (ListView) findViewById(R.id.listView);
		SimpleAdapter simpleAdpt = new SimpleAdapter(this, actionListPatients,
				android.R.layout.two_line_list_item,
				new String[] {"line1", "line2"},
				new int[] {android.R.id.text1, android.R.id.text2});
		lv.setAdapter(simpleAdpt);
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				// When an item is clicked, the corresponding patient
				// activity information page will be displayed.
				launchIntentPatientActivity(patientsByUrgency.get(position));
			}
	    });
	}
	
	/**
     * Starts the activity PatientActivity.
     * * @param patient A patient.
     */
	private void launchIntentPatientActivity(Patient patient) {
		// Creates a PatientActivity for a desired patient.
		Intent intent = new Intent(this, PatientActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nurse", nurse);
		intent.putExtras(bundle);
		intent.putExtra("patient", patient.getHealthNumber());
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
