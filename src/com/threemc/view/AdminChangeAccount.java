package com.threemc.view;

import java.awt.BorderLayout;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.threemc.controller.ControllerForAdmin;
import com.threemc.data.Admin;

public class AdminChangeAccount extends Dialog {

	private JPanel panelCenter;
	private JPanel panelButton;

	private JLabel lblusername;
	private JLabel lblpassword;
	private JLabel lblnewpass;
	private JLabel lblpasstu;

	private JTextField txtuser;
	private JPasswordField txtpass;
	private JPasswordField txtnewpass;
	private JPasswordField txtpasstu;

	private JButton btnSave;
	private JButton btnCancel;

	Font f = CustomFont.setFontTahomaPlain();

	private ControllerForAdmin controllera;

	private Admin admin;

	public AdminChangeAccount(final Window parent, Dialog.ModalityType modal) {
		super(parent, "Admin - Change username and password", modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AdminChangeAccount.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminChangeAccount.this.dispose();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(controllera.connect().equals("ok")) {
						if(admin != null) {
							String username = txtuser.getText();
							String pass = new String(txtpass.getPassword());
							String newpass = new String(txtnewpass.getPassword());
							String passtu = new String(txtpasstu.getPassword());
							if(CustomPatternChecker.checkStringLettersWithNumbers(username) && !username.isEmpty()) {
								if(CustomPatternChecker.checkStringLettersWithNumbers(pass) && !pass.isEmpty()) {
									if(CustomPatternChecker.checkStringLettersWithNumbers(passtu) && !passtu.isEmpty()) {
										if(CustomPatternChecker.checkStringLettersWithNumbers(newpass) && !newpass.isEmpty()) {
											if(admin.getAdminPassword().equals(pass)) {
												if(newpass.equals(passtu)) {
													if(passtu.length() > 7 && newpass.length() > 7) {
														if(controllera.connect().equals("ok")) {
														
															int id = admin.getId();
															String userLastLogin = admin.getAdminUserLastLogin();
															String userStatus = admin.getAdminUserStatus();
															Admin ad = new Admin(id, username, newpass, userLastLogin, userStatus);
															controllera.updateAdmin(ad);
															JOptionPane.showMessageDialog(parent, "Update Successfull", "Admin - Change username and password", JOptionPane.INFORMATION_MESSAGE);
															AdminChangeAccount.this.dispose();
														}
													} else {
														JOptionPane.showMessageDialog(parent, "Password should be more than 7 Alphanumeric characters.", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
													}
												} else {
													JOptionPane.showMessageDialog(parent, "New password does not match!", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
												}
											} else {
												JOptionPane.showMessageDialog(parent, "Current Password is invalid!", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
											}
										} else {
											JOptionPane.showMessageDialog(parent, "New Password is invalid! Empty fields and Special Characters are not allowed.", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
										}
									} else {
										JOptionPane.showMessageDialog(parent, "Re-enter New Password is invalid! Empty fields and Special Characters are not allowed.", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
									}
								} else {
									JOptionPane.showMessageDialog(parent, "Current Password is invalid! Empty fields and Special Characters are not allowed.", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(parent, "Username is invalid! Empty fields and Special Characters are not allowed.", "Admin - Change username and password", JOptionPane.ERROR_MESSAGE);
							}
						}
					} else {
						System.out.println(controllera.connect());
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
		txtuser.setText(admin.getAdminUsername());
		txtpass.setText(admin.getAdminPassword());
	}

	private void initUI() {
		controllera = new ControllerForAdmin();

		panelCenter = new JPanel();
		panelCenter.setLayout(new GridBagLayout());
		panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panelCenter.setBackground(CustomColor.bgColor());

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(Color.WHITE);
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		lblusername = new JLabel("Username: ");
		lblpassword = new JLabel("Current Password: ");
		lblpasstu = new JLabel("Re-enter new Password: ");
		lblnewpass = new JLabel("Enter new Password: ");

		lblusername.setFont(f);
		lblpassword.setFont(f);
		lblpasstu.setFont(f);
		lblnewpass.setFont(f);

		txtuser = new JTextField(20);
		txtpass = new JPasswordField(20);
		txtpasstu = new JPasswordField(20);
		txtnewpass = new JPasswordField(20);

		txtuser.setFont(f);
		txtpass.setFont(f);
		txtpasstu.setFont(f);
		txtnewpass.setFont(f);

		btnSave = new JButton("Save");
		btnCancel = new JButton("Cancel");

		btnSave.setFont(f);
		btnCancel.setFont(f);
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5, 5, 5, 5);

		gc.insets = inset;
		gc.weighty = 0;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		panelCenter.add(lblusername, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelCenter.add(txtuser, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		panelCenter.add(lblpassword, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelCenter.add(txtpass, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		panelCenter.add(lblnewpass, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelCenter.add(txtnewpass, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		panelCenter.add(lblpasstu, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		panelCenter.add(txtpasstu, gc);

		gc.gridy++;
		gc.gridx = 1;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.NONE;

		panelButton.add(btnSave);
		panelButton.add(btnCancel);

		panelCenter.add(panelButton, gc);

		add(panelCenter, BorderLayout.CENTER);
	}

	private void set(final Window parent) {
		setLayout(new BorderLayout());
		setSize(450, 250);
		setLocationRelativeTo(parent);
		setResizable(false);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
