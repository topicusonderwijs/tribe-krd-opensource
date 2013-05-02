package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.participatie.helpers.VerzuimmeldingDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums.Verzuimsoort;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

public class VerzuimmeldingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<IbgVerzuimmelding, DeelnemerVerzuimloketZoekfilter>
		implements VerzuimmeldingDataAccessHelper
{
	public VerzuimmeldingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Functie get(Integer meldingsnummer, Date vanafDatum, Date tmDatum, String status,
			Verzuimsoort verzuimsoort)
	{
		return null;
	}

	@Override
	protected Criteria createCriteria(DeelnemerVerzuimloketZoekfilter filter)
	{
		Criteria criteria = createCriteria(IbgVerzuimmelding.class);

		addCriteria(criteria, filter);

		return criteria;
	}

	public void addCriteria(Criteria criteria, DeelnemerVerzuimloketZoekfilter filter)
	{
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (filter.getVerbintenis() != null)
		{

			builder.addEquals("verbintenis", filter.getVerbintenis());
		}
		else
		{
			builder.createAlias("verbintenis", "verbintenis");
			builder.createAlias("verbintenis.deelnemer", "deelnemer");
			builder.addEquals("deelnemer.deelnemernummer", filter.getLeerlingnummer());
			builder.createAlias("deelnemer.persoon", "persoon");
			builder.addILikeCheckWildcard("persoon.achternaam", filter.getNaam(), MatchMode.START);
		}
		builder.addGreaterOrEquals("begindatum", filter.getVanafDatum());
		builder.addNullOrLessOrEquals("einddatum", filter.getTmDatum());

		builder.addEquals("meldingsnummer", filter.getMeldingsnummer());
		builder.addEquals("status", filter.getStatus());
		builder.addEquals("verzuimsoort", filter.getVerzuimsoort());
	}
}
