package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.dataproviders.SortableListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.labels.JaNeeLabel;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingFase;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.entities.vrijevelden.OpleidingVrijVeld;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.web.components.menu.OpleidingMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.JasperReportBottomRowButton;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingFaseTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
@PageInfo(title = "Opleidingkaart", menu = {"Onderwijs > [opleiding]"})
@InPrincipal(OpleidingInzien.class)
public class OpleidingkaartPage extends AbstractOpleidingPage
{
	private static final long serialVersionUID = 1L;

	private final SecurePage returnToPage;

	private boolean variant;

	private final class AanbodModel extends LoadableDetachableModel<List<OpleidingAanbod>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<OpleidingAanbod> load()
		{
			return getContextOpleiding().getAanbod();
		}
	}

	public OpleidingkaartPage(OpleidingProvider provider)
	{
		this(provider.getOpleiding());
	}

	public OpleidingkaartPage(Opleiding opleiding)
	{
		this(opleiding, null);
	}

	public OpleidingkaartPage(Opleiding opleiding, SecurePage returnToPage)
	{
		super(OpleidingMenuItem.Opleidingkaart, ModelFactory.getCompoundModel(opleiding));
		this.returnToPage = returnToPage;

		variant = opleiding instanceof Opleidingsvariant;
		addVariantVelden();

		add(ComponentFactory.getDataLabel("code"));
		add(ComponentFactory.getDataLabel("naam"));
		WebMarkupContainer wervingContainer = new WebMarkupContainer("wervingContainer");
		wervingContainer.setVisible(EduArteApp.get().isModuleActive(
			EduArteModuleKey.DIGITAALAANMELDEN));
		add(wervingContainer);
		wervingContainer.add(ComponentFactory.getDataLabel("wervingsnaam"));
		add(ComponentFactory.getDataLabel("leerweg"));
		add(ComponentFactory.getDataLabel("defaultIntensiteit"));
		add(ComponentFactory.getDataLabel("verbintenisgebied.taxonomiecode"));
		add(ComponentFactory.getDataLabel("verbintenisgebied.naam"));
		add(ComponentFactory.getDataLabel("verbintenisgebied.niveauNaam"));
		add(ComponentFactory.getDataLabel("verbintenisgebied.prijsfactor"));
		add(ComponentFactory.getDataLabel("begindatum"));
		add(ComponentFactory.getDataLabel("einddatum"));
		add(ComponentFactory.getDataLabel("duurInMaanden"));
		add(ComponentFactory.getDataLabel("beginLeerjaar"));
		add(ComponentFactory.getDataLabel("eindLeerjaar"));
		add(ComponentFactory.getDataLabel("datumLaatsteInschrijving"));
		add(ComponentFactory.getDataLabel("KiesKennisCenterumOmschrijving"));
		add(ComponentFactory.getDataLabel("diplomatekst1"));
		add(ComponentFactory.getDataLabel("diplomatekst2"));
		add(ComponentFactory.getDataLabel("diplomatekst3"));
		add(ComponentFactory.getDataLabel("communicerenMetBRONOmschrijving"));
		add(ComponentFactory.getDataLabel("negeerLandelijkeProductregelsOmschrijving"));
		add(ComponentFactory.getDataLabel("negeerLandelijkeCriteriaOmschrijving"));

		CollectionDataProvider<OpleidingAanbod> aanbodProvider =
			new CollectionDataProvider<OpleidingAanbod>(new AanbodModel());
		EduArteDataPanel<OpleidingAanbod> aanbod =
			new EduArteDataPanel<OpleidingAanbod>("aanbod", aanbodProvider,
				new OrganisatieLocatieTable<OpleidingAanbod>(false, true).setTitle("Aanbod"));
		add(aanbod);

		SortableListModelDataProvider<OpleidingFase> faseProvider =
			new SortableListModelDataProvider<OpleidingFase>(
				new PropertyModel<List<OpleidingFase>>(getContextOpleidingModel(), "fases"));
		EduArteDataPanel<OpleidingFase> fases =
			new EduArteDataPanel<OpleidingFase>("fases", faseProvider, new OpleidingFaseTable());
		fases.setVisible(getContextOpleiding().getVerbintenisgebied().getTaxonomie().isHO());
		add(fases);

		VrijVeldEntiteitPanel<OpleidingVrijVeld, Opleiding> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<OpleidingVrijVeld, Opleiding>("vrijvelden",
				getContextOpleidingModel());
		add(vrijVeldenPanel);
		vrijVeldenPanel.setDossierScherm(true);

		createComponents();
	}

	private void addVariantVelden()
	{
		WebMarkupContainer parentContainer = new WebMarkupContainer("parentContainer");
		TargetBasedSecurePageLink<OpleidingkaartPage> parentLink =
			new TargetBasedSecurePageLink<OpleidingkaartPage>("parentLink", new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return OpleidingkaartPage.class;
				}

				@Override
				public Page getPage()
				{
					return new OpleidingkaartPage(((Opleidingsvariant) getContextOpleiding())
						.getParent());
				}
			});
		parentLink.add(ComponentFactory.getDataLabel("parent"));
		parentContainer.add(parentLink);
		add(parentContainer);
		parentContainer.setVisible(variant);
		WebMarkupContainer instroomvariantContainer =
			new WebMarkupContainer("instroomvariantContainer");
		instroomvariantContainer.add(new JaNeeLabel("instroomvariant"));
		instroomvariantContainer.setVisible(variant);
		add(instroomvariantContainer);
		WebMarkupContainer uitstroomvariantContainer =
			new WebMarkupContainer("uitstroomvariantContainer");
		uitstroomvariantContainer.add(new JaNeeLabel("uitstroomvariant"));
		uitstroomvariantContainer.setVisible(variant);
		add(uitstroomvariantContainer);
	}

	public boolean viaOpleidingHerstellenPage()
	{
		if (returnToPage == null)
			return false;
		return returnToPage instanceof OpleidingHerstellenPage;
	}

	public SecurePage getReturnToPage()
	{
		return returnToPage;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);

		panel.addButton(new ModuleEditPageButton<Opleiding>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Opleiding.class, getSelectedMenuItem(),
			OpleidingkaartPage.this, getContextOpleidingModel()));

		panel.addButton(new JasperReportBottomRowButton<Opleiding>(panel, "opleidingkaart.jrxml",
			getClass(), "opleiding")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Opleiding> getContextModel()
			{
				return getContextOpleidingModel();
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		if (returnToPage != null)
			returnToPage.detach();
	}
}
