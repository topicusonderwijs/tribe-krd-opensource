package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerResultaatVersie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurDeelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurMedewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class ResultaatstructuurHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Resultaatstructuur, ResultaatstructuurZoekFilter>
		implements ResultaatstructuurDataAccessHelper
{
	public ResultaatstructuurHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(ResultaatstructuurZoekFilter filter)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class, "resultaatstructuur");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		addCriteria(builder, filter, "resultaatstructuur", false);
		return criteria;
	}

	@Override
	public void addCriteria(CriteriaBuilder builder, ResultaatstructuurZoekFilter filter,
			String alias, boolean resultaatQuery)
	{
		builder.createAlias(alias + ".onderwijsproduct", "onderwijsproduct");

		if (filter.getDeelnemers() != null)
		{
			AbstractCriteriaBuilder dcBuilder;
			if (resultaatQuery)
			{
				dcBuilder = builder;
			}
			else
			{
				DetachedCriteria dc = createDetachedCriteria(Resultaatstructuur.class, alias);
				dcBuilder = new DetachedCriteriaBuilder(dc);
				dcBuilder.createAlias(alias + ".onderwijsproduct", "onderwijsproduct");
				dc.setProjection(Projections.property("id"));
				builder.propertyIn(alias + ".id", dc);
				filter.setResultCacheable(false);
			}
			dcBuilder.createAlias("onderwijsproduct.afnames", "afname");
			dcBuilder.addPropertyEquals(alias + ".cohort", "afname.cohort");
			dcBuilder.addIn("afname.deelnemer", filter.getDeelnemers());
			dcBuilder.addLessOrEquals("afname.begindatum", filter.getPeildatum());
			dcBuilder.addGreaterOrEquals("afname.einddatumNotNull", filter.getPeildatum());

			if (filter.isAlleenGekoppeldAanVerbintenis() && filter.getContextVerbintenis() != null)
			{
				dcBuilder.createAlias("afname.afnameContexten", "onderwijsproductafnamecontext");
				dcBuilder.addEquals("onderwijsproductafnamecontext.verbintenis", filter
					.getContextVerbintenis());
			}
			if (filter.isAlleenGekoppeldAanVerbintenis() && filter.isAlleenActieveVerbintenissen())
			{
				dcBuilder.createAlias("afname.afnameContexten", "onderwijsproductafnamecontext");
				dcBuilder.createAlias("onderwijsproductafnamecontext.verbintenis", "verbintenis");
				dcBuilder.addLessOrEquals("verbintenis.begindatum", filter.getPeildatum());
				dcBuilder.addGreaterOrEquals("verbintenis.einddatumNotNull", filter.getPeildatum());
			}
		}

		if (filter.getVerwijsbareToets() != null)
		{
			DetachedCriteria dc = createDetachedCriteria(Toets.class, "toets");
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("verwijsbaar", true);
			dc.setProjection(Projections.property("resultaatstructuur"));
			if (filter.getVerwijsbareToets())
				builder.propertyIn(alias + ".id", dc);
			else
				builder.propertyNotIn(alias + ".id", dc);

			filter.setResultCacheable(false);
		}

		if (Type.FORMATIEF.equals(filter.getType()) && filter.getMedewerker() != null
			&& !filter.isOokVanAnderen())
		{
			DetachedCriteria dc =
				createDetachedCriteria(ResultaatstructuurMedewerker.class, "resMedewerker");
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.addEquals("medewerker", filter.getMedewerker());
			dc.setProjection(Projections.property("resultaatstructuur"));
			builder.addOrs(Restrictions.eq(alias + ".specifiek", false), Restrictions.eq(alias
				+ ".auteur", filter.getMedewerker()), Subqueries.propertyIn(alias + ".id", dc));
			filter.setResultCacheable(false);
		}

		if (Type.FORMATIEF.equals(filter.getType())
			&& (filter.getDeelnemers() != null || filter.getGroepDirect() != null))
		{
			DetachedCriteria deelnemer =
				createDetachedCriteria(ResultaatstructuurDeelnemer.class, "resDeelnemer");
			DetachedCriteriaBuilder deelnemerBuilder = new DetachedCriteriaBuilder(deelnemer);
			deelnemer.setProjection(Projections.property("resultaatstructuur"));
			deelnemerBuilder.addNullFilterExpression("deelnemer", NullFilter.IsNotNull);
			deelnemerBuilder.addIn("deelnemer", filter.getDeelnemers());

			DetachedCriteria groep =
				createDetachedCriteria(ResultaatstructuurDeelnemer.class, "resDeelnemer");
			DetachedCriteriaBuilder groepBuilder = new DetachedCriteriaBuilder(groep);
			groep.setProjection(Projections.property("resultaatstructuur"));
			groepBuilder.createAlias("groep", "groep");
			groepBuilder.createAlias("groep.deelnamesUnordered", "deelnamesUnordered");
			groepBuilder.addIn("deelnamesUnordered.deelnemer", filter.getDeelnemers());
			groepBuilder.addEquals("groep", filter.getGroepDirect());
			groepBuilder.addNullFilterExpression("groep", NullFilter.IsNotNull);
			groepBuilder.addLessOrEquals("groep.begindatum", filter.getPeildatum());
			groepBuilder.addGreaterOrEquals("groep.einddatumNotNull", filter.getPeildatum());
			groepBuilder.addLessOrEquals("deelnamesUnordered.begindatum", filter.getPeildatum());
			groepBuilder.addGreaterOrEquals("deelnamesUnordered.einddatumNotNull", filter
				.getPeildatum());

			List<Criterion> ors = new ArrayList<Criterion>();
			if (filter.getGroepDirect() == null)
				ors.add(Restrictions.eq(alias + ".specifiek", false));
			if (filter.getDeelnemers() != null)
				ors.add(Subqueries.propertyIn(alias + ".id", deelnemer));
			ors.add(Subqueries.propertyIn(alias + ".id", groep));
			builder.addOrs(ors);
			filter.setResultCacheable(false);
		}

		if (!filter.addOrganisatieEenheidLocatieDetachedCriteria(this, builder.getCriteria(),
			OnderwijsproductAanbod.class, "onderwijsproduct", alias + ".onderwijsproduct"))
			builder.getCriteria().add(Restrictions.sqlRestriction("0 = 1"));

		if (filter.getTaxonomiecode() != null)
		{
			DetachedCriteria dc =
				createDetachedCriteria(OnderwijsproductTaxonomie.class, "productTax");
			DetachedCriteriaBuilder dcBuilder = new DetachedCriteriaBuilder(dc);
			dcBuilder.createAlias("productTax.taxonomieElement", "taxonomieElement");
			dcBuilder.addILikeCheckWildcard("taxonomieElement.taxonomiecode", filter
				.getTaxonomiecode(), MatchMode.START);
			dc.setProjection(Projections.property("onderwijsproduct"));
			builder.propertyIn(alias + ".onderwijsproduct", dc);
			filter.setResultCacheable(false);
		}

		builder.addEquals(alias + ".type", filter.getType());
		builder.addEquals(alias + ".auteur", filter.getAuteur());
		builder.addEquals(alias + ".cohort", filter.getCohort());
		builder.addEquals(alias + ".onderwijsproduct", filter.getOnderwijsproduct());
		builder.addEquals(alias + ".actief", filter.getActief());
		builder.addEquals(alias + ".categorie", filter.getCategorie());
		builder.addILikeCheckWildcard("code", filter.getCode(), MatchMode.START);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
	}

	@Override
	public List<Resultaatstructuur> getStructuren(Collection<Deelnemer> deelnemers)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("onderwijsproduct", "onderwijsproduct");
		builder.createAlias("onderwijsproduct.afnames", "afname");
		builder.addPropertyEquals("cohort", "afname.cohort");
		builder.addIn("afname.deelnemer", deelnemers);
		return cachedList(criteria);
	}

	@Override
	public List<DeelnemerResultaatVersie> getVersies(List<Deelnemer> deelnemers,
			List<Resultaatstructuur> structuren)
	{
		Criteria criteria = createCriteria(DeelnemerResultaatVersie.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("resultaatstructuur", structuren);
		builder.addIn("deelnemer", deelnemers);
		return cachedList(criteria);
	}

	@Override
	public void incrementVersies(List<DeelnemerResultaatVersie> versies)
	{
		// update DeelnemerResultaatVersie set versie = versie+1 where organisatie =
		// :organisatie and id in (:versies)
		for (DeelnemerResultaatVersie curVersie : versies)
		{
			curVersie.increment();
			curVersie.saveOrUpdate();
		}
	}

	@Override
	public Resultaatstructuur getSummatieveResultaatstructuur(Onderwijsproduct product,
			Cohort cohort)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", product);
		builder.addEquals("cohort", cohort);
		builder.addEquals("type", Resultaatstructuur.Type.SUMMATIEF);
		return cachedTypedUnique(criteria);
	}

	@Override
	public Resultaatstructuur getResultaatstructuur(Onderwijsproduct product, Cohort cohort,
			Type type, String code)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", product);
		builder.addEquals("cohort", cohort);
		builder.addEquals("type", type);
		builder.addEquals("code", code);
		return cachedTypedUnique(criteria);
	}

	@Override
	public List<Resultaatstructuur> getResultaatstructuren(Onderwijsproduct product, Cohort cohort,
			Type type, String code)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", product);
		builder.addEquals("cohort", cohort);
		builder.addEquals("type", type);
		builder.addEquals("code", code);
		return cachedTypedList(criteria);
	}

	@Override
	public List<Long> getResultaatstructuurIds(Collection<Onderwijsproduct> producten, Cohort cohort)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("onderwijsproduct", producten);
		builder.addEquals("cohort", cohort);
		criteria.setProjection(Projections.id());
		return cachedList(criteria);
	}

	@Override
	public List<Resultaatstructuur> getResultaatstructuren(Collection<Onderwijsproduct> producten,
			Cohort cohort)
	{
		Criteria criteria = createCriteria(Resultaatstructuur.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("onderwijsproduct", producten);
		builder.addEquals("cohort", cohort);
		return cachedTypedList(criteria);
	}

	@Override
	public Resultaatstructuur get(Long resultaatstructuurId)
	{
		return get(Resultaatstructuur.class, resultaatstructuurId);
	}

	@Override
	public boolean heeftResultaten(Resultaatstructuur structuur)
	{
		Criteria ret = createCriteria(Resultaat.class);
		ret.createAlias("toets", "toets");
		CriteriaBuilder builder = new CriteriaBuilder(ret);
		builder.addEquals("toets.resultaatstructuur", structuur);
		ret.setProjection(Projections.rowCount());
		return ((Long) uncachedUnique(ret)) > 0;
	}

	@Override
	public boolean isStructuurAfgenomen(Deelnemer deelnemer, Resultaatstructuur structuur)
	{
		Criteria criteria = createCriteria(OnderwijsproductAfname.class, "afname");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("onderwijsproduct", structuur.getOnderwijsproduct());
		builder.addEquals("cohort", structuur.getCohort());
		builder.addEquals("deelnemer", deelnemer);
		return cachedUnique(criteria) != null;
	}

	@Override
	public void verwijderStructuurEnBijbehorendeResultaten(
			IChangeRecordingModel<Resultaatstructuur> resultaatstructuurModel)
	{
		verwijderBijbehorendeResultaten(resultaatstructuurModel.getObject());

		String hqlDeleteVersies =
			"delete from DeelnemerResultaatVersie where resultaatstructuur = :structuur";
		Query query = createQuery(hqlDeleteVersies, DeelnemerResultaatVersie.class);
		query.setParameter("structuur", resultaatstructuurModel.getObject());
		query.executeUpdate();

		resultaatstructuurModel.deleteObject();
	}

	@Override
	public void verwijderBijbehorendeResultaten(Resultaatstructuur structuur)
	{
		Resultaat updateResultaat = null;
		List<Toets> toetsen = new ArrayList<Toets>(structuur.getToetsen());
		Iterator<Toets> it = toetsen.iterator();
		while (it.hasNext())
		{
			Toets curToets = it.next();
			if (!curToets.isSaved())
				it.remove();
			else
			{
				if (updateResultaat == null && !curToets.getResultaten().isEmpty())
					updateResultaat = curToets.getResultaten().get(0);
				curToets.getResultaten().clear();
			}
		}
		// zorg ervoor dat de query cache geleegd wordt: HHH-5426
		if (updateResultaat != null)
		{
			updateResultaat.touch();
			updateResultaat.flush();
		}

		String hqlUpdateRes = "update Resultaat set overschrijft=null where toets in (:toetsen)";
		Query query = createQuery(hqlUpdateRes, Resultaat.class);
		query.setParameterList("toetsen", toetsen);
		query.executeUpdate();

		String hqlDeleteRes = "delete from Resultaat where toets in (:toetsen)";
		query = createQuery(hqlDeleteRes, Resultaat.class);
		query.setParameterList("toetsen", toetsen);
		query.executeUpdate();
	}

	@Override
	public List<Deelnemer> getDeelnemersMetResultaten(Resultaatstructuur structuur)
	{
		Criteria criteria = createCriteria(Resultaat.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.createAlias("toets", "toets");
		builder.addEquals("toets.resultaatstructuur", structuur);
		criteria.setProjection(Projections.distinct(Projections.property("deelnemer")));
		return cachedList(criteria);
	}

	@Override
	public Resultaatstructuur getMeestWaarschijnlijkeStructuur(ResultaatstructuurZoekFilter filter)
	{
		ResultaatstructuurZoekFilter filterCopy = new ZoekFilterCopyManager().copyObject(filter);
		filterCopy.setAlleenGekoppeldAanVerbintenis(true);
		filterCopy.setAlleenActieveVerbintenissen(true);
		List<Resultaatstructuur> structuren = list(filterCopy);
		if (structuren.isEmpty())
			structuren = list(filter);
		return structuren.isEmpty() ? null : structuren.get(0);
	}
}
