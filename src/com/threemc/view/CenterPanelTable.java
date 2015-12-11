package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForLogs;
import com.threemc.data.Booking;
import com.threemc.data.HomeData;
import com.threemc.data.Log;
import com.threemc.data.Notice;
import com.threemc.events.CenterPanelTableEventListener;

public class CenterPanelTable extends JPanel {
	// Date Started: 06/26/2015
	private CenterPanelTableModel tableModel;
	private JTable table;

	private ControllerForBookingDetails controllerb;
	private ControllerForLogs controllerl;

	private DefaultComboBoxModel<String> typemod;
	private JComboBox<String> cboType;

	private JPanel panelSearch;
	private JPanel panelTable;
	private JPanel panelTxt;
	private JPanel panelCenter;
	private JTextField txtSearch;

	private JTextArea txtNoticeboard;
	private JTextArea txtLog;

	private JButton btnRefresh;
	private JButton btnEvent;
	private JButton btnEditnotice;
	private JButton btnSavenotice;

	private Timer timer;
	private Timer timerLog;

	private ArrayList<Log> logList;
	private ArrayList<HomeData> homeDataList;

	private JPopupMenu popMain;
	private JMenuItem mniPackage;

	private Color colo = CustomColor.goldColor();
	private CenterPanelTableEventListener listener;

