package com.threemc.view;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.threemc.controller.ControllerForOutputDetails;
import com.threemc.data.Output;
import com.threemc.data.OutputsUpdate;

public class OutputUpdates extends JDialog {

	private JTextArea txtUpdateList;
	
	private JPanel panelTop;
	private JLabel lblName;
	
	private Output out;
	
	private ControllerForOutputDetails controllero;

	private int id = 0;

	public OutputUpdates(final Window parent, final Dialog.ModalityType modal) {
		super(parent, "Output - Updates", modal);
		set(parent);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				OutputUpdates.this.dispose();
			}
		});

		controllero = new ControllerForOutputDetails();
		
		Font f = CustomFont.setFontTahomaPlain();
		
		txtUpdateList = new JTextArea();
		txtUpdateList.setFont(f);
		txtUpdateList.setLineWrap(true);
		txtUpdateList.setEditable(false);
		
		panelTop = new JPanel();
		panelTop.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelTop.setBackground(CustomColor.bgColor());
		panelTop.setBorder(BorderFactory.createEtchedBorder());
		
		lblName = new JLabel("");
		lblName.setFont(f);
		
		GridBagConstraints gc = new GridBagConstraints();
		Insets inset = new Insets(5,5,5,5);
		
		gc.insets = inset;
		gc.weightx = 1;
		gc.weighty = 0;
		
		gc.gridy = 0;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		panelTop.add(lblName);
		
		add(panelTop, gc);
		
		gc.gridy++;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.BOTH;
		gc.weighty = 1;

		add(new JScrollPane(txtUpdateList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),gc);
	}
	
	public void setOutput(Output out) {
		this.out = out;
		lblName.setText(out.getOutputName());
	}
	
	public void setOutputID(int id) {
		this.id  = id;
	}
	
	public void setOutputName(String name) {
		lblName.setText(name);
	}

	public void setOutputUpdate() {
		txtUpdateList.setText("");
		ArrayList<OutputsUpdate> outs;
		StringBuffer list = new StringBuffer();
		try {
			controllero.connect();
			controllero.loadAllOutputsUpdateById(id);
			outs = controllero.getOutputsUpdates();
			if (outs.size() >= 1) {
				for (OutputsUpdate ouh : outs) {
					list.append(ouh.getOuDate() + " " + ouh.getOuDesc()
							+ "\n\n");
				}
				txtUpdateList.setText(list.toString());
			} else {
				txtUpdateList.setText("No updates for this output yet");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void set(final Window parent) {
		setLayout(new GridBagLayout());
		setResizable(false);
		setSize(550, 450);
		setLocationRelativeTo(parent);
		setBackground(CustomColor.bgColor());
		Image img = new ImageIcon(getClass().getResource("/res/mcicon.png"))
				.getImage();
		setIconImage(img);
	}

}
