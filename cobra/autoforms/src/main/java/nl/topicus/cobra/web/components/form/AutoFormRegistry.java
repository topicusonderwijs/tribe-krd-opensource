package nl.topicus.cobra.web.components.form;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.persistence.Lob;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.form.simple.SimpleMarkupRenderer;
import nl.topicus.cobra.web.components.form.table.EduArteTableMarkupRenderer;
import nl.topicus.cobra.web.components.form.table.EduArteTableWithoutLabelMarkupRender;
import nl.topicus.cobra.web.components.form.table.TableMarkupRenderer;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;

/**
 * {@code AutoFormRegistry} bepaalt de standaard rendering voor auto forms. Het bevat een
 * register voor {@link FieldMarkupRenderer} implementaties en een register voor de field
 * componenten.
 * <p>
 * Het register voor {@link FieldMarkupRenderer} werkt met behulp van een key. De
 * standaard renderer is geregistreerd onder {@link #DEFAULT_MARKUP_RENDERER}, andere
 * renderers kunnen ook geregistreerd worden.
 * <p>
 * Het register voor field componenten werkt op basis van het type van de property. Het
 * register bevat informatie over de editor en display class en container voor
 * verschillende property types (in {@link RenderSettings} instances). Als voor een
 * bepaald type geen geschikte entry gevonden kan worden, wordt terug gevallen op het
 * supertype. Ook worden primitives gewrapped en vise versa.
 * <p>
 * Voor elk property type (inclusief superclasses en primitives) kunnen meerdere
 * {@link RenderSettings} instances geregistreerd worden. Deze worden van achter naar voor
 * (later toegevoegde settings overschrijven dus bestaande settings) doorgezocht tot
 * {@linkplain RenderSettings#matches(FieldProperties) een match} gevonden is. Dit systeem
 * maakt het bijvoorbeeld mogelijk om voor properties van het type {@code String} een
 * andere editor te gebruiken als de {@code Lob} annotation aan de property gekoppeld is.
 * Eigen instellingen kunnen via een van de {@code register} methodes toegevoegd worden.
 * <p>
 * Standaard zijn een aantal componenten geregistreerd. Dit zijn:
 * <ul>
 * <li>{@link TableMarkupRenderer} als standaard {@link FieldMarkupRenderer}.</li>
 * <li>{@link Label} als standaard component voor weergave.</li>
 * <li>{@link TextField} als editor voor string of integer properties.</li>
 * <li>{@link TextArea} als editor voor string properties met de {@code Lob} annotation.</li>
 * <li>{@link CheckBox} als edtior voor boolean properties.</li>
 * </ul>
 * 
 * @author papegaaij
 */
public final class AutoFormRegistry
{
	private static final AutoFormRegistry INSTANCE = new AutoFormRegistry();

	/**
	 * De key voor de standaard {@link FieldMarkupRenderer}.
	 */
	public static final String DEFAULT_MARKUP_RENDERER = "DEFAULT_MARKUP_RENDERER";

	/**
	 * Geeft de singleton instance van {@code AutoFormRegistry}.
	 * 
	 * @return De singleton instance van {@code AutoFormRegistry}.
	 */
	public static AutoFormRegistry getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Verschillende instellingen voor auto forms.
	 * 
	 * @author papegaaij
	 */
	public static enum RenderSetting
	{
		/** Komt overeen met {@link AutoForm#displayClass()}. */
		DISPLAY_CLASS,
		/** Komt overeen met {@link AutoForm#displayContainer()}. */
		DISPLAY_CONTAINER,
		/** Komt overeen met {@link AutoForm#editorClass()}. */
		EDITOR_CLASS,
		/** Komt overeen met {@link AutoForm#editorContainer()}. */
		EDITOR_CONTAINER;
	}

