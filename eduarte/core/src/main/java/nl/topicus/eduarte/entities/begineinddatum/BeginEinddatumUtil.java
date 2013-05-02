package nl.topicus.eduarte.entities.begineinddatum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;

/**
 * Class met enkele handige methodes die door de verschillende begin-einddatum entititen
 * gebruikt kan worden.
 * 
 * @author loite
 */
public class BeginEinddatumUtil
{

	/**
	 * @param entiteit
	 * @param peildatum
	 * @return true indien de gegeven entiteit actief is op de gegeven peildatum, of als
	 *         de peildatum null is.
	 */
	public static boolean isActief(IBeginEinddatumEntiteit entiteit, Date peildatum)
	{
		return peildatum == null
			|| TimeUtil.getInstance().dateBetweenOrBeginEndIsNull(entiteit.getBegindatum(),
				entiteit.getEinddatum(), peildatum);
	}

	/**
	 * @param <T>
	 *            type van de entiteit met een begin en einddatum.
	 * @param lijst
	 *            de lijst die gefilterd moet worden op actieve elementen
	 * @param peildatum
	 *            de peildatum waarop gefilterd moet worden
	 * @return een lijst met alle elementen uit de lijst die actief zijn op de peildatum
	 */
	public static <T extends IBeginEinddatumEntiteit> List<T> getElementenOpPeildatum(
			List<T> lijst, Date peildatum)
	{
		List<T> resultaat = new ArrayList<T>();
		for (T element : lijst)
		{
			if (element.isActief(peildatum))
			{
				resultaat.add(element);
			}
		}
		return resultaat;
	}

	/**
	 * @param <T>
	 *            type van de entiteit met een begin en einddatum.
	 * @param lijst
	 *            de lijst waarin gezocht moet worden naar een actief element
	 * @param peildatum
	 *            de peildatum waarop gefilterd moet worden
	 * @return het element dat actief is op de gegeven peildatum
	 */
	public static <T extends IBeginEinddatumEntiteit> T getElementOpPeildatum(List<T> lijst,
			Date peildatum)
	{
		for (T element : lijst)
		{
			if (element.isActief(peildatum))
			{
				return element;
			}
		}
		return null;
	}

	public static boolean isActiefTijdens(IBeginEinddatumEntiteit entiteit, Date begindatum,
			Date einddatum)
	{
		return TimeUtil.getInstance().isOverlapping(entiteit.getBegindatum(),
			entiteit.getEinddatum(), begindatum, einddatum);
	}
}
