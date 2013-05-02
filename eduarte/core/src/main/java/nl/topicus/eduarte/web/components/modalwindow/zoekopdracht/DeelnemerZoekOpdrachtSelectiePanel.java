package nl.topicus.eduarte.web.components.modalwindow.zoekopdracht;

import java.util.List;

import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerZoekOpdrachtTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het selecteren van een deelnemerzoekopdracht uit de meegegeven lijst.
 * 
 * @author niesink
 */
public class DeelnemerZoekOpdrachtSelectiePanel extends ModalWindowBasePanel<DeelnemerZoekOpdracht>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekOpdrachtSelectiePanel(String id,
			CobraModalWindow<DeelnemerZoekOpdracht> modalWindow,
			IModel<List<DeelnemerZoekOpdracht>> deelnemerZoekOpdrachtListModel)
	{
		super(id, modalWindow);
		IModelDataProvider<DeelnemerZoekOpdracht> provider =
			new IModelDataProvider<DeelnemerZoekOpdracht>(deelnemerZoekOpdrachtListModel);
		CustomDataPanel<DeelnemerZoekOpdracht> datapanel =
			new EduArteDataPanel<DeelnemerZoekOpdracht>("datapanel", provider,
				new DeelnemerZoekOpdrachtTable());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<DeelnemerZoekOpdracht>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<DeelnemerZoekOpdracht> item)
			{
				getModalWindow().setModel(item.getModel());
				getModalWindow().close(target);
			}
		});
		datapanel.setItemsPerPage(10);
		add(datapanel);
		createComponents();
	}

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
}
