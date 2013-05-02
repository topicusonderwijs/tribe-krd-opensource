/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.signalen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.wiquery.IEDisabledAnimation;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventDescription;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.app.signalering.NoEventAbonnementConfiguration;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.DeadlineEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.VerzuimTaakEventAbonnementConfiguration;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.accordion.Accordion;

public abstract class BeheerSignalenPanel<T extends EventAbonnementSetting> extends Panel
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private Map<String, EventAbonnementInstelling> instellingen;

	private Map<String, IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >>> configurations;

	private Class<T> settingClazz;

	private ModelManager manager;

	public BeheerSignalenPanel(String id, Class<T> settingClazz)
	{
		super(id);

		manager =
			new DefaultModelManager(AbstractEventAbonnementConfiguration.class,
				DeadlineEventAbonnementConfiguration.class,
				VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel.class,
				VerzuimTaakEventAbonnementConfiguration.class);
		this.settingClazz = settingClazz;
		readInstellingen();

		form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				saveSettings();
			}
		};
		add(form);

		Accordion abonnementTypes = new Accordion("abonnementTypes");
		abonnementTypes.setAnimated(new IEDisabledAnimation());
		abonnementTypes.setAutoHeight(false);
		form.add(abonnementTypes);

		ListView<EventAbonnementType> typeList =
			new ListView<EventAbonnementType>("typeList", getAbonnementTypes())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<EventAbonnementType> item)
				{
					Label label = new Label("label", item.getModel());
					item.add(label);
					item.add(createSignaalSettingPanel("events", item.getModelObject()));
				}
			};
		abonnementTypes.add(typeList);
	}

	public abstract List<EventAbonnementType> getAbonnementTypes();

	public abstract List<EventAbonnementInstelling> getAbonnementInstellingen();

	protected abstract T createSetting();

	protected void saveSettings()
	{
		for (Map.Entry<String, IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >>> curConfigEntry : configurations
			.entrySet())
		{
			curConfigEntry.getValue().saveObject();
			if (!instellingen.containsKey(curConfigEntry.getKey()))
				instellingen.put(curConfigEntry.getKey(), EventAbonnementInstelling.Aan);
		}
		for (T curSetting : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).list(settingClazz))
		{
			String key = getKey(curSetting);
			if (!instellingen.containsKey(key))
			{
				curSetting.delete();
			}
			else if (!instellingen.get(key).equals(curSetting.getWaarde()))
			{
				curSetting.setWaarde(instellingen.get(key));
				curSetting.update();
			}
			instellingen.remove(key);
		}
		for (Map.Entry<String, EventAbonnementInstelling> curInstelling : instellingen.entrySet())
		{
			IChangeRecordingModel< ? > configModel = configurations.get(curInstelling.getKey());
			String[] splitKey = curInstelling.getKey().split(":");
			T newSetting = createSetting();
			newSetting.setType(EventAbonnementType.valueOf(splitKey[0]));
			newSetting.setTransportClassname(splitKey[1]);
			newSetting.setEventClassname(splitKey[2]);
			newSetting.setWaarde(curInstelling.getValue());
			if (configModel != null)
				newSetting.setConfiguratie((AbstractEventAbonnementConfiguration< ? >) configModel
					.getObject());
			newSetting.save();
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	public EventAbonnementInstelling getWaarde(String key)
	{
		EventAbonnementInstelling instelling = instellingen.get(key);
		return instelling == null ? EventAbonnementInstelling.Aan : instelling;
	}

	public AbstractEventAbonnementConfiguration< ? > getConfiguration(String key,
			EventDescription description)
	{
		if (configurations.containsKey(key))
			return configurations.get(key).getObject();
		else if (description.configuratie().equals(NoEventAbonnementConfiguration.class))
			return null;

		AbstractEventAbonnementConfiguration< ? > defaultWaarde = getStandaardConfiguratie(key);
		if (defaultWaarde != null)
		{
			defaultWaarde = defaultWaarde.copy();
			configurations.put(key, ModelFactory.getCompoundChangeRecordingModel(defaultWaarde,
				manager));
			return defaultWaarde;
		}

		AbstractEventAbonnementConfiguration< ? > ret =
			(AbstractEventAbonnementConfiguration< ? >) ReflectionUtil
				.invokeConstructor(description.configuratie());
		configurations.put(key, ModelFactory.getCompoundChangeRecordingModel(ret, manager));
		return ret;
	}

	@SuppressWarnings("unused")
	protected AbstractEventAbonnementConfiguration< ? > getStandaardConfiguratie(String key)
	{
		return null;
	}

	public IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >> getConfigurationModel(
			String key)
	{
		return configurations.get(key);
	}

	public Map<String, EventAbonnementInstelling> getInstellingen()
	{
		return instellingen;
	}

	@SuppressWarnings("unused")
	protected boolean isEnabled(String key)
	{
		return true;
	}

	private void readInstellingen()
	{
		instellingen = new HashMap<String, EventAbonnementInstelling>();
		configurations =
			new HashMap<String, IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >>>();
		for (T curSetting : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).list(settingClazz))
		{
			String key = getKey(curSetting);
			instellingen.put(key, curSetting.getWaarde());
			if (curSetting.getConfiguratie() != null)
				configurations.put(key, ModelFactory.getCompoundChangeRecordingModel(curSetting
					.getConfiguratie(), manager));
		}
	}

	public String getKey(EventAbonnementSetting setting)
	{
		return setting.getType().name() + ":" + setting.getTransportClassname() + ":"
			+ setting.getEventClassname();
	}

	private Component createSignaalSettingPanel(String id, final EventAbonnementType type)
	{
		SortedMap<String, Class< ? extends Event>> events =
			new TreeMap<String, Class< ? extends Event>>();
		for (Class< ? extends Event> curEventClass : EduArteApp.get().getEventClasses())
		{
			Module module = curEventClass.getAnnotation(Module.class);
			if (module == null || EduArteApp.get().isModuleActive(module.value()))
			{
				EventDescription description = curEventClass.getAnnotation(EventDescription.class);
				if (Arrays.asList(description.abonnementTypes()).contains(type))
				{
					events.put(description.name(), curEventClass);
				}
			}
		}
		CustomDataPanelContentDescription<Class< ? extends Event>> table =
			new CustomDataPanelContentDescription<Class< ? extends Event>>("Signalen");
		table.addColumn(new CustomPropertyColumn<Class< ? extends Event>>("Signaal", "Signaal", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> createLabelModel(IModel<Class< ? extends Event>> embeddedModel)
			{
				Class< ? extends Event> eventClass = embeddedModel.getObject();
				EventDescription description = eventClass.getAnnotation(EventDescription.class);
				return new Model<String>(description.name());
			}
		});
		for (EventTransport curTransport : EduArteApp.get().getTransports())
		{
			table.addColumn(new ConfiguratieColumn(type, curTransport, this));
		}
		EduArteDataPanel<Class< ? extends Event>> ret =
			new EduArteDataPanel<Class< ? extends Event>>(id,
				new CollectionDataProvider<Class< ? extends Event>>(
					new ArrayList<Class< ? extends Event>>(events.values())), table);
		ret.setButtonsVisible(false);
		return ret;
	}

	public Form<Void> getForm()
	{
		return form;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(configurations);
		manager.detach();
	}
}
