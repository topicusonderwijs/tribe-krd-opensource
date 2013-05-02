/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.bron;

import java.util.HashMap;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.Hibernate;

/**
 * Een white-list implementatie van alle properties en entiteiten die ten behoeve van de
 * BRON-communicatie gemonitord moeten worden.
 * 
 * @author dashorst
 */
public class BronWatchList
{
	private static final long serialVersionUID = 1L;

	private static HashMap<Class< ? extends Entiteit>, HashMap<String, Property>> list =
		new HashMap<Class< ? extends Entiteit>, HashMap<String, Property>>();

	static class Property
	{
		private final Class< ? extends Entiteit> entiteit;

		private final String expressie;

		private final boolean sleutelGegeven;

		public Property(Class< ? extends Entiteit> clazz, String expressie, boolean sleutelGegeven)
		{
			this.entiteit = clazz;
			this.expressie = expressie;
			this.sleutelGegeven = sleutelGegeven;
		}

		public Class< ? extends Entiteit> getEntiteit()
		{
			return entiteit;
		}

		public String getExpressie()
		{
			return expressie;
		}

		public boolean isSleutelGegeven()
		{
			return sleutelGegeven;
		}
	}

	static
	{
		addBronProperties(Deelnemer.class);
		addBronProperties(Persoon.class);
		addBronProperties(PersoonAdres.class);
		addBronProperties(Adres.class);
		addBronProperties(Verbintenis.class);
		addBronProperties(Plaatsing.class);
		addBronProperties(Bekostigingsperiode.class);
		addBronProperties(BPVInschrijving.class);
		addBronProperties(BPVBedrijfsgegeven.class);
		addBronProperties(Examendeelname.class);
		addBronProperties(OnderwijsproductAfname.class);
		addBronProperties(OnderwijsproductAfnameContext.class);
		addBronProperties(Resultaat.class);
		addBronProperties(Vooropleiding.class);
	}

	static <T extends Entiteit> void addBronProperties(Class<T> clazz)
	{
		List<nl.topicus.cobra.reflection.Property<T, ? , ? >> properties =
			ReflectionUtil.findProperties(clazz);
		for (nl.topicus.cobra.reflection.Property<T, ? , ? > property : properties)
		{
			if (property.isAnnotationPresent(Bron.class))
			{
				Bron bron = property.getAnnotation(Bron.class);
				put(clazz, property.getName(), bron.sleutel());
			}
		}
	}

	static HashMap<Class< ? extends Entiteit>, HashMap<String, Property>> getList()
	{
		return list;
	}

	private static void put(Class< ? extends Entiteit> entiteit, String expression, boolean sleutel)
	{
		HashMap<String, Property> properties = getEntiteitProperties(entiteit);
		properties.put(expression, new Property(entiteit, expression, sleutel));
	}

	private static HashMap<String, Property> getEntiteitProperties(
			Class< ? extends Entiteit> entiteit)
	{
		HashMap<String, Property> properties = list.get(entiteit);
		if (properties == null)
		{
			properties = new HashMap<String, Property>();
			list.put(entiteit, properties);
		}
		return properties;
	}

	/**
	 * @return <code>true</code> wanneer de entiteit in de watchlist voorkomt.
	 */
	public boolean isWatched(Entiteit entity)
	{
		return list.containsKey(Hibernate.getClass(entity));
	}

	/**
	 * @return <code>true</code> wanneer de entiteit in de watchlist voorkomt en de
	 *         property een gemonitorde property is.
	 */
	public boolean isWatched(Entiteit entiteit, String property)
	{
		Class< ? > key = Hibernate.getClass(entiteit);
		HashMap<String, Property> properties = list.get(key);

		if (properties != null)
		{
			boolean isInWatchList = properties.containsKey(property);
			return isInWatchList;
		}
		return false;
	}

	public static boolean isSleutelWaarde(Entiteit entiteit, String expression)
	{
		Class< ? > key = Hibernate.getClass(entiteit);
		HashMap<String, Property> properties = list.get(key);
		if (properties != null && properties.containsKey(expression))
		{
			Property property = properties.get(expression);
			return property.isSleutelGegeven();
		}
		return false;
	}

	public static boolean isSleutelWaarde(IdObject entity, String propertyName)
	{
		if (entity instanceof Entiteit)
		{
			return isSleutelWaarde((Entiteit) entity, propertyName);
		}
		return false;
	}

}
