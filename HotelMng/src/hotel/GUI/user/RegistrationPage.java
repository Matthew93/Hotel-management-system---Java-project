package hotel.GUI.user;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.Query;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import hotel.DAO.*;
import hotel.utils.*;

public class RegistrationPage {

	JFrame frmSignUp;
	private JTextField firstNameTxt;
	private JTextField lastNameTxt;
	private JTextField phoneNumberTxt;
	private JTextField emailTxt;
	private JPasswordField passwordTxt;
	private boolean validPhoneNumber = false;
	private boolean validEmail = false;
	private boolean validPassword = false;
	Validations validate = new Validations();

	/**
	 * Create the application.
	 */
	public RegistrationPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSignUp = new JFrame();
		frmSignUp.setTitle("Sign up");
		frmSignUp.setBounds(100, 100, 339, 600);
		frmSignUp.getContentPane().setLayout(null);

		JLabel lbFirstName = new JLabel("First Name:");
		lbFirstName.setBounds(49, 182, 79, 24);
		frmSignUp.getContentPane().add(lbFirstName);

		firstNameTxt = new JTextField();
		firstNameTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

			}
		});
		firstNameTxt.setBounds(136, 184, 123, 20);
		frmSignUp.getContentPane().add(firstNameTxt);
		firstNameTxt.setColumns(10);

		JLabel lbLastName = new JLabel("Last Name:");
		lbLastName.setBounds(49, 235, 79, 20);
		frmSignUp.getContentPane().add(lbLastName);

		lastNameTxt = new JTextField();
		lastNameTxt.setBounds(136, 235, 123, 20);
		frmSignUp.getContentPane().add(lastNameTxt);
		lastNameTxt.setColumns(10);

		JLabel lbPhoneNumber = new JLabel("Phone number:");
		lbPhoneNumber.setBounds(30, 285, 98, 20);
		frmSignUp.getContentPane().add(lbPhoneNumber);

		JLabel lbCheckPhoneNumber = new JLabel("");

		lbCheckPhoneNumber.setForeground(Color.RED);
		lbCheckPhoneNumber.setBounds(119, 310, 194, 14);
		frmSignUp.getContentPane().add(lbCheckPhoneNumber);

		JLabel lbCheckEmail = new JLabel("");

		lbCheckEmail.setForeground(Color.RED);
		lbCheckEmail.setBounds(119, 369, 194, 14);
		frmSignUp.getContentPane().add(lbCheckEmail);

		JLabel lbCheckPassword = new JLabel("");
		lbCheckPassword.setForeground(Color.RED);
		lbCheckPassword.setBounds(49, 435, 210, 14);
		frmSignUp.getContentPane().add(lbCheckPassword);

		phoneNumberTxt = new JTextField();
		phoneNumberTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lbCheckPhoneNumber.setText("");

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!validate.checkPhone(phoneNumberTxt.getText())) {
					lbCheckPhoneNumber.setText("Invalid phone number!");
					validPhoneNumber = false;

				} else {
					validPhoneNumber = true;
					lbCheckPhoneNumber.setText("");
				}

			}
		});
		phoneNumberTxt.setBounds(136, 285, 123, 20);
		frmSignUp.getContentPane().add(phoneNumberTxt);
		phoneNumberTxt.setColumns(10);

		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setBounds(62, 347, 47, 24);
		frmSignUp.getContentPane().add(lblNewLabel);

		emailTxt = new JTextField();
		emailTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!validate.checkEmail(emailTxt.getText())) {
					lbCheckEmail.setText("Invalid email!");
					validEmail = false;

				} else {
					validEmail = true;
					lbCheckEmail.setText("");
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				lbCheckEmail.setText("");
			}
		});

		emailTxt.setColumns(10);
		emailTxt.setBounds(136, 349, 123, 20);
		frmSignUp.getContentPane().add(emailTxt);

		JLabel lbPasswordTxt = new JLabel("Password:");
		lbPasswordTxt.setBounds(50, 402, 79, 24);
		frmSignUp.getContentPane().add(lbPasswordTxt);

		passwordTxt = new JPasswordField();
		passwordTxt.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (!validate.checkPassword(passwordTxt.getText())) {
					lbCheckPassword.setText("Invalid password!");
					validPassword = false;

				} else {
					validPassword = true;
					lbCheckPassword.setText("");
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				lbCheckPassword.setText("");
			}
		});

		passwordTxt.setBounds(136, 404, 123, 20);
		frmSignUp.getContentPane().add(passwordTxt);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(
				"C:\\Users\\Rodica\\git\\Hotel-management-system---Java-project\\HotelMng\\images\\j.jpg"));
		lblNewLabel_1.setBounds(62, 37, 203, 134);
		frmSignUp.getContentPane().add(lblNewLabel_1);

		JLabel lbEmptyFields = new JLabel("");
		lbEmptyFields.setForeground(Color.RED);
		lbEmptyFields.setBounds(10, 478, 303, 14);
		frmSignUp.getContentPane().add(lbEmptyFields);

		JButton RegistrationPageBtn = new JButton("Create account");
		RegistrationPageBtn.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				String passwordEncr = "";

				if ((firstNameTxt.getText().isEmpty() || lastNameTxt.getText().isEmpty()
						|| phoneNumberTxt.getText().isEmpty() || emailTxt.getText().isEmpty())
						|| (validPhoneNumber == false || validEmail == false || validPassword == false)) {
					lbEmptyFields.setText("All fields should be completed or some fields are invalid!");
				} else {
					//encrypt the user password
					try {
						Encrypting encryptPass = new Encrypting();

						passwordEncr = encryptPass.encrypt(passwordTxt.getText());

					} catch (Exception e1) {

						e1.printStackTrace();
					}

					lbEmptyFields.setText("");
					SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
							.addAnnotatedClass(Customer.class).addAnnotatedClass(Users.class).buildSessionFactory();
					Session session = factory.getCurrentSession();

					try {
						//create the customer with the user inputs
						Customer customer = new Customer(firstNameTxt.getText(), lastNameTxt.getText(),
								phoneNumberTxt.getText(), emailTxt.getText());

						session.beginTransaction();
						//create the user with the user inputs
						Users user = new Users(emailTxt.getText(), passwordEncr, "user");

						//check the email from the database if exists to avoid duplicate email
						Query userq = session.createQuery("from Users where email=:emailDb");
						userq.setParameter("emailDb", emailTxt.getText());
						@SuppressWarnings("unchecked")
						List<Users> getUser = userq.getResultList();
						if (getUser.size() > 0) {
							JOptionPane.showMessageDialog(RegistrationPageBtn, "Email already exists!");
						} else {

							session.save(customer);
							session.save(user);

							JOptionPane.showMessageDialog(RegistrationPageBtn, "Successfully registered!");
							frmSignUp.setVisible(false);


						}

						session.getTransaction().commit();
					} finally {

						firstNameTxt.setText("");
						lastNameTxt.setText("");
						emailTxt.setText("");
						phoneNumberTxt.setText("");
						passwordTxt.setText("");
						factory.close();

					}
				}
			}
		});
		RegistrationPageBtn.setBounds(10, 510, 151, 23);
		frmSignUp.getContentPane().add(RegistrationPageBtn);

		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSignUp.setVisible(false);

			}
		});
		cancelBtn.setBounds(171, 510, 114, 23);
		frmSignUp.getContentPane().add(cancelBtn);

	}
}
