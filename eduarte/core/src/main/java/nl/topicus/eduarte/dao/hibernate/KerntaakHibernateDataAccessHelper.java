package nl.topicus.eduarte.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.KerntaakDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.GroepsbeoordelingOverschrijving;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Kerntaak;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * 
 * @author vanharen
 */
public class KerntaakHibernateDataAccessHelper extends HibernateDataAccessHelper<Kerntaak>
		implements KerntaakDataAccessHelper
{

	public KerntaakHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<Kerntaak> getKerntaken(CompetentieNiveauVerzameling beoordeling)
	{
		Criteria criteria = createCriteria(CompetentieNiveau.class, "competentieNiveau");

		if (beoordeling instanceof GroepsbeoordelingOverschrijving)
		{
			criteria.add(Restrictions.or(Restrictions.eq("niveauVerzameling", beoordeling),
				Restrictions.eq("niveauVerzameling",
					((GroepsbeoordelingOverschrijving) beoordeling).getGroepsBeoordeling())));
		}
		else
		{
			criteria.add(Restrictions.eq("niveauVerzameling", beoordeling));
		}
		criteria.createAlias("competentieNiveau.leerpunt", "leerpunt");
		criteria.createAlias("leerpunt.werkproces", "werkproces");
		criteria.createAlias("werkproces.kerntaak", "kerntaak");
		criteria.setProjection(Projections.distinct(Projections.property("werkproces.kerntaak")));

		List<Kerntaak> ret = new ArrayList<Kerntaak>(cachedTypedList(criteria));
		Collections.sort(ret, new Comparator<Kerntaak>()
		{
			@Override
			public int compare(Kerntaak o1, Kerntaak o2)
			{
				return o1.equals(o2) ? 0 : o1.getVolgnummer() - o2.getVolgnummer();
			}
		});
		return ret;
	}
}
