package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.LeerpuntDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Groepsbeoordeling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.GroepsbeoordelingOverschrijving;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kerntaak;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Leerpunt;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Werkproces;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

/**
 * @author papegaaij
 * @author vanharen
 */
public class LeerpuntHibernateDataAccessHelper extends HibernateDataAccessHelper<Leerpunt>
		implements LeerpuntDataAccessHelper
{
	public LeerpuntHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Leerpunt> getLeerpunten(Kerntaak kerntaak)
	{
		Criteria criteria = createCriteria(Leerpunt.class, "leerpunt");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("leerpunt.werkproces", "werkproces");
		builder.addEquals("werkproces.kerntaak", kerntaak);
		return cachedList(criteria);
	}

	@Override
	public List<Leerpunt> getLeerpunten(Werkproces werkproces)
	{
		Criteria criteria = createCriteria(Leerpunt.class, "leerpunt");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("leerpunt.werkproces", "werkproces");
		builder.addEquals("werkproces", werkproces);
		return cachedList(criteria);
	}

	@Override
	public List<Leerpunt> getUsedLeerpunten(CompetentieMatrix matrix)
	{
		Criteria criteria = createCriteria(CompetentieNiveauVerzameling.class, "cnv");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("cnv.competentieNiveaus", "competentieNiveau");
		builder.addEquals("matrix", matrix);
		criteria.setProjection(Projections.property("competentieNiveau.leerpunt"));
		return cachedList(criteria);
	}

	@Override
	public List<Leerpunt> getGroepsbeoordelingOverschrevenLeerpunten(
			Groepsbeoordeling groepsBeoordeling)
	{
		Criteria criteria = createCriteria(GroepsbeoordelingOverschrijving.class, "go");
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		criteria.createAlias("go.competentieNiveaus", "competentieNiveau");
		builder.addEquals("groepsBeoordeling", groepsBeoordeling);
		criteria.setProjection(Projections.property("competentieNiveau.leerpunt"));
		return cachedList(criteria);
	}

}
