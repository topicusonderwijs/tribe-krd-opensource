package nl.topicus.eduarte.web.pages;

import java.io.Serializable;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;

public interface PageContext extends IDetachable, Serializable
{
	public MainMenuItem getCurrentMainMenuItem();

	public Component getTitle(String id);

	public AbstractMenuBar getMenu(String id);

}
