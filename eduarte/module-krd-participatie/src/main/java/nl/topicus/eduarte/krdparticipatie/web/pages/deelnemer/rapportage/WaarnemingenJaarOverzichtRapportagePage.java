package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.fileresources.PdfFileResourceStream;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.Schooljaar;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerJaarOverzichtRapportage;
import nl.topicus.eduarte.krdparticipatie.util.LestijdUtil;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.models.TotalenModel;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WaarnemingTotaalOverzicht;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming.WeekWaarnemingen;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.LesUurComboBox;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.GroepCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.DeelnemerTitel;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pagina voor het maken van een waarnemingen rapportage
 * 
 * @author vandekamp
 */
@PageInfo(title = "Deelnemerwaarnemingenoverzicht", menu = {"Deelnemer > Rapportage > waarnemingoverzicht > [deelnemer]"})
@InPrincipal(DeelnemerJaarOverzichtRapportage.class)
public class WaarnemingenJaarOverzichtRapportagePage extends SecurePage
{
	private static final Logger log =
		LoggerFactory.getLogger(WaarnemingenJaarOverzichtRapportagePage.class);

	private IModel<Deelnemer> deelnemerModel;

	private IModel<Verbintenis> inschrijvingModel;

	private IModel<List<Verbintenis>> inschrijvingenModel;

	private MenuItemKey selectedMenuItem;

	private WaarnemingOverzichtZoekFilter filter;

	private Form<WaarnemingOverzichtZoekFilter> form;

	private static final WaarnemingOverzichtZoekFilter getDefaultFilter()
	{
		WaarnemingOverzichtZoekFilter filter = new WaarnemingOverzichtZoekFilter();
		filter.setBeginDatum(Schooljaar.getHuidigSchooljaar().getVanafDatum());
		filter.setEindDatum(TimeUtil.getInstance().currentDate());
		filter.setAbsentieOfPresentie(AbsentiePresentieEnum.Absentie_en_Presentie);
		filter.setWaarnemingWeergave(WaarnemingWeergaveEnum.AlleenWaarneming);
		filter.setToonLegeRegels(true);
		return filter;
	}

	public WaarnemingenJaarOverzichtRapportagePage(List<Verbintenis> inschrijvingen,
			CoreMainMenuItem coreMainMenuItem)
	{
		super(coreMainMenuItem);
		this.inschrijvingenModel = ModelFactory.getListModel(inschrijvingen);
		if (coreMainMenuItem.equals(CoreMainMenuItem.Groep))
			this.selectedMenuItem = GroepCollectiefMenuItem.Rapportages;
		else
			this.selectedMenuItem = DeelnemerCollectiefMenuItem.Rapportages;
		init();
	}

