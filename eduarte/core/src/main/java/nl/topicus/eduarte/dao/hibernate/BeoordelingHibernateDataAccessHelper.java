package nl.topicus.eduarte.dao.hibernate;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.helpers.BeoordelingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Beoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.BeoordelingsType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;
import nl.topicus.eduarte.zoekfilters.BeoordelingZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author vandenbrink
 */
public class BeoordelingHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Beoordeling, BeoordelingZoekFilter> implements
		BeoordelingDataAccessHelper
{

	public BeoordelingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(BeoordelingZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter.getSubClass());

		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);
		builder.addEquals("opgenomenIn", filter.getOpgenomenIn());
		if (filter.getMedewerker() != null)
		{
			criteria.add(Restrictions.or(Restrictions.eq("medewerker", filter.getMedewerker()),
				Restrictions.ne("type", BeoordelingsType.DOCENTBEOORDELING)));
		}
		builder.addEquals("deelnemer", filter.getDeelnemer());
		builder.addEquals("groep", filter.getGroep());
		if (filter.getMatrix() == null && !filter.isMatrixBewustNull())
		{
			builder.addNullFilterExpression("matrix", NullFilter.IsNull);
		}
		else
		{
			builder.addEquals("matrix", filter.getMatrix());
		}
		builder.addEquals("type", filter.getType());
		if (filter.getOpgenomenInIsNull() != null)
		{
			builder.addNullFilterExpression("opgenomenIn", filter.getOpgenomenInIsNull()
				? NullFilter.IsNull : NullFilter.IsNotNull);
		}
		if (filter.isMustExcludeIds())
			builder.addNotIn("id", filter.getIds());
		else if (filter.isMustIncludeIds())
			builder.addIn("id", filter.getIds());

		builder.addEquals("cohort", filter.getCohort());
		builder.addLessOrEquals("datum", filter.getEindDatum());
		builder.addGreaterOrEquals("datum", filter.getBeginDatum());

		builder.addEquals("groepsBeoordeling", filter.getGroepsbeoordeling());

		return criteria;
	}

	@Override
	public boolean isCohortInGebruik(Cohort cohort)
	{
		Criteria criteria = createCriteria(CompetentieNiveauVerzameling.class);
		criteria.createAlias("matrix", "matrix");
		criteria.createAlias("matrix.dossier", "dossier");
		criteria.add(Restrictions.eq("dossier.cohort", cohort));
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public Beoordeling getEvcEvk(CompetentieMatrix matrix, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Beoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matrix", matrix);
		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("type", BeoordelingsType.EVC_EVK);
		return cachedUnique(criteria);
	}

	@Override
	public Beoordeling getBasisNiveau(CompetentieMatrix matrix, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Beoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("deelnemer", deelnemer);
		builder.addEquals("matrix", matrix);
		builder.addNotEquals("type", BeoordelingsType.DOCENTBEOORDELING);
		builder.addNullFilterExpression("opgenomenIn", NullFilter.IsNull);
		return cachedUnique(criteria);
	}

	@Override
	public List<Beoordeling> getOpgenomen(Beoordeling vastgestelde)
	{
		if (vastgestelde == null)
		{
			return Collections.emptyList();
		}
		Criteria criteria = createCriteria(Beoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("opgenomenIn", vastgestelde);
		return cachedList(criteria);
	}

	@Override
	public List<CompetentieNiveauVerzameling> getVerzamelingen(CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(CompetentieNiveauVerzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matrix", matrix);
		return cachedList(criteria);
	}

	@Override
	public boolean isBeoordeeld(CompetentieMatrix matrix, Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(Beoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matrix", matrix);
		builder.addEquals("deelnemer", deelnemer);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}

	@Override
	public void batchDeleteBeoordeling(Beoordeling beoordeling)
	{
		for (Beoordeling curBeoordeling : DataAccessRegistry.getHelper(
			BeoordelingDataAccessHelper.class).getOpgenomen(beoordeling))
		{
			curBeoordeling.setOpgenomenIn(null);
			curBeoordeling.update();
		}
		for (CompetentieNiveau curNiveau : beoordeling.getCompetentieNiveaus())
		{
			curNiveau.delete();
		}
		beoordeling.delete();
		beoordeling.commit();
	}

	@Override
	public List<Groepsbeoordeling> getBeoordelingen(Groep groep)
	{
		Criteria criteria = createCriteria(Groepsbeoordeling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("groep", groep);
		return cachedList(criteria);
	}

	@Override
	public boolean bestaatBeoordeling(List<Long> deelnemerIDs, CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(CompetentieNiveauVerzameling.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("matrix", matrix);
		builder.addIn("deelnemer.id", deelnemerIDs);
		criteria.setProjection(Projections.rowCount());
		return (Long) cachedUnique(criteria) > 0;
	}
}
