package com.threemc.view;

import java.awt.Color;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Notice;

public class Notices extends Dialog {

	private JPanel panelButton;

	private JTextArea txtNotice;

	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnClear;

	private SimpleDateFormat dateFormat = new SimpleDateFormat(" MMMMM dd , yyyy - EEEEE");

	private ControllerForBookingDetails controllerb;
	private Notice not;

	public Notices(final Window parent , final ModalityType modal) {
		super(parent, "Notices" , modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				Notices.this.dispose();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String date = dateFormat.format(new Date(System.currentTimeMillis()));
				String desc = txtNotice.getText();
				Notice not = new Notice(date, desc);
				try {
					if(controllerb.connect().equals("ok")) {
						controllerb.saveNotice(not);
						loadFirstNotice();
						btnSave.setEnabled(false);
						btnClear.setEnabled(false);
						btnUpdate.setEnabled(true);
						txtNotice.setEditable(false);
						JOptionPane.showMessageDialog(Notices.this, "Successfully saved Notice Board Message!", "Notice Board", JOptionPane.INFORMATION_MESSAGE);
					} else {
						System.out.println(controllerb.connect());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSave.setEnabled(true);
				btnClear.setEnabled(true);
				btnUpdate.setEnabled(false);
				txtNotice.setEditable(true);
				txtNotice.setBackground(Color.WHITE);
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNotice.setText("");
			}
		});
	}

	private void loadFirstNotice() {
		try {
			if(controllerb.connect().equals("ok")) {
				not = controllerb.loadLastNotice();
				if(not != null) {
					StringBuffer msg = new StringBuffer();
					msg.append(not.getDesc());
					txtNotice.setText(msg.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5, 5, 5, 5);

		gc.weighty = 1;
		gc.weightx = 1;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;

		add(txtNotice, gc);

		gc.weighty = 0;

		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelButton.add(btnSave);
		panelButton.add(btnUpdate);
		panelButton.add(btnClear);

		add(panelButton, gc);
	}

	private void initUI() {
		controllerb = new ControllerForBookingDetails();
		Font f = CustomFont.setFontTahomaPlain();

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		txtNotice = new JTextArea(30,30);
		txtNotice.setBackground(CustomColor.bgColor());
		txtNotice.setEditable(false);
		txtNotice.setFont(f);

		btnSave = new JButton("Save");
		btnUpdate = new JButton("Update");
		btnClear = new JButton("Clear");
		
		btnSave.setEnabled(false);
		btnClear.setEnabled(false);
		btnUpdate.setEnabled(true);
		
		String msg = "This is the Notice Board.\n\nYou can input reminders,notices or any "
				+ "messages you want to pass\non to the staff that uses the\nThree McQueens "
				+ "Eventi System for Staff/Employee";
		txtNotice.setText(msg);

		loadFirstNotice();
	}

	private void set(final Window parent) {
		setResizable(false);
		setLayout(new GridBagLayout());
		setSize(800, 700);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
//		setBackground(CustomColor.bgColor());
	}
}
