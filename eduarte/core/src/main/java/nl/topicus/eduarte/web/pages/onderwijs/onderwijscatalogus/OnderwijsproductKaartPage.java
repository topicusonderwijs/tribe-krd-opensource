package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductInzien;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAanbod;
import nl.topicus.eduarte.entities.vrijevelden.OnderwijsproductVrijVeld;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.ContBoxAfdrukLinkPanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.criteria.BPVCriteriaOnderwijsproductOverzichtPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.PropertyModel;

/**
 * Algemene pagina over een onderwijsproduct.
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproductkaart", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct]"})
@InPrincipal(OnderwijsproductInzien.class)
public class OnderwijsproductKaartPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductKaartPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Algemeen, ModelFactory.getCompoundModel(onderwijsproduct));
		add(new ContBoxAfdrukLinkPanel("afdrukLinkPanel", "Onderwijsproductkaart"));
		add(ComponentFactory.getDataLabel("code"));
		add(ComponentFactory.getDataLabel("titel"));
		add(ComponentFactory.getDataLabel("omschrijving"));
		add(ComponentFactory.getDataLabel("zoektermenAlsString"));
		add(ComponentFactory.getDataLabel("soortProduct.naam"));
		add(ComponentFactory.getDataLabel("heeftWerkstuktitelOmschrijving"));
		add(ComponentFactory.getDataLabel("alleenExternOmschrijving"));
		add(ComponentFactory.getDataLabel("status"));
		add(ComponentFactory.getDataLabel("begindatum"));
		add(ComponentFactory.getDataLabel("einddatum"));
		boolean ho = EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS);
		WebMarkupContainer wmc = new WebMarkupContainer("creditsContainer");
		wmc.add(ComponentFactory.getDataLabel("credits"));
		add(wmc.setVisible(ho));
		add(ComponentFactory.getDataLabel("omvang").setVisible(!ho));
		add(ComponentFactory.getDataLabel("belasting").setVisible(!ho));
		add(ComponentFactory.getDataLabel("aggregatieniveau"));
		add(ComponentFactory.getDataLabel("getStartonderwijsproductOmschrijving"));
		add(ComponentFactory.getDataLabel("leerstijl.naam").setVisible(!ho));
		add(ComponentFactory.getDataLabel("minimumAantalDeelnemers"));
		add(ComponentFactory.getDataLabel("maximumAantalDeelnemers"));
		add(ComponentFactory.getDataLabel("soortPraktijklokaal.naam"));
		add(ComponentFactory.getDataLabel("typeToets.naam"));
		add(ComponentFactory.getDataLabel("typeLocatie.naam"));
		add(ComponentFactory.getDataLabel("kostprijs"));
		add(ComponentFactory.getDataLabel("getBijIntakeOmschrijving"));

		VrijVeldEntiteitPanel<OnderwijsproductVrijVeld, Onderwijsproduct> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<OnderwijsproductVrijVeld, Onderwijsproduct>("vrijvelden",
				getContextOnderwijsproductModel());
		add(vrijVeldenPanel);
		vrijVeldenPanel.setDossierScherm(true);

		CollectionDataProvider<OnderwijsproductAanbod> aanbodProvider =
			new CollectionDataProvider<OnderwijsproductAanbod>(
				new PropertyModel<List<OnderwijsproductAanbod>>(getDefaultModel(),
					"onderwijsproductAanbodList"));
		CustomDataPanel<OnderwijsproductAanbod> aanbod =
			new EduArteDataPanel<OnderwijsproductAanbod>("aanbod", aanbodProvider,
				new OrganisatieLocatieTable<OnderwijsproductAanbod>(false).setTitle("Aanbod"));

		BPVCriteriaOnderwijsproductOverzichtPanel bpvCriteriaOnderwijsproductOverzichtPanel =
			new BPVCriteriaOnderwijsproductOverzichtPanel("bpvCriteriaOnderwijsproduct",
				new PropertyModel<List<BPVCriteriaOnderwijsproduct>>(getDefaultModel(),
					"bpvCriteria"));

		add(bpvCriteriaOnderwijsproductOverzichtPanel);

		if (!(EduArteApp.get().isModuleActive(EduArteModuleKey.BPV_HOGERONDERWIJS) && onderwijsproduct
			.getSoortProduct().isStage()))
		{
			bpvCriteriaOnderwijsproductOverzichtPanel.setVisible(false);
		}
		add(aanbod);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Onderwijsproduct.class, getSelectedMenuItem(),
			OnderwijsproductKaartPage.this, getContextOnderwijsproductModel()));
	}
}
