package hotel.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import java.awt.Component;
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
	private JTextField availableTxt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoomsPage window = new RoomsPage();
					window.frmAdmin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		lblRoomType.setBounds(22, 275, 74, 14);
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
		roomTypeCmb.setBounds(112, 271, 100, 22);
		panel.add(roomTypeCmb);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex > -1) {
					idTxt.setText(model.getValueAt(selectedRowIndex, 0).toString());
					roomNumberTxt.setText(model.getValueAt(selectedRowIndex, 1).toString());
					priceTxt.setText(model.getValueAt(selectedRowIndex, 2).toString());
					availableTxt.setText(model.getValueAt(selectedRowIndex, 3).toString());
					roomTypeCmb.setSelectedItem(model.getValueAt(selectedRowIndex, 4));
				}
			}
		});
		scrollPane.setViewportView(table);

		model = new DefaultTableModel();
		Object[] column = { "Id", "Room number", "Price", "Available", "Room type" };
		model.setColumnIdentifiers(column);
		Object[] row = new Object[5];
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
				row[3] = roomsList.get(i).getAvailable();
				row[4] = roomsList.get(i).getRoomType();
				model.addRow(row);

			}

			table.setModel(model);
			session.getTransaction().commit();
		} finally {

			factory.close();

		}

		table.setModel(model);

		availableTxt = new JTextField();
		Boolean correctAvailableInput = Validations.checkAvailable(availableTxt.getText());
		availableTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (Validations.checkAvailable(availableTxt.getText()) == false) {
					lblAvailableErr.setText("Input should be true or false");
				} else {
					lblAvailableErr.setText("");

				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				lblAvailableErr.setText("");
			}
		});
		availableTxt.setColumns(10);
		availableTxt.setBounds(113, 211, 99, 20);
		panel.add(availableTxt);

		roomNumberTxt = new JTextField();
		Boolean correctRoomNumberInput = Validations.checkRoomNumber(roomNumberTxt.getText());
		roomNumberTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblRoomNumberErr.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (Validations.checkRoomNumber(roomNumberTxt.getText()) == false) {
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
		Boolean correctPriceInput = Validations.checkPrice(priceTxt.getText());
		priceTxt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (Validations.checkPrice(priceTxt.getText()) == false) {
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
				if ((roomNumberTxt.getText().equals("") || priceTxt.getText().equals("")
						|| availableTxt.getText().equals(""))
						|| ((Validations.checkRoomNumber(roomNumberTxt.getText()) == false)
								|| Validations.checkPrice(priceTxt.getText()) == false
								|| Validations.checkAvailable(availableTxt.getText()) == false)) {
					JOptionPane.showMessageDialog(btnAdd, "All fields should be completed with valid inputs!");
				} else {

					SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
							.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class).buildSessionFactory();
					Session session = factory.getCurrentSession();

					try {
						session.beginTransaction();

						Rooms room = new Rooms(roomNumberTxt.getText(), Double.parseDouble(priceTxt.getText()),
								Boolean.parseBoolean(availableTxt.getText()), (RoomType) roomTypeCmb.getSelectedItem());

						session.save(room);

						session.getTransaction().commit();
					} finally {

						factory.close();

					}
				}
			}
		});
		btnAdd.setBounds(35, 321, 89, 23);
		panel.add(btnAdd);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = table.getSelectedRow();
				if ((roomNumberTxt.getText().equals("") || priceTxt.getText().equals("")
						|| availableTxt.getText().equals(""))
						|| ((Validations.checkRoomNumber(roomNumberTxt.getText()) == false)
								|| Validations.checkPrice(priceTxt.getText()) == false
								|| Validations.checkAvailable(availableTxt.getText()) == false)) {
					JOptionPane.showMessageDialog(btnAdd, "All fields should be completed with valid inputs!");
				} else {
					if (selectedRowIndex > -1) {

						int roomId = (int) (model.getValueAt(table.getSelectedRow(), 0));

						int answer = JOptionPane.showConfirmDialog(btnEdit, "Are you sure you want to edit this row?",
								"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (answer == JOptionPane.YES_OPTION) {

							SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
									.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class)
									.buildSessionFactory();
							Session session = factory.getCurrentSession();

							try {
								session.beginTransaction();

								Rooms room = session.get(Rooms.class, roomId);
								room.setRoomNumber(roomNumberTxt.getText());
								room.setPrice(Double.parseDouble(priceTxt.getText()));
								room.setAvailable(Boolean.parseBoolean(availableTxt.getText()));
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
		btnEdit.setBounds(148, 321, 89, 23);
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
								.addAnnotatedClass(Rooms.class).addAnnotatedClass(Review.class).buildSessionFactory();
						Session session = factory.getCurrentSession();

						try {
							session.beginTransaction();

							Rooms room = session.get(Rooms.class, roomId);
							session.delete(room);

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
		btnDelete.setBounds(35, 370, 89, 20);
		panel.add(btnDelete);

		JLabel lblAvailable = new JLabel("Available:");
		lblAvailable.setBounds(29, 214, 67, 14);
		panel.add(lblAvailable);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idTxt.setText("");
				roomNumberTxt.setText("");
				priceTxt.setText("");
				availableTxt.setText("");
			}
		});
		btnClear.setBounds(148, 369, 89, 20);
		panel.add(btnClear);

		JButton btnRefresh = new JButton("");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = new DefaultTableModel();
				Object[] column = { "Id", "Room number", "Price", "Available", "Room type" };
				model.setColumnIdentifiers(column);
				Object[] row = new Object[5];
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
						row[3] = roomsList.get(i).getAvailable();
						row[4] = roomsList.get(i).getRoomType();
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
				"C:\\Users\\Rodica\\eclipse-workspace\\HotelMng\\images\\depositphotos_3881741-stock-illustration-3d-refresh-icon.jpg"));
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

	@SuppressWarnings("unused")
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
