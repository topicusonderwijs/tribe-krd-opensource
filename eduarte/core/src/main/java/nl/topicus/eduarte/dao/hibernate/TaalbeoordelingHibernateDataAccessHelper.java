package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.TaalbeoordelingDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalbeoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalscore;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalscoreNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheid;
import nl.topicus.eduarte.zoekfilters.TaalbeoordelingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TaalbeoordelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Taalbeoordeling, TaalbeoordelingZoekFilter> implements
		TaalbeoordelingDataAccessHelper
{

	public TaalbeoordelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaalbeoordelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(Beoordeling.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("medewerker", filter.getMedewerker());
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("uitstroom", filter.getUitstroom());
		builder.addEquals("taal", filter.getTaal());

		return criteria;
	}

	@Override
	public boolean isCohortInGebruik(Cohort cohort)
	{
		Criteria criteria = createCriteria(Taalbeoordeling.class);
		criteria.createAlias("uitstroom", "uitstroom");
		criteria.createAlias("uitstroom.dossier", "dossier");
		criteria.add(Restrictions.eq("dossier.cohort", cohort));
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public boolean isTaalInGebruik(ModerneTaal taal, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Taalbeoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taal", taal);
		builder.addEquals("deelnemer", deelnemer);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public List<Taalbeoordeling> getTaalbeoordelingen(Deelnemer deelnemer, ModerneTaal taal)
	{
		Criteria criteria = createCriteria(Taalbeoordeling.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		criteria.add(Restrictions.eq("taal", taal));
		criteria.addOrder(Order.desc("datum"));
		return cachedList(criteria);
	}

	@Override
	public List<Taalscore> getTaalscoresOrdered(TaalscoreNiveauVerzameling verzameling)
	{
		Criteria criteria = createCriteria(Taalscore.class);
		criteria.add(Restrictions.eq("taalscoreNiveauVerzameling", verzameling));
		criteria.createAlias("taalvaardigheid", "taalvaardigheid");
		criteria.addOrder(Order.asc("taalvaardigheid.titel"));
		return cachedList(criteria);
	}

	@Override
	public Taalbeoordeling getNieuwsteTaalbeoordeling(Deelnemer deelnemer, ModerneTaal taal)
	{
		Criteria criteria = createCriteria(Taalbeoordeling.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		criteria.add(Restrictions.eq("taal", taal));
		criteria.addOrder(Order.desc("datum"));
		criteria.setMaxResults(1);
		return cachedUnique(criteria);
	}

	@Override
	public Taalscore getTaalscore(Taalvaardigheid vaardigheid,
			TaalscoreNiveauVerzameling verzameling)
	{
		Criteria criteria = createCriteria(Taalscore.class);
		criteria.add(Restrictions.eq("taalvaardigheid", vaardigheid));
		criteria.add(Restrictions.eq("taalscoreNiveauVerzameling", verzameling));
		return cachedUnique(criteria);
	}

	@Override
	public boolean bestaatBeoordeling(List<Long> deelnemerIDs)
	{
		Criteria criteria = createCriteria(TaalscoreNiveauVerzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("deelnemer.id", deelnemerIDs);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}
}
