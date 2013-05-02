package nl.topicus.cobra.web.components.form;

/**
 * {@code FieldContainerType} bevat een aantal standaard field containers. Aan de hand van
 * het container type wordt door {@link FieldMarkupRenderer} de markup geleverd die het
 * input veld bevat. Deze markup bestaat uit standaard markup voor het onder elkaar zetten
 * van de veld, het renderen van een label en het input veld zelf. Een implementatie van
 * {@link FieldMarkupRenderer} wordt geacht tenminste deze set te ondersteunen.
 * 
 * @author papegaaij
 */
public class FieldContainerType
{
	/**
	 * Levert de markup voor een tekst veld: {@code <input type="text">}
	 */
	public static final String INPUT_TEXT = "INPUT_TEXT";

	/**
	 * Levert de markup voor een password veld: {@code <input type="password">}
	 */
	public static final String INPUT_PASSWORD = "INPUT_PASSWORD";

	/**
	 * Levert de markup voor een check box: {@code <input type="checkbox">}
	 */
	public static final String INPUT_CHECKBOX = "INPUT_CHECKBOX";

	/**
	 * Levert de markup voor een file input: {@code <input type="file">}
	 */
	public static final String INPUT_FILE = "INPUT_FILE";

	/**
	 * Levert de markup voor een textarea: {@code <textarea>}
	 */
	public static final String TEXTAREA = "TEXTAREA";

	/**
	 * Levert de markup voor een drop down: {@code <select>}
	 */
	public static final String SELECT = "SELECT";

	/**
	 * Levert de markup voor een label: {@code <span>}
	 */
	public static final String LABEL = "LABEL";

	/**
	 * Levert de markup voor een panel: {@code <div>}
	 */
	public static final String DIV = "DIV";
}
