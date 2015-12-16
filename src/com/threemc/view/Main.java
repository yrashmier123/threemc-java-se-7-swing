package com.threemc.view;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Image;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.threemc.events.PrefsListener;
import com.threemc.model.DatabaseConnection;

public class Main {
	//Date Started: 06/26/2015

	private static ProgressbarMain prog;
	private static MainSystemInterface mainSys;
	
	private static String ip = "";
	private static String dbName = "";
	private static String dbUserName = "";
	private static String dbPassword = "";
	private static String dbPort = "";
	
	private static Preferences prefs;
	
	private static DatabaseSettings ds;

	public static void main(String[] args ) {
		
		prefs = Preferences.userRoot().node("db");

		ds = new DatabaseSettings(null, ModalityType.APPLICATION_MODAL);
		ds.setPrefsListener(new PrefsListener() {
			public void preferenceSet(String ip, String dbName, String username, String password, int port) {
				prefs.put("dbname", dbName);
				prefs.put("ip", ip);
				prefs.put("username", username);
				prefs.put("password", password);
				prefs.putInt("port", 3306);
				ds.dispose();
			}
		});

		ip = prefs.get("ip", "localhost");
		dbName = prefs.get("dbname", "threemcqueens");
		dbUserName = prefs.get("username", "root");
		dbPassword = prefs.get("password", "");
		dbPort = prefs.get("port", ""+3306);

		ds.setDefaults(ip, dbName, dbUserName, dbPassword, Integer.parseInt(dbPort));
		DatabaseConnection.setDefaults(ip, dbName, dbUserName, dbPassword, Integer.parseInt(dbPort));
		ds.setVisible(true);
		
		prog = new ProgressbarMain(mainSys, ModalityType.DOCUMENT_MODAL);
		prog.setIndeterminate(true);
		prog.setVisible(true);


		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				mainSys = new MainSystemInterface();
				return null;
			}

			protected void done() {
				mainSys.setSize(1500, 800);
				mainSys.setLocationRelativeTo(null);
				mainSys.setMinimumSize(new Dimension(1500, 800));
				mainSys.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainSys.setExtendedState(JFrame.MAXIMIZED_BOTH);
				Image img = new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage();
				mainSys.setIconImage(img);
				mainSys.setVisible(true);
				prog.dispose();
				mainSys.login();
				mainSys.showIfEventisNow();
			}
		};
		worker.execute();
		
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				
//			}
//		});
	}
}

