package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.LesuurIndelingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * @author loite
 * @TODO - Nick Deze DAH fixen
 */
public class LesuurIndelingHibernateDataAccessHelper extends
		HibernateDataAccessHelper<LesuurIndeling> implements LesuurIndelingDataAccessHelper
{

	/**
	 * @param provider
	 */
	public LesuurIndelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<LesuurIndeling> getAlleLesuurIndelingen()
	{

		return getLesuurIndelingen(null, EduArteContext.get()
			.getFirstOrInstellingOrganisatieEenheid());
	}

	@Override
	public List<LesuurIndeling> getLesuurIndelingen(OrganisatieEenheid orgEenheid)
	{
		return getLesuurIndelingen(null, orgEenheid);
	}

	@Override
	public List<LesuurIndeling> getLesuurIndelingen(Locatie locatie, OrganisatieEenheid orgEenheid)
	{
		Asserts.assertNotNull("OrganisatieEenheid", orgEenheid);
		List<LesweekIndeling> lesweken =
			DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).list();
		Set<LesuurIndeling> ret = new TreeSet<LesuurIndeling>(new Comparator<LesuurIndeling>()
		{
			@Override
			public int compare(LesuurIndeling o1, LesuurIndeling o2)
			{
				int diff = o1.getId().compareTo(o2.getId());
				if (diff != 0)
					return diff;
				return (int) (o1.getId() - o2.getId());
			}
		});
		for (LesweekIndeling curLesweek : lesweken)
		{
			for (LesdagIndeling curLesdag : curLesweek.getLesdagIndelingen())
			{
				for (LesuurIndeling les : curLesdag.getLesuurIndeling())
					if (!ret.contains(les))
					{
						ret.add(les);
					}
			}
		}
		List<LesuurIndeling> retList = new ArrayList<LesuurIndeling>(ret);
		DataAccessRegistry.getInstance();

		LesweekIndeling defaultLesweek = null;
		if (locatie != null && orgEenheid != null)
		{
			LesweekindelingZoekFilter filter = getLesweekIndelingFilter(locatie, orgEenheid);
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));
			defaultLesweek =
				DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class)
					.getlesweekIndeling(filter);
		}
		// else
		// defaultLesweek = orgEenheid.getLesweekindeling();

		if (defaultLesweek != null)
		{
			Set<LesuurIndeling> defaultWeek = new LinkedHashSet<LesuurIndeling>();
			for (LesdagIndeling curLesdag : defaultLesweek.getLesdagIndelingenOrderedByDay())
			{
				List<LesuurIndeling> lesuren = curLesdag.getLesuurIndeling();
				for (LesuurIndeling les : lesuren)
				{
					retList.remove(les);
					if (!defaultWeek.contains(les))
						defaultWeek.add(les);
				}
			}
			retList.addAll(0, defaultWeek);
		}
		return retList;
	}

	private LesweekindelingZoekFilter getLesweekIndelingFilter(Locatie locatie,
			OrganisatieEenheid orgEenheid)
	{
		LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.setLocatie(locatie);
		filter.setOrganisatieEenheid(orgEenheid);
		return filter;
	}

	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur)
	{
		return getLesTijd(deelnemer, datum, lesuur, null, EduArteContext.get()
			.getDefaultOrganisatieEenheid());
	}

	@Override
	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur,
			OrganisatieEenheid orgEenheid)
	{
		return getLesTijd(deelnemer, datum, lesuur, null, orgEenheid);
	}

	@Override
	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur, Locatie locatie,
			OrganisatieEenheid orgEenheid)
	{
		Asserts.assertNotNull("Deelnemer", deelnemer);
		Asserts.assertNotNull("datum", datum);
		Asserts.assertNotNull("uur", lesuur);
		Asserts.assertNotNull("OrganisatieEenheid", orgEenheid);

		LesweekIndeling lesweekIndeling = null;
		LesweekindelingZoekFilter filter = getLesweekIndelingFilter(locatie, orgEenheid);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));

		lesweekIndeling =
			DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).getlesweekIndeling(
				filter);

		if (lesweekIndeling != null)
		{
			List<LesdagIndeling> lesdagen =
				new ArrayList<LesdagIndeling>(lesweekIndeling.getLesdagIndelingen());
			for (LesdagIndeling lesdag : lesdagen)
			{
				if (lesdag.valtOpDatum(datum))
				{
					for (LesuurIndeling lestijd : lesdag.getLesuurIndeling())
					{
						if (lestijd.getLesuur() == lesuur)
							return lestijd;
					}

				}
			}
		}
		return null;
	}
}
