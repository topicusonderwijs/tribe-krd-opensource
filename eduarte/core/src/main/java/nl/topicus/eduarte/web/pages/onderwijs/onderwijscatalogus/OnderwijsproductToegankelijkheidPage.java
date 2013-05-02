package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductToegankelijkheid;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.ContBoxAfdrukLinkPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Pagina over een onderwijsproduct met de toegankelijkheid
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct toegankelijkheid", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Toegankelijkheid"})
@InPrincipal(OnderwijsproductToegankelijkheid.class)
public class OnderwijsproductToegankelijkheidPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param onderwijsproduct
	 */
	public OnderwijsproductToegankelijkheidPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Toegankelijkheid, ModelFactory
			.getCompoundModel(onderwijsproduct));
		add(new ContBoxAfdrukLinkPanel("afdrukLinkPanel", "Onderwijsproduct toegankelijkheid"));
		add(ComponentFactory.getDataLabel("toegankelijkheid"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Onderwijsproduct.class, getSelectedMenuItem(),
			OnderwijsproductToegankelijkheidPage.this, getContextOnderwijsproductModel()));
	}
}
