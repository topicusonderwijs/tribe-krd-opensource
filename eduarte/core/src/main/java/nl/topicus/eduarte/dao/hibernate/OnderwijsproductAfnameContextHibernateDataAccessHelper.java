package nl.topicus.eduarte.dao.hibernate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameContextZoekFilter;

import org.hibernate.Criteria;

public class OnderwijsproductAfnameContextHibernateDataAccessHelper
		extends
		AbstractZoekFilterDataAccessHelper<OnderwijsproductAfnameContext, OnderwijsproductAfnameContextZoekFilter>
		implements OnderwijsproductAfnameContextDataAccessHelper
{
	public OnderwijsproductAfnameContextHibernateDataAccessHelper(
			HibernateSessionProvider provider, QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OnderwijsproductAfnameContextZoekFilter filter)
	{
		Criteria criteria = createCriteria(OnderwijsproductAfnameContext.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("productregel", filter.getProductregel());
		builder.addEquals("verbintenis", filter.getVerbintenis());
		builder.addEquals("onderwijsproductAfname", filter.getOnderwijsproductAfname());
		return criteria;
	}

	@Override
	public OnderwijsproductAfnameContext getOnderwijsproductAfnameContext(Verbintenis verbintenis,
			Productregel productregel)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Asserts.assertNotNull("productregel", productregel);
		Criteria criteria = createCriteria(OnderwijsproductAfnameContext.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("productregel", productregel);
		builder.addEquals("verbintenis", verbintenis);
		criteria.setMaxResults(1);
		return cachedTypedUnique(criteria);
	}

	@Override
	public Map<Productregel, OnderwijsproductAfnameContext> list(Verbintenis verbintenis)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Criteria criteria = createCriteria(OnderwijsproductAfnameContext.class);
		criteria.createAlias("onderwijsproductAfname", "onderwijsproductAfname");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("verbintenis", verbintenis);

		List<OnderwijsproductAfnameContext> list = cachedTypedList(criteria);
		Map<Productregel, OnderwijsproductAfnameContext> res =
			new HashMap<Productregel, OnderwijsproductAfnameContext>(list.size());
		for (OnderwijsproductAfnameContext keuze : list)
		{
			res.put(keuze.getProductregel(), keuze);
		}
		return res;
	}

	@Override
	public Set<Onderwijsproduct> getDuplicaten(Verbintenis verbintenis)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Criteria criteria = createCriteria(OnderwijsproductAfnameContext.class);
		criteria.createAlias("onderwijsproductAfname", "onderwijsproductAfname");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("verbintenis", verbintenis);

		Set<Onderwijsproduct> duplicaten = new HashSet<Onderwijsproduct>();
		List<OnderwijsproductAfnameContext> list = cachedTypedList(criteria);
		// Controleer op duplicaten.
		for (OnderwijsproductAfnameContext context : list)
		{
			for (OnderwijsproductAfnameContext other : list)
			{
				if (context != other)
				{
					if (context.getOnderwijsproductAfname().getOnderwijsproduct()
						.equals(other.getOnderwijsproductAfname().getOnderwijsproduct()))
					{
						duplicaten.add(context.getOnderwijsproductAfname().getOnderwijsproduct());
					}
				}
			}
		}
		return duplicaten;
	}

	@Override
	public List<OnderwijsproductAfnameContext> listContexten(Verbintenis verbintenis)
	{
		Asserts.assertNotNull("verbintenis", verbintenis);
		Criteria criteria = createCriteria(OnderwijsproductAfnameContext.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("verbintenis", verbintenis);
		return cachedTypedList(criteria);
	}
}
