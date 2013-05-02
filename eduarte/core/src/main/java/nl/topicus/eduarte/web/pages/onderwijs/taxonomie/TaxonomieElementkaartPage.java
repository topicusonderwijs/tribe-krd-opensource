package nl.topicus.eduarte.web.pages.onderwijs.taxonomie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.taxonomie.TaxonomieElementRead;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Kaart van een taxonomieelement.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie-elementkaart", menu = {"Onderwijs > Taxonomie > [taxonomie-element]"})
@InPrincipal(TaxonomieElementRead.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class TaxonomieElementkaartPage extends AbstractTaxonomieElementPage
{
	private static final long serialVersionUID = 1L;

	public TaxonomieElementkaartPage(TaxonomieElement taxonomieElement)
	{
		super(TaxonomieElementMenuItem.Algemeen, ModelFactory.getCompoundModel(taxonomieElement));
		add(ComponentFactory.getDataLabel("afkorting"));
		add(ComponentFactory.getDataLabel("naam"));
		add(ComponentFactory.getDataLabel("taxonomiecode"));
		add(ComponentFactory.getDataLabel("externeCode"));
		add(ComponentFactory.getDataLabel("niveauNaam"));
		add(ComponentFactory.getDataLabel("taxonomie.naam"));
		add(ComponentFactory.getDataLabel("taxonomieElementType.naam"));
		add(ComponentFactory.getDataLabel("taxonomie.landelijkOmschrijving"));
		add(ComponentFactory.getDataLabel("begindatum"));
		add(ComponentFactory.getDataLabel("einddatum"));
		add(ComponentFactory.getDataLabel("brinKenniscentrum"));
		add(ComponentFactory.getDataLabel("naamKenniscentrum"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		ModuleEditPageButton<TaxonomieElement> bewerken =
			new ModuleEditPageButton<TaxonomieElement>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				TaxonomieElement.class, getSelectedMenuItem(), TaxonomieElementkaartPage.this,
				getContextTaxonomieElementModel());
		bewerken.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
		panel.addButton(bewerken);
	}

}
