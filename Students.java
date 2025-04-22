public class Students {
	private int roll_no;
	private String name;
	private String class;
	private char grade;
	private int age;

	public Students() {
	}

	public int getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(int roll_no) {
		this.roll_no = roll_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClass() {
		return class;
	}

	public void setClass(String class) {
		this.class = class;
	}

	public char getGrade() {
		return grade;
	}

	public void setGrade(char grade) {
		this.grade = grade;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Students {" +
				"roll_no: " + roll_no + ", " + "name: " + name + ", " + "class: " + class + ", " + "grade: " + grade + ", " + "age: " + age + "}";
	}
}