package nl.topicus.eduarte.web.components.modalwindow.adres;

import java.util.List;

import nl.topicus.cobra.dataproviders.SortableListModelDataProvider;
import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.Adresseerbaar;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AdresEntiteitTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AdressenPanel<T extends AdresEntiteit<T>> extends Panel
{
	private static final long serialVersionUID = 1L;

	private IModel<T> selectedModel;

	public AdressenPanel(String id, IModel< ? extends Adresseerbaar<T>> entiteitModel)
	{
		super(id);

		selectedModel = new HibernateModel<T>(null);

		final CobraModalWindow<T> modalWindow =
			new CobraModalWindow<T>("modalWindow", selectedModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected CobraModalWindowBasePanel<T> createContents(String panelId)
				{
					return new ModalWindowAdresPanel<T>(panelId, this);
				}
			};
		modalWindow.setInitialHeight(200);
		modalWindow.setInitialWidth(470);
		modalWindow.setResizable(false);
		add(modalWindow);

		SortableListModelDataProvider<T> provider =
			new SortableListModelDataProvider<T>(new PropertyModel<List<T>>(entiteitModel,
				"adressen"));
		provider.getSortState().setPropertySortOrder("begindatum", ISortState.DESCENDING);
		EduArteDataPanel<T> datapanel =
			new EduArteDataPanel<T>("datapanel", provider, new AdresEntiteitTable<T>());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<T>(selectedModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<T> item)
			{
				modalWindow.show(target);
			}

			@Override
			protected boolean isSelected(CustomDataPanel<T> panel, Item<T> item, IModel<T> itemModel)
			{
				return false;
			}
		});
		add(datapanel);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		selectedModel.detach();
	}
}
