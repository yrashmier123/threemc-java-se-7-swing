package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Booking;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class BookingsUpdate extends Dialog {
	
	// TODO
	private JLabel lblEventName;
	private JLabel lblvenue;
	private JLabel lblGuest;
	private JLabel lbldate;
	private JLabel lbltime;
	private JLabel lbltype;
	private JLabel lbldetails;
	
	private JTextField txtEventName;
	private JTextField txtVenue;
	private JTextField txtGuest;
	private JTextField txtOthers;
	
	private JTextArea txtDetails;
	
	private JComboBox<String> cboTime;
	private JComboBox<String> cboType;
	
	private JDateChooser datePicker;
	private DateFormat dateFormat;
	
	private JButton btnSave;
	private JButton btnCancel;
	
	private ControllerForBookingDetails controller;
	
	private JPanel panelCenter;
	private JPanel panelButtons;
	
	private Booking booking;
	
	
	// TODO
	public BookingsUpdate(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Booking - Update" , modal);
		set(parent);
		initUI();
		layoutComponents();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				BookingsUpdate.this.dispose();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cboType.getSelectedItem().equals("Others")) {
					if(!txtOthers.getText().isEmpty()) {
						save();
					} else {
						JOptionPane.showMessageDialog(BookingsUpdate.this, "Event Type field is Null!", "Booking - Update", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					save();
				}
			}

			private void save() {
				try {
					if(controller.connect().equals("ok")) {
						controller.addBooking(getBookingEventFor());
						controller.saveBooking();
						JOptionPane.showMessageDialog(BookingsUpdate.this, "Success!", "Booking - Update", JOptionPane.INFORMATION_MESSAGE);
						BookingsUpdate.this.dispose();
					} else {
						System.out.println(controller.connect());
					}
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(BookingsUpdate.this, "Don't leave empty fields and The only allowed special characters are as follows:\n"
							+ "\n\n ( ) - : @ & ' ! .  !",
					"Booking - Update", JOptionPane.ERROR_MESSAGE);;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(BookingsUpdate.this, "Check your guest number!", "Booking - Update", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cboType.getSelectedItem().equals("Others")) {
					txtOthers.setEnabled(true);
				} else {
					txtOthers.setEnabled(false);
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BookingsUpdate.this.dispose();
			}
		});
	}
	
	protected BookingEventForm getBookingEventFor() {

		BookingEventForm ev = null;

		String evName = txtEventName.getText();
		String evVenue = txtVenue.getText();
		String evDate = dateFormat.format(datePicker.getDate());
		String evTime = (String) cboTime.getSelectedItem();
		int evGuest = Integer.parseInt(txtGuest.getText());
		String guest = txtGuest.getText();
		String evDetails = txtDetails.getText();
		int c_id = booking.getClient_id();
		int id = booking.getId();
		String evType = "None given";
		if (cboType.getSelectedItem().equals("Others")) {
			evType = txtOthers.getText().trim();
		} else {
			evType = cboType.getSelectedItem().toString();
		}
		if (!guest.isEmpty()&& CustomPatternChecker.checkStringNumbersOnly(guest)) {
			if (!evName.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(evName)) {
				if (!evVenue.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(evVenue)) {
					if (!evDetails.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(evDetails)) {
						if (!evType.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(evType)) {
							ev = new BookingEventForm(this, id, c_id, evName,
									evVenue, evDetails, evGuest, evDate,
									evTime, evType);
							return ev;
						}
					}
				}
			}
		}
		return ev;
	}

	public void setBooking(Booking book) {
		this.booking = book;
	}
	
	public void setBookingFormInfo()  {
		txtEventName.setText(booking.getEventName());
		txtVenue.setText(booking.getEventVenue());
		txtGuest.setText(""+booking.getEventGuestNumber());
		txtDetails.setText(booking.getEventDetails());
		
		String type = booking.getEventType();
		if (type.equals(CategoryEventType.SchoolAffairs)) {
			cboType.setSelectedItem("School Affairs");
		} else if (type.equals(CategoryEventType.Anniversary.name())) {
			cboType.setSelectedItem("Anniversary");
		} else if (type.equals(CategoryEventType.Baptismal.name())) {
			cboType.setSelectedItem("Baptismal");
		} else if (type.equals(CategoryEventType.Birthday.name())) {
			cboType.setSelectedItem("Birthday");
		} else if (type.equals(CategoryEventType.Forum.name())) {
			cboType.setSelectedItem("Forum");
		} else if (type.equals(CategoryEventType.Pageant.name())) {
			cboType.setSelectedItem("Pageant");
		} else if (type.equals(CategoryEventType.Photoshoot.name())) {
			cboType.setSelectedItem("Photoshoot");
		} else if (type.equals(CategoryEventType.Reunion.name())) {
			cboType.setSelectedItem("Reunion");
		} else if (type.equals(CategoryEventType.Seminar.name())) {
			cboType.setSelectedItem("Seminar");
		} else if (type.equals(CategoryEventType.Wedding.name())) {
			cboType.setSelectedItem("Wedding");
		} else {
			cboType.setSelectedItem("Others");
			txtOthers.setText(type);
			txtOthers.setEnabled(false);
		}
		
		cboTime.setSelectedItem(booking.getEventTime());
		
		try {
			Date d = dateFormat.parse(booking.getEventDate());
			datePicker.setDate(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(2,2,2,2);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.NONE;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblEventName, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtEventName, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblvenue, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtVenue, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lbltype, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboType, gc);

		gc.gridy++;
		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtOthers, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lbldate, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(datePicker, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lbltime, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboTime, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblGuest, gc);

		gc.gridx = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtGuest, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		panelCenter.add(lbldetails, gc);

		gc.gridx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		panelCenter.add(new JScrollPane(txtDetails, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		panelButtons.add(btnSave);
		panelButtons.add(btnCancel);

		gc.gridy++;
		gc.gridx = 1;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.NONE;
		panelCenter.add(panelButtons, gc);

		add(panelCenter, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		
		controller = new ControllerForBookingDetails();
		Font f = CustomFont.setFont("Tahoma",Font.PLAIN, 15);
		
		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelCenter.setBackground(CustomColor.bgColor());

		panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButtons.setBackground(CustomColor.bgColor());
		
		lblEventName = new JLabel("Event Title: ");
		lblvenue = new JLabel("Venue: ");
		lblGuest = new JLabel("No. Of Guest: ");
		lbldate = new JLabel("Date: ");
		lbltime = new JLabel("Time: ");
		lbltype = new JLabel("Event Type: ");
		lbldetails = new JLabel("Details: ");
		
		lblEventName.setFont(f);
		lblvenue.setFont(f);
		lblGuest.setFont(f);
		lbldate.setFont(f);
		lbltime.setFont(f);
		lbltype.setFont(f);
		lbldetails.setFont(f);
		
		txtEventName = new JTextField(20);
		txtVenue = new JTextField(20);
		txtGuest = new JTextField(20);
		txtOthers = new JTextField(20);
		txtOthers.setEnabled(false);
		
		txtDetails = new JTextArea(1,1);
		txtDetails.setLineWrap(true);
		
		txtEventName.setFont(f);
		txtVenue.setFont(f);
		txtGuest.setFont(f);
		txtOthers.setFont(f);
		txtDetails.setFont(f);
		
		dateFormat = new SimpleDateFormat(" MMMMM dd , yyyy - EEEEE");

		Dimension d = getPreferredSize();
		d.width = 470;
		d.height = 420;
		
		JCalendar cal = new JCalendar();
		cal.setDecorationBordersVisible(true);
		cal.setDecorationBackgroundColor(Color.WHITE);
		cal.setTodayButtonVisible(true);
		cal.setSundayForeground(Color.RED);
		cal.setPreferredSize(d);
		
		datePicker = new JDateChooser(cal, null,null,null);
		datePicker.setDate(Calendar.getInstance().getTime());
		datePicker.setDateFormatString(" MMMMM dd , yyyy - EEEEE");
		datePicker.setFont(f);
		
		cboTime = new JComboBox<String>();
		cboType = new JComboBox<String>();
		cboTime.setFont(f);
		cboType.setFont(f);
		
		btnSave = new JButton("Save");
		btnCancel = new JButton("Cancel");
		btnSave.setFont(f);
		btnCancel.setFont(f);

		DefaultComboBoxModel<String> modTime = new DefaultComboBoxModel<String>();
		modTime.addElement("1 am");
		modTime.addElement("2 am");
		modTime.addElement("3 am");
		modTime.addElement("4 am");
		modTime.addElement("5 am");
		modTime.addElement("6 am");
		modTime.addElement("7 am");
		modTime.addElement("8 am");
		modTime.addElement("9 am");
		modTime.addElement("10 am");
		modTime.addElement("11 am");
		modTime.addElement("12 am");
		modTime.addElement("1 pm");
		modTime.addElement("2 pm");
		modTime.addElement("3 pm");
		modTime.addElement("4 pm");
		modTime.addElement("5 pm");
		modTime.addElement("6 pm");
		modTime.addElement("7 pm");
		modTime.addElement("8 pm");
		modTime.addElement("9 pm");
		modTime.addElement("10 pm");
		modTime.addElement("11 pm");
		modTime.addElement("12 pm");

		cboTime.setModel(modTime);

		DefaultComboBoxModel<String> modType = new DefaultComboBoxModel<String>();
		modType.addElement("Anniversary");
		modType.addElement("Baptismal");
		modType.addElement("Birthday");
		modType.addElement("Forum");
		modType.addElement("Pageant");
		modType.addElement("Photoshoot");
		modType.addElement("Reunion");
		modType.addElement("School Affairs");
		modType.addElement("Seminar");
		modType.addElement("Sponsorship");
		modType.addElement("Wedding");
		modType.addElement("Others");

		cboType.setModel(modType);
		cboType.setSelectedItem("Others");

		if (cboType.getSelectedItem().equals("Others")) {
			txtOthers.setVisible(true);
		} else {
			txtOthers.setVisible(false);
		}
	}

	// TODO
	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(400, 550);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}

}
