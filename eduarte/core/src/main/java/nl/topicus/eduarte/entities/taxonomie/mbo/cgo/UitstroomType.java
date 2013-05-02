package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

/**
 * @author vandenbrink
 */
public enum UitstroomType
{
	/**
	 * Een gewone uitstroom.
	 */
	StandaardUitstroom("Standaard uitstroom"),

	/**
	 * Uitstroom die eigenlijk het dossier zelf representeert als "algemene uitstroom".
	 * Deelnemers zouden deze bijv. kunnen volgen alvorens een van de specifiekere
	 * standaard uitstromen bij het dossier te volgen. De dossieruitstroom wordt
	 * aangemaakt door de matrices van alle uitstromen bij het dossier over elkaar te
	 * leggen.
	 */
	DossierUitstroom("Dossier uitstroom");

	private String displayName;

	private UitstroomType(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
