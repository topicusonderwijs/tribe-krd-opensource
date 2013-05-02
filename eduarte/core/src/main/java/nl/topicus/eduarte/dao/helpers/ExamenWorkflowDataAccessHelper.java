package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;

/**
 * @author vandekamp
 */
public interface ExamenWorkflowDataAccessHelper extends BatchDataAccessHelper<ExamenWorkflow>
{

	/**
	 * @return Een lijst met alle landelijk + instellingspecifieke examenworkflows
	 */
	public List<ExamenWorkflow> list();

	public ExamenWorkflow getByNaam(String naam);
}
