package com.threemc.view;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;

import javax.swing.SwingWorker;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.Units;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.data.Client;
import com.threemc.data.PaymentHistory;

public class ReportClientDetails {

	private ReportModified report = null;
	private int client_id = 0;
	private Client client;
	private ControllerForBookingDetails controller = new ControllerForBookingDetails();
	private ThisWorker tw;

	private StyleBuilder boldStyle = stl.style().bold();
	private StyleBuilder font12Style = stl.style().setFontSize(12);
	private StyleBuilder fontBold12Style = stl.style(boldStyle).setFontSize(12);
	private StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private StyleBuilder titleStyle = DynamicReports.stl.style(boldCenteredStyle).setFontSize(12).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
	private StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(DynamicReports.stl.pen1Point()).setFontSize(12).setBackgroundColor(Color.LIGHT_GRAY);

	public ReportClientDetails() {
		tw = new ThisWorker();
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	private void setClient() {
		try {
			if (controller.connect().equals("ok")) {
				controller.searchClientById(client_id);
				client = controller.getClient().get(0);
			} else {
				System.out.println(controller.connect());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JRDataSource createClientReportDataSource() {
		DRDataSource dataSource = new DRDataSource("id", "name");
		dataSource.add(""+client.getId(), client.getClientLastName() + ", " + client.getClientFirstName() + " " + client.getClientMiddleName());
		return dataSource;
	}

	public void createReport() throws Exception {
		setClient();
		tw.execute();
	}

	private class ThisWorker extends SwingWorker<Void, Void> {
		protected Void doInBackground() throws Exception {
			report = new ReportModified();
			TextColumnBuilder<String> id = col.column("Client ID","id", type.stringType()).setStyle(font12Style).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
			TextColumnBuilder<String> name = col.column("Full name","name", type.stringType()).setStyle(font12Style).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

			report.setColumnTitleStyle(columnTitleStyle);
			report.columns(id, name);
			report.highlightDetailEvenRows();

			report.title(
					cmp
					.horizontalList()
					.add(cmp.image(Templates.class.getResource("/res/mccrown.png")).setFixedDimension(Units.inch(1.3), Units.inch(0.8)),
						cmp.verticalList()
								.add(cmp.text("Three McQueens Eventi Automated System ").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
									 cmp.text("Client Details").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)))
					.newRow().add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(5))
					.newRow().add(cmp.verticalGap(25))
					.add(cmp.filler()
							.setStyle(stl.style().setTopBorder(stl.pen()))
							.setFixedHeight(5))
			);

//			 set datasource
			report.setDataSource(createClientReportDataSource());
			
			report.summary(cmp.verticalList()
							.add(cmp.verticalGap(35))
							.add(cmp.text("Other Details").setStyle(fontBold12Style),
								 cmp.text("Contact Number: " + client.getClientContactNumber()).setStyle(font12Style),
								 cmp.text("Gender: " + client.getClientGender()).setStyle(font12Style),
								 cmp.text("Address: " + client.getClientAddress()).setStyle(font12Style)
							)
						);

			// page footer
			report.pageFooter(cmp.pageXofY());

			// when there is no data
			report.noData(Templates.createTitleComponent("No Data"), cmp.text("There is no data"));

			//page layout
			report.setPageFormat(Units.inch(8.5), Units.inch(13),PageOrientation.LANDSCAPE);
			report.setPageMargin(margin(Units.inch(1)));

			return null;
		}

		protected void done() {
			try {
				report.show(false);
			} catch (DRException e) {
				e.printStackTrace();
			}
		}
	}
}
