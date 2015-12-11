package com.threemc.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomPatternChecker {

	public static boolean checkStringSomeCharsAllowed(String str) {
		Pattern p = Pattern.compile("[^a-zA-Z0-9() -:@&'!.,\t\n%]");
		Matcher m = p.matcher(str);
		if(m.find()) {
			return false;
		} 
		return true;
	}

	public static boolean checkStringLettersOnly(String str) {
		Pattern p = Pattern.compile("[^a-zA-Z ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		if(m.find()) {
			return false;
		} 
		return true;
	}

	public static boolean checkStringLettersWithNumbers(String str) {
		Pattern p = Pattern.compile("[^a-zA-Z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		if(m.find()) {
			return false;
		} 
		return true;
	}

	public static boolean checkStringNumbersOnly(String num) {
		String str = ""+num;
		Pattern p = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return m.find();
	}
}
