package nl.topicus.eduarte.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.models.OrganisatieEenheidLocatieKoppeling;
import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.apache.wicket.model.IModel;

public final class OrganisatieEenheidLocatieUtil
{

	private OrganisatieEenheidLocatieUtil()
	{
	}

	/**
	 * @return Een lijst met de organisatie-eenheden waar de medewerker per vandaag aan
	 *         gekoppeld is.
	 */
	public static List<OrganisatieEenheid> getActieveOrganisatieEenheden(
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > entiteit)
	{
		return getActieveOrganisatieEenheden(entiteit.getOrganisatieEenheidLocatieKoppelingen(),
			null);
	}

	public static List<OrganisatieEenheid> getActieveOrganisatieEenheden(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> koppelList,
			Locatie locatie)
	{
		Date currentDate = TimeUtil.getInstance().currentDate();
		Set<OrganisatieEenheid> result = new LinkedHashSet<OrganisatieEenheid>(koppelList.size());
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > koppeling : koppelList)
		{
			if (koppeling.isActief(currentDate))
			{
				if (locatie == null)
					addToSet(result, koppeling.getOrganisatieEenheid(), koppeling
						.getOrganisatieEenheid().getActieveChildren(
							EduArteContext.get().getPeildatumOfVandaag()));
				else
				{
					if (koppeling.getLocatie() == null || koppeling.getLocatie().equals(locatie))
					{
						for (OrganisatieEenheid curEenheid : koppeling.getOrganisatieEenheid()
							.getActieveChildren(EduArteContext.get().getPeildatumOfVandaag()))
						{
							if (curEenheid.isVerbondenAanLocatie(locatie, EduArteContext.get()
								.getPeildatumOfVandaag()))
							{
								addToSet(result, curEenheid, null);
							}
						}
					}
				}
			}
		}
		filterInactief(result);
		return new ArrayList<OrganisatieEenheid>(result);
	}

	/**
	 * @return Een lijst met de locaties die gekoppeld zijn aan de gegeven
	 *         organisatie-eenheid en waaraan de medewerker is gekoppeld
	 */
	public static List<Locatie> getActieveLocaties(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> koppelList,
			OrganisatieEenheid organisatieEenheid)
	{
		Date currentDate = TimeUtil.getInstance().currentDate();
		Set<Locatie> result = new LinkedHashSet<Locatie>();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > koppeling : koppelList)
		{
			if (koppeling.isActief(currentDate))
			{
				if (organisatieEenheid == null
					|| koppeling.getOrganisatieEenheid().isParentOf(organisatieEenheid))
				{
					if (koppeling.getLocatie() == null)
					{
						List<Locatie> locatiesToAdd = new ArrayList<Locatie>();
						if (organisatieEenheid == null)
						{
							for (OrganisatieEenheidLocatie curLocatie : koppeling
								.getOrganisatieEenheid().getActieveLocatiesVanParentsEnChildren(
									EduArteContext.get().getPeildatumOfVandaag()))
								locatiesToAdd.add(curLocatie.getLocatie());
						}
						else
						{
							for (OrganisatieEenheidLocatie curLocatie : organisatieEenheid
								.getActieveLocaties(EduArteContext.get().getPeildatumOfVandaag()))
								locatiesToAdd.add(curLocatie.getLocatie());
						}
						addToSet(result, null, locatiesToAdd);
					}
					else
						addToSet(result, koppeling.getLocatie(), null);
				}
			}
		}
		filterInactief(result);
		return new ArrayList<Locatie>(result);
	}

	private static <T> void addToSet(Set<T> set, T first, Collection<T> others)
	{
		if (first != null && !set.contains(first))
			set.add(first);
		if (others != null)
		{
			List<T> tmp = new ArrayList<T>(others);
			tmp.removeAll(set);
			set.addAll(tmp);
		}
	}

	private static void filterInactief(Set< ? extends IBeginEinddatumEntiteit> result)
	{
		Iterator< ? extends IBeginEinddatumEntiteit> it = result.iterator();
		while (it.hasNext())
			if (!it.next().isActief(EduArteContext.get().getPeildatumOfVandaag()))
				it.remove();
	}

	public static <T extends OrganisatieEenheidLocatieProvider> List<T> filterOrganisatieEenheidLocatie(
			List<T> inList,
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> filterEntiteitList)
	{
		List<T> ret = new ArrayList<T>();
		for (T entiteitKoppeling : inList)
		{
			if (hoortBij(filterEntiteitList, entiteitKoppeling))
				ret.add(entiteitKoppeling);
		}
		return ret;
	}

	public static boolean gelijkeOrganisatieEenheidLocatie(
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > account,
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > entiteit)
	{
		List<OrganisatieEenheidLocatieKoppeling> flatList =
			flatten(account.getOrganisatieEenheidLocatieKoppelingen());
		return gelijkeOrganisatieEenheidLocatie(flatList, entiteit);
	}

	public static boolean gelijkeOrganisatieEenheidLocatie(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> checkList,
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > entiteit)
	{
		List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> entiteitList =
			entiteit.getOrganisatieEenheidLocatieKoppelingen();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > entiteitKoppeling : entiteitList)
		{
			if (hoortBij(checkList, entiteitKoppeling))
				return true;
		}
		return false;
	}

	/**
	 * Hoort de account bij een OrganisatieEenheid-locatie.
	 * 
	 * @param entiteitKoppeling
	 *            de OrganisatieEenheid-locatie
	 * @return true als de account bij de OrganisatieEenheid-locatie hoort
	 */
	public static boolean hoortBij(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> checkList,
			OrganisatieEenheidLocatieProvider entiteitKoppeling)
	{
		if (entiteitKoppeling.getOrganisatieEenheid() == null)
			return false;

		Date currentDate = TimeUtil.getInstance().currentDate();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > checkKoppeling : checkList)
		{
			if (!checkKoppeling.isActief(currentDate))
				continue;

			if (checkKoppeling.getOrganisatieEenheid().isParentOf(
				entiteitKoppeling.getOrganisatieEenheid()))
			{
				if (checkKoppeling.getLocatie() == null || entiteitKoppeling.getLocatie() == null
					|| checkKoppeling.getLocatie().equals(entiteitKoppeling.getLocatie()))
					return true;
			}
		}
		return false;
	}

	public static List<OrganisatieEenheidLocatieKoppeling> flatten(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> checklist)
	{
		Date currentDate = TimeUtil.getInstance().currentDate();
		Date peilDate = EduArteContext.get().getPeildatum();
		List<OrganisatieEenheidLocatieKoppeling> ret =
			new ArrayList<OrganisatieEenheidLocatieKoppeling>();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > curKoppeling : checklist)
		{
			if (!curKoppeling.isActief(currentDate) || curKoppeling.getOrganisatieEenheid() == null)
				continue;

			for (OrganisatieEenheid curEenheid : curKoppeling.getOrganisatieEenheid()
				.getActieveChildren(peilDate))
			{
				if (curKoppeling.getLocatie() == null)
				{
					List<OrganisatieEenheidLocatie> locaties =
						curEenheid.getActieveLocaties(peilDate);
					if (locaties.isEmpty())
						ret.add(new OrganisatieEenheidLocatieKoppeling(curEenheid, null));
					else
					{
						for (OrganisatieEenheidLocatie curLocatie : locaties)
							ret.add(new OrganisatieEenheidLocatieKoppeling(curEenheid, curLocatie
								.getLocatie()));
					}
				}
				else
					ret.add(new OrganisatieEenheidLocatieKoppeling(curEenheid, curKoppeling
						.getLocatie()));
			}
		}
		return ret;
	}

	public static boolean isGekoppeldAanParent(
			List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> checklist,
			OrganisatieEenheidLocatieProvider check)
	{
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > curKoppeling : checklist)
		{
			if (check.getOrganisatieEenheid().isParentOf(curKoppeling.getOrganisatieEenheid()))
			{
				if (check.getLocatie() == null || curKoppeling.getLocatie() == null
					|| check.getLocatie().equals(curKoppeling.getLocatie()))
					return true;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >> getFilterKoppelingen(
			IModel filterEntiteitModel)
	{
		if (filterEntiteitModel == null || filterEntiteitModel.getObject() == null)
			return null;
		if (filterEntiteitModel.getObject() instanceof IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >)
		{
			return ((IOrganisatieEenheidLocatieKoppelbaarEntiteit) filterEntiteitModel.getObject())
				.getOrganisatieEenheidLocatieKoppelingen();
		}
		if (filterEntiteitModel.getObject() instanceof OrganisatieEenheidLocatieProvider)
		{
			List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >> ret =
				new ArrayList<IOrganisatieEenheidLocatieKoppelEntiteit< ? >>();
			ret.add(new OrganisatieEenheidLocatieKoppeling(
				(OrganisatieEenheidLocatieProvider) filterEntiteitModel.getObject()));
			return ret;
		}
		throw new IllegalStateException(filterEntiteitModel.getObject().getClass().getName()
			+ " moet IOrganisatieEenheidLocatieKoppelbaarEntiteit"
			+ " of OrganisatieEenheidLocatieProvider implementeren");
	}
}
