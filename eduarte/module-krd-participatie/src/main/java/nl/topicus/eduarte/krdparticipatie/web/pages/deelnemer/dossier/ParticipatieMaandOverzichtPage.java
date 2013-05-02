/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.cobra.web.components.listview.ExportableListView;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerMaandOverzichtRapportage;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.filter.ParticipatieMaandAanwezigheidZoekFilterPanel;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidMaandFilter.Maand;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;

/**
 * @author vandekamp
 */
@PageInfo(title = "Maandoverzicht", menu = "Deelnemer > [deelnemer] > Participatie > Maandoverzicht")
@InPrincipal(DeelnemerMaandOverzichtRapportage.class)
public class ParticipatieMaandOverzichtPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	private String export;

	private final ParticipatieAanwezigheidMaandZoekFilter filter;

	private static final ParticipatieAanwezigheidMaandZoekFilter getDefaultFilter(
			Deelnemer deelnemer)
	{
		ParticipatieAanwezigheidMaandZoekFilter filter =
			new ParticipatieAanwezigheidMaandZoekFilter(deelnemer);
		Maand maand = Maand.getHuidigeMaand();
		filter.setTotMaand(maand);
		maand = maand.vorigeMaand().vorigeMaand().vorigeMaand();
		filter.setVanafMaand(maand);
		filter.setAlleenIIVOAfspraken(false);

		return filter;
	}

	public ParticipatieMaandOverzichtPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer().getEersteInschrijvingOpPeildatum());
	}

	public ParticipatieMaandOverzichtPage(Deelnemer deelnemer)
	{
		this(deelnemer, null);
	}

	public ParticipatieMaandOverzichtPage(Verbintenis verbintenis)
	{
		this(verbintenis.getDeelnemer(), verbintenis);
	}

	public ParticipatieMaandOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this(deelnemer, verbintenis, getDefaultFilter(deelnemer));
	}

	public ParticipatieMaandOverzichtPage(Deelnemer deelnemer, Verbintenis verbintenis,
			ParticipatieAanwezigheidMaandZoekFilter filter)
	{
		super(ParticipatieDeelnemerMenuItem.Maandoverzicht, deelnemer, verbintenis);
		this.filter = filter;
		add(new ParticipatieMaandAanwezigheidZoekFilterPanel("filter", filter));
		add(ComponentFactory.getDataLabel("vanafMaand", new PropertyModel<Maand>(filter,
			"vanafMaand.langeNaam")));
		add(ComponentFactory.getDataLabel("totMaand", new PropertyModel<Maand>(filter,
			"totMaand.langeNaam")));

		ExportableListView<ParticipatieMaandOverzicht> overzichten =
			new ExportableListView<ParticipatieMaandOverzicht>("overzicht",
				new OverzichtModel(this))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<ParticipatieMaandOverzicht> item)
				{
					item.add(ComponentFactory.getDataLabel("maand.langeNaam"));
					item.add(ComponentFactory.getDataLabel("aantalAfspraken"));
					item.add(ComponentFactory.getDataLabel("totaalUrenAfspraken"));
					item.add(ComponentFactory.getDataLabel("aanwezigheidVereistAfspraken"));
					item.add(ComponentFactory.getDataLabel("openAfspraken"));
					item.add(ComponentFactory.getDataLabel("urenPresentieWaarnemingen"));
					item.add(ComponentFactory.getDataLabel("urenAbsentieWaarnemingen"));
					item.add(ComponentFactory.getDataLabel("percentagePresent"));
					item.add(ComponentFactory.getDataLabel("aantalAbsentiemeldingen"));
					item.add(ComponentFactory.getDataLabel("dagenAbsentGemeld"));
					item.add(ComponentFactory.getDataLabel("urenAbsentGemeld"));
				}

				@Override
				protected IModel<ParticipatieMaandOverzicht> getListItemModel(
						IModel< ? extends List<ParticipatieMaandOverzicht>> listViewModel, int index)
				{
					return new CompoundPropertyModel<ParticipatieMaandOverzicht>(super
						.getListItemModel(listViewModel, index));
				}

				@Override
				public boolean allowSkipColumns()
				{
					return true;
				}
			};
		add(overzichten);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractLinkButton(panel, "PDF genereren", CobraKeyAction.LINKKNOP1,
			ButtonAlignment.RIGHT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				getPdfReport();
			}

		});
	}

	@Override
	protected void onAfterRender()
	{
		super.onAfterRender();
		try
		{
			ExportableListView< ? > listview = (ExportableListView< ? >) get("overzicht");
			StringWriter writer = new StringWriter(listview.size());
			listview.writeExport(writer);
			export = writer.toString();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private final class OverzichtModel extends
			LoadableDetachableModel<List<ParticipatieMaandOverzicht>>
	{
		private static final long serialVersionUID = 1L;

		private Component parent;

		public OverzichtModel(Component parent)
		{
			this.parent = parent;
		}

		@Override
		protected List<ParticipatieMaandOverzicht> load()
		{
			List<ParticipatieMaandOverzicht> res = new ArrayList<ParticipatieMaandOverzicht>();
			filter
				.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(parent));
			Maand maand = filter.getTotMaand();
			while (maand != null)
			{
				ParticipatieMaandOverzicht overzicht = new ParticipatieMaandOverzicht();
				overzicht.setMaand(maand);
				DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class).getMaandOverzicht(
					filter, overzicht);
				DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class)
					.getMaandOverzicht(filter, overzicht);
				DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).getMaandOverzicht(
					filter, overzicht);
				res.add(overzicht);
				if (maand.equals(filter.getVanafMaand()))
				{
					break;
				}
				maand = maand.vorigeMaand();
			}
			return res;
		}
	}

	@SuppressWarnings( {"unchecked", "deprecation"})
	private void getPdfReport()
	{
		HashMap params = new HashMap();
		params.put("Verbintenis", Collections.singletonList(getContextVerbintenis().getId()));
		params.put("ImageDir", EduArteRequestCycle.get().getImageDirectory());
		Deelnemer deelnemer = getContextDeelnemer();
		Persoon persoon = deelnemer.getPersoon();

		params.put("nummer", deelnemer.getDeelnemernummer());
		params.put("roepnaam", persoon.getRoepnaam());
		params.put("achternaam", persoon.getVoorvoegselAchternaam());
		params.put("geboortedatum", TimeUtil.getInstance().formatDate(
			deelnemer.getPersoon().getGeboortedatum()));
		if (persoon.getEersteTelefoon() != null)
			params.put("telefoonnummer", persoon.getEersteTelefoon().getFormattedContactgegeven());
		params.put("opleiding", getContextVerbintenis().getOpleiding().getNaam());
		params.put("begeleiders", deelnemer.getBegeleidersOpDatum(EduArteContext.get()
			.getPeildatumOfVandaag()));

		List<String> columns = new ArrayList<String>(12);
		columns.add("0");
		columns.add("1");
		columns.add("2");
		columns.add("3");
		columns.add("4");
		columns.add("5");
		columns.add("6");
		columns.add("7");
		columns.add("8");
		columns.add("9");
		columns.add("10");

		String[] columnNames = new String[columns.size()];
		int totalWidth = 0;
		try
		{
			// Get template.
			InputStream is =
				CustomDataPanelAfdrukkenLink.class
					.getResourceAsStream("/nl/topicus/eduarte/krdparticipatie/web/reports/DeelnemerParticipatieMaandOverzicht.jrxml");
			JasperDesign design = JRXmlLoader.load(is);
			// int pageWidth =
			// design.getPageWidth() - design.getLeftMargin() - design.getRightMargin() -
			// 132;

			int colIndex = 0;
			for (String column : columns)
			{
				String text = null;
				text = column;
				totalWidth = totalWidth + 50;
				columnNames[colIndex] = text;
				colIndex++;
			}
			colIndex = 0;
			int currentX = 0;
			for (String column : columns)
			{
				int columnWidth = 0;

				JRDesignStaticText headerField = new JRDesignStaticText();
				headerField.setX(currentX);
				headerField.setY(129);
				headerField.setHeight(12);
				headerField.setFontSize(7);
				headerField.setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
				headerField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);

				headerField.setForecolor(new Color(0x21, 0x58, 0xD3));
				headerField.setBackcolor(new Color(0xF2, 0xF2, 0xF2));
				headerField.setTopBorder(new Byte("1"));

				headerField.setBorderColor(new Color(0xDF, 0xDF, 0xDF));
				headerField.setMode(JRElement.MODE_TRANSPARENT);
				String text = column;
				headerField.setText(text);

				// geen header toevoegen, wordt in jrxml statisch ingevuld
				// header.addElement(headerField);
				headerField.setLeftPadding(5);

				// Voeg datakolom toe.
				JRDesignField field = new JRDesignField();
				field.setDescription("COLUMN_" + colIndex);
				field.setName("COLUMN_" + colIndex);
				field.setValueClass(String.class);
				design.addField(field);
				JRDesignTextField textField = new JRDesignTextField();
				textField.setX(currentX);
				textField.setY(0);
				textField.setHeight(12);
				textField.setFontSize(8);
				textField.setBottomBorder(new Byte("1"));
				textField.setBorderColor(new Color(0xDF, 0xDF, 0xDF));
				textField.setMode(JRElement.MODE_TRANSPARENT);
				textField.setLeftPadding(5);
				textField.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
				textField.setBlankWhenNull(true);
				JRDesignExpression expression = new JRDesignExpression();
				expression.setValueClass(String.class);

				if (colIndex == 0)
				{
					textField.setLeftBorder(new Byte("1"));
					headerField.setLeftBorder(new Byte("1"));
					expression.setText("$F{COLUMN_" + colIndex + "}");
					columnWidth = 100;
				}
				else if (colIndex == 1 || colIndex == 5 || colIndex == 6 || colIndex == 7
					|| colIndex == 8)
				{
					textField.setLeftBorder(new Byte("1"));
					headerField.setLeftBorder(new Byte("1"));
					expression.setText("$F{COLUMN_" + colIndex + "}");
					columnWidth = 30;
					if (colIndex == 7)
						columnWidth = 50;
				}
				if (colIndex == 2 || colIndex == 3 || colIndex == 4 || colIndex == 5
					|| colIndex == 6)
				{
					expression.setText("$F{COLUMN_" + colIndex + "} + \" uur\"");
					columnWidth = 60;
					if (colIndex == 10)
						textField.setLeftBorder(new Byte("1"));
				}
				else
				{
					expression.setText("$F{COLUMN_" + colIndex + "}");
					columnWidth = 60;
				}
				if (colIndex == 10)
					textField.setRightBorder(new Byte("1"));

				textField.setExpression(expression);
				headerField.setWidth(columnWidth);
				textField.setWidth(columnWidth);

				JRDesignBand band = (JRDesignBand) design.getDetail();
				band.addElement(textField);

				colIndex++;
				currentX = currentX + columnWidth;
			}

			JasperReport report = JasperCompileManager.compileReport(design);

			JRCsvDataSource source = new JRCsvDataSource(new StringReader(export));
			source.setFieldDelimiter(';');
			source.setRecordDelimiter("\r\n");
			source.setUseFirstRowAsHeader(false);
			source.setColumnNames(columnNames);
			JasperPrint print = JasperFillManager.fillReport(report, params, source);
			File file = File.createTempFile("DeelnemerResultaten", ".pdf");
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
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
