package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpuntLocatie;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.NieuwAanleverpuntModalWindow;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronAanleverpuntTable;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BronInrichtingAanleverpuntenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private EduArteDataPanel<BronAanleverpuntLocatie> datapanel;

	private IModel<BronAanleverpunt> aanleverpuntModel;

	private NieuwAanleverpuntModalWindow aanleverpuntModalWindow;

	private final class AanleverpuntListModel extends
			LoadableDetachableModel<List<BronAanleverpuntLocatie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		@SuppressWarnings("unchecked")
		protected List<BronAanleverpuntLocatie> load()
		{
			return DataAccessRegistry.getHelper(DataAccessHelper.class).list(
				BronAanleverpuntLocatie.class, "aanleverpunt");
		}
	}

	public BronInrichtingAanleverpuntenPanel(String id)
	{
		super(id);
		aanleverpuntModel =
			ModelFactory.getCompoundChangeRecordingModel(new BronAanleverpunt(),
				new DefaultModelManager(BronAanleverpunt.class));
		aanleverpuntModalWindow =
			new NieuwAanleverpuntModalWindow("aanleverpuntWindow", aanleverpuntModel);
		aanleverpuntModalWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(datapanel);
			}
		});
		add(aanleverpuntModalWindow);

		CollectionDataProvider<BronAanleverpuntLocatie> provider =
			new CollectionDataProvider<BronAanleverpuntLocatie>(new AanleverpuntListModel());
		BronAanleverpuntTable table = new BronAanleverpuntTable();
		table.addColumn(new AjaxDeleteColumn<BronAanleverpuntLocatie>("delete", "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<BronAanleverpuntLocatie> rowModel,
					AjaxRequestTarget target)
			{
				BronAanleverpuntLocatie bronAanleverpuntLocatie = rowModel.getObject();
				bronAanleverpuntLocatie.delete();
				bronAanleverpuntLocatie.commit();
				target.addComponent(datapanel);
			}
		}.setPositioning(Positioning.FIXED_RIGHT));
		datapanel = new EduArteDataPanel<BronAanleverpuntLocatie>("datapanel", provider, table);
		datapanel
			.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<BronAanleverpuntLocatie>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onClick(AjaxRequestTarget target, Item<BronAanleverpuntLocatie> item)
				{
					BronAanleverpuntLocatie locatie = item.getModelObject();
					aanleverpuntModel.setObject(locatie.getAanleverpunt());
					aanleverpuntModalWindow.show(target);
				}
			});
		add(datapanel);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(aanleverpuntModel);
		super.onDetach();
	}
}