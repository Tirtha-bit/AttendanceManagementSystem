package frontend;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

import classes.*;

import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginGUI extends JFrame{

	private static final long serialVersionUID = -4417979625294510790L;
	private Font titleFont = new Font("Calibri", Font.BOLD, 30);
	private Font baseFont = new Font("Calibri", Font.BOLD, 20);
	private JTextField emailField;
	private JPasswordField passwordField;


	public LoginGUI() {

		JPanel loginpanel = new JPanel();
		getContentPane().add(loginpanel, BorderLayout.CENTER);
		loginpanel.setLayout(null);

		JLabel title = new JLabel("Attendance Management System");
		title.setFont(titleFont);
		title.setBounds(223, 49, 500, 45);
		loginpanel.add(title);

		JLabel loginas = new JLabel("Login As : ");
		loginas.setBounds(223, 161, 120, 32);
		loginas.setFont(baseFont);

		loginpanel.add(loginas);

		JComboBox<String> roleDropdown = new JComboBox<String>();
		roleDropdown.setModel(new DefaultComboBoxModel<String>(new String[] {"Student", "Teacher", "Admin"}));
		roleDropdown.setBounds(422, 162, 164, 28);
		loginpanel.add(roleDropdown);

		JLabel email = new JLabel("Email : ");
		email.setFont(new Font("Calibri", Font.BOLD, 20));
		email.setBounds(223, 221, 108, 32);
		loginpanel.add(email);

		JLabel password = new JLabel("Password : ");
		password.setFont(new Font("Calibri", Font.BOLD, 20));
		password.setBounds(223, 279, 150, 32);
		loginpanel.add(password);

		emailField = new JTextField();
		emailField.setBounds(422, 221, 164, 25);
		loginpanel.add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(422, 282, 164, 25);
		loginpanel.add(passwordField);

		JButton loginButton = new JButton("Login");
		loginButton.setBounds(223, 364, 155, 45);
		loginButton.setFont(baseFont);
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
        String username = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleDropdown.getSelectedItem().toString().toUpperCase();

        System.out.println("Login Attempted with:");
        System.out.println("Email: " + username);
        System.out.println("Role: " + role);

        if (username.length() > 0 && password.length() > 0 && role.length() > 0) {
            User u = User.login(Main.db, username, password);

            if (u != null) {
                u.getInfo();

                switch (role) {
                    case "STUDENT":
                        Student student = Student.login(Main.db, u.getId());
                        if (student != null) {
                            System.out.println("Student login successful.");
                            setVisible(false);
                            StudentDashboard studentDashboard = new StudentDashboard(student);
                            studentDashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            studentDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            studentDashboard.setVisible(true);
                        }
                        break;

                    case "TEACHER":
                        Teacher teacher = Teacher.login(Main.db, u.getId());
                        if (teacher != null) {
                            System.out.println("Teacher login successful.");
                            setVisible(false);
                            TeacherDashboard teacherDashboard = new TeacherDashboard(teacher);
                            teacherDashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            teacherDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            teacherDashboard.setVisible(true);
                        }
                        break;

                    case "ADMIN":
                        Admin admin = Admin.login(Main.db, u.getId());
                        if (admin != null) {
                            System.out.println("Admin login successful.");
                            setVisible(false);
                            AdminDashboard adminDashboard = new AdminDashboard(admin);
                            adminDashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            adminDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            adminDashboard.setVisible(true);
                        } else {
                            System.out.println("Admin.login() returned null.");
                        }
                        break;

                    default:
                        System.out.println("Unknown role selected.");
                        break;
                }
            } else {
                System.out.println("User login failed. Invalid credentials.");
            }
        } else {
            System.out.println("Username or password or role is empty.");
        }
    }
});

		loginpanel.add(loginButton);

//		JButton closeButton = new JButton("Close");
//		closeButton.setBounds(405, 364, 181, 45);
//		closeButton.setFont(baseFont);
//		loginpanel.add(closeButton);
//		closeButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				setVisible(false);
//			}
//		});

		this.setBounds(0, 0, 840, 570);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
