package nl.topicus.eduarte.web.pages.beheer.organisatie;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.tree.OrganisatieEenhedenTree;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

/**
 * Pagina waarmee de structuur van de organisatie-eenheden van een onderwijsinstelling
 * ingezien kan worden.
 * 
 * @author loite
 */
@PageInfo(title = "Organisatiestructuur", menu = "Beheer > Systeem > Organisatie")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class OrganisatieEenhedenPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenhedenPage()
	{
		super(BeheerMenuItem.Organisatieboom);
		OrganisatieEenhedenTree boom =
			new OrganisatieEenhedenTree("boom", EduArteSession.get().getPeildatumModel());
		add(boom);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<OrganisatieEenheid>(panel,
			"Organisatie-eenheid toevoegen", CobraKeyAction.TOEVOEGEN, OrganisatieEenheid.class,
			BeheerMenuItem.Organisatieboom, OrganisatieEenhedenPage.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected OrganisatieEenheid getEntity()
			{
				return new OrganisatieEenheid();
			}

		});
	}

}
