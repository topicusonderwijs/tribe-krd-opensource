package nl.topicus.eduarte.entities.adres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.topicus.eduarte.entities.adres.filter.AdresCompositeFilter;
import nl.topicus.eduarte.entities.adres.filter.AdresFilter;
import nl.topicus.eduarte.entities.adres.filter.FactuuradresFilter;
import nl.topicus.eduarte.entities.adres.filter.FysiekadresFilter;
import nl.topicus.eduarte.entities.adres.filter.PeildatumAdresFilter;
import nl.topicus.eduarte.entities.adres.filter.PostadresFilter;

/**
 * Class met enkele handige methodes die door de verschillende {@link Adresseerbaar}
 * entititen gebruikt kan worden.
 * 
 * @author hoeve
 */
public class AdresseerbaarUtil
{
	public static <T extends AdresEntiteit<T>> List<T> sortAdressenOpBegindatum(List<T> adressen)
	{
		List<T> ret = new ArrayList<T>(adressen);
		Collections.sort(ret, new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				return o1.getBegindatum().compareTo(o2.getBegindatum());
			}
		});
		return ret;
	}

	public static <T extends AdresEntiteit<T>> List<T> getAdressen(Adresseerbaar<T> adresseerbaar,
			AdresFilter filter)
	{
		List<T> ret = new LinkedList<T>(adresseerbaar.getAdressen());
		Iterator<T> it = ret.iterator();
		while (it.hasNext())
			if (!filter.matches(it.next()))
				it.remove();
		return Collections.unmodifiableList(ret);
	}

	public static <T extends AdresEntiteit<T>> T getEersteAdres(Adresseerbaar<T> adresseerbaar,
			AdresFilter filter)
	{
		if (adresseerbaar.getAdressen() == null)
		{
			return null;
		}
		for (T curAdres : adresseerbaar.getAdressen())
		{
			if (filter.matches(curAdres))
				return curAdres;
		}
		return null;
	}

	public static AdresFilter createPeildatumFilter()
	{
		return new PeildatumAdresFilter();
	}

	public static AdresFilter createFysiekadresFilter()

	{
		return new FysiekadresFilter();
	}

	public static AdresFilter createPostadresFilter()
	{
		return new PostadresFilter();
	}

	public static AdresFilter createFactuuradresFilter()
	{
		return new FactuuradresFilter();
	}

	public static AdresFilter createPostadresOpPeildatumFilter()
	{
		return new AdresCompositeFilter(new PostadresFilter(), new PeildatumAdresFilter());
	}

	public static AdresFilter createFysiekadresOpPeildatumFilter()
	{
		return new AdresCompositeFilter(new FysiekadresFilter(), new PeildatumAdresFilter());
	}

	public static AdresFilter createFactuuradresOpPeildatumFilter()
	{
		return new AdresCompositeFilter(new FactuuradresFilter(), new PeildatumAdresFilter());
	}

	public static AdresFilter createPostadresOpPeildatumFilter(Date peildatum)
	{
		return new AdresCompositeFilter(new PostadresFilter(), new PeildatumAdresFilter(peildatum));
	}

	public static AdresFilter createFysiekadresOpPeildatumFilter(Date peildatum)
	{
		return new AdresCompositeFilter(new FysiekadresFilter(),
			new PeildatumAdresFilter(peildatum));
	}

	public static AdresFilter createFactuuradresOpPeildatumFilter(Date peildatum)
	{
		return new AdresCompositeFilter(new FactuuradresFilter(), new PeildatumAdresFilter(
			peildatum));
	}
}
