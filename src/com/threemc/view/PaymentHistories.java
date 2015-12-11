package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.PaymentHistory;

public class PaymentHistories extends Dialog {

	/**
	 * 
	 */
	
	private JPanel panelBot;
	private JPanel panelPack;
	
	private JTable table;
	private JLabel lblTitle;
	private JLabel lblClientInfo;
	private JLabel lblPaid;
	private JLabel lblTotal;
	
	private int client_id = 0;
	private int event_id = 0;
	private int payment_id = 0;
	
	private ControllerForPaymentDetails controllerp;
	private PaymentHistoryTableModel tableModel;

	public PaymentHistories(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Paymtent History", modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PaymentHistories.this.dispose();
			}
		});
	}

	public void loadFirstData() {
		try {
			if(controllerp.connect().equals("ok")) {
				controllerp.loadPaymentHistoryByClientEvent(client_id, event_id);
				setData(controllerp.getPaymentHistory());
				refresh();
				
				int tot = 0;
				if(controllerp.getPaymentHistory().size() != 0) {
					for (int i = 0; i < controllerp.getPaymentHistory().size(); i++) {
						PaymentHistory ph = controllerp.getPaymentHistory().get(i);
						tot = tot+ph.getPaymentPaidThisDate();
					}
				}
				lblPaid.setText("Total Paid : " + tot);
				if(payment_id != 0) {
					lblTotal.setText("Package Cost : "+controllerp.loadPaymentDataTotalCostById(payment_id));
				}
			} else {
				System.out.println(controllerp.connect());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void setData(ArrayList<PaymentHistory> db) {
		tableModel.setData(db);
	}
	
	private void refresh() {
		tableModel.fireTableDataChanged();
	}
	
	
	public void setID(int client_id, int event_id, int payment_id) {
		this.client_id = client_id;
		this.event_id = event_id;
		this.payment_id = payment_id;
	}
	
	public void setName(String text) {
		lblClientInfo.setText("Name of Client: " + text);
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		
		Insets inset = new Insets(5,5,5,5);

		gc.insets = inset;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 2;
		panelPack.add(lblClientInfo, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 1;
		panelPack.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				gc);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.weighty = 0;
		gc.gridwidth = 1;
		panelPack.add(lblPaid, gc);
		
		gc.gridx = 1;
		panelPack.add(lblTotal, gc);
		
		add(lblTitle, BorderLayout.NORTH);
		add(panelPack, BorderLayout.CENTER);
	}

	private void initUI() {
		controllerp = new ControllerForPaymentDetails();
		Font f = CustomFont.setFont("Tahoma", Font.PLAIN, 15);

		panelPack = new JPanel();
		panelPack.setLayout(new GridBagLayout());
		panelPack.setBackground(CustomColor.bgColor());
		panelPack.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		panelBot = new JPanel();
		panelBot.setLayout(new FlowLayout());
		panelBot.setBackground(CustomColor.bgColor());
		panelBot.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		lblTitle = new JLabel("Client Payment History", SwingConstants.CENTER);
		lblTitle.setFont(CustomFont.setFont("Tahoma", Font.BOLD, 20));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		tableModel = new PaymentHistoryTableModel();
		table = new JTable(tableModel);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		table.getTableHeader().setFont(f);
		table.setFont(f);
		table.setRowHeight(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(200);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(450);

		Font f2 = CustomFont.setFont("Tahoma", Font.BOLD, 15);
		
		lblClientInfo = new JLabel("Name of Client");
		lblPaid = new JLabel("Total Paid : 0");
		lblTotal = new JLabel("Package Cost : 0");
		
		lblClientInfo.setFont(f2);
		lblPaid.setFont(f2);
		lblTotal.setFont(f2);
		
		lblClientInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
	}

	private void set(final Window parent) {
		setSize(600, 450);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());
		setResizable(false);
		setBackground(CustomColor.bgColor());
	}
}