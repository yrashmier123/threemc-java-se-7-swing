package com.threemc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.threemc.data.Service;

public class CustomTableServices extends JTable{

	private ArrayList<Service> serviceList;
	private BookingPackagesForServicesTableModel model;
	private Color col = CustomColor.notOkColorBackGround();
	private Color colo = CustomColor.goldColor();
	private Font f = CustomFont.setFont("Tahoma", Font.PLAIN,15);
	private Font fbold = CustomFont.setFont("Tahoma", Font.BOLD,15);

	public CustomTableServices(BookingPackagesForServicesTableModel model) {
		super(model);
		this.model = model;

		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				if (isSelected) {
					c.setBackground(Color.BLACK);
					c.setForeground(colo);
				} else {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}
				return c;
			}
		});

		getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int col = CustomTableServices.this.columnAtPoint(e.getPoint());
				if(serviceList != null) {
					if(col == 0) {
						Collections.sort(serviceList, Service.SnameComp);
						setData(serviceList);
						refresh();
					} else if(col == 1) {
						Collections.sort(serviceList, Service.ScatComp);
						setData(serviceList);
						refresh();
					} else if(col == 2) {
						Collections.sort(serviceList, Service.SpriceComp);
						setData(serviceList);
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

		TableColumnModel tcm = getColumnModel();
		tcm.getColumn(0).setPreferredWidth(200);
		tcm.getColumn(1).setPreferredWidth(130);
		tcm.getColumn(2).setPreferredWidth(150);
		tcm.getColumn(3).setPreferredWidth(400);
	}

	protected void setServiceList(ArrayList<Service> serviceList) {
		this.serviceList = serviceList;
		setData(serviceList);
		refresh();
	}

	protected ArrayList<Service> getServiceList() {
		return serviceList;
	}

	protected void setData(ArrayList<Service> db) {
		model.setData(db);
	}

	protected void refresh() {
		model.fireTableDataChanged();
	}
}
