package com.threemc.view;

import java.awt.Dimension;

import javax.swing.JButton;

public class CustomJButton extends JButton {
	//Date Started: 06/26/2015
	public CustomJButton(String text) {
		super(text);
		Dimension d = new Dimension();
		d.width = 100;
		d.height = 70;
		setPreferredSize(d);
	}
}