	/**
	 * {@code RenderSettings} biedt de basis voor het registreren van standaard
	 * instellingen voor auto forms. De class kan instellingen bevatten die overeenkomen
	 * met de waardes van {@link RenderSetting}. Via de {@link #matches(FieldProperties)}
	 * methode kan een instance van {@code RenderSettings} aangeven of de instellingen van
	 * toepassing zijn op de property. Subclasses kunnen hier ander gedrag hebben. Er
	 * dient wel op gelet te worden dat de subclass dan ook
	 * {@link #equalMatch(RenderSettings)} overschrijft.
	 * 
	 * @author papegaaij
	 */
	public static class RenderSettings
	{
		private Class< ? extends Component> displayClass;

		private String displayContainer;

		private Class< ? extends Component> editorClass;

		private String editorContainer;

		/**
		 * Maakt een nieuwe, lege {@code RenderSettings}.
		 */
		public RenderSettings()
		{
		}

		/**
		 * Geeft true als de gegeven setting gezet is (niet null).
		 * 
		 * @param setting
		 *            De setting om te controleren.
		 * @return True als de setting gezet is, anders false.
		 */
		public boolean isSet(RenderSetting setting)
		{
			switch (setting)
			{
				case DISPLAY_CLASS:
					return displayClass != null;
				case DISPLAY_CONTAINER:
					return displayContainer != null;
				case EDITOR_CLASS:
					return editorClass != null;
				case EDITOR_CONTAINER:
					return editorContainer != null;
			}
			return false;
		}

		/**
		 * Voegt de velden van de gegeven settings samen met deze settings. Bestaande
		 * instellingen worden overschreven.
		 * 
		 * @param settings
		 *            De settings om samen te voegen. Niet gezette instellingen worden
		 *            genegeerd.
		 */
		public void mergeWith(RenderSettings settings)
		{
			if (settings.isSet(RenderSetting.DISPLAY_CLASS))
			{
				setDisplayClass(settings.getDisplayClass());
			}
			if (settings.isSet(RenderSetting.DISPLAY_CONTAINER))
			{
				setDisplayContainer(settings.getDisplayContainer());
			}
			if (settings.isSet(RenderSetting.EDITOR_CLASS))
			{
				setEditorClass(settings.getEditorClass());
			}
			if (settings.isSet(RenderSetting.EDITOR_CONTAINER))
			{
				setEditorContainer(settings.getEditorContainer());
			}
		}

		/**
		 * Geeft aan of deze {@code RenderSettings} van toepassing op de gegeven property.
		 * Standaard geeft deze methode altijd true. Als een subclass deze methode
		 * overschrijft, dient ook {@link #equalMatch(RenderSettings)} overschreven te
		 * worden.
		 * 
		 * @param property
		 *            De property om te controleren.
		 * @return True als deze instellingen van toepassing zijn op de property, anders
		 *         false.
		 */
		public <X, Y, Z> boolean matches(FieldProperties<X, Y, Z> property)
		{
			return true;
		}

		/**
		 * Geeft aan of de gegeven {@code RenderSettings} dezelfde criteria gebruikt om te
		 * bepalen of de instellingen wel of niet van toepassing zijn op een property (zie
		 * {@link #matches(FieldProperties)}). Standaard wordt gekeken of de gegeven
		 * {@code RenderSettings} van dezelfde class is als deze {@code RenderSettings}.
		 * 
		 * @param other
		 *            De andere {@code RenderSettings}.
		 * @return True als deze {@code RenderSettings} dezelfde criteria gebruikt om te
		 *         bepalen of de instellingen wel of niet van toepassing zijn op een
		 *         property, anders false.
		 */
		public boolean equalMatch(RenderSettings other)
		{
			return other.getClass().equals(getClass());
		}

		public Class< ? extends Component> getDisplayClass()
		{
			return displayClass;
		}

		public void setDisplayClass(Class< ? extends Component> displayClass)
		{
			this.displayClass = displayClass;
		}

		public String getDisplayContainer()
		{
			return displayContainer;
		}

