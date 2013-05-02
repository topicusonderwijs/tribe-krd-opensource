package nl.topicus.eduarte.participatie.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenningsRegel;
import nl.topicus.eduarte.entities.participatie.enums.MaatregelToekennenOp;
import nl.topicus.eduarte.entities.participatie.enums.PeriodeType;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelZoekFilter;

/**
 * Class voor het berekenen van maatregelen die aan een deelnemer gekoppeld moeten worden
 * als gevolg van nieuwe absentiemeldingen. De maatregelen worden berekend op basis van de
 * aangemaakte maatregel toekenningsregels. Er worden nooit maatregelen verwijderd, alleen
 * toegevoegd indien dit volgens de regels zou moeten gebeuren. Haal eerst alle actieve
 * maatregelen voor de organisatieenheid op Haal van deze maatregelen alle actieve
 * toekennnings regels op Haal het aantal absentiemeldingen op voor de deelnemer op basis
 * van begin en eindatum toekenningsregel, en absentieReden maak maatregel toekeninngen
 * 
 * @author vandekamp
 */
public class AbsentieMaatregelenUtil
{
	private final Map<MaatregelToekenningsRegel, Integer> origineleAantallen;

	private final Deelnemer deelnemer;

	private final Medewerker eigenaarMedewerker;

	private final Deelnemer eigenaarDeelnemer;

	/**
	 * Constructor voor een util waarmee de eventueel automatisch aan te maken maatregelen
	 * voor een deelnemer berekend kan worden. Een instance van deze util moet aangemaakt
	 * worden voordat de nieuwe absentiemeldingen voor de deelnemer worden opgeslagen. Na
	 * het opslaan van de nieuwe absentiemeldingen moet de methode berekenMaatregelen
	 * aangeroepen worden.
	 * 
	 * @param deelnemer
	 * @param medewerker
	 */
	public AbsentieMaatregelenUtil(Deelnemer deelnemer, Medewerker medewerker)
	{
		this.deelnemer = deelnemer;
		this.eigenaarMedewerker = medewerker;
		this.eigenaarDeelnemer = null;
		origineleAantallen = loadAantallen();
	}

	/**
	 * Constructor voor een util waarmee de eventueel automatisch aan te maken maatregelen
	 * voor een deelnemer berekend kan worden. Een instance van deze util moet aangemaakt
	 * worden voordat de nieuwe absentiemeldingen voor de deelnemer worden opgeslagen. Na
	 * het opslaan van de nieuwe absentiemeldingen moet de methode berekenMaatregelen
	 * aangeroepen worden.
	 * 
	 * @param deelnemer
	 * @param eigenaarDeelnemer
	 */
	public AbsentieMaatregelenUtil(Deelnemer deelnemer, Deelnemer eigenaarDeelnemer)
	{
		this.deelnemer = deelnemer;
		this.eigenaarMedewerker = null;
		this.eigenaarDeelnemer = eigenaarDeelnemer;
		origineleAantallen = loadAantallen();
	}

