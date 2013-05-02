package nl.topicus.eduarte.krd.web.pages.intake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.reflection.copy.CopyManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.RelatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SoortContactgegevenDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.inschrijving.Aanmelding;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.VerbintenisContract;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.*;
import nl.topicus.eduarte.entities.settings.DebiteurNummerSetting;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class IntakeWizardModel implements IModel<IntakeWizardModel>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Registratiedatum")
	private Date registratieDatum;

	private IChangeRecordingModel<Deelnemer> deelnemerModel;

	private VerbintenisUndo stap4Undo;

	private VerbintenisKeuze stap4keuze;

	private Serializable stap4idVanNieuweVerbintenis;

	private final IModel<Boolean> maakOnderwijsproductAfnamesModel =
		new Model<Boolean>(Boolean.TRUE);

	private DefaultModelManager manager;

	private transient boolean detaching = false;

	private Page returnPage;

	private VerbintenisZoekFilter stap0Filter;

	/**
	 * geeft aan of er al een keer relaties zijn gezocht op basis van het adres van de
	 * deelnemer
	 */
	private boolean bestaandeRelatiesGekoppeld = false;

	@AutoForm(required = true, label = "Organisatie-eenheid", htmlClasses = "unit_max")
	private IModel<OrganisatieEenheid> defaultOrganisatieEenheid;

	@AutoForm(required = true, label = "Verbintenis actief vanaf")
	private IModel<Date> begindatum;

	public IntakeWizardModel()
	{
		this(null);
	}

	/**
	 * Constructor voor de intake van een nieuwe deelnemer. Maakt nieuwe deelnemer,
	 * verbintenis en plaatsing.
	 * 
	 * @param medewerker
	 *            Wordt gebruikt voor default organisatie-eenheid
	 */
	public IntakeWizardModel(Medewerker medewerker)
	{
		registratieDatum = TimeUtil.getInstance().currentDate();

		Deelnemer deelnemer = createDefaultDeelnemer();

		init(deelnemer, medewerker);
	}

	/**
	 * Constructor voor de herintake van een bestaande deelnemer. Voegt een nieuwe
	 * verbintenis toe en daarin een nieuwe plaatsing.
	 * 
	 * @param deelnemer
	 * @param medewerker
	 *            Wordt gebruikt voor default organisatie-eenheid
	 */
	public IntakeWizardModel(Deelnemer deelnemer, Medewerker medewerker)
	{
		registratieDatum =
			deelnemer.getRegistratieDatum() != null ? deelnemer.getRegistratieDatum() : deelnemer
				.getCreatedAt();

		init(deelnemer, medewerker);
	}

	private void init(Deelnemer deelnemer, Medewerker medewerker)
	{
		manager =
			new DefaultModelManager(PersoonAdres.class, Adres.class, PersoonContactgegeven.class,
				Relatie.class, PersoonExterneOrganisatie.class, VrijVeldOptieKeuze.class,
				VooropleidingVrijVeld.class, Vooropleiding.class, IntakegesprekVrijVeld.class,
				Intakegesprek.class, PlaatsingVrijVeld.class, Plaatsing.class,
				VerbintenisContract.class, VerbintenisVrijVeld.class, Aanmelding.class,
				Verbintenis.class, Deelnemer.class, PersoonVrijVeld.class, Persoon.class);

		deelnemerModel = ModelFactory.getCompoundChangeRecordingModel(deelnemer, manager);

		OrganisatieEenheid organisatieEenheid = null;
		if (medewerker != null)
		{
			organisatieEenheid = medewerker.getDefaultOrganisatieEenheid();
		}
		if (organisatieEenheid == null)
			organisatieEenheid = deelnemer.getOrganisatie().getRootOrganisatieEenheid();
		defaultOrganisatieEenheid = ModelFactory.getModel(organisatieEenheid);
		setBegindatum(Cohort.getCohort(TimeUtil.getInstance().getCurrentYear()).getBegindatum());
	}

	public Intakegesprek createDefaultIntakegesprek(Verbintenis verbintenis)
	{
		Intakegesprek gesprek = new Intakegesprek();
		gesprek.setVerbintenis(verbintenis);
		gesprek.setOrganisatieEenheid(verbintenis.getOrganisatieEenheid());

		verbintenis.getIntakegesprekken().add(gesprek);

		return gesprek;
	}

	public void startStap2()
	{
		if (!bestaandeRelatiesGekoppeld)
		{
			Deelnemer deelnemer = deelnemerModel.getObject();
			if (!deelnemer.getPersoon().isMeerderjarig())
			{
				koppelBestaandeRelatiesMetPersoon();
				bestaandeRelatiesGekoppeld = true;
			}
		}
	}

	private void koppelBestaandeRelatiesMetPersoon()
	{
		List<Relatie> bestaandeRelatiesOpAdres = zoekRelatiesOpDeelnemerWoonadres();
		for (Relatie relatie : bestaandeRelatiesOpAdres)
		{
			koppelBestaandeRelatieMetDeelnemer(relatie);
		}
	}

	/**
	 * 
	 * @return bestaande relaties op dit woonadres. Iedere persoon komt max. 1 keer voor.
	 */
	private List<Relatie> zoekRelatiesOpDeelnemerWoonadres()
	{
		Persoon persoon = getDeelnemer().getPersoon();
		PersoonAdres woonadres = persoon.getFysiekAdres();
		if (woonadres == null)
			return Collections.emptyList();

		RelatieZoekFilter filter = new RelatieZoekFilter();
		filter.setLand(woonadres.getAdres().getLand());
		filter.setPostcode(woonadres.getAdres().getPostcode());
		filter.setHuisnummer(woonadres.getAdres().getHuisnummer());
		filter.setHuisnummerToevoeging(woonadres.getAdres().getHuisnummerToevoeging());

		RelatieDataAccessHelper relatieHelper =
			DataAccessRegistry.getHelper(RelatieDataAccessHelper.class);
		List<Relatie> bestaandeRelatiesOpAdres = relatieHelper.list(filter);
		List<Relatie> huidigeRelaties = persoon.getRelatiesRelatie();

		List<Relatie> uniekePersoonRelatiesOpAdres =
			filterDubbelePersonen(bestaandeRelatiesOpAdres, huidigeRelaties);
		return uniekePersoonRelatiesOpAdres;
	}

	private List<Relatie> filterDubbelePersonen(List<Relatie> bestaandeRelatiesOpAdres,
			List<Relatie> huidigeRelaties)
	{
		Set<Persoon> personen = new HashSet<Persoon>();

		// huidige relaties nooit toevoegen
		for (Relatie relatie : huidigeRelaties)
			personen.add(relatie.getRelatie());

		List<Relatie> uniekePersoonRelatiesOpAdres = new ArrayList<Relatie>();
		for (Relatie relatie : bestaandeRelatiesOpAdres)
		{
			if (!personen.contains(relatie.getRelatie()))
			{
				personen.add(relatie.getRelatie());
				uniekePersoonRelatiesOpAdres.add(relatie);
			}
		}
		return uniekePersoonRelatiesOpAdres;
	}

	private void koppelBestaandeRelatieMetDeelnemer(Relatie relatie)
	{
		Persoon persoon = getDeelnemer().getPersoon();
		Relatie nieuweRelatie = new Relatie();
		nieuweRelatie.setRelatieSoort(relatie.getRelatieSoort());

		nieuweRelatie.setBegindatum(registratieDatum);

		nieuweRelatie.setBetalingsplichtige(relatie.isBetalingsplichtige());
		nieuweRelatie.setDeelnemer(persoon);
		nieuweRelatie.setRelatie(relatie.getRelatie());
		nieuweRelatie.setWettelijkeVertegenwoordiger(relatie.isWettelijkeVertegenwoordiger());
		persoon.getRelaties().add(nieuweRelatie);
	}

	private Deelnemer createDefaultDeelnemer()
	{
		Deelnemer deelnemer = new Deelnemer();

		NummerGeneratorDataAccessHelper generator =
			DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
		deelnemer.setDeelnemernummer(generator.newDeelnemernummer());

		deelnemer.setPersoon(createDefaultPersoon());

		SettingsDataAccessHelper settingsHelper =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class);
		DebiteurNummerSetting setting = settingsHelper.getSetting(DebiteurNummerSetting.class);
		// wanneer expliciet ingesteld een nieuw debiteurnummer genereren, anders default
		// deelnemernummer gebruiken.
		if (setting != null && setting.getValue() != null
			&& (!setting.getValue().isDeelnemernummerIsDebiteurnummer()))
		{
			deelnemer.getPersoon().setDebiteurennummer(generator.newDebiteurnummer());
		}
		else
		{
			deelnemer.getPersoon()
				.setDebiteurennummer(Long.valueOf(deelnemer.getDeelnemernummer()));
		}
		return deelnemer;
	}

	public Verbintenis createDefaultVerbintenis()
	{
		Deelnemer deelnemer = getDeelnemer();
		Verbintenis verbintenis =
			deelnemer.nieuweVerbintenisMetCohort(getDefaultOrganisatieEenheid(), getBegindatum());
		deelnemer.getVerbintenissen().add(verbintenis);

		// trigger het aanmaken van een temporary ID
		manager.getModel(verbintenis, null);
		return verbintenis;
	}

	private Persoon createDefaultPersoon()
	{
		Persoon persoon = new Persoon();
		persoon.setNationaliteit1(Nationaliteit.getNederlands());
		persoon.setGeboorteland(Land.getNederland());

		voegAdressenToe(persoon);
		voegContactgegevensToe(persoon);

		return persoon;
	}

	private void voegAdressenToe(Persoon persoon)
	{
		PersoonAdres adres = persoon.newAdres();
		adres.setFysiekadres(true);
		adres.setPostadres(true);
		adres.setFactuuradres(true);
		adres.setBegindatum(registratieDatum);
		persoon.getAdressen().add(adres);
	}

	private void voegContactgegevensToe(Persoon persoon)
	{
		SoortContactgegevenDataAccessHelper soortHelper =
			DataAccessRegistry.getHelper(SoortContactgegevenDataAccessHelper.class);

		List<SoortContactgegeven> soorten =
			soortHelper.list(Arrays.asList(StandaardContactgegeven.StandaardTonenBijPersoon,
				StandaardContactgegeven.StandaardTonen), false);
		for (SoortContactgegeven soort : soorten)
			voegContactgegevenToeAanPersoon(persoon, soort);

	}

	private void voegContactgegevenToeAanPersoon(Persoon persoon, SoortContactgegeven soort)
	{
		PersoonContactgegeven gegeven = new PersoonContactgegeven();
		gegeven.setPersoon(persoon);
		gegeven.setSoortContactgegeven(soort);
		persoon.getContactgegevens().add(gegeven);
	}

	public Date getRegistratieDatum()
	{
		return registratieDatum;
	}

	public void setRegistratieDatum(Date registratieDatum)
	{
		this.registratieDatum = registratieDatum;
	}

	public void setBetalingsplichtigeRelatie(AbstractRelatie relatie)
	{
		for (AbstractRelatie r : getDeelnemer().getPersoon().getRelaties())
			if (r.isBetalingsplichtige() && !r.equals(relatie))
				r.setBetalingsplichtige(false);
		relatie.setBetalingsplichtige(true);// voor de zekerheid
	}

	public DefaultModelManager getManager()
	{
		return manager;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	public List<Verbintenis> getIntakes()
	{
		List<Verbintenis> ret = new ArrayList<Verbintenis>();
		for (Verbintenis curVerbintenis : getDeelnemer().getVerbintenissen())
		{
			if (VerbintenisStatus.Intake.equals(curVerbintenis.getStatus())
				|| (!curVerbintenis.isSaved() && !curVerbintenis.getCurrentId().equals(
					getStap4idVanNieuweVerbintenis())))
				ret.add(curVerbintenis);
		}
		return ret;
	}

	public int getDeelnemernummer()
	{
		return getDeelnemer().getDeelnemernummer();
	}

	@Override
	public IntakeWizardModel getObject()
	{
		return this;
	}

	@Override
	public void setObject(IntakeWizardModel object)
	{
		throw new UnsupportedOperationException();
	}

	public void saveAll()
	{
		Verbintenis stap4Verbintenis = getVerbintenis();
		getDeelnemer().setRegistratieDatum(getRegistratieDatum());
		if (getDeelnemer().getActieveVerbintenissenOpPeildatumOfInDeToekomst().isEmpty())
			createDefaultVerbintenis();
		deelnemerModel.saveObject();

		if (getMaakOnderwijsproductAfnames() && stap4Verbintenis != null)
			stap4Verbintenis.maakDefaultProductregelKeuzes();

		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	public Verbintenis getVerbintenis()
	{
		return stap4keuze == null ? null : stap4keuze.getVerbintenis(this);
	}

	public Relatie addNieuwePersoonRelatie()
	{
		Relatie relatie = new Relatie();
		Persoon deelnemer = getDeelnemer().getPersoon();
		relatie.setDeelnemer(deelnemer);

		Persoon persoon = new Persoon();

		persoon.setNationaliteit1(null);
		persoon.setGeboorteland(null);

		NummerGeneratorDataAccessHelper generator =
			DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
		persoon.setDebiteurennummer(generator.newDebiteurnummer());

		kopieerAdressenVanDeelnemerNaarRelatie(persoon);

		relatie.setRelatie(persoon);

		if (deelnemer.getGeboortedatum() != null)
			relatie.setBegindatum(deelnemer.getGeboortedatum());
		else
			relatie.setBegindatum(TimeUtil.getInstance().getMinDate());

		if (!deelnemer.isMeerderjarig())
		{
			relatie.setWettelijkeVertegenwoordiger(true);

			boolean betalingsplichtige = true;
			for (AbstractRelatie r : deelnemer.getRelaties())
				if (r.isBetalingsplichtige())
					betalingsplichtige = false;
			relatie.setBetalingsplichtige(betalingsplichtige);
		}

		persoon.setAchternaam(deelnemer.getOfficieleAchternaam());
		persoon.setVoorvoegsel(deelnemer.getOfficieleVoorvoegsel());

		deelnemer.getRelaties().add(relatie);
		return relatie;
	}

	public PersoonExterneOrganisatie addNieuweOrganisatieRelatie(ExterneOrganisatie organisatie)
	{
		PersoonExterneOrganisatie relatie = new PersoonExterneOrganisatie();
		Persoon deelnemer = getDeelnemer().getPersoon();
		relatie.setDeelnemer(deelnemer);

		relatie.setRelatie(organisatie);

		if (deelnemer.getGeboortedatum() != null)
			relatie.setBegindatum(deelnemer.getGeboortedatum());
		else
			relatie.setBegindatum(TimeUtil.getInstance().getMinDate());

		if (!deelnemer.isMeerderjarig())
		{
			relatie.setWettelijkeVertegenwoordiger(true);

			boolean betalingsplichtige = true;
			for (AbstractRelatie r : deelnemer.getRelaties())
				if (r.isBetalingsplichtige())
					betalingsplichtige = false;
			relatie.setBetalingsplichtige(betalingsplichtige);
		}

		deelnemer.getRelaties().add(relatie);
		return relatie;
	}

	public Vooropleiding createNieuweVooropleiding()
	{
		Vooropleiding vooropleiding = new Vooropleiding();
		Deelnemer deelnemer = getDeelnemer();
		vooropleiding.setDeelnemer(deelnemer);
		deelnemer.getVooropleidingen().add(vooropleiding);

		return vooropleiding;
	}

	public void removeRelatie(AbstractRelatie relatie)
	{
		getDeelnemer().getPersoon().getRelaties().remove(relatie);
	}

	public void removeVooropleiding(Vooropleiding vooropleiding)
	{
		getDeelnemer().getVooropleidingen().remove(vooropleiding);
	}

	public void kopieerAdressenVanDeelnemerNaarRelatie(Persoon relatie)
	{
		Persoon deelnemer = getDeelnemer().getPersoon();
		relatie.getAdressen().clear();

		CopyManager copyManager = new HibernateObjectCopyManager(PersoonAdres.class);
		for (PersoonAdres persoonAdres : deelnemer.getAdressenOpPeildatum())
		{
			PersoonAdres copyPersoonAdres = copyManager.copyObject(persoonAdres);
			copyPersoonAdres.getAdres().getAdresEntiteiten().add(copyPersoonAdres);
			copyPersoonAdres.getAdres().getPersoonAdressen().add(copyPersoonAdres);
			copyPersoonAdres.setPersoon(relatie);
			relatie.getAdressen().add(copyPersoonAdres);
		}
	}

	public void maakAdressenLeeg(Persoon persoon)
	{
		persoon.getAdressen().clear();
	}

	public IModel<AbstractRelatie> getModel(AbstractRelatie relatie)
	{
		return new PropertyModel<AbstractRelatie>(this, getPath(relatie));
	}

	private String getPath(AbstractRelatie relatie)
	{
		return "deelnemer.persoon.relaties["
			+ getDeelnemer().getPersoon().getRelaties().indexOf(relatie) + "]";
	}

	public IModel<Vooropleiding> getModel(Vooropleiding vooropleiding)
	{
		return new PropertyModel<Vooropleiding>(this, getPath(vooropleiding));
	}

	private String getPath(Vooropleiding vooropleiding)
	{
		return "deelnemer.vooropleidingen["
			+ getDeelnemer().getVooropleidingen().indexOf(vooropleiding) + "]";
	}

	public IModel<Intakegesprek> getModel(Intakegesprek intakegesprek)
	{
		return new PropertyModel<Intakegesprek>(this, getPath(intakegesprek));
	}

	private String getPath(Intakegesprek intakegesprek)
	{
		return "deelnemer.verbintenissen["
			+ getDeelnemer().getVerbintenissen().indexOf(intakegesprek.getVerbintenis())
			+ "].intakegesprekken["
			+ intakegesprek.getVerbintenis().getIntakegesprekken().indexOf(intakegesprek) + "]";
	}

	public Page getReturnPage()
	{
		return returnPage;
	}

	public void setReturnPage(Page returnPage)
	{
		this.returnPage = returnPage;
	}

	public boolean getMaakOnderwijsproductAfnames()
	{
		return maakOnderwijsproductAfnamesModel.getObject();
	}

	public void setMaakOnderwijsproductAfnames(boolean maakOnderwijsproductAfnames)
	{
		maakOnderwijsproductAfnamesModel.setObject(maakOnderwijsproductAfnames);
	}

	public void setStap0Filter(VerbintenisZoekFilter stap0Filter)
	{
		this.stap0Filter = stap0Filter;
	}

	public VerbintenisZoekFilter getStap0Filter()
	{
		return stap0Filter;
	}

	public VerbintenisUndo getStap4Undo()
	{
		return stap4Undo;
	}

	public void setStap4Undo(VerbintenisUndo stap4Undo)
	{
		this.stap4Undo = stap4Undo;
	}

	public void setStap4keuze(VerbintenisKeuze stap4keuze)
	{
		this.stap4keuze = stap4keuze;
	}

	public VerbintenisKeuze getStap4keuze()
	{
		return stap4keuze;
	}

	public OrganisatieEenheid getDefaultOrganisatieEenheid()
	{
		return defaultOrganisatieEenheid.getObject();
	}

	public void setDefaultOrganisatieEenheid(OrganisatieEenheid defaultOrganisatieEenheid)
	{
		this.defaultOrganisatieEenheid = ModelFactory.getModel(defaultOrganisatieEenheid);
	}

	public void setStap4idVanNieuweVerbintenis(Serializable stap4idVanNieuweVerbintenis)
	{
		this.stap4idVanNieuweVerbintenis = stap4idVanNieuweVerbintenis;
	}

	public Serializable getStap4idVanNieuweVerbintenis()
	{
		return stap4idVanNieuweVerbintenis;
	}

	@Override
	public void detach()
	{
		if (!detaching && deelnemerModel.isAttached())
		{
			detaching = true;
			ComponentUtil.detachFields(this);
			ComponentUtil.detachQuietly(manager);
			ComponentUtil.detachQuietly(deelnemerModel);

			onDetach();
			detaching = false;
		}
		ComponentUtil.detachQuietly(defaultOrganisatieEenheid);
		ComponentUtil.detachQuietly(begindatum);
	}

	protected void onDetach()
	{
		// extra models in de subclass detachen
	}

	public IChangeRecordingModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemerModel;
	}

	public void setBegindatum(Date begindatum)
	{
		this.begindatum = ModelFactory.getModel(begindatum);
	}

	public Date getBegindatum()
	{
		return begindatum.getObject();
	}
}