		public void setDisplayContainer(String displayContainer)
		{
			this.displayContainer = displayContainer;
		}

		public Class< ? extends Component> getEditorClass()
		{
			return editorClass;
		}

		public void setEditorClass(Class< ? extends Component> editorClass)
		{
			this.editorClass = editorClass;
		}

		public String getEditorContainer()
		{
			return editorContainer;
		}

		public void setEditorContainer(String editorContainer)
		{
			this.editorContainer = editorContainer;
		}
	}

	/**
	 * {@code AnnotatedRenderSettings} bevat instellingen die van toepassing zijn op
	 * properties met een bepaalde annotation.
	 * 
	 * @author papegaaij
	 */
	public static class AnnotatedRenderSettings extends RenderSettings
	{
		private Class< ? extends Annotation> annotationClass;

		/**
		 * Maakt een nieuwe, lege {@code AnnotatedRenderSettings}.
		 * 
		 * @param annotationClass
		 *            De annotation die aanwezig moet zijn op de properties.
		 */
		public AnnotatedRenderSettings(Class< ? extends Annotation> annotationClass)
		{
			this.annotationClass = annotationClass;
		}

		/**
		 * Geeft true als de gegeven property de juiste annotation heeft, en deze
		 * instellingen dus van toepassing zijn.
		 * 
		 * @param property
		 *            De property om te controleren.
		 * @return True als deze instellingen van toepassing zijn op de property, anders
		 *         false.
		 * @see RenderSettings#matches(FieldProperties)
		 */
		@Override
		public <X, Y, Z> boolean matches(FieldProperties<X, Y, Z> property)
		{
			return property.getProperty().isAnnotationPresent(annotationClass);
		}

		/**
		 * {@inheritDoc} Als de classes hetzelfde zijn, wordt gekeken of de andere {@code
		 * RenderSettings} naar dezelfde annotation kijkt.
		 * 
		 * @param other
		 *            De andere {@code RenderSettings}.
		 * @return True als deze {@code RenderSettings} dezelfde criteria gebruikt om te
		 *         bepalen of de instellingen wel of niet van toepassing zijn op een
		 *         property, anders false.
		 * @see RenderSettings#equalMatch(RenderSettings)
		 */
		@Override
		public boolean equalMatch(RenderSettings other)
		{
			return super.equalMatch(other)
				&& ((AnnotatedRenderSettings) other).getAnnotationClass().equals(
					getAnnotationClass());
		}

		public Class< ? extends Annotation> getAnnotationClass()
		{
			return annotationClass;
		}
	}

	/**
	 * {@code EditorFieldSubclassRenderSettings} bevat instellingen die van toepassing
	 * zijn voor editors die een subclass zijn van een gegeven class. Deze class is vooral
	 * nuttig om standaard editor containers te registreren voor editor componenten die
	 * regelmatig gesubclassed worden, zoals {@link DropDownChoice} en {@link TextField}
	 * (met auto completion).
	 * 
	 * @author papegaaij
	 */
	public static class EditorFieldSubclassRenderSettings extends RenderSettings
	{
		private Class< ? extends Component> editorFieldClass;

		/**
		 * Maakt een nieuwe, lege {@code EditorFieldSubclassRenderSettings}.
		 * 
		 * @param editorFieldClass
		 *            De class waar de editor een subclass van moet zijn.
		 */
		public EditorFieldSubclassRenderSettings(Class< ? extends Component> editorFieldClass)
		{
			this.editorFieldClass = editorFieldClass;
		}

		/**
		 * Geeft true als de gegeven property een editorClass definieerd die een subclass
		 * is van de bij de constructor gegeven class.
		 * 
		 * @param property
		 *            De property om te controleren.
		 * @return True als deze instellingen van toepassing zijn op de property, anders
		 *         false.
		 * @see RenderSettings#matches(FieldProperties)
		 */
		@Override
		public <X, Y, Z> boolean matches(FieldProperties<X, Y, Z> property)
		{
			Class< ? extends Component> editorClass = property.getEditor();
			if (editorClass != null && !editorClass.equals(Component.class))
				return editorFieldClass.isAssignableFrom(editorClass);
			return false;
		}

