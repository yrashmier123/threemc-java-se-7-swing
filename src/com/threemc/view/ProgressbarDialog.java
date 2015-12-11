package com.threemc.view;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ProgressbarDialog extends JDialog {
	
	private JProgressBar progressbar;
	
	public ProgressbarDialog(final Window parent , final Dialog.ModalityType modal) {
		super(parent , "" , modal);
		set(parent);
		
		progressbar = new JProgressBar();
		progressbar.setPreferredSize(new Dimension(300, 30));
		
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.weightx = 1;
		gc.weighty = 1;
		
		gc.fill = GridBagConstraints.BOTH;
		
		gc.gridx = 0;
		gc.gridy = 0;
		

		add(progressbar, gc);
	}

	public void setIndeterminate(boolean b) {
		progressbar.setIndeterminate(b);
	}
	
	public void setVisible(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgressbarDialog.super.setVisible(b);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setMaximum(int value) {
		progressbar.setMaximum(value);
	}
	
	public void setValue(int value) {
		progressbar.setValue(value);
	}

	private void set(final Window parent) {
		setUndecorated(true);
		setSize(400, 40);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new GridBagLayout());
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		}
	}
}
