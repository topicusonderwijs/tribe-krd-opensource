/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.TaalDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalkeuze;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheidseis;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalvaardigheidseisLlb;
import nl.topicus.eduarte.zoekfilters.TaalZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class TaalHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<ModerneTaal, TaalZoekFilter> implements
		TaalDataAccessHelper
{

	public TaalHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaalZoekFilter filter)
	{
		Criteria criteria = createCriteria(ModerneTaal.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("afkorting", filter.getAfkorting(), MatchMode.START);
		builder.addILikeCheckWildcard("omschrijving", filter.getOmschrijving(), MatchMode.ANYWHERE);
		builder.addEquals("type", filter.getType());
		builder.addEquals("voorgedefinieerd", filter.getVoorgedefinieerd());
		builder.addNotIn("id", filter.getTalenUitgezonderd());

		return criteria;
	}

	@Override
	public List<Taalvaardigheidseis> getTaalvaardigheidseisen()
	{
		Criteria criteria = createCriteria(Taalvaardigheidseis.class);
		return cachedList(criteria);
	}

	@Override
	public List<Taalvaardigheid> getTaalvaardigheden()
	{
		Criteria criteria = createCriteria(Taalvaardigheid.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		List<String> uitzonderingen = new ArrayList<String>(5);
		uitzonderingen.add("Getallen");
		uitzonderingen.add("RuimteEnVorm");
		uitzonderingen.add("Gegevensverwerking");
		uitzonderingen.add("Verbanden");

		builder.addNotIn("titel", uitzonderingen);

		return cachedList(criteria);
	}

	@Override
	public Taalvaardigheid getTaalvaardigheid(String titel)
	{
		Criteria criteria = createCriteria(Taalvaardigheid.class);
		criteria.add(Restrictions.eq("titel", titel));
		return cachedUnique(criteria);
	}

	@Override
	public List<Taalvaardigheidseis> getTaalvaardigheidsEisen(CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(Taalvaardigheidseis.class);
		DetachedCriteria sub = createDetachedCriteria(TaalvaardigheidseisLlb.class);
		sub.setProjection(Projections.id());
		sub.add(Restrictions.eq("mboNiveau", matrix.getNiveau()));
		criteria.add(Restrictions.or(Restrictions.eq("uitstroom", matrix),
			Subqueries.propertyIn("id", sub)));
		criteria.createAlias("taalType", "taalType");
		criteria.addOrder(Order.asc("taalType.titel"));

		return cachedList(criteria);
	}

	@Override
	public Map<TaalType, List<Taalvaardigheidseis>> getTaalvaardigheidseisenClustered(
			CompetentieMatrix matrix)
	{
		Map<TaalType, List<Taalvaardigheidseis>> ret =
			new HashMap<TaalType, List<Taalvaardigheidseis>>();
		if (matrix == null)
		{
			return ret;
		}

		Criteria criteria = createCriteria(Taalvaardigheidseis.class);
		DetachedCriteria sub = createDetachedCriteria(TaalvaardigheidseisLlb.class);
		sub.setProjection(Projections.id());
		sub.add(Restrictions.eq("mboNiveau", matrix.getNiveau()));
		criteria.add(Restrictions.or(Restrictions.eq("uitstroom", matrix),
			Subqueries.propertyIn("id", sub)));
		criteria.createAlias("taalType", "taalType");
		criteria.addOrder(Order.asc("taalType.titel"));

		List<Taalvaardigheidseis> eisen = cachedList(criteria);

		for (Taalvaardigheidseis eis : eisen)
		{
			if (ret.containsKey(eis.getTaalType()))
			{
				ret.get(eis.getTaalType()).add(eis);
			}
			else
			{
				List<Taalvaardigheidseis> eisenCluster = new ArrayList<Taalvaardigheidseis>();
				eisenCluster.add(eis);
				ret.put(eis.getTaalType(), eisenCluster);
			}
		}
		return ret;
	}

	@Override
	public TaalvaardigheidseisLlb getTaalvaardigheidseisLlb(TaalType taalType, MBONiveau niveau)
	{
		Criteria criteria = createCriteria(TaalvaardigheidseisLlb.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taalType", taalType);
		builder.addEquals("mboNiveau", niveau);
		return cachedUnique(criteria);
	}

	@Override
	public List<TaalType> getTaalTypes()
	{
		Criteria criteria = createCriteria(TaalType.class);
		criteria.addOrder(Order.asc("titel"));
		return cachedList(criteria);
	}

	@Override
	public TaalType getTaalType(String titel)
	{
		Criteria criteria = createCriteria(TaalType.class);
		criteria.add(Restrictions.eq("titel", titel));
		return cachedUnique(criteria);
	}

	@Override
	public Boolean isTaalInGebruik(ModerneTaal taal)
	{
		Criteria criteria = createCriteria(Taalkeuze.class);
		criteria.add(Restrictions.eq("taal", taal));
		criteria.setProjection(Projections.rowCount());
		return ((Long) cachedUnique(criteria) > 0);
	}

	@Override
	public List<ModerneTaal> getTalen(Verbintenis verbintenis)
	{
		Criteria criteria = createCriteria(ModerneTaal.class);
		DetachedCriteria dc = createDetachedCriteria(Taalkeuze.class);
		dc.add(Restrictions.eq("verbintenis", verbintenis));
		dc.setProjection(Projections.property("taal"));

		criteria.add(Subqueries.propertyIn("id", dc));
		return uncachedList(criteria);
	}
}