		/**
		 * {@inheritDoc} Als de classes hetzelfde zijn, wordt gekeken of de andere {@code
		 * RenderSettings} op dezelfde editor class selecteerd.
		 * 
		 * @param other
		 *            De andere {@code RenderSettings}.
		 * @return True als deze {@code RenderSettings} dezelfde criteria gebruikt om te
		 *         bepalen of de instellingen wel of niet van toepassing zijn op een
		 *         property, anders false.
		 * @see RenderSettings#equalMatch(RenderSettings)
		 */
		@Override
		public boolean equalMatch(RenderSettings other)
		{
			return super.equalMatch(other)
				&& ((EditorFieldSubclassRenderSettings) other).getEditorFieldClass().equals(
					getEditorFieldClass());
		}

		public Class< ? extends Component> getEditorFieldClass()
		{
			return editorFieldClass;
		}

		/**
		 * Geeft altijd een exception, aangezien deze instelling niet ondersteund wordt
		 * door deze class.
		 * 
		 * @param editorClass
		 *            Ongebruikt
		 * @see RenderSettings#setEditorClass(java.lang.Class)
		 */
		@Override
		public void setEditorClass(Class< ? extends Component> editorClass)
		{
			throw new UnsupportedOperationException(
				"EditorFieldSubclassRenderSettings cannot contain an editor class");
		}
	}

	/**
	 * {@code DisplayFieldSubclassRenderSettings} bevat instellingen die van toepassing
	 * zijn voor displays die een subclass zijn van een gegeven class.
	 * 
	 * @author hoeve
	 */
	public static class DisplayFieldSubclassRenderSettings extends RenderSettings
	{
		private Class< ? extends Component> displayFieldClass;

		/**
		 * Maakt een nieuwe, lege {@code EditorFieldSubclassRenderSettings}.
		 * 
		 * @param displayFieldClass
		 *            De class waar de display een subclass van moet zijn.
		 */
		public DisplayFieldSubclassRenderSettings(Class< ? extends Component> displayFieldClass)
		{
			this.displayFieldClass = displayFieldClass;
		}

		/**
		 * Geeft true als de gegeven property een displayClass definieerd die een subclass
		 * is van de bij de constructor gegeven class.
		 * 
		 * @param property
		 *            De property om te controleren.
		 * @return True als deze instellingen van toepassing zijn op de property, anders
		 *         false.
		 * @see RenderSettings#matches(FieldProperties)
		 */
		@Override
		public <X, Y, Z> boolean matches(FieldProperties<X, Y, Z> property)
		{
			Class< ? extends Component> displayClass = property.getEditor();
			if (displayClass != null && !displayClass.equals(Component.class))
				return displayFieldClass.isAssignableFrom(displayClass);
			return false;
		}

		/**
		 * {@inheritDoc} Als de classes hetzelfde zijn, wordt gekeken of de andere {@code
		 * RenderSettings} op dezelfde display class selecteerd.
		 * 
		 * @param other
		 *            De andere {@code RenderSettings}.
		 * @return True als deze {@code RenderSettings} dezelfde criteria gebruikt om te
		 *         bepalen of de instellingen wel of niet van toepassing zijn op een
		 *         property, anders false.
		 * @see RenderSettings#equalMatch(RenderSettings)
		 */
		@Override
		public boolean equalMatch(RenderSettings other)
		{
			return super.equalMatch(other)
				&& ((DisplayFieldSubclassRenderSettings) other).getDisplayFieldClass().equals(
					getDisplayFieldClass());
		}

		public Class< ? extends Component> getDisplayFieldClass()
		{
			return displayFieldClass;
		}

