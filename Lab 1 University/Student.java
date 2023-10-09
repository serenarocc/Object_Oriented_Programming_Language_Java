package university;

 class Student {

	private static final String SEPARATOR=" ";
	//attributi 
	private final int ID;
	private final String first;//nome e cognome studente
	private final String last;

	private Course[] courses;
	private Exam[] exams;//vettore di esami
	private int nextExam=0;

	

	
	//genero il costruttore con: source->generate construactor using fields:
public Student(Integer id, String first, String last) {//costruttore ha il nome della classe
		//super(); serve per ereditarieta non ci serve
		this.ID=id;
		this.first = first;
		this.last = last;
		courses = new Course[University.MAX_COURSES_PER_STUDENT];
		exams = new Exam[University.MAX_COURSES_PER_STUDENT];

	}


//metodo per rappresentare come stringa stud: source-> generate toString()

 @Override
	public String toString() {
		return  + ID + " " + first +  " " + last;
	}


 
 public void enroll(Course c){
		for(int i=0; i< courses.length; ++i){
			if( courses[i] == null){//se e' nullo
				courses[i] = c;//lo assegno
				break;
			}
		}
	}

	public String courses() {//simile a classee attenda classe course riga 37
		StringBuilder result = new StringBuilder();
		for(Course c : courses){
			if(c!=null){
				result.append(c).append("\n");
			}
		}
		return result.toString();
	}
	
	void addExam(Exam e) {
		exams[nextExam++] = e;
	}
	
	public static boolean isValid(double x) {
		return ! Double.isNaN(x);//ritorno vero se lo specifico numero e' un non-numero
		//value oppure falso
	}
	
	double average() {//lavora su vettore exam
		if(nextExam==0) return Double.NaN;
		double average = 0.0;
		for(Exam e : exams) {
			if(e==null) break;
			average += e.getGrade();
		}
		return average/nextExam;//restituisce media
	}

 
 


// I GETTER CONVENZIONALMENTE SI METTONO ALLA FINE
//devo creare 3 metodi getter per ottenere i 3 attributi:
//source->generate getter and setters->seleziono solo i getter per i 3 attributi

	public Integer getId() {
		return ID;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}



	public double getScore() {
		double avg = average();
		if(! isValid(avg)) return avg;
		int taken = nextExam;
		int enrolled = 0;
		while(courses[enrolled]!=null) {enrolled++;}
		
		return avg + 10*taken/(double)enrolled;
	}
 }
 


