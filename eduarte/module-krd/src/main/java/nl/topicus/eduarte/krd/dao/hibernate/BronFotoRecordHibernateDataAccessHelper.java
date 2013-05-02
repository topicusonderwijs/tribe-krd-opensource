package nl.topicus.eduarte.krd.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.krd.dao.helpers.BronFotoRecordDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.IBronFotoOnderwijsontvangendeRecord;
import nl.topicus.eduarte.krd.zoekfilters.BronFotoRecordZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

public class BronFotoRecordHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<BronFotoRecord, BronFotoRecordZoekFilter> implements
		BronFotoRecordDataAccessHelper
{
	public BronFotoRecordHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BronFotoRecordZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter.getClazz());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("bestand", filter.getFotobestand());
		return criteria;
	}

	@Override
	public List<BronFotoRecord> getAlleBronFotoRecords(BronFotobestand fotobestand)
	{
		Asserts.assertNotNull("fotobestand", fotobestand);
		Criteria criteria = createCriteria(BronFotoRecord.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("bestand", fotobestand);
		return cachedTypedList(criteria);
	}

	@Override
	public List<Long> listIds(BronFotoRecordZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter);
		criteria.setProjection(Projections.property("id"));
		return list(criteria, filter.isResultCacheable());
	}

	@Override
	public IBronFotoOnderwijsontvangendeRecord getOnderwijsontvangendeRecord(
			BronFotobestand fotobestand, Long pgn,
			Class< ? extends IBronFotoOnderwijsontvangendeRecord> clazz)
	{
		Asserts.assertNotNull("fotobestand", fotobestand);
		Asserts.assertNotNull("pgn", pgn);
		Asserts.assertNotNull("clazz", clazz);
		Criteria criteria = createCriteria(clazz);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("bestand", fotobestand);
		builder.addEquals("pgn", pgn);

		return (IBronFotoOnderwijsontvangendeRecord) uncachedUnique(criteria);
	}

}
