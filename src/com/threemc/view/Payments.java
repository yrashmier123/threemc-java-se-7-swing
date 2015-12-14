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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.PaymentModuleData;

public class Payments extends Dialog {

	// TODO
	private JTabbedPane tabPane;
	
	private JPanel panel;
	private JPanel panelCenter;
	private JPanel panelTable;
	private JPanel panelBot;
	private JPanel panelPaid;
	private JPanel panelPaidTable;
	private JPanel panelSearchPaid;
	
	private JLabel lblMessage;
	private JLabel lblTitle;
	private JLabel lblPaid;
	private JLabel lblBalance;
	private JLabel lblAmount;
	private JLabel lblSearch;
	private JLabel lblType;
	private JLabel lblSearchPaid;

	private JTextField txtPaid;
	private JTextField txtBalance;
	private JTextField txtAmount;
	private JTextField txtSearch;
	private JTextField txtSearchPaid;
	private JComboBox<String> cboPaymentType;
	private Color col = CustomColor.goldColor();

	private JButton btnSave;
	private JButton btnShowHistory;
	private JButton btnShowHistoryPaid;
	private JButton btnSearch;
	private JButton btnUpdatePayment;

	private JComboBox<String> cboType;
	private JComboBox<String> cboTypePaid;

	private CustomTablePayment table;
	private PaymentTableModel tableModel;

	private CustomTablePayment tablePaid;
	private PaymentTableModel tableModelPaid;

	private ControllerForPaymentDetails controller;
	private ControllerForBookingDetails controllerb;
	private ArrayList<PaymentModuleData> paymentList;
	private ArrayList<PaymentModuleData> paymentListPaid;

	private String category = "c.client_firstName";
	private String categoryPaid = "c.client_firstName";
	private int amountPaid = 0;
	private int newAmount = 0;
	private int balance = 0;
	private int newBalance = 0;
	private DateFormat dateFormat;

	private JPopupMenu pop;
	private JMenu mniShow;
	private JMenuItem mniTotal;
	private JMenuItem mniTBalance;
	private JMenuItem mniTbill;

