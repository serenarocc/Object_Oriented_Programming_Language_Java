package university;

 class Course {
	private static final String SEPARATOR=",";
	private int code;
	private String title;
	private String teacher;
	private Student[] students;
	private Exam[] exams;
	private int nextExam=0;
	
	
	public Course(int code,String title,String teacher) {//costruttore=stesso nome della classe
		this.code=code;
		this.title=title;
		this.teacher=teacher;
		this.students = new Student[University.MAX_STUDENTS_PER_COURSE];// x ogni corso alloco array di studenti
		this.exams = new Exam[University.MAX_COURSES_PER_STUDENT];// x ogni corso alloco array di esami
	}
	

	public String toString() {
		return code+SEPARATOR+title+SEPARATOR+teacher;
	}
	
	
	public void enroll(Student s) {
		for(int i=0;i<students.length;i++) {
			if(students[i]==null) {
				students[i]=s;
				break;
			}
		}
	}
	
	public String attendees() {
		StringBuffer result= new StringBuffer();
		//StringBuffer = costruttore di stringhe da come risultato una stringa
		//StringBuffer = costruisce un generatore di strighe senza caratteri e con una capacita iniziale di 16 caratteri
		
		for(Student s: students) {//FOR EACH: x ogni studente s che fa parte dell array students fai un' azione
			//prendo il singolo elemento faccio una det cosa x quel singolo elemento. Termino quando faccio quella cosa per tutti gli elem dell'array
			
			if(s!=null) {
				result.append(s.toString()).append("\n");//gli stud appaiono uno per riga(riga termina con un \n)
				// .append() il contenuto () viene agiunto alla stampa
				//stringa result + stringa to string + a capo
			}
		}
		return result.toString();
	}
	
	
	public void addExam(Exam exam) {
		exams[nextExam++] = exam;
	}
	
	double average() {
		if(nextExam==0) return Double.NaN;
		double average = 0.0;
		for(Exam e : exams) {
			if(e==null) break;
			average += e.getGrade();
		}
		return average/nextExam;
	}
	
	public static boolean isValid(double x) {
		return !Double.isNaN(x);
	}


	public Object getTitle() {
		// TODO Auto-generated method stub
		return title;
	}
	

}
