package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.participatie.util.AbsentieMaatregelenUtil;
import nl.topicus.eduarte.participatie.util.LesuurIndelingUtil;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

/**
 * @author vandekamp
 * @author loite
 */
public class WaarnemingAbsentieKoppelUtil
{

	private Medewerker eigenaarMedewerker;

	private Deelnemer eigenaarDeelnemer;

	private List<AbsentieMelding> toegevoegdeMeldingen;

	private List<Waarneming> toegevoegdeWaarnemingen = new ArrayList<Waarneming>();

	private List<Waarneming> verwijderdeWaarnemingen = new ArrayList<Waarneming>();

	/**
	 * Deze class is voor het koppelen van waarnemingen aan absentiemeldingen en andersom.
	 * Nu ook voor het koppelen van waarnemingen aan een afspraak. Omdat deze util altijd
	 * aangeroepen wordt als er een waarneming is toegevoegd
	 * 
	 * @param eigenaarMedewerker
	 */
	public WaarnemingAbsentieKoppelUtil(Medewerker eigenaarMedewerker)
	{
		this.eigenaarMedewerker = eigenaarMedewerker;
		this.eigenaarDeelnemer = null;
		this.toegevoegdeMeldingen = new ArrayList<AbsentieMelding>();
	}

	/**
	 * Deze class is voor het koppelen van waarnemingen aan absentiemeldingen en andersom
	 * 
	 * @param eigenaarDeelnemer
	 */
	public WaarnemingAbsentieKoppelUtil(Deelnemer eigenaarDeelnemer)
	{
		this.eigenaarMedewerker = null;
		this.eigenaarDeelnemer = eigenaarDeelnemer;
		this.toegevoegdeMeldingen = new ArrayList<AbsentieMelding>();
	}

