package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

/**
 * Factory interface voor het aanmaken van knoppen voor nieuwe accounts
 */
public interface NieuweAccountButtonFactory extends ModuleComponentFactory
{
	public void createNieuweAccountButton(BottomRowPanel parent, SecurePage returnPage);
}