package nl.topicus.eduarte.web.pages.beheer;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.CommaSeparatedStringValidator;
import nl.topicus.cobra.web.validators.IpAdresValidator;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.systeem.AlgemeneInstellingen;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.settings.OrganisatieIpAdresSetting;
import nl.topicus.eduarte.entities.settings.OrganisatieSetting;
import nl.topicus.eduarte.entities.settings.ScreenSaverSetting;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MinimumValidator;

@PageInfo(title = "Algemene instellingen", menu = "Beheer > Algemene instellingen")
@InPrincipal(AlgemeneInstellingen.class)
public class AlgemeneInstellingenPage extends AbstractBeheerPage<Void>
{
	private Form<Void> form;

	private ModelManager modelManager;

	private List<IChangeRecordingModel< ? extends OrganisatieSetting< ? >>> settingModels =
		new ArrayList<IChangeRecordingModel< ? extends OrganisatieSetting< ? >>>();

	public AlgemeneInstellingenPage()
	{
		super(BeheerMenuItem.AlgemeneInstellingen);
		List<Class< ? extends OrganisatieSetting< ? >>> settings =
			new ArrayList<Class< ? extends OrganisatieSetting< ? >>>();
		for (AbstractEduArteModule module : EduArteApp.get().getActiveModules(
			AbstractEduArteModule.class, EduArteContext.get().getOrganisatie()))
		{
			settings.addAll(module.getSettings());
		}
		modelManager = new DefaultModelManager(settings);

		// oorspronkelijke settings verplaatst naar de CoreModule

		// ScreenSaverSetting.class, LoginSetting.class,
		// PasswordSetting.class, RadiusServerSetting.class,
		// ResultaatControleSetting.class,
		// GebruikLandelijkeExterneOrganisatiesSetting.class,
		// OrganisatieIpAdresSetting.class);

		add(form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				saveSettings();
			}
		});
		form.add(new ListView<Class< ? extends OrganisatieSetting< ? >>>("settings", settings)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Class< ? extends OrganisatieSetting< ? >>> item)
			{
				item.add(createSettingEditor("settingEditor", item.getModelObject()));
			}
		}.setReuseItems(true));

		createComponents();
	}

	protected void saveSettings()
	{
		for (IChangeRecordingModel< ? extends OrganisatieSetting< ? >> curModel : settingModels)
		{
			curModel.saveObject();
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	protected <T extends OrganisatieSetting< ? >> Component createSettingEditor(String id,
			Class<T> settingClass)
	{
		T setting =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(settingClass);
		IChangeRecordingModel<T> settingModel =
			ModelFactory.getCompoundChangeRecordingModel(setting, modelManager);
		settingModels.add(settingModel);
		AutoFieldSet<T> editor = new AutoFieldSet<T>(id, settingModel, setting.getOmschrijving());
		editor.setOutputMarkupId(true);
		editor.setRenderMode(RenderMode.EDIT);

		addValidators(settingClass, editor);
		addActiefModifiers(settingClass, editor);

		return editor;
	}

	private <T extends OrganisatieSetting< ? >> void addValidators(Class<T> settingClass,
			final AutoFieldSet<T> editor)
	{
		if (ScreenSaverSetting.class.equals(settingClass))
		{
			editor.addModifier("value.timeout", new MinimumValidator<Integer>(1));
			editor.addModifier("value.sessieTimeout", new MinimumValidator<Integer>(1));
		}
		if (OrganisatieIpAdresSetting.class.equals(settingClass))
		{
			editor.addModifier("value.ipAdressen", new CommaSeparatedStringValidator(
				new IpAdresValidator()));
		}
	}

	private <T extends OrganisatieSetting< ? >> void addActiefModifiers(Class<T> settingClass,
			final AutoFieldSet<T> editor)
	{
		Property<T, ? , ? > valueProperty = ReflectionUtil.findProperty(settingClass, "value");
		List<Property<T, ? , ? >> properties = ReflectionUtil.findProperties(valueProperty);
		Property<T, ? , ? > actiefProperty = findActiefProperty(properties);
		if (actiefProperty != null)
		{
			properties.remove(actiefProperty);
			editor.addFieldModifier(new BehaviorModifier(new AjaxFormComponentUpdatingBehavior(
				"onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(editor);
				}
			}, actiefProperty.getPath()));
			editor.addFieldModifier(new EnableModifier(new PropertyModel<Boolean>(
				editor.getModel(), actiefProperty.getPath()), getPropertyPaths(properties)));
		}
	}

	private <T extends OrganisatieSetting< ? >> Property<T, ? , ? > findActiefProperty(
			List<Property<T, ? , ? >> properties)
	{
		for (Property<T, ? , ? > curProperty : properties)
			if (curProperty.getName().equals("actief"))
				return curProperty;
		return null;
	}

	private <T extends OrganisatieSetting< ? >> String[] getPropertyPaths(
			List<Property<T, ? , ? >> properties)
	{
		String[] ret = new String[properties.size()];
		for (int count = 0; count < properties.size(); count++)
		{
			ret[count] = properties.get(count).getPath();
		}
		return ret;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, AlgemeneInstellingenPage.class));
	}
}
