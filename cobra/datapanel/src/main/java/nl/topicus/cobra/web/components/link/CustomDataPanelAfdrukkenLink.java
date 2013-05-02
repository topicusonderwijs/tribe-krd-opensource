/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.link;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.BorderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ExportHeaderColumn;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author loite
 */
public class CustomDataPanelAfdrukkenLink<T> extends Link<T>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(CustomDataPanelAfdrukkenLink.class);

	private final CustomDataPanel<T> datapanel;

	private final ExportableDataView<T> rows;

	private final static int MIN_WIDTH_PER_CHAR = 5;

	private static final int MAX_ROWS_IN_REQUEST = 2000;

	public CustomDataPanelAfdrukkenLink(String id, CustomDataPanel<T> datapanel,
			ExportableDataView<T> rows)
	{
		super(id);
		this.datapanel = datapanel;
		this.rows = rows;
	}

	@Override
	public void onClick()
	{
		datapanel.createContextProvider();
		int aantal = rows.getRowCount();
		if (aantal > MAX_ROWS_IN_REQUEST)
		{
			if (datapanel.supportsExportJobs())
			{
				datapanel.triggerExportJob(this);
				info("De export is op de achtergrond opgestart. U kunt het bestand terugvinden onder Home | Rapportage.");
			}
			else
			{
				error("De zoeklijst bevat meer dan " + MAX_ROWS_IN_REQUEST
					+ " elementen. Verklein de selectie om deze te kunnen exporteren");
			}
		}
		else
		{
			try
			{
				JasperPrint print = getJasperPrint(null);
				File file = File.createTempFile("lijst", ".pdf");
				JasperExportManager.exportReportToPdfFile(print, file.getPath());
				PdfFileResourceStream stream =
					new PdfFileResourceStream(file, TimeUtil.getInstance().currentDateTime(),
						"Lijst afdrukken");
				((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
					"attachment; filename=\"" + stream.getFileName() + "\"");
				getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(stream));
				getRequestCycle().setRedirect(false);
			}
			catch (JRException e)
			{
				log.error(e.toString(), e);
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
			}
			catch (InterruptedException e)
			{
				log.error(e.toString(), e);
			}
		}
	}

	@SuppressWarnings( {"unchecked", "deprecation"})
	public JasperPrint getJasperPrint(IProgressCallback progressCallback)
			throws InterruptedException
	{
		ColumnModel<T> columnModel = datapanel.getModel();
		List<CustomColumn<T>> columns = collectColumns(columnModel);
		Map<String, Object> parameters = datapanel.getAfdrukParameters();
		String[] columnNames = new String[columns.size()];
		GroupProperty groupingProperty = datapanel.getSelectedGroupingProperty();
		JasperPrint print = null;
		int totalWidth = 0;
		try
		{
			// Get template.
			InputStream is = getTemplateInputStream();
			JasperDesign design = JRXmlLoader.load(is);
			int pageWidth =
				design.getPageWidth() - design.getLeftMargin() - design.getRightMargin();

			int colIndex = 0;
			for (CustomColumn<T> column : columns)
			{
				String text = null;
				if (column instanceof ExportHeaderColumn< ? >)
				{
					columnNames[colIndex] = ((ExportHeaderColumn<T>) column).getExportHeader();
				}
				else if (column instanceof AbstractColumn< ? >)
				{
					AbstractColumn<T> propColumn = (AbstractColumn<T>) column;
					if (propColumn.getDisplayModel().getObject() != null)
						text = propColumn.getDisplayModel().getObject().toString();
					// + estimateColumnSize(propColumn);
					columnNames[colIndex] = text;
				}
				colIndex++;
			}

			// maximale kolombreedtes
			int[] relativeColumnWidths = new int[columns.size()];
			for (int i = 0; i < relativeColumnWidths.length; i++)
				relativeColumnWidths[i] = columnNames[i].length();

			StringWriter writer = new StringWriter(totalWidth * rows.getRowCount());
			rows.writeExport(writer, groupingProperty == null ? null : groupingProperty
				.getProperty(), datapanel.getGroupingPrefix(), progressCallback);
			String export = writer.toString();
			String[] lines = export.split("\r\n");
			// Haal de kolommen opnieuw op. Na de export kunnen deze null zijn geworden
			// wegens een detach.
			columns = collectColumns(columnModel);

			// zoek breedste kolom
			for (String line : lines)
			{
				String[] cells = line.split(";");
				for (int i = 0; i < relativeColumnWidths.length; i++)
					if (i < cells.length && cells[i].length() > relativeColumnWidths[i])
						relativeColumnWidths[i] = cells[i].length();
			}

			for (int i = 0; i < relativeColumnWidths.length; i++)
				totalWidth += relativeColumnWidths[i];

			// reken de relatieve breedtes nu om naar absolute breedtes
			// de strategie is als volgt:
			// Er wordt onderscheid gemaakt tussen "brede" en "smalle" kolommen. Een brede
			// kolom is breder dan gemiddeld, een smalle
			// is smaller dan gemiddeld. De kolombreedtes worden in principe gelijkelijk
			// verdeeld, rekening houdend met de relatieve breedtes.
			// Voor "smalle" kolommen geldt echter een minimum van 5 pixels per karakter.
			// Dit gaat dus ten koste van de rest.

			int average = totalWidth / relativeColumnWidths.length;

			// controleer eerst of het uberhaupt mogelijk is om alle "smalle" kolommen hun
			// minimale breedte te geven: als de "brede" kolommen onder het gemiddelde uit
			// zouden
			// komen, wordt dit niet gedaan.
			int totalMinWidth = 0;
			for (int i = 0; i < relativeColumnWidths.length; i++)
			{
				if (relativeColumnWidths[i] < average)
					totalMinWidth += MIN_WIDTH_PER_CHAR * relativeColumnWidths[i];
				else
					totalMinWidth += MIN_WIDTH_PER_CHAR * average;
			}
			boolean allowMinWidth = totalMinWidth < pageWidth;

			int[] columnWidths = new int[relativeColumnWidths.length];
			int remainingWidth = pageWidth;
			int remainingTotal = totalWidth;
			// verdeel eerst de ruimte over de "smalle" kolommen
			for (int i = 0; i < relativeColumnWidths.length; i++)
			{
				if (relativeColumnWidths[i] < average)
				{
					columnWidths[i] =
						(int) (remainingWidth * ((float) relativeColumnWidths[i] / (float) remainingTotal));
					int verschil = MIN_WIDTH_PER_CHAR * relativeColumnWidths[i] - columnWidths[i];
					if (allowMinWidth && verschil > 0)
					{
						remainingWidth = remainingWidth - columnWidths[i] - verschil;
						remainingTotal -= relativeColumnWidths[i];
						columnWidths[i] += verschil;
						if (remainingTotal <= 0)
							remainingTotal = 1; // om te voorkomen dat we delen door 0
					}
				}
			}
			// verdeel dan de resterende ruimte over de "brede" kolommen
			for (int i = 0; i < relativeColumnWidths.length; i++)
			{
				if (relativeColumnWidths[i] >= average)
				{
					columnWidths[i] =
						(int) (remainingWidth * ((float) relativeColumnWidths[i] / (float) remainingTotal));
				}
			}

			JRDesignGroup group = null;
			if (groupingProperty != null)
			{
				group = new JRDesignGroup();
				group.setStartNewPage(groupingProperty.getStartNewPage());
				group.setReprintHeaderOnEachPage(true);
				group.setName("defaultGrouping");
				JRDesignExpression groupExpression = new JRDesignExpression();
				groupExpression.setText("$F{COLUMN_" + colIndex + "}");
				groupExpression.setValueClass(String.class);
				group.setExpression(groupExpression);
				design.addGroup(group);
				JRDesignBand groupHeader = new JRDesignBand();
				groupHeader.setHeight(15);
				JRDesignTextField groupField = new JRDesignTextField();
				groupField.setX(0);
				groupField.setY(0);
				groupField.setWidth(pageWidth);
				groupField.setHeight(14);
				groupField.setFontSize(8);
				groupField.setBackcolor(new Color(0xE6, 0xE6, 0xE6));
				groupField.setMode(JRElement.MODE_OPAQUE);
				groupField.setForecolor(new Color(0, 105, 200));
				groupField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
				groupField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
				groupField.setBlankWhenNull(true);
				JRDesignExpression groupFieldExpression = new JRDesignExpression();
				groupFieldExpression.setValueClass(String.class);
				groupFieldExpression.setText("$F{COLUMN_" + colIndex
					+ "} + \" (\" + $V{defaultGrouping_COUNT}.toString() + \")\"");
				groupField.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
				groupField.setExpression(groupFieldExpression);
				groupField.setEvaluationGroup(group);
				groupHeader.addElement(groupField);
				JRDesignLine line = new JRDesignLine();
				line.setX(0);
				line.setY(14);
				line.setWidth(pageWidth);
				line.setHeight(1);
				groupHeader.addElement(line);
				group.setGroupHeader(groupHeader);
			}

			colIndex = 0;
			int currentX = 0;
			for (CustomColumn<T> column : columns)
			{
				int columnWidth = 0;
				if (column instanceof AbstractColumn< ? >)
				{
					// Voeg header toe.
					JRDesignBand header = (JRDesignBand) design.getColumnHeader();
					JRDesignStaticText headerField = new JRDesignStaticText();
					headerField.setX(currentX);
					headerField.setY(0);
					headerField.setHeight(12);
					headerField.setFontSize(8);
					headerField.setBackcolor(new Color(0xC0, 0xC0, 0xC0));
					headerField.setMode(JRElement.MODE_TRANSPARENT);
					headerField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
					String text = columnNames[columns.indexOf(column)];
					headerField.setText(text);
					header.addElement(headerField);
					columnWidth = columnWidths[colIndex];
					// (int) (pageWidth * ((float) relativeColumnWidths[colIndex] /
					// (float) totalWidth));
					headerField.setWidth(columnWidth);
				}

				// Voeg datakolom toe.
				JRDesignField field = new JRDesignField();
				field.setDescription("COLUMN_" + colIndex);
				field.setName("COLUMN_" + colIndex);
				field.setValueClass(String.class);
				design.addField(field);

				// Voeg tekstveld toe
				JRDesignTextField textField = new JRDesignTextField();
				textField.setX(currentX);
				textField.setY(0);
				textField.setWidth(columnWidth);
				textField.setHeight(12);
				textField.setFontSize(8);
				textField.setBackcolor(new Color(0xC0, 0xC0, 0xC0));
				textField.setMode(JRElement.MODE_TRANSPARENT);
				textField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
				textField.setBlankWhenNull(true);
				textField.setStretchWithOverflow(true);

				JRDesignExpression designExpression = new JRDesignExpression();
				designExpression.setValueClass(Boolean.class);
				designExpression.setText("new Boolean($F{COLUMN_" + colIndex + "}.contains(\""
					+ "[inactive]" + "\"))");
				JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
				conditionalStyle.setConditionExpression(designExpression);
				conditionalStyle.setForecolor(new Color(0x77, 0x77, 0x77));

				JRDesignStyle style = new JRDesignStyle();
				style.setName(" " + currentX + " " + colIndex);
				style.addConditionalStyle(conditionalStyle);
				conditionalStyle.setParentStyle(style);
				design.addStyle(style);
				textField.setStyle(style);

				if (column instanceof BorderColumn< ? >)
				{
					if (((BorderColumn<T>) column).hasBorder())
					{
						textField.getLineBox().getBottomPen().setLineColor(Color.black);
						textField.getLineBox().getBottomPen().setLineWidth(0.5f);
						textField.getLineBox().getBottomPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
						textField.getLineBox().getLeftPen().setLineColor(Color.black);
						textField.getLineBox().getLeftPen().setLineWidth(0.5f);
						textField.getLineBox().getLeftPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
						textField.getLineBox().getRightPen().setLineColor(Color.black);
						textField.getLineBox().getRightPen().setLineWidth(0.5f);
						textField.getLineBox().getRightPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
						textField.getLineBox().getTopPen().setLineColor(Color.black);
						textField.getLineBox().getTopPen().setLineWidth(0.5f);
						textField.getLineBox().getTopPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
					}
				}

				JRDesignExpression expression = new JRDesignExpression();
				expression.setValueClass(String.class);

				expression.setText("!($F{COLUMN_" + colIndex
					+ "}.contains(\"[inactive]\")) ? $F{COLUMN_" + colIndex + "} : $F{COLUMN_"
					+ colIndex + "}.substring(10)");

				// expression.setText("$F{COLUMN_" + colIndex + "}");
				textField.setExpression(expression);
				// Koppel tekstveld aan groepering.
				if (group != null)
				{
					textField.setEvaluationGroup(group);
				}

				JRDesignBand band = (JRDesignBand) design.getDetail();
				band.addElement(textField);

				colIndex++;
				currentX = currentX + columnWidth;
			}

			// Voeg groeperings datakolom toe.
			if (groupingProperty != null)
			{
				JRDesignField field = new JRDesignField();
				field.setDescription("COLUMN_" + colIndex);
				field.setName("COLUMN_" + colIndex);
				field.setValueClass(String.class);
				design.addField(field);
			}

			JasperReport report = JasperCompileManager.compileReport(design);
			JRCsvDataSource source = new JRCsvDataSource(new StringReader(export));
			source.setFieldDelimiter(';');
			source.setRecordDelimiter("\r\n");
			source.setUseFirstRowAsHeader(false);
			source.setColumnNames(columnNames);
			print = JasperFillManager.fillReport(report, parameters, source);
		}
		catch (JRException e)
		{
			log.error(e.toString(), e);
		}
		catch (IOException e)
		{
			log.error(e.toString(), e);
		}
		return print;

	}

	/**
	 * @return een input stream van de pdf template. default is dit
	 *         "classiclandscape.jrxml".
	 */
	protected InputStream getTemplateInputStream()
	{
		return CustomDataPanelAfdrukkenLink.class.getResourceAsStream("classiclandscape.jrxml");
	}

	private List<CustomColumn<T>> collectColumns(ColumnModel<T> columnModel)
	{
		List<CustomColumn<T>> columns = columnModel.getObject();
		Iterator<CustomColumn<T>> columnIt = columns.iterator();
		while (columnIt.hasNext())
		{
			CustomColumn<T> cur = columnIt.next();
			if (cur instanceof HideableColumn< ? >)
			{
				HideableColumn< ? > hideCol = (HideableColumn< ? >) cur;
				if (!hideCol.isColumnVisible() || !hideCol.isColumnVisibleInExport())
					columnIt.remove();
			}
		}
		return columns;
	}
}
