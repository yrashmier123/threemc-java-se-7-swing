package com.threemc.view;

import java.net.URL;

import javax.swing.ImageIcon;

public class CustomIcon extends ImageIcon {
	public ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		ImageIcon icon = new ImageIcon(url);
		return icon;
	}
}
