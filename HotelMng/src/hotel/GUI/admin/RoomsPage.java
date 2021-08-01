package hotel.GUI.admin;

import javax.swing.JFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.persistence.Query;
import javax.swing.ImageIcon;
import java.awt.Font;
import hotel.DAO.*;
import hotel.utils.*;

@SuppressWarnings("serial")
public class RoomsPage extends JFrame {

	JFrame frmAdmin;
	DefaultTableModel model;
	private JTextField idTxt;
	private JTextField roomNumberTxt;
	private JTextField priceTxt;
	private JTable table;
	Validations validate = new Validations();

	/**
	 * Create the application.
	 */

	public RoomsPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frmAdmin = new JFrame();
		frmAdmin.setTitle("Rooms");
		frmAdmin.setBounds(100, 100, 670, 500);
		frmAdmin.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 248, 255));
		panel.setBounds(0, 0, 654, 436);
		frmAdmin.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Id:");
		lblNewLabel.setBounds(50, 81, 46, 14);
		panel.add(lblNewLabel);

		JLabel lblRoomNumber = new JLabel("Room number:");
		lblRoomNumber.setBounds(10, 109, 96, 14);
		panel.add(lblRoomNumber);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(50, 155, 46, 14);
		panel.add(lblPrice);

		JLabel lblRoomType = new JLabel("Room type:");
		lblRoomType.setBounds(20, 200, 74, 14);
		panel.add(lblRoomType);

		idTxt = new JTextField();
		idTxt.setEditable(false);
		idTxt.setBounds(116, 78, 96, 20);
		panel.add(idTxt);
		idTxt.setColumns(10);

		JLabel lblRoomNumberErr = new JLabel("");
		lblRoomNumberErr.setForeground(Color.RED);
		lblRoomNumberErr.setBounds(20, 127, 192, 14);
		panel.add(lblRoomNumberErr);

		JLabel lblPriceErr = new JLabel("");
		lblPriceErr.setForeground(Color.RED);
		lblPriceErr.setBounds(22, 171, 193, 14);
		panel.add(lblPriceErr);

		JLabel lblAvailableErr = new JLabel("");
		lblAvailableErr.setForeground(Color.RED);
		lblAvailableErr.setBounds(22, 231, 190, 14);
		panel.add(lblAvailableErr);

		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(247, 78, 372, 317);
		panel.add(scrollPane);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		JComboBox roomTypeCmb = new JComboBox(RoomType.values());
		roomTypeCmb.setBounds(110, 196, 100, 22);
		panel.add(roomTypeCmb);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// get the selected row and fill the corresponding labels with the informations
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex > -1) {
					idTxt.setText(model.getValueAt(selectedRowIndex, 0).toString());
					roomNumberTxt.setText(model.getValueAt(selectedRowIndex, 1).toString());
					priceTxt.setText(model.getValueAt(selectedRowIndex, 2).toString());
					roomTypeCmb.setSelectedItem(model.getValueAt(selectedRowIndex, 3));
				}
			}
		});
		scrollPane.setViewportView(table);

		// fill the table with rooms from the database
		model = new DefaultTableModel();
		Object[] column = { "Id", "Room number", "Price", "Room type" };
		model.setColumnIdentifiers(column);
		Object[] row = new Object[4];
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Rooms.class)
				.addAnnotatedClass(Review.class).buildSessionFactory();
		Session session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Rooms> roomsList = (List<Rooms>) session.createQuery("from Rooms").list();

			for (int i = 0; i < roomsList.size(); i++) {
				row[0] = roomsList.get(i).getId();
				row[1] = roomsList.get(i).getRoomNumber();
				row[2] = roomsList.get(i).getPrice();

				row[3] = roomsList.get(i).getRoomType();
				model.addRow(row);

			}

			table.setModel(model);
			session.getTransaction().commit();
		} finally {

			factory.close();

		}

		table.setModel(model);

		roomNumberTxt = new JTextField();
		// after the field is filled and focus is lost the input is checked to be valid
		roomNumberTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblRoomNumberErr.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (validate.checkRoomNumber(roomNumberTxt.getText()) == false) {
					lblRoomNumberErr.setText("Input should be like A01");
				} else {
					lblRoomNumberErr.setText("");
				}
			}
		});
		roomNumberTxt.setColumns(10);
		roomNumberTxt.setBounds(113, 106, 99, 20);
		panel.add(roomNumberTxt);

		priceTxt = new JTextField();
		priceTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (validate.checkPrice(priceTxt.getText()) == false) {
					lblPriceErr.setText("Input should be a number");
				} else {
					lblPriceErr.setText("");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				lblPriceErr.setText("");
			}
		});
		priceTxt.setColumns(10);
		priceTxt.setBounds(113, 152, 99, 20);
		panel.add(priceTxt);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if ((roomNumberTxt.getText().isEmpty() || priceTxt.getText().isEmpty())
						|| ((validate.checkRoomNumber(roomNumberTxt.getText()) == false)
								|| validate.checkPrice(priceTxt.getText()) == false)) {

					JOptionPane.showMessageDialog(btnAdd, "All fields should be completed with valid inputs!");
				} else {

					SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
							.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class).buildSessionFactory();
					Session session = factory.getCurrentSession();

					try {
						session.beginTransaction();

						// adding a room by getting the infos from the labels and saving them to
						// database
						Rooms room = new Rooms(roomNumberTxt.getText(), Double.parseDouble(priceTxt.getText()),
								(RoomType) roomTypeCmb.getSelectedItem());

						session.save(room);

						session.getTransaction().commit();
					} finally {

						factory.close();

					}
				}
			}
		});
		btnAdd.setBounds(20, 270, 89, 23);
		panel.add(btnAdd);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();

				if ((roomNumberTxt.getText().isEmpty() || priceTxt.getText().isEmpty())
						|| ((validate.checkRoomNumber(roomNumberTxt.getText()) == false)
								|| validate.checkPrice(priceTxt.getText()) == false)) {

					JOptionPane.showMessageDialog(btnAdd, "All fields should be completed with valid inputs!");
				} else {
					if (selectedRowIndex > -1) {

						int roomId = (int) (model.getValueAt(table.getSelectedRow(), 0));

						int answer = JOptionPane.showConfirmDialog(btnEdit, "Are you sure you want to edit this row?",
								"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (answer == JOptionPane.YES_OPTION) {

							SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
									.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class)
									.addAnnotatedClass(Customer.class).addAnnotatedClass(Reservation.class)
									.buildSessionFactory();
							Session session = factory.getCurrentSession();

							try {
								session.beginTransaction();

								// get the room from database with the selected roomId
								Rooms room = session.get(Rooms.class, roomId);

								// set all the fields with the user input in the database
								room.setRoomNumber(roomNumberTxt.getText());

								room.setPrice(Double.parseDouble(priceTxt.getText()));

								room.setRoomType((RoomType) roomTypeCmb.getSelectedItem());

								session.getTransaction().commit();
							} finally {

								factory.close();

							}
						}
					} else {
						JOptionPane.showMessageDialog(btnEdit, "Please select a room!");
					}

				}
			}
		});
		btnEdit.setBounds(133, 270, 89, 23);
		panel.add(btnEdit);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex > -1) {

					int roomId = (int) (model.getValueAt(table.getSelectedRow(), 0));

					int answer = JOptionPane.showConfirmDialog(btnDelete, "Are you sure you want to delete this row?",
							"Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (answer == JOptionPane.YES_OPTION) {

						SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
								.addAnnotatedClass(Rooms.class).addAnnotatedClass(Customer.class)
								.addAnnotatedClass(Reservation.class).addAnnotatedClass(Review.class)
								.buildSessionFactory();
						Session session = factory.getCurrentSession();

						try {
							session.beginTransaction();

							Rooms room = session.get(Rooms.class, roomId);

							Query reservationsQ = session.createQuery("from Reservation where room_id=:roomID");
							reservationsQ.setParameter("roomID", roomId);
							@SuppressWarnings("unchecked")
							List<Reservation> reservations = reservationsQ.getResultList();
							if (reservations.size() > 0) {

								JOptionPane.showMessageDialog(btnDelete,
										"This room is booked and cannot be deleted! Please delete the reservation first!");

							} else {

								session.delete(room);

							}

							session.getTransaction().commit();
						} finally {

							factory.close();

						}
					}
				} else {
					JOptionPane.showMessageDialog(btnDelete, "Please select a room!");
				}

			}
		});
		btnDelete.setBounds(20, 319, 89, 20);
		panel.add(btnDelete);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				idTxt.setText("");
				roomNumberTxt.setText("");
				priceTxt.setText("");

			}
		});
		btnClear.setBounds(133, 318, 89, 20);
		panel.add(btnClear);

		JButton btnRefresh = new JButton("");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = new DefaultTableModel();
				Object[] column = { "Id", "Room number", "Price", "Room type" };
				model.setColumnIdentifiers(column);
				Object[] row = new Object[4];
				SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
						.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class).buildSessionFactory();
				Session session = factory.getCurrentSession();

				try {
					session.beginTransaction();

					@SuppressWarnings("unchecked")
					List<Rooms> roomsList = (List<Rooms>) session.createQuery("from Rooms").list();

					for (int i = 0; i < roomsList.size(); i++) {
						row[0] = roomsList.get(i).getId();
						row[1] = roomsList.get(i).getRoomNumber();
						row[2] = roomsList.get(i).getPrice();

						row[3] = roomsList.get(i).getRoomType();
						model.addRow(row);

					}

					table.setModel(model);
					session.getTransaction().commit();
				} finally {

					factory.close();

				}

				table.setModel(model);
			}
		});
		btnRefresh.setIcon(new ImageIcon(
				"C:\\Users\\Rodica\\git\\Hotel-management-system---Java-project\\HotelMng\\images\\depositphotos_3881741-stock-illustration-3d-refresh-icon.jpg"));
		btnRefresh.setBounds(583, 402, 22, 23);
		panel.add(btnRefresh);

		JLabel lblNewLabel_1 = new JLabel("Rooms");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblNewLabel_1.setBounds(274, 11, 153, 56);
		panel.add(lblNewLabel_1);

		JPanel panel_reservation = new JPanel();
		panel_reservation.setBackground(new Color(144, 238, 144));
		panel_reservation.setBounds(0, 0, 629, 410);
		frmAdmin.getContentPane().add(panel_reservation);

	}

}
