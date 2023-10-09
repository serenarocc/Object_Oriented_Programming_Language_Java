package university;

public class Exam {
	private final int grade;//voto dell'esame
	
	Exam(Student student, Course course, int grade) {
		super();//eredita'
		this.grade = grade;
		student.addExam(this);
		course.addExam(this);
	}

	public int getGrade() {
		return grade;
	}

}
