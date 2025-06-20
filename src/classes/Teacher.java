package classes;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import backend.*;
import frontend.Main;

public class Teacher extends User {
	private final String table = "TEACHERS";
	private int id;
	private int userId;

	public String name, phone, address;
	public int grNo;
	public String department;

	public ArrayList<Subject> subjects;

	public Teacher(int id, int userId, String email, String name, String pass, String dept, int grNo, String phone, String address) {
		super(userId, email, pass, "TEACHER");
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.department = dept;
		this.grNo = grNo;
		this.phone = phone;
		this.address = address;
		this.subjects = Subject.getTeacherSubjects(Main.db, id);
	}

	public static boolean createTable(Database db) {
		String[] conditions = {
			"ID			INT		PRIMARY KEY		GENERATED BY DEFAULT AS IDENTITY",
			"USERID		INT						NOT NULL",
			"NAME 		TEXT					NOT NULL",
			"DEPT		TEXT					NOT NULL",
			"GRNO		INT						NOT NULL",
			"PHONE		TEXT					NULL",
			"ADDR		TEXT					NULL",
		};
		db.createTable("TEACHERS", conditions);
		return true;
	}

	public static boolean dropTable(Database db) {
		db.dropTable("TEACHERS");
		return true;
	}

	public static void insert(Database db, int userId, String name, String dept, int grNo, String phone, String address) {
		String schema = "USERID, NAME, DEPT, GRNO, PHONE, ADDR";
		String values = String.format("%d, '%s', '%s', %d, '%s', '%s'", userId, name, dept, grNo, phone, address);

		try {
			db.insertRow("TEACHERS", schema, values);
		} catch (Exception e) {
			System.out.println("TEACHER Error while inserting TEACHER " + userId);
			e.printStackTrace();
		}
	}

	public void update(Database db) {
		super.update(db);
		String[] conditions = {
			String.format("USERID=%d", this.userId),
			String.format("NAME='%s'", this.name),
			String.format("DEPT='%s'", this.department),
			String.format("GRNO=%d", this.grNo),
			String.format("PHONE='%s'", this.phone),
			String.format("ADDR='%s'", this.address),
		};

		String idCond = String.format("ID=%d", this.id);

		try {
			db.updateRow(this.table, conditions, idCond);
		} catch (Exception e) {
			System.out.println("TEACHER Error while updating TEACHER " + this.id);
			e.printStackTrace();
		}
	}

	public static Teacher login(Database db, int userId) {
		User u = User.getById(db, userId);
		if (u == null) {
			System.out.println("No user with user id " + userId);
			return null;
		}

		String[] conditions = { String.format("USERID = %d", userId) };

		try {
			ResultSet rs = db.getRows("TEACHERS", conditions);
			if (rs != null) {
				while (rs.next()) {
					int id = rs.getInt("ID");
					String name = rs.getString("NAME");
					String dept = rs.getString("DEPT");
					int grNo = rs.getInt("GRNO");
					String phone = rs.getString("PHONE");
					String address = rs.getString("ADDR");

					return new Teacher(id, userId, u.email, name, u.password, dept, grNo, phone, address);
				}
			}
		} catch (Exception e) {
			System.out.println("Teacher Error while logging in teacher");
			e.printStackTrace();
		}

		return null;
	}

	public void printAcademicInfo() {
		System.out.println("Academic Details");
		System.out.println("  Teacher Id: " + this.id);
		System.out.println("  Teacher Name: " + this.name);
		System.out.println("  Department: " + this.department);
		System.out.println("  GR No.: " + this.grNo);
		System.out.println("  Phone: " + this.phone);
		System.out.println("  Address: " + this.address);
	}

	public void getInfo() {
		System.out.println("TEACHER " + this.id);
		System.out.println("  USER ID: " + this.userId);
		System.out.println("  NAME: " + this.name);
		System.out.println("  DEPT: " + this.department);
		System.out.println("  Subjects: ");
		for (Subject s : this.subjects) {
			System.out.println("    " + s.name);
		}
	}

	public void addSubject(Subject s) {
		if (!this.subjects.contains(s)) {
			this.subjects.add(s);
		}
	}

	public void removeSubject(Subject s) {
		this.subjects.remove(s);
	}

	public static void printTeachers(Database db) {
		ArrayList<HashMap<String, Object>> temp = new ArrayList<>();

		try {
			ResultSet rs = db.getRows("TEACHERS");
			System.out.println("---------------------------------------------------------------------------");
			System.out.println("ID \tUSERID \tNAME \t\tDEPT \tGR No. \t\tPHONE \t\tADDRESS");
			System.out.println("---------------------------------------------------------------------------");

			while (!rs.isClosed() && rs.next()) {
				int id = rs.getInt("ID");
				int userId = rs.getInt("USERID");
				String name = rs.getString("NAME");
				String dept = rs.getString("DEPT");
				int grNo = rs.getInt("GRNO");
				String phone = rs.getString("PHONE");
				String address = rs.getString("ADDR");

				HashMap<String, Object> teacher = new HashMap<>();
				teacher.put("ID", id);
				teacher.put("USERID", userId);
				teacher.put("NAME", name);
				teacher.put("DEPT", dept);
				teacher.put("GRNO", grNo);
				teacher.put("PHONE", phone);
				teacher.put("ADDR", address);

				temp.add(teacher);
			}

			for (HashMap<String, Object> hashmap : temp) {
				int id = (int) hashmap.get("ID");
				int userId = (int) hashmap.get("USERID");
				String name = (String) hashmap.get("NAME");
				String dept = (String) hashmap.get("DEPT");
				int grNo = (int) hashmap.get("GRNO");
				String phone = (String) hashmap.get("PHONE");
				String address = (String) hashmap.get("ADDR");

				System.out.print(String.format("%d \t%d \t%s \t%s \t%d \t%s \t%s", id, userId, name, dept, grNo, phone, address));

				ArrayList<Subject> subjects = Subject.getTeacherSubjects(Main.db, id);
				for (Subject s : subjects) {
					System.out.print(" \t" + s.name);
				}
				System.out.println();
			}
			System.out.println("---------------------------------------------------------------------------");

		} catch (Exception e) {
			System.out.println("TEACHER Error while printing TEACHERS");
			e.printStackTrace();
		}
	}
}
