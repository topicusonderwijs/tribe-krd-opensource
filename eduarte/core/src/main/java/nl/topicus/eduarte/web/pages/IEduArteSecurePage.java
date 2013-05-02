package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.Component;

/**
 * Securepage in EduArte.
 * 
 * @author loite
 */
public interface IEduArteSecurePage extends FeedbackComponent
{
	/**
	 * Het menu voor deze pagina. Factory methode.
	 * 
	 * @param id
	 * @return menu, nooit null
	 */
	public AbstractMenuBar createMenu(String id);

	/**
	 * Een label of panel met daarop de context-gevoelige header van de pagina.
	 * 
	 * @param id
	 * @return label of panel, nooit null
	 */
	public Component createTitle(String id);

	/**
	 * @return Het geselecteerde mainmenuitem van deze pagina.
	 */
	public MainMenuItem getSelectedItem();
}
