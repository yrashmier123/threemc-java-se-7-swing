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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Booking;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

// TODO
public class BookingsList extends Dialog {

	private JLabel lblTitle;
	private JLabel lblSearch;

	private CustomIcon ci;

	private JTextField txtSearch;

	private JComboBox<String> cboSearchType;
	private JComboBox<String> cboStatus;
	private JComboBox<String> cboStatusu;
	private JComboBox<String> cboPayment;

	private JDateChooser picker;
	private SimpleDateFormat dateFormat;

	private BookingEventInfoTableModel eventModel;
	private CustomTableEvent table;

	private JPanel panelCenter;
	private JPanel panelBar;
	private JPanel panelButtons;
	private JPanel panelCbo;
	private JPanel panelStatus;

	private JButton btnShowList;
	private JButton btnUpdateList;
	private JButton btnClose;
	private JButton btnUpdate;
	private JButton btnOpen;
	private JButton btnRefresh;
	private JButton btnCancel;
	private JButton btnFinish;

	private ButtonGroup grp;
	private JRadioButton rdOpen;

	private JLabel lblStatus;
	private JLabel lblStatusPayment;
	private JLabel lblList;

	private ControllerForBookingDetails controller;
	private ArrayList<Booking> bookingList;

	private Color col = CustomColor.notOkColorBackGround();
	private Color colo = CustomColor.goldColor();

	private int event_id = 0;
	private String status;

