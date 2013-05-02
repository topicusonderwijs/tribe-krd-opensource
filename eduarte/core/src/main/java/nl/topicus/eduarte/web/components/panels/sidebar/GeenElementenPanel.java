package nl.topicus.eduarte.web.components.panels.sidebar;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panel dat een specifiek bericht toont als er geen recente deelnemers zijn. Dit panel
 * kan direct in een table tag opgenomen worden, of in een tbody tag. Deze zal alleen een
 * tr met daarin een td opnemen met de opgegeven colspan.
 */
public class GeenElementenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            component identifier
	 * @param bericht
	 *            het bericht dat getoond moet worden (bijv. "Geen deelnemers gevonden")
	 * @param colspan
	 *            het aantal kolommen dat dit panel moet overspannen in de tabel.
	 */
	public GeenElementenPanel(String id, String bericht, int colspan)
	{
		super(id);

		Label label = new Label("colspan", bericht);
		label.add(new SimpleAttributeModifier("colspan", "" + colspan));
		add(label);
	}
}
