package com.example.patientorganizerapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

import android.content.Context;

/**
 * A Nurse.
 * @author c2mckenn
 * @author g3jiawei
 * @author c3yaucle
 * @author
 */
public class Nurse extends User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2081334363493187035L;

	private List<Patient> patientUrgencies;

	/** 
	 * Constructs a Nurse and initializes patients map with saved data, if
	 * it exists. Puts the patients who have not seen a Physician into the list
	 * sorted by patient urgencies.
	 */
	public Nurse(Context context){
		super(context);
		patientUrgencies = new ArrayList<Patient>(); 
		for (Patient patient : patients.values()){
			if (!patient.getSeenDoctor())
				patientUrgencies.add(patient);
		}
		sort();
	}

	/**
	 * This creates a Patient object and adds it into patients.
	 * @param name The name of the Patient to be created.
	 * @param DoB The date of birth of the Patient to be created in DD/MM/YY.
	 * @param arrivalTime The time the Patient to be created arrived at the
	 *                    hospital.
	 * @param healthNum The Health Card Number of the Patient to be created.
	 */
	public void addPatient(String name, String DoB, String arrivalTime, 
			String healthNum){
		if (patients.containsKey(healthNum)){
			patientUrgencies.add(patients.get(healthNum));
			patients.get(healthNum).setSeenDoctor(false);
		}
		else{
			// Creates a patient
			Patient patient = new Patient(name, DoB, arrivalTime, healthNum);
			// Adds patient to list
			patients.put(patient.getHealthNumber(), patient);
			if (patient.getAge() < 2)
				patient.setUrgency(1);
			patientUrgencies.add(patient);
		}
	}

	/**
	 * Updates the specified Patient's vital signs.
	 * @param patient The Patient to be updated.
	 * @param temp The new temperature of the Patient to be updated.
	 * @param bloodPressD The new blood pressure of the Patient to be updated
	 *                    measured in diatolic.
	 * @param bloodPressS The new blood pressure of the Patient to be updated
	 *                    measured in systolic.
	 * @param heartRate The new heart rate of the Patient to be updated.
	 * @param time The time this Patient is being updated.
	 */
	public void updatePatientVitals(Patient patient, Double temp, Integer bloodPressD,
			Integer bloodPressS, Integer heartRate, String time){
		patient.setTemperature(temp);
		patient.setBloodPressureDias(bloodPressD);
		patient.setBloodPressureSys(bloodPressS);
		patient.setHeartRate(heartRate);
		patient.setVitalsTime(time);
		patient.setVitalsHistory();             
		patient.setUrgency(calculateUrgency(patient));
		sort();
	}


	/**
	 * Calculates patient's urgency based on hospital policy.
	 * @param patient The patient to whose urgency is being calculated.
	 * @return The patient's urgency (numeric value).
	 */
	public Integer calculateUrgency(Patient patient){
		int urgency = 0;

		if (patient.getTemperature() >= 39)
			urgency ++;
		if (patient.getBloodPressureDias() >= 90 || patient.getBloodPressureSys() >= 140)
			urgency ++;
		if (patient.getHeartRate() >= 100 || patient.getHeartRate() <= 50)
			urgency ++;
		if(patient.getAge() < 2)
			urgency ++;

		return urgency;
	}

	/**
	 * Returns a list of patients that have not yet seen a Physician,
	 * ordered by decreasing urgency.
	 * @return A list of patients sorted by decreasing urgency.
	 */
	public List<Patient> getPatientUrgencies(){
		return this.patientUrgencies;
	}

	/**
	 * Sets patient's seenDoctor variable to true, and removes patient from
	 * the list of patients ordered by urgecy.
	 * @param patient The patient who has seen the Physician.
	 * @param dateTime The date and time this patient was seen by a Physician.
	 */
	public void setSeenByDoctor(Patient patient, String dateTime){
		patient.setLastSeenDoctor(dateTime);
		patient.addDoctorHistory(dateTime);
		patient.setSeenDoctor(true);
		patientUrgencies.remove(patient);
	}

	/**
	 * Sorts this Nurse's patientUrgencies list in order of decreasing
	 * patient urgency.
	 */
	public void sort() {
		if (patients.size() > 1)
			Collections.sort(patientUrgencies);
	}
}


