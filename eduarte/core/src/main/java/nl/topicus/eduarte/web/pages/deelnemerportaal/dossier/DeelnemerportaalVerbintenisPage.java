package nl.topicus.eduarte.web.pages.deelnemerportaal.dossier;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.eduarte.core.principals.deelnemerportaal.DeelnemerportaalVerbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.web.components.datapanel.HighlightVerbintenisRowFactoryDecorator;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalDossierMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.VerbintenisMetPlaatsingenTable;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVerbintenisDetailPanel;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.VerbintenisMetPlaatsingenDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
@PageInfo(title = "Inschrijvingen onderwijs", menu = "Deelnemer > [deelnemer] > Inschrijvingen")
@InPrincipal(DeelnemerportaalVerbintenis.class)
public class DeelnemerportaalVerbintenisPage extends AbstractDeelnemerportaalDossierPage
{
	private static final long serialVersionUID = 1L;

	protected DeelnemerVerbintenisDetailPanel detailPanel;

	protected CustomDataPanel<IdObject> datapanel;

	public DeelnemerportaalVerbintenisPage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalVerbintenisPage(Verbintenis verbintenis)
	{
		super(DeelnemerportaalDossierMenuItem.Inschrijving, verbintenis);

		IDataProvider<IdObject> dataprovider =
			new VerbintenisMetPlaatsingenDataProvider(getContextDeelnemerModel());

		detailPanel =
			new DeelnemerVerbintenisDetailPanel("detailpanel", ModelFactory.getModel(verbintenis));
		detailPanel.setOutputMarkupId(true);
		updateVerbintenis(verbintenis);

		CollapsableRowFactoryDecorator<IdObject> rowFactory =
			new CollapsableRowFactoryDecorator<IdObject>(
				new HighlightVerbintenisRowFactoryDecorator<IdObject>(
					new CustomDataPanelAjaxClickableRowFactory<IdObject>(detailPanel
						.getPlaatsingOfVerbintenisModel())
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onClick(AjaxRequestTarget target, Item<IdObject> item)
						{
							target.addComponent(datapanel);
							target.addComponent(recreateBottomRow());
							target.addComponent(detailPanel);
						}

						@Override
						protected boolean isSelected(CustomDataPanel<IdObject> panel,
								Item<IdObject> item, IModel<IdObject> itemModel)
						{
							return selectedObject != null
								&& JavaUtil.equalsOrBothNull(selectedObject.getObject(), itemModel
									.getObject());
						}
					}));

		datapanel =
			new EduArteDataPanel<IdObject>("datapanel", dataprovider,
				new VerbintenisMetPlaatsingenTable(rowFactory));
		datapanel.setGroeperenButtonVisible(false);
		datapanel.setRowFactory(rowFactory);

		add(datapanel);
		add(detailPanel);

		createComponents();
	}

	private void updateVerbintenis(Verbintenis verbintenis)
	{
		IModel<Verbintenis> model = detailPanel.getModelAsVerbintenis();
		if (verbintenis != null && verbintenis.getStatus() == VerbintenisStatus.Intake)
			model.setObject(null);
		else
			model.setObject(verbintenis);
	}
}
