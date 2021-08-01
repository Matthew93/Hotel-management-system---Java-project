package hotel.GUI.admin;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import hotel.GUI.user.LoginPage;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminPage {

	public JFrame frmAdmin;

	/**
	 * Create the application.
	 */
	public AdminPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdmin = new JFrame();
		frmAdmin.setTitle("Admin ");
		frmAdmin.setBounds(100, 100, 450, 300);
		frmAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdmin.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 434, 261);
		frmAdmin.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(
				"C:\\Users\\Rodica\\git\\Hotel-management-system---Java-project\\HotelMng\\images\\Admin-resized-2.jpg"));
		lblNewLabel.setBounds(51, 37, 332, 147);
		panel.add(lblNewLabel);

		JButton btnReservation = new JButton("Reservations");
		btnReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				Open the frame with all the reservations.

				ReservationsPage openReservations = new ReservationsPage();

				openReservations.frmReservations.setVisible(true);

			}
		});
		btnReservation.setBackground(new Color(192, 192, 192));
		btnReservation.setFont(new Font("Times New Roman", Font.ITALIC, 14));
		btnReservation.setBounds(61, 195, 141, 32);
		panel.add(btnReservation);

		JButton btnRooms = new JButton("Rooms");
		btnRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				Open the frame with all the rooms.

				RoomsPage viewRooms = new RoomsPage();
				viewRooms.frmAdmin.setVisible(true);
			}
		});
		btnRooms.setBackground(new Color(192, 192, 192));
		btnRooms.setFont(new Font("Times New Roman", Font.ITALIC, 14));
		btnRooms.setBounds(227, 195, 141, 32);
		panel.add(btnRooms);

		JButton btnSignUp = new JButton("Log out");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				Open the frame with the login and closing the admin one.

				frmAdmin.setVisible(false);

				LoginPage login = new LoginPage();

				login.frmLogin.setVisible(true);

			}
		});
		btnSignUp.setFont(new Font("Times New Roman", Font.ITALIC, 14));
		btnSignUp.setBackground(new Color(192, 192, 192));
		btnSignUp.setBounds(340, 11, 84, 17);
		panel.add(btnSignUp);
	}
}
