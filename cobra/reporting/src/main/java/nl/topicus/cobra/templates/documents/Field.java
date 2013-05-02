package nl.topicus.cobra.templates.documents;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * @author Laurens Hop
 */
public interface Field
{
	public abstract String getType();

	public abstract String getName();

	/**
	 * Probeert het veld te substitueren door data uit de aangegeven context. Indien dit
	 * veld van type MERGEFIELD is, wordt geprobeerd een object te vinden gedefinieerd
	 * door de naam van dit veld, gebruikmakend van BeanUtils syntax (bijv.
	 * <code>deelnemer.persoon.volledigeNaam</code> ). Als dit een ander field type is,
	 * wordt een object gezocht gedefinieerd door het type van dit field (bijv.
	 * <code>AUTHOR</code>).
	 * 
	 * @param resolver
	 *            FieldResolver die de velden gaat opzoeken
	 */
	public abstract void merge(FieldResolver resolver) throws TemplateException;

	public abstract Object getMergedResult();

	/**
	 * Gebruikt de fieldresolver om informatie over dit veld te verkrijgen
	 * 
	 * @param resolver
	 * @return nieuw FieldInfo record
	 */
	public abstract FieldInfo getFieldInfo(FieldResolver resolver);

}
