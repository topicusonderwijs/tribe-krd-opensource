package nl.topicus.eduarte.krd.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

public class MutatieLogVerwerkersLogHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<MutatieLogVerwerkersLog, MutatieLogVerwerkersLogZoekFilter>
		implements MutatieLogVerwerkersLogDataAccessHelper
{
	public MutatieLogVerwerkersLogHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MutatieLogVerwerkersLogZoekFilter filter)
	{
		Criteria criteria = createCriteria(MutatieLogVerwerkersLog.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.isActief());
		builder.addEquals("organisatie", filter.getOrganisatie());
		builder.addEquals("verwerker", filter.getVerwerker());
		return builder.getCriteria();
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getQueueCount(MutatieLogVerwerkersLog mutatieLogVerwerkersLog)
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT count(distinct m.modified_id) AS aantalInWachtrij ");
		buffer.append("FROM mutatielog m ");
		buffer
			.append("INNER JOIN mutatielogverwerkerstabellen mlvt ON m.modified_table = mlvt.table_name ");
		buffer.append("INNER JOIN mutatielogverwerkers mlv ON mlv.ID = mlvt.verwerker ");
		buffer
			.append("INNER JOIN mutatielogverwerkerslog mlvl ON mlvl.organisatie = m.organisatie AND mlvl.verwerker = mlv.ID AND m.ID > mlvl.laatst_verwerkt_id ");
		buffer.append("WHERE mlvl.actief = :actief ");
		buffer.append("AND mlvl.organisatie = :organisatie ");
		buffer.append("AND mlvl.verwerker = :verwerker ");

		SQLQuery query = createSQLQuery(buffer.toString());
		query.setParameter("actief", mutatieLogVerwerkersLog.isActief());
		query.setParameter("organisatie", mutatieLogVerwerkersLog.getOrganisatie().getId());
		query.setParameter("verwerker", mutatieLogVerwerkersLog.getVerwerker().getId());
		query.addScalar("aantalInWachtrij", Hibernate.INTEGER);
		Integer count = (Integer) uncachedResult(query);

		return count != null ? (int) count : 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public long getLaatstTeVerwerkenMutatieId(MutatieLogVerwerkersLog mutatieLogVerwerkersLog)
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT max(m.id) AS maxId ");
		buffer.append("FROM mutatielog m ");
		buffer
			.append("INNER JOIN mutatielogverwerkerstabellen mlvt ON m.modified_table = mlvt.table_name ");
		buffer.append("INNER JOIN mutatielogverwerkers mlv ON mlv.ID = mlvt.verwerker ");
		buffer
			.append("INNER JOIN mutatielogverwerkerslog mlvl ON mlvl.organisatie = m.organisatie AND mlvl.verwerker = mlv.ID ");
		buffer.append("WHERE mlvl.actief = :actief ");
		buffer.append("AND mlvl.organisatie = :organisatie ");
		buffer.append("AND mlvl.verwerker = :verwerker ");

		SQLQuery query = createSQLQuery(buffer.toString());
		query.setParameter("actief", mutatieLogVerwerkersLog.isActief());
		query.setParameter("organisatie", mutatieLogVerwerkersLog.getOrganisatie().getId());
		query.setParameter("verwerker", mutatieLogVerwerkersLog.getVerwerker().getId());
		query.addScalar("maxId", Hibernate.LONG);
		Long maxId = (Long) uncachedResult(query);

		return maxId != null ? (long) maxId : 0;
	}
}
