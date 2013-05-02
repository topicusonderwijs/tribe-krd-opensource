package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.InschrijvingsverzoekDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Inschrijvingsverzoek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.zoekfilters.InschrijvingsverzoekZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

public class InschrijvingsverzoekHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Inschrijvingsverzoek, InschrijvingsverzoekZoekFilter>
		implements InschrijvingsverzoekDataAccessHelper
{
	public InschrijvingsverzoekHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(InschrijvingsverzoekZoekFilter filter)
	{
		Criteria criteria = createCriteria(Inschrijvingsverzoek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("verbintenis", "verbintenis");
		builder.createAlias("verbintenis.deelnemer", "student");
		builder.createAlias("student.persoon", "persoon");
		// builder.createAlias("verbintenis", "verbintenis",
		// JoinFragment.LEFT_OUTER_JOIN);

		if (!filter.addOrganisatieEenheidLocatieCriteria("verbintenis", criteria))
			return null;

		// TODO Paul Kan alleen als er een verbintenis is
		// builder.addGreaterOrEquals("verbintenis.einddatumNotNull",
		// filter.getPeildatum());

		if (StringUtil.isNotEmpty(filter.getOfficieelofaanspreek()))
		{
			// officiele achternaam of aanspreekachternaam
			List<Criterion> whereList = new ArrayList<Criterion>();
			whereList.add(builder.createILike("persoon.officieleAchternaam", filter
				.getOfficieelofaanspreek(), MatchMode.START));
			whereList.add(builder.createILike("persoon.achternaam", filter
				.getOfficieelofaanspreek(), MatchMode.START));
			builder.addOrs(whereList);
		}

		builder.addEquals("persoon.geboortedatum", filter.getGeboortedatum());

		if (filter.getOpleiding() != null)
		{
			builder.addEquals("verbintenis.opleiding", filter.getOpleiding());
		}

		if (filter.getDeelnemer() != null)
		{
			builder.addEquals("verbintenis.deelnemer", filter.getDeelnemer());
		}

		builder.addEquals("status", filter.getInschrijvingsverzoekStatus());

		/* uitgebreid zoeken */

		builder.addEquals("lotingStatus", filter.getLotingStatus());
		builder.addEquals("lotingStatusDatum", filter.getLotingStatusDatum());
		builder.addEquals("verbintenis.organisatieEenheid", filter.getOrganisatieEenheid());
		builder.addEquals("verbintenis.opleiding", filter.getOpleiding());
		builder.addEquals("eersteJaars", filter.getEersteJaars());
		builder.addEquals("instroommoment.code", filter.getInstroommomentCode());

		DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).addCriteria(builder,
			filter.getDeelnemerZoekFilter(), "student", "persoon");

		return criteria;
	}

	@Override
	public Inschrijvingsverzoek get(Verbintenis verbintenis, int studiejaar,
			String instroommomentCode)
	{
		Criteria criteria = createCriteria(Inschrijvingsverzoek.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("verbintenis", "verbintenis");
		builder.createAlias("plaatsing", "plaatsing");
		builder.createAlias("instroommoment", "instroommoment");

		builder.addEquals("verbintenis", verbintenis);
		builder.addEquals("plaatsing.leerjaar", studiejaar);
		builder.addEquals("instroommoment.code", instroommomentCode);

		return cachedTypedUnique(criteria);
	}

}
