package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductEigenaar;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Pagina over een onderwijsproduct met de juridisch eigenaar en gebruiksrecht
 * 
 * @author loite
 */
@PageInfo(title = "Onderwijsproduct eigenaar", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Algemeen > Eigenaar"})
@InPrincipal(OnderwijsproductEigenaar.class)
public class OnderwijsproductEigenaarPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductEigenaarPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Eigenaar, ModelFactory.getCompoundModel(onderwijsproduct));
		add(ComponentFactory.getDataLabel("juridischEigenaar"));
		add(ComponentFactory.getDataLabel("gebruiksrecht"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Onderwijsproduct.class, getSelectedMenuItem(),
			OnderwijsproductEigenaarPage.this, getContextOnderwijsproductModel()));
	}
}