	/**
	 * Berekent eventuele automatische maatregelen die toegekend moeten worden aan deze
	 * deelnemer.
	 * 
	 * @param commitChanges
	 *            Hiermee kan aangegeven worden of de wijzigingen gecommit moeten worden,
	 *            of dat de aanroeper dit zelf doet.
	 * @return De lijst met aangemaakte matregelen. Dit is altijd non-null, maar kan een
	 *         lege lijst zijn.
	 */
	public List<MaatregelToekenning> berekenMaatregelen(boolean commitChanges)
	{
		List<MaatregelToekenning> res = new ArrayList<MaatregelToekenning>(2);
		Map<MaatregelToekenningsRegel, Integer> map = loadAantallen();
		// Vergelijk huidige aantallen met de oorspronkelijke aantallen.
		for (MaatregelToekenningsRegel regel : map.keySet())
		{
			int huidigAantal = map.get(regel).intValue();
			int origAantal = origineleAantallen.get(regel).intValue();
			if (huidigAantal > origAantal)
			{
				// Melding(en) toegevoegd. Controleer of het aantal
				// overeenkomt met iets waarbij een maatregel toegevoegd
				// moet worden.
				switch (regel.getRegelsoort())
				{
					case Gelijk_Aan:
						// Aantal meldingen moet gelijk zijn aan een bepaald aantal.
						// Als dit aantal hoger is dan het vorige aantal, en kleiner of
						// gelijk aan het nieuwe aantal, moet een maatregel toegevoegd
						// worden.
						if (origAantal < regel.getAantalMeldingen()
							&& huidigAantal >= regel.getAantalMeldingen())
						{
							// Deelnemer voldoet aan eisen. Voeg maatregel toe.
							if (regel.getMaatregel().isAutomatischeMaatregelTonen())
							{
								res.add(maakAbsentieMaatregelToekenning(regel, commitChanges));
							}
							else
							{
								// voeg de maatregel niet toe aan de lijst, zodat ze niet
								// getoond worden, en sla direct op
								maakAbsentieMaatregelToekenning(regel, true);
							}
						}
						break;
					case Elke_X_Meldingen:
						// Aantal meldingen moet deelbaar zijn door een bepaald aantal.
						// Er kunnen een aantal vrije meldingen gelden.
						for (int aantal = origAantal + 1; aantal <= huidigAantal; aantal++)
						{
							int testAantal = aantal - regel.getAantalVrijeMeldingen();
							if (testAantal > 0 && testAantal % regel.getAantalMeldingen() == 0)
							{
								// Deelnemer voldoet aan eisen. Voeg maatregel toe.
								if (regel.getMaatregel().isAutomatischeMaatregelTonen())
								{
									res.add(maakAbsentieMaatregelToekenning(regel, commitChanges));
								}
								else
								{
									// voeg de maatregel niet toe aan de lijst, zodat ze
									// niet getoond worden, en sla direct op
									maakAbsentieMaatregelToekenning(regel, true);
								}
							}
						}
						break;
				}
			}
		}

		AbsentieMeldingDataAccessHelper helper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		helper.batchExecute();
		return res;
	}

	/**
	 * Maakt een nieuwe maatregeltoekenning aan voor de huidige deelnemer.
	 * 
	 * @param regel
	 * @param commitChanges
	 * @return
	 */
	private MaatregelToekenning maakAbsentieMaatregelToekenning(MaatregelToekenningsRegel regel,
			boolean commitChanges)
	{
		AbsentieMeldingDataAccessHelper meldingHelper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
		absentieMeldingZoekFilter.setDeelnemer(deelnemer);
		absentieMeldingZoekFilter.setAbsentieReden(regel.getAbsentieReden());
		absentieMeldingZoekFilter.setBeginDatumTijd(getBeginDatum(regel));
		absentieMeldingZoekFilter.setEindDatumTijd(getEindDatum(regel));

		MaatregelToekenning maatregel = new MaatregelToekenning();
		maatregel.setDeelnemer(deelnemer);
		if (eigenaarMedewerker != null)
			maatregel.setEigenaarMedewerker(eigenaarMedewerker);
		else
			maatregel.setEigenaarDeelnemer(eigenaarDeelnemer);
		maatregel.setMaatregel(regel.getMaatregel());
		maatregel.setAutomatischToegekend(true);
		maatregel.setMaatregelDatum(setMaatregelDatum(regel));
		AbsentieMelding veroorzaaktDoor =
			meldingHelper.getLaatstToegevoegdeMelding(absentieMeldingZoekFilter);
		maatregel.setVeroorzaaktDoor(veroorzaaktDoor);
		if (commitChanges)
			maatregel.save();

		return maatregel;
	}

	private Date setMaatregelDatum(MaatregelToekenningsRegel regel)
	{
		Date maatregelToekennenDatum = null;
		Date currentDate = TimeUtil.getInstance().currentDate();
		currentDate = TimeUtil.getInstance().setTimeOnDate(currentDate, Time.valueOf("23:59"));
		if (regel.getMaatregelToekennenOp().equals(MaatregelToekennenOp.Dezelfde_Dag))
			maatregelToekennenDatum = currentDate;
		else if (regel.getMaatregelToekennenOp().equals(MaatregelToekennenOp.Volgende_Schooldag))
		{
			AfspraakDataAccessHelper helper =
				DataAccessRegistry.getHelper(AfspraakDataAccessHelper.class);
			Afspraak afspraak = helper.getVolgendeAfspraak(deelnemer, currentDate);
			if (afspraak != null)
				maatregelToekennenDatum = afspraak.getBeginDatum();
		}
		if (maatregelToekennenDatum != null)
			return maatregelToekennenDatum;
		return TimeUtil.getInstance().nextWorkDay(currentDate);
	}

