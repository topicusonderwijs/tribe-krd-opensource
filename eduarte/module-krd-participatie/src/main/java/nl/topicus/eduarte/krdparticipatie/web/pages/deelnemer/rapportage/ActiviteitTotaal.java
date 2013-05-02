package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.entities.personen.Deelnemer;

/**
 * @author vanderkamp
 */
public class ActiviteitTotaal extends AbstractActiviteitTotaal
{
	private static final long serialVersionUID = 1L;

	public ActiviteitTotaal(String omschrijving)
	{
		super(omschrijving);
	}

	public ActiviteitTotaal(Deelnemer deelnemer, String omschrijving, List<Afspraak> afsprakenList,
			BigDecimal budget)
	{
		super(omschrijving);
		Map<String, Long> perRedenSeconds = new HashMap<String, Long>();
		Map<String, Integer> perRedenLesuren = new HashMap<String, Integer>();
		long aanbodSeconds = 0;
		long aanwezigSeconds = 0;
		long afwezigSeconds = 0;
		long geoorAfwSeconds = 0;
		long ongeoorAfwSeconds = 0;
		long geenRedenSeconds = 0;
		int aanbLesuren = 0;
		int aanwLesuren = 0;
		int afwLesuren = 0;
		int geoorAfwLesuren = 0;
		int ongeoorAfwLesuren = 0;
		int geenRedLesuren = 0;
		List<Waarneming> verwerkteWaarnemingen = new ArrayList<Waarneming>();
		for (Afspraak afspraak : afsprakenList)
		{
			long afspraakSeconds = afspraak.getDuurInSeconds();
			int afspraakLesuren = afspraak.getDuurInLesuren();
			aanbodSeconds += afspraakSeconds;
			aanbLesuren += afspraak.getDuurInLesuren();
			long totaalWaarenmingenSeconds = 0;
			int totaalWaarnemingenLesuren = 0;
			List<Waarneming> waarnemingen = getWaarnemingen(deelnemer, afspraak);
			for (Waarneming waarneming : waarnemingen)
			{
				if (verwerkteWaarnemingen.contains(waarneming))
					break;
				verwerkteWaarnemingen.add(waarneming);
				long waarnemingSeconds = waarneming.getDuurInSeconds();
				int waarnemingLesuren = waarneming.getDuurInLesuren();
				totaalWaarenmingenSeconds += waarnemingSeconds;
				totaalWaarnemingenLesuren += waarnemingLesuren;
				if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
				{
					aanwezigSeconds += waarnemingSeconds;
					aanwLesuren += waarnemingLesuren;
					afspraakSeconds -= waarnemingSeconds;
					afspraakLesuren -= waarnemingLesuren;
				}
				else if (waarneming.getAbsentieMelding() != null
					&& waarneming.getAbsentieMelding().getAbsentieReden().isGeoorloofd())
				{
					geoorAfwSeconds += waarnemingSeconds;
					geoorAfwLesuren += waarnemingLesuren;
					afspraakSeconds -= waarnemingSeconds;
					afspraakLesuren -= waarnemingLesuren;
					afwezigSeconds += waarnemingSeconds;
					afwLesuren += waarnemingLesuren;
				}
				else
				{
					ongeoorAfwSeconds += waarnemingSeconds;
					ongeoorAfwLesuren += waarnemingLesuren;
					afspraakSeconds -= waarnemingSeconds;
					afspraakLesuren -= waarnemingLesuren;
					afwezigSeconds += waarnemingSeconds;
					afwLesuren += waarnemingLesuren;
				}
				if (waarneming.getAbsentieMelding() != null)
				{
					String key = waarneming.getAbsentieMelding().getAbsentieReden().getAfkorting();
					if (!perRedenSeconds.containsKey(key))
					{
						perRedenSeconds.put(key, new Long(0));
					}
					perRedenSeconds.put(key, perRedenSeconds.get(key) + waarnemingSeconds);
					if (!perRedenLesuren.containsKey(key))
					{
						perRedenLesuren.put(key, 0);
					}
					perRedenLesuren.put(key, perRedenLesuren.get(key) + waarnemingLesuren);
				}
				else if (!waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
				{
					geenRedenSeconds += waarnemingSeconds;
					geenRedLesuren += waarnemingLesuren;
				}
			}
		}
		Map<String, BigDecimal> perRedenKlokUren = new HashMap<String, BigDecimal>();
		for (String key : perRedenSeconds.keySet())
		{
			perRedenKlokUren.put(key, getKlokUren(perRedenSeconds.get(key)));
		}
		setTotalenPerReden(perRedenKlokUren);
		setAanbod(getKlokUren(aanbodSeconds));
		setAanwezig(getKlokUren(aanwezigSeconds));
		setAfwezig(getKlokUren(afwezigSeconds));
		setGeoorloofdAfwezig(getKlokUren(geoorAfwSeconds));
		setOngeoorloofdAfwezig(getKlokUren(ongeoorAfwSeconds));
		setGeenReden(getKlokUren(geenRedenSeconds));
		long totaalSeconds = geoorAfwSeconds + ongeoorAfwSeconds + aanwezigSeconds;
		setProcentAanwezig(getProcentAanwezig(totaalSeconds, aanwezigSeconds));
		setProcentAfwezig(getProcentGeoorloofdAfwezig(totaalSeconds, afwezigSeconds));
		setProcentGeoorloofdAfwezig(getProcentGeoorloofdAfwezig(totaalSeconds, geoorAfwSeconds));
		setProcentOngeoorloofdAfwezig(getProcentOngeoorloofdAfwezig(totaalSeconds,
			ongeoorAfwSeconds));
		setTotalenPerRedenLesuren(perRedenLesuren);
		if (budget.compareTo(BigDecimal.ZERO) == 1)
		{
			setBudget(budget);
			setResterend(getBudget().subtract(getAanbod()));
		}
		setAanbodLesuren(aanbLesuren);
		setAanwezigLesuren(aanwLesuren);
		setAfwezigLesuren(afwLesuren);
		setGeoorloofdAfwezigLesuren(geoorAfwLesuren);
		setOngeoorloofdAfwezigLesuren(ongeoorAfwLesuren);
		setGeenRedenLesuren(geenRedLesuren);
	}

	private List<Waarneming> getWaarnemingen(Deelnemer deelnemer, Afspraak afspraak)
	{
		List<Waarneming> waarnemingen =
			DataAccessRegistry.getHelper(WaarnemingDataAccessHelper.class).getByDeelnemerAfspraak(
				deelnemer, afspraak, afspraak.getOrganisatie());
		if (waarnemingen != null && !waarnemingen.isEmpty())
			return waarnemingen;
		return Collections.emptyList();
	}
}
