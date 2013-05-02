package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.helpers.IJkpuntDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.DeelnemerMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;
import nl.topicus.eduarte.zoekfilters.IJkpuntZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class IJkpuntHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<IJkpunt, IJkpuntZoekFilter> implements
		IJkpuntDataAccessHelper
{
	public IJkpuntHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(IJkpuntZoekFilter filter)
	{
		Criteria criteria = createCriteria(IJkpunt.class);

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);

		if (filter.isFilterOnDeelnemer())
		{
			if (filter.getDeelnemer() != null)
			{
				if (filter.isDeelnemerNullOrEquals())
					builder.addNullOrEquals("deelnemer", filter.getDeelnemer());
				else
					builder.addEquals("deelnemer", filter.getDeelnemer());
			}
			else
				criteria.add(Restrictions.isNull("deelnemer"));
		}
		builder.addNullOrEquals("opleiding", filter.getOpleiding());
		builder.addEquals("cohort", filter.getCohort());
		if (filter.getVerbintenissen() != null && !filter.getVerbintenissen().isEmpty())
		{
			Set<Opleiding> aanboden = new HashSet<Opleiding>();
			// TODO not optimal, but any other method seems to fail
			for (Verbintenis curInschrijving : filter.getVerbintenissen())
			{
				if (curInschrijving.getOpleiding() != null)
					aanboden.add(curInschrijving.getOpleiding());
			}
			builder.addIn("opleiding", aanboden);
		}
		if (filter.getMatrix() != null || filter.isMatrixBewustNull())
			builder.addEquals("matrix", filter.getMatrix());
		else
			criteria.add(Restrictions.isNull("matrix"));

		builder.addLessOrEquals("datum", filter.getEindDatum());
		builder.addGreaterOrEquals("datum", filter.getBeginDatum());
		return criteria;
	}

	@Override
	public void batchDeleteIJkpunten(CompetentieMatrix matrix, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(IJkpunt.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matrix", matrix);
		builder.addEquals("deelnemer", deelnemer);
		List<IJkpunt> ijkpunten = cachedList(criteria);
		for (IJkpunt curIJkpunt : ijkpunten)
		{
			for (CompetentieNiveau curNiveau : curIJkpunt.getCompetentieNiveaus())
			{
				curNiveau.delete();
			}
			curIJkpunt.delete();
		}
	}

	@Override
	public List<IJkpunt> getIJkpunten(Opleiding opleiding, Deelnemer deelnemer,
			CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(IJkpunt.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("matrix", matrix);
		builder.addEquals("opleiding", opleiding);
		builder.addNullOrEquals("deelnemer", deelnemer);

		criteria.addOrder(Order.desc("datum"));
		return cachedList(criteria);
	}

	@Override
	public List<IJkpunt> getIndividueleIJkpunten(Opleiding opleiding, Deelnemer deelnemer,
			CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(IJkpunt.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("matrix", matrix);
		builder.addEquals("opleiding", opleiding);
		builder.addEquals("deelnemer", deelnemer);

		criteria.addOrder(Order.desc("datum"));
		return cachedList(criteria);
	}

	@Override
	public List<IJkpunt> getBereikteIJkpunten(Deelnemer deelnemer, Beoordeling beoordeling)
	{
		IJkpuntZoekFilter filter = new IJkpuntZoekFilter();
		filter.setDeelnemer(deelnemer);
		filter.setDeelnemerNullOrEquals(true);
		filter.setMatrix(beoordeling.getMatrix());
		filter.setOpleiding(beoordeling.getOpleiding());
		List<IJkpunt> ret = new ArrayList<IJkpunt>();
		List<IJkpunt> ijkpunten = list(filter);

		// TODO Olav kan hier vast iets beters voor bedenken
		for (IJkpunt curIJkpunt : ijkpunten)
		{
			if (curIJkpunt.isBereikt(beoordeling.getCompetentieNiveauAsMap()))
			{
				ret.add(curIJkpunt);
			}
		}
		return ret;
	}

	@Override
	public List<Deelnemer> getDeelnemers(IJkpunt ijkpunt)
	{
		if (ijkpunt.getDeelnemer() != null)
			return Arrays.asList(ijkpunt.getDeelnemer());

		if (ijkpunt.getOpleiding() == null)
		{
			Criteria criteria = createCriteria(DeelnemerMatrix.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.addEquals("matrix", ijkpunt.getMatrix());
			criteria.setProjection(Projections.property("deelnemer"));
			return cachedList(criteria);
		}

		Criteria criteria = createCriteria(Verbintenis.class);
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.setOpleiding(ijkpunt.getOpleiding());
		criteria.setProjection(Projections.property("deelnemer"));
		return cachedList(criteria);
	}

	@Override
	public boolean isSignaalVerstuurd(IJkpunt ijkpunt, Deelnemer deelnemer, IdObject ontvanger)
	{
		return false;
	}
}
