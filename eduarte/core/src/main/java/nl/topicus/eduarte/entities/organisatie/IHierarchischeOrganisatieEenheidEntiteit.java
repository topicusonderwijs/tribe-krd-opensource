package nl.topicus.eduarte.entities.organisatie;

/**
 * Interface voor entiteiten die in de organisatie-eenheden hierarchie zijn ingeordend.
 * 
 * @author loite
 */
public interface IHierarchischeOrganisatieEenheidEntiteit
{
	/**
	 * @return De organisatie-eenheid van de entiteit
	 */
	public OrganisatieEenheid getOrganisatieEenheid();

	/**
	 * @return De naam van de entiteit
	 */
	public String getVergelijkingsNaam();
}
