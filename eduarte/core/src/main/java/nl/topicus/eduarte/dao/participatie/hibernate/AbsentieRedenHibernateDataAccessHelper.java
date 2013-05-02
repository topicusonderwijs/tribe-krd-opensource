package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class AbsentieRedenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<AbsentieReden, AbsentieRedenZoekFilter> implements
		AbsentieRedenDataAccessHelper
{
	public AbsentieRedenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(AbsentieRedenZoekFilter filter)
	{
		Criteria criteria = createCriteria(AbsentieReden.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (!filter.addOrganisatieEenheidLocatieCriteria(criteria))
			return null;

		builder.addEquals("actief", filter.getActief());
		builder.addEquals("absentieSoort", filter.getAbsentieSoort());
		if (filter.isAlleenToegestaanVoorDeelnemers())
			builder
				.addEquals("toegestaanVoorDeelnemers", filter.isAlleenToegestaanVoorDeelnemers());
		if (filter.getOrganisatieEenheid() != null)
			builder.addNullOrIn("organisatieEenheid", filter.getGeselecteerdeOrganisatieEenheden());

		return criteria;
	}

	@Override
	public List<AbsentieReden> getTonenBijWaarnemingen(AbsentieRedenZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("tonenBijWaarnemingen", true);
		builder.addEquals("actief", true);

		return cachedTypedList(criteria);
	}

	@Override
	public boolean bestaatEnActief(AbsentieReden absentieReden)
	{
		Asserts.assertNotNull("absentieReden.instelling", absentieReden.getOrganisatie());
		Criteria criteria = createCriteria(AbsentieReden.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("organisatie", absentieReden.getOrganisatie());
		builder.addEquals("actief", Boolean.TRUE);
		criteria.add(Restrictions.or(Restrictions.eq("afkorting", absentieReden.getAfkorting()),
			Restrictions.eq("omschrijving", absentieReden.getOmschrijving())));

		OrganisatieEenheid organisatieEenheid = absentieReden.getOrganisatieEenheid();
		if (organisatieEenheid != null)
		{
			builder.addNullOrIn("organisatieEenheid", organisatieEenheid
				.getParentsEnChildren(EduArteContext.get().getPeildatumOfVandaag()));
		}
		List<AbsentieReden> absentieRedenList = cachedList(criteria);
		for (AbsentieReden reden : absentieRedenList)
		{
			if (!reden.equals(absentieReden))
				return true;
		}
		return false;
	}

	@Override
	public AbsentieReden get(String afkorting)
	{
		Asserts.assertNotEmpty("afkorting", afkorting);
		Criteria criteria = createCriteria(AbsentieReden.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afkorting", afkorting);

		return cachedTypedUnique(criteria);
	}
}
