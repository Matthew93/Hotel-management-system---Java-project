package hotel.GUI;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.toedter.calendar.JDateChooser;

import hotel.DAO.Customer;
import hotel.DAO.Reservation;
import hotel.DAO.Review;
import hotel.DAO.RoomType;
import hotel.DAO.Rooms;
import hotel.utils.Validations;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReservationsPage {

	JFrame frmReservations;
	private JLabel lblGetID;
	private JTextField totalPriceTxt;
	private JTable table;
	DefaultTableModel model;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservationsPage window = new ReservationsPage();
					window.frmReservations.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ReservationsPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReservations = new JFrame();
		frmReservations.setTitle("Reservations");
		frmReservations.setBounds(100, 100, 667, 405);
		frmReservations.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 651, 373);
		frmReservations.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(58, 49, 24, 14);
		panel.add(lblId);

		lblGetID = new JLabel("");
		lblGetID.setBounds(117, 49, 82, 14);
		panel.add(lblGetID);

		JLabel lblCustomerId = new JLabel("Customer id:");
		lblCustomerId.setBounds(24, 74, 80, 14);
		panel.add(lblCustomerId);

		JLabel lblGetCustomerId = new JLabel("");
		lblGetCustomerId.setBounds(117, 74, 92, 14);
		panel.add(lblGetCustomerId);

		JLabel lblRoomId = new JLabel("Room id:");
		lblRoomId.setBounds(47, 106, 66, 14);
		panel.add(lblRoomId);

		JLabel lblGetRoomId = new JLabel("");
		lblGetRoomId.setBounds(117, 106, 102, 14);
		panel.add(lblGetRoomId);

		JLabel lblId_2_1 = new JLabel("Check in:");
		lblId_2_1.setBounds(47, 138, 66, 14);
		panel.add(lblId_2_1);

		JLabel lblId_2_2 = new JLabel("Check out:");
		lblId_2_2.setBounds(34, 171, 66, 14);
		panel.add(lblId_2_2);

		JLabel lblId_2_2_1 = new JLabel("Total price:");
		lblId_2_2_1.setBounds(34, 206, 59, 14);
		panel.add(lblId_2_2_1);

		JDateChooser checkInDate = new JDateChooser();
		checkInDate.setBounds(124, 138, 75, 20);
		panel.add(checkInDate);

		JDateChooser checkOutDate = new JDateChooser();
		checkOutDate.setBounds(124, 171, 75, 20);
		panel.add(checkOutDate);

		totalPriceTxt = new JTextField();
		totalPriceTxt.setBounds(103, 203, 96, 20);
		panel.add(totalPriceTxt);
		totalPriceTxt.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(226, 49, 397, 213);
		panel.add(scrollPane);

		table = new JTable();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex > -1) {
					lblGetID.setText(model.getValueAt(selectedRowIndex, 0).toString());
					lblGetCustomerId.setText(model.getValueAt(selectedRowIndex, 1).toString());
					lblGetRoomId.setText(model.getValueAt(selectedRowIndex, 2).toString());
					checkInDate.setDate((Date) model.getValueAt(selectedRowIndex, 3));
					checkOutDate.setDate((Date) model.getValueAt(selectedRowIndex, 4));
					totalPriceTxt.setText(model.getValueAt(selectedRowIndex, 5).toString());
				}
			}
		});

		JButton btnRefresh = new JButton("");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				model = new DefaultTableModel();
				Object[] column = { "Id", "Customer Id", "Room Id", "Check in", "Check out", "Total price" };
				model.setColumnIdentifiers(column);
				Object[] row = new Object[6];
				SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
						.addAnnotatedClass(Rooms.class).addAnnotatedClass(Customer.class)
						.addAnnotatedClass(Reservation.class).addAnnotatedClass(Review.class).buildSessionFactory();
				Session session = factory.getCurrentSession();

				try {
					session.beginTransaction();

					@SuppressWarnings("unchecked")
					List<Reservation> reserve = (List<Reservation>) session.createQuery("from Reservation").list();
					for (int i = 0; i < reserve.size(); i++) {
						row[0] = reserve.get(i).getId();
						row[1] = reserve.get(i).getCustomer().getId();
						row[2] = reserve.get(i).getRoom().getId();
						row[3] = reserve.get(i).getCheckIn();
						row[4] = reserve.get(i).getCheckOut();
						row[5] = reserve.get(i).getTotalPrice();
						model.addRow(row);

					}

					table.setModel(model);
					session.getTransaction().commit();
				} finally {

					factory.close();

				}

			}
		});
		btnRefresh.setIcon(new ImageIcon(
				"C:\\Users\\Rodica\\eclipse-workspace\\HotelMng\\images\\depositphotos_3881741-stock-illustration-3d-refresh-icon.jpg"));
		btnRefresh.setBounds(593, 267, 30, 29);
		panel.add(btnRefresh);

		model = new DefaultTableModel();
		Object[] column = { "Id", "Customer Id", "Room Id", "Check in", "Check out", "Total price" };
		model.setColumnIdentifiers(column);
		Object[] row = new Object[6];
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Rooms.class)
				.addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class).addAnnotatedClass(Review.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Reservation> reserve = (List<Reservation>) session.createQuery("from Reservation").list();
			for (int i = 0; i < reserve.size(); i++) {
				row[0] = reserve.get(i).getId();
				row[1] = reserve.get(i).getCustomer().getId();
				row[2] = reserve.get(i).getRoom().getId();
				row[3] = reserve.get(i).getCheckIn();
				row[4] = reserve.get(i).getCheckOut();
				row[5] = reserve.get(i).getTotalPrice();
				model.addRow(row);

			}

			table.setModel(model);
			session.getTransaction().commit();
		} finally {

			factory.close();

		}

		// .setModel(model);

		scrollPane.setViewportView(table);

		JButton btnEditReservation = new JButton("Edit");
		btnEditReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if (selectedRowIndex > -1) {

					int reservationId = (int) (model.getValueAt(table.getSelectedRow(), 0));

					int answer = JOptionPane.showConfirmDialog(btnEditReservation,
							"Are you sure you want to edit this row?", "Warning", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (answer == JOptionPane.YES_OPTION) {

						SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
								.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class)
								.addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class)
								.buildSessionFactory();
						Session session = factory.getCurrentSession();

						try {
							session.beginTransaction();

							Reservation reserve = session.get(Reservation.class, reservationId);

							reserve.setCheckIn(checkInDate.getDate());
							reserve.setCheckOut(checkOutDate.getDate());
							reserve.setTotalPrice(Double.parseDouble(totalPriceTxt.getText()));

							session.getTransaction().commit();
						} finally {

							factory.close();

						}
					}
				} else {
					JOptionPane.showMessageDialog(btnEditReservation, "Please select a room!");
				}

			}

		});
		btnEditReservation.setBounds(74, 252, 89, 23);
		panel.add(btnEditReservation);

		JButton btnDeleteReservation = new JButton("Delete");
		btnDeleteReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if (selectedRowIndex > -1) {

					int reservationId = (int) (model.getValueAt(table.getSelectedRow(), 0));

					int answer = JOptionPane.showConfirmDialog(btnEditReservation,
							"Are you sure you want to delete this row?", "Warning", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (answer == JOptionPane.YES_OPTION) {

						SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
								.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class)
								.addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class)
								.buildSessionFactory();
						Session session = factory.getCurrentSession();

						try {
							session.beginTransaction();

							Reservation reserve = session.get(Reservation.class, reservationId);

							session.delete(reserve);

							reserve.getRoom().setAvailable(true);

							session.getTransaction().commit();
						} finally {

							factory.close();

						}
					}
				} else {
					JOptionPane.showMessageDialog(btnEditReservation, "Please select a room!");
				}

			}
		});
		btnDeleteReservation.setBounds(74, 286, 89, 23);
		panel.add(btnDeleteReservation);

		JButton btnViewCustomerDetail = new JButton("View customer details");
		btnViewCustomerDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if (selectedRowIndex > -1) {

					int customerId = (int) (model.getValueAt(table.getSelectedRow(), 1));

					CustomerInfoPage customerInfo = new CustomerInfoPage();
					customerInfo.lblGetId.setText(String.valueOf(customerId));
					customerInfo.getCustomerInfo();
					customerInfo.frmCustomerDetails.setVisible(true);

				} else {

					JOptionPane.showMessageDialog(btnEditReservation, "Please select a reservation!");

				}

			}
		});
		btnViewCustomerDetail.setBounds(348, 273, 188, 23);
		panel.add(btnViewCustomerDetail);

		JButton btnViewRoomDetails = new JButton("View room details");
		btnViewRoomDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if (selectedRowIndex > -1) {

					int roomId = (int) (model.getValueAt(table.getSelectedRow(), 2));

					RoomInfoPage roomInfo = new RoomInfoPage();
					roomInfo.lblGetId.setText(String.valueOf(roomId));
					roomInfo.getRoomInfo();

					roomInfo.frmRoomDetails.setVisible(true);

				} else {

					JOptionPane.showMessageDialog(btnEditReservation, "Please select a reservation!");

				}

			}
		});
		btnViewRoomDetails.setBounds(348, 307, 188, 23);
		panel.add(btnViewRoomDetails);

	}
}
