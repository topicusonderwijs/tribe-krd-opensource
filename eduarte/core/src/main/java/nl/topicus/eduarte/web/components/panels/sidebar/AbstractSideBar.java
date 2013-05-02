package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Sidebar panels kunnen getoond worden in de sidebar van de applicatie, waarin shortcuts
 * of andere handige methodes en links getoond kunnen worden.
 * 
 * @author loite
 */
public abstract class AbstractSideBar extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param page
	 *            De pagina waarvoor de sidebar wordt gemaakt.
	 */
	public AbstractSideBar(String id, SecurePage page)
	{
		super(id);
		// Vraag de datastore op. Dit zorgt ervoor dat de datastore altijd aangemaakt is.
		getDataStore();
	}

	/**
	 * @return De class van de datastore van deze sidebar
	 */
	protected abstract Class< ? extends AbstractSideBarDataStore> getDataStoreClass();

	/**
	 * @return De datastore van deze sidebar
	 */
	protected AbstractSideBarDataStore getDataStore()
	{
		Class< ? extends AbstractSideBarDataStore> clazz = getDataStoreClass();
		if (clazz != null)
			return EduArteSession.get().getSideBarDataStore(clazz);
		return null;
	}

	/**
	 * @see org.apache.wicket.Component#detachModels()
	 */
	@Override
	public void detachModels()
	{
		super.detachModels();
		AbstractSideBarDataStore datastore = getDataStore();
		if (datastore != null)
		{
			datastore.detach();
		}
	}

}
