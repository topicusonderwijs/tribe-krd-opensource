package nl.topicus.cobra.web.components.form;

/**
 * De RenderMode van een {@link AutoFieldSet} of property bepaald of de property (of
 * properties) bewerkt kunnen worden or read-only zijn.
 * 
 * @author papegaaij
 */
public enum RenderMode
{
	/**
	 * Het component kan bewerkt worden.
	 */
	EDIT,

	/**
	 * Het component is read-only.
	 */
	DISPLAY;
}
