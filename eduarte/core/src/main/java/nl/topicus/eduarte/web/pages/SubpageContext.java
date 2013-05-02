package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;

import org.apache.wicket.Component;

public class SubpageContext implements PageContext
{
	private static final long serialVersionUID = 1L;

	private SecurePage basepage;

	public SubpageContext(SecurePage basepage)
	{
		this.basepage = basepage;
	}

	@Override
	public MainMenuItem getCurrentMainMenuItem()
	{
		return basepage.getSelectedItem();
	}

	@Override
	public AbstractMenuBar getMenu(String id)
	{
		return basepage.createMenu(id);
	}

	@Override
	public Component getTitle(String id)
	{
		return basepage.createTitle(id);
	}

	@Override
	public void detach()
	{
		basepage.detach();
	}
}
