package nl.topicus.eduarte.rapportage.leerplicht;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;
import nl.topicus.eduarte.zoekfilters.LeerplichtRapportageZoekFilter;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vanderkamp
 */
public class LeerplichtRapportageModel extends LoadableDetachableModel<List<LeerplichtRapportage>>
{
	private static final long serialVersionUID = 1L;

	private LeerplichtRapportageZoekFilter filter;

	private List<LeerplichtRapportage> leerplichtRapportages;

	public LeerplichtRapportageModel(LeerplichtRapportageZoekFilter filter)
	{
		this.filter = filter;
	}

	@Override
	protected List<LeerplichtRapportage> load()
	{

		// Als er niks is geselecteerd moet hij niks doen
		if (filter.getSoortDeelnemer() == null)
		{
			return new ArrayList<LeerplichtRapportage>();
		}

		DeelnemerDataAccessHelper helper =
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class);

		List<Deelnemer> deelnemers = getDeelnemers(helper);

		leerplichtRapportages = new ArrayList<LeerplichtRapportage>();

		WaarnemingDataAccessHelper waarnemingHelper =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class);

		WaarnemingZoekFilter waarnemingFilter = getWaarnemingZoekFilter();

		Date beginDatumAbsent =
			TimeUtil.getInstance().addWeeks(filter.getPeildatum(), (0 - filter.getAantalWeken()));

		for (Deelnemer deelnemer : deelnemers)
		{

			waarnemingFilter.setDeelnemer(deelnemer);
			waarnemingFilter.setBeginDatumTijd(beginDatumAbsent);
			// waarnemingFilter.setOrganisatieEenheid(filter.getOrganisatieEenheid());
			// waarnemingFilter.setLocatie(filter.getLocatie());

			SoortLeerplichtDeelnemer soortDeelnemer = filter.getSoortDeelnemer();

			if (soortDeelnemer == SoortLeerplichtDeelnemer.KWALIFICATIE_PLICHTIG
				|| soortDeelnemer == SoortLeerplichtDeelnemer.LEERPLICHTIG)
			{
				List<Waarneming> waarnemingenAantalUrenAbsent =
					waarnemingHelper.list(waarnemingFilter);

				int aantalUrenAbsent = getAantalUrenAbsent(waarnemingenAantalUrenAbsent);
				if (aantalUrenAbsent >= filter.getAantalklokuren())
					leerplichtRapportages.add(new LeerplichtRapportage(deelnemer, aantalUrenAbsent,
						0));
			}
			else
			{
				int aantalWekenAbsent =
					getAantalWekenAbsent(waarnemingHelper, waarnemingFilter, filter, deelnemer);
				if (aantalWekenAbsent >= filter.getAantalWekenAchtereenvolgendAfwezig())
					leerplichtRapportages.add(new LeerplichtRapportage(deelnemer, 0,
						aantalWekenAbsent));
			}

		}

		return leerplichtRapportages;
	}

	private WaarnemingZoekFilter getWaarnemingZoekFilter()
	{
		WaarnemingZoekFilter waarnemingFilter = new WaarnemingZoekFilter();
		waarnemingFilter.setEindDatumTijd(filter.getPeildatum());
		waarnemingFilter.setDatumTijdExactGelijk(false);
		waarnemingFilter.addOrderByProperty("beginDatumTijd");
		waarnemingFilter.setAlleenOngeoorloofd(filter.isOngeoorlooft());
		waarnemingFilter.setOrganisatieEenheid(filter.getOrganisatieEenheid());
		waarnemingFilter.setLocatie(filter.getLocatie());
		return waarnemingFilter;
	}

	private List<Deelnemer> getDeelnemers(DeelnemerDataAccessHelper helper)
	{

		SoortLeerplichtDeelnemer soortDeelnemer = filter.getSoortDeelnemer();

		Date geboorteVanaf = null;
		Date geboorteTot = null;
		Date refDatum =
			TimeUtil.getInstance().asDate(TimeUtil.getInstance().getYear(filter.getPeildatum()), 7,
				31);

		if (soortDeelnemer != null)
		{
			switch (soortDeelnemer)
			{
				case KWALIFICATIE_PLICHTIG:
					// begindatum 31/7 van het jaar van de 16 verjaardag : eindatum 18e
					// Verjaardag
					geboorteTot = TimeUtil.getInstance().addYears(refDatum, (0 - 16));
					geboorteVanaf =
						TimeUtil.getInstance().addYears(filter.getPeildatum(), (0 - 18));
					break;

				case LEERPLICHTIG:
					// begindatum 5 jaar of ouder : einddatum 31/7 van het jaar van de 16
					// verjaardag
					geboorteTot = TimeUtil.getInstance().addYears(filter.getPeildatum(), (0 - 5));
					geboorteVanaf = TimeUtil.getInstance().addYears(refDatum, (0 - 16));
					break;

				case VSV:
					// begindatum 18e Verjaardag : eindatum 23e Verjaardag
					geboorteTot = TimeUtil.getInstance().addYears(filter.getPeildatum(), (0 - 18));
					geboorteVanaf =
						TimeUtil.getInstance().addYears(filter.getPeildatum(), (0 - 23));
					break;

				case WTOS_WSF:
					break;
			}
		}
		else
		{
			geboorteTot = filter.getPeildatum();
		}

		filter.getDeelnemerFilter().setGeboortedatumVanaf(geboorteVanaf);
		filter.getDeelnemerFilter().setGeboortedatumTotEnMet(geboorteTot);
		filter.getDeelnemerFilter().setOrganisatieEenheid(filter.getOrganisatieEenheid());
		filter.getDeelnemerFilter().setLocatie(filter.getLocatie());
		List<Deelnemer> deelnemers = helper.getLeerplichtige(filter.getDeelnemerFilter());

		return deelnemers;
	}

	/**
	 * deze functie berekent het aantal dagen absent op basis van het volgende: elke dag
	 * waarop de deelnemer absent is geweest en niet dezelfde dag ook aanwezig wordt
	 * aangemerkt als een dag absent
	 */
	private int getAantalUrenAbsent(List<Waarneming> waarnemingen)
	{
		int aantalUren = 0;
		TimeUtil util = TimeUtil.getInstance();

		if (!waarnemingen.isEmpty())
		{
			// boolean alleenAbsentieWaarnemingen = true;
			for (Waarneming waarneming : waarnemingen)
				if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
				{
					aantalUren =
						aantalUren
							+ util.getDifferenceInMinutes(waarneming.getEindDatumTijd(), waarneming
								.getBeginDatumTijd());
				}
		}

		aantalUren /= 60;

		return aantalUren;
	}

	private int getAantalWekenAbsent(WaarnemingDataAccessHelper helper,
			WaarnemingZoekFilter waarnemingFilter, LeerplichtRapportageZoekFilter leerplichtFilter,
			Deelnemer deelnemer)
	{

		TimeUtil tutil = TimeUtil.getInstance();
		WaarnemingZoekFilter TempFilter = waarnemingFilter;
		TempFilter.setEindDatumTijd(tutil.addDays(TempFilter.getBeginDatumTijd(), 7));
		TempFilter.setDeelnemer(deelnemer);
		int weekCounter = 0;

		while (TempFilter.getEindDatumTijd().before(waarnemingFilter.getPeildatum()))
		{

			List<Waarneming> waarnemingen = helper.list(TempFilter);
			int afzwezigCounter = 0;
			int aanwezigCounter = 0;

			for (Waarneming waarneming : waarnemingen)
			{
				if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Afwezig)
				{
					afzwezigCounter++;
				}
				else if (waarneming.getWaarnemingSoort() == WaarnemingSoort.Aanwezig)
				{
					aanwezigCounter++;
				}
			}

			if (afzwezigCounter >= 1 && aanwezigCounter == 0)
			{
				weekCounter++;
			}
			else if (aanwezigCounter >= 1)
			{
				if (weekCounter <= leerplichtFilter.getAantalWekenAchtereenvolgendAfwezig())
				{
					return weekCounter;
				}
				else
				{
					weekCounter = 0;
				}
			}
			TempFilter.setBeginDatumTijd(tutil.addDays(TempFilter.getBeginDatumTijd(), 7));
			TempFilter.setEindDatumTijd(tutil.addDays(TempFilter.getEindDatumTijd(), 7));
		}

		return weekCounter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
		if (leerplichtRapportages != null)
		{
			for (LeerplichtRapportage leerplichtRapportage : leerplichtRapportages)
			{
				ComponentUtil.detachQuietly(leerplichtRapportage);
			}
		}
	}
}
