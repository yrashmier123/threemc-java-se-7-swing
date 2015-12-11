package com.threemc.view;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

public class Main {
	//Date Started: 06/26/2015

	private static ProgressbarMain prog;
	private static MainSystemInterface mainSys;

	public static void main(String[] args ) {

		prog = new ProgressbarMain(mainSys, ModalityType.DOCUMENT_MODAL);
		prog.setIndeterminate(true);
		prog.setVisible(true);

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				mainSys = new MainSystemInterface();
				return null;
			}

			protected void done() {
//				mainSys.setJMenuBar(createMenuBar());
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

