package nl.topicus.cobra.web.components.choice;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.core.javascript.helper.TraversingHelper;

public abstract class AjaxFieldsetRadioChoicePanel<T extends Object & Serializable> extends
		TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	public AjaxFieldsetRadioChoicePanel(String id, IModel< ? extends List<T>> choices,
			IModel<T> radiogroupModel)
	{
		super(id, radiogroupModel);
		setOutputMarkupId(true);
		add(new AttributeAppender("class", new Model<String>("AjaxFieldsetRadioChoicePanel"), " "));

		final RadioGroup<T> radioGroup = new RadioGroup<T>("radiogroup", radiogroupModel);
		add(radioGroup);
		ListView<T> fieldsets = new ListView<T>("fieldsets", choices)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<T> item)
			{
				Radio<T> radiochoice = new Radio<T>("radiochoice", item.getModel(), radioGroup);
				radiochoice.add(new AjaxFormSubmitBehavior("onclick")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSubmit(AjaxRequestTarget target)
					{
						onUpdate(target, radioGroup.getModel());
					}

					@Override
					protected void onError(AjaxRequestTarget target)
					{

					}
				});

				radiochoice.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public JsScope callback()
					{
						return new JsScope()
						{
							private static final long serialVersionUID = 1L;

							@Override
							protected void execute(JsScopeContext scopeContext)
							{
								// alles onder de panel disablen.
								scopeContext.self().chain(
									TraversingHelper.parents(".AjaxFieldsetRadioChoicePanel"))
									.chain(
										TraversingHelper
											.children(".AjaxFieldsetRadioChoicePanelFieldset"))
									.chain("addClass",
										"'AjaxFieldsetRadioChoicePanelFieldsetDisabled'");

								// ons zelf niet meer disablen
								scopeContext.self().chain(
									TraversingHelper
										.parents(".AjaxFieldsetRadioChoicePanelFieldset"))
									.chain("removeClass",
										"'AjaxFieldsetRadioChoicePanelFieldsetDisabled'");
							}
						};
					}
				}));

				item.add(radiochoice);
				item.add(new Label("radiochoicelabel", new Model<String>(item.getModelObject()
					.toString())));
				item.add(getPanel("radiochoicecontent", item.getModel()));
			}
		};
		radioGroup.add(fieldsets);
	}

	/**
	 * Kan overschreven worden om de click af te vangen op een van de radio buttons.
	 */
	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target, IModel<T> model)
	{

	}

	/**
	 * Override deze functie om de inhoud van de fieldsets te bepalen op basis van het
	 * model.
	 */
	public abstract Panel getPanel(String panelId, IModel<T> fieldsetModel);
}
