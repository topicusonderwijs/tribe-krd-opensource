package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.ExterneAgenda;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaException;
import nl.topicus.eduarte.entities.personen.Persoon;

public interface ExterneAgendaDataAccessHelper extends BatchDataAccessHelper<ExterneAgenda>
{
	public List<ExterneAgendaException> update(List<Persoon> personen, Date startDate, Date endDate);

	public List<ExterneAgenda> list(Persoon persoon);
}
