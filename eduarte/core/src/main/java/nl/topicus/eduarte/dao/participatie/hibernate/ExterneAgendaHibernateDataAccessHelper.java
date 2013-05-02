package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.HibernateRequestCycle;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.participatie.helpers.CacheRegionDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneAgendaDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.ExterneAgendaKoppelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.CacheRegion;
import nl.topicus.eduarte.entities.participatie.ExterneAgenda;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaException;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;
import nl.topicus.eduarte.entities.participatie.enums.ExterneAgendaConnection;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

public class ExterneAgendaHibernateDataAccessHelper extends
		HibernateDataAccessHelper<ExterneAgenda> implements ExterneAgendaDataAccessHelper
{

	public ExterneAgendaHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	public List<ExterneAgenda> list(List<Persoon> personen)
	{
		if (personen.isEmpty())
			return Collections.emptyList();

		List<Long> persoonIds = new ArrayList<Long>(personen.size());
		for (Persoon curPersoon : personen)
		{
			persoonIds.add(curPersoon.getId());
		}
		boolean flushRequired = false;
		for (ExterneAgendaKoppeling curKoppeling : DataAccessRegistry.getHelper(
			ExterneAgendaKoppelingDataAccessHelper.class).listAutomatischeKoppelingen())
		{
			Criteria persoonCriteria = createCriteria(Persoon.class);
			CriteriaBuilder builder = new CriteriaBuilder(persoonCriteria);
			DetachedCriteria zonderAgendaCriteria = createDetachedCriteria(ExterneAgenda.class);
			zonderAgendaCriteria.add(Restrictions.eq("koppeling", curKoppeling));
			zonderAgendaCriteria.setProjection(Projections.property("eigenaar"));
			builder.addIn("id", persoonIds);
			persoonCriteria.add(Subqueries.propertyNotIn("id", zonderAgendaCriteria));
			List<Persoon> personenZonderAgenda = uncachedList(persoonCriteria);
			for (Persoon curPersoon : personenZonderAgenda)
			{
				ExterneAgenda nieuweAgenda = new ExterneAgenda();
				nieuweAgenda.setEigenaar(curPersoon);
				nieuweAgenda.setKoppeling(curKoppeling);
				nieuweAgenda.setNaam(curKoppeling.getNaam() + " (automatisch)");
				nieuweAgenda.save();
				flushRequired = true;
			}
		}
		if (flushRequired)
			flush();

		Criteria criteria = createCriteria(ExterneAgenda.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addIn("eigenaar", personen);
		return cachedTypedList(criteria);
	}

	@Override
	public List<ExterneAgenda> list(Persoon persoon)
	{
		Criteria criteria = createCriteria(ExterneAgenda.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("eigenaar", persoon);
		return cachedTypedList(criteria);
	}

	public void update(ExterneAgenda agenda, Date startDate, Date endDate)
			throws ExterneAgendaException
	{
		CacheRegionDataAccessHelper cacheHelper =
			DataAccessRegistry.getHelper(CacheRegionDataAccessHelper.class);
		List<CacheRegion> regions = cacheHelper.listAndCreate(agenda, startDate, endDate);
		ExterneAgendaConnection connection = null;
		for (CacheRegion curRegion : regions)
		{
			if (!curRegion.isValid())
			{
				cacheHelper.clear(curRegion);
				if (agenda.getKoppeling().isActief())
				{
					if (connection == null)
						connection = agenda.getKoppeling().connect(agenda);
					connection.fetch(curRegion);
				}
				curRegion.setLastUpdate(new Date());
				curRegion.setDirty(false);
				curRegion.saveOrUpdate();
			}
		}
	}

	@Override
	public List<ExterneAgendaException> update(List<Persoon> personen, Date startDate, Date endDate)
	{
		List<ExterneAgendaException> exceptions = new ArrayList<ExterneAgendaException>();
		// geen request cycle beschikbaar in jobs, dus lukt dit trucje niet, dan maar niet
		// de externe agenda's updaten
		if (HibernateRequestCycle.get() != null)
		{
			HibernateRequestCycle.get().openAndPushHibernateSession();
			try
			{
				for (ExterneAgenda curAgenda : list(personen))
				{
					try
					{
						update(curAgenda, startDate, endDate);
					}
					catch (ExterneAgendaException e)
					{
						exceptions.add(e);
					}
				}
				batchExecute();
			}
			finally
			{
				HibernateRequestCycle.get().closeAndPopHibernateSession();
			}
		}
		return exceptions;
	}
}