		/**
		 * Geeft altijd een exception, aangezien deze instelling niet ondersteund wordt
		 * door deze class.
		 * 
		 * @param displayClass
		 *            Ongebruikt
		 * @see RenderSettings#setEditorClass(java.lang.Class)
		 */
		@Override
		public void setDisplayClass(Class< ? extends Component> displayClass)
		{
			throw new UnsupportedOperationException(
				"DisplayFieldSubclassRenderSettings cannot contain a display class");
		}
	}

	/**
	 * De standaard {@link ClassResolver} die getClass() gebruikt.
	 * 
	 * @author papegaaij
	 */
	public static class DefaultClassResolver implements ClassResolver
	{
		@Override
		public Class< ? > resolveClass(Object object)
		{
			return object == null ? null : object.getClass();
		}
	}

	/** Het register voor form component instellingen */
	private Map<Class< ? >, List<RenderSettings>> register =
		new HashMap<Class< ? >, List<RenderSettings>>();

	/** Het register voor markup renderers */
	private Map<String, FieldMarkupRenderer> markupRendererRegister =
		new HashMap<String, FieldMarkupRenderer>();

	/** register voor class resolvers */
	private List<ClassResolver> classResolvers = new ArrayList<ClassResolver>();

	/**
	 * Maakt een nieuw {@code AutoFormRegistry}, welke een aantal standaard instellingen
	 * bevat.
	 */
	private AutoFormRegistry()
	{
		registerMarkupRenderer(DEFAULT_MARKUP_RENDERER, new TableMarkupRenderer());
		registerMarkupRenderer(TableMarkupRenderer.NAME, new TableMarkupRenderer());
		registerMarkupRenderer(EduArteTableMarkupRenderer.NAME, new EduArteTableMarkupRenderer());
		registerMarkupRenderer(EduArteTableWithoutLabelMarkupRender.NAME,
			new EduArteTableWithoutLabelMarkupRender());
		registerMarkupRenderer(SimpleMarkupRenderer.NAME, new SimpleMarkupRenderer());

		registerDisplay(Object.class, Label.class, FieldContainerType.LABEL);
		registerEditor(Integer.class, TextField.class);
		registerEditor(String.class, TextField.class);
		registerEditor(String.class, TextArea.class, new AnnotatedRenderSettings(Lob.class));
		registerEditor(Boolean.class, CheckBox.class);

		registerContainerForEditor(Object.class, TextField.class, FieldContainerType.INPUT_TEXT);
		registerContainerForEditor(Object.class, PasswordTextField.class,
			FieldContainerType.INPUT_PASSWORD);
		registerContainerForEditor(Object.class, TextArea.class, FieldContainerType.TEXTAREA);
		registerContainerForEditor(Object.class, CheckBox.class, FieldContainerType.INPUT_CHECKBOX);
		registerContainerForEditor(Object.class, DropDownChoice.class, FieldContainerType.SELECT);
		registerContainerForEditor(Object.class, FileUploadField.class,
			FieldContainerType.INPUT_FILE);

		addClassResolver(new DefaultClassResolver());
	}

	/**
	 * Registreert de gegeven resolver als de primaire resolver. Indien deze resolver voor
	 * een object geen resultaat geeft, wordt teruggevallen op de al aanwezige resolvers.
	 * 
	 * @param classResolver
	 */
	public void addClassResolver(ClassResolver classResolver)
	{
		classResolvers.add(0, classResolver);
	}

	/**
	 * Geeft alle {@link ClassResolver}s, in volgorde waarin ze langsgelopen dienen te
	 * worden.
	 * 
	 * @return De {@link ClassResolver}s.
	 */
	public List<ClassResolver> getClassResolvers()
	{
		return classResolvers;
	}

