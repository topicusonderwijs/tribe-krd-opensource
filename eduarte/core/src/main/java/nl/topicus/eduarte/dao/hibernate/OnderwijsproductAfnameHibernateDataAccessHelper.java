package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class OnderwijsproductAfnameHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<OnderwijsproductAfname, OnderwijsproductAfnameZoekFilter>
		implements OnderwijsproductAfnameDataAccessHelper
{
	public OnderwijsproductAfnameHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OnderwijsproductAfnameZoekFilter filter)
	{
		Criteria criteria = createCriteria(OnderwijsproductAfname.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("onderwijsproduct", "onderwijsproduct");
		builder.createAlias("onderwijsproduct.soortProduct", "soortProduct");
		builder.createAlias("deelnemer", "deelnemer");
		builder.addEquals("cohort", filter.getCohort());
		builder.addEquals("onderwijsproduct.soortProduct", filter.getSoortOnderwijsproduct());
		builder.addILikeCheckWildcard("onderwijsproduct.code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("onderwijsproduct.titel", filter.getTitel(), MatchMode.START);
		builder.addEquals("soortProduct.summatief", filter.getSummatief());
		builder.addEquals("onderwijsproduct", filter.getOnderwijsproduct());
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("vrijstellingType", filter.getVrijstellingType());
		if (filter.getActief() != null)
		{
			if (filter.getActief())
			{
				builder.addLessOrEquals("begindatum", filter.getPeilEindDatum());
				builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
			}
			else
			{
				builder.addOrs(Restrictions.lt("einddatumNotNull", filter.getPeildatum()),
					Restrictions.gt("begindatum", filter.getPeilEindDatum()));
			}
		}
		if (filter.getBeeindigdeProductafnames() != null)
		{
			if (!filter.getBeeindigdeProductafnames())
				builder.addGreaterThan("einddatumNotNull", filter.getPeilEindDatum());
			else
			{
				builder.addGreaterThan("einddatumNotNull", filter.getPeildatum());
				builder.addLessOrEquals("einddatumNotNull", filter.getPeilEindDatum());
			}
		}

		if (filter.getDeelnemerFilter() != null)
		{
			DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).addCriteria(builder,
				filter.getDeelnemerFilter(), "deelnemer", "persoon");
		}
		return criteria;
	}

	@Override
	public List<OnderwijsproductAfname> getAfgenomenOnderwijsproductenNietGekoppeldAanVerbintenis(
			Deelnemer deelnemer, Verbintenis verbintenis)
	{
		Criteria criteria = createCriteria(OnderwijsproductAfname.class);
		criteria.createAlias("afnameContexten", "context", JoinFragment.LEFT_OUTER_JOIN);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addNullOrNotEquals("context.verbintenis", verbintenis);

		return cachedTypedList(criteria);
	}
}
