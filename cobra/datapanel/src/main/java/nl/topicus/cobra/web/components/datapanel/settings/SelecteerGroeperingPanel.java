package nl.topicus.cobra.web.components.datapanel.settings;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class SelecteerGroeperingPanel<T> extends ModalWindowBasePanel<GroupProperty<T>>
{
	private static final long serialVersionUID = 1L;

	private final IModel<GroupProperty<T>> settingModel;

	private final CustomDataPanelContentDescription<T> content;

	private Form< ? > form;

	public SelecteerGroeperingPanel(String id, SelecteerGroeperingModalWindow<T> window,
			final CustomDataPanel<T> datapanel, IModel<GroupProperty<T>> settingModel)
	{
		super(id, window);
		this.settingModel = settingModel;
		this.content = datapanel.getContentDescription();
		final RadioGroup<GroupProperty<T>> group =
			new RadioGroup<GroupProperty<T>>("radioGroup", settingModel);
		form = new Form<Object>("form");
		form.add(group);
		ListView<GroupProperty<T>> listview =
			new ListView<GroupProperty<T>>("listview", content.getGroupProperties())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<GroupProperty<T>> item)
				{
					item.add(new Radio<GroupProperty<T>>("radio", item.getModel()));
					item.add(ComponentFactory.getDataLabel("property",
						new PropertyModel<GroupProperty<T>>(item.getModel(), "omschrijving")));
				}

			};
		listview.setReuseItems(true);
		group.add(listview);
		add(form);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > frm)
			{
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > frm)
			{
				refreshFeedback(target);
			}

		});
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		settingModel.detach();
		content.detach();
	}
}
