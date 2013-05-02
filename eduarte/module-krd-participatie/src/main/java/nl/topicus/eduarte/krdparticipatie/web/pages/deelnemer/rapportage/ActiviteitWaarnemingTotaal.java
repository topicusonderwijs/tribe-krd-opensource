package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;

/**
 * @author vanderkamp
 */
public class ActiviteitWaarnemingTotaal extends AbstractActiviteitTotaal
{
	private static final long serialVersionUID = 1L;

	public ActiviteitWaarnemingTotaal(String omschrijving)
	{
		super(omschrijving);
	}

	public ActiviteitWaarnemingTotaal(String omschrijving, List<Waarneming> waarnemingenList,
			BigDecimal budget)
	{
		super(omschrijving);
		Map<String, Long> perRedenSeconds = new HashMap<String, Long>();
		Map<String, Integer> perRedenLesuren = new HashMap<String, Integer>();
		long aanwezigSeconds = 0;
		long afwezigSeconds = 0;
		long geoorAfwSeconds = 0;
		long ongeoorAfwSeconds = 0;
		long geenRedenSeconds = 0;
		int aanwLesuren = 0;
		int afwLesuren = 0;
		int geoorAfwLesuren = 0;
		int ongeoorAfwLesuren = 0;
		int geenRedLesuren = 0;
		long totaalWaarenmingenSeconds = 0;
		int totaalWaarnemingenLesuren = 0;
		for (Waarneming waarneming : waarnemingenList)
		{
			long waarnemingSeconds = waarneming.getDuurInSeconds();
			int waarnemingLesuren = waarneming.getDuurInLesuren();
			totaalWaarenmingenSeconds += waarnemingSeconds;
			totaalWaarnemingenLesuren += waarnemingLesuren;
			if (waarneming.getWaarnemingSoort().equals(WaarnemingSoort.Aanwezig))
			{
				aanwezigSeconds += waarnemingSeconds;
				aanwLesuren += waarnemingLesuren;
			}
			else if (waarneming.getAbsentieMelding() != null
				&& waarneming.getAbsentieMelding().getAbsentieReden().isGeoorloofd())
			{
				geoorAfwSeconds += waarnemingSeconds;
				geoorAfwLesuren += waarnemingLesuren;
				afwezigSeconds += waarnemingSeconds;
				afwLesuren += waarnemingLesuren;
			}
			else
			{
				ongeoorAfwSeconds += waarnemingSeconds;
				ongeoorAfwLesuren += waarnemingLesuren;
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
		Map<String, BigDecimal> perRedenKlokUren = new HashMap<String, BigDecimal>();
		for (String key : perRedenSeconds.keySet())
		{
			perRedenKlokUren.put(key, getKlokUren(perRedenSeconds.get(key)));
		}
		setTotalenPerReden(perRedenKlokUren);
		setAanbod(getKlokUren(totaalWaarenmingenSeconds));
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
		setAanbodLesuren(totaalWaarnemingenLesuren);
		setAanwezigLesuren(aanwLesuren);
		setAfwezigLesuren(afwLesuren);
		setGeoorloofdAfwezigLesuren(geoorAfwLesuren);
		setOngeoorloofdAfwezigLesuren(ongeoorAfwLesuren);
		setGeenRedenLesuren(geenRedLesuren);
	}

	/**
	 * Maakt een nieuwe ActiviteitTotaal dat de optelsom is van de meegegeven
	 * activiteittotalen.
	 * 
	 * @param list
	 * @return het totaal van de gegeven lijst
	 */
	public static ActiviteitWaarnemingTotaal createTotaal(List<ActiviteitWaarnemingTotaal> list)
	{
		boolean budgetGevonden = false;
		BigDecimal aanbod = BigDecimal.ZERO;
		int aanbodLesuren = 0;
		BigDecimal aanwezig = BigDecimal.ZERO;
		int aanwezigLesuren = 0;
		BigDecimal afwezig = BigDecimal.ZERO;
		int afwezigLesuren = 0;
		BigDecimal budget = BigDecimal.ZERO;
		BigDecimal geenReden = BigDecimal.ZERO;
		int geenRedenLesuren = 0;
		BigDecimal geoorloofdAfwezig = BigDecimal.ZERO;
		int geoorloofdAfwezigLesuren = 0;
		BigDecimal ongeoorloofdAfwezig = BigDecimal.ZERO;
		int ongeoorloofdAfwezigLesuren = 0;
		BigDecimal resterend = BigDecimal.ZERO;
		Map<String, BigDecimal> totalenPerReden = new HashMap<String, BigDecimal>();
		Map<String, Integer> totalenPerRedenLesuur = new HashMap<String, Integer>();
		for (ActiviteitWaarnemingTotaal totaal : list)
		{
			aanbod = aanbod.add(totaal.getAanbod());
			aanbodLesuren = aanbodLesuren + totaal.getAanbodLesuren();
			aanwezig = aanwezig.add(totaal.getAanwezig());
			aanwezigLesuren = aanwezigLesuren + totaal.getAanwezigLesuren();
			afwezig = afwezig.add(totaal.getAfwezig());
			afwezigLesuren = afwezigLesuren + totaal.getAfwezigLesuren();
			if (totaal.getBudget() != null)
			{
				budgetGevonden = true;
				budget = budget.add(totaal.getBudget());
			}
			if (totaal.getResterend() != null)
			{
				resterend = resterend.add(totaal.getResterend());
			}
			geenReden = geenReden.add(totaal.getGeenReden());
			geenRedenLesuren = geenRedenLesuren + totaal.getGeenRedenLesuren();
			geoorloofdAfwezig = geoorloofdAfwezig.add(totaal.getGeoorloofdAfwezig());
			geoorloofdAfwezigLesuren =
				geoorloofdAfwezigLesuren + totaal.getGeoorloofdAfwezigLesuren();
			ongeoorloofdAfwezig = ongeoorloofdAfwezig.add(totaal.getOngeoorloofdAfwezig());
			ongeoorloofdAfwezigLesuren =
				ongeoorloofdAfwezigLesuren + totaal.getOngeoorloofdAfwezigLesuren();
			for (String key : totaal.getTotalenPerReden().keySet())
			{
				BigDecimal redenTotaal = totaal.getTotaalVoorReden(key);
				if (redenTotaal == null)
					redenTotaal = BigDecimal.ZERO;
				BigDecimal totaalRedenTotaal = totalenPerReden.get(key);
				if (totaalRedenTotaal == null)
					totaalRedenTotaal = BigDecimal.ZERO;
				totaalRedenTotaal = totaalRedenTotaal.add(redenTotaal);
				totalenPerReden.put(key, totaalRedenTotaal);
			}
			for (String key : totaal.getTotalenPerRedenLesuren().keySet())
			{
				Integer redenTotaal = totaal.getTotaalVoorRedenLesuren(key);
				if (redenTotaal == null)
					redenTotaal = Integer.valueOf(0);
				Integer totaalRedenTotaal = totalenPerRedenLesuur.get(key);
				if (totaalRedenTotaal == null)
					totaalRedenTotaal = Integer.valueOf(0);
				totaalRedenTotaal =
					Integer.valueOf(totaalRedenTotaal.intValue() + redenTotaal.intValue());
				totalenPerRedenLesuur.put(key, totaalRedenTotaal);
			}
		}
		ActiviteitWaarnemingTotaal res = new ActiviteitWaarnemingTotaal("Totaal");
		res.setAanbod(aanbod);
		res.setAanbodLesuren(aanbodLesuren);
		res.setAanwezig(aanwezig);
		res.setAanwezigLesuren(aanwezigLesuren);
		res.setAfwezig(afwezig);
		res.setAfwezigLesuren(afwezigLesuren);
		res.setGeenReden(geenReden);
		res.setGeenRedenLesuren(geenRedenLesuren);
		res.setGeoorloofdAfwezig(geoorloofdAfwezig);
		res.setGeoorloofdAfwezigLesuren(geoorloofdAfwezigLesuren);
		res.setOngeoorloofdAfwezig(ongeoorloofdAfwezig);
		res.setOngeoorloofdAfwezigLesuren(ongeoorloofdAfwezigLesuren);
		BigDecimal hour = BigDecimal.valueOf(3600);
		long aanwezigSeconden = res.getAanwezig().multiply(hour).longValue();
		long afwezigSeconden = res.getAfwezig().multiply(hour).longValue();
		long geoorloofdAfwezigSeconden = res.getGeoorloofdAfwezig().multiply(hour).longValue();
		long ongeoorloofdAfwezigSeconden = res.getOngeoorloofdAfwezig().multiply(hour).longValue();
		long totaalSeconden =
			aanwezigSeconden + geoorloofdAfwezigSeconden + ongeoorloofdAfwezigSeconden;
		res.setProcentAanwezig(getProcentAanwezig(totaalSeconden, aanwezigSeconden));
		res.setProcentAfwezig(getProcentGeoorloofdAfwezig(totaalSeconden, afwezigSeconden));
		res.setProcentGeoorloofdAfwezig(getProcentGeoorloofdAfwezig(totaalSeconden,
			geoorloofdAfwezigSeconden));
		res.setProcentOngeoorloofdAfwezig(getProcentOngeoorloofdAfwezig(totaalSeconden,
			ongeoorloofdAfwezigSeconden));
		res.setTotalenPerReden(totalenPerReden);
		res.setTotalenPerRedenLesuren(totalenPerRedenLesuur);

		if (budgetGevonden)
		{
			res.setBudget(budget);
			res.setResterend(resterend);
		}

		return res;
	}
}
