/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.signalen;

import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.web.components.choice.AjaxRadioChoice;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.app.signalering.EventDescription;
import nl.topicus.eduarte.app.signalering.EventTransport;
import nl.topicus.eduarte.app.signalering.NoEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ConfiguratieColumn extends AbstractCustomColumn<Class< ? extends Event>>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends EventTransport> transportClass;

	private EventAbonnementType type;

	private BeheerSignalenPanel< ? > parent;

	public ConfiguratieColumn(EventAbonnementType type, EventTransport transport,
			BeheerSignalenPanel< ? > parent)
	{
		super(transport.getName(), transport.getName());
		this.type = type;
		this.transportClass = transport.getClass();
		this.parent = parent;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<Class< ? extends Event>> rowModel, int span)
	{
		cell.add(new ConfiguratiePanel(componentId, rowModel));
	}

	private class ConfiguratiePanel extends TypedPanel<Class< ? extends Event>>
	{
		private static final long serialVersionUID = 1L;

		private AjaxLink<Void> link;

		public ConfiguratiePanel(String id, final IModel<Class< ? extends Event>> rowModel)
		{
			super(id, rowModel);
			setRenderBodyOnly(true);
			Class< ? extends Event> eventClass = rowModel.getObject();
			final String key =
				type.name() + ":" + transportClass.getName() + ":" + eventClass.getName();
			IModel<EventAbonnementInstelling> radioModel = new IModel<EventAbonnementInstelling>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public EventAbonnementInstelling getObject()
				{
					return parent.getWaarde(key);
				}

				@Override
				public void setObject(EventAbonnementInstelling object)
				{
					parent.getInstellingen().put(key, object);
				}

				@Override
				public void detach()
				{
				}
			};

			final EventDescription description = eventClass.getAnnotation(EventDescription.class);
			boolean hasConfig =
				!description.configuratie().equals(NoEventAbonnementConfiguration.class);
			AjaxRadioChoice<EventAbonnementInstelling> choice =
				new AjaxRadioChoice<EventAbonnementInstelling>("radio", radioModel, parent
					.getAbonnementInstellingen(), new ChoiceRenderer<EventAbonnementInstelling>(),
					hasConfig)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled()
					{
						return super.isEnabled() && parent.isEnabled(key);
					}

					@Override
					protected void onUpdate(AjaxRequestTarget target, Object newSelection)
					{
						target.addComponent(link);
					}
				};
			choice.setSuffix(" ");
			add(choice);

			if (hasConfig)
			{
				IModel<AbstractEventAbonnementConfiguration< ? >> configModel =
					new AbstractReadOnlyModel<AbstractEventAbonnementConfiguration< ? >>()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public AbstractEventAbonnementConfiguration< ? > getObject()
						{
							return parent.getConfiguration(key, description);
						}
					};
				final EventConfiguratieModalWindow modalWindow =
					new EventConfiguratieModalWindow("configWindow", configModel)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void close(AjaxRequestTarget target)
						{
							target.addComponent(link);
							super.close(target);
						}

						@Override
						protected IChangeRecordingModel< ? extends AbstractEventAbonnementConfiguration< ? >> getChangeRecordingModel()
						{
							return parent.getConfigurationModel(key);
						}
					};
				add(modalWindow);
				link = new AjaxLink<Void>("config")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled()
					{
						return super.isEnabled()
							&& !EventAbonnementInstelling.Uit.equals(parent.getWaarde(key));
					}

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						modalWindow.show(target);
					}
				};
				link.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return EventAbonnementInstelling.Uit.equals(parent.getWaarde(key)) ? "mute"
							: null;
					}
				}, " "));
				link.setOutputMarkupId(true);
				add(link);
				link.add(new Label("label", configModel));
			}
			else
			{
				add(new WebMarkupContainer("config").setVisible(false));
				add(new WebMarkupContainer("configWindow").setVisible(false));
			}
		}
	}
}
