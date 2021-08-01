package hotel.GUI.user;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;

import java.util.List;


import javax.persistence.Query;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.toedter.calendar.JDateChooser;

import hotel.DAO.Customer;
import hotel.DAO.Reservation;
import hotel.DAO.Review;

import hotel.DAO.Rooms;

@SuppressWarnings("serial")
public class CreateReservationPage extends JFrame {
	
	JFrame frmCreateReservation;
	private JTable table;
	String email;
	DefaultTableModel model;
	DefaultTableModel modelDates;
	JLabel lblUserEmail = new JLabel("");//this label is set when the user logs in
	Date date = new Date();
	private JTable tableDates;



	/**
	 * Create the application.
	 */
	public CreateReservationPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frmCreateReservation = new JFrame();
		frmCreateReservation.setTitle("Create reservation");
		frmCreateReservation.setBounds(100, 100, 598, 441);
		frmCreateReservation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCreateReservation.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 582, 402);
		frmCreateReservation.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(259, 26, 313, 213);
		panel.add(scrollPane);

		lblUserEmail.setBounds(43, 0, 171, 14);
		panel.add(lblUserEmail);

		JLabel lblRoomNumber = new JLabel("");
		lblRoomNumber.setBounds(536, 254, 36, 19);
		panel.add(lblRoomNumber);

		JLabel lblRn = new JLabel("Room number:");
		lblRn.setBounds(10, 47, 89, 14);
		panel.add(lblRn);

		JLabel lblPr = new JLabel("Price:");
		lblPr.setBounds(10, 72, 70, 14);
		panel.add(lblPr);

		JLabel lblRT = new JLabel("Room type:");
		lblRT.setBounds(10, 97, 70, 14);
		panel.add(lblRT);

		JLabel lblGetRN = new JLabel("");
		lblGetRN.setBounds(99, 47, 100, 14);
		panel.add(lblGetRN);

		JLabel lblGetPr = new JLabel("");
		lblGetPr.setBounds(99, 72, 100, 14);
		panel.add(lblGetPr);

		JLabel lblGetRT = new JLabel("");
		lblGetRT.setBounds(99, 97, 100, 14);
		panel.add(lblGetRT);

		JLabel lblCheckIn = new JLabel("Check in:");
		lblCheckIn.setBounds(10, 122, 70, 14);
		panel.add(lblCheckIn);

		JLabel lblCheckOut = new JLabel("Check out:");
		lblCheckOut.setBounds(10, 147, 70, 14);
		panel.add(lblCheckOut);

		JDateChooser dateCheckIn = new JDateChooser();
		dateCheckIn.setBounds(99, 122, 100, 20);
		panel.add(dateCheckIn);

		JDateChooser dateCheckOut = new JDateChooser();
		dateCheckOut.setBounds(99, 147, 100, 20);
		panel.add(dateCheckOut);

		JLabel lblInvalidDate = new JLabel("");
		lblInvalidDate.setForeground(Color.RED);
		lblInvalidDate.setBounds(20, 172, 229, 14);
		panel.add(lblInvalidDate);
		JScrollPane scrollPaneDates = new JScrollPane();
		scrollPaneDates.setBounds(30, 288, 229, 103);
		panel.add(scrollPaneDates);
		
		tableDates = new JTable();
