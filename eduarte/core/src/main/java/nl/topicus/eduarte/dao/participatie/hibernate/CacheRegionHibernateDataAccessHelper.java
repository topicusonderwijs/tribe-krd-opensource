package nl.topicus.eduarte.dao.participatie.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.CacheRegionDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakBijlage;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.CacheRegion;
import nl.topicus.eduarte.entities.participatie.ExterneAgenda;

import org.hibernate.Criteria;

public class CacheRegionHibernateDataAccessHelper extends HibernateDataAccessHelper<CacheRegion>
		implements CacheRegionDataAccessHelper
{

	public CacheRegionHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public void markDirty(ExterneAgenda agenda)
	{
		for (CacheRegion curRegion : agenda.getCacheRegions())
		{
			curRegion.setDirty(true);
			curRegion.update();
		}
		agenda.commit();
	}

	@Override
	public List<CacheRegion> list(ExterneAgenda agenda, Date startDate, Date endDate)
	{
		Criteria criteria = createCriteria(CacheRegion.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("externeAgenda", agenda);
		builder.addGreaterThan("regionEndDate", startDate);
		builder.addLessThan("regionStartDate", endDate);
		return cachedList(criteria);
	}

	@Override
	public List<CacheRegion> listAndCreate(ExterneAgenda agenda, Date startDate, Date endDate)
	{
		List<CacheRegion> ret = new ArrayList<CacheRegion>();
		List<CacheRegion> inDb = list(agenda, startDate, endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(TimeUtil.getInstance().asDate(startDate));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		while (cal.getTime().compareTo(endDate) < 0)
		{
			Date curStartDate = cal.getTime();
			CacheRegion region = null;
			Iterator<CacheRegion> it = inDb.iterator();
			while (it.hasNext())
			{
				CacheRegion curRegion = it.next();
				if (curRegion.getRegionStartDate().equals(curStartDate))
				{
					region = curRegion;
					it.remove();
				}
			}
			if (region == null)
			{
				region = new CacheRegion();
				region.setExterneAgenda(agenda);
				region.setDirty(true);
				region.setRegionStartDate(curStartDate);
				region.setLastUpdate(curStartDate);
				region.setRegionEndDate(TimeUtil.getInstance().addMonths(curStartDate, 1));
			}
			ret.add(region);
			cal.add(Calendar.MONTH, 1);
		}
		return ret;
	}

	@Override
	public void clear(CacheRegion region)
	{
		for (Afspraak curAfspraak : region.getAfspraken())
		{
			for (AfspraakParticipant curParticipant : curAfspraak.getParticipanten())
			{
				curParticipant.delete();
				if (curParticipant.getExterne() != null)
				{
					curParticipant.getExterne().delete();
				}
			}
			for (AfspraakBijlage curBijlage : curAfspraak.getBijlagen())
			{
				curBijlage.getBijlage().delete();
				curBijlage.delete();
			}
			curAfspraak.delete();
		}
		region.setAfspraken(new ArrayList<Afspraak>());
	}
}
