package com.threemc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.prompt.PromptSupport;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Client;
import com.threemc.events.BookingEventClientListener;

public class BookingClientInfo extends JPanel {

	private BookingEventClientListener listener;

	private JLabel lblFullname;
	private JLabel lblContact;
	private JLabel lblGender;
	private JLabel lblAddress;
	private JLabel lblSearch;
	private JLabel lblMessage;

	private JTextField txtFname;
	private JTextField txtMname;
	private JTextField txtLname;
	private JTextField txtContact;
	private JTextField txtAddress;
	private JTextField txtSearch;

	// container for buttons
	private JPanel panelButton;
	private JPanel panelMessagebar;

	private JButton btnSave;
	private JButton btnClear;
	private JButton btnAdd;
	private JButton btnBook;

	// private JPopupMenu pop;
	// private JMenuItem mniNext;
	// private JMenuItem mniDel;

	private JComboBox<String> cboGen;
	private ControllerForBookingDetails controller;
	private Color col = CustomColor.notOkColorBackGround();
	private Color colo = CustomColor.goldColor();

	private CustomTableClient table;
	private ClientTableModel tableModel;
	
	private ArrayList<Client> clientList;

	private int id = 0;

	private ProgressbarDialog prog;
	private Window parent;
	private StringBuffer cat;
	
	// TODO
	public BookingClientInfo(final Window parent) {
		this.parent = parent;
		set();
		initUI();
		layoutComponents();

		loadFirstData("c.client_id");

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enableButtons();
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTextbox();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (id == 0) {
					try {
						saveClient();
						clearTextbox();
						JOptionPane.showMessageDialog(BookingClientInfo.this,
								"Successfully saved client's Information!",
								"Success", JOptionPane.INFORMATION_MESSAGE);
						setMessageBar("Successfully saved client's Information!" , 1);
					} catch (NullPointerException e1) {
						JOptionPane.showMessageDialog(BookingClientInfo.this,
								"Please fill up the form properly",
								"Failed! Error in fields",
								JOptionPane.ERROR_MESSAGE);
						setMessageBar("Please fill up the form properly" , 0);
					} catch (Exception e1) {
						e1.printStackTrace();
						setMessageBar(e1.getMessage() , 0);
					}
				} else if (id > 0) {
					int yesno = JOptionPane
							.showConfirmDialog(
									BookingClientInfo.this,
									"You are about to update an existing record.\nDo you want to proceed?",
									"Booking Client",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.INFORMATION_MESSAGE);
					if (yesno == JOptionPane.YES_OPTION) {
						try {
							saveClient();
							clearTextbox();
							JOptionPane
									.showMessageDialog(
											BookingClientInfo.this,
											"Successfully updated client's Information!",
											"Success",
											JOptionPane.INFORMATION_MESSAGE);
							setMessageBar("Successfully updated client's Information!", 1);
						} catch (NullPointerException e1) {
							JOptionPane.showMessageDialog(
									BookingClientInfo.this,
									"Please fill up the form properly",
									"Failed! Error in fields",
									JOptionPane.ERROR_MESSAGE);
							setMessageBar("Please fill up the form properly", 0);
						} catch (Exception e1) {
							e1.printStackTrace();
							setMessageBar(e1.getMessage() , 0);
						}
					}
				}
			}

