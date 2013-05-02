package nl.topicus.eduarte.krd.web.pages.deelnemer.examens;

import java.util.Collection;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.principals.deelnemer.examen.DeelnemerExamensCollectief;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.VoltooienButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Selectie pagina voor Deelnemers.
 * 
 * @author vandekamp
 */
@PageInfo(title = "Geselecteerde deelnemers", menu = "Deelnemer -> Examen -> Acties-overzicht -> [Actie] -> Volgende stap")
@InPrincipal(DeelnemerExamensCollectief.class)
public class DeelnemerKwalificatieGeselecteerdPage extends SecurePage implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private KwalificatieModel kwalificatieModel;

	private IModel<Collection<Verbintenis>> selection;

	private SecurePage returnPage;

	public DeelnemerKwalificatieGeselecteerdPage(KwalificatieModel kwalificatieModel,
			Collection<Verbintenis> selection, SecurePage returnPage)
	{
		super(CoreMainMenuItem.Deelnemer);
		this.returnPage = returnPage;
		this.kwalificatieModel = kwalificatieModel;
		this.selection = ModelFactory.getListModel(selection);
		add(new EduArteDataPanel<Verbintenis>("datapanel", new IModelDataProvider<Verbintenis>(
			this.selection), new DeelnemerTable()));
		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ActieOverzicht);
	}

	@Override
	public Label createTitle(String id)
	{
		if (kwalificatieModel.getGeselecteerdeStatus() != null)
			return new Label(id, kwalificatieModel.getToegestaneExamenstatusOvergang().getActie()
				+ ": " + kwalificatieModel.getGeselecteerdeStatus().getNaam());
		return new Label(id, kwalificatieModel.getToegestaneExamenstatusOvergang().getActie());
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, returnPage));
		panel.addButton(new VoltooienButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				kwalificatieModel.voltooi(selection.getObject());
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				Class< ? extends Page> responsePage = kwalificatieModel.getResponsePageClass();
				if (responsePage.equals(DeelnemerKwalificatiePage.class))
					setResponsePage(new DeelnemerKwalificatiePage(kwalificatieModel.getInfoString()));
				else
					setResponsePage(responsePage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, DeelnemerKwalificatiePage.class));

	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(kwalificatieModel);
		ComponentUtil.detachQuietly(selection);
		super.onDetach();
	}
}
