package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.threemc.controller.ControllerForPackageDetails;
import com.threemc.data.Package;
import com.threemc.data.Service;

public class PackagesCopy extends Dialog {
	
	
	private JPanel panelBut;
	private JPanel panelContainer;
	private JPanel panelCenter;
	
	private JLabel lblPackage;

	private ControllerForPackageDetails controller;
	private ArrayList<Package> packageList;
	private ArrayList<Service> serviceList;
	private Package pack;
	
	private JComboBox<String> cboPackage;
	private JButton btnOk;
	private JButton btnCancel;
	
	private CustomTableServices table;
	private BookingPackagesForServicesTableModel tableModel;
	
	public PackagesCopy(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Copy Services from Existing", modal);
		
		set(parent);
		initUI();
		layoutComponent();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				PackagesCopy.this.dispose();
			}
		});

		cboPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serviceList = packageList.get(cboPackage.getSelectedIndex()).getPackageServices();
				for (Service service : serviceList) {
					service.setId(0);
				}
				table.setServiceList(serviceList);
			}
		});

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(controller.connect().equals("ok")) {
						controller.addPackage(getPackageEventForm());
						controller.savePackage();
						JOptionPane.showMessageDialog(PackagesCopy.this, "Successfully saved services Information", "Package - Duplicate", JOptionPane.INFORMATION_MESSAGE);
						PackagesCopy.this.dispose();
					} else {
						System.out.println(controller.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PackagesEventForm getPackageEventForm() {
		PackagesEventForm ev = null;
		
		String pname = pack.getPackageName();
		String pdesc = pack.getPackageDesc();
		int id = pack.getId();
		
		int tots = 0;
		for (Service service : serviceList) {
			tots = tots + service.getServiceCost();
		}
		
		ev = new PackagesEventForm(this, id, pname, pdesc, tots, serviceList);
		
		return ev;
	}

	private void layoutComponent() {
		panelContainer.add(lblPackage, BorderLayout.NORTH);
		panelCenter.add(cboPackage, BorderLayout.NORTH);
		panelCenter.add(new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		panelContainer.add(panelCenter, BorderLayout.CENTER);
		panelBut.add(btnOk);
		panelBut.add(btnCancel);
		panelContainer.add(panelBut, BorderLayout.SOUTH);
		
		add(panelContainer, BorderLayout.CENTER);
	}

	private void initUI() {
		controller = new ControllerForPackageDetails();
		
		lblPackage = new JLabel("Select Package", SwingConstants.CENTER);
		
		Font f = CustomFont.setFont(lblPackage.getFont().toString(), Font.BOLD, 20);
		Font f2 = CustomFont.setFont(lblPackage.getFont().toString(), Font.PLAIN, 15);
		
		lblPackage.setFont(f);
		lblPackage.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		tableModel = new BookingPackagesForServicesTableModel();
		table = new CustomTableServices(tableModel);
		
		btnOk = new JButton("Save");
		btnCancel = new JButton("Cancel");
		
		panelBut = new JPanel();
		panelBut.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBut.setBackground(CustomColor.bgColor());
		
		panelContainer = new JPanel();
		panelContainer.setLayout(new BorderLayout());
		panelContainer.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
		panelContainer.setBackground(CustomColor.bgColor());
		
		panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBackground(CustomColor.bgColor());
		
		try {
			if(controller.connect().equals("ok")) {
				controller.loadAllPackageRecordsWithServices();
				packageList = controller.getPackagesWithServices();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cboPackage = new JComboBox<String>();

		DefaultComboBoxModel<String> ee = new DefaultComboBoxModel<String>();
		for (Package pack : packageList) {
			if(pack.getHasServices().equals("True")) {
				ee.addElement(pack.getPackageName());
			}
		}
		
		cboPackage.setModel(ee);
		if(packageList.size() != 0 ) {
			cboPackage.setSelectedIndex(0);
		}
		cboPackage.setFont(f2);
	}

	private void set(final Window parent) {
		setSize(520, 430);
		setLocationRelativeTo(parent);
		setResizable(false);
		setLayout(new BorderLayout());
		setBackground(CustomColor.bgColor());
	}
	
	public void setPackage(Package pack) {
		this.pack = pack;
	}

}
