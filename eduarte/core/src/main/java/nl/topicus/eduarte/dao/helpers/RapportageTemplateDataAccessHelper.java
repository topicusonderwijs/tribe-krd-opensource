package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.RapportageTemplate;

public interface RapportageTemplateDataAccessHelper extends DataAccessHelper<RapportageTemplate>
{
	public List<RapportageTemplate> getTemplates(Medewerker medewerker);

	public void saveTemplate(RapportageTemplate template);

	public void deleteTemplate(RapportageTemplate template);

	public RapportageTemplate newTemplate(Medewerker medewerker);
}