	/**
	 * Laadt de huidige aantallen van absentiemeldingen voor deze deelnemer in geheugen.
	 * 
	 * @return
	 */
	private Map<MaatregelToekenningsRegel, Integer> loadAantallen()
	{
		Map<MaatregelToekenningsRegel, Integer> map =
			new HashMap<MaatregelToekenningsRegel, Integer>();
		AbsentieMeldingDataAccessHelper meldingHelper =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class);
		AbsentieMeldingZoekFilter absentieMeldingZoekFilter = new AbsentieMeldingZoekFilter();
		absentieMeldingZoekFilter.setDeelnemer(deelnemer);

		MaatregelDataAccessHelper maatregelHelper =
			DataAccessRegistry.getHelper(MaatregelDataAccessHelper.class);
		MaatregelZoekFilter maatregelZoekFilter = new MaatregelZoekFilter();
		maatregelZoekFilter.setActief(Boolean.TRUE);
		List<Maatregel> maatregelen = maatregelHelper.list(maatregelZoekFilter);
		for (Maatregel maatregel : maatregelen)
		{
			for (MaatregelToekenningsRegel regel : maatregel.getMaatregelToekenningsRegels())
			{

				if (regel.isActief() && hoortBijOrganisatieEenheid(regel, deelnemer))
				{
					// checken op periodes en data daarvanaf l
					absentieMeldingZoekFilter.setAbsentieReden(regel.getAbsentieReden());
					absentieMeldingZoekFilter.setBeginDatumTijd(getBeginDatum(regel));
					absentieMeldingZoekFilter.setEindDatumTijd(getEindDatum(regel));
					int aantal = meldingHelper.getAantalMeldingen(absentieMeldingZoekFilter);
					map.put(regel, Integer.valueOf(aantal));
				}
			}
		}
		return map;
	}

	private boolean hoortBijOrganisatieEenheid(MaatregelToekenningsRegel regel, Deelnemer deelnemer2)
	{
		if (regel.getOrganisatieEenheid() == null)
			return true;

		for (Verbintenis verbintenis : ((Deelnemer) deelnemer2.doUnproxy()).getVerbintenissen())
		{
			if (regel.getOrganisatieEenheid().getActieveChildren(
				TimeUtil.getInstance().currentDate()).contains(verbintenis.getOrganisatieEenheid()))
				return true;
		}
		return false;
	}

	private Date getEindDatum(MaatregelToekenningsRegel regel)
	{
		Date eindDatum = TimeUtil.getInstance().currentDate();
		TimeUtil.getInstance().setTimeOnDate(eindDatum, Time.valueOf("23:59"));
		if (regel.getPeriodeType().equals(PeriodeType.Schooljaar))
		{
			return Schooljaar.huidigSchooljaar().getEinddatum();
		}
		if (regel.getPeriodeType().equals(PeriodeType.Laatste_x_weken))
		{
			return eindDatum;
		}
		if (regel.getPeriodeType().equals(PeriodeType.Periode))
		{
			if (regel.getPeriode() != null
				&& regel.getPeriode().getPeriodesVanPeildatum(eindDatum) != null
				&& regel.getPeriode().getPeriodesVanPeildatum(eindDatum).getDatumEind() != null)
				return regel.getPeriode().getPeriodesVanPeildatum(eindDatum).getDatumEind();
		}
		return eindDatum;
	}

	private Date getBeginDatum(MaatregelToekenningsRegel regel)
	{
		Date beginDatum = TimeUtil.getInstance().currentDate();
		if (regel.getPeriodeType().equals(PeriodeType.Schooljaar))
		{
			return Schooljaar.huidigSchooljaar().getBegindatum();
		}
		if (regel.getPeriodeType().equals(PeriodeType.Laatste_x_weken))
		{
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.add(Calendar.WEEK_OF_YEAR, (0 - regel.getAantalWeken()));
			return beginCalendar.getTime();
		}
		if (regel.getPeriodeType().equals(PeriodeType.Periode))
		{
			if (regel.getPeriode().getPeriodesVanPeildatum(beginDatum) != null)
				return regel.getPeriode().getPeriodesVanPeildatum(beginDatum).getDatumBegin();
		}
		return beginDatum;
	}
}
