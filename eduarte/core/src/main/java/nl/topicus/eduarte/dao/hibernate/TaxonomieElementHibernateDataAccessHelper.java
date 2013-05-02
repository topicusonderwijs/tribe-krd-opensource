package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.VerbintenisgebiedOnderdeel;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kwalificatiedossier;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

public class TaxonomieElementHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<TaxonomieElement, TaxonomieElementZoekFilter> implements
		TaxonomieElementDataAccessHelper
{
	public TaxonomieElementHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(TaxonomieElementZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter.getZoekenNaarClass());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.createAlias("parent", "parent", JoinFragment.LEFT_OUTER_JOIN);
		builder.createAlias("taxonomie", "taxonomie");
		builder.createAlias("taxonomieElementType", "taxonomieElementType");
		builder.addNullFilterExpression("organisatie", filter.getOrganisatieFilter());
		builder.addILikeCheckWildcard("afkorting", filter.getAfkorting(), MatchMode.START);
		builder.addILikeFixedMatchMode("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("taxonomiecode", filter.getTaxonomiecode(), MatchMode.START);
		builder.addEquals("taxonomie", filter.getTaxonomie());
		builder.addEquals("taxonomieElementType", filter.getTaxonomieElementType());
		builder.addEquals("externeCode", filter.getExterneCode());
		builder.addEquals("parent", filter.getParent());
		builder.addEquals("taxonomieElementType.inschrijfbaar", filter.getInschrijfbaar());
		if (filter.getGekoppeldAanOnderwijsProduct() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductTaxonomie.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("onderwijsproduct", filter.getGekoppeldAanOnderwijsProduct());
			dc.setProjection(Projections.property("taxonomieElement"));
			builder.propertyIn("id", dc);
		}
		builder.addEquals("uitzonderlijk", filter.getUitzonderlijk());
		filter.addQuickSearchCriteria(builder, "afkorting", "naam", "taxonomiecode", "externeCode");

		return criteria;
	}

	@Override
	public List<Taxonomie> listTaxonomien()
	{
		Criteria criteria = createCriteria(Taxonomie.class);
		criteria.addOrder(Order.asc("taxonomiecode"));
		return cachedList(criteria);
	}

	@Override
	public boolean isInGebruik(TaxonomieElement element)
	{
		// Controleer op opleidingen.
		if (element instanceof Verbintenisgebied)
		{
			Criteria criteria = createCriteria(Opleiding.class);
			criteria.add(Restrictions.eq("verbintenisgebied", element));
			criteria.setProjection(Projections.rowCount());
			Long count = cachedUnique(criteria);
			if (count.intValue() > 0)
			{
				return true;
			}
			// Controleer op onderdeel van.
			criteria = createCriteria(VerbintenisgebiedOnderdeel.class);
			criteria.add(Restrictions.or(Restrictions.eq("parent", element),
				Restrictions.eq("child", element)));
			criteria.setProjection(Projections.rowCount());
			count = cachedUnique(criteria);
			if (count.intValue() > 0)
			{
				return true;
			}
		}
		// Controleer op onderwijsproducten.
		Criteria criteria = createCriteria(OnderwijsproductTaxonomie.class);
		criteria.add(Restrictions.eq("taxonomieElement", element));
		criteria.setProjection(Projections.rowCount());
		Integer count = cachedUnique(criteria);
		if (count.intValue() > 0)
		{
			return true;
		}
		// Niets gevonden.
		return false;
	}

	@Override
	public TaxonomieElement getTaxonomieElement(TaxonomieElement parent, String naam,
			TaxonomieElementType type)
	{
		Asserts.assertNotNull("parent", parent);
		Asserts.assertNotNull("naam", naam);
		Asserts.assertNotNull("type", type);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("parent", parent);
		builder.addEquals("naam", naam);
		builder.addEquals("taxonomieElementType", type);

		return cachedTypedUnique(criteria);
	}

	@Override
	public int getAantalChildren(TaxonomieElement parent, boolean includeerOnbekend)
	{
		Asserts.assertNotNull("parent", parent);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("parent", parent);
		if (!includeerOnbekend)
			builder.addNotEquals("naam", "Onbekend");
		criteria.setProjection(Projections.rowCount());

		return ((Number) uncachedUnique(criteria)).intValue();
	}

	@Override
	public String getMaxTaxonomiecode(TaxonomieElement parent, boolean includeerOnbekend)
	{
		Asserts.assertNotNull("parent", parent);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("parent", parent);
		if (!includeerOnbekend)
			builder.addNotEquals("naam", "Onbekend");
		criteria.setProjection(Projections.max("sorteercode"));

		return (String) uncachedUnique(criteria);
	}

	@Override
	public int getMaxTaxonomiecodeVolgnummer(TaxonomieElement parent, boolean includeerOnbekend)
	{
		Asserts.assertNotNull("parent", parent);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("parent", parent);
		if (!includeerOnbekend)
			builder.addNotEquals("naam", "Onbekend");
		criteria.setProjection(Projections.max("volgnummer"));

		return (Integer) uncachedUnique(criteria);
	}

	@Override
	public TaxonomieElement get(String taxonomiecode)
	{
		Asserts.assertNotNull("taxonomiecode", taxonomiecode);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taxonomiecode", taxonomiecode);
		return cachedTypedUnique(criteria);
	}

	@Override
	public TaxonomieElement getLandelijk(String taxonomiecode)
	{
		Asserts.assertNotNull("taxonomiecode", taxonomiecode);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("taxonomiecode", taxonomiecode);
		builder.addIsNull("organisatie", true);
		TaxonomieElement element = cachedTypedUnique(criteria);
		if (element != null && !element.isLandelijk())
		{
			// Niet landelijk element opgevraagd zonder een instelling op te geven --> Dat
			// mag niet.
			throw new IllegalArgumentException(
				"Niet landelijke taxonomie gevonden met de gegeven code, terwijl er geen instelling is opgegeven.");
		}
		return element;
	}

	@Override
	public List<TaxonomieElement> list(TaxonomieElementType taxonomieElementType, String externeCode)
	{
		Asserts.assertNotNull("taxonomieElementType", taxonomieElementType);
		Asserts.assertNotNull("externeCode", externeCode);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("externeCode", externeCode);
		builder.addEquals("taxonomieElementType", taxonomieElementType);
		return cachedTypedList(criteria);
	}

	@Override
	public <T extends TaxonomieElement> List<T> getTaxonomieElementen(Class<T> clazz, String naam,
			boolean strict)
	{
		Asserts.assertNotNull("clazz", clazz);
		Asserts.assertNotNull("naam", naam);
		// Probeer eerst met echt gelijk.
		Criteria criteria = createCriteria(clazz);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam);
		List<T> res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}
		if (strict)
		{
			return Collections.emptyList();
		}

		// Soms gaat het mis met trema's e.d.
		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addEquals("naam", naam.replace("Ã«", "ë").replace("Ã©", "é"));
		res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}

		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", naam + " _", MatchMode.EXACT);
		res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}

		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", naam, MatchMode.START);
		res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}

		// Soms wordt 'en' een '&'
		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam",
			naam.replace("en", "&").replace("-", "/").replace(" sw6", ""), MatchMode.START);
		res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}

		// Soms gaat het mis met trema's e.d.
		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", naam.replace("Ã«", "ë").replace("Ã©", "é"),
			MatchMode.START);
		res = cachedList(criteria);
		if (res.size() > 0)
		{
			return res;
		}

		criteria = createCriteria(clazz);
		builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", naam.replace("ë", "Ã«").replace("é", "Ã©"),
			MatchMode.START);
		res = cachedList(criteria);

		return res;
	}

	@Override
	public List<TaxonomieElement> list(String prefix, List<String> codes)
	{
		Asserts.assertNotNull("prefix", prefix);
		Asserts.assertNotEmpty("codes", codes);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		List<String> taxonomiecodes = new ArrayList<String>(codes.size());
		for (String code : codes)
		{
			taxonomiecodes.add(prefix + code);
		}
		builder.addIn("taxonomiecode", taxonomiecodes);
		return cachedTypedList(criteria);
	}

	@Override
	public List<Kwalificatiedossier> getKwalificatieDossiers()
	{
		Criteria criteria = createCriteria(Kwalificatiedossier.class);
		return cachedList(criteria);
	}

	@Override
	public TaxonomieElement get(Long id)
	{
		Asserts.assertNotNull("id", id);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("id", id);
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<TaxonomieElement> list(String externeCode)
	{
		Asserts.assertNotNull("externeCode", externeCode);
		Criteria criteria = createCriteria(TaxonomieElement.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("externeCode", externeCode);
		return cachedTypedList(criteria);
	}
}