//		Creating table by making some specific rows.
		scrollPaneDates.setViewportView(tableDates);
		model = new DefaultTableModel();
		Object[] column = { "Room number", "Price", "Room type" };
		model.setColumnIdentifiers(column);
		Object[] row = new Object[3];
	
	
		modelDates = new DefaultTableModel();
		Object[] columnDates = { "CheckIn" ,"CheckOut"};
		modelDates.setColumnIdentifiers(columnDates);
		Object[] rowDates = new Object[2];

		JButton btnCreateReservation = new JButton("Create reservation");
		btnCreateReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//			clearing the table with the reservations for the selected room	
				modelDates.setRowCount(0);
				if (dateCheckIn.getDate() == null || dateCheckOut.getDate() == null) {
					lblInvalidDate.setForeground(Color.RED);
					lblInvalidDate.setText("No date selected!");
				} else {

					long diffDays = ChronoUnit.DAYS.between(dateCheckIn.getDate().toInstant(),
							dateCheckOut.getDate().toInstant());

					int selectedRowIndex = table.getSelectedRow();

					Period pastDate = Period.between(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
							dateCheckIn.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

					if (diffDays <= 0 || selectedRowIndex == -1 || pastDate.getDays() < 0) {

						lblInvalidDate.setForeground(Color.RED);
						lblInvalidDate.setText("Invalid dates or no room selected!");

					} else {
						SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
								.addAnnotatedClass(Customer.class).addAnnotatedClass(Rooms.class)
								.addAnnotatedClass(Reservation.class).addAnnotatedClass(Review.class)
								.buildSessionFactory();

						Session session = factory.getCurrentSession();

						try {
							session.beginTransaction();
							Query customerQ = session.createQuery("from Customer where email=:custEmail");
							customerQ.setParameter("custEmail", lblUserEmail.getText());

							Customer customer = (Customer) customerQ.getResultList().get(0);

							Query roomQ = session.createQuery("from Rooms where roomNumber=:roomNr");
							roomQ.setParameter("roomNr", lblGetRN.getText());
							Rooms room = (Rooms) roomQ.getResultList().get(0);

							Query resQ = session.createQuery("from Reservation where room_id=:roomId");
							resQ.setParameter("roomId", room.getId());
							@SuppressWarnings("unchecked")
							List<Reservation> resList = resQ.getResultList();
							int boolRes = 0;
							for(int i = 0; i<resList.size();i++) {
								
								if((dateCheckIn.getDate().compareTo(resList.get(i).getCheckOut()) <0) && (dateCheckOut.getDate().compareTo(resList.get(i).getCheckIn()) > 0)) {
									JOptionPane.showMessageDialog(btnCreateReservation, "The room is reserved at this date");
									boolRes = 1;
									rowDates[0] = resList.get(i).getCheckIn();
									rowDates[1] = resList.get(i).getCheckOut();
									modelDates.addRow(rowDates);
									break;
								}
								else {
									boolRes= 0;
									
								}
								}
							tableDates.setModel(modelDates);
							if(boolRes == 0) {
								

							double totalPrice = room.getPrice() * diffDays;

							java.sql.Date sqldateCheckIn = new java.sql.Date(dateCheckIn.getDate().getTime());
							java.sql.Date sqldateCheckOut = new java.sql.Date(dateCheckOut.getDate().getTime());

							Reservation reserve = new Reservation(customer, room, sqldateCheckIn, sqldateCheckOut,
									totalPrice);

							int answer = JOptionPane.showConfirmDialog(btnCreateReservation,
									"Total price is: " + totalPrice + ". Are you agree?", "Warning",
									JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (answer == JOptionPane.YES_OPTION) {

								session.save(reserve);
								lblInvalidDate.setForeground(Color.GREEN);
								lblInvalidDate.setText("Reservation succesfully created!");

								@SuppressWarnings("unchecked")
								List<Rooms> roomsList = (List<Rooms>) session
										.createQuery("from Rooms").list();
								model = new DefaultTableModel();

								model.setColumnIdentifiers(column);

								for (int i = 0; i < roomsList.size(); i++) {
									row[0] = roomsList.get(i).getRoomNumber();
									row[1] = roomsList.get(i).getPrice();
									row[2] = roomsList.get(i).getRoomType();
									model.addRow(row);
								}

								table.setModel(model);

								
								session.getTransaction().commit();
							}}

						} finally {
							factory.close();
						}
					}
				}

			}
		});
		btnCreateReservation.setBounds(43, 197, 182, 23);
		panel.add(btnCreateReservation);
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex > -1) {
					lblGetRN.setText(model.getValueAt(selectedRowIndex, 0).toString());
					lblGetPr.setText(model.getValueAt(selectedRowIndex, 1).toString());
					lblGetRT.setText(model.getValueAt(selectedRowIndex, 2).toString());
					lblRoomNumber.setText(model.getValueAt(selectedRowIndex, 0).toString());
					lblInvalidDate.setText("");
					
				}
			}
		});

		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Rooms.class)
				.addAnnotatedClass(Review.class).addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Rooms> roomsList = (List<Rooms>) session.createQuery("from Rooms").list();

			for (int i = 0; i < roomsList.size(); i++) {

				row[0] = roomsList.get(i).getRoomNumber();
				row[1] = roomsList.get(i).getPrice();
				row[2] = roomsList.get(i).getRoomType();

				model.addRow(row);

			}

			table.setModel(model);
			session.getTransaction().commit();

		} finally {

			factory.close();

		}

		table.setModel(model);

		scrollPane.setViewportView(table);

		JButton btnAddReview = new JButton("Write a review for room:");
		btnAddReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddReviewPage addReview = new AddReviewPage();

				if (lblGetRN.getText().isEmpty()) {

					JOptionPane.showMessageDialog(btnAddReview, "Please select a room!");
				} else {

					addReview.frmAddReview.setVisible(true);
					addReview.lblRoomNumber.setText(lblGetRN.getText());

				}

			}
		});
		btnAddReview.setBounds(310, 250, 222, 23);
		panel.add(btnAddReview);

		JButton btnReviews = new JButton("View reviews for all rooms");

		btnReviews.setBounds(310, 284, 222, 23);
		panel.add(btnReviews);
		btnReviews.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReviewPage review = new ReviewPage();
				review.frmReviews.setVisible(true);

			}
		});

		JLabel lblNewLabel_1 = new JLabel("Hello,");
		lblNewLabel_1.setBounds(10, 0, 46, 14);
		panel.add(lblNewLabel_1);

		JButton btnLogout = new JButton("Log out");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmCreateReservation.setVisible(false);
				LoginPage login = new LoginPage();
				login.frmLogin.setVisible(true);
			}
		});
		btnLogout.setBounds(385, 0, 100, 23);
		panel.add(btnLogout);
		

		
		JLabel lblNewLabel = new JLabel("The following periods are taken:");
		lblNewLabel.setBounds(20, 267, 194, 14);
		panel.add(lblNewLabel);
		
		JButton btnViewYourReservation = new JButton("View your reservations");
		btnViewYourReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
						.addAnnotatedClass(Customer.class).addAnnotatedClass(Rooms.class)
						.addAnnotatedClass(Reservation.class).addAnnotatedClass(Review.class)
						.buildSessionFactory();

				Session session = factory.getCurrentSession();

				try {
					session.beginTransaction();
					Query customerQ = session.createQuery("from Customer where email=:custEmail");
					customerQ.setParameter("custEmail", lblUserEmail.getText());

					Customer customer = (Customer) customerQ.getResultList().get(0);

						CustomerReservationsPage customerReservations  = new CustomerReservationsPage();
						
						customerReservations.setCustomerId(customer.getId());
						customerReservations.getReservationInfo();
						customerReservations.frmReservations.setVisible(true);
						
						session.getTransaction().commit();
				

				} finally {
					factory.close();
				}
				
			}
		});
		btnViewYourReservation.setBounds(43, 231, 182, 23);
		panel.add(btnViewYourReservation);
		


	}
}
