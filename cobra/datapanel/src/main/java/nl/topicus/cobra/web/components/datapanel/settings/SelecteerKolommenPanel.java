package nl.topicus.cobra.web.components.datapanel.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.ColumnModel;
import nl.topicus.cobra.web.components.datapanel.ColumnRenderer;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.wiquery.order.OrderSelect;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class SelecteerKolommenPanel<T> extends ModalWindowBasePanel<Void>
{
	private static final long serialVersionUID = 1L;

	private final CustomDataPanelContentDescription<T> content;

	private Form< ? > form;

	private ColumnModel<T> settingModel;

	public SelecteerKolommenPanel(String id, SelecteerKolommenModalWindow<T> window,
			CustomDataPanelContentDescription<T> content, final ColumnModel<T> settingModel)
	{
		super(id, window);
		this.content = content;
		this.settingModel = settingModel;

		form = new Form<Object>("form");
		final OrderSelect<CustomColumn<T>> orderSelect =
			new OrderSelect<CustomColumn<T>>("orderSelect", settingModel
				.getCustomizableColumnsModel(), new AbstractReadOnlyModel<List<CustomColumn<T>>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<CustomColumn<T>> getObject()
				{
					List<CustomColumn<T>> ret =
						new ArrayList<CustomColumn<T>>(SelecteerKolommenPanel.this.content
							.getCustomizableColumns());
					Collections.sort(ret, new Comparator<CustomColumn<T>>()
					{

						@Override
						public int compare(CustomColumn<T> o1, CustomColumn<T> o2)
						{
							return o1.getId().compareTo(o2.getId());
						}
					});
					return ret;
				}
			}, new ColumnRenderer<T>());
		form.add(orderSelect);
		add(form);
		getLayLeft().add(new AppendingAttributeModifier("class", "hideoverflow"));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > iform)
			{
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > iform)
			{
				refreshFeedback(target);
			}
		});
		panel.addButton(new AjaxActieButton(panel, "Standaard")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				settingModel.resetToDefaults();
				getModalWindow().close(target);
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
