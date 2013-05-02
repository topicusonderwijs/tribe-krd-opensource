package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.incident.IrisKoppelingKey;

public interface IrisKoppelingKeyDataAccessHelper extends BatchDataAccessHelper<IrisKoppelingKey>
{
	IrisKoppelingKey getKey();
}
