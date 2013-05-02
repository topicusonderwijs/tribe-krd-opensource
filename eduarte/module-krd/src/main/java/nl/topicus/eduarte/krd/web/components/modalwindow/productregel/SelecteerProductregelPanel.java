package nl.topicus.eduarte.krd.web.components.modalwindow.productregel;

import java.util.List;

import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Pagina voor het selectern van een productregel uit de meegegeven lijst.
 * 
 * @author vandekamp
 */
public class SelecteerProductregelPanel extends ModalWindowBasePanel<Productregel>
{
	private static final long serialVersionUID = 1L;

	public SelecteerProductregelPanel(String id, CobraModalWindow<Productregel> modalWindow,
			IModel<List<Productregel>> productregelListModel)
	{
		super(id, modalWindow);
		Form<Void> form = new Form<Void>("form");
		add(form);
		IModelDataProvider<Productregel> provider =
			new IModelDataProvider<Productregel>(productregelListModel);
		CustomDataPanel<Productregel> datapanel =
			new EduArteDataPanel<Productregel>("datapanel", provider, new ProductregelTable());
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<Productregel>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<Productregel> item)
			{
				getModalWindow().setModel(item.getModel());
				getModalWindow().close(target);
			}
		});
		datapanel.setItemsPerPage(10);
		form.add(datapanel);
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
