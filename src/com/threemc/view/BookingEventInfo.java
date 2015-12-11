package com.threemc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Booking;
import com.threemc.events.BookingEventListener;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class BookingEventInfo extends JPanel {

	private BookingEventListener listener;

	private JLabel lblEventName;
	private JLabel lblEventType;
	private JLabel lblEventDate;
	private JLabel lblVenue;
	private JLabel lblGuests;
	private JLabel lblTime;
	private JLabel lblDesc;
	private JLabel lblClient;
	private JLabel lblClientInfo;
	private JLabel lblMessage;

	private JTextField txtEventName;
	private JTextField txtGuests;
	private JTextField txtVenue;
	private JTextField txtOthers;

	private JTextArea txtArea;
	
	private BookingEventInfoTableModel eventModel;
	private CustomTableEvent table;

	private JComboBox<String> cboTime;
	private JComboBox<String> cboap;
	private JComboBox<String> cboType;

	private JDateChooser picker;
	private Color col = Color.decode("#fd9c9c");
	private Color colo = Color.decode("#ffd700");

	private JPanel panelMessenger;
	private JPanel panelButton;
	private JButton btnNext;
	private JButton btnBackClient;
	private JButton btnSave;
	private JButton btnClear;
	private JButton btnNew;
	private JButton btnShow;

	private int id = 0;
	private int client_id = 0;
	private int checkSave = 0;
	private SimpleDateFormat dateFormat;
	private DateFormat format;
	private ControllerForBookingDetails controller = new ControllerForBookingDetails();
	private ArrayList<Booking> bookingList;
	private ArrayList<Booking> bookingListDate;

	// TODO
	public BookingEventInfo() {
		set();
		initUI();
		layoutComponents();

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (table.getSelectedRow() != -1) {
						if(checkSave == 1) {
							if (listener != null) {
								listener.saveEventActionOccured(getEventInformation());
							}
						} else {
							int yesno = JOptionPane
									.showConfirmDialog(
											BookingEventInfo.this,
											"Event has not yet been saved.\nDo you want to proceed?",
											"Booking", JOptionPane.YES_NO_OPTION,
											JOptionPane.INFORMATION_MESSAGE);
							if(yesno == JOptionPane.YES_OPTION) {
								if (listener != null) {
									listener.saveEventActionOccured(getEventInformation());
								}
							}
						}
					} else {
						JOptionPane
								.showMessageDialog(
										BookingEventInfo.this,
										"Please select an Entry in the Booking list before proceeding",
										"Bookings", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException e) {
					JOptionPane
							.showMessageDialog(
									BookingEventInfo.this,
									"Please select an Entry in the Booking list before proceeding",
									"Bookings", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnBackClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTextboxes();
				if (listener != null) {
					listener.backEventActionOccured();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					String evName = txtEventName.getText();
					String evVenue = txtVenue.getText();
					String evDate = format.format(picker.getDate());
					String evTime = (String) cboTime.getSelectedItem();
					int evGuest = Integer.parseInt(txtGuests.getText());
					String evDetails = txtArea.getText();
					String evType = "None given";
					if (cboType.getSelectedItem().equals("Others")) {
						evType = txtOthers.getText().trim();
					} else {
						evType = cboType.getSelectedItem().toString();
					}
					Date date = new Date(System.currentTimeMillis());
					if (id == 0) {
						int yesno = JOptionPane.showConfirmDialog(
								BookingEventInfo.this,
								"ARE YOU SURE YOU WANT TO SAVE THESE EXISTING EVENT DETAILS?\n\n"
										+ "Title: " + evName + "\n\n"
										+ "Venue: " + evVenue + "\n\n"
										+ "Type : " + evType + "\n\n"
										+ "Date : " + evDate + "\n\n"
										+ "Time : " + evTime + "\n\n"
										+ "Number or Guest: " + evGuest
										+ "\n\n" + "Other Details \n\t"
										+ evDetails, "Bookings",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (yesno == JOptionPane.YES_OPTION) {
							if(controller.connect().equals("ok")) {
								controller.addBooking(getEventInformation());
								controller.saveBooking();
								controller.loadBookingRecordsByClientIDTypeStatus(client_id, 2, "Ongoing");
								bookingList = controller.getBooking();
								table.setBookingList(bookingList);
								closeBoxes();
								JOptionPane
										.showMessageDialog(
												BookingEventInfo.this,
												"Successfully saved client's booking Information!",
												"Bookings",
												JOptionPane.INFORMATION_MESSAGE);
								setMessage(
										"Successfully saved client's booking Information!",
										1);
								checkSave = 1;
							} else {
								System.out.println(controller.connect());
							}
						}
					} else {
						int yesno = JOptionPane.showConfirmDialog(
								BookingEventInfo.this,
								"ARE YOU SURE YOU WANT TO UPDATE THESE EXISTING EVENT DETAILS?\n\n"
										+ "Title: " + evName + "\n\n"
										+ "Venue: " + evVenue + "\n\n"
										+ "Type : " + evType + "\n\n"
										+ "Date : " + evDate + "\n\n"
										+ "Time : " + evTime + "\n\n"
										+ "Number or Guest: " + evGuest
										+ "\n\n" + "Other Details \n\n"
										+ evDetails, "Bookings",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (yesno == JOptionPane.YES_OPTION) {
							if(controller.connect().equals("ok")) {
								controller.addBooking(getEventInformation());
								controller.saveBooking();
								controller.loadBookingRecordsByClientIDTypeStatus(client_id, 2, "Ongoing");
								bookingList = controller.getBooking();
								table.setBookingList(bookingList);
								closeBoxes();
								JOptionPane
										.showMessageDialog(
												BookingEventInfo.this,
												"Successfully saved client's booking Information!",
												"Bookings",
												JOptionPane.INFORMATION_MESSAGE);
								setMessage(
										"Successfully saved client's booking Information!",
										1);
								checkSave = 1;
							} else {
								System.out.println(controller.connect());
							}
						}
					}
				} catch (NullPointerException e) {
					JOptionPane
							.showMessageDialog(
									BookingEventInfo.this,
									"Don't leave empty fields and The only allowed special characters are as follows:\n"
											+ "\n\n ( ) - : @ & ' ! . ",
									"Bookings", JOptionPane.ERROR_MESSAGE);
					setMessage("Don't leave empty fields!", 0);
				} catch (NumberFormatException ed) {
					JOptionPane.showMessageDialog(BookingEventInfo.this,
							"Check your guest number!", "Bookings",
							JOptionPane.ERROR_MESSAGE);
					setMessage("Check your guest number!", 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearTextboxes();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (listener != null) {
						listener.saveEventActionOccured(getEventInformation());
					}
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				tableToTextboxes();
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				tableToTextboxes();
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						if (listener != null) {
							listener.saveEventActionOccured(getEventInformation());
						}
					} catch (NumberFormatException e1) {
						JOptionPane
								.showMessageDialog(
										BookingEventInfo.this,
										"Please Select a record from the list of Events",
										"Bookings", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				table.refresh();
				openBoxes();
				clearTextboxes();
			}
		});

		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cboType.getSelectedItem().equals("Others")) {
					txtOthers.setVisible(true);
				} else {
					txtOthers.setVisible(false);
				}
			}
		});

		picker.getDateEditor().addPropertyChangeListener("date",new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				try {
					String date = dateFormat.format(picker.getDate());
					int row = table.getSelectedRow();
					if(controller.connect().equals("ok")) {
						bookingListDate = controller.checkifDateHasEvent(date, "Open" , "Ongoing");
						if(bookingListDate.size() > 0 ) {
							if(row != -1) {
								Booking book = bookingList.get(row);
								if(!book.getEventDate().equals(date)) {
									setMessage("("+bookingListDate.size() + ") Event/s booked on the selected date.", 0);
								} else {
									if(bookingListDate.size() > 1) {
										int s = bookingListDate.size() - 1;
										setMessage("("+ s + ") Event/s booked on the same day as the selected record.", 0);
									} else {
										setMessage("Status", 2);
									}
								}
							} else {
								setMessage("("+bookingListDate.size() + ") Event/s booked on the selected date.", 0);
							}
						} else {
							setMessage("Status", 2);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void tableToTextboxes() {
		int row = table.getSelectedRow();
		if (row != -1) {
			Booking book = bookingList.get(row);
			id = book.getId();
			client_id = book.getClient_id();
			txtEventName.setText(book.getEventName());
			txtVenue.setText(book.getEventVenue());
			String time = book.getEventTime();
			cboTime.setSelectedItem(time);
			String type = book.getEventType();
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
			} else if (type.equals(CategoryEventType.Party.name())) {
				cboType.setSelectedItem("Party");
			} else if (type.equals(CategoryEventType.Reunion.name())) {
				cboType.setSelectedItem("Reunion");
			} else if (type.equals(CategoryEventType.Seminar.name())) {
				cboType.setSelectedItem("Seminar");
			} else if (type.equals(CategoryEventType.Wedding.name())) {
				cboType.setSelectedItem("Wedding");
			} else {
				cboType.setSelectedItem("Others");
				txtOthers.setText(type);
			}
			txtGuests.setText("" + book.getEventGuestNumber());
			txtArea.setText(book.getEventDetails());

			try {
				Date d = dateFormat.parse(book.getEventDate());
				picker.setDate(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
	}

	public void openBoxes() {
		btnSave.setEnabled(true);
		txtEventName.setEnabled(true);
		txtVenue.setEnabled(true);
		txtGuests.setEnabled(true);
		txtArea.setEnabled(true);
		picker.setEnabled(true);
		cboTime.setEnabled(true);
		cboType.setEnabled(true);
		txtOthers.setEnabled(true);
		btnNew.setEnabled(false);
	}

	public void closeBoxes() {
		btnSave.setEnabled(false);
		txtEventName.setEnabled(false);
		txtVenue.setEnabled(false);
		txtGuests.setEnabled(false);
		txtOthers.setEnabled(false);
		txtArea.setEnabled(false);
		picker.setEnabled(false);
		cboTime.setEnabled(false);
		cboType.setEnabled(false);
		btnNew.setEnabled(true);
	}

	public void clearTextboxes() {
		id = 0;
		txtEventName.setText("");
		txtVenue.setText("");
		txtGuests.setText("");
		txtArea.setText("");
		txtOthers.setText("");
	}

	public void setTableData() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				try {
					if(controller.connect().equals("ok")) {
						controller.loadBookingRecordsByClientIDTypeStatus(client_id, 2, "Ongoing");
						bookingList = controller.getBooking();
					} else {
						System.out.println(controller.connect());
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void done() {
				if(bookingList != null) {
					table.setBookingList(bookingList);
				}
			}
		};
		worker.execute();
	}

	public BookingEventForm getEventInformation() {
		BookingEventForm ev = null;
		String evName = txtEventName.getText();
		String evVenue = txtVenue.getText();
		String evDate = dateFormat.format(picker.getDate());
		String evTime = (String) cboTime.getSelectedItem();
		int evGuest = Integer.parseInt(txtGuests.getText());
		String guest = txtGuests.getText();
		String evDetails = txtArea.getText();
		int c_id = client_id;
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

	private void setMessage(String msg, int msgtype) {
		lblMessage.setText(msg);
		if (msgtype == 1) {
			panelMessenger.setBackground(CustomColor.okColorBackGround());
		} else if (msgtype == 0) {
			panelMessenger.setBackground(CustomColor.notOkColorBackGround());
		} else {
			panelMessenger.setBackground(CustomColor.bgColor());
		}
	}

	public void setBookingEventListener(BookingEventListener listener) {
		this.listener = listener;
	}

	public void setClientText(String str) {
		lblClientInfo.setText(str);
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public int getEvent_id() {
		return id;
	}

	public int getClient_id() {
		return client_id;
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weighty = 0;
		gc.insets = inset;
		
		gc.gridy = 0;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblClient, gc);

		gc.weightx = 1;
		gc.gridx = 1;
		gc.gridwidth = 8;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(lblClientInfo, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblEventName, gc);

		gc.weightx = 1;
		gc.gridx = 1;
		gc.gridwidth = 8;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(txtEventName, gc);

		gc.weightx = 0;
		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblVenue, gc);

		gc.weightx = 1;
		gc.gridx = 1;
		gc.gridwidth = 8;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(txtVenue, gc);

		gc.weightx = 1;
		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblEventDate, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(picker, gc);

		gc.gridx = 4;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblTime, gc);

		gc.gridx = 5;
		gc.anchor = GridBagConstraints.LINE_START;
		add(cboTime, gc);

		gc.gridx = 7;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		add(lblEventType, gc);

		gc.gridx = 8;
		gc.anchor = GridBagConstraints.LINE_START;
		add(cboType, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		add(lblGuests, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(txtGuests, gc);

		gc.gridx = 8;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(txtOthers, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.fill = GridBagConstraints.NONE;
		add(lblDesc, gc);

		gc.gridx = 1;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.gridwidth = 8;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		gc.gridy++;
		gc.gridx = 1;
		add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		panelButton.add(btnBackClient);
		panelButton.add(btnNew);
		panelButton.add(btnSave);
		panelButton.add(btnClear);
		panelButton.add(btnNext);

		panelMessenger.add(lblMessage);

		gc.gridy++;
		gc.gridx = 1;
		gc.weighty = 0;
		add(panelMessenger, gc);

		gc.gridy++;
		gc.gridx = 1;
		add(panelButton, gc);

	}

	// TODO
	private void initUI() {

		lblEventName = new JLabel("Event Title");
		lblEventType = new JLabel("Event Type");
		lblEventDate = new JLabel("Event Date");
		lblVenue = new JLabel("Venue ");
		lblGuests = new JLabel("Guest's no.");
		lblTime = new JLabel("Time ");
		lblDesc = new JLabel("Details");
		lblClient = new JLabel("Client");
		lblClientInfo = new JLabel("");
		lblMessage = new JLabel("STATUS: ");

		Font f = CustomFont.setFont(lblEventName.getFont().toString(),
				Font.PLAIN, 15);
		Font fbold = CustomFont.setFont(lblEventName.getFont().toString(),
				Font.BOLD, 15);

		lblEventName.setFont(f);
		lblEventType.setFont(f);
		lblEventDate.setFont(f);
		lblVenue.setFont(f);
		lblGuests.setFont(f);
		lblTime.setFont(f);
		lblDesc.setFont(f);
		lblClient.setFont(f);
		lblClientInfo.setFont(f);

		txtEventName = new JTextField(20);
		txtGuests = new JTextField(20);
		txtVenue = new JTextField(20);
		txtOthers = new JTextField(20);
		txtArea = new JTextArea();

		txtEventName.setFont(f);
		txtOthers.setFont(f);
		txtGuests.setFont(f);
		txtVenue.setFont(f);
		txtArea.setFont(f);
		txtArea.setLineWrap(true);

		eventModel = new BookingEventInfoTableModel();
		table = new CustomTableEvent(eventModel);

		cboTime = new JComboBox<String>();
		cboap = new JComboBox<String>();
		cboType = new JComboBox<String>();

		cboType.setFont(f);
		cboTime.setFont(f);

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		panelMessenger = new JPanel();
		panelMessenger.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelMessenger.setBackground(Color.WHITE);
		panelMessenger.setBorder(BorderFactory.createEtchedBorder());

		CustomIcon ci = new CustomIcon();

		btnNext = new JButton("Proceed", ci.createIcon("/res/next.png"));
		btnBackClient = new JButton("Back", ci.createIcon("/res/back.png"));
		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnNew = new JButton("New Booking",ci.createIcon("/res/add_booking.png"));
		btnShow = new JButton("show");
		
		btnNext.setBackground(CustomColor.bgColor());
		btnBackClient.setBackground(CustomColor.bgColor());
		btnSave.setBackground(CustomColor.bgColor());
		btnClear.setBackground(CustomColor.bgColor());
		btnNew.setBackground(CustomColor.bgColor());

		btnNext.setVerticalTextPosition(AbstractButton.CENTER);
		btnNext.setHorizontalTextPosition(AbstractButton.LEADING);

		dateFormat = new SimpleDateFormat(" MMMMM dd , yyyy - EEEEE");
		format = new SimpleDateFormat("MMMMM/dd/yyyy");

		Dimension d = getPreferredSize();
		d.width = 470;
		d.height = 420;
		
		JCalendar cal = new JCalendar();
		cal.setDecorationBordersVisible(true);
		cal.setDecorationBackgroundColor(Color.WHITE);
		cal.setTodayButtonVisible(true);
		cal.setSundayForeground(Color.RED);
		cal.setPreferredSize(d);
		
		picker = new JDateChooser(cal, null,null,null);
		picker.setDate(Calendar.getInstance().getTime());
		picker.setDateFormatString(" MMMMM dd , yyyy - EEEEE");
		picker.setBackground(CustomColor.bgColor());
		
		picker.setFont(f);

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

		DefaultComboBoxModel<String> modTiamp = new DefaultComboBoxModel<String>();
		modTiamp.addElement("am");
		modTiamp.addElement("pm");

		cboap.setModel(modTiamp);

		DefaultComboBoxModel<String> modType = new DefaultComboBoxModel<String>();
		modType.addElement("Anniversary");
		modType.addElement("Baptismal");
		modType.addElement("Birthday");
		modType.addElement("Forum");
		modType.addElement("Pageant");
		modType.addElement("Photoshoot");
		modType.addElement("Party");
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

		closeBoxes();
	}

	// TODO
	private void set() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setPreferredSize(CustomDimension.setDimensionHeight(400));
		setBackground(CustomColor.bgColor());
	}
}