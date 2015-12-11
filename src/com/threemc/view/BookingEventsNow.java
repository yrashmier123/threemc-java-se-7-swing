package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.threemc.data.Booking;

public class BookingEventsNow extends JDialog {

	private JPanel panelCenter;

	private JLabel lblEventName;
	private JLabel lblEventDt;
	private JLabel lblEventVenue;
	private JLabel lblEventType;
	private JLabel lblEventGuest;
	private JLabel lblEventDetails;

	private Booking booking;

	public BookingEventsNow() {
		set();

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBackground(CustomColor.bgColor());

		Font fTi = CustomFont.setFont("Tahoma", Font.BOLD, 35);
		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 20);

		lblEventName = new JLabel("");
		lblEventDt = new JLabel("");
		lblEventVenue = new JLabel("");
		lblEventType = new JLabel("");
		lblEventGuest = new JLabel("");
		lblEventDetails = new JLabel("");

		lblEventName.setFont(fTi);
		lblEventDt.setFont(f);
		lblEventVenue.setFont(f);
		lblEventType.setFont(f);
		lblEventGuest.setFont(f);
		lblEventDetails.setFont(f);

		GridBagConstraints gc = new GridBagConstraints();

		Insets insets = new Insets(5, 5, 5, 5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.CENTER;
		gc.insets = insets;

		gc.gridy = 0;
		gc.gridx = 0;
		panelCenter.add(lblEventName, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(lblEventVenue, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(lblEventDt, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(lblEventType, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(lblEventGuest, gc);

		gc.gridy++;
		gc.gridx = 0;
		panelCenter.add(lblEventDetails, gc);

		add(new JScrollPane(panelCenter,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
		if (this.booking != null) {
			lblEventName.setText(booking.getEventName());
			lblEventDt.setText(booking.getEventDate() + " , " + booking.getEventTime());
			lblEventVenue.setText(booking.getEventVenue());
			lblEventType.setText(booking.getEventType());
			lblEventGuest.setText("" + booking.getEventGuestNumber());
			lblEventDetails.setText(booking.getEventDetails());
		}
	}

	private void set() {
		setSize(800, 400);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
		setTitle("EVENT'S FOR TODAY!");
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	}

}