			private void saveClient() throws Exception, SQLException {
				controller.addClient(getClientInformation());
				if(controller.connect().equals("ok")) {
					controller.saveClient();
					controller.loadClientRecordsForBooking("c.client_id");
					clientList = controller.getClient();
					table.setClientList(clientList);
					BookingClientInfo.this.disableButtons();
				} else {
					System.out.println(controller.connect());
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				tableToTextBoxes();
			}

		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				super.keyPressed(e);
				tableToTextBoxes();
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (listener != null) {
						listener.saveEventActionOccured(getClientInformation());
					}
				}
			}
		});

		btnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listener != null) {
					listener.saveEventActionOccured(getClientInformation());
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (listener != null) {
						listener.saveEventActionOccured(getClientInformation());
					}
				}
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

		txtContact.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				super.keyPressed(arg0);
				if (txtContact.getText().length() >= 11) {
					txtContact.setText(txtContact.getText().substring(0, 10));
				}
			}
		});
	}

	// TODO
	public void loadFirstData(final String cat) {
		prog.setIndeterminate(true);
		prog.setVisible(true);

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				if(controller.connect().equals("ok")) {
					controller.loadClientRecordsForBooking(cat);
				} else {
					System.out.println(controller.connect());
				}
				return null;
			}

			protected void done() {
				try {
					if(controller.connect().equals("ok")) {
						clientList = controller.getClient();
						table.setClientList(clientList);
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				prog.setIndeterminate(false);
				prog.setVisible(false);
			}
		};
		worker.execute();
	}

	public void tableToTextBoxes() {
		int row = table.getSelectedRow();
		if (row != -1) {
			id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
			txtFname.setText(tableModel.getValueAt(row, 1).toString());
			txtMname.setText(tableModel.getValueAt(row, 2).toString());
			txtLname.setText(tableModel.getValueAt(row, 3).toString());
			txtContact.setText(tableModel.getValueAt(row, 4).toString());
			txtAddress.setText(tableModel.getValueAt(row, 5).toString());
			String n = tableModel.getValueAt(row, 6).toString();
			if (n.equals("male")) {
				cboGen.setSelectedIndex(0);
			} else if (n.equals("female")) {
				cboGen.setSelectedIndex(1);
			}
		}
	}

	public void clearTextbox() {
		txtFname.setText("");
		txtMname.setText("");
		txtLname.setText("");
		txtContact.setText("");
		txtAddress.setText("");
		id = 0;
	}
	
	public void enableButtons() {
		btnSave.setEnabled(true);
		btnAdd.setEnabled(false);
		txtFname.setEditable(true);
		txtMname.setEditable(true);
		txtLname.setEditable(true);
		txtContact.setEditable(true);
		txtAddress.setEditable(true);
		cboGen.setEnabled(true);
	}

	public void search() {
		try {
			if(controller.connect().equals("ok")) {
				controller.searchClient(txtSearch.getText().toString());
				clientList = controller.getClient();
				table.setClientList(clientList);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}

	public void setTextBoxesforClient(Client client) {
		id = client.getId();
		txtFname.setText(client.getClientFirstName());
		txtMname.setText(client.getClientMiddleName());
		txtLname.setText(client.getClientLastName());
		txtAddress.setText(client.getClientAddress());
		txtContact.setText(client.getClientContactNumber());
		if (client.getClientGender().name().equals("male")) {
			cboGen.setSelectedItem("Male");
		} else {
			cboGen.setSelectedItem("Female");
		}

		try {
			if(controller.connect().equals("ok")) {
				controller.searchClient(client.getClientFirstName());
				clientList = controller.getClient();
				table.setClientList(clientList);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}
	
	private void disableButtons() {
		btnAdd.setEnabled(true);
		btnSave.setEnabled(false);
		txtFname.setEditable(false);
		txtMname.setEditable(false);
		txtLname.setEditable(false);
		txtContact.setEditable(false);
		txtAddress.setEditable(false);
		cboGen.setEnabled(false);
	}

	public BookingClientEventForm getClientInformation() {

		BookingClientEventForm ev = null;

		int id = BookingClientInfo.this.id;
		String fname = txtFname.getText();
		String mname = txtMname.getText();
		String lname = txtLname.getText();
		String cont = (String) txtContact.getText();
		String address = txtAddress.getText();
		String gender = (String) cboGen.getSelectedItem();

		if (!fname.isEmpty()&& CustomPatternChecker.checkStringLettersOnly(fname)) {
			txtFname.setBackground(Color.WHITE);
			if (!mname.isEmpty()&& CustomPatternChecker.checkStringLettersOnly(mname)) {
				txtMname.setBackground(Color.WHITE);
				if (!lname.isEmpty()&& CustomPatternChecker.checkStringLettersOnly(lname)) {
					txtLname.setBackground(Color.WHITE);
					if (!cont.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(cont)) {
						txtContact.setBackground(Color.WHITE);
						if (!address.isEmpty()&& CustomPatternChecker.checkStringSomeCharsAllowed(address)) {
							txtAddress.setBackground(Color.WHITE);
							ev = new BookingClientEventForm(this, id, fname,
									mname, lname, address, cont, gender);
							return ev;
						}else {
							txtAddress.setBackground(CustomColor.notOkColorBackGround());
						}
					}else {
						txtContact.setBackground(CustomColor.notOkColorBackGround());
					}
				}else {
					txtLname.setBackground(CustomColor.notOkColorBackGround());
				}
			}else {
				txtMname.setBackground(CustomColor.notOkColorBackGround());
			}
		} else {
			txtFname.setBackground(CustomColor.notOkColorBackGround());
		}
		return ev;
	}

	public void setBookingEventClientListener(
			BookingEventClientListener listener) {
		this.listener = listener;
	}
	
	private void setMessageBar(String msg, int msgType) {
		lblMessage.setText(msg);
		if(msgType == 1) {
			panelMessagebar.setBackground(CustomColor.okColorBackGround());
		} else if(msgType == 0) {
			panelMessagebar.setBackground(CustomColor.notOkColorBackGround());
		} else {
			panelMessagebar.setBackground(CustomColor.bgColor());
		}
	}

	// TODO
	private void initUI() {
		prog = new ProgressbarDialog(parent, ModalityType.APPLICATION_MODAL);
		controller = new ControllerForBookingDetails();

		lblFullname = new JLabel("Full name ");
		lblContact = new JLabel("Contact no. ");
		lblGender = new JLabel("Gender");
		lblAddress = new JLabel("Address");
		lblSearch = new JLabel("Search");
		lblMessage = new JLabel("Status : ");

		Font f = CustomFont.setFont(lblFullname.getFont().toString(), Font.PLAIN, 15);
		Font fbold = CustomFont.setFont(lblFullname.getFont().toString(), Font.BOLD, 15);

		lblFullname.setFont(f);
		lblContact.setFont(f);
		lblGender.setFont(f);
		lblAddress.setFont(f);
		lblSearch.setFont(f);

		txtFname = new JTextField(20);
		txtMname = new JTextField(20);
		txtLname = new JTextField(20);
		txtAddress = new JTextField(20);
		txtSearch = new JTextField(20);
		txtContact = new JTextField(20);

		txtFname.setFont(f);
		txtMname.setFont(f);
		txtLname.setFont(f);
		txtContact.setFont(f);
		txtAddress.setFont(f);
		txtSearch.setFont(f);

		PromptSupport.setPrompt("First name", txtFname);
		PromptSupport.setPrompt("Middle name", txtMname);
		PromptSupport.setPrompt("last name", txtLname);
		// PromptSupport.setPrompt("Client's full address", txtAddress);
		// PromptSupport.setPrompt("Contact Number", txtContact);

		DefaultComboBoxModel<String> cmbmod = new DefaultComboBoxModel<String>();
		cmbmod.addElement("Male");
		cmbmod.addElement("Female");

		cboGen = new JComboBox<String>();
		cboGen.setModel(cmbmod);
		cboGen.setFont(f);

		tableModel = new ClientTableModel();
		table = new CustomTableClient(tableModel);

		panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.setBackground(Color.WHITE);
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		panelMessagebar = new JPanel();
		panelMessagebar.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelMessagebar.setBackground(CustomColor.bgColor());

		CustomIcon ci = new CustomIcon();

		btnSave = new JButton("Save", ci.createIcon("/res/save.png"));
		btnClear = new JButton("Clear", ci.createIcon("/res/clear.png"));
		btnAdd = new JButton("Add New Client",ci.createIcon("/res/add_client.png"));
		btnBook = new JButton("Book Client",ci.createIcon("/res/book_client.png"));

		btnSave.setBackground(CustomColor.bgColor());
		btnClear.setBackground(CustomColor.bgColor());
		btnAdd.setBackground(CustomColor.bgColor());
		btnBook.setBackground(CustomColor.bgColor());

		disableButtons();
	}

	// TODO
	private void layoutComponents() {

		Insets inset = new Insets(5, 5, 5, 5);

		GridBagConstraints gc = new GridBagConstraints();
		GridBagConstraints gc2 = new GridBagConstraints();

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;

		gc.gridx = 0;
		gc.gridy = 0;
		add(lblFullname, gc);

		gc.weightx = 1;
		gc.gridx = 1;
		add(txtFname, gc);

		gc.gridx = 2;
		add(txtMname, gc);

		gc.gridx = 3;
		add(txtLname, gc);

		gc.gridy++;
		gc.weightx = 0;
		gc.gridx = 0;
		add(lblAddress, gc);

		gc.gridx = 1;
		gc.gridwidth = GridBagConstraints.REMAINDER;
		add(txtAddress, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 1;
		add(lblContact, gc);

		gc.gridx = 1;
		add(txtContact, gc);

		gc.fill = GridBagConstraints.NONE;

		gc.gridy++;
		gc.gridx = 0;
		add(lblGender, gc);

		gc.gridx = 1;
		add(cboGen, gc);

		gc.fill = GridBagConstraints.BOTH;
		gc.gridy++;
		gc.gridx = 0;
		add(lblSearch, gc);

		gc.gridx = 1;
		add(txtSearch, gc);

		gc.gridy++;
		gc.weighty = 1;
		gc.weightx = 1;
		gc.gridx = 0;
		gc.gridwidth = 4;

		add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);

		panelMessagebar.add(lblMessage);

		Insets insetf = new Insets(2, 2, 2, 2);

		gc2.weightx = 0;
		gc2.weighty = 0;
		gc2.insets = insetf;
		

		gc2.gridy = 0;
		gc2.gridx = 0;
		gc2.anchor = GridBagConstraints.LINE_START;
		
		panelButton.add(btnAdd, gc2);

		gc2.gridx++;
		panelButton.add(btnSave, gc2);

		gc2.gridx++;
		panelButton.add(btnClear, gc2);

		gc2.gridx++;
		panelButton.add(btnBook, gc2);

		gc2.gridx++;
		gc2.weightx = 1;
		gc2.fill = GridBagConstraints.BOTH;
		panelButton.add(panelMessagebar, gc2);

		gc.gridy++;
		gc.weighty = 0;
		gc.weightx = 0;
		gc.gridx = 0;

		add(panelButton, gc);
	}

	// TODO
	private void set() {
		setLayout(new GridBagLayout());
		setPreferredSize(getMaximumSize());
		setBackground(CustomColor.bgColor());
		// setBorder(BorderFactory.createTitledBorder(BorderFactory
		// .createCompoundBorder(
		// BorderFactory.createEmptyBorder(10, 10, 10, 10),
		// BorderFactory.createLineBorder(Color.BLACK)),
		// "Client's Information", TitledBorder.LEFT,
		// TitledBorder.DEFAULT_POSITION, CustomFont.setFont("Rokwell",
		// Font.PLAIN, 18), Color.BLACK));
	}
}
