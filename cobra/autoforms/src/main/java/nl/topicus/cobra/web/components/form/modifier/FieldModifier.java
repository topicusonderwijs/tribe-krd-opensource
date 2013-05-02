package nl.topicus.cobra.web.components.form.modifier;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Een FieldModifier definieert aangepast gedrag voor 1 of meer velden in een AutoForm.
 * Een FieldModifier geeft aan op welke velden en operaties hij van toepassing is mbv de
 * {@link #isApplicable(AutoFieldSet, Property, Action)} methode. Een lege base class
 * wordt gegeven door {@link FieldAdapter}. Voor FieldModifiers die slechts 1 operatie op
 * 1 property aanpassen, is er {@link SingleFieldAdapter}. Verder zijn er een aantal
 * standaard implementaties voor veelgebruikte aanpassingen.
 * 
 * @author papegaaij
 */
public interface FieldModifier extends Serializable, IDetachable
{
	/**
	 * Operaties waarvoor aangepast gedrag gedefinieerd kan worden.
	 * 
	 * @author papegaaij
	 */
	public enum Action
	{
		/**
		 * Verzameling van de velden
		 */
		PROPERTY_COLLECTION,

		/**
		 * Aanwezigheid van de velden
		 */
		INCLUSION,

		/**
		 * Sortering van de velden
		 */
		ORDER,

		/**
		 * Verplichtheid van de velden
		 */
		REQUIRED,

		/**
		 * Extra html classes voor de velden
		 */
		HTML_CLASSES,

		/**
		 * De class van de velden
		 */
		FIELD_CLASS,

		/**
		 * De container voor de velden
		 */
		FIELD_CONTAINER,

		/**
		 * Het label voor de velden
		 */
		LABEL,

		/**
		 * De beschrijving voor de velden
		 */
		DESCRIPTION,

		/**
		 * De render mode voor de velden
		 */
		RENDER_MODE,

		/**
		 * Het model voor de velden
		 */
		MODEL,

		/**
		 * Het creeren van de velden
		 */
		CREATION,

		/**
		 * Nabewerken van de velden
		 */
		POST_PROCESS,

		/**
		 * Zichtbaarheid van de velden
		 */
		VISIBILITY,

		/**
		 * De (form)validatoren van de velden
		 */
		VALIDATOR;
	}

	public <T> void bind(AutoFieldSet<T> fieldSet);

	/**
	 * Geeft aan of deze FieldModifier van toepassing is op de gegeven property en
	 * operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property die gecontroleerd wordt.
	 * @param action
	 *            De operatie die uitgevoerd gaat worden.
	 * @return True als deze FieldModifier aangepast gedrag definieerd voor de gegeven
	 *         property en operatie combinatie.
	 */
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action);

	/**
	 * Definieert welke properties uit de class gebruikt moeten worden. Er kunnen
	 * properties toegevoegd of verwijderd worden. Deze actie wordt uitgevoerd op een
	 * 'null' property. Dit komt overeen met de {@link Action#PROPERTY_COLLECTION}
	 * operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @return De lijst met properties die gebruikt moeten worden.
	 */
	public <T> List<Property<T, ? , ? >> collectProperties(AutoFieldSet<T> fieldSet,
			List<Property<T, ? , ? >> properties);

	/**
	 * Definieert de aanwezigheid van de property. Dit komt overeen met de
	 * {@link Action#INCLUSION} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @return True als de property aanwezig moet zijn, anders false.
	 */
	public <T> boolean isIncluded(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property);

	/**
	 * Definieert de zichtbaarheid van de property. Dit komt overeen met de
	 * {@link Action#VISIBILITY} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return Een model met true als de property zichtbaar moet zijn.
	 */
	public <T> IModel<Boolean> getVisibility(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de volgorde index van de property. Dit komt overeen met de
	 * {@link Action#ORDER} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param index
	 *            De index van de property in de class.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return De volgorde index voor de property.
	 */
	public <T> int getOrder(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property, int index,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de verplichtheid van de property. Dit komt overeen met de
	 * {@link Action#REQUIRED} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return True als de property verplicht is, anders false.
	 */
	public <T> boolean isRequired(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert extra HTML classes voor de property. Dit komt overeen met de
	 * {@link Action#HTML_CLASSES} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return Een lijst met extra HTML classes.
	 */
	public <T> List<String> getHtmlClasses(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert het veld type voor de property. Dit komt overeen met de
	 * {@link Action#FIELD_CLASS} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return Het veld type voor de property.
	 */
	public <T> Class< ? extends Component> getFieldType(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de veld container voor de property. Dit komt overeen met de
	 * {@link Action#FIELD_CONTAINER} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return De veld container voor de property.
	 */
	public <T> String getFieldContainer(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de lijst met {@link AutoFormValidator}s voor de property. Dit komt
	 * overeen met de {@link Action#VALIDATOR} operatie.
	 */
	public <T> IModel<AutoFormValidator[]> getValidators(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert het label voor de property. Dit komt overeen met de {@link Action#LABEL}
	 * operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return Het label voor de property.
	 */
	public <T> IModel<String> getLabel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de beschrijving voor de property. Dit komt overeen met de
	 * {@link Action#DESCRIPTION} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return De beschrijving voor de property.
	 */
	public <T> IModel<String> getDescription(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Definieert de render modus voor de property. Dit komt overeen met de
	 * {@link Action#RENDER_MODE} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} tot nu toe.
	 * @return De render modus voor de property.
	 */
	public <T> RenderMode getRenderMode(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Instantieert het model voor de property. Dit komt overeen met de
	 * {@link Action#MODEL} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} voor de property.
	 * @return Het model voor de property.
	 */
	public <T> IModel< ? > createModel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Instantieert het veld voor de property. Dit komt overeen met de
	 * {@link Action#CREATION} operatie.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param id
	 *            Het id van het veld.
	 * @param model
	 *            Het model voor het veld.
	 * @param property
	 *            De property.
	 * @param fieldProperties
	 *            De {@link FieldProperties} voor de property.
	 * @return Het veld voor de property.
	 */
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties);

	/**
	 * Bewerkt het veld voor de property na. Dit komt overeen met de
	 * {@link Action#POST_PROCESS} operatie. Nadat alle components verwerkt zijn, wordt
	 * deze methode met null aan geroepen voor een postproces van de AutoFieldSet.
	 * 
	 * @param fieldSet
	 *            De AutoFieldSet.
	 * @param field
	 *            Het veld.
	 * @param fieldProperties
	 *            De {@link FieldProperties} voor de property.
	 */
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties);
}
