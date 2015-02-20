package com.example.patientorganizerapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Physician extends User implements Serializable {

	private static final long serialVersionUID = -5714815276650947129L;

	/**
     * Constructs a Physician by initializing a Map of health card numbers to
     * the corresponding patients from any saved data, if it exists.
     */
	public Physician(Context context){
		super(context);
	}

    /**
     * Records a prescription for patient with medName as the name and
     * instruct as the instructions. Sets the Patient's recent prescription
     * accordingly to the given medName and instructions.
     * @param patient The patient who the prescription is for.
     * @param medName The name what is being prescribed.
     * @param instructions The instructions on how to use what was prescribed.
     */
	public void addPatientPrescription(Patient patient, String medName, String instructions){
		List<String> prescription = new ArrayList<String>();
		prescription.add(medName);
		prescription.add(instructions);
		
		patient.setRecentPrescriptionName(medName);
		patient.setRecentPrescriptionInstruc(instructions);
		patient.addPrescription(prescription);
	}
}
