/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.PersistentJobSchedule;
import nl.topicus.eduarte.jobs.EduArteJob;

public interface JobScheduleDataAccessHelper extends BatchDataAccessHelper<PersistentJobSchedule>
{
	public PersistentJobSchedule getSchedule(Class< ? extends EduArteJob> type);

	public List<PersistentJobSchedule> listSchedules(Class< ? extends EduArteJob> type);

	public List<PersistentJobSchedule> listEnabledSchedulesForAllInstellingen();

	public PersistentJobSchedule createJobSchedule(Class< ? extends EduArteJob> type);
}