	// TODO
	public Payments(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Payments || Three McQueens Eventi Automated System",
				modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Payments.this.dispose();
				super.windowClosing(e);
			}
		});

		loadFirst();

		cboType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cboType.getSelectedItem().equals("Name of event")) {
					category = "e.event_title";
				} else if (cboType.getSelectedItem().equals("Contact Number")) {
					category = "c.client_contactNo";
				} else if (cboType.getSelectedItem().equals("Balance")) {
					category = "p.payment_balance";
				} else if (cboType.getSelectedItem().equals("First Name")) {
					category = "c.client_firstName";
				} else if (cboType.getSelectedItem().equals("Last Name")) {
					category = "c.client_lastName";
				}
			}
		});

		cboTypePaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cboTypePaid.getSelectedItem().equals("Name of event")) {
					categoryPaid = "e.event_title";
				} else if (cboTypePaid.getSelectedItem().equals("Contact Number")) {
					categoryPaid = "c.client_contactNo";
				} else if (cboTypePaid.getSelectedItem().equals("Balance")) {
					categoryPaid = "p.payment_balance";
				} else if (cboTypePaid.getSelectedItem().equals("First Name")) {
					categoryPaid = "c.client_firstName";
				} else if (cboTypePaid.getSelectedItem().equals("Last Name")) {
					categoryPaid = "c.client_lastName";
				}
			}
		});

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				searchWithBalance();
			}

			public void insertUpdate(DocumentEvent e) {
				searchWithBalance();
			}

			public void changedUpdate(DocumentEvent e) {
				searchWithBalance();
			}
		});

		txtSearchPaid.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				searchPaid();
			}
			
			public void insertUpdate(DocumentEvent e) {
				searchPaid();
			}
			
			public void changedUpdate(DocumentEvent e) {
				searchPaid();
			}
		});

		txtAmount.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}

			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void update() {
				try {
					if (!txtAmount.getText().equals("")) {
						newAmount = amountPaid
								+ Integer.parseInt(txtAmount.getText()
										.toString());
						newBalance = balance
								- Integer.parseInt(txtAmount.getText()
										.toString());
						txtPaid.setText("" + newAmount);
						txtBalance.setText("" + newBalance);
						if(newBalance == 0) {
							cboPaymentType.setSelectedIndex(3);
							txtBalance.setForeground(CustomColor.okColorBackGround());
						} else {
							txtBalance.setForeground(CustomColor.notOkColorBackGround());
						}
					} else {
						txtPaid.setText("" + amountPaid);
						txtBalance.setText("" + balance);
						if(newBalance != 0) {
							cboPaymentType.setSelectedIndex(1);
						}
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Payments.this,
							"Invalid input!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tableToTextBoxes();
				if (e.getButton() == MouseEvent.BUTTON3) {
					pop.show(table, e.getX(), e.getY());
				}
			}
		});

		tablePaid.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					pop.show(tablePaid, e.getX(), e.getY());
				}
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				tableToTextBoxes();
			}
		});

		// TODO
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(table.getSelectedRowCount() == 1) {
						String eventName = paymentList.get(table.getSelectedRow()).getEventName();
						int val = Integer.parseInt(txtAmount.getText().toString());
						String msg = "Are you sure you want to add payment to " + eventName + " \nwith an amount of Php" + val +"?";
						int aok = JOptionPane.showConfirmDialog(Payments.this, msg, "Payments", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if(aok == JOptionPane.YES_OPTION) {
							if (Integer.parseInt(txtBalance.getText()) >= 0) {
								if(controller.connect().equals("ok")) {
									controller.updatePayment(getPayment());
									controller.saveInPaymentHistory(getDetailsForPaymentHistory(getPayment()));
									if (Integer.parseInt(txtBalance.getText()) == 0) {
										int event_id = paymentList.get(table.getSelectedRow()).getEvent_id();
										if(controllerb.connect().equals("ok")) {
											controllerb.updateBookingStatusPayment(event_id, "Fully Paid");
										} else {
											System.out.println(controllerb.connect());
										}
									}
									loadFirst();
									JOptionPane
											.showMessageDialog(
													Payments.this,
													"Successfully updated Client's payment for this event",
													"Payments",
													JOptionPane.INFORMATION_MESSAGE);
									setMessage("Successfully updated Client's payment for this event", 1);
									txtAmount.setText("");
									txtBalance.setText("");
									txtPaid.setText("");
								} else {
									System.out.println(controller.connect());
								}
							} else {
								JOptionPane
								.showMessageDialog(
										Payments.this,
										"Amount paid exceeded account's balance",
										"Payments",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						JOptionPane.showMessageDialog(Payments.this,
								"Can only update 1 record at a time", "Update Failed",
								JOptionPane.ERROR_MESSAGE);
						txtAmount.setText("");
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(Payments.this,
							"Amount paid value error!", "Update Failed",
							JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
					nfe.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException aib) {
					JOptionPane.showMessageDialog(Payments.this,
							"Please select a record in the List first!",
							"Update Failed", JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
					aib.printStackTrace();
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(Payments.this,
							"Amount paid value error!", "Update Failed",
							JOptionPane.ERROR_MESSAGE);
					txtAmount.setText("");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnShowHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() == 1) {
					if(table.getSelectedRow() != -1) {
						int client_id = paymentList.get(table.getSelectedRow())
								.getClient_id();
						int event_id = paymentList.get(table.getSelectedRow())
								.getEvent_id();
						int payment_id = paymentList.get(table.getSelectedRow())
								.getPayment_id();
						String name = table.getValueAt(table.getSelectedRow(), 0).toString();
						PaymentHistories pay = new PaymentHistories(parent, modal);
						pay.setID(client_id, event_id, payment_id);
						pay.setName(name);
						pay.loadFirstData();
						pay.setVisible(true);
					} else {
						setMessage("Please select a record from the list of payments.", 0);
					}
				} else if (table.getSelectedRowCount() > 1){
					setMessage("Failed to check multiple records, Please select one at a time.", 0);
				} else {
					setMessage("None Selected!", 0);
				}
			}
		});

		btnShowHistoryPaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tablePaid.getSelectedRowCount() == 1) {
					int row = tablePaid.getSelectedRow();
					if(row != -1) {
						int client_id = paymentListPaid.get(row).getClient_id();
						int event_id = paymentListPaid.get(row).getEvent_id();
						int payment_id = paymentListPaid.get(row).getPayment_id();
						String name = tablePaid.getValueAt(row, 0).toString();
						PaymentHistories pay = new PaymentHistories(parent, modal);
						pay.setID(client_id, event_id, payment_id);
						pay.setName(name);
						pay.loadFirstData();
						pay.setVisible(true);
					} else {
						setMessage("Please select a record from the list of payments.", 0);
					}
				} else if (table.getSelectedRowCount() > 1){
					setMessage("Failed to check multiple records, Please select one at a time.", 0);
				} else {
					setMessage("None Selected!", 0);
				}
			}
		});

		btnUpdatePayment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row = table.getSelectedRow();
					if(row != -1) {
						PaymentModuleData pmd = paymentList.get(row);
						if(pmd.getPaymentBalance() != 0) {
							PaymentUpdate pu = new PaymentUpdate(parent, modal);
							pu.setPayment(pmd);
							pu.setVisible(true);
							if(controller.equals("ok")) {
								controller.loadPaymentDataWithBalance();
								paymentList = controller.getPaymentDataWithBalance();
								table.setPaymentList(paymentList);
							}
						} else {
							setMessage("Account is Fully Paid", 1);
						}
					} else {
						setMessage("None Selected", 0);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		mniTbill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(table.isShowing()) {
					System.out.println("here");
					showTotal(table , 5 , 1);
				} else if(tablePaid.isShowing()) {
					System.out.println("heres");
					showTotal(tablePaid , 5 , 0);
				}
			}
		});

		mniTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(table.isShowing()) {
					System.out.println("here");
					showTotal(table , 6 , 1);
				} else if(tablePaid.isShowing()) {
					System.out.println("heres");
					showTotal(tablePaid , 6 , 0);
				}
			}
		});

		mniTBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(table.isShowing()) {
					System.out.println("here");
					showTotal(table , 7 , 1);
				} else if(tablePaid.isShowing()) {
					System.out.println("heres");
					showTotal(tablePaid , 7 , 0);
				}
			}
		});
	}
	
	private void setMessage(String msg, int type) {
		lblMessage.setText(msg);
		if(type == 0) {
			panelBot.setBackground(CustomColor.notOkColorBackGround());
		} else if (type == 1) {
			panelBot.setBackground(CustomColor.okColorBackGround());
		} else {
			panelBot.setBackground(CustomColor.bgColor());
		}
	}

	private void loadFirst() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadPaymentDataWithBalance();
				paymentList = controller.getPaymentDataWithBalance();
				table.setPaymentList(paymentList);
				
				controller.loadPaidPaymentData();
				paymentListPaid = controller.getPaidPaymentData();
				tablePaid.setPaymentList(paymentListPaid);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showTotal(CustomTablePayment entryTable, int col, int data) {
		int row = entryTable.getSelectedRow();
		if(row != -1) {
			if(col == 5) {
				String msg = "Payment Details for Selected records.\n\nClient Name: "
						+ entryTable.getModel().getValueAt(row, 0) + ""
						+ "\n\nTotal Bill: ";
				showPaymenets(entryTable, 5, msg, data);
			} else if(col == 6) {
				String msg = "Payment Details for Selected records.\n\nClient Name: "
						+ entryTable.getModel().getValueAt(row, 0) + ""
						+ "\n\nTotal Paid: ";
				showPaymenets(entryTable, 6, msg, data);
			} else if(col == 7) {
				String msg = "Payment Details for Selected records.\n\nClient Name: "
						+ entryTable.getModel().getValueAt(row, 0) + ""
						+ "\n\nTotal Balance: ";
				showPaymenets(entryTable, 7, msg, data);
			}
		} else {
			setMessage("Please select records from the list first", 0);
		}
	}

	private void showPaymenets(CustomTablePayment entryTable, int column, String message, int data) {
		if (entryTable.getRowCount() > 0) {
			if (entryTable.getSelectedRowCount() > 0) {
				int selectedRow[] = entryTable.getSelectedRows();
				int total = 0;
				int tempid = 0;
				boolean b = true;
				
				if(data == 1) {
					tempid = controller.getPaymentDataWithBalance().get(entryTable.getSelectedRow()).getClient_id();
					for (int i : selectedRow) {
						PaymentModuleData pay = controller.getPaymentDataWithBalance().get(i);
						if (tempid != pay.getClient_id()) {
							b = false;
						}
					}
				} else if(data == 0)  {
					tempid = controller.getPaidPaymentData().get(entryTable.getSelectedRow()).getClient_id();
					for (int i : selectedRow) {
						PaymentModuleData pay = controller.getPaidPaymentData().get(i);
						if (tempid != pay.getClient_id()) {
							b = false;
						}
					}
				}
				if (b) {
					for (int i : selectedRow) {
						total = total
								+ Integer.parseInt(entryTable.getModel().getValueAt(i,
										column).toString());
					}

					JOptionPane.showMessageDialog(Payments.this, message
							+ total, "Payments",
							JOptionPane.INFORMATION_MESSAGE);
					total = 0;
				} else {
					JOptionPane
							.showMessageDialog(
									Payments.this,
									"Selected records does not match Clients to process each Event's Payment details",
									"Payments", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void tableToTextBoxes() {
		int row = table.getSelectedRow();
		if (row != -1) {
			txtPaid.setText(tableModel.getValueAt(row, 6).toString());
			txtBalance.setText(tableModel.getValueAt(row, 7).toString());
			amountPaid = Integer.parseInt(tableModel.getValueAt(row, 6).toString());
			balance = Integer.parseInt(tableModel.getValueAt(row, 7).toString());
			if (tableModel.getValueAt(row, 4).toString().equals("reservation")) {
				cboPaymentType.setSelectedIndex(0);
			} else if (tableModel.getValueAt(row, 4).toString()
					.equals("booking")) {
				cboPaymentType.setSelectedIndex(1);
			} else if (tableModel.getValueAt(row, 4).toString()
					.equals("Pre-preparation")) {
				cboPaymentType.setSelectedIndex(2);
			} else if (tableModel.getValueAt(row, 4).toString()
					.equals("finalpayment")) {
				cboPaymentType.setSelectedIndex(3);
			}

			if (balance == 0) {
				txtAmount.setEditable(false);
				btnSave.setEnabled(false);
				txtBalance.setForeground(CustomColor.okColorBackGround());
			} else {
				txtAmount.setEditable(true);
				btnSave.setEnabled(true);
				txtBalance.setForeground(CustomColor.notOkColorBackGround());
			}
			txtAmount.setText("");
		} else {
			
		}
	}

	private Payment getPayment() {
		Payment ev = null;

		PaymentModuleData pmd = paymentList.get(table.getSelectedRow());

		if (CustomPatternChecker.checkStringNumbersOnly(txtAmount.getText())) {
			int payment_id = pmd.getPayment_id();
			int client_id = pmd.getClient_id();
			int event_id = pmd.getEvent_id();
			int paymentTotal = pmd.getPaymentTotal();
			int paymentPaid = newAmount;
			CategoryPaymentType paymentType = null;
			int type = cboPaymentType.getSelectedIndex();
			if( type == 0) {
				paymentType = CategoryPaymentType.reservation;
			} else if( type == 1) {
				paymentType = CategoryPaymentType.booking;
			} else if( type == 2) {
				paymentType = CategoryPaymentType.prepreparation;
			} else if( type == 3) {
				paymentType = CategoryPaymentType.finalpayment;
			}
			
			int paymentBalance = newBalance;
			String paymentDate = pmd.getPaymentDate();

			ev = new Payment(payment_id, client_id, event_id, paymentTotal,
					paymentType, paymentPaid, paymentBalance, paymentDate);
		}

		return ev;
	}

	private PaymentHistory getDetailsForPaymentHistory(Payment pay) {

		PaymentHistory ev = null;

		PaymentModuleData pmd = paymentList.get(table.getSelectedRow());

		int payment_id = pmd.getPayment_id();
		int client_id = pmd.getClient_id();
		int event_id = pmd.getEvent_id();
		CategoryPaymentType paymentType = pmd.getPaymentType();
		int paymentPaidThisDate = Integer.parseInt(txtAmount.getText());
		String paymentDate = (String) dateFormat.format(System
				.currentTimeMillis());

		ev = new PaymentHistory(client_id, event_id, payment_id, paymentType,
				paymentPaidThisDate, paymentDate);
		ev.setPaymentDesc("Paid with the Amount as recorded");
		return ev;
	}

	private void searchWithBalance() {
		try {
			if(!txtSearch.getText().toString().equals("")) {
				if(controller.connect().equals("ok")) {
					controller.searchPaymentWithBalance(category, txtSearch.getText().toString());
					paymentList = controller.getPaymentDataWithBalance();
					table.setPaymentList(paymentList);
					if(paymentList.size() > 0) {
						setMessage("Found "+ paymentList.size() + " in Database" , 1);
					} else {
						setMessage("Found "+ paymentList.size() + " in Database" , 0);
					}
				} else {
					System.out.println(controller.connect());
				}
			} else {
				loadFirst();
				setMessage("STATUS" , 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void searchPaid() {
		try {
			if(!txtSearchPaid.getText().toString().equals("")) {
				if(controller.connect().equals("ok")) {
					controller.searchPaidPaymentData(categoryPaid, txtSearchPaid.getText().toString());
					paymentListPaid = controller.getPaidPaymentData();
					tablePaid.setPaymentList(paymentListPaid);
				} else {
					System.out.println(controller.connect());
				}
			} else {
				loadFirst();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO
	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = inset;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblPaid, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtPaid, gc);

		gc.gridx = 2;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblBalance, gc);

		gc.gridx = 3;
		gc.gridwidth = 3;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtBalance, gc);

		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblAmount, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtAmount, gc);

		gc.gridx = 2;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblType, gc);

		gc.gridx = 3;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(cboPaymentType, gc);

		gc.gridx = 4;
		panelCenter.add(btnSave, gc);
		
		gc.gridx = 5;
		panelCenter.add(btnUpdatePayment, gc);


		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblSearch, gc);

		gc.gridx = 1;
		gc.gridwidth = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		panelCenter.add(txtSearch, gc);

		gc.gridx = 2;
		// gc.insets = top;

		panelCenter.add(cboType, gc);
		
		gc.gridx = 3;

		panelCenter.add(btnShowHistory, gc);

		// gc.gridx = 3;
		// gc.gridy = 2;
		// gc.insets = inset;
		//
		// panelCenter.add(btnSearch, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 6;
		gc.fill = GridBagConstraints.BOTH;
		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		panelCenter.add(panelTable, gc);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelBot.add(lblMessage);
		
		panelCenter.add(panelBot, gc);

		panel.add(lblTitle, BorderLayout.CENTER);
		
		GridBagConstraints gx = new GridBagConstraints();
		
		panelSearchPaid.add(lblSearchPaid);
		panelSearchPaid.add(txtSearchPaid);
		panelSearchPaid.add(cboTypePaid);
		panelSearchPaid.add(btnShowHistoryPaid);
		
		gx.insets = inset;
		gx.weightx = 1;
		gx.weighty = 0;

		gx.gridy = 0;
		gx.gridx = 0;
		gx.fill = GridBagConstraints.HORIZONTAL;
		gx.anchor = GridBagConstraints.LINE_START;
		
		panelPaid.add(panelSearchPaid, gx);

		gx.gridy++;
		gx.gridx = 0;
		gx.weighty = 1;
		gx.fill = GridBagConstraints.BOTH;
		gx.anchor = GridBagConstraints.LINE_START;
		
		panelPaidTable.add(new JScrollPane(tablePaid,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		panelPaid.add(panelPaidTable, gx);
		
		add(panel, BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);
	}

	// TODO
	private void initUI() {
		controllerb = new ControllerForBookingDetails();
		controller = new ControllerForPaymentDetails();
		dateFormat = new SimpleDateFormat("MMMMM/dd/yyyy hh:mm");

		paymentList = new ArrayList<PaymentModuleData>();

		panel = new JPanel();

		panel.setPreferredSize(new Dimension(0, 100));
		panel.setBackground(Color.BLACK);
		panel.setLayout(new BorderLayout());

		panelTable = new JPanel();
		panelTable.setPreferredSize(getMaximumSize());
		panelTable.setLayout(new BorderLayout());
		
		panelBot = new JPanel();
		panelBot.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBot.setBorder(BorderFactory.createEtchedBorder());
		panelBot.setBackground(CustomColor.bgColor());

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
//		panelCenter.setSize(getMaximumSize());
		panelCenter.setBackground(CustomColor.bgColor());
		
		panelPaid = new JPanel();
		panelPaid.setLayout(new GridBagLayout());
		panelPaid.setBackground(CustomColor.bgColor());
		
		panelPaidTable = new JPanel();
		panelPaidTable.setLayout(new BorderLayout());
		
		panelSearchPaid = new JPanel();
		panelSearchPaid.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSearchPaid.setBorder(BorderFactory.createEtchedBorder());
		panelSearchPaid.setBackground(CustomColor.bgColor());
		
		tabPane = new JTabbedPane();
		tabPane.setFont(new Font("Tahoma", Font.BOLD, 16));
		tabPane.add("Balance sheet", panelCenter);
		tabPane.add("Payment Record List", panelPaid);

		tableModel = new PaymentTableModel();
		table = new CustomTablePayment(tableModel);
		
		tableModelPaid = new PaymentTableModel();
		tablePaid = new CustomTablePayment(tableModelPaid);
		
		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);
		Font af = CustomFont.setFont("Tahoma", Font.BOLD, 15);

		CustomIcon ci = new CustomIcon();
		lblTitle = new JLabel("Payment");
		lblPaid = new JLabel("Amount Paid ");
		lblBalance = new JLabel("Balance ");
		lblAmount = new JLabel("Enter Amount ");
		lblSearch = new JLabel("Search: ");
		lblType = new JLabel("Type");
		lblSearchPaid = new JLabel("Search: ");
		lblMessage = new JLabel("STATUS ");

		lblPaid.setFont(af);
		lblBalance.setFont(af);
		lblAmount.setFont(f);
		lblSearch.setFont(f);
		lblType.setFont(f);
		lblMessage.setFont(f);
		lblSearchPaid.setFont(f);
		
		lblTitle.setIcon(ci.createIcon("/res/paymenttop.png"));
//		txtTotalBill.setHorizontalAlignment(JTextField.RIGHT);
//		txtTotalBill.setEditable(false);
//		txtTotalBill.setForeground(CustomColor.okColorBackGround());

		txtPaid = new JTextField(20);
		txtPaid.setFont(af);
		txtPaid.setEditable(false);
		txtPaid.setHorizontalAlignment(JTextField.RIGHT);
		txtPaid.setForeground(CustomColor.okColorBackGround());
		txtPaid.setBackground(Color.BLACK);

		txtBalance = new JTextField(20);
		txtBalance.setFont(af);
		txtBalance.setEditable(false);
		txtBalance.setBackground(Color.BLACK);
		txtBalance.setForeground(CustomColor.notOkColorBackGround());
		txtBalance.setHorizontalAlignment(JTextField.RIGHT);

		txtAmount = new JTextField(20);
		txtAmount.setFont(f);
		txtAmount.setHorizontalAlignment(JTextField.RIGHT);

		txtSearch = new JTextField(20);
		txtSearch.setFont(f);
		
		txtSearchPaid = new JTextField(30);
		txtSearchPaid.setFont(f);

		btnSave = new JButton("Save");
		btnShowHistory = new JButton("Show History");
		btnShowHistoryPaid = new JButton("Show History");
		btnSearch = new JButton("Search");
		btnUpdatePayment = new JButton("Change Payment Info");
		
		btnSave.setFont(CustomFont.setFontTahomaBold());
		btnShowHistory.setFont(f);
		btnSearch.setFont(f);
		btnUpdatePayment.setFont(f);
		
		btnSave.setBackground(CustomColor.bgColor());
		btnShowHistory.setBackground(CustomColor.bgColor());
		btnSearch.setBackground(CustomColor.bgColor());

		cboType = new JComboBox<String>();
		cboType.setBackground(CustomColor.bgColor());

		DefaultComboBoxModel<String> df = new DefaultComboBoxModel<String>();
		df.addElement("First Name");
		df.addElement("Last Name");
		df.addElement("Name of event");
		df.addElement("Contact Number");

		cboType.setModel(df);
		cboType.setFont(f);
		
		cboTypePaid = new JComboBox<String>();
		cboTypePaid.setBackground(CustomColor.bgColor());

		DefaultComboBoxModel<String> dfs = new DefaultComboBoxModel<String>();
		dfs.addElement("First Name");
		dfs.addElement("Last Name");
		dfs.addElement("Name of event");
		dfs.addElement("Contact Number");

		cboTypePaid.setModel(dfs);
		cboTypePaid.setFont(f);

		cboPaymentType = new JComboBox<String>();

		DefaultComboBoxModel<String> fdf = new DefaultComboBoxModel<String>();
		fdf.addElement("Reservation fee");
		fdf.addElement("Booking fee");
		fdf.addElement("Pre-preparation fee");
		fdf.addElement("final payment");

		cboPaymentType.setModel(fdf);
		cboPaymentType.setFont(f);

		mniTotal = new JMenuItem("Total Paid");
		mniTBalance = new JMenuItem("Total Balance");
		mniTbill = new JMenuItem("Total Bill");
		mniShow = new JMenu("Show selected client record for");
		mniShow.add(mniTBalance);
		mniShow.add(mniTotal);
		mniShow.add(mniTbill);
		pop = new JPopupMenu();
		pop.add(mniShow);
	}

	private void set(final Window parent) {
		setResizable(false);
		setLayout(new BorderLayout());
		setSize(1100, 700);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
