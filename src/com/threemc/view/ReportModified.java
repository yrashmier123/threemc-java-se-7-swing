package com.threemc.view;

import javax.swing.ImageIcon;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.view.JasperViewer;

public class ReportModified extends JasperReportBuilder {
	@Override
	public JasperReportBuilder show(boolean exitOnClose) throws DRException {
		JasperViewer jview = new JasperViewer(toJasperPrint(), exitOnClose, null);
		jview.setTitle("Three McQueens Eventi Automated System");
		//below is standart icon - can change it
		jview.setIconImage(new ImageIcon(getClass().getResource("/res/mcicon.png")).getImage());
		jview.setVisible(true);
		return this;
	}
}
