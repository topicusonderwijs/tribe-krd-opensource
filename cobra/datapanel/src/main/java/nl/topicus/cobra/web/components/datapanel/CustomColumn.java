/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * @author marrink
 */
public interface CustomColumn<T> extends IColumn<T>
{
	public enum Positioning
	{
		FIXED_LEFT,
		FIXED_RIGHT,
		CUSTOMIZABLE
	}

	/**
	 * ID is geen Wicket {@link Component} id maar het id waaraan deze kolom is te
	 * herkennen voor de settings.
	 * 
	 * @return id
	 */
	public String getId();

	/**
	 * Is deze kolom standaard zichtbaar. Handig voor als de gebruiker nog geen custom
	 * view heeft gedefinieerd.
	 * 
	 * @return true als deze kolom standaard zichtbaar zou moeten zijn, anders false.
	 */
	public boolean isDefaultVisible();

	/**
	 * @return return standaard true, als deze methode false teruggeeft wordt de kolom
	 *         nergens teruggegeven
	 */
	public boolean isVisible();

	/**
	 * Check functie voor de inhoud van deze Column. Bedoelt voor o.a. buttons en links
	 * welke niet getoond mogen worden op basis van de waarde van het model.
	 * 
	 * @param rowModel
	 * @return default {@link #isVisible()}.
	 */
	public boolean isContentsVisible(IModel<T> rowModel);

	/**
	 * Check functie voor de inhoud van deze Column. Bedoelt voor o.a. checkboxes en
	 * selects welke disabled moeten zijn op basis van de waarde van het model.
	 * 
	 * @param rowModel
	 * @return default {@link #isVisible()}.
	 */
	public boolean isContentsEnabled(IModel<T> rowModel);

	/**
	 * @return true als de waarde van deze kolom afgedrukt moet worden ook al is de waarde
	 *         gelijk aan de waarde van de vorige regel. Als deze methdoe false geeft,
	 *         worden waarden niet op elke regel herhaald (als het object achter de twee
	 *         regels ook hetzelfde zijn).
	 */
	public boolean isRepeatWhenEqualToPrevRow();

	/**
	 * Geeft aan waar de kolom geplaatst moet worden. Dit kan vast links of rechts zijn,
	 * waarbij de gebruiker de kolom niet kan verwijderen (bijvoorbeeld voor checkboxes of
	 * knoppen). Standaard is een kolom customizable, waarbij de gebruiker zelf kan
	 * aangeven of hij wel of niet weergegeven moet worden, en waar.
	 */
	public Positioning getPositioning();

	public void populateItem(WebMarkupContainer cell, final String componentId,
			WebMarkupContainer row, final IModel<T> rowModel, int span);

	public Component getHeader(Component cell, String componentId);
}
