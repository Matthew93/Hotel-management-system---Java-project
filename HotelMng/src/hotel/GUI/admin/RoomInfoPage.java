package hotel.GUI.admin;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Font;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hotel.DAO.Review;
import hotel.DAO.Rooms;
import hotel.GUI.user.ReviewPage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoomInfoPage {

	JFrame frmRoomDetails;
	JLabel lblGetId = new JLabel("");// this label is set with customerId from ReservationsPage when admin press view
	// customer details
	JLabel lblGetPrice = new JLabel("");
	JLabel lblGetRoomNumber = new JLabel("");
	JLabel lblGetAvailable = new JLabel("");
	JLabel lblGetRoomType = new JLabel("");

	/**
	 * Create the application.
	 */
	public RoomInfoPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRoomDetails = new JFrame();
		frmRoomDetails.setTitle("Room details");
		frmRoomDetails.setBounds(100, 100, 315, 282);
		frmRoomDetails.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 303, 250);
		frmRoomDetails.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Id:");
		lblNewLabel.setBounds(33, 36, 77, 14);
		panel.add(lblNewLabel);

		JLabel lblRoomNumber = new JLabel("Room number:");
		lblRoomNumber.setBounds(33, 63, 77, 14);
		panel.add(lblRoomNumber);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(33, 94, 77, 14);
		panel.add(lblPrice);

		JLabel lblAvailable = new JLabel("Available:");
		lblAvailable.setBounds(33, 119, 77, 14);
		panel.add(lblAvailable);

		JLabel lblRoomType = new JLabel("Room type:");
		lblRoomType.setBounds(33, 144, 77, 14);
		panel.add(lblRoomType);

		lblGetId.setBounds(137, 36, 119, 14);
		panel.add(lblGetId);

		lblGetRoomNumber.setBounds(137, 63, 119, 14);
		panel.add(lblGetRoomNumber);

		lblGetPrice.setBounds(137, 94, 119, 14);
		panel.add(lblGetPrice);

		lblGetAvailable.setBounds(137, 119, 119, 14);
		panel.add(lblGetAvailable);

		lblGetRoomType.setBounds(137, 144, 119, 14);
		panel.add(lblGetRoomType);

		JLabel lblRoomInfo = new JLabel("Room details");
		lblRoomInfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRoomInfo.setBounds(99, 0, 126, 25);
		panel.add(lblRoomInfo);

		// this button will hide the current frame
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmRoomDetails.setVisible(false);

			}
		});
		btnCancel.setBounds(174, 200, 89, 23);
		panel.add(btnCancel);

		// this button will show the reviews from the selected room
		JButton btnReview = new JButton("View reviews");
		btnReview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ReviewPage reviews = new ReviewPage();
				reviews.frmReviews.setVisible(true);

			}
		});
		btnReview.setBounds(34, 200, 110, 23);
		panel.add(btnReview);
	}

	// getting the informations about the room that was selected by admin
	public void getRoomInfo() {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Rooms.class)
				.addAnnotatedClass(Review.class).buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();
			int id = Integer.parseInt(lblGetId.getText());
			Rooms room = session.get(Rooms.class, id);

			lblGetRoomNumber.setText(room.getRoomNumber());
			lblGetPrice.setText(String.valueOf(room.getPrice()));

			lblGetRoomType.setText(String.valueOf(room.getRoomType()));

			session.getTransaction().commit();
		} finally {
			factory.close();
		}

	}

}
