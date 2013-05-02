package nl.topicus.eduarte.dao.hibernate;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.DetachedCriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.dao.helpers.CompetentieMatrixDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.DeelnemerMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LLBMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.VrijeMatrix;
import nl.topicus.eduarte.zoekfilters.CompetentieMatrixZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class CompetentieMatrixHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<CompetentieMatrix, CompetentieMatrixZoekFilter>
		implements CompetentieMatrixDataAccessHelper
{
	public CompetentieMatrixHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	protected Criteria createCriteria(CompetentieMatrixZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter.getMatrixType(), "matrix");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);

		builder.addEquals("hoofdstuk", filter.getHoofdstuk());
		builder.addEquals("niveau", filter.getNiveau());
		builder.addILikeCheckWildcard("naam", filter.getNaam(), MatchMode.ANYWHERE);

		if (VrijeMatrix.class.equals(filter.getMatrixType()))
		{
			if (filter.getExclusiefDeelnemer() == null)
				builder.addNullFilterExpression("deelnemer", NullFilter.IsNull);
			else
				builder.addEquals("deelnemer", filter.getExclusiefDeelnemer());
			if (filter.getKoppelDeelnemer() != null)
			{
				DetachedCriteria dcDeelnemerM = createDetachedCriteria(DeelnemerMatrix.class);
				dcDeelnemerM.setProjection(Projections.property("matrix"));
				DetachedCriteriaBuilder dcDMBuilder = new DetachedCriteriaBuilder(dcDeelnemerM);
				dcDMBuilder.addEquals("deelnemer", filter.getKoppelDeelnemer());
				if (!filter.isExcludeKoppelDeelnemer())
				{
					criteria.add(Subqueries.propertyIn("matrix.id", dcDeelnemerM));
				}
				else
				{
					criteria.add(Subqueries.propertyNotIn("matrix.id", dcDeelnemerM));
				}
			}
		}

		if (filter.getTitelDossier() != null || filter.getCohort() != null)
		{
			criteria.createAlias("dossier", "dossier");
			if (filter.getTitelDossier() != null)
				builder.addILikeCheckWildcard("dossier.titel", filter.getTitelDossier(),
					MatchMode.ANYWHERE);
			if (filter.getCohort() != null)
				builder.addEquals("dossier.cohort", filter.getCohort());
		}

		if (filter.isMustExcludeIds())
			builder.addNotIn("id", filter.getIds());
		else if (filter.isMustIncludeIds())
			builder.addIn("id", filter.getIds());
		return criteria;
	}

	@Override
	public LLBMatrix getLLB(Date peildatum, MBONiveau niveau)
	{
		if (niveau != null)
		{
			Criteria criteria = createCriteria(LLBMatrix.class);
			CriteriaBuilder builder = new CriteriaBuilder(criteria);
			builder.addEquals("niveau", niveau);
			builder.addLessOrEquals("begindatum", peildatum);
			builder.addNullOrGreaterOrEquals("einddatum", peildatum);
			return (LLBMatrix) uncachedUnique(criteria);
		}
		return null;
	}

	@Override
	public List<VrijeMatrix> getVrijeMatrices(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(VrijeMatrix.class, "matrix");
		criteria.createAlias("deelnemerMatrices", "deelnemerMatrices",
			CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("deelnemer", deelnemer),
			Restrictions.eq("deelnemerMatrices.deelnemer", deelnemer)));
		return cachedList(criteria);
	}

	@Override
	public List<VrijeMatrix> getGlobaleVrijeMatrices()
	{
		Criteria criteria = createCriteria(VrijeMatrix.class);
		criteria.add(Restrictions.isNull("deelnemer"));
		return cachedList(criteria);
	}

	@Override
	public List<DeelnemerMatrix> getGekoppeldeVrijeMatrices(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(DeelnemerMatrix.class, "deelnemerMatrix");
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		return cachedList(criteria);
	}
}
