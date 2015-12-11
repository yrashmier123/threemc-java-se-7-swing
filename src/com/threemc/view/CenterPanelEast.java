package com.threemc.view;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import com.threemc.controller.ControllerForOutputDetails;
import com.threemc.data.OutputsUpdate;
import com.threemc.events.CenterPanelEastEventListener;

public class CenterPanelEast extends JPanel {
	// Date Started: 06/26/2015

	private JLabel lblTop;
	private ControllerForOutputDetails controllero;
	private ArrayList<OutputsUpdate> ouList;
	private Timer timerUpdate;
	private CenterPanelEastEventListener listener;
	private int count = 0;
	private JPanel panelContainer;

	private Dimension d = getPreferredSize();
	private JFrame parent;

	public CenterPanelEast() {
		set();
		initUI();
		layoutComp();
	}

	private void loadfirstOutputsUpdates() {
		try {
			if (controllero.connect().equals("ok")) {
				controllero.loadAllOutputsUpdate();
				ouList = controllero.getOutputsUpdates();
				if (ouList != null) {
					final ArrayList<JPanel> panels = new ArrayList<JPanel>();
					for (int i = 0; i < ouList.size(); i++) {
						JPanel panel = new JPanel();
						panel.setBackground(CustomColor.bgColor());
						panel.setLayout(new GridBagLayout());
						panel.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createEtchedBorder(),
								BorderFactory.createEmptyBorder(5, 5, 5, 5)));
						panel.setPreferredSize(d);
						panel.setMinimumSize(d);
						panels.add(panel);
					}
					for (int i = 0; i < ouList.size(); i++) {
						Insets inset = new Insets(2, 2, 2, 2);
						GridBagConstraints gc = new GridBagConstraints();
						gc.insets = inset;
						final OutputsUpdate ou = ouList.get(i);
						String outputName = ou.getOutput().getOutputName();
						String empFName = ou.getEmployee().getEmpFirstName();
						String desc = ou.getOuDesc();
						String date = ou.getOuDate();
						JLabel lblouname = new JLabel(outputName);
						JLabel lblempname = new JLabel("by: " + empFName);
						JTextArea lbldesc = new JTextArea();
						JLabel lbldate = new JLabel(date);
						JLabel lblSee = new JLabel("See all ...");
						lblSee.setFont(CustomFont.setFont("Tahom", Font.BOLD, 11));
						lblouname.setFont(CustomFont.setFont("Tahom", Font.BOLD, 11));
						lblempname.setFont(CustomFont.setFont("Tahom", Font.BOLD, 11));
						lblSee.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent arg0) {
								OutputUpdates ous = new OutputUpdates(parent, Dialog.ModalityType.APPLICATION_MODAL);
								ous.setOutputID(ou.getOutput_id());
								ous.setOutputName(ou.getOutput().getOutputName());
								ous.setOutputUpdate();
								ous.setVisible(true);
								if(listener != null) {
									listener.invokeCenterPanelEastListClickEventListener();
								}
							}
						});

						lbldesc.setText(desc);
						lbldesc.setLineWrap(true);
						lbldesc.setFont(CustomFont.setFont("Tahoma",
								Font.PLAIN, 11));
						lbldesc.setEditable(false);

						gc.gridy = 0;
						gc.gridx = 0;
						gc.weighty = 0;
						gc.weightx = 0;
						gc.gridwidth =2;
						gc.anchor = GridBagConstraints.FIRST_LINE_START;

						panels.get(i).add(lblouname, gc);

						gc.gridy++;
						gc.gridx = 0;

						panels.get(i).add(lbldate, gc);

						gc.gridy++;
						gc.gridx = 0;
						gc.weighty = 1;
						gc.weightx = 1;
						gc.fill = GridBagConstraints.BOTH;

						panels.get(i)
								.add(new JScrollPane(
										lbldesc,
										JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
										gc);

						gc.gridy++;
						gc.gridx = 0;
						gc.weighty = 0;
						gc.weightx = 1;
						gc.fill = GridBagConstraints.HORIZONTAL;
						gc.gridwidth = 1;

						panels.get(i).add(lblempname, gc);
						
						gc.gridx = 1;
						gc.weightx = 0;
						gc.fill = GridBagConstraints.NONE;
						panels.get(i).add(lblSee, gc);
					}
					GridBagConstraints gc = new GridBagConstraints();

					gc.gridy = 0;

					for (int i = 0; i < ouList.size(); i++) {
						panelContainer.add(panels.get(i), gc);
						gc.gridy++;
					}

					String msg = "";
					if (count == 1) {
						msg = count
								+ " New Update in Output by "
								+ ouList.get(ouList.size() - ouList.size())
										.getEmployee().getEmpFirstName() + ".";
					} else {
						msg = count + " New Update in Outputs";
					}

					if (listener != null) {
						listener.invokeCenterPanelEastNewOutputEventListener(msg);
					}
				}
			} else {
				System.out.println(controllero.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}
	}
	
	public void setParent(JFrame parent) {
		this.parent = parent;
	}

	public void setCenterPanelEastEventListener(
			CenterPanelEastEventListener listener) {
		this.listener = listener;
	}

	private void initUI() {
		controllero = new ControllerForOutputDetails();

		d.height = 150;
		d.width = 227;

		lblTop = new JLabel("OUTPUT UPDATES", SwingConstants.CENTER);
		lblTop.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		lblTop.setFont(CustomFont.setFont("Tahoma", Font.BOLD, 20));

		panelContainer = new JPanel();
		panelContainer.setLayout(new GridBagLayout());

		loadfirstOutputsUpdates();

		timerUpdate = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent w) {
				try {
					count = 0;
					if (controllero.connect().equals("ok")) {
						if (controllero.getOutputUpdatesNewCount() > 0) {
							for (int i = 0; i < controllero
									.getOutputUpdatesNewCount(); i++) {
								count++;
							}
							panelContainer.removeAll();
							loadfirstOutputsUpdates();
							panelContainer.revalidate();
							panelContainer.repaint();
							controllero.updateOutputUpdatesIfNew();
						}
					} else {
						System.out.println(controllero.connect());
					}
				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println(e.getMessage());
				}
			}
		});
		timerUpdate.start();
	}

	private void layoutComp() {
		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 1;
		gc.fill = GridBagConstraints.BOTH;

		gc.gridx = 0;
		gc.gridy = 0;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(lblTop, gc);

		gc.gridy = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JScrollPane(panelContainer,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gc);
	}

	private void set() {
		setLayout(new GridBagLayout());
		Dimension dim = new Dimension();
		dim.width = 250;
		setPreferredSize(dim);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		// setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
	}
}
