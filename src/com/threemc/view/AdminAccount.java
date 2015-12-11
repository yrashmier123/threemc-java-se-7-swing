package com.threemc.view;

import java.awt.Dialog;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.threemc.data.Admin;
import com.threemc.events.AdminAccountListener;

public class AdminAccount extends Dialog {

	private JButton btnLogout;
	private JButton btnChange;

	private Admin admin;

	private AdminAccountListener listener;

	public AdminAccount(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Account" , modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				AdminAccount.this.dispose();
			}
		});

		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(listener != null) {
					listener.logOutAdmin();
					AdminAccount.this.setVisible(false);
				}
			}
		});

		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminChangeAccount ac = new AdminChangeAccount(parent, modal);
				ac.setAdmin(admin);
				ac.setVisible(true);
			}
		});
	}

	public void setAdmin(Admin user) {
		this.admin = user;
	}

	public void setAdminnAccountListener(AdminAccountListener listener) {
		this.listener = listener;
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();

		Insets inset = new Insets(5, 5, 5, 5);

		gc.weightx = 1;
		gc.weighty = 0;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(btnChange, gc);

		gc.gridy++;
		gc.gridx = 0;

		add(btnLogout, gc);
	}

	private void initUI() {
		Font f = CustomFont.setFont("Tahoma", Font.BOLD, 25);

		btnLogout = new JButton("Log out");
		btnLogout.setFont(f);

		btnChange = new JButton("Change password");
		btnChange.setFont(f);
	}

	private void set(final Window parent) {
		setLayout(new GridBagLayout());
		setSize(400, 280);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
