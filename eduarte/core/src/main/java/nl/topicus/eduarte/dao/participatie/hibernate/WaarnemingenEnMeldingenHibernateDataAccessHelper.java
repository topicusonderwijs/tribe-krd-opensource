package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.WaarnemingenEnMeldingenDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class WaarnemingenEnMeldingenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Verbintenis, VerbintenisZoekFilter> implements
		WaarnemingenEnMeldingenDataAccessHelper
{
	public WaarnemingenEnMeldingenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(VerbintenisZoekFilter abstractFilter)
	{
		Criteria criteria = createCriteria(Verbintenis.class);
		criteria.createAlias("deelnemer", "deelnemer");
		criteria.createAlias("deelnemer.persoon", "persoon");

		DetachedCriteria dcAbsentieMelding = createDetachedCriteria(AbsentieMelding.class);
		dcAbsentieMelding.setProjection(Projections.property("deelnemer"));
		DetachedCriteriaBuilder dcBuilderAbsentieMelding =
			new DetachedCriteriaBuilder(dcAbsentieMelding);
		dcBuilderAbsentieMelding.addEquals("afgehandeld", Boolean.FALSE);

		DetachedCriteria dcWaarneming = createDetachedCriteria(Waarneming.class);
		dcWaarneming.setProjection(Projections.property("deelnemer"));
		DetachedCriteriaBuilder dcBuilderWaarneming = new DetachedCriteriaBuilder(dcWaarneming);
		dcBuilderWaarneming.addEquals("afgehandeld", Boolean.FALSE);
		dcBuilderWaarneming.addEquals("waarnemingSoort", WaarnemingSoort.Afwezig);

		criteria.add(Restrictions.or(Subqueries.propertyIn("deelnemer", dcWaarneming),
			Subqueries.propertyIn("deelnemer", dcAbsentieMelding)));

		return criteria;
	}
}
