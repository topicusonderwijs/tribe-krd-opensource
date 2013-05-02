package nl.topicus.cobra.web.components.form;

import java.util.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.MethodNotFoundException;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.form.modifier.FieldModifier.Action;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code AutoFieldSet} rendert een field set voor properties van een entiteit. Een field
 * set bestaat uit een caption (of legend) met een aantal form fields. {@code
 * AutoFieldSet} probeert zelf te bepalen welk type input veld gebruikt moet worden voor
 * de velden. Voor het instellen van {@code AutoFieldSet} kan de {@link AutoForm}
 * annotation gebruikt worden. Het is mogelijk om mbv {@link FieldModifier}s het gedrag
 * aan te passen. Bijna alle methodes in deze class zijn protected, en kunnen overschreven
 * worden in een subclass voor aangepast gedrag.
 * 
 * @author papegaaij
 */
public class AutoFieldSet<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * AutoForm logger
	 */
	public static final Logger LOG = LoggerFactory.getLogger(AutoFieldSet.class);

	private String caption;

	private List<String> propertyNames;

	private SortedSet<FieldProperties<T, ? , ? >> fieldProperties;

	private RepeatingView inputFields;

	private RenderMode renderMode = RenderMode.DISPLAY;

	private List<String> htmlClasses;

	private List<FieldModifier> fieldModifiers = new ArrayList<FieldModifier>();

	private boolean sortAccordingToPropertyNames = false;

	private String markupRendererName = AutoFormRegistry.DEFAULT_MARKUP_RENDERER;

	/**
	 * Maakt een nieuwe {@code AutoFieldSet}.
	 * 
	 * @param id
	 *            Het id van de field set.
	 * @param model
	 *            Het model waar de fields onderdeel van zijn.
	 */
	public AutoFieldSet(String id, IModel<T> model)
	{
		this(id, model, null);
	}

	/**
	 * Maakt een nieuwe {@code AutoFieldSet}.
	 * 
	 * @param id
	 *            Het id van de field set.
	 * @param model
	 *            Het model waar de fields onderdeel van zijn.
	 * @param caption
	 *            De caption die boven de field set komt te staan, mag null zijn.
	 */
	public AutoFieldSet(String id, IModel<T> model, String caption)
	{
		super(id, model);
		this.caption = caption;
	}

	public Collection<String> getPropertyNames()
	{
		return propertyNames;
	}

	public void setPropertyNames(Collection<String> propertyNames)
	{
		this.propertyNames = new ArrayList<String>(propertyNames);
	}

	public void setPropertyNames(String... propertyNames)
	{
		if (propertyNames.length > 0)
			this.propertyNames = Arrays.asList(propertyNames);
		else
			this.propertyNames = null;
	}

	/**
	 * Voegt de propertyName toe aan de huidige lijst.
	 * 
	 * @param propertyName
	 */
	public void addPropertyName(String propertyName)
	{
		this.propertyNames.add(propertyName);
	}

	public RenderMode getRenderMode()
	{
		return renderMode;
	}

	public void setRenderMode(RenderMode renderMode)
	{
		this.renderMode = renderMode;
	}

	public String getMarkupRendererName()
	{
		return markupRendererName;
	}

	public void setMarkupRendererName(String markupRendererName)
	{
		this.markupRendererName = markupRendererName;
	}

	public List<FieldModifier> getFieldModifiers()
	{
		return fieldModifiers;
	}

	public void setFieldModifiers(List<FieldModifier> fieldModifiers)
	{
		this.fieldModifiers = fieldModifiers;
		for (FieldModifier curModifier : fieldModifiers)
		{
			curModifier.bind(this);
		}
	}

	public void setFieldModifiers(FieldModifier... fieldModifiers)
	{
		setFieldModifiers(Arrays.asList(fieldModifiers));
	}

	/**
	 * Voegt de modifier toe aan de huidige lijst.
	 * 
	 * @param fieldModifier
	 */
	public void addFieldModifier(FieldModifier fieldModifier)
	{
		getFieldModifiers().add(fieldModifier);
		fieldModifier.bind(this);
	}

	/**
	 * Voegt een {@link ValidateModifier} voor de validator toe aan de huidige lijst.
	 * 
	 * @param property
	 *            de property waaraan de validator gekoppeld moet worden
	 * @param validator
	 *            de validator
	 */
	public void addModifier(String property, IValidator< ? > validator)
	{
		addFieldModifier(new ValidateModifier(validator, property));
	}

	/**
	 * Voegt een {@link BehaviorModifier} voor de behavior toe aan de huidige lijst.
	 * 
	 * @param property
	 *            de property waaraan de behavior gekoppeld moet worden
	 * @param behavior
	 *            de behavior
	 */
	public void addModifier(String property, IBehavior behavior)
	{
		addFieldModifier(new BehaviorModifier(behavior, property));
	}

	public void setHtmlClasses(List<String> htmlClasses)
	{
		this.htmlClasses = htmlClasses;
	}

	public void setHtmlClasses(String... htmlClasses)
	{
		if (htmlClasses.length > 0)
			this.htmlClasses = Arrays.asList(htmlClasses);
		else
			this.htmlClasses = null;
	}

	public FieldProperties< ? , ? , ? > getFieldProperties(String property)
	{
		if (fieldProperties == null)
			throw new IllegalStateException(
				"Cannot use getFieldProperties before onBeforeRender is called");
		for (FieldProperties< ? , ? , ? > curFieldProps : fieldProperties)
		{
			if (curFieldProps.getPropertyName().equals(property))
				return curFieldProps;
		}
		throw new IllegalArgumentException("Field not found: " + property);
	}

	/**
	 * Zoekt het component dat verantwoordelijk is voor de gegeven property. Let op: deze
	 * methode kan pas gebruikt worden in onBeforeRender.
	 * 
	 * @param property
	 *            De property die gezocht moet worden.
	 * @return Het component.
	 * @throws IllegalArgumentException
	 *             Als er geen field is met die naam.
	 */
	@SuppressWarnings("unchecked")
	public <Y extends Component> Y findFieldComponent(String property)
	{
		return (Y) getFieldProperties(property).getComponent();
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			onBeforeRenderMarkup();
			onBeforeRenderGather();
			onBeforeRenderApply();
		}
		super.onBeforeRender();
	}

	private void onBeforeRenderMarkup()
	{
		Panel fieldSetMarkup =
			AutoFormRegistry.getInstance().getMarkupRenderer(markupRendererName)
				.createFieldSetMarkup("fieldSetMarkup", this);
		fieldSetMarkup.setRenderBodyOnly(true);
		add(fieldSetMarkup);

		fieldSetMarkup.add(new Label("caption", new PropertyModel<String>(this, "caption"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getCaption() != null;
			}
		});

		fieldSetMarkup.add(inputFields = new RepeatingView("inputFields"));
	}

	/**
	 * Vergaar de informatie voor de onBeforeRender
	 */
	private void onBeforeRenderGather()
	{
		fieldProperties = new TreeSet<FieldProperties<T, ? , ? >>();
		int count = 0;
		for (Property<T, ? , ? > curProperty : determineProperties(propertyNames))
		{
			fieldProperties.add(getFieldProperties(curProperty, count++));
		}
	}

	/**
	 * Pas de informatie voor de onBeforeRender toe
	 */
	private void onBeforeRenderApply()
	{
		int count = 0;
		for (FieldProperties<T, ? , ? > curFieldProps : fieldProperties)
		{
			AbstractFieldContainer<T, ? , ? > container =
				createContainer(Integer.toString(count++), curFieldProps);
			inputFields.add(container);
			Component formField =
				createFormField("formField", curFieldProps, createModel(curFieldProps));
			curFieldProps.setComponent(formField);
			setFormFieldProperties(formField, curFieldProps);

			container.postProcessFormField(formField, this);
			for (String curHtmlClass : curFieldProps.getHtmlClasses())
				formField.add(new AttributeAppender("class", new Model<String>(curHtmlClass), " "));
			container.add(formField);

			for (FieldModifier postProcessModifier : getFieldModifiers(curFieldProps.getProperty(),
				Action.POST_PROCESS))
				postProcessModifier.postProcess(this, formField, curFieldProps);
		}

		postProcessValidators();

		for (FieldModifier postProcessModifier : getFieldModifiers(null, Action.POST_PROCESS))
			postProcessModifier.postProcess(this, null, null);
	}

	protected void postProcessValidators()
	{
		if (findForm() == null)
			return;

		// moet wel n^2 want alle fields moeten zijn aangemaakt.
		for (FieldProperties<T, ? , ? > curFieldProps : fieldProperties)
		{
			IModel<AutoFormValidator[]> validatorsModel = curFieldProps.getValidators();
			if (validatorsModel == null || validatorsModel.getObject() == null
				|| !(curFieldProps.getComponent() instanceof FormComponent< ? >))
				continue;

			AutoFormValidator[] curValidators = validatorsModel.getObject();

			for (AutoFormValidator curVal : curValidators)
			{
				if (curVal.formValidator() != null && !curVal.formValidator().isInterface())
					postProcessFormValidator(curVal, curFieldProps);
				else if (curVal.validator() != null && !curVal.validator().isInterface())
					postProcessValidator(curVal, curFieldProps);
				else
					LOG.info("Cannot instantiate AutoFormValidator for property "
						+ curFieldProps.getProperty().getName() + ": no class specified.");
			}
		}
	}

	protected void postProcessFormValidator(AutoFormValidator validator,
			FieldProperties<T, ? , ? > curFieldProps)
	{
		ArrayList<Object> args = new ArrayList<Object>(validator.otherProperties().length + 1);
		args.add(curFieldProps.getComponent());

		for (String otherProperty : validator.otherProperties())
		{
			try
			{
				Component otherComponent = this.findFieldComponent(otherProperty);
				args.add(otherComponent);
			}
			catch (IllegalArgumentException e)
			{
				LOG.info("property " + otherProperty + " niet gevonden.");
				// ignore error
			}
		}

		try
		{
			IFormValidator curValidator =
				ReflectionUtil.invokeConstructor(validator.formValidator(), args.toArray());

			findForm().add(curValidator);
		}
		catch (InvocationFailedException e)
		{
			LOG.info("constructor " + validator.validator().getName() + " met " + args.size()
				+ " argumenten niet gevonden.");
			// ignore error
		}
	}

	@SuppressWarnings("unchecked")
	protected void postProcessValidator(AutoFormValidator validator, FieldProperties curFieldProps)
	{
		ArrayList<Object> args = new ArrayList<Object>(validator.otherProperties().length + 1);
		args.add(curFieldProps.getComponent());
		for (String otherProperty : validator.otherProperties())
		{
			try
			{
				Component otherComponent = this.findFieldComponent(otherProperty);
				args.add(otherComponent);
			}
			catch (IllegalArgumentException e)
			{
				LOG.info("property " + otherProperty + " niet gevonden.");
				// ignore error
			}
		}

		try
		{
			IValidator curValidator =
				ReflectionUtil.invokeConstructor(validator.validator(), args.toArray());

			((FormComponent) curFieldProps.getComponent()).add(curValidator);
		}
		catch (InvocationFailedException e)
		{
			LOG.info("constructor " + validator.validator().getName() + " met " + args.size()
				+ " argumenten niet gevonden.");
			// ignore error
		}
	}

	@SuppressWarnings("unchecked")
	protected <Y> Form<Y> findForm()
	{
		return findParent(Form.class);
	}

	protected <Y, Z> FieldModifier getFieldModifier(Property<T, Y, Z> property, Action action)
	{
		List<FieldModifier> modifiers = getFieldModifiers(property, action);
		if (modifiers.isEmpty())
			return null;
		if (modifiers.size() > 1)
			throw new IllegalStateException("Two or more field modifiers are applicable for "
				+ action + " on " + property + ": " + modifiers);
		return modifiers.get(0);
	}

	private <Y, Z> List<FieldModifier> getFieldModifiers(Property<T, Y, Z> property, Action action)
	{
		List<FieldModifier> ret = new ArrayList<FieldModifier>();
		for (FieldModifier curModifier : fieldModifiers)
		{
			if (curModifier.isApplicable(this, property, action))
				ret.add(curModifier);
		}
		return ret;
	}

	/**
	 * Bepaald de class van het object waar de AutoFieldSet voor gerenderd dient te
	 * worden.
	 * 
	 * @return De class van het object.
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> determineClass()
	{
		for (ClassResolver curResolver : AutoFormRegistry.getInstance().getClassResolvers())
		{
			Class<T> clazz = (Class<T>) curResolver.resolveClass(getDefaultModelObject());
			if (clazz != null)
				return clazz;
		}
		throw new IllegalStateException(
			"Non of the ClassResolvers was able to resolve the class of " + getDefaultModelObject());
	}

	/**
	 * Zoekt de properties die getoont moeten worden.
	 * 
	 * @param propertyNames
	 *            De namen van de properties, zoals die meegegeven zijn aan de
	 *            constructor. Dit kan null zijn.
	 * @return De lijst met properties die getoont moeten worden.
	 */
	@SuppressWarnings("hiding")
	protected List<Property<T, ? , ? >> determineProperties(Collection<String> propertyNames)
	{
		List<Property<T, ? , ? >> ret = determineProperties(null, propertyNames);
		for (FieldModifier curModifier : getFieldModifiers(null, Action.PROPERTY_COLLECTION))
			ret = curModifier.collectProperties(this, ret);
		return ret;
	}

	/**
	 * Zoekt de properties die getoont moeten worden met een andere Property als
	 * uitgangspunt.
	 * 
	 * @param baseProperty
	 *            wanneer null dan neemt men het modelObject van deze AutoFieldSet.
	 * @param propertyNames
	 *            De namen van de properties, zoals die meegegeven zijn aan de
	 *            constructor. Dit kan null zijn.
	 * @return De lijst met properties die getoont moeten worden.
	 */
	@SuppressWarnings("hiding")
	protected List<Property<T, ? , ? >> determineProperties(Property<T, ? , ? > baseProperty,
			Collection<String> propertyNames)
	{
		List<Property<T, ? , ? >> endResultProperties = new ArrayList<Property<T, ? , ? >>();
		List<Property<T, ? , ? >> properties;

		if (baseProperty != null)
			properties = ReflectionUtil.findProperties(baseProperty);
		else
			properties = ReflectionUtil.findProperties(determineClass());

		Iterator<Property<T, ? , ? >> it = properties.iterator();
		while (it.hasNext())
		{
			Property<T, ? , ? > curProperty = it.next();
			// check of de huidige property is gemarkeerd als AutoFormEmbedded, zoja dan
			// neem zijn properties ook mee.
			AutoFormEmbedded autoFormEmbedded = curProperty.getAnnotation(AutoFormEmbedded.class);
			if (autoFormEmbedded != null)
			{
				Collection<String> embeddedPropertyNames = propertyNames;
				if (propertyNames == null && autoFormEmbedded.includeProperties().length > 0)
				{
					embeddedPropertyNames = new ArrayList<String>();
					for (String curEmbedded : autoFormEmbedded.includeProperties())
						embeddedPropertyNames.add(curProperty.getPath() + "." + curEmbedded);
				}

				List<Property<T, ? , ? >> subProperties =
					determineProperties(curProperty, embeddedPropertyNames);
				endResultProperties.addAll(subProperties);
				it.remove();
			}
			// zo niet dan is het een 'gewone' property en check hem.
			else if (propertyNames == null && !isPropertyIncluded(curProperty))
				it.remove();
			else if (propertyNames != null && !propertyNames.contains(curProperty.getPath()))
				it.remove();
		}

		endResultProperties.addAll(properties);
		return endResultProperties;
	}

	/**
	 * Bepaald of de gegeven property meegenomen moet worden. Standaard wordt een property
	 * niet meegenomen als hij de {@code Version}, {@code Transient} of {@code Id}
	 * annotation heeft. Verder worden properties die geen instance field hebben standaard
	 * niet meegenomen.
	 * 
	 * @param property
	 *            De property.
	 * @return True als de property getoont moet worden, anders false.
	 */
	protected boolean isPropertyIncluded(Property<T, ? , ? > property)
	{
		if (property.isAnnotationPresent(Version.class)
			|| property.isAnnotationPresent(Transient.class)
			|| property.isAnnotationPresent(Id.class))
			return false;

		FieldModifier modifier = getFieldModifier(property, Action.INCLUSION);
		if (modifier != null)
			return modifier.isIncluded(this, property);

		AutoForm autoForm = property.getAnnotation(AutoForm.class);
		if (autoForm == null)
		{
			return property.getField() != null;
		}
		return autoForm.include();
	}

	/**
	 * Bepaalt alle instellingen voor de gegeven property. De instellingen worden in de
	 * volgende volgorde bepaald:
	 * {@linkplain #determineVisibility(Property, AutoForm, FieldProperties) visibility},
	 * {@linkplain #determineRenderMode(Property, AutoForm, FieldProperties) render mode},
	 * {@linkplain #determineOrder(Property, AutoForm, int, FieldProperties) volgorde},
	 * {@linkplain #determineLabel(Property, AutoForm, FieldProperties) label},
	 * {@linkplain #determineDescription(Property, AutoForm, FieldProperties)
	 * beschrijving}, {@linkplain #determineRequired(Property, AutoForm, FieldProperties)
	 * verplichtheid},
	 * {@linkplain #determineHtmlClasses(Property, AutoForm, FieldProperties) extra html
	 * classes}, {@linkplain #determineFieldType(Property, AutoForm, FieldProperties) veld
	 * type}, {@linkplain #determineContainerType(Property, AutoForm, FieldProperties)
	 * container type},
	 * {@linkplain #determineValidators(Property, AutoForm, FieldProperties) container
	 * type}.
	 * 
	 * @param property
	 *            De property.
	 * @param index
	 *            De index van de property in de class.
	 * @return Alle instellingen voor de gegeven property.
	 */
	protected <Y, Z> FieldProperties<T, Y, Z> getFieldProperties(Property<T, Y, Z> property,
			int index)
	{
		AutoForm autoForm = property.getAnnotation(AutoForm.class);
		FieldProperties<T, Y, Z> ret = new FieldProperties<T, Y, Z>(property);

		ret.setVisibilityModel(determineVisibility(property, autoForm, ret));
		ret.setRenderMode(determineRenderMode(property, autoForm, ret));
		ret.setOrder(determineOrder(property, autoForm, index, ret));
		ret.setLabelModel(determineLabel(property, autoForm, ret));
		ret.setDescriptionModel(determineDescription(property, autoForm, ret));
		ret.setRequired(determineRequired(property, autoForm, ret));
		ret.setHtmlClasses(determineHtmlClasses(property, autoForm, ret));
		ret.setEditor(determineFieldType(property, autoForm, ret));
		ret.setFieldContainerType(determineContainerType(property, autoForm, ret));
		ret.setValidators(determineValidators(property, autoForm, ret));
		return ret;
	}

	/**
	 * Bepaald of het veld getoond moet worden.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return Een model met een boolean die aangeeft of het veld wel of niet getoond moet
	 *         worden.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> IModel<Boolean> determineVisibility(Property<T, Y, Z> property,
			AutoForm autoForm, FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.VISIBILITY);
		if (modifier != null)
			return modifier.getVisibility(this, property, fieldProperties);

		return new Model<Boolean>(true);
	}

	/**
	 * Bepaald in welke render mode de property getoont moet worden. Als het veld een
	 * {@link AutoForm} annotation heeft met {@link AutoForm#readOnly()} true, wordt
	 * {@link RenderMode#DISPLAY} gebruikt, anders wordt teruggevallen op de waarde die
	 * aan de constructor is meegegeven.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return De render mode voor de property.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> RenderMode determineRenderMode(Property<T, Y, Z> property, AutoForm autoForm,
			FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.RENDER_MODE);
		if (modifier != null)
			return modifier.getRenderMode(this, property, fieldProperties);

		if (autoForm != null && autoForm.readOnly())
			return RenderMode.DISPLAY;

		if (!property.isWriteAllowed())
			return RenderMode.DISPLAY;

		return getRenderMode();
	}

	/**
	 * Bepaald de volgorde van de velden. Hoe hoger de waarde, hoe lager het veld komt te
	 * staan. Standaard wordt de de index van de property gebruikt. Deze kan overschreven
	 * worden met @ AutoForm#order()}.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param index
	 *            De index van de property.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return De volgorde waarde voor de property.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> int determineOrder(Property<T, Y, Z> property, AutoForm autoForm, int index,
			FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.ORDER);
		if (modifier != null)
			return modifier.getOrder(this, property, index, fieldProperties);

		if (sortAccordingToPropertyNames && propertyNames != null)
		{
			int ret = propertyNames.indexOf(property.getPath());
			if (ret != -1)
				return ret;
		}

		if (autoForm != null && autoForm.order() != Integer.MAX_VALUE)
			return autoForm.order();
		return index;
	}

	/**
	 * Bepaald het label voor het veld. Het standaard label is de naam van de property.
	 * Dit kan overschreven worden met {@link AutoForm#label()}.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return Het label voor de property.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> IModel<String> determineLabel(Property<T, Y, Z> property, AutoForm autoForm,
			FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.LABEL);
		if (modifier != null)
			return modifier.getLabel(this, property, fieldProperties);

		if (autoForm == null || autoForm.label().equals(AutoForm.DEFAULT))
			return new Model<String>(StringUtil.convertCamelCase(Character.toUpperCase(property
				.getName().charAt(0))
				+ property.getName().substring(1)));

		return new Model<String>(autoForm.label());
	}

	/**
	 * Bepaald de beschrijving voor het veld. Dit kan overschreven worden met
	 * {@link AutoForm#description()}.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return De beschrijving voor de property.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> IModel<String> determineDescription(Property<T, Y, Z> property,
			AutoForm autoForm, FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.DESCRIPTION);
		if (modifier != null)
			return modifier.getDescription(this, property, fieldProperties);

		if (autoForm != null && !autoForm.description().isEmpty())
			return new Model<String>(autoForm.description());

		return new Model<String>("");
	}

	/**
	 * Bepaald of het veld verplicht is of niet. Als het property type een primitive is,
	 * is deze altijd verplicht (kan niet null zijn). Verder wordt gekeken naar {@code
	 * Column.nullable()}, {@code JoinColumn.nullable()}, {@code ManyToOne.optional()} en
	 * als laatste naar {@link AutoForm#required()}.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return Of het veld verplicht is of niet.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> boolean determineRequired(Property<T, Y, Z> property, AutoForm autoForm,
			FieldProperties<T, Y, Z> fieldProperties)
	{
		if (property.getType().isPrimitive())
			return true;
		Column column = property.getAnnotation(Column.class);
		if (column != null && !column.nullable())
			return true;
		JoinColumn jColumn = property.getAnnotation(JoinColumn.class);
		if (jColumn != null && !jColumn.nullable())
			return true;
		ManyToOne manyToOne = property.getAnnotation(ManyToOne.class);
		if (manyToOne != null && !manyToOne.optional())
			return true;
		Basic basic = property.getAnnotation(Basic.class);
		if (basic != null && !basic.optional())
			return true;

		FieldModifier modifier = getFieldModifier(property, Action.REQUIRED);
		if (modifier != null)
			return modifier.isRequired(this, property, fieldProperties);

		return autoForm != null ? autoForm.required() : false;
	}

	/**
	 * Bepaald of er extra HTML classes aan het editor veld moeten worden gehangen. Deze
	 * worden uit {@link AutoForm#htmlClasses()} gehaald.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return Extra HTML classes die aan het editor veld gehangen moeten worden.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> List<String> determineHtmlClasses(Property<T, Y, Z> property,
			AutoForm autoForm, FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.HTML_CLASSES);
		if (modifier != null)
			return modifier.getHtmlClasses(this, property, fieldProperties);

		if (autoForm == null)
			return Collections.emptyList();

		return Arrays.asList(autoForm.htmlClasses());
	}

	/**
	 * Bepaald de class van het component dat het veld moet tonen. Dit kan worden
	 * opgegeven via {@link AutoForm#editorClass()} en {@link AutoForm#displayClass()},
	 * afhankelijk van de render mode. Is dit niet opgegeven, dan wordt terug gevallen op
	 * de {@link AutoFormRegistry}.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return De class van het component dat gebruikt moet worden om het veld te tonen.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> Class< ? extends Component> determineFieldType(Property<T, Y, Z> property,
			AutoForm autoForm, FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.FIELD_CLASS);
		if (modifier != null)
			return modifier.getFieldType(this, property, fieldProperties);

		Class< ? extends Component> ret;
		switch (fieldProperties.getRenderMode())
		{
			case EDIT:
				if (autoForm != null && !autoForm.editorClass().equals(Component.class))
					return autoForm.editorClass();
				ret = AutoFormRegistry.getInstance().getEditorClass(fieldProperties);
				if (ret != null)
					return ret;
				break;
			case DISPLAY:
				if (autoForm != null && !autoForm.displayClass().equals(Component.class))
					return autoForm.displayClass();
				ret = AutoFormRegistry.getInstance().getDisplayClass(fieldProperties);
				if (ret != null)
					return ret;
				break;
		}
		throw new IllegalArgumentException("Property type not supported for "
			+ fieldProperties.getRenderMode() + ": " + property);
	}

	/**
	 * Bepaald de container van het component dat het veld moet tonen. Dit kan worden
	 * opgegeven via {@link AutoForm#editorContainer()} en
	 * {@link AutoForm#displayContainer()}, afhankelijk van de render mode. Is dit niet
	 * opgegeven, dan wordt terug gevallen op de {@link AutoFormRegistry}. Als deze ook
	 * geen resultaten geeft, wordt {@link FieldContainerType#DIV} of
	 * {@link FieldContainerType#LABEL} gebruikt.
	 * 
	 * @param property
	 *            De property.
	 * @param autoForm
	 *            De {@link AutoForm} annotation of null.
	 * @param fieldProperties
	 *            De instellingen voor de property tot nu toe.
	 * @return De container van het component dat gebruikt moet worden om het veld te
	 *         tonen.
	 */
	@SuppressWarnings("hiding")
	protected <Y, Z> String determineContainerType(Property<T, Y, Z> property, AutoForm autoForm,
			FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.FIELD_CONTAINER);
		if (modifier != null)
			return modifier.getFieldContainer(this, property, fieldProperties);

		String ret;
		switch (fieldProperties.getRenderMode())
		{
			case EDIT:
				if (autoForm != null && !autoForm.editorContainer().equals(AutoForm.DEFAULT))
					return autoForm.editorContainer();
				ret = AutoFormRegistry.getInstance().getEditorContainer(fieldProperties);
				return ret != null ? ret : FieldContainerType.DIV;
			case DISPLAY:
				if (autoForm != null && !autoForm.displayContainer().equals(AutoForm.DEFAULT))
					return autoForm.displayContainer();
				ret = AutoFormRegistry.getInstance().getDisplayContainer(fieldProperties);
				return ret != null ? ret : FieldContainerType.LABEL;
		}
		throw new IllegalArgumentException("Invalid render mode");
	}

	@SuppressWarnings("hiding")
	protected <Y, Z> IModel<AutoFormValidator[]> determineValidators(Property<T, Y, Z> property,
			AutoForm autoForm, FieldProperties<T, Y, Z> fieldProperties)
	{
		FieldModifier modifier = getFieldModifier(property, Action.VALIDATOR);
		if (modifier != null)
			return modifier.getValidators(this, property, fieldProperties);

		return autoForm != null ? new Model<AutoFormValidator[]>(autoForm.validators())
			: new Model<AutoFormValidator[]>();
	}

	/**
	 * Maakt de container voor het gegeven veld.
	 * 
	 * @param id
	 *            Het id van de container.
	 * @param properties
	 *            De properties, aan hand waarvan de container gemaakt wordt.
	 * @return De container.
	 */
	protected <Y, Z> AbstractFieldContainer<T, Y, Z> createContainer(String id,
			FieldProperties<T, Y, Z> properties)
	{
		return AutoFormRegistry.getInstance().getMarkupRenderer(markupRendererName)
			.createContainer(id, properties);
	}

	/**
	 * Maakt het model voor het gegeven veld.
	 * 
	 * @param fieldProperties
	 *            De properties van het veld.
	 * @return Het model voor het veld.
	 */
	@SuppressWarnings("hiding")
	protected IModel< ? > createModel(FieldProperties<T, ? , ? > fieldProperties)
	{
		Property<T, ? , ? > property = fieldProperties.getProperty();
		FieldModifier modifier = getFieldModifier(property, Action.MODEL);
		if (modifier != null)
			return modifier.createModel(this, property, fieldProperties);

		return new PropertyModel<Object>(getDefaultModel(), fieldProperties.getModelPath());
	}

	/**
	 * Maakt het form field.
	 * 
	 * @param id
	 *            Het id van het veld.
	 * @param fieldProperties
	 *            De properties van het veld.
	 * @param model
	 *            Het model voor het veld.
	 * @return Het nieuwe component.
	 */
	@SuppressWarnings("hiding")
	protected Component createFormField(String id, FieldProperties<T, ? , ? > fieldProperties,
			IModel< ? > model)
	{
		Property<T, ? , ? > property = fieldProperties.getProperty();
		FieldModifier modifier = getFieldModifier(property, Action.CREATION);
		if (modifier != null)
			return modifier.createField(this, id, model, property, fieldProperties);

		try
		{
			return ReflectionUtil.invokeConstructor(fieldProperties.getEditor(), id, this, model);
		}
		catch (InvocationFailedException e)
		{
			// ignoring exception for optional constructor
			if (!(e.getCause() instanceof MethodNotFoundException))
			{
				LOG.info(e.getMessage(), e);
			}
		}
		return ReflectionUtil.invokeConstructor(fieldProperties.getEditor(), id, model);
	}

	/**
	 * Zet extra properties op het form field. Standaard zijn dit het label, de required
	 * flag en het type.
	 * 
	 * @param formField
	 *            Het veld.
	 * @param properties
	 *            De properties.
	 */
	protected void setFormFieldProperties(Component formField, FieldProperties<T, ? , ? > properties)
	{
		try
		{
			ReflectionUtil.invokeMethod(formField, "setRequired", properties.isRequired());
		}
		catch (InvocationFailedException e)
		{
			if (!(e.getCause() instanceof MethodNotFoundException))
			{
				LOG.info("Cannot set required flag, due to " + e.getMessage());
			}
		}

		if (formField instanceof FormComponent< ? >)
		{
			FormComponent< ? > formCField = (FormComponent< ? >) formField;
			formCField.setLabel(properties.getLabelModel());
		}
		if (!formField.getRenderBodyOnly())
			formField.setOutputMarkupId(true);
	}

	/**
	 * Geeft de caption van de field set.
	 * 
	 * @return De caption.
	 */
	public String getCaption()
	{
		return caption;
	}

	/**
	 * Geeft de lijst met html classes die aan de fieldset gehangen moeten worden.
	 * 
	 * @return De lijst.
	 */
	public List<String> getHtmlClasses()
	{
		return htmlClasses;
	}

	/**
	 * @return een bool om aan te geven of de velden nog moeten worden gesorteerd a.d.h.v
	 *         de opgegeven PropertyNames lijst.
	 */
	public boolean isSortAccordingToPropertyNames()
	{
		return sortAccordingToPropertyNames;
	}

	/**
	 * @param sortAccordingToPropertyNames
	 *            een bool om aan te geven of de velden nog moeten worden gesorteerd
	 *            a.d.h.v de opgegeven PropertyNames lijst.
	 */
	public void setSortAccordingToPropertyNames(boolean sortAccordingToPropertyNames)
	{
		this.sortAccordingToPropertyNames = sortAccordingToPropertyNames;
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getModel()
	{
		return (IModel<T>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public T getModelObject()
	{
		return (T) getDefaultModelObject();
	}

	@Override
	protected void onDetach()
	{
		for (FieldModifier curModifier : fieldModifiers)
		{
			curModifier.detach();
		}

		super.onDetach();
	}
}