	/**
	 * Constructor voor aanroepen vanuit het Competenties menu bij een deelnemer.
	 * 
	 * @param deelnemer
	 * @param verbintenis
	 */
	public WaarnemingenJaarOverzichtRapportagePage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.deelnemerModel = ModelFactory.getModel(deelnemer);
		this.inschrijvingModel = ModelFactory.getModel(verbintenis);
		this.inschrijvingenModel = ModelFactory.getListModel(Arrays.asList(verbintenis));
		this.selectedMenuItem = ParticipatieDeelnemerMenuItem.Jaaroverzicht;
		init();
	}

	public WaarnemingenJaarOverzichtRapportagePage(Verbintenis verbintenis,
			WaarnemingOverzichtZoekFilter filter)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.filter = filter;
		this.deelnemerModel = ModelFactory.getModel(verbintenis.getDeelnemer());
		this.inschrijvingModel = ModelFactory.getModel(verbintenis);
		this.inschrijvingenModel = ModelFactory.getListModel(Arrays.asList(verbintenis));
		createPDF();
	}

	private void init()
	{
		filter = getDefaultFilter();
		OrganisatieEenheid organisatieEenheid = getOrganisatieEenheidMetMeesteLestijden();

		if (organisatieEenheid == null)
		{
			error("Er zijn geen lestijden ingesteld voor deze deelnemer");
			IModel<WaarnemingOverzichtZoekFilter> filterModel =
				new CompoundPropertyModel<WaarnemingOverzichtZoekFilter>(filter);
			form = new Form<WaarnemingOverzichtZoekFilter>("form", filterModel);
			form.add(new WebMarkupContainer("beginDatum"));
			form.add(new WebMarkupContainer("eindDatum"));
			form.add(new WebMarkupContainer("vanafLesuur"));
			form.add(new WebMarkupContainer("totLesuur"));
			form.add(new WebMarkupContainer("absentieOfPresentie"));
			form.add(new WebMarkupContainer("waarnemingWeergave"));
			form.add(new WebMarkupContainer("financieringsLabel"));
			form.add(new WebMarkupContainer("toonLegeRegels"));
			form.add(new WebMarkupContainer("toonTotalenKolommen"));
			form.setVisible(false);
			add(form);
			createComponents();
			return;
		}

		List<LesuurIndeling> lestijden = LestijdUtil.getLestijden(organisatieEenheid, getLocatie());
		filter.setVanafLesuur(lestijden.get(0));
		filter.setTotLesuur(lestijden.get(lestijden.size() - 1));

		IModel<WaarnemingOverzichtZoekFilter> filterModel =
			new CompoundPropertyModel<WaarnemingOverzichtZoekFilter>(filter);
		form = new Form<WaarnemingOverzichtZoekFilter>("form", filterModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				createPDF();
			}

		};
		add(form);
		DatumField beginDatum = new DatumField("beginDatum");
		beginDatum.setRequired(true);
		form.add(beginDatum);
		DatumField eindDatum = new DatumField("eindDatum");
		eindDatum.setRequired(true);
		form.add(eindDatum);

		form.add(new LesUurComboBox("vanafLesuur", null, organisatieEenheid, getLocatie()));
		form.add(new LesUurComboBox("totLesuur", null, organisatieEenheid, getLocatie()));
		form.add(new EnumCombobox<AbsentiePresentieEnum>("absentieOfPresentie",
			AbsentiePresentieEnum.values()));
		form.add(new EnumCombobox<WaarnemingWeergaveEnum>("waarnemingWeergave",
			WaarnemingWeergaveEnum.values()));
		form.add(new ContractSearchEditor("contract", new PropertyModel<Contract>(form
			.getModelObject(), "contract")));
		form.add(new JaNeeCombobox("toonLegeRegels").setRequired(true));
		form.add(new JaNeeCombobox("toonTotalenKolommen").setRequired(true));
		createComponents();
	}

	private Locatie getLocatie()
	{
		return (inschrijvingModel.getObject()).getLocatie();
	}

	private OrganisatieEenheid getOrganisatieEenheid()
	{
		return (inschrijvingModel.getObject()).getOrganisatieEenheid();
	}

	/**
	 * Gaat de pdf maken
	 */
	@SuppressWarnings( {"unchecked", "deprecation"})
	protected void createPDF()
	{
		List<Verbintenis> inschrijvingen = inschrijvingenModel.getObject();
		List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
		JasperPrint firstPrint = null;
		boolean isFirstPrint = true;
		try
		{
			for (Verbintenis verbintenis : inschrijvingen)
			{
				filter.setDeelnemer(verbintenis.getDeelnemer());
				StringWriter writer = new StringWriter(1000);
				Deelnemer deelnemer = verbintenis.getDeelnemer();

				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("ImageDir", EduArteRequestCycle.get().getImageDirectory());
				parameters.put("nummer", deelnemer.getDeelnemernummer());
				parameters.put("roepnaam", deelnemer.getPersoon().getRoepnaam());
				parameters.put("achternaam", deelnemer.getPersoon().getVoorvoegselAchternaam());
				parameters.put("geboortedatum", TimeUtil.getInstance().formatDate(
					deelnemer.getPersoon().getGeboortedatum()));
				if (deelnemer.getPersoon().getEersteTelefoon() != null)
					parameters.put("telefoonnummer", deelnemer.getPersoon().getEersteTelefoon());
				parameters.put("opleiding", deelnemer.getEersteInschrijvingOpPeildatum()
					.getVerbintenis());
				parameters.put("begeleiders", deelnemer.getBegeleidersOpDatum(EduArteContext.get()
					.getPeildatum()));

				LesweekIndeling lesweekIndeling = getLesweekIndeling();
				if (lesweekIndeling == null)
				{
					error("Geen lestijden gevonden voor organisatie van verbintenis "
						+ verbintenis.toString());
					return;
				}
				List<String> kolommen = getKolommen(lesweekIndeling);
				Date datum = filter.getBeginWeek(filter.getBeginDatum());
				for (; !datum.after(filter.getEindDatum());)
				{
					WeekWaarnemingen weekWaarnemingen =
						new WeekWaarnemingen(datum, deelnemer, filter);
					String weekString = "Week-" + TimeUtil.getInstance().getWeekOfYear(datum) + ";";

					boolean heeftLabel = false;
					for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
					{
						int dagVanWeek = lesdag.getDagNummer();
						for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
						{
							if (lestijd.getLesuur() <= filter.getTotLesuur().getLesuur()
								&& lestijd.getLesuur() >= filter.getVanafLesuur().getLesuur())
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

				InputStream is =
					WaarnemingenJaarOverzichtRapportagePage.class
						.getResourceAsStream("/nl/topicus/soundscape/participatie/web/reports/DeelnemerWaarnemingenOverzicht.jrxml");
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
						expression.setText("($F{COLUMN_" + colIndex
							+ "}.length() == 0) ? $F{COLUMN_" + colIndex + "} : $F{COLUMN_"
							+ colIndex + "}.substring(0,1)");
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
			File file = File.createTempFile("lijst", ".pdf");
			JasperExportManager.exportReportToPdfFile(firstPrint, file.getPath());
			PdfFileResourceStream stream =
				new PdfFileResourceStream(file, TimeUtil.getInstance().currentDateTime(),
					"Lijst afdrukken");
			((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
				"attachment; filename=\"" + "Waarnemingenoverzicht.pdf" + "\"");
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

	@SuppressWarnings("unused")
	private String getTitel(Deelnemer deelnemer)
	{
		String titel = "Waarnemingenweekoverzicht van ";
		titel += deelnemer.getPersoon().getVolledigeNaam();
		titel += "(";
		titel += deelnemer.getDeelnemernummer();
		titel += ")";
		return titel;
	}

	private boolean isEindeLesdag(LesweekIndeling lesweekIndeling, int colIndex)
	{

		int newColIndex = colIndex;
		for (LesdagIndeling lesdag : lesweekIndeling.getLesdagIndelingenOrderedByDay())
		{
			int dagVanWeek = lesdag.getDagNummer();
			for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
			{
				int lesuur = lestijd.getLesuur();
				if (lesuur >= getVanLesuur(dagVanWeek)
					&& lesuur <= getTotLesuur(lesweekIndeling, dagVanWeek))
				{
					newColIndex--;
				}
			}
			if (newColIndex == 0)
				return true;

		}
		return false;
	}

	private LesweekIndeling getLesweekIndeling()
	{
		LesweekindelingZoekFilter abstractFilter = new LesweekindelingZoekFilter();
		abstractFilter.setLocatie(getLocatie());
		abstractFilter.setOrganisatieEenheid(getOrganisatieEenheid());
		return DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class)
			.getlesweekIndeling(abstractFilter);
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

	private OrganisatieEenheid getOrganisatieEenheidMetMeesteLestijden()
	{
		List<Verbintenis> inschrijvingenList = inschrijvingenModel.getObject();
		int meesteLestijden = 0;
		OrganisatieEenheid organisatieEenheid = null;
		for (Verbintenis verbintenis : inschrijvingenList)
		{
			if (verbintenis.getOrganisatieEenheid() != null)
			{
				LesweekindelingZoekFilter abstractFilter = new LesweekindelingZoekFilter();
				abstractFilter.setOrganisatieEenheid(verbintenis.getOrganisatieEenheid());
				abstractFilter.setLocatie(verbintenis.getLocatie());

				abstractFilter
					.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));

				LesweekIndeling lesweekIndeling =
					DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class)
						.getlesweekIndeling(abstractFilter);
				if (lesweekIndeling != null)
				{
					for (LesdagIndeling lesdagIndeling : lesweekIndeling.getLesdagIndelingen())
					{
						if (lesdagIndeling.getLesuurIndeling().size() > meesteLestijden)
						{
							meesteLestijden = lesdagIndeling.getLesuurIndeling().size();
							organisatieEenheid = verbintenis.getOrganisatieEenheid();
						}
					}
				}
			}
		}
		return organisatieEenheid;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form, "PDF genereren"));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (DeelnemerCollectiefMenuItem.Rapportages.equals(selectedMenuItem))
		{
			return new DeelnemerCollectiefMenu(id, selectedMenuItem);
		}
		if (GroepCollectiefMenuItem.Rapportages.equals(selectedMenuItem))
		{
			return new GroepCollectiefMenu(id, selectedMenuItem);
		}
		return new DeelnemerMenu(id, selectedMenuItem, deelnemerModel, inschrijvingModel);
	}

	/**
	 * @return true als het rapport vanuit een specifieke deelnemer is aangeroepen
	 */
	public boolean isUsingDeelnemerTitel()
	{
		return deelnemerModel != null;
	}

	@Override
	public Component createTitle(String id)
	{
		if (isUsingDeelnemerTitel())
			return new DeelnemerTitel(id, deelnemerModel, inschrijvingModel);
		return new Label(id, "Deelnemerwaarnemingenoverzicht");
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(deelnemerModel);
		ComponentUtil.detachQuietly(inschrijvingModel);
		ComponentUtil.detachQuietly(inschrijvingenModel);
	}
}
