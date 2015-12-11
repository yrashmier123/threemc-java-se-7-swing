package com.threemc.view;

import java.awt.Dimension;

public class CustomDimension {
	private CustomDimension(){
		
	}

	public static Dimension setDimension(int width,int height) {
		Dimension dim = new Dimension();
		dim.width = width;
		dim.height = height;
		return dim;
	}

	public static Dimension setDimensionWidth(int width) {
		Dimension dim = new Dimension();
		dim.width = width;
		return dim;
	}

	public static Dimension setDimensionHeight(int height) {
		Dimension dim = new Dimension();
		dim.height = height;
		return dim;
	}
}
