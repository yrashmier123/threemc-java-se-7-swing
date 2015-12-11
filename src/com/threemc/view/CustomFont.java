package com.threemc.view;

import java.awt.Font;

public class CustomFont {
	//Date Started: 06/27/2015
	private static Font fontCustom;	
	public static Font setFont(String fontStyle, int orientation, int fontSize) {
		fontCustom = new Font(fontStyle, orientation, fontSize);
		return fontCustom;
	}

	public static Font setFontRockwellBold() {
		fontCustom = new Font("Rockwell", Font.BOLD, 15);
		return fontCustom;
	}

	public static Font setFontRockwellPlain() {
		fontCustom = new Font("Rockwell", Font.PLAIN, 15);
		return fontCustom;
	}

	public static Font setFontTahomaPlain() {
		fontCustom = new Font("Tahoma", Font.PLAIN, 15);
		return fontCustom;
	}

	public static Font setFontTahomaBold() {
		fontCustom = new Font("Tahoma", Font.BOLD, 15);
		return fontCustom;
	}
}
