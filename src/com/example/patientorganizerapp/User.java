package com.example.patientorganizerapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class User implements Serializable {

	private static final long serialVersionUID = 8262013915654286663L;

	private String fileName = "user_file";

    /**
     * A map with a Patient's healthNumber as keys and the corresponding
     * Patient object as the value.
     */
	protected Map<String, Patient> patients;

    /**
     * Constructs a User by initializing patients to saved data, if it exists.
     */
	public User(Context fileContext){
		patients = new HashMap<String,Patient>();
		
		File file = fileContext.getFileStreamPath(fileName);
		
    	if(file.exists()) {
    		save(fileContext);
    	}
		load(fileContext);
	}
	
     /**
	 * Saves the patients data that this User object has accumulated into a file.
	 */
	public void save(Context fileContext) {
		FileOutputStream fos;
		try {
			fos = fileContext.openFileOutput(fileName, fileContext.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(patients);
			out.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Sets patients to any saved data of a map with health card numbers as
	 * keys and Patients as values.
	 */
	@SuppressWarnings("unchecked")
	public void load(Context fileContext) {
		try {
			FileInputStream fis = fileContext.openFileInput(fileName);
			ObjectInputStream in = new ObjectInputStream(fis);
			try {
				patients = (HashMap<String, Patient>)in.readObject();
				in.close();
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the Patient object that is associated with a the parameter
	 * healthNum, returns null if the Health Card Number does not exist as a
	 * key in patients.
	 * @param healthNum The Health Card Number of the Patient.
	 * @return The Patient associated to the parameter healthNum.
	 */
	public Patient findPatient(String healthNum){
		if (patients.containsKey(healthNum))
			return patients.get(healthNum);
		else
			return null;
	}

}
