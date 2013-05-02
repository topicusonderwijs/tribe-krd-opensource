package nl.topicus.eduarte.krd.bron;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.topicus.cobra.entities.IdObject;

/**
 * Houdt een waarde wijziging van een property uit de watchlist van een <tt>Entiteit</tt>
 * vast die door de <tt>BronHibernateInterceptor</tt> afgevangen is.
 * 
 * @author dashorst
 */
public class BronStateChange implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final IdObject entity;

	private final String propertyName;

	private final Object previousValue;

	private final Object currentValue;

	private final boolean sleutelWaarde;

	public BronStateChange(IdObject entity, String propertyName, Object previousValue,
			Object currentValue)
	{
		this.entity = entity;
		this.propertyName = propertyName;
		this.previousValue = previousValue;
		this.currentValue = currentValue;
		this.sleutelWaarde = BronWatchList.isSleutelWaarde(entity, propertyName);
	}

	public boolean isSleutelwaardeWijziging()
	{
		return sleutelWaarde;
	}

	public IdObject getEntity()
	{
		return entity;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getPreviousValue()
	{
		return (T) previousValue;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCurrentValue()
	{
		return (T) currentValue;
	}

	@Override
	public String toString()
	{
		return entity.getClass().getSimpleName() + "." + propertyName + ": "
			+ toString(previousValue) + " -> " + toString(currentValue);
	}

	/**
	 * Formatteert de waarde naar een leesbare string. Als <tt>value</tt> een datum is,
	 * wordt deze als <tt>yyyy-MM-dd</tt> geformatteerd.
	 */
	private String toString(Object value)
	{
		if (value instanceof Date)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format((Date) value);
		}
		if (value == null)
		{
			return "";
		}
		return String.valueOf(value);
	}
}
