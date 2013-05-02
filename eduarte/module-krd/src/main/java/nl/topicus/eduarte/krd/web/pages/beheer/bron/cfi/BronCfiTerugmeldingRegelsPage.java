package nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.dao.helpers.BronCfiTerugmeldingRegelDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiRegelType;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmeldingRegel;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronCfiTerugmeldingRegelTable;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.zoekfilters.BronCfiTerugmeldingRegelZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "BRON CFI-terugmeldingregels", menu = "Deelnemer > BRON > CFI-terugmeldingen [terugmelding] -> details")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronCfiTerugmeldingRegelsPage extends AbstractBronPage
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingRegelsPage(BronCfiTerugmelding terugmelding,
			BronCfiRegelType regelType)
	{
		setDefaultModel(ModelFactory.getCompoundModel(terugmelding));
		BronCfiTerugmeldingRegelZoekFilter filter =
			new BronCfiTerugmeldingRegelZoekFilter(terugmelding, regelType);
		GeneralFilteredSortableDataProvider<BronCfiTerugmeldingRegel, BronCfiTerugmeldingRegelZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				BronCfiTerugmeldingRegelDataAccessHelper.class);
		EduArteDataPanel<BronCfiTerugmeldingRegel> datapanel =
			new EduArteDataPanel<BronCfiTerugmeldingRegel>("datapanel", provider,
				new BronCfiTerugmeldingRegelTable(regelType));
		datapanel.setItemsPerPage(20);
		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				BronCfiTerugmelding terugmelding =
					(BronCfiTerugmelding) BronCfiTerugmeldingRegelsPage.this
						.getDefaultModelObject();
				return new BronCfiTerugmeldingPage(terugmelding);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronCfiTerugmeldingPage.class;
			}
		}));
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
