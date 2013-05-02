package nl.topicus.eduarte.app.sidebar.datastores;

import java.io.Serializable;

import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IDetachable;

/**
 * Sidebars hebben een datastore in de sessie nodig omdat deze over meerdere paginas heen
 * state moeten vasthouden. Subclasses moeten een argumentloze constructor hebben.
 * 
 * @author loite
 */
public abstract class AbstractSideBarDataStore implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	public AbstractSideBarDataStore()
	{
	}

	protected boolean isActief()
	{
		return false;
	}

	@SuppressWarnings("unused")
	public void onBeforeRender(SecurePage page)
	{

	}
}
