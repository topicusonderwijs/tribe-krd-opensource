package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.GroepsdeelnameDataAccessHelper;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinFragment;

public class GroepsdeelnameHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Groepsdeelname, GroepsdeelnameZoekFilter> implements
		GroepsdeelnameDataAccessHelper
{
	public GroepsdeelnameHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(GroepsdeelnameZoekFilter filter)
	{
		Criteria criteria = createCriteria(Groepsdeelname.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("groep", "groep", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("groep.groepstype", "groepstype", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("groep.organisatieEenheid", "organisatieEenheid",
			JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("groep.locatie", "locatie", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("deelnemer", "deelnemer");
		builder.createAlias("deelnemer.persoon", "persoon");

		if (!filter.getGroepFilter().addOrganisatieEenheidLocatieCriteria("groep", criteria))
			return null;
		builder.addEquals("groep.organisatie", EduArteContext.get().getInstelling());
		builder.addEquals("deelnemer.organisatie", EduArteContext.get().getInstelling());
		builder.addEquals("persoon.organisatie", EduArteContext.get().getInstelling());
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("groep", filter.getGroep());
		builder.addEquals("groep.groepstype", filter.getGroepstype());
		if (!filter.isToonToekomstigeDeelnames())
			builder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		if (filter.getBeeindigdeGroepsdeelnames() != null)
		{
			if (!filter.getBeeindigdeGroepsdeelnames())
				builder.addGreaterThan("einddatumNotNull", filter.getPeilEindDatum());
			else
			{
				builder.addGreaterThan("einddatumNotNull", filter.getPeildatum());
				builder.addLessOrEquals("einddatumNotNull", filter.getPeilEindDatum());
			}
		}

		GroepZoekFilter groepfilter = filter.getGroepFilter();
		builder.addILikeCheckWildcard("groep.code", groepfilter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("groep.naam", groepfilter.getNaam(), MatchMode.START);
		builder.addEquals("groep.groepstype", groepfilter.getType());
		if (!filter.isToonToekomstigeDeelnames())
			builder.addLessOrEquals("groep.begindatum", groepfilter.getPeildatum());
		builder.addGreaterOrEquals("groep.einddatumNotNull", groepfilter.getPeildatum());
		builder.addEquals("groepstype.plaatsingsgroep", groepfilter.getPlaatsingsgroep());

		groepfilter.addQuickSearchCriteria(builder, "groep.code", "groep.naam");

		if (groepfilter.getMentorOrDocent() != null)
		{
			/*
			 * Hier voegen we de zoektocht naar de groepen die de gegeven medewerker als
			 * docent of mentor hebben.
			 */
			DetachedCriteria dcritMentor = createDetachedCriteria(GroepMentor.class);
			DetachedCriteriaBuilder dcritMentorBuilder = new DetachedCriteriaBuilder(dcritMentor);
			dcritMentorBuilder.addEquals("medewerker", groepfilter.getMentorOrDocent());
			if (!filter.isToonToekomstigeDeelnames())
				dcritMentorBuilder.addLessOrEquals("begindatum", groepfilter.getPeildatum());
			dcritMentorBuilder.addGreaterOrEquals("einddatumNotNull", groepfilter.getPeildatum());
			dcritMentor.setProjection(Projections.property("groep"));

			DetachedCriteria dcritDocent = createDetachedCriteria(GroepDocent.class);
			DetachedCriteriaBuilder dcritDocentBuilder = new DetachedCriteriaBuilder(dcritDocent);
			dcritDocentBuilder.addEquals("medewerker", groepfilter.getMentorOrDocent());
			if (!filter.isToonToekomstigeDeelnames())
				dcritDocentBuilder.addLessOrEquals("begindatum", groepfilter.getPeildatum());
			dcritDocentBuilder.addGreaterOrEquals("einddatumNotNull", groepfilter.getPeildatum());
			dcritDocent.setProjection(Projections.property("groep"));

			builder.addOrs(Subqueries.propertyIn("groep", dcritDocent), Subqueries.propertyIn(
				"groep", dcritMentor));
		}
		if (groepfilter.getSnelZoekenString() != null)
		{
			builder.addOrs(Restrictions.eq("groep.code", groepfilter.getSnelZoekenString())
				.ignoreCase(), Restrictions.ilike("groep.naam", groepfilter.getSnelZoekenString(),
				MatchMode.ANYWHERE));
		}
		DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).addCriteria(builder,
			filter.getDeelnemerFilter(), "deelnemer", "persoon");

		return criteria;
	}
}
