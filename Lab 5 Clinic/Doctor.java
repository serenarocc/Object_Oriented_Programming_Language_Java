package clinic;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Doctor extends Patient {
	private final int id;
	private final String specialization;
	private final List<Patient> patients = new LinkedList<>();
	
	public Doctor(String first, String last, String ssn, int docID,
			String specialization) {
		super(first,last,ssn);
		this.id=docID;
		this.specialization = specialization;
	}

	public int getId(){
		return id;
	}
	
	public String getSpecialization(){
		return specialization;
	}
	
	public Collection<Patient> getPatients() {
		Collections.sort(patients);
		return patients;
	}


	public void addPatient(Patient p) {
		patients.add(p);
	}

	void removeMe(Patient person) {
		patients.remove(person);
	}
	
	boolean isIdle() {
		return patients.isEmpty();
	}
	
	int numPatients() {
		return patients.size();
	}
	
	@Override
	public String toString() {
		return super.toString() + " [" + id + "] : " + specialization;
	}
}
