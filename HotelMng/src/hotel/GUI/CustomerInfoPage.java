package hotel.GUI;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hotel.DAO.Customer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomerInfoPage {

	JFrame frmCustomerDetails;
	JLabel lblGetId = new JLabel("");
	JPanel panel = new JPanel();
	JLabel lblGetFirstName = new JLabel("");
	JLabel lblGetLastName = new JLabel("");
	JLabel lblGetPhone = new JLabel("");
	JLabel lblGetEmail = new JLabel("");

	/**
	 * Launch the application.
	 */


	/**
	 * Create the application.
	 */
	public CustomerInfoPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCustomerDetails = new JFrame();
		frmCustomerDetails.setTitle("Customer details");
		frmCustomerDetails.setBounds(100, 100, 320, 300);
		frmCustomerDetails.getContentPane().setLayout(null);

		
		panel.setBounds(0, 0, 304, 261);
		frmCustomerDetails.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblId = new JLabel("Id:");
		lblId.setBounds(26, 56, 247, 14);
		panel.add(lblId);

		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(26, 81, 247, 14);
		panel.add(lblFirstName);

		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(26, 106, 247, 14);
		panel.add(lblLastName);

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(26, 128, 247, 14);
		panel.add(lblPhone);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(26, 153, 247, 14);
		panel.add(lblEmail);

		JLabel lblCust = new JLabel("Customer details");
		lblCust.setBounds(98, 11, 124, 31);
		panel.add(lblCust);
		lblCust.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGetId.setBounds(103, 56, 170, 14);
		panel.add(lblGetId);

		
		lblGetFirstName.setBounds(103, 81, 170, 14);
		panel.add(lblGetFirstName);

		
		lblGetLastName.setBounds(103, 106, 170, 14);
		panel.add(lblGetLastName);

		
		lblGetPhone.setBounds(103, 128, 170, 14);
		panel.add(lblGetPhone);

		
		lblGetEmail.setBounds(103, 153, 170, 14);
		panel.add(lblGetEmail);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmCustomerDetails.setVisible(false);
			}
		});
		btnCancel.setBounds(115, 195, 89, 23);
		panel.add(btnCancel);
		
		


	

	}
	public void getCustomerInfo() {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class)
				.buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();
			int id = Integer.parseInt(lblGetId.getText());
			Customer customer = session.get(Customer.class, id);

			lblGetFirstName.setText(customer.getFirstName());
			lblGetLastName.setText(customer.getLastName());
			lblGetPhone.setText(customer.getPhone());
			lblGetEmail.setText(customer.getEmail());

			session.getTransaction().commit();
		} finally {
			factory.close();
		}
		
	}
}