	// TODO
	public BookingsList(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Booking - List", modal);
		set(parent);
		initUI();
		layoutComponent();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				BookingsList.this.dispose();
			}
		});

		try {
			if(controller.connect().equals("ok")) {
				controller.loadAllBookingRecord("Open" , "" , "");
				bookingList = controller.getBooking();
				table.setBookingList(bookingList);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					event_id = bookingList.get(row).getId();
					lblStatus.setText("Status: "+bookingList.get(row).getEventStatus() +", "+ bookingList.get(row).getEventStatus2());
					lblStatusPayment.setText("Payment Status: "+bookingList.get(row).getEventPaymentStatus());
				}
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					event_id = bookingList.get(row).getId();
					lblStatus.setText("Status: "+bookingList.get(row).getEventStatus() +", "+ bookingList.get(row).getEventStatus2());
					lblStatusPayment.setText("Payment Status: "+bookingList.get(row).getEventPaymentStatus());
				}
			}
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				search();
			}

			public void insertUpdate(DocumentEvent arg0) {
				search();
			}

			public void changedUpdate(DocumentEvent arg0) {
				search();
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshTable();
			}
		});

		picker.getDateEditor().addPropertyChangeListener("date", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				try {
					String date = dateFormat.format(picker.getDate());
					if(controller.connect().equals("ok")) {
						bookingList = controller.checkifDateHasEventForBookingList(date);
						table.setBookingList(bookingList);
						lblList.setText("Found " + bookingList.size() + " Event(s) in the Database");
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					Booking book = bookingList.get(row);
					if(!book.getEventPaymentStatus().equals("Fully Paid")) {
						int yon = JOptionPane.showConfirmDialog(BookingsList.this, "Are you sure you want to close the selected event?", 
								"Bookings - List", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(yon == JOptionPane.YES_OPTION) {
							if (table.getSelectedRow() != -1) {
								try {
									if(controller.connect().equals("ok")) {
										controller.updateBookingStatus(event_id, "Close");
										refreshTable();
										JOptionPane.showMessageDialog(BookingsList.this, "Booking record closed", "Bookings - List", JOptionPane.INFORMATION_MESSAGE);
									} else {
										System.out.println(controller.connect());
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(BookingsList.this, "The selected record is already tagged as Fully Paid", "Bookings - List", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					Booking book = bookingList.get(row);
					if(!book.getEventPaymentStatus().equals("Fully Paid")) {
						int yon = JOptionPane.showConfirmDialog(BookingsList.this, "Are you sure you want to cancel the selected event?", 
								"Bookings - List", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if(yon == JOptionPane.YES_OPTION) {
							if (table.getSelectedRow() != -1) {
								try {
									if(controller.connect().equals("ok")) {
										controller.updateBookingStatus2(event_id, "Cancel");
										refreshTable();
										JOptionPane.showMessageDialog(BookingsList.this, "Booking record cancelled", "Bookings - List", JOptionPane.INFORMATION_MESSAGE);
									} else {
										System.out.println(controller.connect());
									}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(BookingsList.this, "The selected record is already tagged as Fully Paid", "Bookings - List", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					Booking book = bookingList.get(row);
					if(book.getEventStatus().equals("Close")) {
						try {
							if(controller.connect().equals("ok")) {
								controller.updateBookingStatus(event_id, "Open");
								controller.updateBookingStatus2(event_id, "Ongoing");
								refreshTable();
								JOptionPane.showMessageDialog(BookingsList.this, "Booking record opened", "Bookings - List", JOptionPane.INFORMATION_MESSAGE);
							} else {
								System.out.println(controller.connect());
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					Booking book = bookingList.get(row);
					if(book.getEventPaymentStatus().equals("Fully Paid")) {
						int yon = JOptionPane.showConfirmDialog(BookingsList.this, "Are you sure you want to Set this event as DONE?", "Bookings - List", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if(yon == JOptionPane.YES_OPTION) {
							try {
								if(controller.connect().equals("ok")) {
									controller.updateBookingStatus2(event_id, "Done");
									refreshTable();
									JOptionPane.showMessageDialog(BookingsList.this, "Successfully Tagged Event as DONE", "Bookings - List", JOptionPane.INFORMATION_MESSAGE);
								} else {
									System.out.println(controller.connect());
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					} else if(book.getEventPaymentStatus().equals("Partial")) {
						JOptionPane.showMessageDialog(BookingsList.this, "The selected record have balances that needs to be settled first.", "Bookings - List", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(BookingsList.this, "No payment set yet.", "Bookings - List", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() != -1) {
					if (bookingList.get(table.getSelectedRow()).getEventStatus().equals("Open")) {
						BookingsUpdate bu = new BookingsUpdate(parent, modal);
						bu.setBooking(bookingList.get(table.getSelectedRow()));
						bu.setBookingFormInfo();
						bu.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(BookingsList.this,
								"Event is closed", "Booking - List",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		btnUpdateList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if (row != -1) {
					Booking book = bookingList.get(row);
					if (book.getEventStatus().equals("Open")) {
						if(book.getEventStatus2().equals("Ongoing")) {
							if(book.getEventPaymentStatus().equals("Partial")) {
								showUpdateServices(parent, modal, row);
							} else if (book.getEventPaymentStatus().equals("Fully Paid")) {
								int okcancel = JOptionPane.showConfirmDialog(BookingsList.this, 
										"The Record you are trying to update is already tagged as \"Fully Paid\".\n\nDo you want to Continue?", 
										"Booking - List", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
								
								if(okcancel == JOptionPane.OK_OPTION) {
									showUpdateServices(parent, modal, row);
								}
							} else {
								showUpdateServices(parent, modal, row);
							}
						} else if(book.getEventStatus2().equals("Done")) {
							int okcancel = JOptionPane.showConfirmDialog(BookingsList.this, 
									"The Record you are trying to update is already tagged as \"Done\".\n\nDo you want to Continue?", 
									"Booking - List", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							
							if(okcancel == JOptionPane.OK_OPTION) {
								showUpdateServices(parent, modal, row);
							}
						} else {
							JOptionPane.showMessageDialog(BookingsList.this,
									"Event is cancelled", "Booking - List",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(BookingsList.this,
								"Event is closed", "Booking - List",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}

			private void showUpdateServices(final Window parent,
					final Dialog.ModalityType modal, int row) {
				BookingsUpdateServices bpu = new BookingsUpdateServices(parent, modal);
				bpu.setBooking(bookingList.get(row));
				bpu.checkIfHasPayment();
				bpu.setVisible(true);
			}
		});

		btnShowList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					if (bookingList.get(row).isHasServices()) {
						if(bookingList.get(row).getEventPaymentStatus().equals("Partial") || 
								bookingList.get(row).getEventPaymentStatus().equals("Fully Paid")) {
							if (bookingList.get(row).getEventStatus().equals("Open")) {
								BookingsShowList bssl = new BookingsShowList(parent, modal);
								int cid = bookingList.get(row).getClient_id();
								int eid = bookingList.get(row).getId();
								bssl.setClientAndEventID(cid, eid);
								bssl.showData();
								bssl.setClientDetails();
								bssl.setPaymentDetails();
								bssl.setBookingDetails();
								bssl.setVisible(true);
							} else {
								JOptionPane.showMessageDialog(BookingsList.this,
										"Event is closed", "Booking - List",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(BookingsList.this,
									"No Paymet set yet", "Booking - List",
									JOptionPane.ERROR_MESSAGE);
						}
					} else { 
						JOptionPane.showMessageDialog(BookingsList.this,
							"Event does not have any services.", "Booking - List",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		cboStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cboStatusu.setSelectedIndex(0);
				cboPayment.setSelectedIndex(0);
				refreshTable();
				lblList.setText("Found " + bookingList.size() + " Event(s) in the Database");
			}
		});

		cboStatusu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cboPayment.setSelectedIndex(0);
				refreshTable();
				lblList.setText("Found " + bookingList.size() + " Event(s) in the Database");
			}
		});

		cboPayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable();
				lblList.setText("Found " + bookingList.size() + " Event(s) in the Database");
			}
		});
	}

	private void refreshTable() {
		try {
			if(controller.connect().equals("ok")) {
				String status = cboStatus.getSelectedItem().toString();
				String statusu = cboStatusu.getSelectedItem().toString();
				String payment = cboPayment.getSelectedItem().toString();
				if(status.equals("All")) {
					status = "";
				}
				if(statusu.equals("All")) {
					statusu = "";
				}
				if(payment.equals("All")) {
					payment = "";
				}
				controller.loadAllBookingRecord(status, statusu, payment);
				bookingList = controller.getBooking();
				table.setBookingList(bookingList);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void search() {
		String val = txtSearch.getText().toString();
		String cat = "";
		if (cboSearchType.getSelectedIndex() == 0) {
			cat = "e.event_title";
		} else if (cboSearchType.getSelectedIndex() == 1) {
			cat = "e.event_venue";
		} else if (cboSearchType.getSelectedIndex() == 2) {
			cat = "e.event_date";
		} else if (cboSearchType.getSelectedIndex() == 3) {
			cat = "e.event_type";
		}

		String status = cboStatus.getSelectedItem().toString();
		String statusu = cboStatusu.getSelectedItem().toString();
		String payment = cboPayment.getSelectedItem().toString();
		if(status.equals("All")) {
			status = "";
		}
		if(statusu.equals("All")) {
			statusu = "";
		}
		if(payment.equals("All")) {
			payment = "";
		}

		try {
			if(controller.connect().equals("ok")) {
				controller.searchAllBookingRecord(val, cat, status, statusu, payment);
				bookingList = controller.getBooking();
				table.setBookingList(bookingList);
				lblList.setText("Found " + bookingList.size() + " Event(s) in the Database");
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponent() {

		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridy = 0;
		gc.gridx = 0;

		add(lblTitle, gc);

		GridBagConstraints gb = new GridBagConstraints();
		Insets inset = new Insets(2,2,2,2);

		gb.weighty = 0;
		gb.insets = inset;
		
		gb.gridy = 0;
		gb.gridx = 0;
		gb.weightx = 0;
		gb.anchor = GridBagConstraints.LINE_START;
		gb.fill = GridBagConstraints.NONE;

		panelBar.add(lblSearch, gb);

		gb.gridx++;
		gb.weightx = 1;
		gb.anchor = GridBagConstraints.LINE_START;
		gb.fill = GridBagConstraints.HORIZONTAL;

		panelBar.add(txtSearch, gb);

		gb.gridx++;
		gb.weightx = 0;
		gb.anchor = GridBagConstraints.LINE_START;
		gb.fill = GridBagConstraints.NONE;

		panelBar.add(cboSearchType, gb);

		gb.gridx++;
		gb.weightx = 1;
		gb.anchor = GridBagConstraints.LINE_START;
		gb.fill = GridBagConstraints.BOTH;

		panelBar.add(picker, gb);

		gb.gridx++;
		gb.weightx = 0;
		gb.anchor = GridBagConstraints.LINE_START;
		gb.fill = GridBagConstraints.NONE;

		// ---------------
		
		panelBar.add(btnRefresh, gb);

		panelCenter.add(panelBar, BorderLayout.NORTH);
		panelCenter.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		
		add(panelCenter, gc);
		
		// ---------------
		
		GridBagConstraints gs = new GridBagConstraints();

		gs.weighty = 0;
		gs.insets = inset;

		gs.gridy = 0;
		gs.gridx = 0;
		gs.weightx = 1;
		gs.anchor = GridBagConstraints.LINE_START;
		gs.fill = GridBagConstraints.HORIZONTAL;

		panelStatus.add(lblStatus, gs);
		
		gs.gridx++;

		panelStatus.add(lblStatusPayment, gs);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		add(panelStatus, gc);

		// ----------------

		panelCbo.add(cboStatus);
		panelCbo.add(cboStatusu);
		panelCbo.add(cboPayment);
		panelCbo.add(lblList);

		gc.gridy++;
		gc.gridx = 0;
		add(panelCbo, gc);

		panelButtons.add(btnUpdate);
		panelButtons.add(btnShowList);
		panelButtons.add(btnUpdateList);
		panelButtons.add(btnFinish);
		panelButtons.add(btnOpen);
		panelButtons.add(btnCancel);
		panelButtons.add(btnClose);

		gc.gridy++;
		gc.gridx = 0;
		add(panelButtons, gc);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForBookingDetails();

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panelCenter.setBackground(CustomColor.bgColor());

		ci = new CustomIcon();

		lblTitle = new JLabel("");
		lblTitle.setIcon(ci.createIcon("/res/bookingtop.png"));

		Font f = CustomFont.setFontTahomaPlain();
		Font fb = CustomFont.setFontTahomaBold();

		panelBar = new JPanel();
		panelBar.setLayout(new GridBagLayout());
		panelBar.setBorder(BorderFactory.createEtchedBorder());
		panelBar.setBackground(CustomColor.bgColor());

		panelStatus = new JPanel();
		panelStatus.setLayout(new GridBagLayout());
		panelStatus.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(5, 5, 5, 5),
						BorderFactory.createEtchedBorder()));
		panelStatus.setBackground(CustomColor.bgColor());

		panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButtons.setBackground(CustomColor.bgColor());

		panelCbo = new JPanel();
		panelCbo.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelCbo.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createCompoundBorder(
						BorderFactory.createEmptyBorder(2, 2, 2, 2),
						BorderFactory.createLineBorder(Color.BLACK)),
				"Show by status ", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, f, Color.BLACK));
		panelCbo.setBackground(CustomColor.bgColor());

		eventModel = new BookingEventInfoTableModel();
		table = new CustomTableEvent(eventModel);

		lblStatusPayment = new JLabel("Payment Status: ");
		lblStatus = new JLabel("Status: ");
		lblSearch = new JLabel("Search: ");
		lblList = new JLabel("");
		
		lblStatusPayment.setFont(fb);
		lblStatus.setFont(fb);
		lblSearch.setFont(f);
		lblList.setFont(f);

		txtSearch = new JTextField(20);
		txtSearch.setFont(f);

		cboSearchType = new JComboBox<String>();
		cboStatus = new JComboBox<String>();
		cboStatusu = new JComboBox<String>();
		cboPayment = new JComboBox<String>();
		
		cboSearchType.setFont(f);
		cboStatus.setFont(f);
		cboStatusu.setFont(f);
		cboPayment.setFont(f);

		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		ee.addElement("Event Title");
		ee.addElement("Venue");
		ee.addElement("Date");
		ee.addElement("Type");

		cboSearchType.setModel(ee);
		cboSearchType.setSelectedIndex(0);

		DefaultComboBoxModel<String> es = new DefaultComboBoxModel<String>();
		es.addElement("All");
		es.addElement("Open");
		es.addElement("Close");

		cboStatus.setModel(es);

		DefaultComboBoxModel<String> ed = new DefaultComboBoxModel<String>();
		ed.addElement("All");
		ed.addElement("Ongoing");
		ed.addElement("Done");
		ed.addElement("Cancel");

		cboStatusu.setModel(ed);

		DefaultComboBoxModel<String> ef = new DefaultComboBoxModel<String>();
		ef.addElement("All");
		ef.addElement("Fully Paid");
		ef.addElement("Partial");
		ef.addElement("No Payment");

		cboPayment.setModel(ef);

		btnUpdateList = new JButton("Update Services");
		btnShowList = new JButton("Show Details");
		btnUpdate = new JButton("Update");
		btnCancel = new JButton("Cancel Event");
		btnClose = new JButton("Close event");
		btnOpen = new JButton("Open Event");
		btnRefresh = new JButton("Refresh");
		btnFinish = new JButton("Finish Event");

		btnUpdateList.setBackground(CustomColor.bgColor());
		btnShowList.setBackground(CustomColor.bgColor());
		btnRefresh.setBackground(CustomColor.bgColor());
		btnUpdate.setBackground(CustomColor.bgColor());
		btnCancel.setBackground(CustomColor.bgColor());
		btnClose.setBackground(CustomColor.bgColor());
		btnOpen.setBackground(CustomColor.bgColor());
		btnFinish.setBackground(CustomColor.bgColor());
		btnRefresh.setFont(f);

		rdOpen = new JRadioButton("Open");
		rdOpen.setBackground(CustomColor.bgColor());
		rdOpen.setActionCommand("Open");
		rdOpen.setSelected(true);

		grp = new ButtonGroup();
		grp.add(rdOpen);

		status = grp.getSelection().getActionCommand();

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

		picker = new JDateChooser(cal, null,null,null);
		picker.setDate(Calendar.getInstance().getTime());
		picker.setDateFormatString(" MMMMM dd , yyyy - EEEEE");
		picker.setBackground(CustomColor.bgColor());
		picker.setFont(f);
	}

	// TODO
	private void set(final Window parent) {
		setLayout(new GridBagLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}

}
