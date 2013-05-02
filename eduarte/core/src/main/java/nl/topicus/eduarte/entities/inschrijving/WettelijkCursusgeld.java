package nl.topicus.eduarte.entities.inschrijving;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

public class WettelijkCursusgeld
{
	private static final Map<Schooljaar, BigDecimal> BBL_OF_DEELTIJD_BOL_NIVO_1_2 =
		new HashMap<Schooljaar, BigDecimal>();

	private static final Map<Schooljaar, BigDecimal> BBL_OF_DEELTIJD_BOL_NIVO_3_4 =
		new HashMap<Schooljaar, BigDecimal>();

	private static final Map<Schooljaar, BigDecimal> VAVO_PRIJS_PER_45_MINUTEN =
		new HashMap<Schooljaar, BigDecimal>();

	static
	{
		// bedragen 2008/2009
		BBL_OF_DEELTIJD_BOL_NIVO_1_2.put(Schooljaar.valueOf(2008), new BigDecimal("205.00"));
		BBL_OF_DEELTIJD_BOL_NIVO_3_4.put(Schooljaar.valueOf(2008), new BigDecimal("499.00"));
		VAVO_PRIJS_PER_45_MINUTEN.put(Schooljaar.valueOf(2008), new BigDecimal("0.64"));

		// bedragen 2009/2010
		BBL_OF_DEELTIJD_BOL_NIVO_1_2.put(Schooljaar.valueOf(2009), new BigDecimal("210.00"));
		BBL_OF_DEELTIJD_BOL_NIVO_3_4.put(Schooljaar.valueOf(2009), new BigDecimal("511.00"));
		VAVO_PRIJS_PER_45_MINUTEN.put(Schooljaar.valueOf(2009), new BigDecimal("0.65"));

		// bedragen 2010/2011
		BBL_OF_DEELTIJD_BOL_NIVO_1_2.put(Schooljaar.valueOf(2010), new BigDecimal("213.00"));
		BBL_OF_DEELTIJD_BOL_NIVO_3_4.put(Schooljaar.valueOf(2010), new BigDecimal("517.00"));
		VAVO_PRIJS_PER_45_MINUTEN.put(Schooljaar.valueOf(2010), new BigDecimal("0.67"));
	}

	public static boolean isVoltijdBOL(Verbintenis verbintenis)
	{
		Opleiding opleiding = verbintenis.getOpleiding();
		if (opleiding != null)
		{
			MBOLeerweg leerweg = opleiding.getLeerweg();

			return ((MBOLeerweg.BOL.equals(leerweg) && Intensiteit.Voltijd.equals(verbintenis
				.getIntensiteit())));
		}
		return false;
	}

	public static boolean isBBLofDeeltijdBOL(Verbintenis verbintenis)
	{
		Opleiding opleiding = verbintenis.getOpleiding();
		if (opleiding != null)
		{
			MBOLeerweg leerweg = opleiding.getLeerweg();

			return (MBOLeerweg.BBL.equals(leerweg) || (MBOLeerweg.BOL.equals(leerweg) && Intensiteit.Deeltijd
				.equals(verbintenis.getIntensiteit())));
		}
		return false;
	}

	/**
	 * Berekent het bedrag dat in rekening gebracht moet worden na aftrek van gemiste
	 * maanden aan het begin of het einde van de verbintenis.
	 */
	public static BigDecimal verrekenGemisteMaanden(Schooljaar schooljaar, Verbintenis verbintenis,
			BigDecimal bedrag)
	{
		if (verbintenis.getEinddatum() != null
			&& verbintenis.getEinddatum().before(schooljaar.getEenOktober()))
			return null;

		BigDecimal result = bedrag;
		result = result.subtract(getKortingBegin(schooljaar, verbintenis, bedrag));
		result = result.subtract(getKortingEind(schooljaar, verbintenis, bedrag));

		return result;
	}

