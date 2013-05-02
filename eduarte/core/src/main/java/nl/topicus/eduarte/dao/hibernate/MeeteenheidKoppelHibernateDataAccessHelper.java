package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.MeeteenheidKoppelDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppel;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;
import nl.topicus.eduarte.zoekfilters.MeeteenheidKoppelZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author vanharen
 */
public class MeeteenheidKoppelHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<MeeteenheidKoppel, MeeteenheidKoppelZoekFilter>
		implements MeeteenheidKoppelDataAccessHelper
{

	public MeeteenheidKoppelHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(MeeteenheidKoppelZoekFilter filter)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class);
		criteria.createAlias("opleiding", "opleiding");
		criteria.createAlias("opleiding.verbintenisgebied", "verbintenisgebied");

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("cohort", filter.getCohort());
		builder.addEquals("meeteenheid", filter.getMeeteenheid());
		builder.addEquals("opleiding", filter.getOpleiding());
		builder.addEquals("opleiding.verbintenisgebied", filter.getVerbintenisgebied());
		builder.addEquals("opleiding.leerweg", filter.getOpleidingleerweg());
		builder.addILikeCheckWildcard("opleiding.naam", filter.getOpleidingnaam(),
			MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("opleiding.code", filter.getOpleidingcode(),
			MatchMode.ANYWHERE);

		builder.addEquals("type", filter.getType());
		builder.addEquals("vastgezet", filter.getVastgezet());
		builder.addEquals("automatischAangemaakt", filter.getAutomatischAangemaakt());

		builder.addILikeCheckWildcard("opleiding.ContextInfoOmschrijving", filter
			.getOmschrijvingOpleiding(), MatchMode.ANYWHERE);
		builder.addILikeCheckWildcard("meeteenheid.naam", filter.getNaamMeeteenheid(),
			MatchMode.ANYWHERE);

		if (filter.getOrganisatieEenheid() != null)
		{
			criteria.add(Restrictions.isNull("organisatieEenheid"));
			criteria.createAlias("opleiding.aanbod", "opleidingaanbod");
			criteria.add(Restrictions.in("opleidingaanbod.organisatieEenheid", filter
				.getOrganisatieEenheid().getActieveChildren(EduArteContext.get().getPeildatum())));
		}

		if (filter.getCohort() != null)
		{
			builder.addEquals("cohort", filter.getCohort());
		}

		/*
		 * Duplicates filteren: Deze 'oplossing' gaat fout met paginering:
		 * criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		 * Daarom wordt onderstaande methode gebruikt.
		 */
		criteria.setProjection(Projections.distinct(Projections.id()));
		List<MeeteenheidKoppel> list = uncachedList(criteria);

		if (list.size() > 0)
		{
			criteria = createCriteria(Groep.class);

			// De lijst verbonden aan een IN statement mag niet meer dan 1000
			// expressies bevatten, vandaar de splitsing in kleinere lijsten:
			int chunk = 1000;
			if (list.size() < chunk)
			{
				criteria.add(Restrictions.in("id", list));
			}
			else
			{
				builder = new CriteriaBuilder(criteria);
				List<Criterion> criterions = new ArrayList<Criterion>();
				int cnt = 0;
				while (list.size() > cnt)
				{
					int end = list.size() > (cnt + chunk) ? cnt + chunk : list.size();
					criterions.add(Restrictions.in("id", list.subList(cnt, end - 1)));
					cnt += chunk;
				}
				builder.addOrs(criterions);
			}
		}

		return criteria;
	}

	@Override
	public List<MeeteenheidKoppel> list(OrganisatieEenheid organisatieEenheid,
			MeeteenheidKoppelType type, boolean automatischAangemaakt)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class);
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("automatischAangemaakt", automatischAangemaakt));
		criteria.createAlias("opleiding", "opleiding");
		criteria.add(Restrictions.in("organisatieEenheid", organisatieEenheid
			.getActieveChildren(EduArteContext.get().getPeildatumOfVandaag())));
		return cachedList(criteria);
	}

	@Override
	public void delete(Opleiding opleiding, MeeteenheidKoppelType type)
	{
		Criteria criteria = createCriteria(MeeteenheidKoppel.class);
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("opleiding", opleiding));
		for (MeeteenheidKoppel mk : this.<MeeteenheidKoppel> cachedList(criteria))
		{
			delete(mk);
		}
		batchExecute();
	}
}
