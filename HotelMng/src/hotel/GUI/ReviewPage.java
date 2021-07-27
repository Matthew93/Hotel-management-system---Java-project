package hotel.GUI;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hotel.DAO.Review;
import hotel.DAO.Rooms;

import javax.swing.JScrollPane;

public class ReviewPage {

	JFrame frmReviews;
	private JTable table;
	DefaultTableModel model = new DefaultTableModel();

	/**
	 * Create the application.
	 */
	public ReviewPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReviews = new JFrame();
		frmReviews.setTitle("Reviews");
		frmReviews.setBounds(100, 100, 450, 300);
		frmReviews.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		frmReviews.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 11, 360, 202);
		panel.add(scrollPane);

		table = new JTable();

		model = new DefaultTableModel();
		Object[] column = { "Room number", "Comment" };
		model.setColumnIdentifiers(column);
		Object[] row = new Object[2];
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Rooms.class)
				.addAnnotatedClass(Review.class).buildSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Review> revList = (List<Review>) session.createQuery("from Review").list();

			for (int j = 0; j < revList.size(); j++) {

				row[0] = revList.get(j).getRoom().getRoomNumber();

				row[1] = revList.get(j).getComment();

				model.addRow(row);
			}

			table.setModel(model);
			session.getTransaction().commit();

		} finally {

			factory.close();

		}

		table.setModel(model);

		scrollPane.setViewportView(table);
	}
}
