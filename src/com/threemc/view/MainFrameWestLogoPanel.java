package com.threemc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MainFrameWestLogoPanel extends JPanel {
	// Date Started: 06/26/2015
	
	private JLabel lblLogo;
	public MainFrameWestLogoPanel() {
		set();
		
		lblLogo = new JLabel("asd");
		CustomIcon ci = new CustomIcon();
		lblLogo.setIcon(ci.createIcon("/res/leftboxhome.png"));
		
		add(lblLogo, BorderLayout.CENTER);
	}

	private void set() {
		setLayout(new BorderLayout());
		setBackground(CustomColor.bgColor());
		Dimension dim = new Dimension();
		dim.width = 200;
		
		Dimension dimto = new Dimension();
		dimto.width = 200;
		dimto.height = 700;
		setPreferredSize(dimto);
		setMinimumSize(dimto);
		setMaximumSize(dimto);
		setSize(dimto);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
	}
}
