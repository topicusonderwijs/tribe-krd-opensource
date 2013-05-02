package nl.topicus.cobra.web.components.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * De {@code AutoFormEmbedded} annotation is bedoeld om een de standaard instellingen van
 * {@link AutoFieldSet} aan te passen. Alle velden van deze annotation zijn optioneel.
 * Wanneer deze annotation gebruikt wordt zullen alle properties uit de class waar deze
 * property naar wijst worden meegenomen in de AutoForm. Voor een uitgebreide beschrijving
 * van het standaard gedrag, zie {@link AutoFieldSet}.
 * 
 * @author hoeve
 */
@Target( {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFormEmbedded
{
	/**
	 * De standaard waarde voor alle strings.
	 */
	public static final String DEFAULT = "DEFAULT";

	/**
	 * Geeft aan welke properties mee mogen worden genomen via deze annotation. Dit
	 * overschrijft alle AutoForm annotations die in de class van deze property bestaan.
	 */
	String[] includeProperties() default {};
}
