package com.example.patientorganizerapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Patient implements Serializable, Comparable<Patient>{

	private static final long serialVersionUID = -3609747857195109414L;
	
	/** The name of this Patient. */
	private String name;
	/** The date of birth of this Patient. */
	private String DoB;
	/** The arrival of this Patient. */
	private String arrivalTime;
	/** The health card number of this Patient. */
	private String healthNumber;
	/** The current temperature of this Patient. */
	private Double temperature;
	/** The current blood pressure of this Patient, measured in diastolic. */
	private Integer bloodPressureDias;
	/** The current blood pressure of this Patient, measured in systolic. */
	private Integer bloodPressureSys;
	/** The current heart rate of this Patient. */
	private Integer heartRate;
	/** The time this Patient's current vitals were recorded. */
	private String vitalsTime;
	/** The history of this Patient's vitals. */
	private List<String[]> vitalsHistory;
	/** This Patient's current urgency. */
	private Integer urgency;
	/** The most recent prescription's name for this Patient. */
    private String recentPrescriptionName;
    /** The most recent prescription's instructions for this Patient. */
    private String recentPrescriptionInstruc;
    /** The history of prescriptions for this Patient. */
    private List<List<String>> prescriptions;
    /** The last time this Patient was seen by a Physician. */
    private String lastSeenDoctor;
    /** The history of when this Patient has seen a Physician. */
    private List<String> doctorHistory;
    /** True if this Patient has been seen by a Physician during this visit. */
    private boolean seenDoctor = false;
	/**
	 * Constructor to create a new Patient with the given information.
	 * @param name Patient's name
	 * @param DoB Patient's Date of Birth
	 * @param arrivalTime Time of arrival
	 * @param healthNum Patient's healthcare number
	 */
	public Patient(String name, String DoB, String arrivalTime, 
			String healthNum){
		
		this.name = name;
		this.DoB = DoB;
		this.arrivalTime = arrivalTime;
		this.healthNumber = healthNum;
		temperature = null;
		bloodPressureDias = null;
		bloodPressureSys = null;
		heartRate = null;
		vitalsTime = null;
		urgency = 0;
		
        vitalsHistory = new ArrayList<String[]>();
        prescriptions = new ArrayList<List<String>>();
        doctorHistory = new ArrayList<String>();
	}
	/**
	 * Getter for Patient's name
	 * @return name of Patient
	 */
	public String getName() {
		return name;
	}
	/**
	 * Getter for the Patient's DoB
	 * @return Date of Birth
	 */
	public String getDoB() {
		return DoB;
	}
	/**
	 * Getter for the Patient's arrival time
	 * @return arrival time
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}
	/**
	 * Getter for the Patient's healthcare number
	 * @return Patient healthcare number
	 */
	public String getHealthNumber() {
		return healthNumber;
	}
	
	/**
	 * Getter for the Patient's temperature
	 * @return Patient's temperature
	 */
	public Double getTemperature() {
		return temperature;
	}
	/**
	 * Setter for the Patient's temperature
	 * @param temperature new temperature
	 */	
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	/**
	 * Getter for the Patient's bloodPressureDias
	 * @return Patient's blood pressure in diatolic
	 */
	public Integer getBloodPressureDias() {
		return bloodPressureDias;
	}
	/**
	 * Setter for the Patient's bloodPressureDias
	 * @param bloodPressureDias new Blood Pressure Dias
	 * 
	 */
	public void setBloodPressureDias(Integer bloodPressureDias) {
		this.bloodPressureDias = bloodPressureDias;
	}
	/**
	 * Getter for the Patient's bloodPressureSys
	 * @return Blood Pressure measured in systolic
	 */
	public Integer getBloodPressureSys() {
		return bloodPressureSys;
	}
	/**
	 * Setter for the Patient's bloodPressureSys
	 * @param bloodPressureSys new Blood Pressure Sys
	 */
	public void setBloodPressureSys(Integer bloodPressureSys) {
		this.bloodPressureSys = bloodPressureSys;
	}
	/**
	 * Getter for the Patient's heart rate
	 * @return Patient's heart rate
	 */
	public Integer getHeartRate() {
		return heartRate;
	}
	/**
	 * Setter for the Patient's heart rate
	 * @param heartRate new Patient's heart rate
	 */
	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}
	/**
	 * Getter for the Patient's vitals time
	 * @return Patient's Vitals time
	 */
	public String getVitalsTime() {
		return vitalsTime;
	}
	/**
	 * Setter for the Patient's vitals time
	 * @param vitalsTime new vitals time for patient
	 */
	public void setVitalsTime(String vitalsTime) {
		this.vitalsTime = vitalsTime;
	}
	/**
	 * Getter for the Patient's vitals history
	 * @return List of Patient's vitals History
	 */
	public List<String[]> getVitalsHistory() {
		return vitalsHistory;
	}

    /**
     * Setter for the Patient's vitals time
     * @param vitalsTime new vitals time for patient
     */
	public void setVitalsHistory() {
		String[] vitals = new String[]{vitalsTime, temperature.toString(),
				bloodPressureSys.toString(), bloodPressureDias.toString(),
				heartRate.toString()};

		vitalsHistory.add(vitals);
	}

    /**
     * Calculates and returns the age of this Patient according to the
     * current date.
     * @return The age of this Patient.
     */
	public Integer getAge(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = df.format(calendar.getTime());

		int age = 0;

		String[] date = currentDate.split("/");
		String[] birth = DoB.split("/");

		Integer currentDay = Integer.parseInt(date[0]);
		Integer currentMonth = Integer.parseInt(date[1]);
		Integer currentYear = Integer.parseInt(date[2]);

		Integer birthDay = Integer.parseInt(birth[0]);
		Integer birthMonth = Integer.parseInt(birth[1]);
		Integer birthYear = Integer.parseInt(birth[2]);

		age = currentYear - birthYear;
		if (birthMonth > currentMonth) {
			age --;
		} else if (birthMonth == currentMonth) {
			if (birthDay > currentDay) {
				age --;
			}
		}

		return age;
	}

    /**
     * Sets this Patients urgency to urgency.
     * @param urgency The urgency of this Patient.
     */
	public void setUrgency(Integer urgency){
		this.urgency = urgency;
	}

    /**
     * Returns this Patient's urgency.
     * @return This Patient's urgency.
     */
	public Integer getUrgency(){
		return urgency;
	}

    /**
     * Returns the last prescription's name prescribed to this Patient.
     * @return The most recent presciption name of this Patient.
     */
	public String getRecentPrescriptionName(){
		return recentPrescriptionName;
	}

    /**
     * Sets this Patient's most recent prescription to recentPrescriptionName.
     * @param recentPrescriptionName The name of the last prescription that
     * was prescribed to this Patient.
     */
	public void setRecentPrescriptionName(String recentPrescriptionName){
		this.recentPrescriptionName = recentPrescriptionName;
	}

    /**
     * Returns the most recent prescription instrctions of this Patient.
     * @return The most recent Prescription Instructions of this Patient.
     */
	public String getRecentPrescriptionInstruc(){
		return recentPrescriptionInstruc;
	}

    /**
     * Sets the most recent prescription instructions of this patient to
     * recentPrescriptionInstruc.
     * @param recentPrescriptionInstruc The most recent prescription
     * instructions of this Patient.
     */
	public void setRecentPrescriptionInstruc(String recentPrescriptionInstruc){
		this.recentPrescriptionInstruc = recentPrescriptionInstruc;
	}

    /**
     * Returns the prescription history of this Patient.
     * @return All prescriptions prescribed to this Patient.
     */
	public List<List<String>> getPrescriptions(){
		return prescriptions;
	}

    /**
     * Adds prescription to this Patients prescription history.
     * @param prescription The prescription to be added.
     */
	public void addPrescription(List<String> prescription){
		prescriptions.add(prescription);
	}

    /**
     * Returns the last time that this Patient was seen by a Physician.
     * @return the last time this Patient was seen by a Physician.
     */
	public String getLastSeenDoctor(){
		return lastSeenDoctor;
	}

    /**
     * Sets the lastSeenDoctor variable of this Patient to lastSeenDoctor
     * parameter.
     * @param lastSeenDoctor The time this Patient last saw a Physician.
     */
	public void setLastSeenDoctor(String lastSeenDoctor){
		this.lastSeenDoctor =  lastSeenDoctor;
	}

    /**
     * Returns this Patient's doctor history.
     * @return This Patient's doctor history.
     */
	public List<String> getDoctorHistory(){
		return doctorHistory;
	}

    /**
     * Adds dateTime to this Patient's doctor history.
     * @param dateTime The date and time this Patient was seen by a Physician.
     */
	public void addDoctorHistory(String dateTime){
		doctorHistory.add(dateTime);
	}

	/**
     * Returns true if and only if this Patient has been seen by a Physician.
     * @return true if and only if this Patient has been seen by a Physician.
     */
	public boolean getSeenDoctor(){
		return seenDoctor;
	}
	
    /**
     * Sets this Patient's seenDoctor variable to seenDoctor.
     * @param seenDoctor true if and only if this Patient has seen a Physician.
     */
	public void setSeenDoctor(boolean seenDoctor) {
		this.seenDoctor = seenDoctor;
	}

    /**
     * Overrides compareTo method in Comparable. Used in sorting.
     */
	@Override
	public int compareTo(Patient object) {
		return object.getUrgency() - this.getUrgency();
	}
}
