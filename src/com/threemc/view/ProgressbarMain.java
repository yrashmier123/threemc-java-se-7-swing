package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ProgressbarMain extends JDialog {
	
	private CustomIcon ci;
	
	private JPanel panelMain;
	
	private JProgressBar progressbar;
	private JLabel lblTitle;
	private JLabel lblLoading;
	
	public ProgressbarMain(final Window parent , final Dialog.ModalityType modal) {
		super(parent , "" , modal);
		set(parent);

		progressbar = new JProgressBar();
		progressbar.setPreferredSize(new Dimension(1100, 40));
		progressbar.setBackground(Color.BLACK);

		ci = new CustomIcon();

		lblTitle = new JLabel("");
		lblTitle.setIcon(ci.createIcon("/res/wallpaper.jpg"));
		
		lblLoading = new JLabel("Loading ...");
		lblLoading.setFont(CustomFont.setFontTahomaPlain());

		panelMain = new JPanel();
		panelMain.setLayout(new GridBagLayout());
		panelMain.setBackground(Color.WHITE);
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.weightx = 1;
		gc.weighty = 1;
		
		gc.fill = GridBagConstraints.BOTH;
		
		gc.gridy = 0;
		gc.gridx = 0;

		panelMain.add(lblTitle, gc);
		
		gc.weightx = 1;
		gc.weighty = 0;
		
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		gc.gridy++;
		gc.gridx = 0;
		
		panelMain.add(lblLoading, gc);
		
//		gc.weightx = 1;
//		gc.weighty = 0;
//		
//		gc.fill = GridBagConstraints.HORIZONTAL;
//		
//		gc.gridy++;
//		gc.gridx = 0;
		add(panelMain, BorderLayout.CENTER);
		add(progressbar, BorderLayout.SOUTH);
	}

	public void setIndeterminate(boolean b) {
		progressbar.setIndeterminate(b);
	}
	
	public void setVisible(final boolean b) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgressbarMain.super.setVisible(b);
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
		setSize(1100, 700);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());
		setBackground(CustomColor.bgColor());
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
		setIconImage(img);
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
		}
	}
}