package nl.topicus.eduarte.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.entities.IOrganisatie;
import nl.topicus.cobra.modules.AbstractCobraModule;
import nl.topicus.cobra.modules.CobraAnnotationAuthorizationModule;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.menu.MenuExtender;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.settings.OrganisatieSetting;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.beheer.AlgemeneInstellingenPage;

/**
 * {@code AbstractEduArteModule} provides a base implementation of the {@code
 * SoundScapeMenuModule} interface. A subclass can override
 * {@link #registerMenuExtenders()} to register the required {@link MenuExtender}s.
 */
public abstract class AbstractEduArteModule extends AbstractCobraModule implements
		EduArteSignaalModule, EduArteEditPageModule, CobraAnnotationAuthorizationModule,
		EduArteDocumentTemplateModule
{
	private List<Class< ? extends OrganisatieSetting< ? >>> settings =
		new ArrayList<Class< ? extends OrganisatieSetting< ? >>>();

	private Map<Class< ? extends Signaal>, SignaalHandler< ? extends Signaal>> signaalHandlers =
		new HashMap<Class< ? extends Signaal>, SignaalHandler< ? extends Signaal>>();

	private Map<EduArteModuleEditPageKey< ? >, Class< ? extends IModuleEditPage< ? >>> moduleEditPages =
		new HashMap<EduArteModuleEditPageKey< ? >, Class< ? extends IModuleEditPage< ? >>>();

	private List<DocumentTemplateCreator> documentTemplateCreators =
		new ArrayList<DocumentTemplateCreator>();

	private final EncryptionProvider encryptionProvider = new CobraEncryptonProvider();

	/**
	 * Creates a new {@code AbstractSoundScapeModule} with the given key.
	 * 
	 * @param key
	 *            The key of the module to create.
	 */
	public AbstractEduArteModule(EduArteModuleKey key)
	{
		super(key);
		registerSignaalHandlers();
		registerModuleEditPages();
		registerOrganisatieSettings();
		registerDocumentTemplateCreators();
	}

	@Override
	public EduArteModuleKey getKey()
	{
		return (EduArteModuleKey) super.getKey();
	}

	/**
	 * Registers the {@link SignaalHandler}s. Override this method in a subclass when
	 * signals are used in the module.
	 */
	protected void registerSignaalHandlers()
	{
	}

	/**
	 * Registers the {@link IModuleEditPage}s. Override this method in a subclass when
	 * editpages are used in the module.
	 */
	protected void registerModuleEditPages()
	{
	}

	/**
	 * Registers the {@link OrganisatieSetting}s where each module can define its
	 * settings. This enables the {@link AlgemeneInstellingenPage} to only show settings
	 * for modules that are available to the organization.
	 */
	protected void registerOrganisatieSettings()
	{
	}

	/**
	 * Registers the {@link DocumentTemplateCreator}s, which create document templates
	 * provided by the module.
	 */
	protected void registerDocumentTemplateCreators()
	{
	}

	/**
	 * @see nl.topicus.cobra.modules.CobraModule#isModuleActive(IOrganisatie)
	 */
	@Override
	public boolean isModuleActive(IOrganisatie organization)
	{
		if (EduArteContext.get().getOrganisatie().getRechtenSoort().equals(RechtenSoort.INSTELLING))
		{
			OrganisatieDataAccessHelper helper =
				DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class);
			return helper.isModuleAfgenomen(getKey(), encryptionProvider);
		}
		return true;
	}

	/**
	 * Voeg een SignaalHandler toe voor deze module.
	 * 
	 * @param signaalClass
	 *            De class van het signaal
	 * @param handler
	 *            De handler voor het signaal
	 * @param <T>
	 *            Het type van het signaal
	 */
	protected <T extends Signaal> void addSignaalHandler(Class<T> signaalClass,
			SignaalHandler<T> handler)
	{
		signaalHandlers.put(signaalClass, handler);
	}

	@SuppressWarnings("unchecked")
	public <T extends Signaal> SignaalHandler<T> getSignaalHandler(T signaal)
	{
		return (SignaalHandler<T>) signaalHandlers.get(signaal.getClass());
	}

	/**
	 * Voeg een moduleEditPage toe voor deze page. gebruik deze als er maar 1 editpage
	 * voor deze entity is
	 * 
	 * @param <T>
	 * @param entity
	 * @param moduleEditPage
	 */
	protected <T> void addModuleEditPage(Class<T> entity,
			Class< ? extends IModuleEditPage<T>> moduleEditPage)
	{
		EduArteModuleEditPageKey<T> key = new EduArteModuleEditPageKey<T>(entity);
		moduleEditPages.put(key, moduleEditPage);
	}

	/**
	 * Voeg een moduleEditPage toe voor deze page. Gebruik deze als er meer dan 1 editpage
	 * zijn of komen voor deze entity
	 * 
	 * @param <T>
	 * @param entity
	 * @param menuItemKey
	 * @param moduleEditPage
	 */
	protected <T> void addModuleEditPage(Class<T> entity, MenuItemKey menuItemKey,
			Class< ? extends IModuleEditPage<T>> moduleEditPage)
	{
		EduArteModuleEditPageKey<T> key = new EduArteModuleEditPageKey<T>(entity, menuItemKey);
		moduleEditPages.put(key, moduleEditPage);
	}

	/**
	 * @param <T>
	 * @param entityClass
	 * @param menuItemKey
	 * @return List<AbstractPageBottomRowButton>
	 */
	@SuppressWarnings("unchecked")
	public <T> Class< ? extends IModuleEditPage<T>> getModuleEditPage(Class<T> entityClass,
			MenuItemKey menuItemKey)
	{
		EduArteModuleEditPageKey<T> key = new EduArteModuleEditPageKey<T>(entityClass, menuItemKey);
		if (moduleEditPages.containsKey(key))
		{
			return (Class< ? extends IModuleEditPage<T>>) moduleEditPages.get(key);
		}
		return null;
	}

	public List<Class< ? extends OrganisatieSetting< ? >>> getSettings()
	{
		return settings;
	}

	public void addSetting(Class< ? extends OrganisatieSetting< ? >> setting)
	{
		settings.add(setting);
	}

	@Override
	public List<DocumentTemplate> getRegisteredDocumentTemplates()
	{
		List<DocumentTemplate> ret = new ArrayList<DocumentTemplate>();
		for (DocumentTemplateCreator curCreator : documentTemplateCreators)
			ret.add(curCreator.createDocumentTemplate());
		return ret;
	}

	protected void addDocumentTemplateCreator(DocumentTemplateCreator creator)
	{
		documentTemplateCreators.add(creator);
	}
}
