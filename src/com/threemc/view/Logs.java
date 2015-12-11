package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForLogs;
import com.threemc.data.Log;
import com.threemc.model.DatabaseLogs;

public class Logs extends Dialog {

	private JPanel panelTitle;
	private JPanel panelCenter;
	private JPanel panelTable;
	private JLabel lblTitle;
	private JButton btnRefresh;
	private JTextField txtSearch;
	private JTable table;
	private LogTableModel tableModel;

	private ControllerForLogs controller;

	public Logs(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Logs || Three McQueens Eventi Automated System",
				modal);

		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Logs.this.dispose();
				super.windowClosing(e);
			}
		});

		loadDataFirst();

		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				searchData();
			}
			public void insertUpdate(DocumentEvent e) {
				searchData();
			}
			public void changedUpdate(DocumentEvent e) {
				searchData();
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataFirst();
			}
		});
	}
	
	private void searchData() {
		try {
			if(controller.connect().equals("ok")) {
				controller.searchLogs(txtSearch.getText());
				setData(controller.getLog());
				refresh();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDataFirst() {
		try {
			if(controller.connect().equals("ok")) {
				controller.loadLogs();
				setData(controller.getLog());
				refresh();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setData(ArrayList<Log> db) {
		tableModel.setData(db);
	}
	
	private void refresh() {
		tableModel.fireTableDataChanged();
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;
		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.gridx = 0;
		gc.gridy = 0;
		panelCenter.add(txtSearch, gc);

		gc.gridx = 1;
		gc.gridy = 0;

		panelCenter.add(btnRefresh, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridwidth = 2;

		panelTable.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		panelCenter.add(panelTable, gc);

		panelTitle.add(lblTitle, BorderLayout.CENTER);
		add(panelTitle, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
	}

	private void initUI() {
		controller = new ControllerForLogs();

		Color myColor = Color.decode("#FFD700");
		Font f = CustomFont.setFontRockwellPlain();

		panelTitle = new JPanel();
		panelTitle.setLayout(new BorderLayout());
		panelTitle.setPreferredSize(new Dimension(0, 100));
		panelTitle.setBackground(Color.BLACK);

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setPreferredSize(getMaximumSize());

		panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout());
		panelTable.setPreferredSize(getMaximumSize());

		lblTitle = new JLabel("Log");
		CustomIcon ci = new CustomIcon();
		lblTitle.setIcon(ci.createIcon("/res/logstop.png"));

		tableModel = new LogTableModel();
		table = new JTable(tableModel);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false); 
		table.getTableHeader().setResizingAllowed(false);
		table.setRowHeight(20);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(100);
		tcm.getColumn(1).setPreferredWidth(200);
		tcm.getColumn(2).setPreferredWidth(600);
		tcm.getColumn(3).setPreferredWidth(200);

		txtSearch = new JTextField(30);
		txtSearch.setFont(f);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(f);
	}

	private void set(final Window parent) {
		setResizable(false);
		setLayout(new BorderLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
	}
}
