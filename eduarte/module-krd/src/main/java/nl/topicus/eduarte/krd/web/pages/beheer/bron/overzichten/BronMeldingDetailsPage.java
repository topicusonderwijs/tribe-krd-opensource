package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.bron.BronUtils;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.BronBveTerugkoppelRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkMelding;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtWrite;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBron;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.BronRedenGeblokkeerdModalWindow;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronSignalenTable;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

@PageInfo(title = "BRON Melding details", menu = "Deelnemer")
@InPrincipal( {BronOverzichtInzien.class, DeelnemerBron.class})
@RequiredSecurityCheck(DeelnemerSecurityCheck.class)
public class BronMeldingDetailsPage extends AbstractBronPage
{
	@InPrincipal(BronOverzichtWrite.class)
	private final class BronMeldingVerwijderButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		private BronMeldingVerwijderButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Verwijderen", "Weet u zeker dat u deze melding wilt verwijderen? "
				+ "Dit kan ernstige problemen opleveren in de communicatie met BRON.");
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BronMeldingVerwijderButton.class));
		}

		@Override
		public boolean isVisible()
		{
			return inWachtrij();
		}

		@Override
		protected void onClick()
		{
			IBronMelding bronMelding = getBronMelding();
			bronMelding.setVerwijderd(true);
			BronUtils.updateStatussenNaVerwijderen(bronMelding);
			((InstellingEntiteit) getBronMelding()).delete();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
			setResponsePage(getReturnPage());
		}
	}

	@InPrincipal(BronOverzichtWrite.class)
	private final class BronMeldingBlokkerenButton extends AbstractAjaxLinkButton
	{
		private static final long serialVersionUID = 1L;

		private BronMeldingBlokkerenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Blokkeren", CobraKeyAction.GEEN, ButtonAlignment.LEFT);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BronMeldingBlokkerenButton.class));
		}

		@Override
		protected void onClick(AjaxRequestTarget target)
		{
			redenGeblokkeerdWindow.show(target);
		}

		@Override
		public boolean isVisible()
		{
			return inWachtrij() && !isGeblokkeerd();
		}
	}

	@InPrincipal(BronOverzichtWrite.class)
	private final class BronMeldingDeblokkerenButton extends AbstractBottomRowButton
	{
		private static final long serialVersionUID = 1L;

		private BronMeldingDeblokkerenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Deblokkeren", CobraKeyAction.GEEN, ButtonAlignment.LEFT);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				BronMeldingDeblokkerenButton.class));
		}

		@Override
		public boolean isVisible()
		{
			return inWachtrij() && isGeblokkeerd();
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			return new ConfirmationLink<Void>(linkId,
				"Weet u zeker dat u deze melding wilt deblokkeren?")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick()
				{
					IBronMelding bronMelding = getBronMelding();
					bronMelding.setGeblokkeerd(false);
					bronMelding.setRedenGeblokkeerd(null);

					((InstellingEntiteit) bronMelding).saveOrUpdate();
					DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

					setResponsePage(new BronMeldingDetailsPage(getBronMelding(), getReturnPage()));
				}

			};
		}
	}

	private IModel<IBronMelding> meldingModel;

	private SecurePage returnPage;

	private BronRedenGeblokkeerdModalWindow redenGeblokkeerdWindow;

	public BronMeldingDetailsPage(IBronMelding bronMelding, SecurePage returnPage)
	{
		this(bronMelding, returnPage, CoreMainMenuItem.Beheer);
	}

	public BronMeldingDetailsPage(IBronMelding bronMelding, SecurePage returnPage,
			CoreMainMenuItem menuItem)
	{
		super(menuItem);
		DeelnemerSecurityCheck.replaceSecurityCheck(this, bronMelding.getDeelnemer());

		this.returnPage = returnPage;
		meldingModel = ModelFactory.getModel(bronMelding);

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		IBronMelding bronMelding = meldingModel.getObject();

		addOrReplace(new Label("caption", bronMelding.getOnderwijssoort() + "-melding details ("
			+ bronMelding.getDeelnemer() + ")"));

		addOrReplace(new TextArea<String>("reden", getRedenGeformateerdModel()));

		addOrReplace(new BronMeldingAlgemeenDetailPanel("algemeen", meldingModel));

		addOrReplace(redenGeblokkeerdWindow =
			new BronRedenGeblokkeerdModalWindow("redenGeblokkeerdWindow", meldingModel));
		redenGeblokkeerdWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (redenGeblokkeerdWindow.isSaved())
					setResponsePage(new BronMeldingDetailsPage(getBronMelding(), getReturnPage()));
			}
		});

		RepeatingView meldingDetailsLeftRV = new RepeatingView("meldingDetailsLeftRV");
		addOrReplace(meldingDetailsLeftRV);
		RepeatingView meldingDetailsRightRV = new RepeatingView("meldingDetailsRightRV");
		addOrReplace(meldingDetailsRightRV);

		if (bronMelding instanceof AbstractBronVOMelding)
		{
			maakVoDetails(bronMelding, meldingDetailsLeftRV, meldingDetailsRightRV);
		}
		if (bronMelding instanceof BronAanleverMelding)
		{
			maakBveDetails(bronMelding, meldingDetailsLeftRV, meldingDetailsRightRV);
		}

		IDataProvider<IBronSignaal> provider =
			new IModelDataProvider<IBronSignaal>(new PropertyModel<List<IBronSignaal>>(
				meldingModel, "terugkoppelmelding.signalen"));
		EduArteDataPanel<IBronSignaal> datapanel =
			new EduArteDataPanel<IBronSignaal>("datapanel", provider, new BronSignalenTable());
		addOrReplace(datapanel);

		super.onBeforeRender();
	}

	private void maakVoDetails(IBronMelding bronMelding, RepeatingView meldingDetailsLeftRV,
			RepeatingView meldingDetailsRightRV)
	{
		meldingDetailsLeftRV.add(getVoMeldingDetails(meldingDetailsLeftRV.newChildId(), "Melding"));

		if (bronMelding instanceof BronInschrijvingsgegevensVOMelding)
		{
			meldingDetailsRightRV.add(getVoInschrijvinggevensDetails(meldingDetailsRightRV
				.newChildId(), "Inschrijving gegevens"));
		}
		if (bronMelding instanceof BronExamenresultaatVOMelding)
		{
			meldingDetailsLeftRV.add(getVoExamengevensDetails(meldingDetailsLeftRV.newChildId(),
				"Examen gegevens"));
			BronExamenresultaatVOMelding examenMelding = (BronExamenresultaatVOMelding) bronMelding;
			for (BronVakGegegevensVOMelding vakMelding : examenMelding.getVakgegevens())
			{
				meldingDetailsRightRV.add(getVoVakgevensDetails(meldingDetailsRightRV.newChildId(),
					"Vakgegeven", ModelFactory.getModel(vakMelding)));
			}
		}
	}

	private void maakBveDetails(IBronMelding bronMelding, RepeatingView meldingDetailsLeftRV,
			RepeatingView meldingDetailsRightRV)
	{
		meldingDetailsLeftRV
			.add(getBveMeldingDetails(meldingDetailsLeftRV.newChildId(), "Melding"));

		meldingDetailsLeftRV.add(getBveSleutelgegevensDetails(meldingDetailsLeftRV.newChildId(),
			"Sleutelgegevens"));

		BronAanleverMelding bveAanleverMelding = (BronAanleverMelding) bronMelding;
		for (BronBveAanleverRecord record : bveAanleverMelding.getMeldingen())
		{
			IModel<BronBveAanleverRecord> recordModel = ModelFactory.getModel(record);

			switch (record.getRecordType())
			{
				case BRONConstants.BVE_AANLEVERING_WIJZIGING_SLEUTELGEGEVENS:
					meldingDetailsLeftRV.add(getSleutelwijzigingDetailsPanel(meldingDetailsLeftRV
						.newChildId(), "Sleutelwijziging", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS:
					meldingDetailsLeftRV.add(getPersoonsgegevensDetailsPanel(meldingDetailsLeftRV
						.newChildId(), "Persoonsgegevens", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS:
					meldingDetailsLeftRV.add(getInschrijvingDetailsPanel(meldingDetailsLeftRV
						.newChildId(), "Inschrijving", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS:
					meldingDetailsRightRV.add(getInschrijvingPeriodeGegevensdetailsPanel(
						meldingDetailsRightRV.newChildId(), "Inschrijvings- periodegegevens",
						recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS:
					meldingDetailsRightRV.add(getBpvgegevensDetailsPanel(meldingDetailsRightRV
						.newChildId(), "BPV-gegevens", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS:
					meldingDetailsRightRV.add(getExamenDetailsPanel(meldingDetailsRightRV
						.newChildId(), "Examen", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS:
					meldingDetailsLeftRV.add(getInschrijvingDetailsPanel(meldingDetailsLeftRV
						.newChildId(), "Inschrijving", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN:
					meldingDetailsRightRV.add(getNT2VaardighedenDetailsPanel(meldingDetailsRightRV
						.newChildId(), "NT2-vaardigheden", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS:
					meldingDetailsRightRV.add(getResultaatgegevensDetailsPanel(
						meldingDetailsRightRV.newChildId(), "Resultaat-gegevens", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS:
					meldingDetailsRightRV.add(getVakgegevensEDDetailsPanel(meldingDetailsRightRV
						.newChildId(), "Vak-gegevens", recordModel));
					break;

				case BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS:
					meldingDetailsLeftRV.add(getInschrijvingDetailsPanel(meldingDetailsLeftRV
						.newChildId(), "Inschrijving", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_VAVO_VAKGEGEVENS:
					meldingDetailsRightRV.add(getVakgegevensVAVODetailsPanel(meldingDetailsRightRV
						.newChildId(), "Vak-gegevens", recordModel));
					break;
				case BRONConstants.BVE_AANLEVERING_VAVO_EXAMENGEGEVENS:
					meldingDetailsRightRV.add(getExamgegevensVAVODetailsPanel(meldingDetailsRightRV
						.newChildId(), "Examen-gegevens", recordModel));
					break;
			}
		}
		if (bveAanleverMelding.getTerugkoppelmelding() != null)
		{
			for (BronBveTerugkoppelRecord terugkoppelRecord : bveAanleverMelding
				.getTerugkoppelmelding().getRecords())
			{
				if (terugkoppelRecord.getRecordType() == 411)
					meldingDetailsRightRV.add(getGbagegevensDetailsPanel(meldingDetailsRightRV
						.newChildId(), "GBA gegevens", ModelFactory.getModel(terugkoppelRecord)));
			}
		}
	}

	private BronMeldingDetailPanel getVoMeldingDetails(String id, String caption)
	{
		BronMeldingDetailPanel meldingDetails =
			new BronMeldingDetailPanel(id, caption, meldingModel,
				new PropertyModel<IBronTerugkMelding>(meldingModel, "terugkoppelmelding"));

		meldingDetails.addProperty("Vestigings volgnummer", "vestigingsVolgnummer");
		meldingDetails.addProperty("Bestandsnaam", "bestandsnaam");
		meldingDetails.addProperty("Batchnummer", "batchNummer");
		meldingDetails.addProperty("Soort mutatie", "soortMutatie");
		meldingDetails.addProperty("Meldingnummer", "meldingNummer");
		meldingDetails.addProperty("Deelnemernummer", "leerlingNummerInstelling");
		meldingDetails.addProperty("BSN", "sofiNummer");
		meldingDetails.addProperty("Onderwijsnummer", "onderwijsNummer");
		meldingDetails.addProperty("Geboortedatum", "geboorteDatum");
		meldingDetails.addProperty("Geslacht", "geslacht");
		return meldingDetails;
	}

	private BronMeldingDetailPanel getVoInschrijvinggevensDetails(String id, String caption)
	{
		BronMeldingDetailPanel inschrijvingDetails =
			new BronMeldingDetailPanel(id, caption, meldingModel,
				new PropertyModel<IBronTerugkMelding>(meldingModel, "terugkoppelmelding"));
		inschrijvingDetails.addProperty("Achternaam", "achternaam");
		inschrijvingDetails.addProperty("Voorvoegsel", "voorvoegsel");
		inschrijvingDetails.addProperty("Alle voornamen", "alleVoornamen");
		inschrijvingDetails.addProperty("Straatnaam", "straatNaam");
		inschrijvingDetails.addProperty("Huisnummer", "huisNummer");
		inschrijvingDetails.addProperty("Huisnummer toevoeging", "huisnummerToevoeging");
		inschrijvingDetails.addProperty("Locatie", "LocatieOmschrijving");
		inschrijvingDetails.addProperty("Huisnummer aanduiding", "huisnummerAanduiding");
		inschrijvingDetails.addProperty("Postcode", "postcode");
		inschrijvingDetails.addProperty("Plaatsnaam", "plaatsnaam");
		inschrijvingDetails.addProperty("Adresregel Buitenland 1", "adresregelBuitenland1");
		inschrijvingDetails.addProperty("Adresregel Buitenland 2", "adresregelBuitenland2");
		inschrijvingDetails.addProperty("Adresregel Buitenland 3", "adresregelBuitenland3");
		inschrijvingDetails.addProperty("Code land adres", "codeLandAdres");
		inschrijvingDetails.addPropertyZonderTerugKoppelField("Sofinummer achterhaald",
			"sofinummerAchterhaald");
		inschrijvingDetails.addPropertyZonderTerugKoppelField("Geboorte datum gewijzigd",
			"geboorteDatumGewijzigd");
		inschrijvingDetails.addPropertyZonderTerugKoppelField("Geslacht gewijzigd",
			"geslachtGewijzigd");
		inschrijvingDetails.addPropertyZonderTerugKoppelField(
			"Postcode volgens instelling gewijzigd", "postcodeVolgensInstellingGewijzigd");
		inschrijvingDetails.addPropertyZonderTerugKoppelField(
			"Landcode volgens instelling gewijzigd", "landCodeVolgensInstellingGewijzigd");
		inschrijvingDetails.addProperty("Datum ingang adreswijziging", "datumIngangAdresWijziging");
		inschrijvingDetails.addProperty("Ingangsdatum", "ingangsDatum");
		inschrijvingDetails.addProperty("Einddatum", "eindDatum");
		inschrijvingDetails.addProperty("ILT-code", "ILTCode");
		inschrijvingDetails.addProperty("Jaren praktijkonderwijs", "jarenPraktijkOnderwijs");
		inschrijvingDetails.addProperty("Leerjaar", "leerjaar");
		inschrijvingDetails.addProperty("Cumi categorie", "cumiCategorie");
		inschrijvingDetails.addProperty("Cumi ratio", "cumiRatio");
		return inschrijvingDetails;
	}

	private BronMeldingDetailPanel getVoExamengevensDetails(String id, String caption)
	{
		BronMeldingDetailPanel examenDetails =
			new BronMeldingDetailPanel(id, caption, meldingModel,
				new PropertyModel<IBronTerugkMelding>(meldingModel, "terugkoppelmelding"));
		examenDetails.addProperty("Postcode", "postcode");
		examenDetails.addProperty("ILT-code", "ILTCode");
		examenDetails.addProperty("Examenjaar", "examenJaar");
		examenDetails.addProperty("Datum uitslag examen", "datumUitslagExamen");
		examenDetails.addProperty("Uitslag examen", "uitslagExamen");
		examenDetails.addProperty("Titel of thema werkstuk", "titelOfThemaWerkstuk");
		examenDetails.addProperty("Beoordeling werkstuk", "beoordelingWerkstuk");
		examenDetails.addProperty("Toepassing beoordeling werkstuk",
			"toepassingBeoordelingWerkstuk");
		examenDetails.addProperty("Cijfer werkstuk", "cijferWerkstuk");
		return examenDetails;
	}

	private BronMeldingDetailPanel getVoVakgevensDetails(String id, String caption,
			IModel<BronVakGegegevensVOMelding> vakMeldingModel)
	{
		BronMeldingDetailPanel vakDetails =
			new BronMeldingDetailPanel(id, caption, vakMeldingModel,
				new PropertyModel<IBronTerugkMelding>(vakMeldingModel, "terugkoppelmelding"));
		vakDetails.addProperty("Examenvak", "examenVak");
		vakDetails.addProperty("Diploma", "diplomaVak");
		vakDetails.addProperty("Toepassing resultaat of beoordeling examenvak",
			"toepassingResultaatOfBeoordelingExamenVak");
		vakDetails.addProperty("Indicatie werkstuk", "indicatieWerkstuk");
		vakDetails.addProperty("Hoger niveau", "hogerNiveau");
		vakDetails.addProperty("Beoordeling schoolexamen", "beoordelingSchoolExamen");
		vakDetails.addProperty("Cijfer schoolexamen", "cijferSchoolExamen");
		vakDetails.addProperty("Cijfer CE1", "cijferCE1");
		vakDetails.addProperty("Cijfer CE2", "cijferCE2");
		vakDetails.addProperty("Cijfer CE3", "cijferCE3");
		vakDetails.addProperty("Eerste eindcijfer", "eersteEindcijfer");
		vakDetails.addProperty("Tweede eindcijfer", "tweedeEindcijfer");
		vakDetails.addProperty("Derde eindcijfer", "derdeEindcijfer");
		vakDetails.addProperty("Cijfer cijferlijst", "cijferCijferlijst");
		vakDetails.addProperty("Verwezen naar volgende tijdvak", "verwezenNaarVolgendeTijdvak");
		vakDetails.addProperty("Certificaat", "certificaat");
		vakDetails.addProperty("Indicatie combinatiecijfer", "indicatieCombinatieCijfer");
		vakDetails.addProperty("Vakcode hoger niveau", "vakCodeHogerNiveau");
		return vakDetails;
	}

	private BronMeldingDetailPanel getBveMeldingDetails(String id, String caption)
	{
		BronMeldingDetailPanel meldingDetails =
			new BronMeldingDetailPanel(id, caption, meldingModel,
				new PropertyModel<IBronTerugkMelding>(meldingModel, "terugkoppelmelding"));
		meldingDetails.addProperty("Bestandsnaam", "bestandsnaam");
		meldingDetails.addProperty("Batchnummer", "batchNummer");
		meldingDetails.addProperty("Meldingnummer", "meldingnummer");
		meldingDetails.addProperty("Deelnemernummer", "leerlingnummer");
		return meldingDetails;
	}

	private BronMeldingDetailPanel getBveSleutelgegevensDetails(String id, String caption)
	{
		BronMeldingDetailPanel sleutelgegevensDetails =
			new BronMeldingDetailPanel(id, caption, meldingModel,
				new PropertyModel<IBronTerugkMelding>(meldingModel, "terugkoppelmelding"));
		sleutelgegevensDetails.addProperty("BSN", "sofinummer");
		sleutelgegevensDetails.addProperty("Onderwijsnummer", "onderwijsnummer");
		sleutelgegevensDetails.addProperty("Geboortedatum", "geboortedatum");
		sleutelgegevensDetails.addProperty("Geslacht", "geslacht");
		sleutelgegevensDetails.addProperty("Postcode-volgens-instelling",
			"postcodeVolgensInstelling");
		sleutelgegevensDetails.addProperty("Land", "land");
		return sleutelgegevensDetails;
	}

	private BronMeldingDetailPanel getSleutelwijzigingDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("BSN achterhaald", "sofinummerAchterhaald");
		detailPanel.addProperty("Geboortedatum gewijzigd", "geboortedatumGewijzigd");
		detailPanel.addProperty("Geslacht gewijzigd", "geslachtGewijzigd");
		detailPanel.addProperty("Postcode-volgens-instelling gewijzigd",
			"postcodeVolgensInstellingGewijzigd");
		detailPanel.addProperty("Land gewijzigd", "landGewijzigd");
		detailPanel.addProperty("Datum ingang adreswijziging", "datumIngangAdreswijziging");
		return detailPanel;
	}

	private BronMeldingDetailPanel getPersoonsgegevensDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Achternaam", "achternaam");
		detailPanel.addProperty("Voorvoegsel", "voorvoegsel");
		detailPanel.addProperty("Alle voornamen", "alleVoornamen");
		detailPanel.addProperty("Straatnaam", "straatnaam");
		detailPanel.addProperty("Huisnummer", "huisnummer");
		detailPanel.addProperty("Huisnummer toevoeging", "huisnummerToevoeging");
		detailPanel.addProperty("Locatie omschrijving", "locatieOmschrijving");
		detailPanel.addProperty("Huisnummer aanduiding", "huisnummerAanduiding");
		detailPanel.addProperty("Plaatsnaam", "plaatsnaam");
		detailPanel.addProperty("Adresregel buitenland 1", "adresregelBuitenland1");
		detailPanel.addProperty("Adresregel buitenland 2", "adresregelBuitenland2");
		detailPanel.addProperty("Adresregel buitenland 3", "adresregelBuitenland3");
		detailPanel.addProperty("Nationaliteit 1", "nationaliteit1");
		detailPanel.addProperty("Nationaliteit 2", "nationaliteit2");
		return detailPanel;
	}

	/**
	 * Er zijn 3 inschrijvingrecords voor BO, ED en VAVO. Die van ED en VAVO zijn aan
	 * elkaar gelijk, die van BO heeft wat meer velden. IndicatieNieuwkomer en
	 * contacturenPerWeek zijn velden die BO niet heeft.
	 */
	private BronMeldingDetailPanel getInschrijvingDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Gevolgde opleiding", "gevolgdeOpleiding");
		if (recordModel.getObject().getActualRecordType().equals(InschrijvingsgegevensRecord.class))
		{
			detailPanel.addProperty("Leerweg", "leerweg");
			detailPanel.addProperty("Intensiteit", "intensiteit");
		}

		detailPanel.addProperty("Datum inschrijving", "datumInschrijving");
		detailPanel.addProperty("Geplande datum uitschrijving", "geplandeDatumUitschrijving");
		detailPanel.addProperty("Werkelijke datum uitschrijving", "werkelijkeDatumUitschrijving");
		detailPanel.addProperty("Hoogste vooropleiding", "hoogsteVooropleiding");

		if (recordModel.getObject().getActualRecordType().equals(InschrijvingsgegevensRecord.class))
		{
			detailPanel.addProperty("Indicatie risicodeelnemer", "indicatieRisicodeelnemer");
			detailPanel.addProperty("Indicatie gehandicapt", "indicatieGehandicapt");
			detailPanel.addProperty("Laatste vooropleiding", "laatsteVooropleiding");
			detailPanel.addProperty("Locatie", "locatie");
		}
		else
		{
			detailPanel.addProperty("Indicatie Nieuwkomer", "indicatieNieuwkomer");
			detailPanel.addProperty("Contacturen Per Week", "contacturenPerWeek");
		}
		return detailPanel;
	}

	private BronMeldingDetailPanel getInschrijvingPeriodeGegevensdetailsPanel(String id,
			String caption, IModel<BronBveAanleverRecord> recordModel)
	{

		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Datum ingang periodegegevens inschrijving",
			"datumIngangPeriodegegevensInschrijving");
		detailPanel.addProperty("Indicatie bekostiging", "indicatieBekostigingInschrijving");
		detailPanel.addProperty("Indicatie lesgeld", "indicatieLesgeld");
		return detailPanel;
	}

	private BronMeldingDetailPanel getBpvgegevensDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("BPV volgnummer", "bpvVolgnummer");
		detailPanel.addProperty("Afsluitdatum BPV", "afsluitdatumBpv");
		detailPanel.addProperty("Datum begin BPV", "datumBeginBpv");
		detailPanel.addProperty("Geplande datum einde BPV", "geplandeDatumEindeBpv");
		detailPanel.addProperty("Werkelijke datum einde BPV", "werkelijkeDatumEindeBpv");
		detailPanel.addProperty("Leerbedrijf", "leerbedrijf");
		detailPanel.addProperty("Omvang BPV", "omvangBpv");
		detailPanel.addProperty("BRIN code KBB", "brinCodeKbb");
		detailPanel.addProperty("Naam leerbedrijf", "naamLeerbedrijf");
		detailPanel.addProperty("Postcode leerbedrijf", "postcodeLeerbedrijf");
		detailPanel.addProperty("Huisnummer leerbedrijf", "huisnummerLeerbedrijf");
		detailPanel.addProperty("Crebo code BPV", "creboCodeBpv");
		return detailPanel;
	}

	private BronMeldingDetailPanel getExamenDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Behaalde (deel-)kwalificatie", "behaaldeDeelKwalificatie");
		detailPanel.addProperty("Datum behaald", "datumBehaald");
		detailPanel.addProperty("Indicatie bekostiging diploma", "indicatieBekostigingDiploma");
		return detailPanel;
	}

	private BronMeldingDetailPanel getNT2VaardighedenDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Vak volgnummer", "getVakvolgnummer");
		detailPanel.addProperty("NT2 Vaardigheid", "getNT2Vaardigheid");
		detailPanel.addProperty("Start niveau", "getStartniveau");
		detailPanel.addProperty("Behaald niveau", "getBehaaldNiveau");
		return detailPanel;
	}

	private BronMeldingDetailPanel getResultaatgegevensDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Voltooide opleiding", "voltooideOpleiding");
		detailPanel.addProperty("Datum voltooid", "datumVoltooid");
		return detailPanel;
	}

	private BronMeldingDetailPanel getVakgegevensEDDetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Vak volgnummer", "vakvolgnummer");
		detailPanel.addProperty("Vak", "vak");
		detailPanel.addProperty("Datum voltooid", "datumVoltooid");
		return detailPanel;
	}

	private BronMeldingDetailPanel getVakgegevensVAVODetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Examen", "examen");
		detailPanel.addProperty("Examenjaar", "examenjaar");
		detailPanel.addProperty("Examenvak", "examenvak");
		detailPanel.addProperty("Indicatie diplomavak", "indicatieDiplomavak");
		detailPanel.addProperty("Toepassing resultaat examenvak", "toepassingResultaatExamenvak");
		detailPanel.addProperty("Indicatie werkstuk", "indicatieWerkstuk");
		detailPanel.addProperty("Hoger niveau", "hogerNiveau");
		detailPanel.addProperty("Beoordeling schoolexamen", "beoordelingSchoolExamen");
		detailPanel.addProperty("Cijfer schoolexamen", "cijferSchoolExamen");
		detailPanel.addProperty("Cijfer CE1", "cijferCE1");
		detailPanel.addProperty("Cijfer CE2", "cijferCE2");
		detailPanel.addProperty("Cijfer CE3", "cijferCE3");
		detailPanel.addProperty("Eerste eindcijfer", "eersteEindcijfer");
		detailPanel.addProperty("Tweede eindcijfer", "tweedeEindcijfer");
		detailPanel.addProperty("Derde eindcijfer", "derdeEindcijfer");
		detailPanel.addProperty("Cijfer cijferlijst", "cijferCijferlijst");
		detailPanel.addProperty("Indicatie verwezen naar volgend tijdvak",
			"verwezenNaarVolgendeTijdvak");
		detailPanel.addProperty("Indicatie certificaat behaald", "certificaat");
		detailPanel.addProperty("Indicatie combinatiecijfer", "indicatieCombinatieCijfer");
		detailPanel.addProperty("Vakcode hoger niveau", "vakCodeHogerNiveau");
		return detailPanel;
	}

	private BronMeldingDetailPanel getExamgegevensVAVODetailsPanel(String id, String caption,
			IModel<BronBveAanleverRecord> recordModel)
	{
		BronMeldingDetailPanel detailPanel = new BronMeldingDetailPanel(id, caption, recordModel);
		detailPanel.addProperty("Soort mutatie", "soortMutatie");
		detailPanel.addProperty("Inschrijvingsvolgnummer", "inschrijvingsvolgnummer");
		detailPanel.addProperty("Examen", "examen");
		detailPanel.addProperty("Examenjaar", "examenjaar");
		detailPanel.addProperty("Datum uitslag examen", "datumUitslagExamen");
		detailPanel.addProperty("Uitslag examen", "uitslagExamen");
		detailPanel.addProperty("Titel/thema werkstuk", "titelThemaWerkstuk");
		detailPanel.addProperty("Beoordeling werkstuk", "beoordelingWerkstuk");
		detailPanel.addProperty("Toepassing beoordeling werkstuk", "toepassingBeoordelingWerkstuk");
		detailPanel.addProperty("Cijfer werkstuk", "cijferWerkstuk");
		return detailPanel;
	}

	private BronMeldingDetailPanel getGbagegevensDetailsPanel(String id, String caption,
			IModel<BronBveTerugkoppelRecord> terugkoppelRecordModel)
	{
		BronMeldingDetailPanel detailPanel =
			new BronMeldingDetailPanel(id, caption, null, terugkoppelRecordModel);
		detailPanel.addProperty("Datum overlijden", "datumOverlijden");
		detailPanel.addProperty("Postcode volgens GBA", "postcodeVolgensGba");
		detailPanel.addProperty("Datum ingang adres", "datumIngangAdres");
		detailPanel.addProperty("Geboorteland", "geboorteland");
		detailPanel.addProperty("Geboorteland ouder-1", "geboortelandOuder1");
		detailPanel.addProperty("Geslacht ouder-1", "geslachtOuder1");
		detailPanel.addProperty("Geboorteland ouder-2", "geboortelandOuder2");
		detailPanel.addProperty("Geslacht ouder-2", "geslachtOuder2");
		detailPanel.addProperty("Datum vestiging in Nederland", "datumVestigingInNederland");
		detailPanel.addProperty("Datum vertrek uit Nederland", "vertrekUitNederland");
		detailPanel.addProperty("Land waarnaar vertrokken", "landWaarnaarVertrokken");
		detailPanel.addProperty("Nationaliteit-1", "nationaliteit1");
		detailPanel.addProperty("Datum ingang nationaliteit-1", "datumIngangNationaliteit1");
		detailPanel.addProperty("Nationaliteit-2", "nationaliteit2");
		detailPanel.addProperty("Datum ingang nationaliteit-2", "datumIngangNationaliteit2");
		detailPanel.addProperty("Verblijfstitel", "verblijfstitel");
		detailPanel.addProperty("Datum ingang verblijfstitel", "datumIngangVerblijfstitel");
		detailPanel.addProperty("Datum einde verblijfstitel", "datumEindeVerblijfstitel");
		return detailPanel;
	}

	private IModel<String> getRedenGeformateerdModel()
	{
		IBronMelding melding = meldingModel.getObject();
		if (melding.getReden() == null)
			return Model.of("");
		String reden = melding.getReden().replace(", ", "\n");
		// Datum op aparte regel
		reden = reden.substring(0, 19) + "\n" + reden.substring(20);
		return Model.of(reden);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		if (getReturnPage() != null)
			return getReturnPage().createMenu(id);
		else
			return super.createMenu(id);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(IBronMelding.class);
		ctorArgTypes.add(SecurePage.class);
		ctorArgValues.add(meldingModel);
		ctorArgValues.add(getReturnPage());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, getReturnPage()));
		panel.addButton(new BronMeldingVerwijderButton(panel));
		panel.addButton(new BronMeldingBlokkerenButton(panel));
		panel.addButton(new BronMeldingDeblokkerenButton(panel));

		super.fillBottomRow(panel);
	}

	private boolean inWachtrij()
	{
		IBronMelding bronMelding = getBronMelding();
		return (bronMelding.getBronMeldingStatus().equals(BronMeldingStatus.WACHTRIJ));
	}

	private boolean isGeblokkeerd()
	{
		IBronMelding bronMelding = getBronMelding();
		return (bronMelding.isGeblokkeerd());
	}

	private IBronMelding getBronMelding()
	{
		return meldingModel.getObject();
	}

	public SecurePage getReturnPage()
	{
		return returnPage;
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(meldingModel);
		super.onDetach();
	}
}
