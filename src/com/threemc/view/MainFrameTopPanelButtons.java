package com.threemc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.threemc.data.Admin;

public class MainFrameTopPanelButtons extends JToolBar implements ActionListener {
	// Date Started: 06/26/2015

	// Container for top panel buttons
	private JPanel panel;

	// Buttons for top panel
	private JButton btnNewBooking;
	private JButton btnClientList;
	private JButton btnCalculator;
	private JButton btnBookings;
	private JButton btnAccounts;
	private JButton btnPackages;
	private JButton btnPayment;
	private JButton btnOutputs;
	private JButton btnReports;
	private JButton btnProfiling;
	private JButton btnLogs;

	private ListenerButtonTopPanel listener;
	private ImageIcon ic;
	private Color myColor = Color.decode("#FFD700");
	private Dimension dim = getPreferredSize();
	private Dimension dim2 = getPreferredSize();

	public MainFrameTopPanelButtons(String headerText, int headerHeight,
			int fontSize) {
		set();
		initUI();
		layoutComponents();

		btnClientList.setMnemonic(KeyEvent.VK_1);
		btnNewBooking.setMnemonic(KeyEvent.VK_2);
		btnBookings.setMnemonic(KeyEvent.VK_3);
		btnPackages.setMnemonic(KeyEvent.VK_4);
		btnPayment.setMnemonic(KeyEvent.VK_5);
		btnOutputs.setMnemonic(KeyEvent.VK_6);
		btnReports.setMnemonic(KeyEvent.VK_7);
		btnProfiling.setMnemonic(KeyEvent.VK_8);
	}

	private void layoutComponents() {
		add(btnClientList);
		add(btnNewBooking);
		add(btnBookings);
		add(btnPackages);
		add(btnPayment);
		addSeparator();
//		add(btnCalculator);
		add(btnOutputs);
		add(btnReports);
		addSeparator();
		add(btnProfiling);
		add(btnLogs);
		add(btnAccounts);
	}

	private void initUI() {
		panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				if (!isOpaque()) {
					super.paintComponent(g);
					return;
				}
				// here
				Graphics2D g2d = (Graphics2D) g;
				int w = getWidth();
				int h = getHeight();
				Color color2 = getBackground();
				Color color1 = color2.darker();
				// Paint a gradient from top to bottom
				GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);

				g2d.setPaint(gp);
				g2d.fillRect(0, 0, w, h);

				setOpaque(false);
				super.paintComponent(g);
				setOpaque(true);
			}
		};
		// panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setLayout(new GridLayout());
//		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setOpaque(true);

		CustomIcon ci = new CustomIcon();

		btnBookings = new MyButton("Booking List" , ci.createIcon("/res/book.png"));
		btnNewBooking = new MyButton("New Booking" , ci.createIcon("/res/book_edit.png"));
		btnClientList = new MyButton("Client List" , ci.createIcon("/res/user_male_olive_green.png"));
		btnPayment = new MyButton("Payments", ci.createIcon("/res/payment.png"));
		btnPackages = new MyButton("Packages",ci.createIcon("/res/box_present.png"));
		btnCalculator = new MyButton("Calculator",ci.createIcon("/res/calc.png"));
		btnOutputs = new MyButton("Outputs",ci.createIcon("/res/documents.png"));
		btnReports = new MyButton("Reports",ci.createIcon("/res/copy.png"));
		btnLogs = new MyButton("Logs",ci.createIcon("/res/log_in.png"));
		btnAccounts = new MyButton("Account",ci.createIcon("/res/applications_system.png"));
		btnProfiling = new MyButton("Employee",ci.createIcon("/res/profile.png"));

		btnBookings.setToolTipText("Contains List and Manages events  and services per event");
		btnNewBooking.setToolTipText("Add new Client, Bookings, Package and Services, and Initial Payment");
		btnClientList.setToolTipText("List of Clients");
		btnPayment.setToolTipText("Update Payments for Events");
		btnPackages.setToolTipText("Manages Package and Services Information");

		btnCalculator.setEnabled(false);
	}

	private void set() {
//		setLayout(new BorderLayout());
		// setBackground(Color.WHITE);
		dim.height = 100;
		dim.width = 100;
		dim2.height = 100;
		setPreferredSize(dim2);
//		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		// setBackground(myColor);
		setBackground(CustomColor.bgColor());
	}

	public void setListenerButtonTopPanel(ListenerButtonTopPanel listener) {
		this.listener = listener;
	}

	public void actionPerformed(ActionEvent e) {
		JButton click = (JButton) e.getSource();
		if (click == btnNewBooking) {
			if (listener != null) {
				listener.openBookingAction();
			}
		} else if (click == btnClientList) {
			if (listener != null) {
				listener.openClientAction();
			}
		} else if (click == btnPayment) {
			if (listener != null) {
				listener.openPaymentAction();
			}
		} else if (click == btnPackages) {
			if (listener != null) {
				listener.openPackagesAction();
			}
		} else if (click == btnPackages) {
			if (listener != null) {
				listener.openPackagesAction();
			}
		} else if (click == btnCalculator) {
			if (listener != null) {
				listener.openCalculatorAction();
			}
		} else if (click == btnOutputs) {
			if (listener != null) {
				listener.openOutputsAction();
			}
		} else if (click == btnReports) {
			if (listener != null) {
				listener.openReportsAction();
			}
		} else if (click == btnLogs) {
			if (listener != null) {
				listener.openLogsAction();
			}
		} else if (click == btnBookings) {
			if (listener != null) {
				listener.openBookingListAction();
			}
		} else if (click == btnAccounts) {
			if (listener != null) {
				listener.openSettingAction();
			}
		} else if (click == btnProfiling) {
			if (listener != null) {
				listener.openProfilingAction();
			}
		}
	}

	class MyButton extends JButton {
		public MyButton(String txt , ImageIcon ic) {
			super(txt, ic);
			setFont(CustomFont.setFont(this.getFont().toString(), Font.PLAIN, 15));
			setMaximumSize(dim);
			setMaximumSize(dim);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setBackground(CustomColor.bgColor());
			addActionListener(MainFrameTopPanelButtons.this);
		}

		public MyButton(String txt) {
			super(txt);
			setFont(CustomFont.setFont(this.getFont().toString(), Font.PLAIN, 15));
			setMaximumSize(dim);
			setMaximumSize(dim);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setBackground(CustomColor.bgColor());
			addActionListener(MainFrameTopPanelButtons.this);
		}
	}
}
