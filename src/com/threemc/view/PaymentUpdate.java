package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
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
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.PaymentModuleData;

public class PaymentUpdate extends JDialog {

	private JPanel panelCenter;
	private JPanel panelButton;

	private JLabel lblTotal;
	private JLabel lblPaid;
	private JLabel lblBalance;
	private JLabel lblType;

	private JTextField txtTotal;
	private JTextField txtPaid;
	private JTextField txtBalance;

	private JButton btnSave;
	private JButton btnCancel;
	
	private JComboBox<String> cboPaymentType;

	private ControllerForPaymentDetails controllerp;
	
	private DateFormat dateFormat;
	
	private int total = 0;
	private int balance = 0;
	private PaymentModuleData pmd;

	public PaymentUpdate(final Window parent, Dialog.ModalityType modal) {
		super(parent , "" , modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PaymentUpdate.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PaymentUpdate.this.dispose();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!txtTotal.getText().isEmpty()) {
						if(CustomPatternChecker.checkStringLettersWithNumbers(txtTotal.getText())) {
							if(!txtPaid.getText().isEmpty()) {
								if(CustomPatternChecker.checkStringLettersWithNumbers(txtPaid.getText())) {
									if(controllerp.connect().equals("ok")) {
										controllerp.updatePayment(getPayment());
										controllerp.saveInPaymentHistory(getDetailsForPaymentHistory(getPayment()));
										JOptionPane.showMessageDialog(PaymentUpdate.this, 
												"Successfully Updated Payment Record" , 
												"Payment - Update" , JOptionPane.INFORMATION_MESSAGE);
										PaymentUpdate.this.dispose();
									}
								}
							}
						}
					}
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(PaymentUpdate.this, "Invalid Input" , "Payment - Update" , JOptionPane.ERROR_MESSAGE);
				} catch (Exception es) {
					JOptionPane.showMessageDialog(PaymentUpdate.this, es.getMessage() , "Payment - Update" , JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		txtTotal.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				updateBal();
			}

			public void insertUpdate(DocumentEvent arg0) {
				updateBal();
			}

			public void changedUpdate(DocumentEvent arg0) {
				updateBal();
			}
		});

		txtPaid.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent arg0) {
				updateBal();
			}

			public void insertUpdate(DocumentEvent arg0) {
				updateBal();
			}

			public void changedUpdate(DocumentEvent arg0) {
				updateBal();
			}
		});
	}

	private Payment getPayment() {
		Payment ev = null;

		int payment_id = pmd.getPayment_id();
		int client_id = pmd.getClient_id();
		int event_id = pmd.getEvent_id();
		
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

		int paymentTotal = Integer.parseInt(txtTotal.getText());
		int paymentPaid = Integer.parseInt(txtPaid.getText());
		int paymentBalance = Integer.parseInt(txtBalance.getText());
		
		String paymentDate = pmd.getPaymentDate();

		ev = new Payment(payment_id, client_id, event_id, paymentTotal,
				paymentType, paymentPaid, paymentBalance, paymentDate);

		return ev;
	}
	
	private PaymentHistory getDetailsForPaymentHistory(Payment pay) {

		PaymentHistory ev = null;

		int payment_id = pay.getId();
		int client_id = pay.getClient_id();
		int event_id = pay.getEvent_id();
		CategoryPaymentType paymentType = pay.getPaymentType();
		int paymentPaidThisDate = Integer.parseInt(txtPaid.getText()) - pmd.getPaymentPaid();
		String paymentDate = (String) dateFormat.format(System
				.currentTimeMillis());

		ev = new PaymentHistory(client_id, event_id, payment_id, paymentType,
				paymentPaidThisDate, paymentDate);
		if(pmd.getPaymentTotal() != Integer.parseInt(txtTotal.getText())) {
			ev.setPaymentDesc("Total bill changed from Php" + pmd.getPaymentTotal() + " to Php"+ Integer.parseInt(txtTotal.getText()));
		} else {
			ev.setPaymentDesc("Paid with the Amount as recorded");
		}
		return ev;
	}

	private void updateBal() {
		try {
			balance = 0;
			total = Integer.parseInt(txtTotal.getText());
			balance = total - Integer.parseInt(txtPaid.getText());
			txtBalance.setText(""+balance);
		} catch (NumberFormatException e) {
		} catch (Exception es) {
		}
	}

	public void setPayment(PaymentModuleData pmd) {
		txtTotal.setText(""+pmd.getPaymentTotal());
		txtPaid.setText(""+pmd.getPaymentPaid());
		txtBalance.setText(""+pmd.getPaymentBalance());
		this.pmd = pmd;
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5,5,5,5);

		gc.weighty = 1;
		gc.insets = inset;
		

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		panelCenter.add(lblTotal , gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelCenter.add(txtTotal , gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelCenter.add(lblPaid , gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelCenter.add(txtPaid , gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelCenter.add(lblBalance , gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelCenter.add(txtBalance , gc);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		panelCenter.add(lblType , gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;
		panelCenter.add(cboPaymentType , gc);

		panelButton.add(btnSave);
		panelButton.add(btnCancel);
		
		gc.gridy++;
		gc.gridx = 1;
		panelCenter.add(panelButton , gc);

		add(panelCenter, BorderLayout.CENTER);
	}
	
	private void initUI() {
		controllerp = new ControllerForPaymentDetails();
		Font f = CustomFont.setFontTahomaPlain();
		Font f2 = CustomFont.setFontTahomaBold();
		dateFormat = new SimpleDateFormat("MMMMM/dd/yyyy hh:mm");

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBackground(CustomColor.bgColor());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());

		lblTotal = new JLabel("Payment Total: ");
		lblPaid = new JLabel("Total Paid: ");
		lblBalance = new JLabel("Balance: ");
		lblType = new JLabel("Payment Type: ");

		lblTotal.setFont(f);
		lblPaid.setFont(f);
		lblBalance.setFont(f);
		lblType.setFont(f);

		txtTotal = new JTextField(20);
		txtTotal.setFont(f2);
		txtTotal.setHorizontalAlignment(JTextField.RIGHT);

		txtPaid = new JTextField(20);
		txtPaid.setFont(f2);
		txtPaid.setHorizontalAlignment(JTextField.RIGHT);

		txtBalance = new JTextField(20);
		txtBalance.setFont(f2);
		txtBalance.setHorizontalAlignment(JTextField.RIGHT);
		txtBalance.setEditable(false);

		btnSave = new JButton("Save");
		btnCancel = new JButton("Cancel");

		btnSave.setFont(f);
		btnCancel.setFont(f);

		cboPaymentType = new JComboBox<String>();
		DefaultComboBoxModel<String> fdf = new DefaultComboBoxModel<String>();
		fdf.addElement("Reservation fee");
		fdf.addElement("Booking fee");
		fdf.addElement("Pre-preparation fee");
		fdf.addElement("final payment");

		cboPaymentType.setModel(fdf);
		cboPaymentType.setFont(f);
	}

	private void set(final Window parent) {
		setResizable(false);
		setSize(400, 230);
		setLayout(new BorderLayout());
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
