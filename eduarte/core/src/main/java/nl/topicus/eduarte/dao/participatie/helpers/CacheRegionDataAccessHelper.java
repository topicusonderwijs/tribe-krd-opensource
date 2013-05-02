package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.CacheRegion;
import nl.topicus.eduarte.entities.participatie.ExterneAgenda;

public interface CacheRegionDataAccessHelper extends BatchDataAccessHelper<CacheRegion>
{
	public void markDirty(ExterneAgenda agenda);

	public List<CacheRegion> list(ExterneAgenda agenda, Date startDate, Date endDate);

	public List<CacheRegion> listAndCreate(ExterneAgenda agenda, Date startDate, Date endDate);

	public void clear(CacheRegion curRegion);
}
