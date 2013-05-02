package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

/**
 * @author idserda
 */
@PageInfo(title = "Status van verbintenissen bewerken", menu = {"Deelnemer > Verbintenissen"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class, Verantwoordelijk.class,
	Uitvoerend.class, Begeleider.class, Docent.class})
public abstract class AbstractCollectieveStatusovergangPage<T extends Enum<T>> extends SecurePage
{

	public AbstractCollectieveStatusovergangPage(IModel< ? > model)
	{
		super(model, CoreMainMenuItem.Deelnemer);
	}

	protected T getEindstatus()
	{
		return getCollectieveStatusovergangEditModel().getEindstatus();
	}

	protected T getBeginstatus()
	{
		return getCollectieveStatusovergangEditModel().getBeginstatus();
	}

	@SuppressWarnings("unchecked")
	protected CollectieveStatusovergangEditModel<T> getCollectieveStatusovergangEditModel()
	{
		return (CollectieveStatusovergangEditModel<T>) getDefaultModel();
	}

	protected Taxonomie getTaxonomie()
	{
		return getCollectieveStatusovergangEditModel().getTaxonomie();
	}

	@Override
	public abstract AbstractMenuBar createMenu(String id);

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

}