package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public interface LesuurIndelingDataAccessHelper extends BatchDataAccessHelper<LesuurIndeling>

{
	public List<LesuurIndeling> getAlleLesuurIndelingen();

	public List<LesuurIndeling> getLesuurIndelingen(Locatie locatie,
			OrganisatieEenheid organisatieEenheid);

	public List<LesuurIndeling> getLesuurIndelingen(OrganisatieEenheid organisatieEenheid);

	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur);

	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur,
			OrganisatieEenheid organisatieEenheid);

	public LesuurIndeling getLesTijd(Deelnemer deelnemer, Date datum, int lesuur, Locatie locatie,
			OrganisatieEenheid orgEenheid);
}