	private HomeData hd;
	private String category = "c.client_firstName";

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			" MMMMM dd , yyyy - EEEEE");

	private Font f3 = CustomFont.setFont("Tahoma", Font.PLAIN, 15);

	private Notice not;

	public CenterPanelTable() {
		setLayout();
		layoutComponents();

		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
				if (cboType.getSelectedIndex() == 0) {
					category = "c.client_firstName";
				} else if (cboType.getSelectedIndex() == 1) {
					category = "c.client_lastName";
				} else if (cboType.getSelectedIndex() == 2) {
					category = "e.event_title";
				} else if (cboType.getSelectedIndex() == 3) {
					category = "e.event_type";
				} else if (cboType.getSelectedIndex() == 4) {
					category = "e.event_date";
				} else if (cboType.getSelectedIndex() == 5) {
					category = "e.event_venue";
				}
			}
		});
		txtSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtSearch.isFocusOwner()) {
					refresh();
				}
			}
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				se();
			}

			public void insertUpdate(DocumentEvent e) {
				se();
			}

			public void changedUpdate(DocumentEvent e) {
				se();
			}

			private void se() {
				if (txtSearch.getText().isEmpty()) {
					timer.start();
				} else {
					timer.stop();
				}
				search();
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadfirst();
				if(btnRefresh.isVisible()) {
					
				}
			}
		});

		btnEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showIfEventisNow();
			}
		});

		btnEditnotice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEditnotice.setVisible(false);
				btnSavenotice.setVisible(true);
				txtNoticeboard.setEditable(true);
				txtNoticeboard.setBackground(Color.WHITE);
			}
		});

		btnSavenotice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = dateFormat.format(new Date(System.currentTimeMillis()));
				String desc = txtNoticeboard.getText();

				Notice not = new Notice(date, desc);
				try {
					if(controllerb.connect().equals("ok")) {
						controllerb.saveNotice(not);
						loadFirstNotice();
						JOptionPane.showMessageDialog(CenterPanelTable.this, "Successfully saved Notice Board Message!", "Notice Board", JOptionPane.INFORMATION_MESSAGE);
						btnSavenotice.setVisible(false);
						btnEditnotice.setVisible(true);
						txtNoticeboard.setEditable(false);
						txtNoticeboard.setBackground(CustomColor.bgColor());
					} else {
						System.out.println(controllerb.connect());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				try {
					Date currenDate = new Date(System.currentTimeMillis());
					Date eventDate = dateFormat.parse(homeDataList.get(row)
							.getEventDate());

					String cDate = dateFormat.format(currenDate);
					String eDate = dateFormat.format(eventDate);

					if (cDate.equals(eDate)) {
						if (isSelected) {
							c.setBackground(Color.BLACK);
							c.setForeground(colo);
						} else {
							c.setBackground(CustomColor.goldColor());
							c.setForeground(Color.BLACK);
						}
					} else if (currenDate.after(eventDate)) {
						if (isSelected) {
							c.setBackground(Color.BLACK);
							c.setForeground(colo);
						} else {
							c.setBackground(CustomColor.okColorBackGround());
							c.setForeground(Color.BLACK);
						}
					} else {
						if (isSelected) {
							c.setBackground(Color.BLACK);
							c.setForeground(colo);
						} else {
							c.setBackground(Color.WHITE);
							c.setForeground(Color.BLACK);
						}
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				return c;
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				table.getSelectionModel().setSelectionInterval(row, row);
				if (e.getButton() == MouseEvent.BUTTON3) {
					hd = homeDataList.get(table.getSelectedRow());
					popMain.show(table, e.getX(), e.getY());
				}
			}
		});

		mniPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listener != null) {
					listener.openBookingDetailsAction(hd);
				}
			}
		});
	}

	public void showIfEventisNow() {
		try {
			if (controllerb.connect().equals("ok")) {
				Date currenDate = new Date(System.currentTimeMillis());
				String cDate = dateFormat.format(currenDate);

				ArrayList<Booking> bookingList = controllerb
						.loadBookingRecordsByEventDate(cDate);
				if (bookingList.size() != 0) {
					for (int i = 0; i < bookingList.size(); i++) {
						Booking book = bookingList.get(i);
						BookingEventsNow ben = new BookingEventsNow();
						ben.setBooking(book);
						ben.setVisible(true);
					}
				}
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setCenterPanelTableEventListener(
			CenterPanelTableEventListener listener) {
		this.listener = listener;
	}

	public void search() {
		try {
			if (controllerb.connect().equals("ok")) {
				controllerb.searchEventsWithServicesAndPayments(category,
						txtSearch.getText().toString());
				homeDataList = controllerb.getHomeData();
				setData(homeDataList);
				refresh();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
	}

	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	public void setData(ArrayList<HomeData> db) {
		tableModel.setData(db);
	}

	private void loadfirst() {
		try {
			if (controllerb.connect().equals("ok")) {
				controllerb.loadEventsWithServicesAndPayments();
				homeDataList = controllerb.getHomeData();
				setData(homeDataList);
				refresh();
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void loadFirstNotice() {
		try {
			if(controllerb.connect().equals("ok")) {
				not = controllerb.loadLastNotice();
				if(not != null) {
					StringBuffer msg = new StringBuffer();
					msg.append(not.getDesc());
					txtNoticeboard.setText(msg.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reloadfirst() {
		try {
			if (controllerb.connect().equals("ok")) {
				if (controllerb.getEventsWithServicesAndPaymentsCount() > homeDataList
						.size()) {
					loadfirst();
				}
				if (controllerb.getEventsThatisEditedCount() > 0) {
					controllerb.updateBookingisEdited();
					loadfirst();
				}
			} else {
				System.out.println(controllerb.connect());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void loadLog() {
		try {
			if (controllerl.connect().equals("ok")) {
				controllerl.loadLogsLimitTen();
				logList = controllerl.getLog();
				StringBuffer msg = new StringBuffer();
				for (int i = 0; i < logList.size(); i++) {
					Log log = logList.get(i);
					msg.append("["+log.getLogDate()+"]["+log.getLogTitle()+"]"+log.getLogDesc()+"\n");
				}
				txtLog.setText(msg.toString());
			} else {
				System.out.println(controllerl.connect());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	private void reloadLog() {
		try {
			if (controllerl.connect().equals("ok")) {
				if (controllerl.getNewLogsCount() > 0) {
					controllerl.updateLogsNewToOld();
					loadLog();
				}
			} else {
				System.out.println(controllerl.connect());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public void setLayout() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());

		Dimension d = getPreferredSize();
		d.height = 400;

		controllerb = new ControllerForBookingDetails();

		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent w) {
				reloadfirst();
			}
		});

		timerLog = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent w) {
				reloadLog();
			}
		});

		timer.start();
		timerLog.start();

		tableModel = new CenterPanelTableModel();
		table = new JTable(tableModel);

		Font f = CustomFont.setFontTahomaPlain();
		Font fbold = CustomFont.setFontTahomaBold();
		controllerl = new ControllerForLogs();

		panelSearch = new JPanel();
		panelSearch.setLayout(new FlowLayout(FlowLayout.LEFT));

		panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout());
		panelTable.setPreferredSize(d);

		panelTxt = new JPanel();
		panelTxt.setLayout(new BorderLayout());

		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.setRowHeight(20);
		table.getTableHeader().setFont(fbold);
		table.setFont(f);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(400);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(300);
		tcm.getColumn(3).setPreferredWidth(300);
		tcm.getColumn(4).setPreferredWidth(100);
		tcm.getColumn(5).setPreferredWidth(400);
		tcm.getColumn(6).setPreferredWidth(150);
		tcm.getColumn(7).setPreferredWidth(150);

		typemod = new DefaultComboBoxModel<String>();
		typemod.addElement("First Name");
		typemod.addElement("Last Name");
		typemod.addElement("Name of Event");
		typemod.addElement("Type of Event");
		typemod.addElement("Date");
		typemod.addElement("Location");

		cboType = new JComboBox<String>();
		cboType.setModel(typemod);
		cboType.setFont(f);

		txtSearch = new JTextField(40);
		txtSearch.setFont(f);

		btnEditnotice = new JButton("Edit Notice Board");
		btnSavenotice = new JButton("Save Notice Board");
		btnEvent = new JButton("Today's Event");
		btnRefresh = new JButton("Refresh");

		btnEditnotice.setFont(f);
		btnSavenotice.setFont(f);
		btnRefresh.setFont(f);
		btnEvent.setFont(f);

		btnSavenotice.setVisible(false);

		panelSearch.add(txtSearch);
		panelSearch.add(cboType);
		panelSearch.add(btnRefresh);
		panelSearch.add(btnEvent);
		panelSearch.add(btnEditnotice);
		panelSearch.add(btnSavenotice);

		popMain = new JPopupMenu();
		mniPackage = new JMenuItem("Show Complete Details for this Booking");

		txtNoticeboard = new JTextArea(7,30);
		txtNoticeboard.setFont(CustomFont.setFontTahomaPlain());
		txtNoticeboard.setEditable(false);
		txtNoticeboard.setBackground(CustomColor.bgColor());

		txtLog = new JTextArea(5,30);
		txtLog.setFont(CustomFont.setFontTahomaPlain());
		txtLog.setBackground(CustomColor.bgColor());
		txtLog.setEditable(false);

		String msg = "This is the Notice Board.\n\nYou can input reminders,notices or any "
				+ "messages you want to pass\non to the staff that uses the\nThree McQueens "
				+ "Eventi System for Staff/Employee";
		txtNoticeboard.setText(msg);

		popMain.add(mniPackage);

		loadfirst();
		loadLog();
		loadFirstNotice();
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		gc.weighty = 0;
		gc.weightx = 1;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(panelSearch, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;

		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		
		panelCenter.add(panelTable, BorderLayout.NORTH);

//		gc.gridy++;
//		gc.gridx = 0;

		panelTxt.add(new JScrollPane(txtNoticeboard,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		panelCenter.add(panelTxt, BorderLayout.CENTER);

//		gc.gridy++;
//		gc.gridx = 0;

		panelCenter.add(new JScrollPane(txtLog,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);
		
		add(panelCenter, gc);
	}
}
