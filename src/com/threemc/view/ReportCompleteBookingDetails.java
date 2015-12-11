package com.threemc.view;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.Units;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.ServicesWanted;

public class ReportCompleteBookingDetails {

	private ReportModified report = null;
	private ThisWorker tw;

	private ArrayList<ServicesWanted> servicesWanted;
	private ArrayList<PaymentHistory> paymentHistory;

	private ControllerForBookingDetails controller = new ControllerForBookingDetails();
	private ControllerForPaymentDetails controllerp = new ControllerForPaymentDetails();

	private Client client;
	private Booking book;
	private Payment pay;

	private int client_id = 0;
	private int event_id = 0;
	
	private StyleBuilder boldStyle = stl.style().bold();
	private StyleBuilder bold12Style = stl.style().bold().setFontSize(12);
	private StyleBuilder b12Style = stl.style().setFontSize(12);
	private StyleBuilder boldCenteredUnderlined14Style = stl.style().bold().underline().setFontSize(14).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private StyleBuilder italicStyle = stl.style().italic();
	private StyleBuilder centerItalicStyle = stl.style(italicStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	private StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(DynamicReports.stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
	private StyleBuilder titleStyle = DynamicReports.stl.style(boldCenteredStyle).setFontSize(12).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

	public void setClientEvent_id(int client_id, int event_id) {
		this.client_id = client_id;
		this.event_id = event_id;
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

	private void setEvent() {
		try {
			if (controller.connect().equals("ok")) {
				book = controller.loadBookingRecordsByEventId(event_id);
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPaymentHistory() {
		try {
			if (controllerp.connect().equals("ok")) {
				controllerp
						.loadPaymentHistoryByClientEvent(client_id, event_id);
				paymentHistory = controllerp.getPaymentHistory();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPayment() {
		try {
			if (controller.connect().equals("ok")) {
				if (controller.checkIfServicesHasPayment(client_id, event_id)) {
					pay = controller.loadPaymentRecord(client_id, event_id);
				}
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ReportCompleteBookingDetails() throws Exception {
		tw = new ThisWorker();
	}

	public void createReport() throws Exception {
		setClient();
		setEvent();
		setPayment();
		setPaymentHistory();
		tw.execute();
	}

	private ComponentBuilder<?, ?> createEventComponent(String label) {
		HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style(bold12Style).setTopBorder(stl.pen1Point()).setLeftPadding(10));
		addEventDetailsAttribute(list, "Title ", book.getEventName());
		addEventDetailsAttribute(list, "Venue ", book.getEventVenue());
		addEventDetailsAttribute(list, "Date ", book.getEventDate());
		addEventDetailsAttribute(list, "Time ", book.getEventTime());
		addEventDetailsAttribute(list, "Type ", book.getEventType());
		addEventDetailsAttribute(list, "No. of Guest ","" + book.getEventGuestNumber());
		addEventDetailsAttribute(list, "Details ", book.getEventDetails());
		
		return cmp.verticalList(cmp.text(label).setStyle(bold12Style),
				list);
	}

	private void addEventDetailsAttribute(HorizontalListBuilder list, String label,
			String value) {
		if (value != null) {
			list.add(
					cmp.text(label + ":").setFixedColumns(8)
							.setStyle(Templates.boldStyle), cmp.text(value))
					.newRow();
		}
	}

	private JRDataSource createPaymentHistorySubReportDataSource() {
		DRDataSource dataSource = new DRDataSource("date", "amount", "desc");
		for (int i = 0; i < paymentHistory.size(); i++) {
			PaymentHistory ph = paymentHistory.get(i);
			dataSource.add(ph.getPaymentDate(), ph.getPaymentPaidThisDate(), ph.getPaymentDesc());
		}
		return dataSource;
	}
	
	private JasperReportBuilder createServicesWantedSubReport() {
		JasperReportBuilder report = report();
		report.setTemplate(Templates.reportTemplate)
				.title(cmp.text("Package Services").setStyle(Templates.bold12CenteredStyle))
				.columns(col.column("Service name", "sname", type.stringType()),
						 col.column("Service Cost", "scost", type.integerType()).setValueFormatter(new ValueFormatter())
						 	.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
						 col.column("Service Description", "sdesc", type.stringType())
						 .setHorizontalTextAlignment(HorizontalTextAlignment.JUSTIFIED))
				.setDataSource(createServicesWantedSubReportDataSource());
		return report;
	}
	
	private JasperReportBuilder createPaymentHistorySubReport() {
		JasperReportBuilder report = report();
		report.setTemplate(Templates.reportTemplate)
				.title(cmp.text("Payment History").setStyle(Templates.bold12CenteredStyle))
				.columns(col.column("Date of Payment", "date", type.stringType()),
						 col.column("Amount", "amount", type.integerType()).setValueFormatter(new ValueFormatter())
						 	.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER),
						 col.column("Payment Description", "desc", type.stringType()))
				.setDataSource(createPaymentHistorySubReportDataSource());
		return report;
	}
	
	private JRDataSource createServicesWantedSubReportDataSource() {
		DRDataSource source = new DRDataSource("sname", "scost", "sdesc");

		try {
			if (controller.connect().equals("ok")) {
				controller.loadServicesRecord(client_id, event_id);
				servicesWanted = controller.getServicesWanted();
			} else {
				System.out.println(controller.connect());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (ServicesWanted sw : servicesWanted) {
			source.add(sw.getServiceName(), sw.getServiceCost(),
					sw.getServiceDesc());
		}
		return source;
	}

	// TODO
	private class ThisWorker extends SwingWorker<Void, Void> {
		protected Void doInBackground() throws Exception {

//			 add columns
//			 title, field name ,data type
			TextColumnBuilder<String> snameColumn = col.column("Service name","sname", type.stringType());
			TextColumnBuilder<Integer> scostColumn = col.column("Service cost", "scost", type.integerType()).setValueFormatter(new ValueFormatter())
					.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
			TextColumnBuilder<String> sdescColumn = col.column("Service Description", "sdesc", type.stringType());

			// create new report design
			report = new ReportModified();
			report.setTemplate(Templates.reportTemplate);
			report.setColumnTitleStyle(columnTitleStyle);
			report.columns(snameColumn, scostColumn, sdescColumn);
			report.highlightDetailEvenRows();

			// report title
			report.title(
					cmp
					.horizontalList()
					.add(cmp.image(Templates.class.getResource("/res/mccrown.png")).setFixedDimension(Units.inch(1.3), Units.inch(0.8)),
						cmp.verticalList()
								.add(cmp.text("Three McQueens Eventi Automated System ").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
									 cmp.text("Event and Payment Details").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)))
					.newRow().add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(5))
					.newRow().add(cmp.verticalGap(25))
					
					.add(cmp.filler()
							.setStyle(stl.style().setTopBorder(stl.pen()))
							.setFixedHeight(5))
					.newRow()
					.add(cmp.hListCell(createEventComponent("Event Details").setStyle(bold12Style))
							.heightFixedOnTop())
					.setStyle(Templates.bold18CenteredStyle)
					.add(cmp.verticalGap(15))
					.newRow()
					.add(cmp.filler()
							.setStyle(stl.style().setBottomBorder(stl.pen()))
							.setFixedHeight(30))
			);
			// footer - shows number of page at page
			report.pageFooter(cmp.pageXofY());

			report.noData(Templates.createTitleComponent("No Data"), cmp.text("There is no data"));
			report.setColumnHeaderStyle(bold12Style);

//			 set datasource
			report.setDataSource(createServicesWantedSubReportDataSource());

			StringBuffer stringBuffer = new StringBuffer();

			for (PaymentHistory ph : paymentHistory) {
				stringBuffer.append("Date : " + ph.getPaymentDate()
						+ " ; Paid: " + ph.getPaymentPaidThisDate() + "\n\n");
			}
//
			// set summary
			report.summary(cmp.verticalList(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen())).setFixedHeight(30),
							cmp.verticalList()
								.add(cmp.verticalGap(30))
								.add(cmp.subreport(createPaymentHistorySubReport()))
								.add(cmp.verticalGap(30))
								.add(cmp.text("Total Bill:    " + pay.getPaymentTotal()).setStyle(bold12Style))
								.add(cmp.text("Total Paid:    " + pay.getPaymentPaid()).setStyle(bold12Style))
								.add(cmp.text("Total Balance: " + pay.getPaymentBalance()).setStyle(bold12Style))
								.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen())).setFixedHeight(50))
								.add(cmp.text(client.getClientFirstName() + " "+ client.getClientMiddleName() + " "
												+ client.getClientLastName()).setStyle(
														boldCenteredUnderlined14Style))
								.add(cmp.text("Client Name").setStyle(boldCenteredStyle))
								.add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen())).setFixedHeight(30))
								.add(cmp.text("                                                    ").setStyle(boldCenteredUnderlined14Style))
								.add(cmp.text("Received By").setStyle(boldCenteredStyle))));

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
		}
	}
	
	private static class ValueFormatter extends AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;
			public String format(Number value, ReportParameters reportParameters) {
				return type.bigDecimalType().valueToString(value, reportParameters.getLocale()) + " Php";
			}
		}

}
