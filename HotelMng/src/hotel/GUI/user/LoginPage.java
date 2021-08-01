package hotel.GUI.user;

import hotel.DAO.*;
import hotel.GUI.admin.AdminPage;
import hotel.utils.Encrypting;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;

import javax.persistence.Query;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
public class LoginPage extends JFrame {

	public JFrame frmLogin;
	private JTextField emailField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 452, 420);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel lbEmailTxt = new JLabel("Email:");
		lbEmailTxt.setBounds(118, 211, 54, 29);
		frmLogin.getContentPane().add(lbEmailTxt);

		JLabel lbPasswordTxt = new JLabel("Password:");
		lbPasswordTxt.setBounds(111, 264, 64, 34);
		frmLogin.getContentPane().add(lbPasswordTxt);

		JLabel lbEmailErr = new JLabel("");
		lbEmailErr.setForeground(Color.RED);
		lbEmailErr.setBounds(151, 239, 180, 14);
		frmLogin.getContentPane().add(lbEmailErr);

		JLabel lbPasswordErr = new JLabel("");
		lbPasswordErr.setForeground(Color.RED);
		lbPasswordErr.setBounds(141, 296, 190, 14);
		frmLogin.getContentPane().add(lbPasswordErr);

		emailField = new JTextField();
		emailField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (emailField.getText().equals("")) {
					lbEmailErr.setText("Email field is empty!");

				} else {
					lbEmailErr.setText("");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				lbEmailErr.setText("");
			}
		});
		emailField.setBounds(176, 215, 155, 20);
		frmLogin.getContentPane().add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().equals("")) {
					lbPasswordErr.setText("Password field is empty!");

				} else {
					lbPasswordErr.setText("");
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				lbPasswordErr.setText("");
			}
		});
		passwordField.setBounds(176, 271, 155, 20);
		frmLogin.getContentPane().add(passwordField);
		JLabel lbLoginPageErr = new JLabel("");
		lbLoginPageErr.setForeground(Color.RED);
		lbLoginPageErr.setBounds(128, 309, 215, 14);
		frmLogin.getContentPane().add(lbLoginPageErr);

		JButton LoginPageBtn = new JButton("Login");
		LoginPageBtn.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();

				String passwordDecrypt = "";

				if (!(emailField.getText().isEmpty() || passwordField.getText().isEmpty())) {
					SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
							.addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class)
							.addAnnotatedClass(Review.class).addAnnotatedClass(Rooms.class)
							.addAnnotatedClass(Users.class).buildSessionFactory();
					Session session = factory.getCurrentSession();

					try {

						session.beginTransaction();

						Query usersQ = session.createQuery("from Users where email=:email");
						usersQ.setParameter("email", emailField.getText());

						// the list has just one user because the user cannot create multiple accounts
						// with same email address
						@SuppressWarnings("unchecked")
						List<Users> user =  usersQ.getResultList();

						if (user.size() == 0) {
							lbLoginPageErr.setText("The user does not exist!");
						} else {
							try {
								Encrypting encryptPass = new Encrypting();

								passwordDecrypt = encryptPass.decrypt(user.get(0).getPassword());

							} catch (Exception e1) {
								e1.printStackTrace();
							}

							if (user.get(0).getEmail().equals(emailField.getText())
									&& passwordDecrypt.equals(passwordField.getText())) {
								lbLoginPageErr.setText("");
								if (user.get(0).getAccountType().equals("user")) {

									CreateReservationPage window = new CreateReservationPage();
									window.frmCreateReservation.setVisible(true);

									window.lblUserEmail.setText(email);
									frmLogin.setVisible(false);
								} else {

									AdminPage windowAd = new AdminPage();
									windowAd.frmAdmin.setVisible(true);
									frmLogin.setVisible(false);
								}

							} else {
								lbLoginPageErr.setText("The email or password is wrong!");
							}
						}

						session.getTransaction().commit();
					} finally {

						emailField.setText("");
						passwordField.setText("");

						factory.close();

					}
				}

			}
		});
		LoginPageBtn.setBounds(136, 324, 89, 23);
		frmLogin.getContentPane().add(LoginPageBtn);

		JButton btnNewButton = new JButton("Sign up");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							RegistrationPage window = new RegistrationPage();
							window.frmSignUp.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnNewButton.setBounds(235, 324, 89, 23);
		frmLogin.getContentPane().add(btnNewButton);

		JLabel lbIcon = new JLabel("");
		lbIcon.setIcon(new ImageIcon(
				"C:\\Users\\Rodica\\git\\Hotel-management-system---Java-project\\HotelMng\\images\\images.png"));
		lbIcon.setBounds(133, 11, 230, 190);
		frmLogin.getContentPane().add(lbIcon);

	}
}
