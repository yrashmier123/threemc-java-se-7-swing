package com.threemc.view;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.threemc.controller.ControllerForBookingDetails;
import com.threemc.controller.ControllerForPaymentDetails;
import com.threemc.data.Booking;
import com.threemc.data.Client;
import com.threemc.data.Payment;
import com.threemc.data.PaymentHistory;
import com.threemc.data.ServicesWanted;

import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ReportNew {
	
	private ArrayList<ServicesWanted> servicesWanted;
	private ArrayList<PaymentHistory> paymentHistory;

	private ControllerForBookingDetails controller = new ControllerForBookingDetails();
	private ControllerForPaymentDetails controllerp = new ControllerForPaymentDetails();

	private Client client;
	private Booking book;
	private Payment pay;

	private int client_id = 0;
	private int event_id = 0;

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

	public ReportNew() {
		
	}
	
	public void createReport() {
		setClient();
		setEvent();
		setPaymentHistory();
		setPayment();
		build();
	}

	private void build() {
		try {
			report().title(Templates.createTitleComponent("TitleSubreport"),
					cmp.subreport(createServicesWantedSubReport()),
					cmp.subreport(createPaymentHistorySubReport()))
					.pageFooter(Templates.footerComponent).show();
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private JasperReportBuilder createPaymentHistorySubReport() {
		JasperReportBuilder report = report();
		report.setTemplate(Templates.reportTemplate)
				.title(cmp.text("Subreport in title").setStyle(Templates.bold12CenteredStyle))
				.columns(col.column("Date Paid", "date", type.stringType()),
						 col.column("Amount", "amout", type.integerType()))
				.setDataSource(createPaymentHistorySubReportDataSource());

		return report;
	}

	private JRDataSource createPaymentHistorySubReportDataSource() {
		DRDataSource dataSource = new DRDataSource("date", "amout");
		for (int i = 0; i < paymentHistory.size(); i++) {
			PaymentHistory ph = paymentHistory.get(i);
			dataSource.add(ph.getPaymentDate(), ph.getPaymentPaidThisDate());
		}
		return dataSource;
	}
	
	private JasperReportBuilder createServicesWantedSubReport() {
		JasperReportBuilder report = report();
		report.setTemplate(Templates.reportTemplate)
				.title(cmp.text("Subreport in title").setStyle(Templates.bold12CenteredStyle))
				.columns(col.column("Service name", "sname", type.stringType()),
						 col.column("Service Cost", "scost", type.integerType()),
						 col.column("Service Description", "sdesc", type.stringType()))
				.setDataSource(createServicesWantedSubReportDataSource());

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
}
