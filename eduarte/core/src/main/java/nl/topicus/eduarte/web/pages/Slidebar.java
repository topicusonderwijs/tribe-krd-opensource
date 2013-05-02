package nl.topicus.eduarte.web.pages;

import nl.topicus.eduarte.web.components.panels.sidebar.BookmarksSideBar;
import nl.topicus.eduarte.web.components.panels.sidebar.ToDoSideBar;
import nl.topicus.eduarte.web.components.panels.sidebar.deelnemer.RecenteDeelnemersSideBar;
import nl.topicus.eduarte.web.components.panels.sidebar.rapportage.RapportageSideBar;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class Slidebar extends Panel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	public Slidebar(String id, SecurePage page)
	{
		super(id);
		add(new RecenteDeelnemersSideBar("recenteDeelnemers", page));
		add(new BookmarksSideBar("bookmarks", page));
		add(new ToDoSideBar("todos", page));
		add(new RapportageSideBar(SecurePage.ID_RAPPORTAGESIDEBAR, page).setOutputMarkupId(true));
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		wiQueryResourceManager.addJavaScriptResource(new JavascriptResourceReference(ContBox.class,
			"jquery-slidebar.js"));
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("slidebar");
	}
}
