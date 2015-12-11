package com.threemc.view;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.Units;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class Templates {
	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder bold12CenteredStyle;
	public static final StyleBuilder bold18CenteredStyle;
	public static final StyleBuilder bold22CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;

	public static final ReportTemplateBuilder reportTemplate;
	public static final CurrencyType currencyType;
	public static final ComponentBuilder<?, ?> dynamicReportsComponent;
	public static final ComponentBuilder<?, ?> footerComponent;

	static {
		rootStyle = stl.style().setPadding(2);
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
		bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
		bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
		columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.JUSTIFIED);
		columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point())
				.setBackgroundColor(Color.LIGHT_GRAY).bold()
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
		subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(170, 170, 170));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(140, 140, 140));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(stl.pen1Point());

		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
				.setHeadingStyle(0, stl.style(rootStyle).bold());

		reportTemplate = template().setLocale(Locale.ENGLISH)
				.setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle)
				.setGroupStyle(groupStyle).setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle).highlightDetailEvenRows()
				.crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);

		currencyType = new CurrencyType();

		HyperLinkBuilder link = hyperLink("http://www.facebook.com");
		dynamicReportsComponent = cmp.horizontalList(
				cmp.image(Templates.class.getResource("/res/mccrown.png")).setFixedDimension(Units.inch(1.3), Units.inch(0.8)),
				cmp.verticalList(
						cmp.text("Three Mc Queens Eventi").setStyle(bold18CenteredStyle)
								.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
						cmp.text("http://www.facebook.com")
								.setStyle(italicStyle).setHyperLink(link)))
				.setFixedWidth(300);

		footerComponent = cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
	}

	/**
	 * Creates custom component which is possible to add to any report band
	 * component
	 */
	
	public static ComponentBuilder<?, ?> createTitleComponent(String label) {
		return cmp
				.horizontalList()
				.add(dynamicReportsComponent,cmp.text(label)
								.setStyle(bold12CenteredStyle)
								.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)).newRow()
				.add(cmp.line()).newRow().add(cmp.verticalGap(10));
	}

	public static CurrencyValueFormatter createCurrencyValueFormatter(
			String label) {
		return new CurrencyValueFormatter(label);
	}

	public static class CurrencyType extends BigDecimalType {
		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends
			AbstractValueFormatter<String, Number> {
		private static final long serialVersionUID = 1L;

		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		@Override
		public String format(Number value, ReportParameters reportParameters) {
			return label
					+ currencyType.valueToString(value,
							reportParameters.getLocale());
		}
	}
}
