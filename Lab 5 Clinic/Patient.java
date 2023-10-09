package clinic;

class Patient implements Comparable<Patient> {
	private final String first;
	private final String last;
	private final String ssn;
	private Doctor doctor;  
	// int doctorId;  // ~ foreign key, meno flessibile

	Patient(String first, String last, String ssn) {
		super();
		this.first = first;
		this.last = last;
		this.ssn = ssn;
	}

	public String getSSN(){
		return ssn;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	void setDoctor(Doctor d) {
		if(doctor!=null){
			doctor.removeMe(this);
		}
		doctor=d;
		doctor.addPatient(this);
	}

	@Override
	public int compareTo(Patient o) {
		int dl =  this.last.compareTo(o.last);
		if(dl!=0) return dl;
		return this.first.compareTo(o.first);
	}
	
	@Override
	public String toString() {
		return last + " " + first + " (" + ssn + ")";
	}

}
