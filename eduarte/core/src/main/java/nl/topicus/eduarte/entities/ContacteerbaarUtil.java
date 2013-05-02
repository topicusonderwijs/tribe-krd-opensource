package nl.topicus.eduarte.entities;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;

/**
 * Class met enkele handige methodes die door de verschillende {@link Adresseerbaar}
 * entititen gebruikt kan worden.
 * 
 * @author hoeve
 */
public class ContacteerbaarUtil
{
	/**
	 * @param soort
	 * @return alle contactgegevens van deze {@link Contacteerbaar} entiteit van de
	 *         gegeven soort.
	 */
	public static <T extends IContactgegevenEntiteit> List<T> getContactgegevens(
			Contacteerbaar<T> contacteerbaar, SoortContactgegeven soort)
	{
		List<T> res = new ArrayList<T>(2);
		for (T tlf : contacteerbaar.getContactgegevens())
			if (tlf.getSoortContactgegeven().equals(soort))
				res.add(tlf);

		return res;
	}

	/**
	 * Geeft het eerste telefoonnummer. De volgorde wordt bepaald op basis van de volgorde
	 * die aangegeven.
	 * 
	 * @return Het eerste telefoonnummer, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getEersteTelefoon(
			Contacteerbaar<T> contacteerbaar)
	{
		return getSpecifiekContactgegeven(contacteerbaar, TypeContactgegeven.Telefoon);
	}

	/**
	 * Geeft het eerste homepage. De volgorde wordt bepaald op basis van de volgorde die
	 * aangegeven.
	 * 
	 * @return Het eerste homepage, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getEersteHomepage(
			Contacteerbaar<T> contacteerbaar)
	{
		return getSpecifiekContactgegeven(contacteerbaar, TypeContactgegeven.Homepage);
	}

	/**
	 * Geeft het eerste mobieltelefoon. De volgorde wordt bepaald op basis van de volgorde
	 * die aangegeven.
	 * 
	 * @return Het eerste mobieltelefoon, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getEersteMobieltelefoon(
			Contacteerbaar<T> contacteerbaar)
	{
		return getSpecifiekContactgegeven(contacteerbaar, TypeContactgegeven.Mobieltelefoon);
	}

	/**
	 * Geeft het eerste e-mailadres. De volgorde wordt bepaald op basis van de volgorde
	 * die aangegeven.
	 * 
	 * @return Het eerste e-mailadres, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getEersteEmailAdres(
			Contacteerbaar<T> contacteerbaar)
	{
		return getSpecifiekContactgegeven(contacteerbaar, TypeContactgegeven.Email);
	}

	/**
	 * Geeft het eerste overig. De volgorde wordt bepaald op basis van de volgorde die
	 * aangegeven.
	 * 
	 * @return Het eerste overig, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getEersteOverig(
			Contacteerbaar<T> contacteerbaar)
	{
		return getSpecifiekContactgegeven(contacteerbaar, TypeContactgegeven.Overig);
	}

	/**
	 * Geeft het contactgegeven terug als de soortcontactgegeven overeenkomen
	 * 
	 * @return PersoonContactgegeven, of null indien niets wordt gevonden.
	 */
	public static <T extends IContactgegevenEntiteit> T getContactGegevenMetCode(
			Contacteerbaar<T> contacteerbaar, String code)
	{
		return getSpecifiekContactgegevenMetCode(contacteerbaar, code);
	}

	/**
	 * @param type
	 * @return alle contactgegevens van deze {@link Contacteerbaar} enteiteit van de
	 *         gegeven soort.
	 */
	private static <T extends IContactgegevenEntiteit> T getSpecifiekContactgegeven(
			Contacteerbaar<T> contacteerbaar, TypeContactgegeven type)
	{
		if (contacteerbaar.getContactgegevens() != null)
		{
			for (T pt : contacteerbaar.getContactgegevens())
			{
				if (pt.getSoortContactgegeven().getTypeContactgegeven().equals(type))
				{
					return pt;
				}
			}
		}
		return null;
	}

	/**
	 * @param code
	 * @return alle contactgegevens van deze {@link Contacteerbaar} enteiteit van de
	 *         gegeven code.
	 */

	private static <T extends IContactgegevenEntiteit> T getSpecifiekContactgegevenMetCode(
			Contacteerbaar<T> contacteerbaar, String code)
	{
		if (contacteerbaar.getContactgegevens() != null)
		{
			for (T pt : contacteerbaar.getContactgegevens())
			{
				if (pt.getSoortContactgegeven().getCode().equals(code))
				{
					return pt;
				}
			}
		}
		return null;
	}
}
