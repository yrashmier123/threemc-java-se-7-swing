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

import com.threemc.data.Booking;

public class CustomTableEvent extends JTable {

	private ArrayList<Booking> bookingList;
	private BookingEventInfoTableModel model;
	private Color col = CustomColor.notOkColorBackGround();
	private Color colo = CustomColor.goldColor();

	public CustomTableEvent(BookingEventInfoTableModel model) {
		super(model);
		this.model = model;

		Font f = CustomFont.setFont("Tahoma", Font.PLAIN,15);
		Font f3 = CustomFont.setFont("Tahoma", Font.BOLD,15);

		getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int col = CustomTableEvent.this.columnAtPoint(e.getPoint());
				if(bookingList != null) {
					if(col == 0) {
						Collections.sort(bookingList, Booking.BEventName);
						setData(bookingList);
						refresh();
					} else if(col == 1) {
						Collections.sort(bookingList, Booking.BEventVenue);
						setData(bookingList);
						refresh();
					} else if(col == 2) {
						Collections.sort(bookingList, Booking.BEventType);
						setData(bookingList);
						refresh();
					} else if(col == 3) {
						Collections.sort(bookingList, Booking.BEventDate);
						setData(bookingList);
						refresh();
					} else if(col == 4) {
						Collections.sort(bookingList, Booking.BEventTime);
						setData(bookingList);
						refresh();
					} else if(col == 5) {
						Collections.sort(bookingList, Booking.BEventGuestNumber);
						setData(bookingList);
						refresh();
					} else if(col == 6) {
						Collections.sort(bookingList, Booking.BEventDetails);
						setData(bookingList);
						refresh();
					}
				}
			}
		});

		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component c = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				if (CustomTableEvent.this.model.getValueAt(row, 7).equals("False")) {
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

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(true);
		getTableHeader().setFont(f3);
		setFont(f);
		setRowHeight(20);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		TableColumnModel tcm = this.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(400);
		tcm.getColumn(1).setPreferredWidth(400);
		tcm.getColumn(2).setPreferredWidth(100);
		tcm.getColumn(3).setPreferredWidth(300);
		tcm.getColumn(4).setPreferredWidth(100);
		tcm.getColumn(5).setPreferredWidth(130);
		tcm.getColumn(6).setPreferredWidth(400);
		tcm.getColumn(7).setPreferredWidth(150);
	}

	protected void setBookingList(ArrayList<Booking> bookingList) {
		this.bookingList = bookingList;
		setData(bookingList);
		refresh();
	}

	protected ArrayList<Booking> getBookingList() {
		return bookingList;
	}

	protected void setData(ArrayList<Booking> db) {
		model.setData(db);
	}

	protected void refresh() {
		model.fireTableDataChanged();
	}
}
