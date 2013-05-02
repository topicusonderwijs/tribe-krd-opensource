package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.SoortProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.zoekfilters.SoortProductregelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class SoortProductregelHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<SoortProductregel, SoortProductregelZoekFilter>
		implements SoortProductregelDataAccessHelper
{
	public SoortProductregelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<SoortProductregel> list(Taxonomie taxonomie)
	{
		Asserts.assertNotNull("taxonomie", taxonomie);
		Criteria criteria = createCriteria(SoortProductregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taxonomie", taxonomie);
		builder.addEquals("actief", Boolean.TRUE);
		criteria.addOrder(Order.asc("volgnummer"));

		return cachedTypedList(criteria);
	}

	@Override
	protected Criteria createCriteria(SoortProductregelZoekFilter filter)
	{
		Criteria criteria = createCriteria(SoortProductregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("taxonomie", "taxonomie");
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		builder.addEquals("volgnummer", filter.getVolgnummer());
		builder.addEquals("naam", filter.getNaam());
		builder.addEquals("actief", filter.isActief());
		builder.addEquals("taxonomie", filter.getTaxonomie());
		return criteria;
	}

	@Override
	public boolean isSoortProductregelInGebruik(SoortProductregel soortProductregel)
	{
		return soortProductregel.isInGebruik();
	}

	@Override
	public int getVolgendeVolgnummer()
	{
		Criteria criteria = createCriteria(SoortProductregel.class);
		criteria.addOrder(Order.desc("volgnummer"));
		List<SoortProductregel> regels = cachedTypedList(criteria);
		if (!regels.isEmpty())
			return regels.get(0).getVolgnummer() + 1;
		return 0;
	}

	@Override
	public SoortProductregel get(Taxonomie taxonomie, String naam)
	{
		Asserts.assertNotNull("taxonomie", taxonomie);
		Asserts.assertNotEmpty("naam", naam);
		Criteria criteria = createCriteria(SoortProductregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("taxonomie", taxonomie);
		builder.addEquals("naam", naam);
		builder.addEquals("actief", Boolean.TRUE);

		return cachedTypedUnique(criteria);
	}
}
