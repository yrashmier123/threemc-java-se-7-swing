package com.threemc.view;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.sf.dynamicreports.examples.Templates;
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

public class ClientReports {
	private ReportModified report = null;
	private DateFormat format = new SimpleDateFormat("MMMMM/dd/yyyy hh:ss");
	private ProgressbarDialog prog;

	public ClientReports(final Window parent) throws Exception {
		prog = new ProgressbarDialog(parent, ModalityType.DOCUMENT_MODAL);
	}

	public void createReport() throws Exception {
		prog.setIndeterminate(true);
		prog.setVisible(true);

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {

				StyleBuilder boldStyle = stl.style().bold();
				StyleBuilder italicStyle = stl.style().italic();
				StyleBuilder centerItalicStyle = stl.style(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
				StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
				StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(stl.pen1Point())
						.setBackgroundColor(Color.LIGHT_GRAY);

				StyleBuilder titleStyle = stl.style(boldCenteredStyle).setFontSize(12).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

				// add columns
				// title, field name ,data type
				TextColumnBuilder<String> nameColumn = col.column("Client's name" , "clientName", type.stringType());
				TextColumnBuilder<String> genderColumn = col.column("Gender" , "gender" , type.stringType()).setFixedWidth(70);
				TextColumnBuilder<String> addressColumn = col.column("Address" , "address" , type.stringType());
				TextColumnBuilder<String> contactColumn = col.column("Contact #" , "contact" , type.stringType()).setFixedWidth(100);

				// create new report design
				report = new ReportModified();
				report.setColumnTitleStyle(columnTitleStyle);
				report.columns(nameColumn,genderColumn,addressColumn,contactColumn);
				report.highlightDetailEvenRows();

				// report title
				report.title(cmp.horizontalList().add(
							 cmp.image(Templates.class.getResource("/res/mccrown.png")).setFixedDimension(Units.inch(1.3), Units.inch(0.8)),
							 cmp.verticalList().add(
								cmp.text("Three McQueens Eventi Automated System ").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
								cmp.text("List of Clients and their Details").setStyle(centerItalicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
//								cmp.text("As of " + format.format(new Date())).setStyle(centerItalicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
								)
							)
						.newRow()
						.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)));
				// footer - shows number of page at page	
				report.pageFooter(cmp.pageXofY());
				report.setDataSource(createDataSource());// set datasource

				// set report size to long bond paper 8.5x13 inch
				report.setPageFormat(Units.inch(8.5), Units.inch(13),PageOrientation.PORTRAIT);
				report.setPageMargin(margin(Units.inch(0.5)));

				return null;
			}

			protected void done() {
				try {
					report.show(false);
				} catch (DRException e) {
					e.printStackTrace();
				}
				prog.setIndeterminate(false);
				prog.setVisible(false);
			}
		};
		worker.execute();
	}

	private JRDataSource createDataSource() {
		DRDataSource source = new DRDataSource("clientName", "gender",
				"address", "contact");
		ControllerForBookingDetails controller = new ControllerForBookingDetails();
		ArrayList<Client> list = null;
		try {
			if(controller.connect().equals("ok")) {
				controller.loadClient("c.client_lastName");
				list = controller.getClient();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list != null) {
			for (Client client : list) {
				source.add(client.getClientLastName() + ", "
						+ client.getClientFirstName() + " "
						+ client.getClientMiddleName(), client
						.getClientGender().name(), client
						.getClientAddress(), client
						.getClientContactNumber());
			}
		}
		return source;
	}
}
