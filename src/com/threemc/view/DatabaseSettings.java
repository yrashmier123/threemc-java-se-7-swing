package com.threemc.view;

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
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.threemc.events.PrefsListener;

public class DatabaseSettings extends Dialog {

	private JPanel panelButton;

	private JButton btnOk;
	private JButton btnCancel;

	private JLabel lblIpAddress;
	private JLabel lblDatabase;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblPort;

	private JTextField txtIpAddress;
	private JTextField txtDatabase;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JSpinner spnPort;

	private SpinnerNumberModel spinnerModel;

	private PrefsListener listener;
	
	public DatabaseSettings(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Database Setting", modal);
		set(parent);
		initUI();
		layoutComponents();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				DatabaseSettings.this.dispose();
			}
		});

		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ip = txtIpAddress.getText().trim();
				String dbname = txtDatabase.getText().trim();
				String user = txtUsername.getText().trim();
				String pass = new String(txtPassword.getPassword()).trim();
				int portNumber = (int)spnPort.getValue();
				
				if(listener != null) {
					listener.preferenceSet(ip, dbname, user, pass, portNumber);
				}
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseSettings.this.dispose();
			}
		});
	}

	public void setDefaults(String ip, String dbName, String username, String password, int port) {
		txtIpAddress.setText(ip);
		txtDatabase.setText(dbName);
		txtUsername.setText(username);
		txtPassword.setText(password);
		spnPort.setValue(port);
		
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.listener = prefsListener;
	}

	private void layoutComponents() {
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5, 5, 5, 5);

		gc.weighty = 1;
		gc.insets = inset;

		gc.gridy = 0;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		add(lblIpAddress, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(txtIpAddress, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		add(lblDatabase, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(txtDatabase, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		add(lblUsername, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(txtUsername, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		add(lblPassword, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(txtPassword, gc);

		gc.gridy++;
		gc.gridx = 0;
		gc.weightx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;

		add(lblPort, gc);

		gc.gridx = 1;
		gc.weightx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.HORIZONTAL;

		add(spnPort, gc);

		panelButton.add(btnOk);
		panelButton.add(btnCancel);

		gc.gridy++;
		gc.gridx = 0;
		gc.gridwidth = 2;

		add(panelButton, gc);
	}

	private void initUI() {
		Font f = CustomFont.setFontTahomaPlain();

		panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelButton.setBackground(CustomColor.bgColor());
		panelButton.setBorder(BorderFactory.createEtchedBorder());

		btnOk = new JButton("Ok");
		btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic(KeyEvent.VK_ESCAPE);

		lblIpAddress = new JLabel("IP Address: ");
		lblDatabase = new JLabel("Database name: ");
		lblUsername = new JLabel("Username: ");
		lblPassword = new JLabel("Password: ");
		lblPort = new JLabel("Port: ");

		lblIpAddress.setFont(f);
		lblDatabase.setFont(f);
		lblUsername.setFont(f);
		lblPassword.setFont(f);
		lblPort.setFont(f);

		txtIpAddress = new JTextField(20);
		txtDatabase = new JTextField(20);
		txtUsername = new JTextField(20);
		txtPassword = new JPasswordField(20);

		txtIpAddress.setFont(f);
		txtDatabase.setFont(f);
		txtUsername.setFont(f);
		txtPassword.setFont(f);
	
		txtIpAddress.setHorizontalAlignment(JTextField.RIGHT);
		txtDatabase.setHorizontalAlignment(JTextField.RIGHT);
		txtUsername.setHorizontalAlignment(JTextField.RIGHT);
		txtPassword.setHorizontalAlignment(JTextField.RIGHT);

		spinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		spnPort = new JSpinner(spinnerModel);
	}

	private void set(final Window parent) {
		setLayout(new GridBagLayout());
		setSize(400,280);
		setResizable(false);
		setLocationRelativeTo(parent);
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		setBackground(CustomColor.bgColor());
	}
}