	/**
	 * controleeert of er waarnemingen zijn waar de absentiemelding aan gekoppeld moet
	 * worden, en koppel deze als ze er zijn
	 * 
	 * @param melding
	 * @param commit
	 *            Moet de methode een commit uitvoeren aan het eind, of wordt dit later
	 *            door de aanroeper gedaan?
	 */
	public void koppelWaarnemingen(AbsentieMelding melding, boolean commit)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setDeelnemer(melding.getDeelnemer());
		if (melding.getAbsentieReden().getAbsentieSoort().equals(AbsentieSoort.Absent))
		{
			filter.setBeginDatumTijd(melding.getBeginDatumTijd());
			filter.setEindDatumTijd(melding.getEindDatumTijd());
			filter.setWaarnemingSoort(WaarnemingSoort.Afwezig);
			List<Waarneming> waarnemingenList =
				(DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class)
					.getAlleWaarnemingenVanTot(filter));
			for (Waarneming waarneming : waarnemingenList)
			{
				waarneming.setAfgehandeld(true);
				waarneming.setAbsentieMelding(melding);
				waarneming.saveOrUpdate();
			}
		}
		if (melding.getAbsentieReden().getAbsentieSoort().equals(AbsentieSoort.Telaat)
			|| melding.getAbsentieReden().getAbsentieSoort().equals(AbsentieSoort.Verwijderd))
		{
			filter.setBeginDatumTijd(melding.getBeginDatumTijd());
			filter.setEindDatumTijd(melding.getEindDatumTijd());
			filter.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
			List<Waarneming> waarnemingenList =
				(DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class)
					.getAlleWaarnemingenGroterOfGelijk(filter));
			for (Waarneming waarneming : waarnemingenList)
			{
				waarneming.setAfgehandeld(true);
				waarneming.setAbsentieMelding(melding);
				waarneming.saveOrUpdate();
				break;
			}
		}

		if (commit)
		{
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).batchExecute();
		}
	}

	/**
	 * Methode voor als een absentiemelding gewijzigd wordt Controleeert of er
	 * waarnemingen zijn waar de absentiemelding aan gekoppeld was, en ontkoppeld deze.
	 * Roep daarna zelf de kopppelWaarnemingen methode aan zodat de alles opnieuw
	 * gekoppeld wordt.
	 * 
	 * @param melding
	 * @param commit
	 *            Moet de methode een commit uitvoeren aan het eind, of wordt dit later
	 *            door de aanroeper gedaan?
	 */
	public void ontKoppelWaarnemingen(AbsentieMelding melding, boolean commit)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setDeelnemer(melding.getDeelnemer());
		filter.setAbsentieMelding(melding);
		List<Waarneming> waarnemingenList =
			(DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class)
				.getAlleWaarnemingenVanTot(filter));
		for (Waarneming waarneming : waarnemingenList)
		{
			if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
			{
				waarneming.setAfgehandeld(false);
			}
			waarneming.setAbsentieMelding(null);
			waarneming.saveOrUpdate();
		}
		if (commit)
		{
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).batchExecute();
		}
	}

	/**
	 * @param waarneming
	 * @param absentieReden
	 * @param begin
	 * @param eind
	 * @return {@link AbsentieMelding}
	 */
	protected AbsentieMelding createAbsentieMelding(Waarneming waarneming,
			AbsentieReden absentieReden, Date begin, Date eind)
	{
		Date beginDatumTijd = waarneming.getBeginDatumTijd();
		Date eindDatumTijd = waarneming.getEindDatumTijd();
		if (begin != null)
			beginDatumTijd = begin;
		if (eind != null)
			eindDatumTijd = eind;

		AbsentieMaatregelenUtil absentieMaatregelenUtil = null;
		if (eigenaarMedewerker != null)
			absentieMaatregelenUtil =
				new AbsentieMaatregelenUtil(waarneming.getDeelnemer(), eigenaarMedewerker);
		else
			absentieMaatregelenUtil =
				new AbsentieMaatregelenUtil(waarneming.getDeelnemer(), eigenaarDeelnemer);
		AbsentieMelding absentieMelding = new AbsentieMelding();
		absentieMelding.setDeelnemer(waarneming.getDeelnemer());
		absentieMelding.setAbsentieReden(absentieReden);
		absentieMelding.setBeginDatumTijd(beginDatumTijd);
		absentieMelding.setEindDatumTijd(eindDatumTijd);
		if (beginDatumTijd.equals(waarneming.getBeginDatumTijd())
			&& eindDatumTijd.equals(waarneming.getEindDatumTijd()))
		{
			absentieMelding.setBeginLesuur(waarneming.getBeginLesuur());
			absentieMelding.setEindLesuur(waarneming.getEindLesuur());
		}
		else
		{
			absentieMelding.setBeginLesuur(null);
			absentieMelding.setEindLesuur(null);
		}
		if (absentieReden != null)
			absentieMelding.setAfgehandeld(absentieReden.isStandaardAfgehandeld());
		else
			absentieMelding.setAfgehandeld(false);

		// Dit moet wel een save zijn, en geen batchSave, ander ziet de
		// absentieMaatregelenUtil niet dat de aantal absentiemeldingen veranderd zijn
		absentieMelding.save();
		absentieMelding.commit();
		absentieMaatregelenUtil.berekenMaatregelen(true);
		toegevoegdeMeldingen.add(absentieMelding);
		return absentieMelding;
	}

	/**
	 * @param waarneming
	 * @param soortWaarneming
	 * @param ingevuldeTijd
	 * @param commit
	 * @param organisatieEenheid
	 */
	public void handleWaarnemingToegevoegd(Waarneming waarneming, Object soortWaarneming,
			Time ingevuldeTijd, boolean commit, OrganisatieEenheid organisatieEenheid)
	{
		Date datum = null;
		if (ingevuldeTijd != null)
			datum =
				TimeUtil.getInstance().setTimeOnDate(waarneming.getBeginDatumTijd(), ingevuldeTijd);

		Verbintenis verbintenis = waarneming.getDeelnemer().getEersteInschrijvingOpPeildatum();

		werkOverlappendeWaarnemingenBij(waarneming);
		LesuurIndelingUtil.setLesuren(waarneming, organisatieEenheid, verbintenis.getLocatie());
		if (soortWaarneming instanceof AbsentieReden)
		{
			AbsentieReden absentieReden = (AbsentieReden) soortWaarneming;
			AbsentieSoort absentieSoort = absentieReden.getAbsentieSoort();
			switch (absentieSoort)
			{
				case Absent:
					handleWaarnemingRedenAbsentToegevoegd(waarneming, absentieReden, datum);
					break;
				case Telaat:
					handleWaarnemingRedenTeLaatToegevoegd(waarneming, absentieReden, datum);
					break;
				case Verwijderd:
					handleWaarnemingRedenVerwijderdToegevoegd(waarneming, absentieReden, datum);
					break;
				default:
					break;
			}
		}
		if (soortWaarneming instanceof WaarnemingSoort)
		{
			WaarnemingSoort waarnemingSoort = (WaarnemingSoort) soortWaarneming;
			waarneming.setWaarnemingSoort(waarnemingSoort);
			switch (waarnemingSoort)
			{
				case Aanwezig:
					handleAanwezigWaarnemingToegevoegd(waarneming);
					break;
				case Afwezig:
					handleAfwezigWaarnemingToegevoegd(waarneming);
					break;
				case Nvt:
					handleNvtWaarnemingToegevoegd(waarneming);
					break;
				case DeelsAfwezig:
					handleDeelsAfwezigWaarnemingToegevoegd(waarneming);
					break;
				default:
					break;
			}
		}
		if (waarneming.getAfspraak() == null)
			koppelAfspraakAanWaarneming(waarneming);

		if (commit)
		{
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class).batchExecute();
		}

	}

	private void koppelAfspraakAanWaarneming(Waarneming waarneming)
	{
		// Het kan zijn dat er meerder afspraken zijn dan ga ik kijken welke precies
		// overeenkomen qua tijd, als er een is koppel ik die aan de waarneming, als er
		// geen een is gewoon de eerste uit de lijst
		AfspraakDataAccessHelper helper =
			DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);
		List<Afspraak> afsprakenList =
			helper.getAfspraken(waarneming.getDeelnemer(), waarneming.getBeginDatumTijd(),
				waarneming.getEindDatumTijd());
		for (Afspraak afspraak : afsprakenList)
		{
			if (afspraak.getEindDatumTijd().equals(waarneming.getEindDatumTijd())
				&& afspraak.getBeginDatumTijd().equals(waarneming.getBeginDatumTijd()))
			{
				waarneming.setAfspraak(afspraak);
				return;
			}
		}
		if (afsprakenList.size() > 0)
			waarneming.setAfspraak(afsprakenList.get(0));

	}

	private void werkOverlappendeWaarnemingenBij(Waarneming waarneming)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setBeginDatumTijd(waarneming.getBeginDatumTijd());
		filter.setEindDatumTijd(waarneming.getEindDatumTijd());
		filter.setDeelnemer(waarneming.getDeelnemer());
		WaarnemingDataAccessHelper helper =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);
		List<Waarneming> waarnemingenList = helper.getWaarnemingenOverlapEnGelijk(filter);
		for (Waarneming waarn : waarnemingenList)
		{
			if (!waarn.equals(waarneming))
			{
				Verbintenis verbintenis = waarn.getDeelnemer().getEersteInschrijvingOpPeildatum();
				// Als de waarnemingen qua tijd precies gelijk zijn.
				if (!waarn.getBeginDatumTijd().before(waarneming.getBeginDatumTijd())
					&& !waarn.getEindDatumTijd().after(waarneming.getEindDatumTijd()))
				{
					waarn.delete();
					verwijderdeWaarnemingen.add(waarn);
				}
				// als de begindatumtijd voor de begindatumtijd van de nieuwe waarneming
				// is, en de einddatumtijd niet na einddatumtijd van de nieuwe waarneming
				if (waarn.getBeginDatumTijd().before(waarneming.getBeginDatumTijd())
					&& !waarn.getEindDatumTijd().after(waarneming.getEindDatumTijd()))
				{
					waarn.setEindDatumTijd(waarneming.getBeginDatumTijd());
					LesuurIndelingUtil.setLesuren(waarn, verbintenis.getOrganisatieEenheid(),
						verbintenis.getLocatie());
					waarn.saveOrUpdate();

				}
				// als de einddatumtijd na de einddatumtijd van de nieuwe waarneming is,
				// en de begindatumtijd niet voor begindatumtijd van de nieuwe waarneming
				if (waarn.getEindDatumTijd().after(waarneming.getEindDatumTijd())
					&& !waarn.getBeginDatumTijd().before(waarneming.getBeginDatumTijd()))
				{
					waarn.setBeginDatumTijd(waarneming.getEindDatumTijd());
					LesuurIndelingUtil.setLesuren(waarn, verbintenis.getOrganisatieEenheid(),
						verbintenis.getLocatie());
					waarn.saveOrUpdate();
				}
				// als de einddatumtijd na de einddatumtijd van de nieuwe waarneming is,
				// en de begindatumtijd voor begindatumtijd van de nieuwe waarneming
				if (waarn.getEindDatumTijd().after(waarneming.getEindDatumTijd())
					&& waarn.getBeginDatumTijd().before(waarneming.getBeginDatumTijd()))
				{
					Waarneming waarneming1 = new Waarneming(waarn);
					waarneming1.setEindDatumTijd(waarneming.getBeginDatumTijd());
					Verbintenis verbintenis1 =
						waarneming1.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming1,
						verbintenis1.getOrganisatieEenheid(), verbintenis1.getLocatie());
					waarneming1.save();
					waarn.setBeginDatumTijd(waarneming.getEindDatumTijd());
					LesuurIndelingUtil.setLesuren(waarn, verbintenis.getOrganisatieEenheid(),
						verbintenis.getLocatie());
					waarn.saveOrUpdate();
				}
			}
		}
	}

	/*
	 * Er is een absentie waarneming toegevoegd, de deelnemer was dus afwezig, daarvoor
	 * moet dus een absentiemelding aangemaakt worden
	 */
	private void handleWaarnemingRedenAbsentToegevoegd(Waarneming waarneming,
			AbsentieReden absentieReden, Date datum)
	{
		waarneming.setWaarnemingSoort(WaarnemingSoort.Afwezig);
		if (datum != null)
		{
			Waarneming waarneming1 = new Waarneming(waarneming);
			toegevoegdeWaarnemingen.add(waarneming1);
			Verbintenis verbintenis = waarneming1.getDeelnemer().getEersteInschrijvingOpPeildatum();
			waarneming1.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
			waarneming1.setEindDatumTijd(datum);
			waarneming1.save();
			waarneming.setBeginDatumTijd(datum);
			LesuurIndelingUtil.setLesuren(waarneming1, verbintenis.getOrganisatieEenheid(),
				verbintenis.getLocatie());
			waarneming.setAfgehandeld(true);
			waarneming1.setAfgehandeld(true);

		}
		if (waarneming.getAbsentieMelding() == null)
			waarneming.setAbsentieMelding(createAbsentieMelding(waarneming, absentieReden, datum,
				null));
		else if (!waarneming.getAbsentieMelding().getAbsentieReden().equals(absentieReden))
		{
			if (waarneming.getBeginDatumTijd().equals(
				waarneming.getAbsentieMelding().getBeginDatumTijd())
				&& waarneming.getEindDatumTijd().equals(
					waarneming.getAbsentieMelding().getEindDatumTijd()))
				waarneming.getAbsentieMelding().setAbsentieReden(absentieReden);
		}
		waarneming.saveOrUpdate();
	}

	/*
	 * Er is een telaat waarenming toegevoegd, de deelnemer was dus wel aanwezig. Maak een
	 * absentiemelding, met de begintijd van de waarneming, en als eindtijd het ingevulde
	 * tijdstip
	 */
	private void handleWaarnemingRedenTeLaatToegevoegd(Waarneming waarneming,
			AbsentieReden absentieReden, Date datum)
	{
		if (waarneming.getAbsentieMelding() == null)
			waarneming.setAbsentieMelding(createAbsentieMelding(waarneming, absentieReden, null,
				datum));
		waarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
		waarneming.setAfgehandeld(true);
		waarneming.saveOrUpdate();
	}

	/*
	 * Er is een verwijderd waarneming toegevoegd, de deelnemer was dus wel aanwezig. Maak
	 * een absentiemelding met als begintijd het ingevulde tijdstip, en als eindtijd de
	 * eindtijd van de waarneming
	 */
	private void handleWaarnemingRedenVerwijderdToegevoegd(Waarneming waarneming,
			AbsentieReden absentieReden, Date datum)
	{
		if (waarneming.getAbsentieMelding() == null)
			waarneming.setAbsentieMelding(createAbsentieMelding(waarneming, absentieReden, datum,
				null));
		waarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
		waarneming.setAfgehandeld(true);
		waarneming.saveOrUpdate();
	}

	private void handleAanwezigWaarnemingToegevoegd(Waarneming waarneming)
	{
		waarneming.setAfgehandeld(true);
		if (waarneming.getAbsentieMelding() != null
			&& waarneming.getAbsentieMelding().getEindDatumTijd() == null)
		{
			waarneming.getAbsentieMelding().setEindDatumTijd(waarneming.getBeginDatumTijd());
		}
		waarneming.setAbsentieMelding(null);
		waarneming.saveOrUpdate();
	}

	private void handleAfwezigWaarnemingToegevoegd(Waarneming waarneming)
	{
		// probeer een absentiemelding te vinden die hier tegenoverstaat. De meest recente
		// koppelen (aantal-1), want zo gebeurt het ook in de agenda
		AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
		absentieMeldingZoekFilter.setBeginDatumTijd(waarneming.getBeginDatumTijd());
		absentieMeldingZoekFilter.setEindDatumTijd(waarneming.getEindDatumTijd());
		absentieMeldingZoekFilter.setDeelnemer(waarneming.getDeelnemer());
		AbsentieMeldingDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		List<AbsentieMelding> meldingen =
			helper.getOverlappendeMeldingen(absentieMeldingZoekFilter);
		int aantal = meldingen.size();
		if (aantal > 0)
			waarneming.setAbsentieMelding(meldingen.get(aantal - 1));

		if (waarneming.getAbsentieMelding() != null)
		{
			if (waarneming.getAbsentieMelding().getAbsentieReden().getAbsentieSoort() == AbsentieSoort.Telaat)
			{
				waarneming.setAfgehandeld(false);
			}
			else
			{
				waarneming.setAfgehandeld(true);
			}
		}
		else
			waarneming.setAfgehandeld(false);

		waarneming.saveOrUpdate();

	}

	private void handleNvtWaarnemingToegevoegd(Waarneming waarneming)
	{
		waarneming.setAfgehandeld(true);
		waarneming.saveOrUpdate();
	}

	/**
	 * De deelnemer is deels afwezig, dit moet soms resulteren in een aanwezig waarneming
	 * (telaat en uitgestuurd), en soms in twee verschillende waarnemingen (present en
	 * absent)
	 */
	private void handleDeelsAfwezigWaarnemingToegevoegd(Waarneming waarneming)
	{
		if (waarneming.getAbsentieMelding() != null
			&& waarneming.getAbsentieMelding().getAbsentieReden().getAbsentieSoort() == AbsentieSoort.Absent)
		{
			// Maak twee nieuwe waarnemingen aan en sla deze op.
			AbsentieMelding melding = waarneming.getAbsentieMelding();
			Waarneming presentieWaarneming = new Waarneming(waarneming);
			Waarneming absentieWaarneming = new Waarneming(waarneming);
			toegevoegdeWaarnemingen.add(presentieWaarneming);
			toegevoegdeWaarnemingen.add(absentieWaarneming);
			absentieWaarneming.setWaarnemingSoort(WaarnemingSoort.Afwezig);
			absentieWaarneming.setAfgehandeld(true);
			if (melding.getBeginDatumTijd().after(waarneming.getBeginDatumTijd()))
			{
				absentieWaarneming.setBeginDatumTijd(melding.getBeginDatumTijd());
				presentieWaarneming.setEindDatumTijd(melding.getBeginDatumTijd());
			}
			else
			{
				absentieWaarneming.setEindDatumTijd(melding.getEindDatumTijd());
				presentieWaarneming.setBeginDatumTijd(melding.getEindDatumTijd());
			}
			presentieWaarneming.setAbsentieMelding(null);
			presentieWaarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
			presentieWaarneming.setAfgehandeld(true);
			absentieWaarneming.save();
			presentieWaarneming.save();
		}
		else
		{
			waarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
			waarneming.setAfgehandeld(true);
			waarneming.saveOrUpdate();
		}
	}

	/**
	 * Als een absentiemelding toegevoegd wordt, kunnen er een aantal dingen zijn die
	 * geregeld moeten worden zodat alle data met elkaar overeenkomen. Als een deelnemer
	 * bijvoorbeeld een absentiemelding krijgt terwijl hij/zij present waargenomen is, kan
	 * het zijn dat er een absentiewaarneming voor in de plaats moet komen. Bepaalde
	 * absentiemeldingen kunnen ook leiden tot presentiewaarnemingen. Een te laat melding
	 * kan bijvoorbeeld leiden tot een presentiewaarneming als de deelnemer absent
	 * waargenomen is op hetzelfde lesuur. De deelnemer is dan namelijk door de docent
	 * absent waargenomen, en later door de absentieadministratie te laat gemeld.
	 * 
	 * @param melding
	 *            De absentiemelding die toegevoegd is.
	 * @param commit
	 *            Moet de methode een commit uitvoeren aan het eind, of wordt dit later
	 *            door de aanroeper gedaan?
	 * @param organisatieEenheid
	 */
	public void handleAbsentieMeldingToegevoegd(AbsentieMelding melding, boolean commit,
			OrganisatieEenheid organisatieEenheid)
	{
		WaarnemingZoekFilter filter = new WaarnemingZoekFilter();
		filter.setDeelnemer(melding.getDeelnemer());
		filter.setBeginDatumTijd(melding.getBeginDatumTijd());
		filter.setEindDatumTijd(melding.getEindDatumTijd());
		List<Waarneming> waarnemingenList =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class)
				.getWaarnemingenOverlapEnGelijk(filter);

		Verbintenis verbintenis = melding.getDeelnemer().getEersteInschrijvingOpPeildatum();
		LesuurIndelingUtil.setLesuren(melding, verbintenis.getOrganisatieEenheid(), verbintenis
			.getLocatie());
		if (waarnemingenList.size() > 0)
		{
			// Er zijn ook waarnemingen gevonden. Ga dit verder afhandelen.
			if (melding.getAbsentieReden().getAbsentieSoort() == AbsentieSoort.Telaat)
			{
				handleTeLaatMeldingToegevoegd(melding, waarnemingenList);
			}
			// Er zijn ook waarnemingen gevonden. Ga dit verder afhandelen.
			if (melding.getAbsentieReden().getAbsentieSoort() == AbsentieSoort.Verwijderd)
			{
				handleVerwijderdMeldingToegevoegd(melding, waarnemingenList);
			}
			// Er zijn ook waarnemingen gevonden. Ga dit verder afhandelen.
			if (melding.getAbsentieReden().getAbsentieSoort() == AbsentieSoort.Absent)
			{
				handleAbsentMeldingToegevoegd(melding, waarnemingenList);
			}
		}
		if (commit)
		{
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).batchExecute();
		}
	}

	private void handleAbsentMeldingToegevoegd(AbsentieMelding melding,
			List<Waarneming> waarnemingenList)
	{
		for (Waarneming waarneming1 : waarnemingenList)
		{
			if (waarneming1.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
			{
				waarneming1.setAfgehandeld(true);
				waarneming1.setAbsentieMelding(null);
				// in dit geval 3 waarnemingen maken
				if (waarneming1.getBeginDatumTijd().before(melding.getBeginDatumTijd())
					&& waarneming1.getEindDatumTijd().after(melding.getEindDatumTijd()))
				{
					// lesuren nog goed zetten
					Waarneming waarneming2 = new Waarneming(waarneming1);
					toegevoegdeWaarnemingen.add(waarneming2);
					Waarneming waarneming3 = new Waarneming(waarneming1);
					toegevoegdeWaarnemingen.add(waarneming3);
					waarneming1.setEindDatumTijd(melding.getBeginDatumTijd());
					waarneming2.setBeginDatumTijd(melding.getBeginDatumTijd());
					waarneming2.setEindDatumTijd(melding.getEindDatumTijd());
					waarneming2.setAbsentieMelding(melding);
					waarneming2.setWaarnemingSoort(WaarnemingSoort.Afwezig);
					waarneming3.setBeginDatumTijd(melding.getEindDatumTijd());

					Verbintenis verbintenis1 =
						waarneming1.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming1,
						verbintenis1.getOrganisatieEenheid(), verbintenis1.getLocatie());

					Verbintenis verbintenis2 =
						waarneming2.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming2,
						verbintenis2.getOrganisatieEenheid(), verbintenis2.getLocatie());

					Verbintenis verbintenis3 =
						waarneming3.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming3,
						verbintenis3.getOrganisatieEenheid(), verbintenis3.getLocatie());

					waarneming1.saveOrUpdate();
					waarneming2.save();
					waarneming3.save();
				}
				// in dit geval 2 waarnemingen maken
				else if (waarneming1.getBeginDatumTijd().before(melding.getBeginDatumTijd())
					|| waarneming1.getEindDatumTijd().after(melding.getEindDatumTijd()))
				{
					Waarneming waarneming2 = new Waarneming(waarneming1);
					toegevoegdeWaarnemingen.add(waarneming2);
					if (waarneming1.getBeginDatumTijd().before(melding.getBeginDatumTijd()))
					{
						waarneming1.setEindDatumTijd(melding.getBeginDatumTijd());
						waarneming2.setBeginDatumTijd(melding.getBeginDatumTijd());
						waarneming2.setAbsentieMelding(melding);
						waarneming2.setWaarnemingSoort(WaarnemingSoort.Afwezig);
					}
					if (waarneming1.getEindDatumTijd().after(melding.getEindDatumTijd()))
					{
						waarneming1.setEindDatumTijd(melding.getEindDatumTijd());
						waarneming2.setBeginDatumTijd(melding.getEindDatumTijd());
						waarneming1.setAbsentieMelding(melding);
						waarneming1.setWaarnemingSoort(WaarnemingSoort.Afwezig);
					}
					Verbintenis verbintenis1 =
						waarneming1.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming1,
						verbintenis1.getOrganisatieEenheid(), verbintenis1.getLocatie());

					Verbintenis verbintenis2 =
						waarneming2.getDeelnemer().getEersteInschrijvingOpPeildatum();
					LesuurIndelingUtil.setLesuren(waarneming2,
						verbintenis2.getOrganisatieEenheid(), verbintenis2.getLocatie());

					waarneming1.saveOrUpdate();
					waarneming2.save();
				}
				else
				{
					waarneming1.setAbsentieMelding(melding);
					waarneming1.setWaarnemingSoort(WaarnemingSoort.Afwezig);
					waarneming1.saveOrUpdate();
				}
			}
		}
		melding.saveOrUpdate();
	}

	private void handleVerwijderdMeldingToegevoegd(AbsentieMelding melding,
			List<Waarneming> waarnemingenList)
	{
		// Er is een verwijderd melding toegevoegd. Verander een eventuele
		// absentiewaarneming in een presentiewaarneming. Dit betekent namelijk dat de
		// deelnemer al door de docent absent waargenomen is, en pas later door de
		// receptie te laat gemeld is.
		if (waarnemingenList.size() == 1)
		{
			Waarneming waarneming = waarnemingenList.get(0);
			if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
			{
				waarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
				waarneming.setAfgehandeld(true);
			}
			waarneming.setAbsentieMelding(melding);
			waarneming.update();
		}
	}

	/**
	 * Er is een te laat melding toegevoegd.
	 * 
	 * @param melding
	 *            De te laat melding die toegevoegd is.
	 * @param waarnemingen
	 *            De waarnemingen die overlappen met de te laat melding.
	 */
	private void handleTeLaatMeldingToegevoegd(AbsentieMelding melding,
			List<Waarneming> waarnemingen)
	{
		// Er is een te laat melding toegevoegd. Verander een eventuele absentiewaarneming
		// in een presentiewaarneming. Dit betekent namelijk dat de deelnemer al door de
		// docent absent waargenomen is, en pas later door de receptie te laat gemeld is.
		if (waarnemingen.size() == 1)
		{
			Waarneming waarneming = waarnemingen.get(0);
			if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
			{
				waarneming.setWaarnemingSoort(WaarnemingSoort.Aanwezig);
				waarneming.setAfgehandeld(true);
			}
			waarneming.setAbsentieMelding(melding);
			waarneming.update();
		}
	}

	public void koppelAbsentieMeldingen(List<Waarneming> waarnemingen, List<Deelnemer> deelnemers,
			Date beginDatumTijd, Date eindDatumTijd)
	{
		AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
		absentieMeldingZoekFilter.setBeginDatumTijd(beginDatumTijd);
		absentieMeldingZoekFilter.setEindDatumTijd(eindDatumTijd);
		absentieMeldingZoekFilter.setDeelnemersList(deelnemers);
		absentieMeldingZoekFilter.setBeginEindtijdInclusief(false);
		AbsentieMeldingDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		Map<Deelnemer, AbsentieMelding> meldingen =
			helper.makeMap(helper.list(absentieMeldingZoekFilter));

		for (Waarneming waarneming : waarnemingen)
		{
			if (!waarneming.isSaved())
			{
				AbsentieMelding melding = meldingen.get(waarneming.getDeelnemer());
				if (melding != null)
				{
					waarneming.setAbsentieMelding(melding);
					if (melding.getAbsentieReden().getAbsentieSoort().equals(AbsentieSoort.Absent))
					{
						// Controleer of de absentiemelding helemaal over de waarneming
						// heen
						// valt, of dat de deelnemer slechts deels afwezig is.
						if (waarneming.getBeginDatumTijd().before(melding.getBeginDatumTijd())
							|| (melding.getEindDatumTijd() != null && waarneming.getEindDatumTijd()
								.after(melding.getEindDatumTijd())))
						{
							waarneming.setWaarnemingSoort(WaarnemingSoort.DeelsAfwezig);
						}
						else
						{
							waarneming.setWaarnemingSoort(WaarnemingSoort.Afwezig);
						}
					}
					if (melding.getAbsentieReden().getAbsentieSoort().equals(AbsentieSoort.Telaat))
					{
						if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Aanwezig)
						{
							waarneming.setWaarnemingSoort(WaarnemingSoort.DeelsAfwezig);
						}
					}
					if (melding.getAbsentieReden().getAbsentieSoort().equals(
						AbsentieSoort.Verwijderd))
					{
						waarneming.setWaarnemingSoort(WaarnemingSoort.DeelsAfwezig);
					}
				}
			}
		}

	}

	public List<AbsentieMelding> getToegevoegdeMeldingen()
	{
		return toegevoegdeMeldingen;
	}

	public List<Waarneming> getToegevoegdeWaarnemingen()
	{
		return toegevoegdeWaarnemingen;
	}

	public List<Waarneming> getVerwijderdeWaarnemingen()
	{
		return verwijderdeWaarnemingen;
	}
}