	/**
	 * Voegt een {@link RenderSettings} toe aan het register. Als voor het gegeven
	 * property type al een {@link RenderSettings} bestaat, waarvan
	 * {@link RenderSettings#equalMatch(RenderSettings) equalMatch} true geeft, dan worden
	 * de gegeven instellingen {@linkplain RenderSettings#mergeWith(RenderSettings)
	 * samengevoegd}, anders worden de gegeven instellingen toegevoegd aan de lijst met
	 * instellingen voor dat property type.
	 * 
	 * @param propertyType
	 *            Het property type waaronder de instellingen geregistreerd moeten worden.
	 * @param settings
	 *            De instellingen die geregistreerd moeten worden.
	 */
	public void registerRenderSettings(Class< ? > propertyType, RenderSettings settings)
	{
		List<RenderSettings> settingsList = register.get(propertyType);
		if (settingsList == null)
		{
			settingsList = new ArrayList<RenderSettings>();
			register.put(propertyType, settingsList);
		}

		for (RenderSettings curSettings : settingsList)
		{
			if (curSettings.equalMatch(settings))
			{
				curSettings.mergeWith(settings);
				return;
			}
		}
		settingsList.add(settings);
	}

	/**
	 * Helper methode om voor een bepaald property type een editor class te registreren.
	 * 
	 * @param propertyType
	 *            Het property type waar de editor voor gebruikt moet worden.
	 * @param editorClass
	 *            De class van het editor component.
	 */
	public void registerEditor(Class< ? > propertyType, Class< ? extends Component> editorClass)
	{
		registerEditor(propertyType, editorClass, new RenderSettings());
	}

	/**
	 * Helper methode om voor een bepaald property type een display class te registreren.
	 * 
	 * @param propertyType
	 *            Het property type waar de display voor gebruikt moet worden.
	 * @param displayClass
	 *            De class van het display component.
	 */
	public void registerDisplay(Class< ? > propertyType, Class< ? extends Component> displayClass)
	{
		registerDisplay(propertyType, displayClass, new RenderSettings());
	}

