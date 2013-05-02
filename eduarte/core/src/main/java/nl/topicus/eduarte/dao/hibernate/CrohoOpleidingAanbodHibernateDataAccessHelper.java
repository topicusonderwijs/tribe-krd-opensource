package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.CrohoOpleidingAanbodDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;
import nl.topicus.eduarte.zoekfilters.CrohoOpleidingAanbodZoekFilter;

import org.hibernate.Criteria;

public class CrohoOpleidingAanbodHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<CrohoOpleidingAanbod, CrohoOpleidingAanbodZoekFilter>
		implements CrohoOpleidingAanbodDataAccessHelper
{

	public CrohoOpleidingAanbodHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(CrohoOpleidingAanbodZoekFilter filter)
	{
		Criteria criteria = createCriteria(CrohoOpleidingAanbod.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("brin", filter.getBrin());
		builder.addEquals("opleidingsvorm", filter.getOpleidingsvorm());
		builder.addEquals("crohoOpleiding", filter.getCrohoOpleiding());

		return criteria;
	}

	@Override
	public List<CrohoOpleidingAanbod> getAanbod(CrohoOpleiding crohoOpleiding, Brin brin)
	{
		Asserts.assertNotNull("crohoOpleiding", crohoOpleiding);
		Asserts.assertNotNull("brin", brin);

		Criteria criteria = createCriteria(CrohoOpleidingAanbod.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("crohoOpleiding", crohoOpleiding);
		builder.addEquals("brin", brin);

		return cachedTypedList(criteria);
	}

	@Override
	public CrohoOpleidingAanbod getAanbod(CrohoOpleiding crohoOpleiding, Brin brin,
			OpleidingsVorm opleidingsvorm)
	{
		Asserts.assertNotNull("crohoOpleiding", crohoOpleiding);
		Asserts.assertNotNull("brin", brin);
		Asserts.assertNotNull("opleidingsvorm", opleidingsvorm);

		Criteria criteria = createCriteria(CrohoOpleidingAanbod.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("crohoOpleiding", crohoOpleiding);
		builder.addEquals("brin", brin);
		builder.addEquals("opleidingsvorm", opleidingsvorm);

		return cachedTypedUnique(criteria);
	}
}
