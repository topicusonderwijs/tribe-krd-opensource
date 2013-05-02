package nl.topicus.eduarte.web.components.modalwindow;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;

public abstract class AbstractZoekenPanel<T extends IdObject, ZF extends DetachableZoekFilter<T>>
		extends ModalWindowBasePanel<T>
{
	private static final long serialVersionUID = 1L;

	private ZF filter;

	private Class< ? extends ZoekFilterDataAccessHelper<T, ZF>> dahClass;

	private CustomDataPanelContentDescription<T> table;

	public AbstractZoekenPanel(String id, CobraModalWindow<T> window, ZF filter,
			Class< ? extends ZoekFilterDataAccessHelper<T, ZF>> dahClass,
			CustomDataPanelContentDescription<T> table)
	{
		super(id, window);

		this.filter = filter;
		this.dahClass = dahClass;
		this.table = table;

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			Form<Void> form = new Form<Void>("form");
			add(form);

			GeneralFilteredSortableDataProvider<T, ZF> provider =
				GeneralFilteredSortableDataProvider.of(filter, dahClass);
			CustomDataPanel<T> datapanel = createTable("datapanel", provider, table);
			datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<T>(getModalWindow()
				.getModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<T> item)
				{
					getModalWindow().close(target);
				}
			});
			datapanel.setItemsPerPage(10);
			form.add(datapanel);
			form.add(createFilterPanel("filter", filter, datapanel));
		}
		super.onBeforeRender();
	}

	@SuppressWarnings("hiding")
	abstract protected Component createFilterPanel(String id, ZF filter, CustomDataPanel<T> datapanel);

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}

	@SuppressWarnings("hiding")
	protected CustomDataPanel<T> createTable(String panelName,
			GeneralFilteredSortableDataProvider<T, ZF> provider,
			CustomDataPanelContentDescription<T> table)
	{
		return new EduArteDataPanel<T>(panelName, provider, table);
	}
}
