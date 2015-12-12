package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import net.sf.dynamicreports.design.transformation.CustomBatikRenderer;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForClients;
import com.threemc.data.Client;

public class Clients extends Dialog {

	// TODO
	private JPanel panelTitle;
	private JPanel panelCenter;
	private JPanel panelTable;
	private JPanel panelBot;
	private JPanel panelButton;

	private JLabel lblTitle;
	private JLabel lblStatus;

	private JButton btnShowe;

	private JComboBox<String> cboType;
	private JTextField txtSearch;

	private ControllerForBookingDetails controller;
	private ControllerForClients controllerc;
	private Color col = Color.decode("#fd9c9c");

	private CustomTableClient table;
	private ClientTableModel tableModel;

	private JPopupMenu pop;
	private JMenuItem openBooking;
	private JMenuItem openClient;

	private JButton btnRefresh;
	private JButton btnAddClient;

	private BookingEventInfo bookingInfo;
	private Color colo = Color.decode("#ffd700");

	private String category = "client_id";

	private ArrayList<Client> clientList;

	// TODO
	public Clients(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Clients || Three McQueens Eventi Automated System",
				modal);

		set(parent);
		initUI();
		layoutComponents();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Clients.this.dispose();
				super.windowClosing(e);
			}
		});

		loadFirst();

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				int row = table.rowAtPoint(e.getPoint());
				table.getSelectionModel().setSelectionInterval(row, row);
				if(e.getButton() == MouseEvent.BUTTON3 ) {
					pop.show(table, e.getX(), e.getY());
				}
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ClientMenu cmu = new ClientMenu(parent, modal);
					cmu.setClient(getSelectedClient());
					cmu.setVisible(true);
				}
			}
		});

		openClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Bookings booking = new Bookings(Clients.this.getOwner(), getModalityType());
				booking.setInClient(getSelectedClient());
				booking.setVisible(true);
			}
		});

		openBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bookings booking = new Bookings(Clients.this.getOwner(), getModalityType());
				booking.setInBooking(getSelectedClient());
				booking.setVisible(true);
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadFirst();
			}
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});

		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cboType.getSelectedItem().equals("First name")) {
					category = "client_firstName";
				} else if(cboType.getSelectedItem().equals("Middle name")) {
					category = "client_middleName";
				} else if(cboType.getSelectedItem().equals("Last name")) {
					category = "client_lastName";
				} else if(cboType.getSelectedItem().equals("Address")) {
					category = "client_address";
				} else if(cboType.getSelectedItem().equals("Contact no.")) {
					category = "client_contactNo";
				} else if(cboType.getSelectedItem().equals("Client ID")) {
					category = "client_id";
				}
			}
		});

		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bookings book = new Bookings(parent , ModalityType.APPLICATION_MODAL);
				book.setVisible(true);
				Clients.this.dispose();
			}
		});

		btnShowe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	// TODO
	private void setMessageBar(String msg, int msgType) {
		lblStatus.setText(msg);
		if(msgType == 1) {
			panelBot.setBackground(CustomColor.okColorBackGround());
		} else if(msgType == 0) {
			panelBot.setBackground(CustomColor.notOkColorBackGround());
		} else {
			panelBot.setBackground(CustomColor.bgColor());
		}
	}

	public Client getSelectedClient() {
		int row = table.getSelectedRow();
		int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
		String fname = tableModel.getValueAt(row, 1).toString();
		String mname = tableModel.getValueAt(row, 2).toString();
		String lname = tableModel.getValueAt(row, 3).toString();
		String contact = tableModel.getValueAt(row, 4).toString();
		String address = tableModel.getValueAt(row, 5).toString();
		String gen = tableModel.getValueAt(row, 6).toString();
		CategoryGender gena = null;
		if(gen.equals("male")) {
			gena = CategoryGender.male;
		} else {
			gena = CategoryGender.female;
		}
		Client client = new Client(id, fname, mname, lname, address, contact, gena);
		return client;
	}

	public void loadFirst() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadClient("c.client_lastName");
				clientList = controller.getClient();
				table.setClientList(clientList);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search() {
		try {
			if(controllerc.connect().equals("ok")) {
				controllerc.searchClient(category, txtSearch.getText().toString());
				clientList = controllerc.getClient();
				table.setClientList(clientList);
			} else {
				System.out.println(controllerc.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(3, 3, 3, 3);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.gridx = 0;
		gc.gridy = 0;
		panelCenter.add(txtSearch, gc);

		gc.gridx = 1;

		panelCenter.add(cboType, gc);

		gc.gridx = 2;

		panelCenter.add(btnRefresh, gc);

		gc.gridx = 3;

		panelCenter.add(btnAddClient, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 4;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;

		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		panelCenter.add(panelTable, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelBot.add(lblStatus);
		panelCenter.add(panelBot, gc);

		gc.gridy++;
		gc.gridx = 0;

		panelButton.add(btnShowe);
		panelCenter.add(panelButton, gc);

		panelTitle.add(lblTitle, BorderLayout.CENTER);
		add(panelTitle, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controller = new ControllerForBookingDetails();
		controllerc = new ControllerForClients();

		bookingInfo = new BookingEventInfo();

		panelTitle = new JPanel();
		panelTitle.setLayout(new BorderLayout());
		panelTitle.setPreferredSize(new Dimension(0, 100));
		panelTitle.setBackground(Color.BLACK);

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
//		panelCenter.setPreferredSize(getMaximumSize());
		panelCenter.setBackground(CustomColor.bgColor());

		panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout());
		panelTable.setPreferredSize(getMaximumSize());

		panelBot = new JPanel();
		panelBot.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBot.setBackground(Color.WHITE);
		panelBot.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEtchedBorder(), 
				BorderFactory.createEmptyBorder(5,5,5,5)));

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor()); //CustomColor.bgColor()

		lblTitle = new JLabel("Clients");
		CustomIcon ci = new CustomIcon();
		lblTitle.setIcon(ci.createIcon("/res/clientstop.png"));

		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font fbold = CustomFont.setFont("Tahoma", Font.BOLD, 15);

		lblStatus = new JLabel("Status: ");

		DefaultComboBoxModel<String> typemod = new DefaultComboBoxModel<String>();
		typemod.addElement("Client ID");
		typemod.addElement("First name");
		typemod.addElement("Last name");
		typemod.addElement("Middle name");
		typemod.addElement("Address");
		typemod.addElement("Contact no.");

		cboType = new JComboBox<String>();
		cboType.setFont(new Font(cboType.getFont().toString(), Font.PLAIN, 14));
		cboType.setModel(typemod);
		cboType.setBackground(CustomColor.bgColor());

		tableModel = new ClientTableModel();
		table = new CustomTableClient(tableModel);

		txtSearch = new JTextField(30);
		txtSearch.setFont(f);

		btnRefresh = new JButton("Refresh", ci.createIcon("/res/refresh.png"));
		btnAddClient = new JButton("Add New Client", ci.createIcon("/res/add_client.png"));
		btnShowe = new JButton("Show All Events");

		btnRefresh.setBackground(CustomColor.bgColor());
		btnAddClient.setBackground(CustomColor.bgColor());
		btnShowe.setBackground(CustomColor.bgColor());

		openBooking = new JMenuItem("Open in Booking Info.");
		openClient = new JMenuItem("Open in Client Info.");
		pop = new JPopupMenu();
		pop.add(openClient);
		pop.add(openBooking);
		pop.addSeparator();
	}

	private void set(final Window parent) {
		setResizable(false);
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
