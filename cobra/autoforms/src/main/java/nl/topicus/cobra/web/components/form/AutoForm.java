package nl.topicus.cobra.web.components.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.Component;

/**
 * De {@code AutoForm} annotation is bedoeld om de standaard instellingen van
 * {@link AutoFieldSet} aan te passen. Alle velden van deze annotation zijn optioneel.
 * Voor een uitgebreide beschrijving van het standaard gedrag, zie {@link AutoFieldSet}.
 * 
 * @author papegaaij
 */
@Target( {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoForm
{
	/**
	 * De standaard waarde voor alle strings.
	 */
	public static final String DEFAULT = "DEFAULT";

	/**
	 * Geeft aan of een property meegenomen moet worden (de default) of niet.
	 */
	boolean include() default true;

	/**
	 * Kan gebruikt worden om de volgorde van properties aan te passen. Standaard wordt de
	 * index van de property in de class gebruikt.
	 */
	int order() default Integer.MAX_VALUE;

	/**
	 * Geeft aan of een property wel of niet (de default) verplicht is.
	 */
	boolean required() default false;

	/**
	 * Geeft een of meer HTML classes die aan het component gehangen moeten worden.
	 */
	String[] htmlClasses() default {};

	/**
	 * Bepaalt de class van het component dat gebruikt moet worden voor de editor.
	 */
	Class< ? extends Component> editorClass() default Component.class;

	/**
	 * Bepaalt de container die gebruikt moet worden voor de editor.
	 */
	String editorContainer() default DEFAULT;

	/**
	 * Bepaalt de class van het component dat gebruikt moet worden voor de weergave.
	 */
	Class< ? extends Component> displayClass() default Component.class;

	/**
	 * Bepaalt de container die gebruikt moet worden voor de weergave.
	 */
	String displayContainer() default DEFAULT;

	/**
	 * Geeft een aangepast label aan de property.
	 */
	String label() default DEFAULT;

	/**
	 * Geeft een beschrijving van de property.
	 */
	String description() default "";

	/**
	 * Bepaalt of het veld bewerkt kan worden als de field set in {@link RenderMode#EDIT}
	 * getoond wordt.
	 */
	boolean readOnly() default false;

	/**
	 * Lijst van validatoren die voor deze variabele moeten gelden.
	 */
	AutoFormValidator[] validators() default {};
}
