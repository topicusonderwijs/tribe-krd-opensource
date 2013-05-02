package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsproductKalender;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.components.panels.ContBoxAfdrukLinkPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * Pagina over een onderwijsproduct met de kalender
 * 
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproduct Kalender", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Kalender"})
@InPrincipal(OnderwijsproductKalender.class)
public class OnderwijsproductKalenderPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductKalenderPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.Kalender, ModelFactory.getCompoundModel(onderwijsproduct));
		add(new ContBoxAfdrukLinkPanel("afdrukLinkPanel", "Onderwijsproduct kalender"));
		add(ComponentFactory.getDataLabel("kalender"));
		add(ComponentFactory.getDataLabel("uitvoeringsfrequentie"));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<Onderwijsproduct>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Onderwijsproduct.class, getSelectedMenuItem(),
			OnderwijsproductKalenderPage.this, getContextOnderwijsproductModel()));
	}
}
