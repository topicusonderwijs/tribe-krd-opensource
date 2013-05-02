package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductPersoneel;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.ContBoxAfdrukLinkPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Pagina over een onderwijsproduct met de vereisten voor het personeel
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct personeel", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Personeel"})
@InPrincipal(OnderwijsproductPersoneel.class)
public class OnderwijsproductPersoneelPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param onderwijsproduct
	 */
	public OnderwijsproductPersoneelPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Personeel, ModelFactory.getCompoundModel(onderwijsproduct));
		add(new ContBoxAfdrukLinkPanel("afdrukLinkPanel", "Onderwijsproduct personeel"));
		add(ComponentFactory.getDataLabel("personeelCompetenties"));
		add(ComponentFactory.getDataLabel("personeelKennisgebiedEnNiveau"));
		add(ComponentFactory.getDataLabel("personeelWettelijkeVereisten"));
		add(ComponentFactory.getDataLabel("personeelBevoegdheid"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Onderwijsproduct.class, getSelectedMenuItem(),
			OnderwijsproductPersoneelPage.this, getContextOnderwijsproductModel()));
	}
}
