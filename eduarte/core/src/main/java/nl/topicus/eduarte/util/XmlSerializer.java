package nl.topicus.eduarte.util;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.hibernate.proxy.HibernateProxy;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

public final class XmlSerializer
{
	/**
	 * Hide constructor
	 */
	private XmlSerializer()
	{
	}

	/**
	 * Serialiseert de gegeven waarden naar een lijst met xml-documenten.
	 * 
	 * @param values
	 *            De waarden die geserialiseerd moeten worden
	 * @return de waarden in xml-geserialiseerde vorm
	 */
	public static List<String> serializeValues(List< ? > values)
	{
		List<String> res = new ArrayList<String>(values.size());
		// Create XML encoder.
		XStream stream = new XStream();
		stream.omitField(AbstractOrganisatieEenheidLocatieZoekFilter.class, "authorizationContext");
		stream.omitField(HibernateModel.class, "objectAccess");
		stream.omitField(DefaultModelManager.class, "objectAccess");
		for (Object value : values)
		{
			if (value instanceof HibernateProxy)
			{
				value = ((Entiteit) value).doUnproxy();
			}
			if (value instanceof IDetachable)
			{
				((IDetachable) value).detach();
			}
			res.add(stream.toXML(value));
		}
		return res;
	}

	/**
	 * Vertaalt een array van strings naar een array van objecten door elke string te
	 * deserialiseren.
	 * 
	 * @param serializedValues
	 *            De geserialiseerde waarden
	 * @param destinationTypes
	 *            De constructor parameter types
	 * @return De gedeserialiseerde objecten
	 * @throws XStreamException
	 */
	public static Object[] deserializeValues(String[] serializedValues,
			Class< ? >[] destinationTypes) throws XStreamException
	{
		if (serializedValues.length != destinationTypes.length)
		{
			throw new IllegalArgumentException("serializedValues.length != destinationTypes.length");
		}
		XStream stream = new XStream();
		Object[] values = new Object[serializedValues.length];
		int index = 0;
		for (String serializedValue : serializedValues)
		{
			Object value = serializedValue == null ? null : stream.fromXML(serializedValue);
			if (value instanceof IModel< ? >
				&& !IModel.class.isAssignableFrom(destinationTypes[index]))
			{
				// Waarde is blijkbaar opgeslagen als een model (zodat dit gedetached kon
				// worden), maar de constructor verwacht het uiteindelijke object.
				value = ((IModel< ? >) value).getObject();
			}
			values[index] = value;
			index++;
		}
		return values;
	}

}
