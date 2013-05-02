package nl.topicus.eduarte.krd.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

public class BronExamenverzamelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BronExamenverzameling, BronExamenverzamelingZoekFilter>
		implements BronExamenverzamenlingDataAccessHelper
{
	public BronExamenverzamelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronExamenverzamelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronExamenverzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("aanleverpunt", filter.getBronAanleverpunt());
		builder.addEquals("schooljaar", filter.getSchooljaar());
		builder.addEquals("bronOnderwijssoort", filter.getBronOnderwijssoort());
		return criteria;
	}

	@Override
	public long getAantalExVerzGereed(BronExamenverzamelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(BronExamenverzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("aanleverpunt", filter.getBronAanleverpunt());
		builder.addEquals("schooljaar", filter.getSchooljaar());
		builder.addEquals("bronOnderwijssoort", filter.getBronOnderwijssoort());
		builder.addIsNull("bveBatch", true);
		builder.addIsNull("voBatch", true);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria);
	}

	@Override
	public BronExamenverzameling getLaatstAangemaakteVerzameling()
	{
		Session session = getSessionProvider().getSession();
		SQLQuery query =
			session.createSQLQuery("select max(id) " + "from BRON_EXAMENVERZAMELINGEN "
				+ "where organisatie = :organisatie");
		Instelling instelling = EduArteContext.get().getInstelling();
		Asserts.assertNotNull("instelling", instelling);
		query.setEntity("organisatie", instelling);
		BigDecimal id = (BigDecimal) query.uniqueResult();
		if (id == null)
			return null;
		return get(BronExamenverzameling.class, id.longValue());
	}

	@Override
	public List<BronExamenverzameling> getExVerzGekoppeldAanBatch(IBronBatch bronBatch)
	{
		Criteria criteria = createCriteria(BronExamenverzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		if (bronBatch instanceof BronBatchBVE)
			builder.addEquals("bveBatch", bronBatch);
		else
			builder.addEquals("voBatch", bronBatch);
		return cachedTypedList(criteria);
	}
}
