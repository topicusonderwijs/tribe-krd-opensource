package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieWaarnemingenDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;

public class AbsentieWaarnemingenHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Waarneming, WaarnemingZoekFilter> implements
		AbsentieWaarnemingenDataAccessHelper
{
	public AbsentieWaarnemingenHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(WaarnemingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Waarneming.class);
		criteria.createAlias("deelnemer", "deelnemer");
		criteria.createAlias("deelnemer.persoon", "persoon");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addBetween("beginDatumTijd",
			TimeUtil.getInstance().maakBeginVanDagVanDatum(filter.getBeginDatumTijd()), TimeUtil
				.getInstance().maakEindeVanDagVanDatum(filter.getBeginDatumTijd()));

		builder.addBetween("eindDatumTijd",
			TimeUtil.getInstance().maakBeginVanDagVanDatum(filter.getEindDatumTijd()), TimeUtil
				.getInstance().maakEindeVanDagVanDatum(filter.getEindDatumTijd()));

		List<WaarnemingSoort> soortList = new ArrayList<WaarnemingSoort>();
		soortList.add(WaarnemingSoort.Afwezig);
		soortList.add(WaarnemingSoort.DeelsAfwezig);
		builder.addIn("waarnemingSoort", soortList);

		builder.addEquals("afgehandeld", Boolean.FALSE);

		builder.addILikeCheckWildcard("persoon.officieleAchternaam", filter.getAchternaam(),
			MatchMode.START);

		DetachedCriteria dcVerbintenis = createDetachedCriteria(Verbintenis.class);
		dcVerbintenis.setProjection(Projections.property("deelnemer"));
		DetachedCriteriaBuilder dcBuilderVerbintenis = new DetachedCriteriaBuilder(dcVerbintenis);

		dcBuilderVerbintenis.addEquals("locatie", filter.getLocatie());
		dcBuilderVerbintenis.addEquals("organisatieEenheid", filter.getOrganisatieEenheid());
		dcBuilderVerbintenis.addEquals("opleiding", filter.getOpleiding());

		DetachedCriteria dcGroep = createDetachedCriteria(Groepsdeelname.class);
		dcGroep.setProjection(Projections.property("deelnemer"));
		DetachedCriteriaBuilder dcBuilderGroep = new DetachedCriteriaBuilder(dcGroep);

		dcBuilderGroep.addEquals("groep", filter.getGroep());

		criteria.add(Subqueries.propertyIn("deelnemer", dcGroep));

		criteria.add(Subqueries.propertyIn("deelnemer", dcVerbintenis));
		return criteria;
	}
}
