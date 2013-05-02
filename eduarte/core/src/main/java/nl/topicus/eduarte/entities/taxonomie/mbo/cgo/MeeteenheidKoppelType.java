package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

/**
 * De verschillende manieren waarop een meeteenheid aan een organisatie-eenheid of
 * onderwijsaanbod gekoppeld kan zijn.
 * 
 * @author vandenbrink
 */
public enum MeeteenheidKoppelType
{
	/**
	 * Algemene meeteenheid voor competenties
	 */
	Algemeen("Opleidingsmatrix"),

	/**
	 * Meeteenheid voor de Leren, Loopbaan en Burgerschap matrix
	 */
	LLB("LLB matrix"),

	/**
	 * Meeteenheid voor moderne talen
	 */
	Taal("Taal"),

	/**
	 * Meeteenheid voor in de vrije matrix
	 */
	Vrij("Vrije matrix");

	private String displayName;

	private MeeteenheidKoppelType(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
