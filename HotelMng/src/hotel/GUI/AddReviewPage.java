package hotel.GUI;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hotel.DAO.Review;
import hotel.DAO.Rooms;

import javax.persistence.Query;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class AddReviewPage {

	JFrame frmAddReview;
	JLabel lblRoomNumber = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddReviewPage window = new AddReviewPage();
					window.frmAddReview.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AddReviewPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddReview = new JFrame();
		frmAddReview.setTitle("Add review");
		frmAddReview.setBounds(100, 100, 340, 300);
		frmAddReview.getContentPane().setLayout(null);

		JLabel lblRoom = new JLabel("Room:");
		lblRoom.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblRoom.setBounds(32, 11, 94, 41);
		frmAddReview.getContentPane().add(lblRoom);
		lblRoomNumber.setFont(new Font("Tahoma", Font.PLAIN, 25));

		lblRoomNumber.setBounds(110, 17, 46, 35);
		frmAddReview.getContentPane().add(lblRoomNumber);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(60, 76, 239, 98);
		frmAddReview.getContentPane().add(textArea);

		JButton btnAddR = new JButton("Add review");
		btnAddR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
						.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class).buildSessionFactory();

				Session session = factory.getCurrentSession();

				try {
					session.beginTransaction();
					Query query = session.createQuery("from Rooms where roomNumber=:roomNr");
					query.setParameter("roomNr", lblRoomNumber.getText());

					List<Rooms> list = query.getResultList();
					Rooms r = list.get(0);
					int id = r.getId();
					
					Rooms room = session.get(Rooms.class, id);
					Review review = new Review(textArea.getText());
					room.addReview(review);

					session.getTransaction().commit();

				} finally {
					factory.close();
					textArea.setText("");
				}

			}
		});
		btnAddR.setBounds(97, 199, 138, 23);
		frmAddReview.getContentPane().add(btnAddR);
	}
}
