package com.threemc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.data.Client;

public class CustomTableClient extends JTable {

	private ArrayList<Client> clientList;
	private ClientTableModel model;
	private Color col = CustomColor.notOkColorBackGround();
	private Color colo = CustomColor.goldColor();
	private Font f = CustomFont.setFont("Tahoma", Font.PLAIN,15);
	private Font fbold = CustomFont.setFont("Tahoma", Font.BOLD,15);

	public CustomTableClient(ClientTableModel model) {
		super(model);
		this.model = model;

		setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);

				int rowa = table.convertRowIndexToModel(row);

				if (CustomTableClient.this.model.getValueAt(rowa, 7).equals("False")) {
					if (isSelected) {
						c.setBackground(Color.BLACK);
						c.setForeground(colo);
					} else {
						c.setBackground(col);
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
				return c;
			}
		});

		getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int col = CustomTableClient.this.columnAtPoint(e.getPoint());
				if(clientList != null) {
					if(col == 0) {
						Collections.sort(clientList, Client.CIdComp);
						setData(clientList);
						refresh();
					} else if(col == 1) {
						Collections.sort(clientList, Client.CFnameComp);
						setData(clientList);
						refresh();
					} else if(col == 2) {
						Collections.sort(clientList, Client.CMnameComp);
						setData(clientList);
						refresh();
					} else if(col == 3) {
						Collections.sort(clientList, Client.CLnameComp);
						setData(clientList);
						refresh();
					} else if(col == 4) {
						Collections.sort(clientList, Client.CAddressComp);
						setData(clientList);
						refresh();
					} else if(col == 5) {
						Collections.sort(clientList, Client.CContComp);
						setData(clientList);
						refresh();
					} else if(col == 6) {
						Collections.sort(clientList, Client.CGenComp);
						setData(clientList);
						refresh();
					}
				}
			}
		});

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);
		getTableHeader().setFont(fbold);
		setFont(f);
		setRowHeight(20);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcm = this.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(100);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(150);
		tcm.getColumn(3).setPreferredWidth(150);
		tcm.getColumn(4).setPreferredWidth(200);
		tcm.getColumn(5).setPreferredWidth(400);
		tcm.getColumn(7).setPreferredWidth(150);
	}

	protected void setClientList(ArrayList<Client> clientList) {
		this.clientList = clientList;
		setData(clientList);
		refresh();
	}

	protected ArrayList<Client> getClientList() {
		return clientList;
	}

	protected void setData(ArrayList<Client> db) {
		model.setData(db);
	}

	protected void refresh() {
		model.fireTableDataChanged();
	}
}