	/**
	 * Berekent de korting die men krijgt als men later in het schooljaar begint. Dit is
	 * 1/12 van het cursusbedrag voor iedere gemiste maand (na 1 augustus), mits de
	 * begindatum na 1 november van het schooljaar ligt.
	 */
	public static BigDecimal getKortingBegin(Schooljaar schooljaar, Verbintenis verbintenis,
			BigDecimal bedrag)
	{
		BigDecimal kortingBegin = new BigDecimal("0.00");
		Date eenNovember = TimeUtil.getInstance().addMonths(schooljaar.getBegindatum(), 3);
		if (!verbintenis.getBegindatum().before(eenNovember))
		{
			int maandenGemist = 0;
			int beginMaand = TimeUtil.getInstance().getMonth(verbintenis.getBegindatum());
			if (beginMaand >= Calendar.AUGUST)
				maandenGemist = beginMaand - Calendar.AUGUST;
			else
				maandenGemist = 5 + beginMaand; // januari = 6e maand

			kortingBegin =
				bedrag.multiply(new BigDecimal(maandenGemist).divide(new BigDecimal(12), 5,
					BigDecimal.ROUND_HALF_UP));
		}
		return kortingBegin;
	}

	/**
	 * Berekent de korting waar men recht op heeft als men eerder in het schooljaar slaagt
	 * voor de opleiding. Dit is 1/10 voor iedere gemiste maand (voor 1 juni).
	 */
	public static BigDecimal getKortingEind(Schooljaar schooljaar, Verbintenis verbintenis,
			BigDecimal bedrag)
	{
		BigDecimal kortingEind = new BigDecimal("0.00");
		if (verbintenis.getEinddatum() != null
			&& verbintenis.getEinddatum().before(schooljaar.getEinddatum())
			&& verbintenis.getRedenUitschrijving() != null
			&& verbintenis.getRedenUitschrijving().isGeslaagd())
		{
			int maandenGemist = 0;
			int eindMaand = TimeUtil.getInstance().getMonth(verbintenis.getEinddatum());
			if (eindMaand >= Calendar.JANUARY && eindMaand < Calendar.MAY)
				maandenGemist = Calendar.MAY - eindMaand;
			else if (eindMaand >= Calendar.AUGUST)
				maandenGemist = 16 - eindMaand;
			if (maandenGemist > 0)
			{
				kortingEind =
					bedrag.multiply(new BigDecimal(maandenGemist).divide(new BigDecimal(10)));
			}
		}
		return kortingEind;
	}

	/**
	 * @return Het jaarbedrag dat bij deze opleiding hoort. Eventuele korting ivm gemiste
	 *         maanden moet nog verrekend worden. Retourneert null indien voor deze
	 *         verbintenis geen wettelijk cursusgeld hoeft te worden berekend of kan
	 *         worden berekend.
	 */
	public static BigDecimal getOpleidingBedrag(Verbintenis verbintenis, Schooljaar schooljaar)
	{
		BigDecimal result = null;
		Opleiding opleiding = verbintenis.getOpleiding();
		if (opleiding != null)
		{
			if (isBBLofDeeltijdBOL(verbintenis))
			{
				MBONiveau niveau = opleiding.getNiveau();
				if (MBONiveau.Niveau1.equals(niveau) || MBONiveau.Niveau2.equals(niveau))
				{
					result = BBL_OF_DEELTIJD_BOL_NIVO_1_2.get(schooljaar);
				}
				if (MBONiveau.Niveau3.equals(niveau) || MBONiveau.Niveau4.equals(niveau))
				{
					result = BBL_OF_DEELTIJD_BOL_NIVO_3_4.get(schooljaar);
				}
			}
			// VAVO bedragen ahv uurbedrag en contacturen per week
			if (opleiding.isVavo() && verbintenis.getContacturenPerWeek() != null)
			{
				// 40 weken per jaar, per 60 minuten (=4/3 * 45 min)
				BigDecimal bedrag = VAVO_PRIJS_PER_45_MINUTEN.get(schooljaar);
				if (bedrag != null)
				{
					final int WEKEN_PER_JAAR = 40;
					final int MINUTEN_PER_UUR = 60;
					result =
						bedrag.multiply(
							new BigDecimal(WEKEN_PER_JAAR * MINUTEN_PER_UUR).multiply(verbintenis
								.getContacturenPerWeek())).divide(new BigDecimal(45),
							BigDecimal.ROUND_HALF_UP);
				}
			}
		}

		return result;
	}
}
