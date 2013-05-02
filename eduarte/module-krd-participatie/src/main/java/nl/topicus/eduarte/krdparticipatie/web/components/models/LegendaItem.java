package nl.topicus.eduarte.krdparticipatie.web.components.models;

public class LegendaItem
{
	private String omschrijving;

	private String color;

	public LegendaItem(String omschrijving, String color)
	{
		this.omschrijving = omschrijving;
		this.color = color;
	}

	/**
	 * @return Returns the omschrijving.
	 */
	public String getOmschrijving()
	{
		return omschrijving;
	}

	/**
	 * @param omschrijving
	 *            The omschrijving to set.
	 */
	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	/**
	 * @return Returns the color.
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(String color)
	{
		this.color = color;
	}
}
