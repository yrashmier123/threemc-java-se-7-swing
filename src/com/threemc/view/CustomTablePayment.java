package com.threemc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.data.Client;
import com.threemc.data.PaymentModuleData;

public class CustomTablePayment extends JTable {

	private ArrayList<PaymentModuleData> paymentList;
	private PaymentTableModel model;
	private Color col = CustomColor.bgColor();
	private Color colo = CustomColor.goldColor();
	private Font f = CustomFont.setFontTahomaPlain();
	private Font fbold = CustomFont.setFontTahomaBold();

	public CustomTablePayment(PaymentTableModel model) {
		super(model);
		this.model = model;

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);
		getTableHeader().setFont(fbold);
		setFont(f);
		setRowHeight(20);

		TableColumnModel tcm = this.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(250);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(400);
		tcm.getColumn(3).setPreferredWidth(190);
		tcm.getColumn(4).setPreferredWidth(150);
		tcm.getColumn(5).setPreferredWidth(150);
		tcm.getColumn(6).setPreferredWidth(150);
	}

	protected void setPaymentList(ArrayList<PaymentModuleData> paymentList) {
		this.paymentList = paymentList;
		setData(paymentList);
		refresh();
	}

	protected ArrayList<PaymentModuleData> getPaymentList() {
		return paymentList;
	}

	protected void setData(ArrayList<PaymentModuleData> db) {
		model.setData(db);
	}

	protected void refresh() {
		model.fireTableDataChanged();
	}
}
