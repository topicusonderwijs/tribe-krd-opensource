/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.*;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IHierarchischeOrganisatieEenheidEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.entities.settings.ManierVanAanmelden;
import nl.topicus.eduarte.entities.settings.ManierVanAanmeldenSetting;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class OrganisatieEenheidHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<OrganisatieEenheid, OrganisatieEenheidZoekFilter>
		implements OrganisatieEenheidDataAccessHelper
{
	public OrganisatieEenheidHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OrganisatieEenheidZoekFilter filter)
	{
		Criteria criteria = createCriteria(OrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("afkorting", filter.getAfkorting(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.START);
		builder.addEquals("actief", filter.getActief());
		if (filter.getTonenBijDigitaalAanmelden() != null)
		{
			builder.addEquals("tonenBijDigitaalAanmelden", filter.getTonenBijDigitaalAanmelden());
			DetachedCriteria dcCriteria = createDetachedCriteria(ManierVanAanmeldenSetting.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dcCriteria);
			dcBuilder.addEquals("value", ManierVanAanmelden.NietGebruiken);
			dcCriteria.setProjection(Projections.property("organisatieEenheid"));
			if (filter.getTonenBijDigitaalAanmelden())
				criteria.add(Subqueries.propertyNotIn("id", dcCriteria));
			else
				criteria.add(Subqueries.propertyIn("id", dcCriteria));
		}

		builder.addLessOrEquals("begindatum", filter.getPeildatum());
		builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());

		if (filter.getOnlyChildrenOf() != null)
		{
			List<Long> childrenIds = new ArrayList<Long>();
			for (OrganisatieEenheid oe : getChildren((filter.getOnlyChildrenOf()), EduArteContext
				.get().getPeildatum()))
				childrenIds.add(oe.getId());
			builder.addIn("id", childrenIds);
		}
		if (filter.getOrderByList().size() == 0 && !filter.isUnique())
			criteria.addOrder(Order.asc("naam"));

		return criteria;
	}

	@Override
	public List<OrganisatieEenheid> list()
	{
		OrganisatieEenheidZoekFilter filter = new OrganisatieEenheidZoekFilter();
		filter.addOrderByProperty("id");
		filter.addOrderByProperty("naam");
		return list(filter);
	}

	@Override
	public OrganisatieEenheid getRootOrganisatieEenheid()
	{
		OrganisatieEenheidZoekFilter filter = new OrganisatieEenheidZoekFilter();
		filter.setUnique(true);
		Criteria criteria = createCriteria(filter);
		criteria.add(Restrictions.isNull("parent"));
		return cachedTypedUnique(criteria);
	}

	@Override
	public SoortOrganisatieEenheid getSoortOrganisatieEenheid(String code)
	{
		Asserts.assertNotEmpty("code", code);
		Criteria criteria = createCriteria(SoortOrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return cachedUnique(criteria);
	}

	// TODO dit alles in de database doen, kost duur!
	@Override
	public OrganisatieEenheid get(Long id)
	{
		return get(OrganisatieEenheid.class, id);
	}

	private Map<OrganisatieEenheid, Set<OrganisatieEenheid>> getOrganisatieEenheden()
	{
		Query query =
			createQuery(
				"select parent, child from OrganisatieEenheid child inner join child.parent as parent",
				OrganisatieEenheid.class, "parent", "child");
		List<Object[]> list = cachedList(query);
		Map<OrganisatieEenheid, Set<OrganisatieEenheid>> map =
			new HashMap<OrganisatieEenheid, Set<OrganisatieEenheid>>(list.size());
		OrganisatieEenheid temp = null;
		Set<OrganisatieEenheid> set = null;
		for (Object[] array : list)
		{
			temp = (OrganisatieEenheid) array[0];
			set = map.get(temp);
			if (set == null)
			{
				set = new HashSet<OrganisatieEenheid>();
				map.put(temp, set);
			}
			set.add((OrganisatieEenheid) array[1]);
		}
		return map;
	}

	@Override
	public List<OrganisatieEenheid> getChildren(OrganisatieEenheid organisatieEenheid,
			Date peildatum)
	{
		if (organisatieEenheid == null)
			return Collections.emptyList();
		Map<OrganisatieEenheid, Set<OrganisatieEenheid>> map = getOrganisatieEenheden();
		List<OrganisatieEenheid> list = new ArrayList<OrganisatieEenheid>();
		traverce(list, organisatieEenheid, map, peildatum);
		return list;
	}

	@Override
	public List<OrganisatieEenheid> getDirectChildren(OrganisatieEenheid organisatieEenheid,
			Date peildatum)
	{
		Criteria criteria = createCriteria(OrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("parent", organisatieEenheid);
		builder.addLessOrEquals("begindatum", peildatum);
		builder.addGreaterOrEquals("einddatumNotNull", peildatum);
		criteria.addOrder(Order.asc("naam"));
		return cachedList(criteria);
	}

	/**
	 * Voegt current en al zijn (klein)kinderen toe aan target.
	 * 
	 * @param target
	 * @param current
	 * @param children
	 */
	private void traverce(List<OrganisatieEenheid> target, OrganisatieEenheid current,
			Map<OrganisatieEenheid, Set<OrganisatieEenheid>> children, Date peildatum)
	{
		if (target.contains(current))
			return;
		if (peildatum == null || current.isActief(peildatum))
			target.add(current);
		Set<OrganisatieEenheid> childs = children.get(current);
		if (childs == null || childs.isEmpty())
			return;
		for (OrganisatieEenheid child : childs)
			traverce(target, child, children, peildatum);
	}

	@Override
	public List<OrganisatieEenheid> getAncestors(OrganisatieEenheid organisatieEenheid)
	{
		LinkedList<OrganisatieEenheid> ret = new LinkedList<OrganisatieEenheid>();
		ret.add(organisatieEenheid);
		OrganisatieEenheid current = organisatieEenheid;
		while (current.getParent() != null)
		{
			current = current.getParent();
			ret.addFirst(current);
		}
		return ret;
	}

	@Override
	public void removeOverrides(
			List< ? extends IHierarchischeOrganisatieEenheidEntiteit> entiteiten,
			OrganisatieEenheid organisatieEenheid)
	{
		List<OrganisatieEenheid> oEenheden = getAncestors(organisatieEenheid);
		Map<OrganisatieEenheid, Set<String>> names = new HashMap<OrganisatieEenheid, Set<String>>();
		for (OrganisatieEenheid curEenheid : oEenheden)
		{
			names.put(curEenheid, new HashSet<String>());
		}
		for (IHierarchischeOrganisatieEenheidEntiteit curActiviteit : entiteiten)
		{
			names.get(curActiviteit.getOrganisatieEenheid()).add(
				curActiviteit.getVergelijkingsNaam());
		}
		Set<String> removeNames = new HashSet<String>();
		for (OrganisatieEenheid curEenheid : oEenheden)
		{
			Set<String> curNames = names.get(curEenheid);
			curNames.removeAll(removeNames);
			removeNames.addAll(curNames);
		}
		Iterator< ? extends IHierarchischeOrganisatieEenheidEntiteit> it = entiteiten.iterator();
		while (it.hasNext())
		{
			IHierarchischeOrganisatieEenheidEntiteit curActiviteit = it.next();
			if (!names.get(curActiviteit.getOrganisatieEenheid()).contains(
				curActiviteit.getVergelijkingsNaam()))
			{
				it.remove();
			}
		}
	}

	@Override
	public OrganisatieEenheid get(String afkorting)
	{
		Asserts.assertNotNull("afkorting", afkorting);
		Criteria criteria = createCriteria(OrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeFixedMatchMode("afkorting", afkorting, MatchMode.EXACT);
		return cachedTypedUnique(criteria);
	}

	@Override
	public OrganisatieEenheid getByNaam(String naam)
	{
		Asserts.assertNotNull("naam", naam);
		Criteria criteria = createCriteria(OrganisatieEenheid.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeFixedMatchMode("naam", naam, MatchMode.EXACT);
		return cachedTypedUnique(criteria);
	}
}