	/**
	 * Helper methode om voor een bepaald property type en base editor class een container
	 * te registreren.
	 * 
	 * @param propertyType
	 *            Het property type waar de container voor gebruikt moet worden.
	 * @param editorBaseClass
	 *            De base class van het editor component.
	 * @param editorContainer
	 *            De container die gebruikt moet worden.
	 * @see EditorFieldSubclassRenderSettings
	 */
	public void registerContainerForEditor(Class< ? > propertyType,
			Class< ? extends Component> editorBaseClass, String editorContainer)
	{
		EditorFieldSubclassRenderSettings settings =
			new EditorFieldSubclassRenderSettings(editorBaseClass);
		settings.setEditorContainer(editorContainer);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Helper methode om voor een bepaald property type en base display class een
	 * container te registreren.
	 * 
	 * @param propertyType
	 *            Het property type waar de container voor gebruikt moet worden.
	 * @param displayBaseClass
	 *            De base class van het display component.
	 * @param displayContainer
	 *            De container die gebruikt moet worden.
	 * @see EditorFieldSubclassRenderSettings
	 */
	public void registerContainerForDisplay(Class< ? > propertyType,
			Class< ? extends Component> displayBaseClass, String displayContainer)
	{
		DisplayFieldSubclassRenderSettings settings =
			new DisplayFieldSubclassRenderSettings(displayBaseClass);
		settings.setDisplayContainer(displayContainer);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Helper methode om voor een bepaald property type een editor class te registreren
	 * als de property een bepaalde annotation heeft.
	 * 
	 * @param propertyType
	 *            Het property type waar de editor voor gebruikt moet worden.
	 * @param editorClass
	 *            De class van het editor component.
	 * @param settings
	 *            De settings waarmee deze editor moet worden resolved.
	 * @see AnnotatedRenderSettings
	 */
	public void registerEditor(Class< ? > propertyType, Class< ? extends Component> editorClass,
			RenderSettings settings)
	{
		settings.setEditorClass(editorClass);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Helper methode om voor een bepaald property type een display class en container te
	 * registreren.
	 * 
	 * @param propertyType
	 *            Het property type waar de display voor gebruikt moet worden.
	 * @param displayClass
	 *            De class van het display component.
	 * @param displayContainer
	 *            De container die gebruikt moet worden.
	 */
	public void registerDisplay(Class< ? > propertyType, Class< ? extends Component> displayClass,
			String displayContainer)
	{
		RenderSettings settings = new RenderSettings();
		settings.setDisplayClass(displayClass);
		settings.setDisplayContainer(displayContainer);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Helper methode om voor een bepaald property type een display class en container te
	 * registreren als de property een bepaalde annotation heeft.
	 * 
	 * @param propertyType
	 *            Het property type waar de display voor gebruikt moet worden.
	 * @param annotationClass
	 *            De annotation die aanwezig moet zijn op de property.
	 * @param displayClass
	 *            De class van het display component.
	 * @param displayContainer
	 *            De container die gebruikt moet worden.
	 * @see AnnotatedRenderSettings
	 */
	public void registerDisplay(Class< ? > propertyType,
			Class< ? extends Annotation> annotationClass, Class< ? extends Component> displayClass,
			String displayContainer)
	{
		AnnotatedRenderSettings settings = new AnnotatedRenderSettings(annotationClass);
		settings.setDisplayClass(displayClass);
		settings.setDisplayContainer(displayContainer);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Helper methode om voor een bepaald property type een editor class te registreren
	 * als de property een bepaalde annotation heeft.
	 * 
	 * @param propertyType
	 *            Het property type waar de editor voor gebruikt moet worden.
	 * @param displayClass
	 *            De class van het display component.
	 * @param settings
	 *            De settings waarmee deze editor moet worden resolved.
	 * @see AnnotatedRenderSettings
	 */
	public void registerDisplay(Class< ? > propertyType, Class< ? extends Component> displayClass,
			RenderSettings settings)
	{
		settings.setDisplayClass(displayClass);
		registerRenderSettings(propertyType, settings);
	}

	/**
	 * Geeft een lijst met classes die voor het gegeven property type doorzocht moeten
	 * worden, in volgorde van belang. De lijst bevat de gegeven class en alle
	 * superclasses. Als het property type een primitive of wrapper class is, wordt ook de
	 * wrapper of primitive opgenomen.
	 * 
	 * @param propertyType
	 *            Het type van de property waar de classes voor bepaald moeten worden.
	 * @param addPrimitiveForWrapper
	 *            Als true, wordt ook de primitive van een wrapper toegevoegd.
	 * @return De lijst met classes die voor het gegeven property type doorzocht moeten
	 *         worden, in volgorde van belang.
	 */
	private List<Class< ? >> getCheckClasses(Class< ? > propertyType, boolean addPrimitiveForWrapper)
	{
		List<Class< ? >> ret = new ArrayList<Class< ? >>();
		Class< ? > curClass = propertyType;
		while (curClass != null)
		{
			ret.add(curClass);
			curClass = curClass.getSuperclass();
		}
		if (propertyType.isInterface())
		{
			ret.remove(propertyType);
			LinkedList<Class< ? >> interfacesLeft = new LinkedList<Class< ? >>();
			interfacesLeft.add(propertyType);
			while (!interfacesLeft.isEmpty())
			{
				Class< ? > curInterface = interfacesLeft.removeFirst();
				interfacesLeft.addAll(Arrays.asList(curInterface.getInterfaces()));
				ret.add(curInterface);
			}
			ret.add(Object.class);
		}

		if (propertyType.isPrimitive())
			ret.addAll(getCheckClasses(ReflectionUtil.getWrapperClass(propertyType), false));

		if (addPrimitiveForWrapper && ReflectionUtil.isWrapperClass(propertyType))
			ret.add(1, ReflectionUtil.getPrimitiveClass(propertyType));

		return ret;
	}

	/**
	 * Zoekt de {@code RenderSettings} voor de gegeven property waar de gegeven instelling
	 * gezet is.
	 * 
	 * @param property
	 *            De property waar de instellingen voor gezocht worden.
	 * @param setting
	 *            De instelling die gezocht wordt.
	 * @return De {@code RenderSettings} voor de gegeven property waar de gegeven
	 *         instelling gezet is, of null.
	 */

	private <X, Y, Z> RenderSettings findSettings(FieldProperties<X, Y, Z> property,
			RenderSetting setting)
	{
		for (Class< ? > curClass : getCheckClasses(property.getProperty().getType(), true))
		{
			List<RenderSettings> settings = register.get(curClass);
			if (settings != null)
			{
				ListIterator<RenderSettings> it = settings.listIterator(settings.size());
				while (it.hasPrevious())
				{
					RenderSettings curSettings = it.previous();
					if (curSettings.isSet(setting) && curSettings.matches(property))
					{
						return curSettings;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Geeft de editor class die voor de gegeven property gebruikt moet worden.
	 * 
	 * @param property
	 *            De property waar de instelling voor gezocht wordt.
	 * @return De editor class die gebruikt moet worden, of null als er niets voor de
	 *         gegeven property bekend is.
	 */
	<X, Y, Z> Class< ? extends Component> getEditorClass(FieldProperties<X, Y, Z> property)
	{
		RenderSettings ret = findSettings(property, RenderSetting.EDITOR_CLASS);
		return ret == null ? null : ret.getEditorClass();
	}

	/**
	 * Geeft de editor container die voor de gegeven property gebruikt moet worden.
	 * 
	 * @param property
	 *            De property waar de instelling voor gezocht wordt.
	 * @return De editor container die gebruikt moet worden, of null als er niets voor de
	 *         gegeven property bekend is.
	 */
	<X, Y, Z> String getEditorContainer(FieldProperties<X, Y, Z> property)
	{
		RenderSettings ret = findSettings(property, RenderSetting.EDITOR_CONTAINER);
		return ret == null ? null : ret.getEditorContainer();
	}

	/**
	 * Geeft de display class die voor de gegeven property gebruikt moet worden.
	 * 
	 * @param property
	 *            De property waar de instelling voor gezocht wordt.
	 * @return De display class die gebruikt moet worden, of null als er niets voor de
	 *         gegeven property bekend is.
	 */
	<X, Y, Z> Class< ? extends Component> getDisplayClass(FieldProperties<X, Y, Z> property)
	{
		RenderSettings ret = findSettings(property, RenderSetting.DISPLAY_CLASS);
		return ret == null ? null : ret.getDisplayClass();
	}

	/**
	 * Geeft de display container die voor de gegeven property gebruikt moet worden.
	 * 
	 * @param property
	 *            De property waar de instelling voor gezocht wordt.
	 * @return De display container die gebruikt moet worden, of null als er niets voor de
	 *         gegeven property bekend is.
	 */
	<X, Y, Z> String getDisplayContainer(FieldProperties<X, Y, Z> property)
	{
		RenderSettings ret = findSettings(property, RenderSetting.DISPLAY_CONTAINER);
		return ret == null ? null : ret.getDisplayContainer();
	}

	/**
	 * Registreert de gegeven {@link FieldMarkupRenderer} onder de gegeven key.
	 * 
	 * @param name
	 *            De key voor de renderer.
	 * @param markupRenderer
	 *            De renderer zelf.
	 */
	public void registerMarkupRenderer(String name, FieldMarkupRenderer markupRenderer)
	{
		markupRendererRegister.put(name, markupRenderer);
	}

	/**
	 * Zoekt de {@link FieldMarkupRenderer} voor de gegeven key.
	 * 
	 * @param name
	 *            De key waar naar gezocht wordt.
	 * @return De {@link FieldMarkupRenderer} of null.
	 */
	public FieldMarkupRenderer getMarkupRenderer(String name)
	{
		return markupRendererRegister.get(name);
	}
}
