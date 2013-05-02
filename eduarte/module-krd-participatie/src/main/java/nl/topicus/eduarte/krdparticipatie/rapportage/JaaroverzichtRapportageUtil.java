package nl.topicus.eduarte.krdparticipatie.rapportage;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.web.components.models.TotalenModel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingTotaalOverzicht;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WeekWaarnemingen;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaaroverzichtRapportageUtil
{
	protected static final Logger log = LoggerFactory.getLogger(JaaroverzichtRapportageUtil.class);

	private WaarnemingOverzichtZoekFilter filter;

	public JaaroverzichtRapportageUtil(WaarnemingOverzichtZoekFilter filter)
	{
		this.filter = filter;
	}

	@SuppressWarnings( {"unchecked", "deprecation"})
	public JasperPrint generateDocuments(List<Verbintenis> verbintenissen, byte[] bestand)
			throws JRException
	{
		List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
		JasperPrint firstPrint = null;
		boolean isFirstPrint = true;
		for (Verbintenis verbintenis : verbintenissen)
		{
			filter.setDeelnemer(verbintenis.getDeelnemer());
			StringWriter writer = new StringWriter(1000);
			Deelnemer deelnemer = verbintenis.getDeelnemer();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nummer", deelnemer.getDeelnemernummer());
			parameters.put("roepnaam", deelnemer.getPersoon().getRoepnaam());
			parameters.put("achternaam", deelnemer.getPersoon().getVoorvoegselAchternaam());
			parameters.put("geboortedatum", TimeUtil.getInstance().formatDate(
				deelnemer.getPersoon().getGeboortedatum()));
			if (deelnemer.getPersoon().getEersteTelefoon() != null)
				parameters.put("telefoonnummer", deelnemer.getPersoon().getEersteTelefoon()
					.getFormattedContactgegeven());
			parameters.put("opleiding", verbintenis.getOpleiding().getNaam());
			parameters.put("begeleiders", deelnemer.getBegeleidersOpDatum(EduArteContext.get()
				.getPeildatum()));

			LesweekIndeling lesweekIndeling = filter.getLesweekIndeling();
			List<String> kolommen = getKolommen(lesweekIndeling);
			Date datum = filter.getBeginWeek(filter.getBeginDatum());
			for (; !datum.after(filter.getEindDatum());)
			{
				WeekWaarnemingen weekWaarnemingen = new WeekWaarnemingen(datum, deelnemer, filter);
				String weekString = "Week-" + TimeUtil.getInstance().getWeekOfYear(datum) + ";";

				boolean heeftLabel = false;
				for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
				{
					int dagVanWeek = lesdag.getDagNummer();
					for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
					{
						if ((filter.getTotLesuur() == null || lestijd.getLesuur() <= filter
							.getTotLesuur().getLesuur())
							&& (filter.getVanafLesuur() == null || lestijd.getLesuur() >= filter
								.getVanafLesuur().getLesuur()))
						{
							String label = "";
							int lesuur = lestijd.getLesuur();
							if (lesuur >= getVanLesuur(dagVanWeek)
								&& lesuur <= getTotLesuur(lesweekIndeling, dagVanWeek))
							{
								label = weekWaarnemingen.getLabel(dagVanWeek, lestijd);
								if (StringUtil.isNotEmpty(label))
									heeftLabel = true;
								weekString += label;
							}
							String color = weekWaarnemingen.getLastLabelColor();
							{
								weekString += color;
								heeftLabel = true;
							}
							if (label.length() > 0)
								weekString += ";";
						}
					}
				}
				datum = TimeUtil.getInstance().addWeeks(datum, 1);
				if (filter.isToonTotalenKolommen())
				{
					weekString += weekWaarnemingen.getTotaalAanwezig() + ";";
					weekString += weekWaarnemingen.getTotaalGeoorloofdAbsent() + ";";
					weekString += weekWaarnemingen.getTotaalOngeoorloofdAbsent() + ";";

				}
				weekString += "\r\n";
				if (filter.isToonLegeRegels() || heeftLabel)
					writer.write(weekString);
			}

			ByteArrayInputStream is = new ByteArrayInputStream(bestand);
			JasperDesign design = JRXmlLoader.load(is);
			String export = writer.toString();

			int hokjesBreedte = 11;
			int currentX = 50;
			Color grijs = new Color(0x80, 0x80, 0x80);
			for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
			{
				int dagVanWeek = lesdag.getDagNummer();
				String text = TimeUtil.getInstance().getWeekdagNaam(dagVanWeek);
				JRDesignBand header = (JRDesignBand) design.getColumnHeader();
				JRDesignStaticText headerField = new JRDesignStaticText();
				headerField.setX(currentX);
				headerField.setY(80);
				headerField.setHeight(12);
				headerField.setFontSize(8);
				headerField.setForecolor(grijs);
				headerField.setMode(JRElement.MODE_TRANSPARENT);
				headerField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
				headerField.setText(text);

				header.addElement(headerField);
				int aantalLesuren =
					(getTotLesuur(lesweekIndeling, dagVanWeek) - getVanLesuur(dagVanWeek)) + 1;

				int columnWidth = aantalLesuren * hokjesBreedte;
				headerField.setWidth(columnWidth);
				currentX = currentX + columnWidth;
			}

			int colIndex = 0;
			currentX = 0;
			hokjesBreedte = 50;

			boolean gekleurd = false;
			for (String text : kolommen)
			{
				Color backColor = new Color(0xFF, 0xFF, 0xFF);
				if (gekleurd)
					backColor = new Color(0xFF, 0xFF, 0xD0);
				gekleurd = !gekleurd;
				// Voeg header toe.
				JRDesignBand header = (JRDesignBand) design.getColumnHeader();
				JRDesignStaticText headerField = new JRDesignStaticText();
				headerField.setX(currentX);
				headerField.setY(92);
				headerField.setHeight(12);
				headerField.setFontSize(8);
				headerField.setForecolor(grijs);
				headerField.setBackcolor(backColor);
				headerField.setMode(JRElement.MODE_OPAQUE);
				headerField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
				headerField.setText(text);

				JRLineBox headerBox = headerField.getLineBox();
				headerBox.getPen().setLineColor(grijs);
				headerBox.getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
				headerBox.getPen().setLineWidth(0.5f);

				if (isEindeLesdag(lesweekIndeling, colIndex))
				{
					headerBox.getRightPen().setLineColor(Color.black);
					headerBox.getLeftPen().setLineWidth(JRPen.LINE_WIDTH_0);
				}
				else if (isEindeLesdag(lesweekIndeling, colIndex - 1))
				{
					headerBox.getLeftPen().setLineColor(Color.black);
					headerBox.getRightPen().setLineWidth(JRPen.LINE_WIDTH_0);
				}
				else
				{
					headerBox.getLeftPen().setLineWidth(JRPen.LINE_WIDTH_0);
					headerBox.getRightPen().setLineWidth(JRPen.LINE_WIDTH_0);
				}
				header.addElement(headerField);
				headerField.setWidth(hokjesBreedte);

				// Voeg datakolom toe.
				JRDesignField field = new JRDesignField();
				field.setDescription("COLUMN_" + colIndex);
				field.setName("COLUMN_" + colIndex);
				field.setValueClass(String.class);
				design.addField(field);

				// Voeg tekstveld toe
				JRDesignTextField textField = new JRDesignTextField();

				JRLineBox box = textField.getLineBox();
				box.getPen().setLineColor(grijs);
				box.getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
				box.getPen().setLineWidth(0.5f);

				if (isEindeLesdag(lesweekIndeling, colIndex))
				{
					box.getRightPen().setLineColor(Color.black);
				}
				else if (isEindeLesdag(lesweekIndeling, colIndex - 1))
				{
					box.getLeftPen().setLineColor(Color.black);
				}
				if (colIndex == 0)
				{
					textField.setForecolor(grijs);
					box.getLeftPen().setLineWidth(JRPen.LINE_WIDTH_0);
				}

				textField.setX(currentX);
				textField.setY(0);
				textField.setWidth(hokjesBreedte);
				textField.setHeight(12);
				textField.setFontSize(8);

				textField.setMode(JRElement.MODE_OPAQUE);
				textField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
				textField.setBlankWhenNull(true);
				textField.setStretchWithOverflow(false);

				JRDesignExpression expression = new JRDesignExpression();
				expression.setValueClass(String.class);
				if (colIndex == 0 || text.equals("P") || text.equals("G") || text.equals("O"))
					expression.setText("$F{COLUMN_" + colIndex + "}");
				else
					expression.setText("($F{COLUMN_" + colIndex + "}.length() == 0) ? $F{COLUMN_"
						+ colIndex + "} : $F{COLUMN_" + colIndex + "}.substring(0,1)");
				textField.setExpression(expression);
				JRDesignBand band = (JRDesignBand) design.getDetail();

				JRDesignStyle style = new JRDesignStyle();
				style.setName(" " + currentX + " " + colIndex);
				style.setBackcolor(backColor);

				style.addConditionalStyle(getConditionalStyle(new Color(0x00, 0xFF, 0x00),
					colIndex, "Green", style));
				style.addConditionalStyle(getConditionalStyle(new Color(0xFF, 0x00, 0x00),
					colIndex, "Red", style));
				style.addConditionalStyle(getConditionalStyle(new Color(0xFF, 0x6A, 0x00),
					colIndex, "Orange", style));

				design.addStyle(style);
				textField.setStyle(style);
				band.addElement(textField);
				colIndex++;
				currentX = currentX + hokjesBreedte;
				hokjesBreedte = 11;
			}

			// totaal overzicht voor pdf
			IModel totalenModel = new TotalenModel(filter);
			List<WaarnemingTotaalOverzicht> waarnemingTotaalOverzichtList =
				(List<WaarnemingTotaalOverzicht>) totalenModel.getObject();

			int currentY = 12;
			int eersteWidth = 20;
			int width = 150;
			int height = 12;

			JRDesignBand footer = (JRDesignBand) design.getSummary();
			footer.setHeight(height * (waarnemingTotaalOverzichtList.size() + 2));
			for (int i = 0; i < 4; i++)
			{
				JRDesignStaticText textField = new JRDesignStaticText();
				if (i == 1)
					textField.setText("Type");
				if (i == 2)
					textField.setText("Klokuren");
				if (i == 3)
					textField.setText("Lesuren");
				JRLineBox box = textField.getLineBox();
				box.getBottomPen().setLineColor(grijs);
				box.getBottomPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
				box.getBottomPen().setLineWidth(0.5f);
				if (i == 0)
				{
					textField.setX(0);
					textField.setWidth(eersteWidth);
				}
				else
				{
					textField.setX(((i - 1) * width) + eersteWidth);
					textField.setWidth(width);
				}
				textField.setForecolor(grijs);
				textField.setY(currentY);
				textField.setHeight(height);
				textField.setFontSize(8);
				footer.addElement(textField);
			}
			currentY += height;

			JRDesignStyle styleGreen = new JRDesignStyle();
			styleGreen.setName("totaalStyleGreen");
			styleGreen.setBackcolor(new Color(0x00, 0xFF, 0x00));
			design.addStyle(styleGreen);
			JRDesignStyle styleOrange = new JRDesignStyle();
			styleOrange.setName("totaalStyleOrange");
			styleOrange.setBackcolor(new Color(0xFF, 0x6A, 0x00));
			design.addStyle(styleOrange);
			JRDesignStyle styleRed = new JRDesignStyle();
			styleRed.setName("totaalStyleRed");
			styleRed.setBackcolor(new Color(0xFF, 0x00, 0x00));
			design.addStyle(styleRed);

			for (WaarnemingTotaalOverzicht overzicht : waarnemingTotaalOverzichtList)
			{
				for (int i = 0; i < 4; i++)
				{
					JRDesignStaticText textField = new JRDesignStaticText();
					if (i == 1)
						textField.setText(overzicht.getType());
					if (i == 2)
						textField.setText(overzicht.getKlokuren());
					if (i == 3)
						textField.setText(overzicht.getLesuren());

					JRLineBox box = textField.getLineBox();
					box.getBottomPen().setLineColor(grijs);
					box.getBottomPen().setLineStyle(JRPen.LINE_STYLE_DASHED);
					box.getBottomPen().setLineWidth(0.5f);

					if (i == 0)
					{
						textField.setX(0);
						textField.setWidth(eersteWidth);
					}
					else
					{
						textField.setX(((i - 1) * width) + eersteWidth);
						textField.setWidth(width);
					}
					textField.setY(currentY);
					textField.setHeight(height);
					textField.setFontSize(8);
					footer.addElement(textField);
					if (i == 0)
					{
						if (overzicht.getCollor().equals("Green"))
							textField.setStyle(styleGreen);
						if (overzicht.getCollor().equals("Red"))
							textField.setStyle(styleRed);
						if (overzicht.getCollor().equals("Orange"))
							textField.setStyle(styleOrange);
						textField.setMode(JRElement.MODE_OPAQUE);
					}
				}
				currentY += height;
			}
			// einde totaal overzicht voor pdf

			JasperReport report = JasperCompileManager.compileReport(design);
			JRCsvDataSource source = new JRCsvDataSource(new StringReader(export));
			source.setFieldDelimiter(';');
			source.setRecordDelimiter("\r\n");
			source.setUseFirstRowAsHeader(false);
			if (isFirstPrint)
			{
				firstPrint = JasperFillManager.fillReport(report, parameters, source);
				isFirstPrint = false;
			}
			else
			{
				JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
				pages.addAll(print.getPages());
			}
		}

		for (JRPrintPage page : pages)
		{
			if (firstPrint != null)
				firstPrint.addPage(page);
		}
		return firstPrint;
	}

	private JRConditionalStyle getConditionalStyle(Color color, int colIndex, String colorString,
			JRDesignStyle style)
	{
		JRDesignExpression expression = new JRDesignExpression();
		expression.setValueClass(Boolean.class);
		expression.setText("new Boolean($F{COLUMN_" + colIndex + "}.contains(\"" + colorString
			+ "\"))");
		JRDesignConditionalStyle conditionalStyle = new JRDesignConditionalStyle();
		conditionalStyle.setConditionExpression(expression);
		conditionalStyle.setParentStyle(style);
		conditionalStyle.setBackcolor(color);
		return conditionalStyle;
	}

	private boolean isEindeLesdag(LesweekIndeling lesweekIndeling, int oldColIndex)
	{
		int colIndex = oldColIndex;
		for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
		{
			int dagVanWeek = lesdag.getDagNummer();
			for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
			{
				int lesuur = lestijd.getLesuur();
				if (lesuur >= getVanLesuur(dagVanWeek)
					&& lesuur <= getTotLesuur(lesweekIndeling, dagVanWeek))
				{
					colIndex--;
				}
			}
			if (colIndex == 0)
				return true;

		}
		return false;
	}

	private List<String> getKolommen(LesweekIndeling lesweekIndeling)
	{
		List<String> kolommen = new ArrayList<String>();
		kolommen.add("");
		for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
		{
			int dagVanWeek = lesdag.getDagNummer();
			for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
			{
				int lesuur = lestijd.getLesuur();
				if (lesuur >= getVanLesuur(dagVanWeek)
					&& lesuur <= getTotLesuur(lesweekIndeling, dagVanWeek))
				{
					kolommen.add("" + lesuur);
				}
			}
		}
		if (filter.isToonTotalenKolommen())
		{
			kolommen.add("P");
			kolommen.add("G");
			kolommen.add("O");
		}
		return kolommen;

	}

	/**
	 * @param lesweekIndeling
	 * @param dagVanWeek
	 * @return tot het lesuur nummer
	 */
	public int getTotLesuur(LesweekIndeling lesweekIndeling, int dagVanWeek)
	{
		int tot = 0;
		LesdagIndeling lesdagIndeling = getLesdagIndeling(lesweekIndeling, dagVanWeek);
		if (lesdagIndeling != null)
			tot = lesdagIndeling.getLesuurIndeling().size();
		if (filter.getTotLesuur() != null && filter.getTotLesuur().getLesuur() < tot)
			tot = filter.getTotLesuur().getLesuur();
		return tot;
	}

	private LesdagIndeling getLesdagIndeling(LesweekIndeling lesweekIndeling, int dagVanWeek)
	{
		for (LesdagIndeling lesdagIndeling : lesweekIndeling.getLesdagIndelingenOrderedByDay())
		{
			if (lesdagIndeling.getDagNummer() == dagVanWeek)
				return lesdagIndeling;
		}
		return null;
	}

	/**
	 * @param dagVanWeek
	 * @return vanaf lesuur nummer
	 */
	public int getVanLesuur(int dagVanWeek)
	{
		if (filter.getVanafLesuur() != null)
			return filter.getVanafLesuur().getLesuur();
		return 1;

	}
}