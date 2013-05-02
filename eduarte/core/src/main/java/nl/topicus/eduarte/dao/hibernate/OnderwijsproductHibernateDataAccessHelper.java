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
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductOpvolger;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductSamenstelling;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductVoorwaarde;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductZoekterm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class OnderwijsproductHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Onderwijsproduct, OnderwijsproductZoekFilter> implements
		OnderwijsproductDataAccessHelper
{
	public OnderwijsproductHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(OnderwijsproductZoekFilter filter)
	{
		Criteria criteria = createCriteria(Onderwijsproduct.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		if (filter.orderByListContainsAlias("leerstijl"))
			builder.createAlias("leerstijl", "leerstijl", CriteriaSpecification.LEFT_JOIN);
		if (filter.orderByListContainsAlias("typeToets"))
			builder.createAlias("typeToets", "typeToets", CriteriaSpecification.LEFT_JOIN);
		if (filter.orderByListContainsAlias("typeLocatie"))
			builder.createAlias("typeLocatie", "typeLocatie", CriteriaSpecification.LEFT_JOIN);
		if (filter.orderByListContainsAlias("soortPraktijklokaal"))
			builder.createAlias("soortPraktijklokaal", "soortPraktijklokaal",
				CriteriaSpecification.LEFT_JOIN);

		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, criteria,
			OnderwijsproductAanbod.class, "onderwijsproduct"))
			return null;
		builder.addEquals("soortProduct", filter.getSoortOnderwijsproduct());
		builder.addEquals("status", filter.getStatus());
		if (filter.getCohort() == null)
		{
			builder.addGreaterOrEquals("einddatumNotNull", filter.getPeildatum());
		}
		else
		{
			builder.addGreaterOrEquals("einddatumNotNull", filter.getCohort().getEinddatum());
		}

		if (filter.getBijIntake() != null)
			builder.addEquals("bijIntake", filter.getBijIntake());

		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("titel", filter.getTitel(), MatchMode.ANYWHERE);
		filter.addQuickSearchCriteria(builder, "code", "titel");
		if (filter.heeftTaxonomieCriteria())
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductTaxonomie.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);

			dcBuilder.createAlias("taxonomieElement", "taxonomieElement");
			dcBuilder.addEquals("taxonomieElement.taxonomie", filter.getTaxonomie());
			dcBuilder.addEquals("taxonomieElement.taxonomieElementType", filter
				.getTaxonomieElementType());
			dcBuilder.addILikeCheckWildcard("taxonomieElement.taxonomiecode", filter
				.getTaxonomiecode(), MatchMode.START);
			dc.setProjection(Projections.property("onderwijsproduct"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getVoorwaardeVoor() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductVoorwaarde.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("voorwaardeVoor", filter.getVoorwaardeVoor());
			dc.setProjection(Projections.property("voorwaardelijkProduct"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getOpvolgerVan() != null || filter.getNietOpvolgerVan() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductOpvolger.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("oudProduct", filter.getOpvolgerVan());
			dcBuilder.addEquals("oudProduct", filter.getNietOpvolgerVan());
			dc.setProjection(Projections.property("nieuwProduct"));
			if (filter.getOpvolgerVan() != null)
			{
				criteria.add(Subqueries.propertyIn("id", dc));
			}
			else
			{
				criteria.add(Subqueries.propertyNotIn("id", dc));
				builder.addNotEquals("id", filter.getNietOpvolgerVan().getId());
			}
			filter.setResultCacheable(false);
		}
		if (filter.getParent() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductSamenstelling.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("parent", filter.getParent());
			dc.setProjection(Projections.property("child"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getZoekterm() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductZoekterm.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addILikeCheckWildcard("zoekterm", filter.getZoekterm(), MatchMode.ANYWHERE);
			dc.setProjection(Projections.property("onderwijsproduct"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}
		if (filter.getDeelnemers() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(OnderwijsproductAfname.class);
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addIn("deelnemer", filter.getDeelnemers());
			dc.setProjection(Projections.property("onderwijsproduct"));
			criteria.add(Subqueries.propertyIn("id", dc));
			filter.setResultCacheable(false);
		}

		if (filter.getStage() != null)
		{
			criteria.createAlias("soortProduct", "soortProduct");
			builder.addEquals("soortProduct.stage", filter.getStage());
		}

		builder.addEquals("credits", filter.getCredits());
		builder.addGreaterOrEquals("credits", filter.getMincredits());
		builder.addLessOrEquals("credits", filter.getMaxcredits());

		builder.addNotIn("id", filter.getExcludedProductenIds());

		return criteria;
	}

	@Override
	public boolean isInGebruik(Onderwijsproduct onderwijsproduct)
	{
		return onderwijsproduct.isInGebruik();
	}

	@Override
	public List<Onderwijsproduct> getChildren(Onderwijsproduct onderwijsproduct)
	{
		if (onderwijsproduct == null)
			return Collections.emptyList();
		List<Onderwijsproduct> list = new ArrayList<Onderwijsproduct>();
		traverceOnderdelen(list, onderwijsproduct);
		return list;
	}

	@Override
	public List<Onderwijsproduct> getOnderwijsproductByCode(String code)
	{
		Criteria criteria = createCriteria(Onderwijsproduct.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);

		return cachedTypedList(criteria);
	}

	@Override
	public List<Onderwijsproduct> getOnderwijsproductByTaxCode(String taxonomiecode)
	{
		Criteria criteria = createCriteria(OnderwijsproductTaxonomie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("taxonomieElement", "taxonomieElement");
		builder.addEquals("taxonomieElement.taxonomiecode", taxonomiecode);
		criteria.setProjection(Projections.property("onderwijsproduct"));
		return cachedTypedList(criteria);
	}

	/**
	 * Voegt alle kinderen en diens kinderen toe aan de target lijst.
	 * 
	 * @param target
	 * @param current
	 * @param children
	 */
	private void traverceOnderdelen(List<Onderwijsproduct> target, Onderwijsproduct current)
	{
		if (target.contains(current))
			return;
		target.add(current);
		List<OnderwijsproductSamenstelling> childs = current.getOnderdelen();
		if (childs == null || childs.isEmpty())
			return;
		for (OnderwijsproductSamenstelling child : childs)
			traverceOnderdelen(target, child.getChild());
	}

	/**
	 * Voegt alle kinderen en diens kinderen toe aan de target lijst.
	 */
	private void traverceVoorwaarden(List<Onderwijsproduct> target, Onderwijsproduct current)
	{
		if (target.contains(current))
			return;
		target.add(current);
		List<OnderwijsproductVoorwaarde> voorwaarden = current.getVoorwaarden();
		if (voorwaarden == null || voorwaarden.isEmpty())
			return;
		for (OnderwijsproductVoorwaarde voorwaarde : voorwaarden)
			traverceVoorwaarden(target, voorwaarde.getVoorwaardelijkProduct());
	}

	@Override
	public List<Onderwijsproduct> getVoorwaarden(Onderwijsproduct onderwijsproduct)
	{
		if (onderwijsproduct == null)
			return Collections.emptyList();
		List<Onderwijsproduct> list = new ArrayList<Onderwijsproduct>();
		traverceVoorwaarden(list, onderwijsproduct);
		return list;
	}

	@Override
	public <U extends IOrganisatieEenheidLocatieKoppelEntiteit<U>> List<Onderwijsproduct> getGekoppeldeOnderwijsproducten(
			Deelgebied deelgebied, List<U> aanbodList)
	{
		Asserts.assertNotNull("deelgebied", deelgebied);
		Asserts.assertNotNull("aanbodList", aanbodList);
		if (aanbodList.isEmpty())
		{
			return Collections.emptyList();
		}
		Criteria criteria = createCriteria(Onderwijsproduct.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		DetachedCriteria dcTaxonomie = createDetachedCriteria(OnderwijsproductTaxonomie.class);
		dcTaxonomie.add(Restrictions.eq("taxonomieElement", deelgebied));
		dcTaxonomie.setProjection(Projections.property("onderwijsproduct"));
		criteria.add(Subqueries.propertyIn("id", dcTaxonomie));

		List<Criterion> ors = new ArrayList<Criterion>(aanbodList.size());
		for (U aanbod : aanbodList)
		{
			DetachedCriteria dcAanbod = createDetachedCriteria(OnderwijsproductAanbod.class);
			dcAanbod.add(Restrictions.in("organisatieEenheid", aanbod.getOrganisatieEenheid()
				.getParentsEnChildren(EduArteContext.get().getPeildatumOfVandaag())));
			dcAanbod.add(Restrictions.or(Restrictions.eq("locatie", aanbod.getLocatie()),
				Restrictions.isNull("locatie")));
			dcAanbod.setProjection(Projections.property("onderwijsproduct"));
			ors.add(Subqueries.propertyIn("id", dcAanbod));
		}
		builder.addOrs(ors);

		return cachedTypedList(criteria);
	}

	@Override
	public List<String> getCodes()
	{
		Criteria criteria = createCriteria(Onderwijsproduct.class);
		criteria.setProjection(Projections.property("code"));
		return cachedList(criteria);
	}

	@Override
	public Onderwijsproduct get(String code)
	{
		Asserts.assertNotNull("code", code);
		Criteria criteria = createCriteria(Onderwijsproduct.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("code", code);
		return cachedTypedUnique(criteria);
	}
}
