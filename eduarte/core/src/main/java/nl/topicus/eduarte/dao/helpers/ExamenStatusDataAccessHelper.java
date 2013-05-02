package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examenstatus;

/**
 * @author vandekamp
 */
public interface ExamenStatusDataAccessHelper extends BatchDataAccessHelper<Examenstatus>
{
	public Examenstatus getByNaam(String naam, ExamenWorkflow examenWorkflow);
}
