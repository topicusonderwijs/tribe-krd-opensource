package nl.topicus.eduarte.dao.hibernate;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ProductregelHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Productregel, ProductregelZoekFilter> implements
		ProductregelDataAccessHelper
{
	public ProductregelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ProductregelZoekFilter filter)
	{
		Criteria criteria = createCriteria(Productregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("soortProductregel", "soortProductregel");
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		builder.addEquals("cohort", filter.getCohort());
		if (filter.getOpleiding() != null && filter.getVerbintenisgebied() == null)
		{
			Criterion opleidingEqual = Restrictions.eq("opleiding", filter.getOpleiding());
			if (filter.getOpleiding() instanceof Opleidingsvariant)
			{
				opleidingEqual =
					Restrictions.or(
						opleidingEqual,
						Restrictions.eq("opleiding",
							((Opleidingsvariant) filter.getOpleiding()).getParent()));
			}

			// opleiding==filteropleiding or opleiding is null and
			// verbintenisgebied==filteropleiding.verbintenisgebied.
			if (filter.getOpleiding().isNegeerLandelijkeProductregels())
			{
				criteria.add(opleidingEqual);
			}
			else
			{

				criteria.add(Restrictions.or(opleidingEqual, Restrictions.and(Restrictions
					.isNull("opleiding"), Restrictions.eq("verbintenisgebied", filter
					.getOpleiding().getVerbintenisgebied()))));
			}
		}
		else if (filter.getVerbintenisgebied() != null && filter.getOpleiding() == null)
		{
			// Haal alle landelijke productregels op.
			builder.addEquals("verbintenisgebied", filter.getVerbintenisgebied());
			builder.addIsNull("opleiding", Boolean.TRUE);
			builder.addIsNull("organisatie", Boolean.TRUE);
		}
		else
		{
			throw new IllegalArgumentException(
				"Op het filter moet een van opleiding of verbintenisgebied ingesteld zijn.");
		}
		builder.addEquals("typeProductregel", filter.getTypeProductregel());

		return criteria;
	}

	@Override
	public boolean isUnique(IdObject object, Map<String, Object> properties, Opleiding opleiding)
	{
		Criteria crit = createCriteria(Hibernate.getClass(object));
		crit.add(Restrictions.or(
			Restrictions.eq("opleiding", opleiding),
			Restrictions.and(Restrictions.isNull("opleiding"),
				Restrictions.eq("verbintenisgebied", opleiding.getVerbintenisgebied()))));
		CriteriaBuilder builder = new CriteriaBuilder(crit);
		for (String key : properties.keySet())
		{
			Object value = properties.get(key);
			builder.addEquals(key, value);

			if (key.contains("."))
			{
				String associationPath = key.substring(0, key.indexOf("."));
				builder.createAlias(associationPath, associationPath);
			}
		}
		if (object.isSaved())
			builder.addNotEquals("id", object.getIdAsSerializable());
		crit.setProjection(Projections.rowCount());
		return (Long) cachedUnique(crit) == 0;
	}

	@Override
	public Productregel getProductRegel(String afkorting, Opleiding opleiding, Cohort cohort)
	{
		Asserts.assertNotNull("afkorting", afkorting);
		Asserts.assertNotNull("opleiding", opleiding);
		Asserts.assertNotNull("cohort", cohort);
		Criteria criteria = createCriteria(Productregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("afkorting", afkorting);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("cohort", cohort);
		return cachedUnique(criteria);
	}

	@Override
	public List<Productregel> getProductRegels(Opleiding opleiding)
	{
		Criteria criteria = createCriteria(Productregel.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opleiding", opleiding);
		return cachedList(criteria);
	}
}
