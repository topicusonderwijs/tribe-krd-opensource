package nl.topicus.eduarte.krd.bron.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.*;
import nl.topicus.onderwijs.duo.bron.annotation.Record;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.ParsedValue;
import nl.topicus.onderwijs.duo.bron.terugkoppeling.Recording;

public class BronEntiteitRecording implements Recording<Object>
{
	private final Class< ? > expectedClass;

	private final Map<String, ParsedValue< ? >> values = new HashMap<String, ParsedValue< ? >>();

	public BronEntiteitRecording(Class< ? > expectedClass)
	{
		this.expectedClass = expectedClass;
	}

	@Override
	public void addProperty(String fieldName, ParsedValue< ? > value)
	{
		values.put(fieldName, value);
	}

	@Override
	public Object create()
	{
		int type = expectedClass.getAnnotation(Record.class).type();
		if (type == 200)
		{
			return transferPropertyValues(new BronVoTerugkoppelbestand());
		}
		if (type == 201)
		{
			return transferPropertyValues(new BronVoBatchgegevens());
		}
		if (type == 210)
		{
			return transferPropertyValues(new BronVoInschrijvingTerugkoppelMelding());
		}
		if (type == 211)
		{
			return transferPropertyValues(new BronVoSignaal());
		}
		if (type == 220)
		{
			return transferPropertyValues(new BronVoExamenTerugkoppelMelding());
		}
		if (type == 221)
		{
			return transferPropertyValues(new BronVoVakTerugkoppelMelding());
		}
		if (type == 400)
		{
			return transferPropertyValues(new BronBveTerugkoppelbestand());
		}
		if (type == 402)
		{
			return transferPropertyValues(new BronBveBatchgegevens());
		}
		if (type == 405)
		{
			return transferPropertyValues(new BronBveTerugkoppelMelding());
		}
		Class< ? >[] recordtypes = BronBveTerugkoppelRecord.class.getInterfaces();
		for (Class< ? > recordtype : recordtypes)
		{
			if (recordtype.isAnnotationPresent(Record.class)
				&& recordtype.getAnnotation(Record.class).type() == type)
			{
				values.put("recordType", new ParsedValue<Integer>(type));
				BronBveTerugkoppelRecord record = new BronBveTerugkoppelRecord();
				return transferPropertyValues(record);
			}
		}
		throw new IllegalStateException("Type " + type + " kon niet verwerkt worden");
	}

	private Object transferPropertyValues(Object record)
	{
		String recordClass = record.getClass().getSimpleName();
		Set<Entry<String, ParsedValue< ? >>> entrySet = values.entrySet();
		for (Entry<String, ParsedValue< ? >> entry : entrySet)
		{
			Property< ? , ? , ? > property =
				ReflectionUtil.findProperty(record.getClass(), entry.getKey());
			Asserts.assertNotNull(recordClass + "." + entry.getKey(), property);
			property.setValue(record, entry.getValue().value);
		}
		return record;
	}
}
