package nl.topicus.eduarte.krdparticipatie.rapportage;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.web.components.models.LegendaItem;
import nl.topicus.eduarte.krdparticipatie.web.components.models.LegendaModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.BlokkenGenerator;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.AbsentieModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.AfspraakModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.Blok;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.DagModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.UrenModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtUtil.WaarnemingenModel;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter.Dag;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter.Week;

import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeekoverzichtRapportageUtil
{
	protected static final Logger log = LoggerFactory.getLogger(WeekoverzichtRapportageUtil.class);

	private AanwezigheidsWeekFilter filter;

	public WeekoverzichtRapportageUtil(AanwezigheidsWeekFilter filter)
	{
		this.filter = filter;
	}

	@SuppressWarnings("unchecked")
	public JasperPrint generateDocuments(List<Verbintenis> verbintenissen, byte[] bestand)
			throws JRException
	{
		List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
		JasperPrint firstPrint = null;
		boolean isFirstPrint = true;
		for (IdObject object : verbintenissen)
		{
			Verbintenis verbintenis = (Verbintenis) object;
			filter.setDeelnemer(verbintenis.getDeelnemer());
			Deelnemer deelnemer = verbintenis.getDeelnemer();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nummer", Long.valueOf(deelnemer.getDeelnemernummer()));
			parameters.put("roepnaam", deelnemer.getPersoon().getRoepnaam());
			parameters.put("achternaam", deelnemer.getPersoon().getVoorvoegselAchternaam());
			parameters.put("geboortedatum", TimeUtil.getInstance().formatDate(
				deelnemer.getPersoon().getGeboortedatum()));
			if (deelnemer.getPersoon().getEersteTelefoon() != null)
				parameters.put("telefoonnummer", deelnemer.getPersoon().getEersteTelefoon()
					.getFormattedContactgegeven());
			parameters.put("opleiding", verbintenis.getOpleiding() == null ? "" : verbintenis
				.getOpleiding().getNaam());
			parameters.put("begeleiders", deelnemer.getBegeleidersOpDatum(TimeUtil.getInstance()
				.currentDate()));
			parameters.put("title", "Participatieweekoverzicht");

			ByteArrayInputStream is = new ByteArrayInputStream(bestand);
			JasperDesign design = JRXmlLoader.load(is);

			int currentWeek = 0;
			for (Week week : filter.getWeken())
			{
				currentWeek++;
				createWeekLabel(design, week);
				DagModel dagModel =
					new DagModel(week, verbintenis.getOrganisatieEenheid(), verbintenis
						.getLocatie());
				List<Dag> winkelDagen = dagModel.getObject();
				int currentY = 0;
				for (Dag dag : winkelDagen)
				{
					createDag(design, dag, currentY);
					currentY += 62;
				}
				// if laatste week
				if (filter.getWeken().size() == currentWeek)
				{
					addLegenda(design);
				}

				JasperReport report = JasperCompileManager.compileReport(design);
				if (isFirstPrint)
				{
					firstPrint = JasperFillManager.fillReport(report, parameters);
					isFirstPrint = false;
				}
				else
				{
					JasperPrint print = JasperFillManager.fillReport(report, parameters);
					pages.addAll(print.getPages());
				}
			}
		}

		for (JRPrintPage page : pages)
		{
			if (firstPrint != null)
				firstPrint.addPage(page);
		}
		return firstPrint;
	}

	public void addLegenda(JasperDesign design)
	{
		LegendaModel legendaModel = new LegendaModel();
		List<LegendaItem> legendaItems = legendaModel.getObject();

		int currentY = 12;
		int eersteWidth = 20;
		int width = 250;
		int height = 12;

		JRDesignBand footer = (JRDesignBand) design.getSummary();
		footer.setHeight(height * (legendaItems.size() + 2));
		Color grijs = new Color(0x80, 0x80, 0x80);
		for (LegendaItem legendaItem : legendaItems)
		{
			for (int i = 0; i < 2; i++)
			{
				JRDesignStaticText textField = new JRDesignStaticText();
				if (i == 1)
					textField.setText(legendaItem.getOmschrijving());

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
					if (legendaItem.getColor().contains("blue"))
						textField.setBackcolor(Color.blue);
					if (legendaItem.getColor().contains("purple"))
						textField.setBackcolor(new Color(0xb5, 0x5b, 0xa6));
					if (legendaItem.getColor().contains("light_blue"))
						textField.setBackcolor(new Color(0xad, 0xb4, 0xe6));
					if (legendaItem.getColor().contains("green"))
						textField.setBackcolor(Color.green);
					if (legendaItem.getColor().contains("red"))
						textField.setBackcolor(Color.red);
					if (legendaItem.getColor().contains("orange"))
						textField.setBackcolor(Color.orange);
					if (legendaItem.getColor().contains("yellow"))
						textField.setBackcolor(Color.yellow);
					if (legendaItem.getColor().contains("light_green"))
						textField.setBackcolor(new Color(0xA4, 0xEE, 0xA1));
					textField.setMode(JRElement.MODE_OPAQUE);
				}
			}
			currentY += height;
		}
	}

	private void createWeekLabel(JasperDesign design, Week week)
	{
		int jaar = TimeUtil.getInstance().getYear(week.getStart());
		String dataString =
			" (" + TimeUtil.getInstance().formatDate(week.getStart()) + " - "
				+ TimeUtil.getInstance().formatDate(week.getEnd()) + ")";
		String text = "Aanwezigheid week " + week.getWeekNr() + "-" + jaar + dataString;
		JRDesignBand header = (JRDesignBand) design.getPageHeader();
		JRDesignStaticText textField = new JRDesignStaticText();
		textField.setText(text);
		textField.setMode(JRElement.MODE_OPAQUE);
		textField.setWidth(300);
		textField.setHeight(12);
		textField.setY(0);
		textField.setX(0);
		textField.setFontSize(8);
		textField.setForecolor(Color.black);
		textField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		header.addElement(textField);
	}

	private void createDag(JasperDesign design, Dag dag, int currentY)
	{
		JRDesignBand header = (JRDesignBand) design.getColumnHeader();
		int y = currentY;
		int x = 0;
		header.addElement(getDagField(dag, y));
		y += 12;
		header.addElement(getFirstUrenField(x, y));
		UrenModel model = new UrenModel(7, 23);
		List<String> uren = model.getObject();
		x += 10;
		for (String uur : uren)
		{
			header.addElement(getUrenField(uur, x, y));
			x += 40;
		}
		AfspraakModel afspraakModel = new AfspraakModel(filter, null);
		WaarnemingenModel waarnemingenModel = new WaarnemingenModel(filter);
		AbsentieModel absentieModel = new AbsentieModel(filter);
		y += 12;
		tekenBlokken("A", afspraakModel, dag, header, y, true);
		y += 12;
		tekenBlokken("W", waarnemingenModel, dag, header, y, false);
		y += 12;
		tekenBlokken("M", absentieModel, dag, header, y, false);
	}

	@SuppressWarnings("unchecked")
	private void tekenBlokken(String label, IModel model, Dag dag, JRDesignBand header, int y,
			boolean firstRow)
	{
		List<IParticipatieBlokObject> afspraken = (List<IParticipatieBlokObject>) model.getObject();
		BlokkenGenerator blokkenGenerator = new BlokkenGenerator(7, 23, 15);
		List<Blok> blokList = blokkenGenerator.maakBlokken(dag.getDatum(), afspraken);
		int x = 0;
		header.addElement(getFirstLabel(label, x, y));
		x += 10;
		for (Blok blok : blokList)
		{
			int size = blok.getAantalBlokken() * 10;
			header.addElement(getBlokField(blok, size, x, y, firstRow));
			x += size;
		}

	}

	private JRDesignElement getBlokField(Blok blok, int size, int x, int y, boolean firstRow)
	{
		JRDesignStaticText textField = new JRDesignStaticText();

		textField.setMode(JRElement.MODE_OPAQUE);
		textField.setWidth(size);
		textField.setHeight(12);
		textField.setY(y);
		textField.setX(x);
		if (blok.getCssClass() != null)
		{
			if (blok.getCssClass().contains("blue"))
				textField.setBackcolor(Color.blue);
			if (blok.getCssClass().contains("purple"))
				textField.setBackcolor(new Color(0xb5, 0x5b, 0xa6));
			if (blok.getCssClass().contains("light_blue"))
				textField.setBackcolor(new Color(0xad, 0xb4, 0xe6));
			if (blok.getCssClass().contains("green"))
				textField.setBackcolor(Color.green);
			if (blok.getCssClass().contains("red"))
				textField.setBackcolor(Color.red);
			if (blok.getCssClass().contains("orange"))
				textField.setBackcolor(Color.orange);
			if (blok.getCssClass().contains("yellow"))
				textField.setBackcolor(Color.yellow);
			if (blok.getCssClass().contains("light_green"))
				textField.setBackcolor(new Color(0xA4, 0xEE, 0xA1));
		}
		if (firstRow)
		{
			JRLineBox headerBox = textField.getLineBox();
			headerBox.getTopPen().setLineColor(Color.gray);
			headerBox.getTopPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
			headerBox.getTopPen().setLineWidth(0.5f);
		}
		return textField;
	}

	private JRDesignElement getFirstLabel(String text, int currentX, int currentY)
	{
		JRDesignStaticText textField = new JRDesignStaticText();
		textField.setText(text);
		textField.setMode(JRElement.MODE_TRANSPARENT);
		textField.setWidth(10);
		textField.setHeight(12);
		textField.setY(currentY);
		textField.setX(currentX);
		textField.setFontSize(8);
		textField.setForecolor(Color.black);
		textField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		return textField;
	}

	private JRDesignElement getFirstUrenField(int currentX, int currentY)
	{
		JRDesignStaticText textField = new JRDesignStaticText();
		textField.setMode(JRElement.MODE_TRANSPARENT);
		textField.setWidth(10);
		textField.setHeight(12);
		textField.setY(currentY);
		textField.setX(currentX);
		textField.setFontSize(8);
		textField.setForecolor(Color.gray);
		textField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		JRLineBox headerBox = textField.getLineBox();
		headerBox.getPen().setLineColor(Color.gray);
		headerBox.getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
		headerBox.getPen().setLineWidth(0.5f);
		return textField;
	}

	private JRDesignElement getDagField(Dag dag, int currentY)
	{
		JRDesignStaticText textField = new JRDesignStaticText();
		textField.setMode(JRElement.MODE_OPAQUE);
		textField.setWidth(200);
		textField.setHeight(12);
		textField.setText(dag.toString());
		textField.setY(currentY);
		textField.setX(10);
		textField.setFontSize(8);
		textField.setForecolor(Color.gray);
		textField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		return textField;
	}

	private JRDesignElement getUrenField(String uur, int currentX, int currentY)
	{
		JRDesignStaticText textField = new JRDesignStaticText();
		textField.setMode(JRElement.MODE_OPAQUE);
		textField.setWidth(40);
		textField.setHeight(12);
		textField.setText(uur);
		textField.setY(currentY);
		textField.setX(currentX);
		textField.setFontSize(8);
		textField.setForecolor(Color.gray);
		textField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
		JRLineBox headerBox = textField.getLineBox();
		headerBox.getPen().setLineColor(Color.gray);
		headerBox.getPen().setLineStyle(JRPen.LINE_STYLE_SOLID);
		headerBox.getPen().setLineWidth(0.5f);
		return textField;
	}
}